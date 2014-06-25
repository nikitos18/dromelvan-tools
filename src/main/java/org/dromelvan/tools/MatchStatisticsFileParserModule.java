package org.dromelvan.tools;

import org.dromelvan.tools.parser.old.FileParser;
import org.dromelvan.tools.parser.old.soccernet.SoccernetMatchStatisticsParser;
import org.dromelvan.tools.writer.FileWriter;
import org.dromelvan.tools.writer.MatchStatisticsJAXBFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class MatchStatisticsFileParserModule extends AbstractModule {

    private final static Logger logger = LoggerFactory.getLogger(MatchStatisticsFileParserModule.class);

    @Override
    public void configure() {
        logger.debug("Configuring {}.", getClass().getSimpleName());
        bind(FileParser.class).to(SoccernetMatchStatisticsParser.class);
        bind(FileWriter.class).to(MatchStatisticsJAXBFileWriter.class);
    }
}
