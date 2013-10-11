package org.dromelvan.tools;

import java.io.File;
import java.util.Set;

import org.dromelvan.tools.parser.MatchParserObject;
import org.dromelvan.tools.parser.MatchStatisticsParser;
import org.dromelvan.tools.parser.soccernet.SoccernetMatchStatisticsParser;
import org.dromelvan.tools.writer.MatchStatisticsJAXBFileWriter;
import org.dromelvan.tools.writer.MatchStatisticsWriter;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.io.Files;

@RunWith(JukitoRunner.class)
public class WriterTests {

    public static class Module extends JukitoModule {
        protected void configureTest() {
            bind(MatchStatisticsParser.class).to(SoccernetMatchStatisticsParser.class);
            bind(MatchStatisticsWriter.class).to(MatchStatisticsJAXBFileWriter.class);

            File dataDirectory = new File("add-data-dir-here");
            File soccernetDirectory = new File(dataDirectory, "soccernet/omg01");
            bindManyNamedInstances(File.class, "SoccernetFile",
                    new File(soccernetDirectory, "Ars-Ast.html"),
                    new File(soccernetDirectory, "Che-Hul.html"),
                    new File(soccernetDirectory, "Cry-Tot.html"),
                    new File(soccernetDirectory, "Liv-Sto.html"),
                    new File(soccernetDirectory, "MaC-New.html"),
                    new File(soccernetDirectory, "Nor-Eve.html"),
                    new File(soccernetDirectory, "Sun-Ful.html"),
                    new File(soccernetDirectory, "Swa-MaU.html"),
                    new File(soccernetDirectory, "WBA-Sou.html"),
                    new File(soccernetDirectory, "Wes-Car.html")
                    );

        }
    }

    @Test
    public void writeMatchStatisticsFile(@All("SoccernetFile") File inputFile, MatchStatisticsWriter writer, MatchStatisticsParser parser) {
        parser.setFile(inputFile);
        Set<MatchParserObject> matchParserObjects = parser.parse();

        File directory = inputFile.getParentFile();
        String inputFileExtension = Files.getFileExtension(inputFile.getName());
        File outputFile = new File(directory, inputFile.getName().replace(inputFileExtension, "xml"));

        writer.setFile(outputFile);
        writer.write(matchParserObjects.iterator().next());
    }
}
