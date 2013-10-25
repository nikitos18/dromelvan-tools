package org.dromelvan.tools;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class WhoScoredTests {

    public static class Module extends JukitoModule {
        protected void configureTest() {
        }
    }
    
    @Test
    public void downloadTest() throws IOException {
        Document doc = Jsoup.connect("http://www.whoscored.com/Matches/720016/LiveStatistics/England-Premier-League-2013-2014-Manchester-City-Everton")
                                .data("query", "Java")
                                .userAgent("Chrome")
                                .timeout(3000)                                
                                .get();
        
        Elements scriptElements = doc.getElementsByTag("script");
        Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], 0\\] ;(.*)", Pattern.DOTALL);
        Pattern fixturePattern = Pattern.compile("\\[\\d*,\\d*,'([\\w ]*)','([\\w ]*)',.*", Pattern.DOTALL);
        Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[(\\[.*)", Pattern.DOTALL);
        Pattern playerPattern = Pattern.compile("\\[\\d*,'(.*)',[\\d\\.]*,\\[\\[(.*)\\]\\],\\d*,'([\\w\\(\\)]*)',\\d*,\\d*,(\\d*),'([\\w\\(\\),]*)',.*\\]", Pattern.DOTALL);
        
        
        
        Pattern firstElementPattern = Pattern.compile("\\[\\d*,'(.*)',(.*\\]\\]\\]\\]),\\[(\\[.*)", Pattern.DOTALL);
        for(Element scriptElement : scriptElements) {
            for(DataNode node : scriptElement.dataNodes()) {
                Matcher scriptMatcher = scriptPattern.matcher(node.toString());
                if(scriptMatcher.matches()) {
                    String[] scriptVariables = scriptMatcher.group(3).split("\n,");                    
                    for(String scriptVariable : scriptVariables) {
                        scriptVariable = scriptVariable.trim();
                        Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
                        if(fixtureMatcher.matches()) {
                            //System.out.println(fixtureMatcher.group(1) + " vs " + fixtureMatcher.group(2));
                            continue;
                        }
                        Matcher teamMatcher = teamPattern.matcher(scriptVariable);                        
                        if(teamMatcher.matches()) {
                            System.out.println(teamMatcher.group(1) + "\n");
                            scriptVariable = teamMatcher.group(2);
                        }
                        
                        Matcher playerMatcher = playerPattern.matcher(scriptVariable);
                        if(playerMatcher.matches()) {
                            //System.out.println(scriptVariable + "\n");
                            System.out.println(playerMatcher.group(1) + " " + playerMatcher.group(3) + " " + playerMatcher.group(4) + " " + playerMatcher.group(5));
                            String[] playerMatchStatistics = playerMatcher.group(2).replace("]],","]];").split(";");
                            for(String kek : playerMatchStatistics) {
                                System.out.println(kek);
                            }
                        }
                        
                        
                        Matcher firstElementMatcher = firstElementPattern.matcher(scriptVariable);
                        if(scriptVariable.length() > 50) {
                            //System.out.println(scriptVariable + "\n");
//                            if(firstElementMatcher.matches()) {
//                                System.out.println(firstElementMatcher.group(1) + "\n");
//                                System.out.println(firstElementMatcher.group(2) + "\n");
//                            } else {
//                                System.out.println(scriptVariable + "\n");
//                            }
                        }
                    }
                    
                }
            }
        }        
    }        
}
