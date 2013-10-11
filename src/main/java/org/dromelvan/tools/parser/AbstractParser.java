package org.dromelvan.tools.parser;

import java.util.Set;


public abstract class AbstractParser<T extends ParserObject> implements Parser<T> {

    private ParserProperties parserProperties;
    private Set<T> parserObjects;
    
    public AbstractParser(ParserProperties parserProperties) {
        this.parserProperties = parserProperties;
    }
    
    public ParserProperties getParserProperties() {
        return parserProperties;
    }
    public void setParserProperties(ParserProperties parserProperties) {
        this.parserProperties = parserProperties;
    }

    public Set<T> getParserObjects() {
        return parserObjects;
    }
    public void setParserObjects(Set<T> parserObjects) {
        this.parserObjects = parserObjects;
    }
    
}
