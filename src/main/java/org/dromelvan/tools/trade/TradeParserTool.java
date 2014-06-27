package org.dromelvan.tools.trade;

import org.dromelvan.tools.FileParserTool;
import org.dromelvan.tools.parser.trade.TradeFileParser;
import org.dromelvan.tools.parser.trade.TradeParserObject;
import org.dromelvan.tools.writer.jaxb.TradesJAXBFileWriter;

import com.google.inject.Inject;

public class TradeParserTool extends FileParserTool<TradeFileParser, TradesJAXBFileWriter, TradeParserObject> {

	@Inject
	public TradeParserTool(TradeFileParser parser, TradesJAXBFileWriter writer) {
		super(parser, writer);
	}

}
