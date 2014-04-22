package org.dromelvan.tools.parser.whoscored;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.JSoupDocumentParser;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Inject;

public class WhoScoredTeamStandingsParser extends JSoupDocumentParser<TeamStandingsParserObject> {

    @Inject
    public WhoScoredTeamStandingsParser(WhoScoredProperties whoScoredProperties) {
        super(whoScoredProperties);
    }

    @Override
    public Set<TeamStandingsParserObject> parse() {
        Set<TeamStandingsParserObject> teamStandingsParserObjects = new HashSet<TeamStandingsParserObject>();

        Elements scriptElements = getDocument().getElementsByTag("script");
        Pattern standingsPattern = Pattern.compile("(.*)DataStore.prime\\('standings', \\{ stageId: [\\d]* \\}, \\[(.*)\\]\\);(.*)DataStore.prime\\('forms',(.*)DataStore.prime\\('forms',(.*)", Pattern.DOTALL);
        Pattern teamPattern = Pattern.compile("\\[[\\d]*,([\\d]*),'([\\w ]*)',(.*)", Pattern.DOTALL);

        for(Element scriptElement : scriptElements) {
            for(DataNode dataNode : scriptElement.dataNodes()) {
                Matcher standingsMatcher = standingsPattern.matcher(dataNode.toString());
                if(standingsMatcher.matches()) {
                    String[] teams = standingsMatcher.group(2).split("\\n,");

                    for(String team : teams) {
                        Matcher teamMatcher = teamPattern.matcher(team);
                        if(teamMatcher.matches()) {
                            String id = teamMatcher.group(1);
                            String name = teamMatcher.group(2);
                            teamStandingsParserObjects.add(new TeamStandingsParserObject(id, name));
                        }
                    }
                }
            }
        }

        return teamStandingsParserObjects;
    }

}
