package org.dromelvan.tools.parser.trade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.csv.CsvFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TradeFileParser extends CsvFileParser<TradeParserObject> {

	private final Pattern tradePattern = Pattern.compile("(.*),(.*),(.*),(.*)", Pattern.DOTALL);
	private final static Logger logger = LoggerFactory.getLogger(TradeFileParser.class);

	@Inject
	public TradeFileParser(TradeProperties tradeProperties) {
		super(tradeProperties);
	}

	@Override
	protected TradeParserObject parseLine(String line) {
		logger.debug("Parsing line {}.", line);

		Matcher matcher = tradePattern.matcher(line);
		if (matcher.matches()) {
			try {
				int fee = Integer.parseInt(matcher.group(4).replace(".", "").trim());
				TradeParserObject tradeParserObject = new TradeParserObject(matcher.group(1).trim(), matcher.group(2).trim(), matcher.group(3).trim(), fee);
				return tradeParserObject;
			} catch (NumberFormatException e) {
				logger.error("Invalid fee: {}.", matcher.group(4));
			}
		} else {
			logger.error("Line does not match trade pattern: {}.", line);
		}
		return null;
	}

}
