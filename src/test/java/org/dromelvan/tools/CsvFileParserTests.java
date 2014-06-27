package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;

import org.dromelvan.tools.parser.transfer.TransferFileParser;
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
			bindManyInstances(File.class, new File("src/test/resources/transfers.csv"));
			bindManyNamedInstances(File.class, "Transfers", new File("src/test/resources/transfers.csv"));
		}
	}

	@Test
	public void parseTransfersFileTest(@All("Transfers") File file, TransferFileParser transferFileParser) throws IOException {
		transferFileParser.setFile(file);
		transferFileParser.parse();
	}
}
