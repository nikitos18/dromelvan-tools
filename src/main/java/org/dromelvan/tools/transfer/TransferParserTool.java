package org.dromelvan.tools.transfer;

import org.dromelvan.tools.FileParserTool;
import org.dromelvan.tools.parser.transfer.TransferFileParser;
import org.dromelvan.tools.parser.transfer.TransferParserObject;
import org.dromelvan.tools.writer.jaxb.MatchStatisticsJAXBFileWriter;

import com.google.inject.Inject;

public class TransferParserTool extends FileParserTool<TransferFileParser, MatchStatisticsJAXBFileWriter, TransferParserObject> {

	@Inject
	public TransferParserTool(TransferFileParser parser, MatchStatisticsJAXBFileWriter writer) {
		super(parser, writer);
	}

}
