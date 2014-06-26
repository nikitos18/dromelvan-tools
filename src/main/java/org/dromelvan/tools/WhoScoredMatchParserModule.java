package org.dromelvan.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class WhoScoredMatchParserModule extends AbstractModule {

    private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchParserModule.class);

    @Override
    public void configure() {
        logger.debug("Configuring {}.", getClass().getSimpleName());
    }
}
