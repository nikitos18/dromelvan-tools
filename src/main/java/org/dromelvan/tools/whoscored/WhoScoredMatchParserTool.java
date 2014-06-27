package org.dromelvan.tools.whoscored;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.dromelvan.tools.FileParserTool;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.whoscored.match.WhoScoredMatchParser;
import org.dromelvan.tools.writer.jaxb.MatchStatisticsJAXBFileWriter;

import com.google.inject.Inject;

public class WhoScoredMatchParserTool extends FileParserTool<WhoScoredMatchParser, MatchStatisticsJAXBFileWriter, MatchParserObject> {

	@Inject
	public WhoScoredMatchParserTool(WhoScoredMatchParser parser, MatchStatisticsJAXBFileWriter writer) {
		super(parser, writer);
	}

	@Override
	protected Set<MatchParserObject> parseFile(File file) throws IOException {
		getParser().setFile(file);
		Set<MatchParserObject> matchParserObjects = getParser().parse();
		return matchParserObjects;
	}

}
