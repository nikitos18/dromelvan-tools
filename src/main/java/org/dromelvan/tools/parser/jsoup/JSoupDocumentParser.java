package org.dromelvan.tools.parser.jsoup;

import org.dromelvan.tools.parser.AbstractParser;
import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.ParserProperties;
import org.jsoup.nodes.Document;

public abstract class JSoupDocumentParser<T extends ParserObject> extends AbstractParser<T> {

	private Document document;

	public JSoupDocumentParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
