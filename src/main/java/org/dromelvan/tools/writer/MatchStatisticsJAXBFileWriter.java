package org.dromelvan.tools.writer;

import javax.xml.bind.JAXBElement;

import org.dromelvan.jaxb.Card;
import org.dromelvan.jaxb.CardType;
import org.dromelvan.jaxb.Cards;
import org.dromelvan.jaxb.Goal;
import org.dromelvan.jaxb.Goals;
import org.dromelvan.jaxb.ObjectFactory;
import org.dromelvan.jaxb.PLMatch;
import org.dromelvan.jaxb.PLTeam;
import org.dromelvan.jaxb.PlayerMatchStatistics;
import org.dromelvan.jaxb.Players;
import org.dromelvan.jaxb.Substitution;
import org.dromelvan.jaxb.Substitutions;
import org.dromelvan.tools.parser.old.CardParserObject;
import org.dromelvan.tools.parser.old.GoalParserObject;
import org.dromelvan.tools.parser.old.MatchParserObject;
import org.dromelvan.tools.parser.old.PlayerParserObject;
import org.dromelvan.tools.parser.old.SubstitutionParserObject;
import org.dromelvan.tools.parser.old.TeamParserObject;

public class MatchStatisticsJAXBFileWriter extends JAXBFileWriter<MatchParserObject> implements MatchStatisticsWriter {

    public MatchStatisticsJAXBFileWriter() {
        setXmlRootClass(PLMatch.class);
    }
    
    @Override
    protected JAXBElement buildDocument(MatchParserObject matchParserObject) {
        PLMatch plMatch = new PLMatch();

        plMatch.setHomeTeam(buildTeam(matchParserObject.getHomeTeam()));
        plMatch.setAwayTeam(buildTeam(matchParserObject.getAwayTeam()));
        
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createMatch(plMatch);
    }
    
    private PLTeam buildTeam(TeamParserObject teamParserObject) {
        PLTeam plTeam = new PLTeam();
        plTeam.setName(teamParserObject.getName());
        plTeam.setPlayers(new Players());
        plTeam.setGoals(new Goals());
        plTeam.setCards(new Cards());
        plTeam.setSubstitutions(new Substitutions());

        for(PlayerParserObject playerParserObject : teamParserObject.getPlayers()) {
            PlayerMatchStatistics playerMatchStatistics = new PlayerMatchStatistics();
            playerMatchStatistics.setPlayer(playerParserObject.getName());
            playerMatchStatistics.setParticipated(playerParserObject.getParticipated());
            playerMatchStatistics.setAssists(playerParserObject.getAssists());
            playerMatchStatistics.setRating(playerParserObject.getRating());
            plTeam.getPlayers().getPlayerMatchStatistics().add(playerMatchStatistics);            
        }

        for(GoalParserObject goalParserObject : teamParserObject.getGoals()) {
            Goal goal = new Goal();
            goal.setPlayer(goalParserObject.getPlayer());
            goal.setTime(goalParserObject.getTime());
            goal.setPenalty(goalParserObject.isPenalty());
            goal.setOwnGoal(goalParserObject.isOwnGoal());
            plTeam.getGoals().getGoal().add(goal);
        }
        
        for(CardParserObject cardParserObject : teamParserObject.getCards()) {
            Card card = new Card();
            card.setPlayer(cardParserObject.getPlayer());
            card.setTime(cardParserObject.getTime());
            card.setType((cardParserObject.getCardType() == CardParserObject.CardType.YELLOW ? CardType.YELLOW : CardType.RED));
            plTeam.getCards().getCard().add(card);
        }
        
        for(SubstitutionParserObject substitutionParserObject : teamParserObject.getSubstitutions()) {
            Substitution substitution = new Substitution();
            substitution.setPlayerOut(substitutionParserObject.getPlayerOut());
            substitution.setPlayerIn(substitutionParserObject.getPlayerIn());
            substitution.setTime(substitutionParserObject.getTime());
            plTeam.getSubstitutions().getSubstitution().add(substitution);
        }
        return plTeam;
    }
}
