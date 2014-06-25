package org.dromelvan.tools.parser.old;

import java.util.Set;

import org.jsoup.nodes.Document;

public abstract class HTMLFileParser<T extends ParserObject> extends AbstractFileParser<T> {

	private Document document;

	public HTMLFileParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	@Override
	public Set<T> parse() {
		JSoupFileReader reader = new JSoupFileReader(getFile());
		setDocument(reader.read());
		Set<T> parserObjects = parseDocument();
		setParserObjects(parserObjects);
		return parserObjects;
	}

	protected abstract Set<T> parseDocument();

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
