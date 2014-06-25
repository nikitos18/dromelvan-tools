package org.dromelvan.tools.parser.old.whoscored;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.old.JSoupDocumentParser;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class WhoScoredTeamFixturesParser extends JSoupDocumentParser<FixtureParserObject> {

    @Inject
    public WhoScoredTeamFixturesParser(WhoScoredProperties whoScoredProperties) {
        super(whoScoredProperties);
    }

    @Override
    public Set<FixtureParserObject> parse() {
        Set<FixtureParserObject> fixtureParserObjects = new HashSet<FixtureParserObject>();

        Elements scriptElements = getDocument().getElementsByTag("script");
        Pattern fixturesPattern = Pattern.compile("(.*)DataStore.prime\\('teamfixtures', \\$\\.extend\\(\\{ teamId: [\\d]* \\}, parametersCopy\\), \\[(.*)\\]\\);(.*)", Pattern.DOTALL);
        Pattern fixturePattern = Pattern.compile("\\[([\\d]*),[\\d]*,'([\\d]{2}-[\\d]{2}-[\\d]{4})','([\\d]{2}:[\\d]{2})',([\\d]*),'[\\w ]*',[\\d]*,([\\d]*),(.*),'EPL',(.*)\\](.*)", Pattern.DOTALL);

        for(Element scriptElement : scriptElements) {
            for(DataNode dataNode : scriptElement.dataNodes()) {
                Matcher fixturesMatcher = fixturesPattern.matcher(dataNode.toString());
                if(fixturesMatcher.matches()) {
                    String[] fixtures = fixturesMatcher.group(2).split("\\n,");

                    for(String fixture : fixtures) {
                        Matcher fixtureMatcher = fixturePattern.matcher(fixture);
                        if(fixtureMatcher.matches()) {
                            FixtureParserObject fixtureParserObject = new FixtureParserObject(Integer.parseInt(fixtureMatcher.group(1)),
                                                                                              Integer.parseInt(fixtureMatcher.group(4)),
                                                                                              Integer.parseInt(fixtureMatcher.group(5)),
                                                                                              fixtureMatcher.group(2),
                                                                                              fixtureMatcher.group(3));
                            fixtureParserObjects.add(fixtureParserObject);
                        }
                    }
                }
            }
        }

        return fixtureParserObjects;
    }

}
