package org.dromelvan.tools.whoscored;

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

}
