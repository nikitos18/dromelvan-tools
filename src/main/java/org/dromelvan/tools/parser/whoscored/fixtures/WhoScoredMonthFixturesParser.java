package org.dromelvan.tools.parser.whoscored.fixtures;

import java.io.IOException;
import java.util.Set;

import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class WhoScoredMonthFixturesParser extends JSoupDocumentParser<ParserObject> {

	@Inject
	public WhoScoredMonthFixturesParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<ParserObject> parse() throws IOException {
		Element tableElement = getDocument().select("table#tournament-fixture").first();
		Elements tableRowElements = tableElement.getElementsByTag("tr");

		for (int i = 0; i < tableRowElements.size(); ++i) {
			Element tableRowElement = tableRowElements.get(i);

			if (tableRowElement.hasClass("rowgroupheader")) {
				System.out.println(tableRowElement.text());
			} else if (tableRowElement.hasClass("item")) {
				System.out.println(tableRowElement.text());
			}
		}

		return null;
	}

}
