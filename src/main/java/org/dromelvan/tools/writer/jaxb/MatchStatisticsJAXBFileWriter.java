package org.dromelvan.tools.writer.jaxb;

import java.math.BigInteger;
import java.util.Set;

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
import org.dromelvan.tools.parser.match.CardParserObject;
import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.SubstitutionParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;

public class MatchStatisticsJAXBFileWriter extends JAXBFileWriter<MatchParserObject> {

	public MatchStatisticsJAXBFileWriter() {
		setXmlRootClass(PLMatch.class);
	}

	@Override
	protected JAXBElement buildDocument(Set<MatchParserObject> matchParserObjects) {
		PLMatch plMatch = new PLMatch();

		MatchParserObject matchParserObject = matchParserObjects.iterator().next();
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

		for (PlayerParserObject playerParserObject : teamParserObject.getPlayers()) {
			PlayerMatchStatistics playerMatchStatistics = new PlayerMatchStatistics();
			playerMatchStatistics.setPlayer(playerParserObject.getName());
			playerMatchStatistics.setWhoScoredId(BigInteger.valueOf(playerParserObject.getWhoScoredId()));
			playerMatchStatistics.setParticipated(playerParserObject.getParticipated());
			playerMatchStatistics.setAssists(playerParserObject.getAssists());
			playerMatchStatistics.setRating(playerParserObject.getRating());
			plTeam.getPlayers().getPlayerMatchStatistics().add(playerMatchStatistics);
		}

		for (GoalParserObject goalParserObject : teamParserObject.getGoals()) {
			Goal goal = new Goal();
			goal.setPlayer(goalParserObject.getPlayer());
			goal.setWhoScoredId(BigInteger.valueOf(goalParserObject.getWhoScoredId()));
			goal.setTime(goalParserObject.getTime());
			goal.setPenalty(goalParserObject.isPenalty());
			goal.setOwnGoal(goalParserObject.isOwnGoal());
			plTeam.getGoals().getGoal().add(goal);
		}

		for (CardParserObject cardParserObject : teamParserObject.getCards()) {
			Card card = new Card();
			card.setPlayer(cardParserObject.getPlayer());
			card.setWhoScoredId(BigInteger.valueOf(cardParserObject.getWhoScoredId()));
			card.setTime(cardParserObject.getTime());
			card.setType((cardParserObject.getCardType() == CardParserObject.CardType.YELLOW ? CardType.YELLOW : CardType.RED));
			plTeam.getCards().getCard().add(card);
		}

		for (SubstitutionParserObject substitutionParserObject : teamParserObject.getSubstitutions()) {
			Substitution substitution = new Substitution();
			substitution.setPlayerOut(substitutionParserObject.getPlayerOut());
			substitution.setPlayerIn(substitutionParserObject.getPlayerIn());
			substitution.setPlayerOutWhoScoredId(BigInteger.valueOf(substitutionParserObject.getPlayerOutWhoScoredId()));
			substitution.setPlayerInWhoScoredId(BigInteger.valueOf(substitutionParserObject.getPlayerInWhoScoredId()));
			substitution.setTime(substitutionParserObject.getTime());
			plTeam.getSubstitutions().getSubstitution().add(substitution);
		}
		return plTeam;
	}
}
