package org.dromelvan.tools.parser.whoscored.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerNameDictionary {

	private final static Map<Integer, String> dictionary = new HashMap<Integer, String>();

	public static void init(List<String> players) {
		for (int i = 0; i < players.size(); ++i) {
			String playerName = players.get(i);
			if (playerName != null) {
				dictionary.put(i, playerName);
			}
		}
	}

	public static String getName(int id) {
		return dictionary.get(id);
	}
}
