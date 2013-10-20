package org.dromelvan.tools.parser;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractParser<T extends ParserObject> implements Parser<T> {

    private ParserProperties parserProperties;
    private Set<T> parserObjects;
    private final static Logger logger = LoggerFactory.getLogger(AbstractParser.class);

    public AbstractParser(ParserProperties parserProperties) {
        logger.debug("Using parser {}.", getClass().getSimpleName());
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
