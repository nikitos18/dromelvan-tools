package org.dromelvan.tools.parser;

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
