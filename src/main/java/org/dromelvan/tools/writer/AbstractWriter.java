package org.dromelvan.tools.writer;

import org.dromelvan.tools.parser.old.ParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWriter<T extends ParserObject> implements Writer<T> {

    private final static Logger logger = LoggerFactory.getLogger(AbstractWriter.class);

    public AbstractWriter() {
        logger.debug("Using writer {}.", getClass().getSimpleName());
    }
}
