package org.dromelvan.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.JSoupURLReader;
import org.dromelvan.tools.parser.whoscored.FixtureParserObject;
import org.dromelvan.tools.parser.whoscored.TeamStandingsParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredTeamFixturesParser;
import org.dromelvan.tools.parser.whoscored.WhoScoredTeamStandingsParser;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredFixturesParserTest {

    private final static Logger logger = LoggerFactory.getLogger(WhoScoredFixturesParserTest.class);

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            try {
                bindManyInstances(URL.class, new URL("http://www.whoscored.com/Regions/252/Tournaments/2"));
            } catch (MalformedURLException e) {
            }
        }
    }

    @Test
    public void parseFixtures(@All URL competitionURL, WhoScoredTeamStandingsParser whoScoredTeamStandingsParser, WhoScoredTeamFixturesParser whoScoredTeamFixturesParser) throws MalformedURLException, IOException {
        JSoupURLReader jSoupURLReader = new JSoupURLReader(competitionURL);
        Document document = jSoupURLReader.read();
        whoScoredTeamStandingsParser.setDocument(document);

        Set<TeamStandingsParserObject> teamStandingsParserObjects = whoScoredTeamStandingsParser.parse();
        Map<Integer, FixtureParserObject> fixtureMap = new HashMap<Integer, FixtureParserObject>();

        for(TeamStandingsParserObject teamStandingsParserObject : teamStandingsParserObjects) {
            URL teamFixturesURL = new URL("http://www.whoscored.com/Teams/" + teamStandingsParserObject.getId() + "/Fixtures/England-" + teamStandingsParserObject.getName().replace(' ', '-'));

            JSoupURLReader teamFixturesReader = new JSoupURLReader(teamFixturesURL);
            Document teamFixturesDocument = teamFixturesReader.read();

            whoScoredTeamFixturesParser.setDocument(teamFixturesDocument);

            Set<FixtureParserObject> fixtureParserObjects = whoScoredTeamFixturesParser.parse();
            logger.info("Found {} fixtures for team {}.", fixtureParserObjects.size(), teamStandingsParserObject.getName());

            for(FixtureParserObject fixtureParserObject : fixtureParserObjects) {
                fixtureMap.put(fixtureParserObject.getWhoScoredId(), fixtureParserObject);
            }
        }

        logger.info("Total number of fixtures: {}.", fixtureMap.size());

        File output = new File("output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));

        try {
            for(FixtureParserObject fixtureParserObject : fixtureMap.values()) {
                String date = fixtureParserObject.getDate();
                Pattern datePattern = Pattern.compile("([\\d]{2})-([\\d]{2})-([\\d]{4})");
                Matcher dateMatcher = datePattern.matcher(date);

                if(dateMatcher.matches()) {
                    date = String.format("%s-%s-%s", dateMatcher.group(3), dateMatcher.group(2), dateMatcher.group(1));
                    String fixture = String.format("  [ %d, %d, \"%s %s\", %d ],", fixtureParserObject.getHomeTeamId(), fixtureParserObject.getAwayTeamId(), date, fixtureParserObject.getTime(), fixtureParserObject.getWhoScoredId());
                    writer.write(fixture + "\n");
                    writer.flush();
                } else {
                    throw new IllegalArgumentException("Invalid date: " + date);
                }
            }
        } finally {
            writer.close();
        }
    }
}
