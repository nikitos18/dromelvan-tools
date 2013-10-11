package org.dromelvan.tools.parser;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class HTMLFileParser<T extends ParserObject> extends AbstractFileParser<T> {

    private Document document;

    public HTMLFileParser(ParserProperties parserProperties) {
        super(parserProperties);
    }

    @Override
    public Set<T> parse() {
        try {
            this.document = Jsoup.parse(getFile(), "UTF-8");
            Set<T> parserObjects = parseDocument();
            setParserObjects(parserObjects);
            return parserObjects;
        } catch(IOException e) {
            throw new RuntimeException("Parse failed for file " + getFile().getAbsolutePath() + ".", e);
        }
    }

    protected abstract Set<T> parseDocument();

    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }

}
