package org.dromelvan.tools;

import org.dromelvan.tools.parser.FileParser;
import org.dromelvan.tools.parser.soccernet.SoccernetMatchStatisticsParser;
import org.dromelvan.tools.writer.FileWriter;
import org.dromelvan.tools.writer.MatchStatisticsJAXBFileWriter;

import com.google.inject.AbstractModule;

public class MatchStatisticsFileParserModule extends AbstractModule {

    @Override
    public void configure() {
        bind(FileParser.class).to(SoccernetMatchStatisticsParser.class);
        bind(FileWriter.class).to(MatchStatisticsJAXBFileWriter.class);
    }
}
