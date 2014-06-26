package org.dromelvan.tools.parser.old;

import java.io.File;

import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.ParserProperties;

public abstract class AbstractFileParser<T extends ParserObject> extends AbstractParser<T> implements FileParser<T> {

	private File file;

	public AbstractFileParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	public File getFile() {
		return file;
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

}
