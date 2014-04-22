package org.dromelvan.tools.parser.whoscored;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.JSoupDocumentParser;
import org.dromelvan.tools.parser.PlayerInformationParserObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class WhoScoredPlayerPageParser extends JSoupDocumentParser<PlayerInformationParserObject> {

	@Inject
	public WhoScoredPlayerPageParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<PlayerInformationParserObject> parse() {

		Set<PlayerInformationParserObject> playerInformationParserObjects = new HashSet<PlayerInformationParserObject>();

		Map<String, String> valueMap = new HashMap<String, String>();

		Elements playerProfileElements = getDocument().select("div#player-profile");
		Elements playerInfoBlockElements = playerProfileElements.select("dl.player-info-block");

		for (Element playerInfoBlockElement : playerInfoBlockElements) {
			valueMap.put(playerInfoBlockElement.select("dt").text(), playerInfoBlockElement.select("dd").text());
		}

		PlayerInformationParserObject playerInformationParserObject = new PlayerInformationParserObject();
		playerInformationParserObject.setName(valueMap.get("Name:"));
		playerInformationParserObject.setNationality(valueMap.get("Nationality:"));

		String age = valueMap.get("Age:");
		if (age != null) {
			Pattern agePattern = Pattern.compile("(.*) years old \\((.*)-(.*)-(.*)\\)");
			Matcher ageMatcher = agePattern.matcher(age);
			if (ageMatcher.matches()) {
				playerInformationParserObject.setDateOfBirth(ageMatcher.group(4) + "-" + ageMatcher.group(3) + "-" + ageMatcher.group(2));
			}
		}

		String height = valueMap.get("Height:");
		if (height != null) {
			Pattern heightPattern = Pattern.compile("(.*)cm");
			Matcher heightMatcher = heightPattern.matcher(height);
			if (heightMatcher.matches()) {
				playerInformationParserObject.setHeight(Integer.parseInt(heightMatcher.group(1)));
			}
		}

		String weight = valueMap.get("Weight:");
		if (weight != null) {
			Pattern weightPattern = Pattern.compile("(.*)kg");
			Matcher weightMatcher = weightPattern.matcher(weight);
			if (weightMatcher.matches()) {
				playerInformationParserObject.setWeight(Integer.parseInt(weightMatcher.group(1)));
			}
		}

		String shirtNumber = valueMap.get("Shirt Number:");
		if (shirtNumber != null) {
			playerInformationParserObject.setShirtNumber(Integer.parseInt(shirtNumber));
		}

		playerInformationParserObject.setPositions(valueMap.get("Positions:"));
		playerInformationParserObject.setFullName(valueMap.get("Full Name:"));

		playerInformationParserObjects.add(playerInformationParserObject);
		return playerInformationParserObjects;
	}
}
