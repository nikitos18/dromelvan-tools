package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;

import org.dromelvan.tools.parser.trade.TradeFileParser;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class CsvFileParserTests {

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			bindManyNamedInstances(File.class, "Trades", new File("src/test/resources/trades.csv"));
		}
	}

	@Test
	public void parseTradesFileTest(@All("Trades") File file, TradeFileParser tradesFileParser) throws IOException {
		tradesFileParser.setFile(file);
		tradesFileParser.parse();
	}
}
