package org.dromelvan.tools.parser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupFileReader extends JSoupDocumentReader {

	private File file;

	public JSoupFileReader(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public Document read() {
		try {
			return Jsoup.parse(getFile(), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Parse failed for file " + getFile().getAbsolutePath() + ".", e);
		}

	}
}
