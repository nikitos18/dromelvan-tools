package org.dromelvan.tools.parser.jsoup;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupFileReader implements JSoupDocumentReader {

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
	public Document read() throws IOException {
		return Jsoup.parse(getFile(), "UTF-8");
	}
}
