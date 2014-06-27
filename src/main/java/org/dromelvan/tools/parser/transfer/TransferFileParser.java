package org.dromelvan.tools.parser.transfer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.csv.CsvFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TransferFileParser extends CsvFileParser<TransferParserObject> {

	private final Pattern transferPattern = Pattern.compile("(.*),(.*),(.*),(.*)", Pattern.DOTALL);
	private final static Logger logger = LoggerFactory.getLogger(TransferFileParser.class);

	@Inject
	public TransferFileParser(TransferProperties transferProperties) {
		super(transferProperties);
	}

	@Override
	protected TransferParserObject parseLine(String line) {
		logger.debug("Parsing line {}.", line);

		Matcher matcher = transferPattern.matcher(line);
		if (matcher.matches()) {
			try {
				int fee = Integer.parseInt(matcher.group(4).replace(".", "").trim());
				TransferParserObject transferParserObject = new TransferParserObject(matcher.group(1), matcher.group(2), matcher.group(3), fee);
				return transferParserObject;
			} catch (NumberFormatException e) {
				logger.error("Invalid fee: {}.", matcher.group(4));
			}
		} else {
			logger.error("Line does not match transfer pattern: {}.", line);
		}
		return null;
	}

}
