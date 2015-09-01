package org.dromelvan.tools.parser.whoscored.match;

import java.util.List;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;

public class WhoScoredPlayerStatsJavaScriptVariables extends JavaScriptVariables {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2588603981215685662L;
	public final static String MATCH_ID = "matchId";
	public final static String INITIAL_DATA = "initialData";

	@Override
	public void init() {
	}

	public int getMatchId() {
		return (Integer) get(MATCH_ID);
	}

	public MatchParserObject getMatchParserObject() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

		List matchInfo = getMatchInfo();

		TeamParserObject homeTeamParserObject = getTeamParserObject((Integer) matchInfo.get(0));
		TeamParserObject awayTeamParserObject = getTeamParserObject((Integer) matchInfo.get(1));

		matchParserObject.setHomeTeam(homeTeamParserObject);
		matchParserObject.setAwayTeam(awayTeamParserObject);

		matchParserObject.setDateTime((String) matchInfo.get(4));
		matchParserObject.setTimeElapsed((String) matchInfo.get(7));

		return matchParserObject;
	}

	private List getInitialData() {
		return (List) ((List) get(INITIAL_DATA)).get(0);
	}

	private List getMatchInfo() {
		List initialData = getInitialData();
		List matchInfo = (List) initialData.get(0);
		return matchInfo;
	}

	private TeamParserObject getTeamParserObject(int id) {
		for (List team : (List<List>) getInitialData().get(1)) {
			if (id == (Integer) team.get(0)) {
				String name = (String) team.get(1);
				TeamParserObject teamParserObject = new WhoScoredTeamParserObject(name, id);

				List<List> players = (List<List>) team.get(4);
				for (List player : players) {
					WhoScoredPlayerStatsMap whoScoredPlayerStatsMap = new WhoScoredPlayerStatsMap(player);
					WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(whoScoredPlayerStatsMap);
					teamParserObject.getPlayers().add(playerParserObject);
				}

				return teamParserObject;
			}
		}
		return null;
	}
}
