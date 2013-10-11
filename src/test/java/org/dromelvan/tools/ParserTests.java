package org.dromelvan.tools;

import java.io.File;
import java.util.Set;

import org.dromelvan.tools.parser.CardParserObject;
import org.dromelvan.tools.parser.GoalParserObject;
import org.dromelvan.tools.parser.MatchParserObject;
import org.dromelvan.tools.parser.PlayerParserObject;
import org.dromelvan.tools.parser.SubstitutionParserObject;
import org.dromelvan.tools.parser.TeamParserObject;
import org.dromelvan.tools.parser.premierleague.PremierLeagueAssistsParser;
import org.dromelvan.tools.parser.skysports.SkySportsRatingsParser;
import org.dromelvan.tools.parser.soccernet.SoccernetMatchStatisticsParser;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

@RunWith(JukitoRunner.class)
public class ParserTests {

    private final static Logger logger = LoggerFactory.getLogger(ParserTests.class);

    public static class Module extends JukitoModule {
        protected void configureTest() {
            File dataDirectory = new File("add-data-dir-here");
            File premierLeagueDirectory = new File(dataDirectory, "premierleague/omg01");

            bindManyNamedInstances(File.class, "PremierLeagueFile", new File(premierLeagueDirectory, "assists.html"));

            File skySportsDirectory = new File(dataDirectory, "skysports/omg01");
            bindManyNamedInstances(File.class, "SkySportsFile", new File(skySportsDirectory, "Ars-Ast.html"));

//            File soccernetDirectory = new File(dataDirectory, "soccernet/omg01");
//            bindManyNamedInstances(File.class, "SoccernetFile",
//                    new File(soccernetDirectory, "Ars-Ast.html")//,
//                    new File(soccernetDirectory, "Che-Hul.html"),
//                    new File(soccernetDirectory, "Cry-Tot.html"),
//                    new File(soccernetDirectory, "Liv-Sto.html"),
//                    new File(soccernetDirectory, "MaC-New.html"),
//                    new File(soccernetDirectory, "Nor-Eve.html"),
//                    new File(soccernetDirectory, "Sun-Ful.html"),
//                    new File(soccernetDirectory, "Swa-MaU.html"),
//                    new File(soccernetDirectory, "WBA-Sou.html"),
//                    new File(soccernetDirectory, "Wes-Car.html")
//                    );

            File soccernetDirectory = new File(dataDirectory, "soccernet");
            bindManyNamedInstances(File.class, "SoccernetDirectory",
                    new File(soccernetDirectory, "omg01"),
                    new File(soccernetDirectory, "omg02"),
                    new File(soccernetDirectory, "omg03"),
                    new File(soccernetDirectory, "omg04"),
                    new File(soccernetDirectory, "omg05"),
                    new File(soccernetDirectory, "omg06")
                      );

        }
    }

    @Test
    public void parsePremierLeagueAssists(@All("PremierLeagueFile") File premierLeagueFile, PremierLeagueAssistsParser parser) {
        parser.setFile(premierLeagueFile);
        Set<MatchParserObject> matchParserObjects = parser.parse();
        for(MatchParserObject matchParserObject : matchParserObjects) {
            logger.debug("Assists for {} vs {}:", matchParserObject.getHomeTeam().getName(), matchParserObject.getAwayTeam().getName());
            for(PlayerParserObject playerParserObject : matchParserObject.getHomeTeam().getPlayers()) {
                logger.debug("{} assist: {}.", matchParserObject.getHomeTeam().getName(), playerParserObject);
            }
            for(PlayerParserObject playerParserObject : matchParserObject.getAwayTeam().getPlayers()) {
                logger.debug("{} assist: {}.", matchParserObject.getAwayTeam().getName(), playerParserObject);
            }
        }
    }

    @Test
    public void parserSoccernetDirectories(@All("SoccernetDirectory") File soccernetDirectory, SoccernetMatchStatisticsParser parser) {
        if(soccernetDirectory.isDirectory()) {
            for(File file : soccernetDirectory.listFiles()) {
                if(Files.getFileExtension(file.getName()).equalsIgnoreCase("html")
                   && !file.isHidden()) {
                    parseSoccernetMatchStatistics(file, parser);
                }
            }
        }
    }

    @Test
    public void parseSoccernetMatchStatistics(@All("SoccernetFile") File soccernetFile, SoccernetMatchStatisticsParser parser) {
        parser.setFile(soccernetFile);
        Set<MatchParserObject> matchParserObjects = parser.parse();

        for(MatchParserObject matchParserObject : matchParserObjects) {
            TeamParserObject homeTeam = matchParserObject.getHomeTeam();
            TeamParserObject awayTeam = matchParserObject.getAwayTeam();

            logger.debug("Match stats for {} vs {}:", homeTeam, awayTeam);
            for(PlayerParserObject playerParserObject : homeTeam.getPlayers()) {
                logger.debug("{} player: {}.", homeTeam, playerParserObject);
            }
            for(PlayerParserObject playerParserObject : awayTeam.getPlayers()) {
                logger.debug("{} player: {}.", awayTeam, playerParserObject);
            }

            for(GoalParserObject goalParserObject : homeTeam.getGoals()) {
                logger.debug("{} goal: {}.", homeTeam, goalParserObject);
            }
            for(GoalParserObject goalParserObject : awayTeam.getGoals()) {
                logger.debug("{} goal: {}.", awayTeam, goalParserObject);
            }

            for(CardParserObject cardParserObject : homeTeam.getCards()) {
                logger.debug("{} card: {}.", homeTeam, cardParserObject);
            }
            for(CardParserObject cardParserObject : awayTeam.getCards()) {
                logger.debug("{} card: {}.", awayTeam, cardParserObject);
            }

            for(SubstitutionParserObject substitutionParserObject : homeTeam.getSubstitutions()) {
                logger.debug("{} substitution: {}.", homeTeam, substitutionParserObject);
            }
            for(SubstitutionParserObject substitutionParserObject : awayTeam.getSubstitutions()) {
                logger.debug("{} substitution: {}.", awayTeam, substitutionParserObject);
            }
        }
    }

    @Test
    public void parseSkySportsRatingsFile(@All("SkySportsFile") File skySportsFile, SkySportsRatingsParser parser) {
        parser.setFile(skySportsFile);
        Set<MatchParserObject> matchParserObjects = parser.parse();

        for(MatchParserObject matchParserObject : matchParserObjects) {
            TeamParserObject homeTeam = matchParserObject.getHomeTeam();
            TeamParserObject awayTeam = matchParserObject.getAwayTeam();

            logger.debug("Match stats for {} vs {}:", homeTeam, awayTeam);
            for(PlayerParserObject playerParserObject : homeTeam.getPlayers()) {
                logger.debug("{} player: {}.", homeTeam, playerParserObject);
            }
            for(PlayerParserObject playerParserObject : awayTeam.getPlayers()) {
                logger.debug("{} player: {}.", awayTeam, playerParserObject);
            }
        }
    }
}

