package org.dromelvan.tools.parser.whoscored.match;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return parseRegExp();
    }

    public Set<MatchParserObject> parseJsEval() {
        WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

        Elements scriptElements = getDocument().getElementsByTag("script");
        Pattern matchIdPattern = Pattern.compile(".*parameters: \\{ id: (\\d*) \\}.*", Pattern.DOTALL);
        // 1: Home team id, 2: Away team id, 3: Home team name, 4: Away team name, 5: Datetime, 6: Elapsed
        Pattern fixturePattern = Pattern.compile(".*matchHeader.load\\(\\[(\\d*),(\\d*),'([\\w ]*)','([\\w ]*)','(.*)','.*',\\d*?,'(.*?)',.*", Pattern.DOTALL);
        Pattern initialDataPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], 0\\] ;(.*)", Pattern.DOTALL);

        for (Element scriptElement : scriptElements) {
            for (DataNode node : scriptElement.dataNodes()) {
                Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
                Matcher fixtureMatcher = fixturePattern.matcher(node.toString());

                if (matchIdMatcher.matches()) {
                    matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
                }

                if (fixtureMatcher.matches()) {
                    matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
                    matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

                    matchParserObject.setDateTime(fixtureMatcher.group(5));
                    matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
                }

                Matcher initialDataMatcher = initialDataPattern.matcher(node.toString());
                if (initialDataMatcher.matches()) {
                    String initialData = initialDataMatcher.group(3);
                    // The team stats and the first player, usually the goalkeeper, are on the same line.
                    // This should split those up in two separate lines so the first player can be treated
                    // the same was as all the other players.
                    initialData = initialData.replace("]]]],[[", "]]]],[\\\n,[");
                    String[] initialDataArray = initialData.split("\n");

                    TeamParserObject teamParserObject = null;

                    for (String initialDataRow : initialDataArray) {
                        // System.out.println(initialDataRow);
                        try {
                            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
                            String playerStatValues = ((String[]) scriptEngine.eval("(function() { var a = java.lang.reflect.Array.newInstance(java.lang.String,1); a[0]= [" + initialDataRow + "]; return a;})()"))[0];
                            String[] playerStats = playerStatValues.split(",");

                            // Rows with team and match info are shorter. Even substitute rows should all be longer than 30.
                            if (playerStats.length > 15) {
                                WhoScoredPlayerStatsMap map = new WhoScoredPlayerStatsMap(playerStats);
                                if (map.isValid()) {
                                    WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(map);
                                    teamParserObject.getPlayers().add(playerParserObject);
                                } else {
                                    System.out.println(":(");
                                    logger.debug("Script variable {} did not produce a valid stats map.", playerStatValues);
                                }

                            }
                        } catch (ScriptException e) {
                            // The 'team' rows will throw an exception since they're not well formed. Switch TeamParserObject when
                            // this happens.
                            if(initialDataRow.contains(matchParserObject.getHomeTeam().getName())) {
                                teamParserObject = matchParserObject.getHomeTeam();
                            } else if(initialDataRow.contains(matchParserObject.getAwayTeam().getName())) {
                                teamParserObject = matchParserObject.getAwayTeam();
                            }
                        }
                    }
                    break;
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
