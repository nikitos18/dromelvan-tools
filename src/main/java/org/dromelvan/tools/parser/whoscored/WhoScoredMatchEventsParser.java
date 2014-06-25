package org.dromelvan.tools.parser.whoscored;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
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
		MatchParserObject matchParserObject = new MatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern scriptPattern = Pattern.compile("(.*)var initialMatchData = \\[\\[(.*), \\[(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[\\d*,\\d*,'(.*?)','(.*?)','(.*?)','.*?',\\d*,'(.*?)',(.*)\\]", Pattern.DOTALL);
		Pattern matchEventsPattern = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.DOTALL);
		Pattern goalPattern = Pattern.compile(".*\\['(.*?)',('(.*?)')?,'(goal|owngoal)','.*?',('OG')?,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern cardPattern = Pattern.compile(".*\\['(.*?)',,'(yellow|red)',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);
		Pattern substitutionPattern = Pattern.compile(".*\\['(.*?)','(.*?)','subst',,,(\\d*),(\\d*),(\\d*)\\].*", Pattern.DOTALL);

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {
					String[] scriptVariables = scriptMatcher.group(2).split("\n, ");
					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						Matcher matchEventsMatcher = matchEventsPattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							matchParserObject.setHomeTeam(new TeamParserObject(fixtureMatcher.group(1)));
							matchParserObject.setAwayTeam(new TeamParserObject(fixtureMatcher.group(2)));
							matchParserObject.setDateTime(fixtureMatcher.group(3));
							matchParserObject.setTimeElapsed(fixtureMatcher.group(4));
						} else if (matchEventsMatcher.matches()) {
							for (String eventVariable : matchEventsMatcher.group(1).split("\n")) {
								Matcher goalMatcher = goalPattern.matcher(eventVariable);
								Matcher cardMatcher = cardPattern.matcher(eventVariable);
								Matcher substitutionMatcher = substitutionPattern.matcher(eventVariable);
								if (goalMatcher.matches()) {
									// System.out.println("Goal: " + eventVariable);
								} else if (cardMatcher.matches()) {
									// System.out.println("Card: " + eventVariable);
								} else if (substitutionMatcher.matches()) {
									System.out.println("Sub: " + eventVariable);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
}
