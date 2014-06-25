package org.dromelvan.tools.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.match.CardParserObject;
import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.SubstitutionParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserProperties extends Properties {

	/**
     *
     */
	private static final long serialVersionUID = -9177030338967107482L;
	private boolean verifyNames = false;
	private final Properties playerNames = new Properties();
	private final Properties teamNames = new Properties();
	private final static Logger logger = LoggerFactory.getLogger(ParserProperties.class);

	public ParserProperties(String name) {
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream(name + ".properties");
			load(new InputStreamReader(input, "UTF-8"));

			input = getClass().getClassLoader().getResourceAsStream("player_names.properties");
			playerNames.load(new InputStreamReader(input, "UTF-8"));

			input = getClass().getClassLoader().getResourceAsStream("team_names.properties");
			teamNames.load(new InputStreamReader(input, "UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NullPointerException e) {
			logger.error("Could not load {}.properties", name);
		}
	}

	public boolean isVerifyNames() {
		return verifyNames;
	}

	public void setVerifyNames(boolean verifyNames) {
		this.verifyNames = verifyNames;
	}

	public void map(MatchParserObject matchParserObject) {
		map(matchParserObject.getHomeTeam());
		map(matchParserObject.getAwayTeam());
	}

	protected void map(TeamParserObject teamParserObject) {
		if (teamParserObject != null) {
			teamParserObject.setName(mapTeamName(teamParserObject.getName()));
			for (PlayerParserObject playerParserObject : teamParserObject.getPlayers()) {
				map(playerParserObject);
			}
			for (GoalParserObject goalParserObject : teamParserObject.getGoals()) {
				map(goalParserObject);
			}
			for (CardParserObject cardParserObject : teamParserObject.getCards()) {
				map(cardParserObject);
			}
			for (SubstitutionParserObject substitutionParserObject : teamParserObject.getSubstitutions()) {
				map(substitutionParserObject);
			}
		} else {
			logger.warn("Team missing.");
		}
	}

	protected void map(PlayerParserObject playerParserObject) {
		playerParserObject.setName(mapPlayerName(playerParserObject.getName()));
	}

	protected void map(GoalParserObject goalParserObject) {
		goalParserObject.setPlayer(mapPlayerName(goalParserObject.getPlayer()));
	}

	protected void map(CardParserObject cardParserObject) {
		cardParserObject.setPlayer(mapPlayerName(cardParserObject.getPlayer()));
	}

	protected void map(SubstitutionParserObject substitutionParserObject) {
		substitutionParserObject.setPlayerOut(mapPlayerName(substitutionParserObject.getPlayerOut()));
		substitutionParserObject.setPlayerIn(mapPlayerName(substitutionParserObject.getPlayerIn()));
	}

	protected String mapTeamName(String name) {
		name = name.trim();
		name = mapName("team", name);

		if (this.verifyNames) {
			for (Object object : this.teamNames.keySet()) {
				String key = ((String) object).replace('_', ' ');
				if (name.equalsIgnoreCase(key)) {
					return name;
				}
			}
			logger.warn("Unknown team {}.", name);
		}

		return name;
	}

	protected String mapPlayerName(String name) {
		name = name.trim();
		name = mapName("player", name);
		if (this.verifyNames) {

			for (Object object : this.playerNames.keySet()) {
				String key = ((String) object).replace('_', ' ');
				if (name.equalsIgnoreCase(key)) {
					return name;
				}
			}
			logger.warn("Unknown player {}.", name);
		}
		return name;
	}

	protected String mapName(String prefix, String name) {
		String nameKey = prefix + "." + name.replace(' ', '_');
		logger.trace("Mapping key {}.", nameKey);
		for (Object key : keySet()) {
			if (Pattern.compile((String) key).matcher(nameKey).matches()) {
				nameKey = key.toString();
				logger.trace("Key matched regexp {}.", nameKey);
			}
		}

		String mappedName = getProperty(nameKey);
		if (mappedName != null) {
			logger.debug("Mapped name {} to {}.", name, mappedName);
			name = mappedName;
		}
		return name;
	}
}
