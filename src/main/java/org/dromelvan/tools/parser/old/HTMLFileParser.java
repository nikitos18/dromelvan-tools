package org.dromelvan.tools.parser.old;

import java.io.IOException;
import java.util.Set;

import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.ParserProperties;
import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.jsoup.nodes.Document;

public abstract class HTMLFileParser<T extends ParserObject> extends AbstractFileParser<T> {

	private Document document;

	public HTMLFileParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	@Override
	public Set<T> parse() throws IOException {
		JSoupFileReader reader = new JSoupFileReader(getFile());
		setDocument(reader.read());
		Set<T> parserObjects = parseDocument();
		setParserObjects(parserObjects);
		return parserObjects;
	}

	protected abstract Set<T> parseDocument() throws IOException;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
