package org.dromelvan.tools.parser.whoscored;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.CardParserObject.CardType;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredMatchEventsParser extends JSoupDocumentParser<MatchParserObject> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchEventsParser.class);

	@Inject
	public WhoScoredMatchEventsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<MatchParserObject> parse() {
		return parse(new WhoScoredMatchParserObject());
	}

	public URL getPlayerStatsURL() {
		Elements aElements = getDocument().getElementsByTag("a");
		for (Element aElement : aElements) {
			if (aElement.text() != null
					&& aElement.text().equalsIgnoreCase("player statistics")) {
				String urlString = "http://www.whoscored.com" + aElement.attr("href");
				try {
					URL url = new URL(urlString);
					return url;
				} catch (MalformedURLException e) {
					logger.error("Malformed URL exception when getting player stats URL: {}.", urlString);
				}
			}
		}
		return null;
	}

	public Set<MatchParserObject> parse(WhoScoredMatchParserObject whoScoredMatchParserObject) {
		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern matchIdPattern = Pattern.compile(".*var liveMatchUpdater = .*parameters: \\{.*id: (\\d*).*\\}.*", Pattern.DOTALL);
		Pattern scriptPattern = Pattern.compile("(.*)var initialMatchData = \\[\\[(.*), \\[(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'(.*?)','(.*?)','(.*?)','.*?',\\d*,'(.*?)',(.*)\\]", Pattern.DOTALL);
		Pattern matchEventsPattern = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.DOTALL);
		Pattern goalPattern = Pattern.compile(".*\\['(.*?)',('(.*?)')?,'(goal|owngoal|penalty-goal)','.*?',('OG'|'Pen.')?,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern cardPattern = Pattern.compile(".*\\['(.*?)',,'(yellow|red)',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern substitutionPattern = Pattern.compile(".*\\['(.*?)','(.*?)','subst',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
				if (matchIdMatcher.matches()) {
					int whoScoredId = Integer.parseInt(matchIdMatcher.group(1));
					if (whoScoredMatchParserObject.getWhoScoredId() == 0) {
						whoScoredMatchParserObject.setWhoScoredId(whoScoredId);
					} else if (whoScoredMatchParserObject.getWhoScoredId() != whoScoredId) {
						logger.error("Provided WhoScoredMatchParserObject had whoScoredId={} but parsed document had whoScoredId={}", whoScoredMatchParserObject.getWhoScoredId(), whoScoredId);
					}
				}

				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {
					String[] scriptVariables = scriptMatcher.group(2).split("\n, ");
					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						Matcher matchEventsMatcher = matchEventsPattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							if (whoScoredMatchParserObject.getHomeTeam() == null) {
								whoScoredMatchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
							}
							if (whoScoredMatchParserObject.getAwayTeam() == null) {
								whoScoredMatchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));
							}
							if (whoScoredMatchParserObject.getDateTime() == null) {
								whoScoredMatchParserObject.setDateTime(fixtureMatcher.group(5));
							}
							if (whoScoredMatchParserObject.getTimeElapsed() == null) {
								whoScoredMatchParserObject.setTimeElapsed(fixtureMatcher.group(6));
							}
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
											goalMatcher.group(4).equalsIgnoreCase("penalty-goal"),
											goalMatcher.group(4).equalsIgnoreCase("owngoal"));

									WhoScoredTeamParserObject team = whoScoredMatchParserObject.getTeamForGoal(goalParserObject);
									if (team == null) {
										logger.error("Could not find team for goal {}.", goalParserObject);
									} else {
										team.getGoals().add(goalParserObject);
									}
								} else if (cardMatcher.matches()) {
									CardType cardType = (cardMatcher.group(2).equalsIgnoreCase("yellow") ? CardType.YELLOW : CardType.RED);
									WhoScoredCardParserObject cardParserObject = new WhoScoredCardParserObject(cardMatcher.group(1), Integer.parseInt(cardMatcher.group(4)), Integer.parseInt(cardMatcher.group(3)), cardType);
									whoScoredMatchParserObject.getTeamForPlayer(cardParserObject.getPlayerWhoScoredId()).getCards().add(cardParserObject);
								} else if (substitutionMatcher.matches()) {
									WhoScoredSubstitutionParserObject substitutionParserObject = new WhoScoredSubstitutionParserObject(substitutionMatcher.group(1),
											Integer.parseInt(substitutionMatcher.group(4)),
											substitutionMatcher.group(2),
											Integer.parseInt(substitutionMatcher.group(5)),
											Integer.parseInt(substitutionMatcher.group(3)));
									whoScoredMatchParserObject.getTeamForPlayer(substitutionParserObject.getPlayerOutWhoScoredId()).getSubstitutions().add(substitutionParserObject);
								}
							}
						}
					}
				}
			}
		}
		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(whoScoredMatchParserObject);
		return matchParserObjects;
	}
}
