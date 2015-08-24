package org.dromelvan.tools.parser.whoscored.match;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredPlayerStatsParser extends JSoupDocumentParser<MatchParserObject> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredPlayerStatsParser.class);

	@Inject
	public WhoScoredPlayerStatsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

    @Override
    public Set<MatchParserObject> parse() {
        return parseJavaScript();
        //return parseRegExp();
    }

    public Set<MatchParserObject> parseJavaScript() {
        WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

        Pattern initialDataPattern = Pattern.compile(".*(var initialData.*;).*var matchStats.*", Pattern.DOTALL);
        for(Element scriptElement : getDocument().getElementsByTag("script")) {
            Matcher initialDataPatternMatcher = initialDataPattern.matcher(scriptElement.toString());
            if(initialDataPatternMatcher.matches()) {
                try {
                    scriptEngine.eval(initialDataPatternMatcher.group(1));
                    Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
                    Map initialDataMap = (Map)bindings.get("initialData");
                    initialDataMap = (Map)initialDataMap.get(0);

                    Map matchInfoMap = (Map)initialDataMap.get(0);
                    // Ex: [16, 168, Sunderland, Norwich, 08/15/2015 15:00:00, 08/15/2015 00:00:00, 6, FT, 0 : 2, 1 : 3, 1 : 3]
                    List<Object> matchInfo = (List<Object>)matchInfoMap.values();
                    matchParserObject.setHomeTeam(new WhoScoredTeamParserObject((String)matchInfo.get(2), (Integer)matchInfo.get(0)));
                    matchParserObject.setAwayTeam(new WhoScoredTeamParserObject((String)matchInfo.get(3), (Integer)matchInfo.get(1)));
                    matchParserObject.setDateTime((String)matchInfo.get(4));
                    matchParserObject.setTimeElapsed((String)matchInfo.get(7));

                    for(Map teamMap : ((Map<Object, Map>)initialDataMap.get(1)).values()) {
                        List<Object> teamMapValues = (List<Object>)teamMap.values();
                        TeamParserObject teamParserObject = matchParserObject.getTeam((String)teamMapValues.get(1));

                        Map playersStatsMap = (Map)teamMapValues.get(4);
                        for(Map playerStatsMap : (Collection<Map>)playersStatsMap.values()) {
                            WhoScoredPlayerStatsMap whoScoredPlayerStatsMap = new WhoScoredPlayerStatsMap(playerStatsMap);
                            WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(whoScoredPlayerStatsMap);
                            teamParserObject.getPlayers().add(playerParserObject);
                        }
                    }
                } catch(ScriptException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        getParserProperties().map(matchParserObject);

        Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
        matchParserObjects.add(matchParserObject);
        return matchParserObjects;
    }

    public Set<MatchParserObject> parseRegExp() {
        WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

        Elements scriptElements = getDocument().getElementsByTag("script");
        Pattern matchIdPattern = Pattern.compile(".*parameters: \\{ id: (\\d*) \\}.*", Pattern.DOTALL);
        Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], \\d*\\] ;(.*)", Pattern.DOTALL);
        Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'([\\w ]*)','([\\w ]*)','(.*)','.*',\\d*?,'(.*?)',.*", Pattern.DOTALL);
        Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[.*", Pattern.DOTALL);

        SortedSet<String> keySet = new TreeSet<String>();

        for (Element scriptElement : scriptElements) {
            for (DataNode node : scriptElement.dataNodes()) {
                Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
                if (matchIdMatcher.matches()) {
                    matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
                }

                Matcher scriptMatcher = scriptPattern.matcher(node.toString());
                if (scriptMatcher.matches()) {

                    String[] scriptVariables = scriptMatcher.group(3).replace("]]]],[[", "]]]],[\\\n,[").split("\n,");

                    TeamParserObject teamParserObject = null;

                    for (String scriptVariable : scriptVariables) {
                        scriptVariable = scriptVariable.trim();
                        Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
                        if (fixtureMatcher.matches()) {
                            matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
                            matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

                            matchParserObject.setDateTime(fixtureMatcher.group(5));
                            matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
                            continue;
                        } else {
                            Matcher teamMatcher = teamPattern.matcher(scriptVariable);
                            if (teamMatcher.matches()) {
                                teamParserObject = (teamParserObject == null ? matchParserObject.getHomeTeam() : matchParserObject.getAwayTeam());
                            }
                            WhoScoredPlayerStatsMap map = new WhoScoredPlayerStatsMap(scriptVariable);
                            if (map.isValid()) {
                                WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(map);
                                teamParserObject.getPlayers().add(playerParserObject);
                            } else {
                                logger.debug("Script variable {} did not produce a valid stats map.", scriptVariable);
                            }
                            keySet.addAll(map.keySet());
                        }
                    }
                }
            }
        }

        getParserProperties().map(matchParserObject);

        Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
        matchParserObjects.add(matchParserObject);
        return matchParserObjects;
    }

	public Set<MatchParserObject> parseRegExpOld() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern matchIdPattern = Pattern.compile(".*parameters: \\{ id: (\\d*) \\}.*", Pattern.DOTALL);
		Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], \\d*\\] ;(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'([\\w ]*)','([\\w ]*)','(.*)','.*',\\d*?,'(.*?)',.*", Pattern.DOTALL);
		Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[(\\[.*)", Pattern.DOTALL);

		SortedSet<String> keySet = new TreeSet<String>();

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
				if (matchIdMatcher.matches()) {
					matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
				}

				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {

					String[] scriptVariables = scriptMatcher.group(3).split("\n,");

					TeamParserObject teamParserObject = null;

					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
							matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

							matchParserObject.setDateTime(fixtureMatcher.group(5));
							matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
							continue;
						} else {
							Matcher teamMatcher = teamPattern.matcher(scriptVariable);
							if (teamMatcher.matches()) {
								teamParserObject = (teamParserObject == null ? matchParserObject.getHomeTeam() : matchParserObject.getAwayTeam());
								scriptVariable = teamMatcher.group(2);
							}
							WhoScoredPlayerStatsMap map = new WhoScoredPlayerStatsMap(scriptVariable);
							if (map.isValid()) {
								WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(map);
								teamParserObject.getPlayers().add(playerParserObject);
							} else {
								logger.debug("Script variable {} did not produce a valid stats map.", scriptVariable);
							}
							keySet.addAll(map.keySet());
						}
					}
				}
			}
		}

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}
}
