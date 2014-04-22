package org.dromelvan.tools.parser.skysports;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.JSoupDocumentParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class SkySportsTeamPlayerIdParser extends JSoupDocumentParser<PlayerIdParserObject> {

	@Inject
	public SkySportsTeamPlayerIdParser(SkySportsProperties skySportsProperties) {
		super(skySportsProperties);
	}

	@Override
	public Set<PlayerIdParserObject> parse() {
		Set<PlayerIdParserObject> playerPhotoParserObjects = new HashSet<PlayerIdParserObject>();

		Pattern playerLinkPattern = Pattern.compile("/football/player/([\\d]*)/(.*)");

		Elements elements = getDocument().select("div.v5-squad-list");
		for (Element element : elements) {
			Elements playerElements = element.select("a");
			for (Element playerElement : playerElements) {
				Matcher matcher = playerLinkPattern.matcher(playerElement.attr("href"));
				if (matcher.matches()) {
					String name = playerElement.attr("title");
					String id = matcher.group(1);
					PlayerIdParserObject playerPhotoParserObject = new PlayerIdParserObject(name, id);
					playerPhotoParserObjects.add(playerPhotoParserObject);
				}
			}
		}

		return playerPhotoParserObjects;
	}
}
