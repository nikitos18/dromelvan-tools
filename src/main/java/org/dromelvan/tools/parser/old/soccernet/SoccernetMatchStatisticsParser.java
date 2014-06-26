package org.dromelvan.tools.parser.old.soccernet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.match.CardParserObject;
import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.SubstitutionParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.old.MatchStatisticsHTMLFileParser;
import org.dromelvan.tools.parser.old.premierleague.PremierLeagueAssistsParser;
import org.dromelvan.tools.parser.old.skysports.SkySportsRatingsParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class SoccernetMatchStatisticsParser extends MatchStatisticsHTMLFileParser {

	private final PremierLeagueAssistsParser assistsParser;
	private final SkySportsRatingsParser ratingsParser;
	private final static Logger logger = LoggerFactory.getLogger(SoccernetMatchStatisticsParser.class);

	@Inject
	public SoccernetMatchStatisticsParser(SoccernetProperties soccernetProperties, PremierLeagueAssistsParser assistsParser, SkySportsRatingsParser ratingsParser) {
		super(soccernetProperties);
		this.assistsParser = assistsParser;
		this.ratingsParser = ratingsParser;
	}

	@Override
	protected void postParseDocument(Set<MatchParserObject> matchParserObjects) throws IOException {
		super.postParseDocument(matchParserObjects);
		parseAssists(matchParserObjects.iterator().next());
		parseRatings(matchParserObjects.iterator().next());
	}

	@Override
	protected Set<MatchParserObject> parseMatches() {
		MatchParserObject matchParserObject = new MatchParserObject();
		String homeTeam = getDocument().select("h1#home-team").text();
		String awayTeam = getDocument().select("h1#away-team").text();

		matchParserObject.setHomeTeam(new TeamParserObject(homeTeam));
		matchParserObject.setAwayTeam(new TeamParserObject(awayTeam));

		Elements statTables = getDocument().select("table.stat-table");
		matchParserObject.getHomeTeam().setPlayers(parsePlayers(statTables.first()));
		matchParserObject.getAwayTeam().setPlayers(parsePlayers(statTables.last()));

		matchParserObject.getHomeTeam().setCards(parseCards(statTables.first()));
		matchParserObject.getAwayTeam().setCards(parseCards(statTables.last()));

		matchParserObject.getHomeTeam().setSubstitutions(parseSubstitutions(statTables.first()));
		matchParserObject.getAwayTeam().setSubstitutions(parseSubstitutions(statTables.last()));

		Element scoringSummaryContainer = getDocument().select("h1:contains(scoring summary)").first();
		scoringSummaryContainer = scoringSummaryContainer.parent();

		Elements scoringSummaries = scoringSummaryContainer.select("table");

		matchParserObject.getHomeTeam().setGoals(parseGoals(scoringSummaries.first()));
		matchParserObject.getAwayTeam().setGoals(parseGoals(scoringSummaries.last()));

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}

	private List<PlayerParserObject> parsePlayers(Element statTable) {
		List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
		int count = 0;
		for (Element tr : statTable.select("tr")) {
			logger.trace("Parsing stat table element {}.", tr);
			String playerName = tr.select("a").text().trim();
			if (!playerName.isEmpty()) {
				++count;
				PlayerParserObject player = new PlayerParserObject(playerName);
				player.setParticipated((count <= 11 ? 2 : 1));
				logger.debug("Parsed row {} to player {}.", count, player);
				players.add(player);
			}
		}
		return players;
	}

	private List<GoalParserObject> parseGoals(Element scoringSummary) {
		List<GoalParserObject> goals = new ArrayList<GoalParserObject>();

		Pattern homeTeamGoalPattern = Pattern.compile("(.*) \\((\\d{1,2})( \\+ \\d{1,2})?'\\)");
		Pattern homeTeamPenaltyPattern = Pattern.compile("(.*) \\(pen (\\d{1,2})( \\+ \\d{1,2})?'\\)");
		Pattern homeTeamOGPattern = Pattern.compile("(.*) \\(og (\\d{1,2})( \\+ \\d{1,2})?'\\)");
		Pattern awayTeamGoalPattern = Pattern.compile("\\((\\d{1,2})( \\+ \\d{1,2})?'\\) (.*)");
		Pattern awayTeamPenaltyPattern = Pattern.compile("\\(pen (\\d{1,2})( \\+ \\d{1,2})?'\\) (.*)");
		Pattern awayTeamOGPattern = Pattern.compile("\\(og (\\d{1,2})( \\+ \\d{1,2})?'\\) (.*)");

		for (Element td : scoringSummary.select("td")) {
			logger.trace("Parsing goal element {}.", td);
			Matcher homeTeamGoalMatcher = homeTeamGoalPattern.matcher(td.text());
			Matcher homeTeamPenaltyMatcher = homeTeamPenaltyPattern.matcher(td.text());
			Matcher homeTeamOGMatcher = homeTeamOGPattern.matcher(td.text());
			Matcher awayTeamGoalMatcher = awayTeamGoalPattern.matcher(td.text());
			Matcher awayTeamPenaltyMatcher = awayTeamPenaltyPattern.matcher(td.text());
			Matcher awayTeamOGMatcher = awayTeamOGPattern.matcher(td.text());

			GoalParserObject goal = null;
			if (homeTeamGoalMatcher.matches()) {
				goal = new GoalParserObject(homeTeamGoalMatcher.group(1), Integer.parseInt(homeTeamGoalMatcher.group(2)), false, false);
			} else if (homeTeamPenaltyMatcher.matches()) {
				goal = new GoalParserObject(homeTeamPenaltyMatcher.group(1), Integer.parseInt(homeTeamPenaltyMatcher.group(2)), true, false);
			} else if (homeTeamOGMatcher.matches()) {
				goal = new GoalParserObject(homeTeamOGMatcher.group(1), Integer.parseInt(homeTeamOGMatcher.group(2)), false, true);
			} else if (awayTeamGoalMatcher.matches()) {
				goal = new GoalParserObject(awayTeamGoalMatcher.group(3), Integer.parseInt(awayTeamGoalMatcher.group(1)), false, false);
			} else if (awayTeamPenaltyMatcher.matches()) {
				goal = new GoalParserObject(awayTeamPenaltyMatcher.group(3), Integer.parseInt(awayTeamPenaltyMatcher.group(1)), true, false);
			} else if (awayTeamOGMatcher.matches()) {
				goal = new GoalParserObject(awayTeamOGMatcher.group(3), Integer.parseInt(awayTeamOGMatcher.group(1)), false, true);
			}

			if (goal != null) {
				goals.add(goal);
				logger.debug("Parsed text {} to goal {}.", td.text(), goal);
			} else {
				logger.debug("Text {} could not be parsed into a goal.", td.text());
			}
		}

		return goals;
	}

	private List<CardParserObject> parseCards(Element statTable) {
		List<CardParserObject> cards = new ArrayList<CardParserObject>();
		for (Element tr : statTable.select("tr")) {
			logger.trace("Parsing stat table element {}.", tr);
			String playerName = tr.select("a").text().trim();
			Elements div = tr.select("div.soccer-icons-yellowcard");
			if (!div.isEmpty()) {
				int time = parseTime(div.toString());
				CardParserObject card = new CardParserObject(playerName, time, CardParserObject.CardType.YELLOW);
				logger.debug("Parsed text {} to card {}.", div, card);
				cards.add(card);
			}
			div = tr.select("div.soccer-icons-redcard");
			if (!div.isEmpty()) {
				int time = parseTime(div.toString());
				CardParserObject card = new CardParserObject(playerName, time, CardParserObject.CardType.RED);
				logger.debug("Parsed text {} to card {}.", div, card);
				cards.add(card);
			}
		}
		return cards;
	}

	private List<SubstitutionParserObject> parseSubstitutions(Element statTable) {
		List<SubstitutionParserObject> substitutions = new ArrayList<SubstitutionParserObject>();
		Pattern pattern = Pattern.compile("(.*)Off: (.*)'(.*)");
		for (Element tr : statTable.select("tr")) {
			logger.trace("Parsing stat table element {}.", tr);
			String playerIn = tr.select("a").text().trim();
			Elements div = tr.select("div.soccer-icons-subinout");
			if (!div.isEmpty()) {
				Matcher matcher = pattern.matcher(div.toString());
				if (matcher.matches()) {
					String playerOut = matcher.group(2);
					int time = parseTime(div.toString());
					SubstitutionParserObject substitution = new SubstitutionParserObject(playerOut, playerIn, time);
					logger.debug("Parsed text {} to substitution {}.", div, substitution);
					substitutions.add(substitution);
				} else {
					logger.warn("Could not parse player out from text {}.", div.toString());
				}
			}
		}
		return substitutions;
	}

	private int parseTime(String text) {
		int time = 0;
		Pattern pattern = Pattern.compile("(.*) - (\\d{1,2})(.*)");
		Matcher matcher = pattern.matcher(text);
		if (matcher.matches()) {
			time = Integer.parseInt(matcher.group(2));
		} else {
			logger.warn("Could not parse time from text {}.", text);
		}
		return time;
	}

	private void parseAssists(MatchParserObject matchParserObject) throws IOException {
		File premierLeagueFile = new File(getFile().getAbsolutePath().replace("soccernet", "premierleague").replace(getFile().getName(), "assists.html"));
		logger.debug("Parsing assists from file {}.", premierLeagueFile);
		if (premierLeagueFile.exists()) {
			this.assistsParser.setFile(premierLeagueFile);
			this.assistsParser.parse();
			MatchParserObject assistsMatchParserObject = assistsParser.getMatchParserObject(matchParserObject.getHomeTeam().getName(), matchParserObject.getAwayTeam().getName());
			if (assistsMatchParserObject != null) {
				for (TeamParserObject team : assistsMatchParserObject.getTeams()) {
					for (PlayerParserObject assistPlayer : team.getPlayers()) {
						PlayerParserObject player = matchParserObject.getTeam(team.getName()).getPlayer(assistPlayer.getName(), "assists");
						if (player != null) {
							logger.debug("Player {} was mapped to player {}.", assistPlayer.getName(), player.getName());
							player.setAssists(assistPlayer.getAssists());
							logger.debug("Updated assists for player {}.", player);
						} else {
							logger.warn("Could not find player {} in team {} despite having assist(s).", assistPlayer.getName(), team.getName());
						}
					}
				}
			} else {
				logger.warn("Could not find assists for match {} in assists file {}. Outputfile will not contain assists.", matchParserObject, premierLeagueFile);
			}
		} else {
			logger.warn("Assist file {} does not exist. Outputfile will not contain assists.", premierLeagueFile);
		}
	}

	private void parseRatings(MatchParserObject matchParserObject) throws IOException {
		File skySportsFile = new File(getFile().getAbsolutePath().replace("soccernet", "skysports"));
		logger.debug("Parsing ratings from file {}.", skySportsFile);
		if (skySportsFile.exists()) {
			this.ratingsParser.setFile(skySportsFile);
			this.ratingsParser.parse();
			MatchParserObject ratingsMatchParserObject = ratingsParser.getMatchParserObject(matchParserObject.getHomeTeam().getName(), matchParserObject.getAwayTeam().getName());

			if (ratingsMatchParserObject != null) {
				for (TeamParserObject team : ratingsMatchParserObject.getTeams()) {
					for (PlayerParserObject ratingPlayer : team.getPlayers()) {
						if (ratingPlayer.getRating() > 0) {
							PlayerParserObject player = matchParserObject.getTeam(team.getName()).getPlayer(ratingPlayer.getName(), "rating");
							if (player != null) {
								logger.debug("Player {} was mapped to player {}.", ratingPlayer.getName(), player.getName());
								player.setRating(ratingPlayer.getRating());
								logger.debug("Updated rating for player {}.", player);
							} else {
								logger.warn("Could not find player {} in team {} despite having rating.", ratingPlayer.getName(), team.getName());
							}
						}
					}
				}
			} else {
				logger.warn("Could not find ratings for match {} in ratings file {}. Outputfile will not contain ratings.", matchParserObject, skySportsFile);
			}
		} else {
			logger.warn("Ratings file {} does not exist. Outputfile will not contain ratings.", skySportsFile);
		}
	}

}
