package org.dromelvan.tools.parser.old.premierleague;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.old.MatchParserObject;
import org.dromelvan.tools.parser.old.MatchStatisticsHTMLFileParser;
import org.dromelvan.tools.parser.old.PlayerParserObject;
import org.dromelvan.tools.parser.old.TeamParserObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class PremierLeagueAssistsParser extends MatchStatisticsHTMLFileParser {

    private final static Logger logger = LoggerFactory.getLogger(PremierLeagueAssistsParser.class);

    @Inject
    public PremierLeagueAssistsParser(PremierLeagueProperties premierLeagueProperties) {
        super(premierLeagueProperties);
    }

    @Override
    protected Set<MatchParserObject> parseMatches() {
        Elements table = getDocument().select("table.ismFixtureTable");

        List<Element> results = table.select("tr.ismResult");
        List<Element> fixtureSummaries = table.select("tr.ismFixtureSummary");

        Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
        for(int i = 0; i < results.size(); ++i) {
            matchParserObjects.add(parseMatch(results.get(i),fixtureSummaries.get(i)));
        }
        return matchParserObjects;
    }

    private MatchParserObject parseMatch(Element result, Element fixtureSummary) {
        MatchParserObject matchParserObject = new MatchParserObject();
        Element homeTeam = result.select("td.ismHomeTeam").get(0);
        Element awayTeam = result.select("td.ismAwayTeam").get(0);

        Element homeTeamSummary = fixtureSummary.select("dl.ismSummary").get(0);
        Element awayTeamSummary = fixtureSummary.select("dl.ismSummary").get(1);

        logger.debug("Parsing PremierLeague.com file for match: {} vs. {}.", homeTeam.text(), awayTeam.text());

        matchParserObject.setHomeTeam(parseTeam(homeTeam, homeTeamSummary));
        matchParserObject.setAwayTeam(parseTeam(awayTeam, awayTeamSummary));
        return matchParserObject;
    }

    private TeamParserObject parseTeam(Element team, Element summary) {
        TeamParserObject teamParserObject = new TeamParserObject(team.text());
        logger.debug("Parsing team: {}.", team.text());

        int startIndex = -1;
        int stopIndex = 9999;
        for(Element dt : summary.select("dt")) {
            if(dt.text().equalsIgnoreCase("assists")) {
                startIndex = dt.elementSiblingIndex();
                logger.trace("startIndex = " + startIndex);
            } else if(startIndex >= 0) {
                logger.trace("stopIndex = " + stopIndex);
                stopIndex = dt.elementSiblingIndex();
                break;
            }
        }

        if(startIndex > 0) {
            List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
            for(Element assist : summary.select("dd:gt(" + startIndex + "):lt(" + stopIndex + ")")) {
                logger.debug("Found assist for player: {}.", assist.text());
                String name = assist.text();
                int assists = 1;

                Pattern pattern = Pattern.compile("(.*) \\((.*)\\)");
                Matcher matcher = pattern.matcher(name);
                if(matcher.matches()) {
                    name = matcher.group(1);
                    assists = Integer.parseInt(matcher.group(2));
                }

                PlayerParserObject playerParserObject = new PlayerParserObject(name);
                playerParserObject.setAssists(assists);
                players.add(playerParserObject);
            }
            teamParserObject.setPlayers(players);
        }
        return teamParserObject;
    }
}
