package org.dromelvan.tools.parser.whoscored.match;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.FileParser;
import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredMatchParser extends JSoupDocumentParser<MatchParserObject, WhoScoredMatchJavaScriptVariables> implements FileParser<MatchParserObject> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchParser.class);

	@Inject
	public WhoScoredMatchParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public void setFile(File file) {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		try {
			setDocument(jSoupFileReader.read());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<MatchParserObject> parse() {
		WhoScoredMatchJavaScriptVariables whoScoredMatchEventsJavaScriptVariables = getJavaScriptVariables();

		WhoScoredMatchParserObject matchParserObject = whoScoredMatchEventsJavaScriptVariables.getMatchParserObject();

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}
}
