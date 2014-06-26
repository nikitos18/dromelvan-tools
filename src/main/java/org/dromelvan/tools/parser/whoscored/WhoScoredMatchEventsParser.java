package org.dromelvan.tools.parser.whoscored;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class WhoScoredMatchEventsParser extends JSoupDocumentParser<MatchParserObject> {

	@Inject
	public WhoScoredMatchEventsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<MatchParserObject> parse() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern matchIdPattern = Pattern.compile(".*parameters: \\{.*id: (\\d*).*\\}.*", Pattern.DOTALL);
		Pattern scriptPattern = Pattern.compile("(.*)var initialMatchData = \\[\\[(.*), \\[(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'(.*?)','(.*?)','(.*?)','.*?',\\d*,'(.*?)',(.*)\\]", Pattern.DOTALL);
		Pattern matchEventsPattern = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.DOTALL);
		Pattern goalPattern = Pattern.compile(".*\\['(.*?)',('(.*?)')?,'(goal|owngoal)','.*?',('OG')?,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern cardPattern = Pattern.compile(".*\\['(.*?)',,'(yellow|red)',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern substitutionPattern = Pattern.compile(".*\\['(.*?)','(.*?)','subst',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
                Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
                if(matchIdMatcher.matches()) {
                    matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
                }

				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {
					String[] scriptVariables = scriptMatcher.group(2).split("\n, ");
					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						Matcher matchEventsMatcher = matchEventsPattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
                            matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
                            matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

							matchParserObject.setDateTime(fixtureMatcher.group(5));
							matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
						} else if (matchEventsMatcher.matches()) {
							for (String eventVariable : matchEventsMatcher.group(1).split("\n")) {
								Matcher goalMatcher = goalPattern.matcher(eventVariable);
								Matcher cardMatcher = cardPattern.matcher(eventVariable);
								Matcher substitutionMatcher = substitutionPattern.matcher(eventVariable);
								if (goalMatcher.matches()) {
								    WhoScoredGoalParserObject goalParserObject = new WhoScoredGoalParserObject(goalMatcher.group(1),
								                                                                               Integer.parseInt(goalMatcher.group(7)),
								                                                                               goalMatcher.group(3),
								                                                                               Integer.parseInt(goalMatcher.group(8)),
								                                                                               Integer.parseInt(goalMatcher.group(6)),
								                                                                               false,
								                                                                               goalMatcher.group(4).equalsIgnoreCase("owngoal"));
									matchParserObject.getHomeTeam().getGoals().add(goalParserObject);
								} else if (cardMatcher.matches()) {
									// System.out.println("Card: " + eventVariable);
								} else if (substitutionMatcher.matches()) {
									//System.out.println("Sub: " + eventVariable);
								}
							}
						}
					}
				}
			}
		}
        Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
        matchParserObjects.add(matchParserObject);
        return matchParserObjects;
	}
}
