package org.dromelvan.tools.parser.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.AbstractParser;
import org.dromelvan.tools.parser.FileParser;
import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.ParserProperties;

public abstract class CsvFileParser<T extends ParserObject> extends AbstractParser<T> implements FileParser<T> {

	private File file;

	public CsvFileParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	public File getFile() {
		return file;
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public Set<T> parse() throws IOException {
		Set<T> parserObjects = new HashSet<T>();
		BufferedReader reader = new BufferedReader(new FileReader(getFile()));

		String line = reader.readLine();

		while (line != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				T parserObject = parseLine(line);
				if (parserObject != null) {
					parserObjects.add(parseLine(line));
				}
			}
			line = reader.readLine();
		}

		reader.close();
		return parserObjects;
	}

	protected abstract T parseLine(String line);

}
