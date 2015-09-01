package org.dromelvan.tools.parser.skysports;

import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;
import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class SkySportsTeamLinkParser extends JSoupDocumentParser<TeamLinkParserObject, JavaScriptVariables> {

	@Inject
	public SkySportsTeamLinkParser(SkySportsProperties skySportsProperties) {
		super(skySportsProperties);
	}

	@Override
	public Set<TeamLinkParserObject> parse() {
		Set<TeamLinkParserObject> teamLinkParserObjects = new HashSet<TeamLinkParserObject>();

		Elements optGroupElements = getDocument().select("optgroup");
		for (Element optGroupElement : optGroupElements) {
			String label = optGroupElement.attr("label");
			if (label.equals("Premier League") || label.equals("Championship")) {
				System.out.println("Parsing: " + label);
				Elements optionElements = optGroupElement.select("option");
				for (Element optionElement : optionElements) {
					String name = optionElement.text();
					String link = optionElement.attr("value");
					TeamLinkParserObject teamLinkParserObject = new TeamLinkParserObject(name, link);
					teamLinkParserObjects.add(teamLinkParserObject);
				}
			}
		}
		return teamLinkParserObjects;
	}
}
