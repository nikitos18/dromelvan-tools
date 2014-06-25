package org.dromelvan.tools.parser.old.skysports;

import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.old.JSoupDocumentParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class SkySportsTeamLinkParser extends JSoupDocumentParser<TeamLinkParserObject> {

	@Inject
	public SkySportsTeamLinkParser(SkySportsProperties skySportsProperties) {
		super(skySportsProperties);
	}

	@Override
	public Set<TeamLinkParserObject> parse() {
		Set<TeamLinkParserObject> teamLinkParserObjects = new HashSet<TeamLinkParserObject>();

		Elements elements = getDocument().select("div.-3cols");
		Elements teamLinks = elements.first().select("a");

		for (Element teamLink : teamLinks) {
			String name = teamLink.text();
			String link = teamLink.attr("href");
			TeamLinkParserObject teamLinkParserObject = new TeamLinkParserObject(name, link);
			teamLinkParserObjects.add(teamLinkParserObject);
		}
		return teamLinkParserObjects;
	}
}
