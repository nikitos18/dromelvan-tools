package org.dromelvan.tools.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class TradeParserModule extends AbstractModule {

    private final static Logger logger = LoggerFactory.getLogger(TradeParserModule.class);

    @Override
    public void configure() {
        logger.debug("Configuring {}.", getClass().getSimpleName());
    }
}
