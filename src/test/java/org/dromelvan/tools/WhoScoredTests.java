package org.dromelvan.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.dromelvan.tools.parser.MatchParserObject;
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
    
    //@Test
    public void foo() throws Exception {
        String data = null;
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line.replace("\n",""));
                line = br.readLine();
            }
            data = sb.toString();
        } finally {
            br.close();
        }
        
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        
        String convertFuncSrc =
                "function convertArray(arr) {"
              + "  var jArr = java.lang.reflect.Array.newInstance(java.lang.String, arr.length);"
              + "  for (var i = 0; i < arr.length; i++) { "
              + "    jArr[i] = arr[i];"
              + "  }"
              + "  return jArr;"
              + "};";
        engine.eval(convertFuncSrc);
           
        // evaluate JavaScript code from String
        Object result = engine.eval("convertArray(" + data + ");");
        for(String str : ((String[])result)) {
            System.out.println(str.length());
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
        Pattern pattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], 0\\] ;(.*)", Pattern.DOTALL);
        for(Element scriptElement : scriptElements) {
            for(DataNode node : scriptElement.dataNodes()) {
                Matcher matcher = pattern.matcher(node.toString());
                if(matcher.matches()) {
                    String bar = matcher.group(3);
                    System.out.println(bar.length());
                    String[] foo = bar.split("\n,");
                    
                    for(String foobar : foo) {
                        if(foobar.length() > 50)
                        System.out.println(foobar + "\n");
                    }
                    
                }
            }
        }        
    }
    
    public void downloadTest2() throws IOException {
        Document doc = Jsoup.connect("http://www.whoscored.com/Matches/720016/LiveStatistics/England-Premier-League-2013-2014-Manchester-City-Everton")
                                .data("query", "Java")
                                .userAgent("Chrome")
                                .timeout(3000)                                
                                .get();
        
        Elements scriptElements = doc.getElementsByTag("script");
        Pattern pattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], 0\\] ;(.*)", Pattern.DOTALL);
        for(Element scriptElement : scriptElements) {
            for(DataNode node : scriptElement.dataNodes()) {
                Matcher matcher = pattern.matcher(node.toString());
                if(matcher.matches()) {
                    String bar = matcher.group(3).replace("\n", "");
//                    String[] xs = bar.split(", \\[", 2);
//                    System.out.println(xs[0]);
                    
                    MatchParserObject matchParserObject = null;
                    
                    int depth = 0;
                    String row = "";
                    for(int i = 0; i < bar.length(); ++i) {
                        char c = bar.charAt(i);                        
                        if(c == '[') {
                            depth++;
//                            System.out.print("\n" + depth + ": " + c + "\n");
                            //row += (depth <= 4 ? "\n" + depth + ": ": "");
                            if(depth == 1) {
                                row += "\nMatch: ";
                            } else if(depth == 2) {
                                row += "\nTeam: ";
                            } else if(depth == 4) {
                                row += "\nPlayer: ";
                            }
                        } else if(c == ']') {                         
//                            System.out.print("\n" + depth + ": " + c + "\n");
                            if(depth <= 4) {
                                row += "\n";
                                //if(row.length() > 100)
                                //System.out.println(row.substring(0, 100));
                                  
                                if(depth == 1 && matchParserObject == null) {
                                    matchParserObject = new MatchParserObject();
                                    System.out.println(matchParserObject);
                                } 
                                if(depth == 2) {
                                    System.out.println(row);
                                }
                                row = "";
                            }
                            //System.out.println();
                            depth--;
                        } else {
                            //System.out.print(c);
                            row += c;
                        }
                    }
                }
            }
        }        
    }
    
}
