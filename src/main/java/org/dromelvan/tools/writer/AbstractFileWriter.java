package org.dromelvan.tools.writer;

import java.io.File;

import org.dromelvan.tools.parser.ParserObject;

public abstract class AbstractFileWriter<T extends ParserObject> extends AbstractWriter<T> implements FileWriter<T> {

	private File file;

	public File getFile() {
		return file;
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

}
