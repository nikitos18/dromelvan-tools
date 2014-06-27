package org.dromelvan.tools.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class TransferParserModule extends AbstractModule {

    private final static Logger logger = LoggerFactory.getLogger(TransferParserModule.class);

    @Override
    public void configure() {
        logger.debug("Configuring {}.", getClass().getSimpleName());
    }
}
