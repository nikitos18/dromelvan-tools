package org.dromelvan.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.JSoupFileReader;
import org.dromelvan.tools.parser.JSoupURLReader;
import org.dromelvan.tools.parser.PlayerInformationParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredPlayerPageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredPlayerPageParserTests {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredPlayerPageParserTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			try {
				bindManyInstances(URL.class, new URL("http://www.whoscored.com/Players/22221/"),
						new URL("http://www.whoscored.com/Players/22222/"),
						new URL("http://www.whoscored.com/Players/22223/"));
				bindManyInstances(File.class, new File("src/test/resources/Luis Suarez Football Statistics   WhoScored.com.htm"));
			} catch (MalformedURLException e) {
			}
		}
	}

	@Test
	public void createSeed() throws IOException {
	    File input = new File("src/test/resources/players.txt");
	    BufferedReader reader = new BufferedReader(new FileReader(input));

        File output = new File("output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));

	    String line = reader.readLine();
	    while(line != null) {
	        String[] data = line.split(";");
	        String firstName = data[1];
	        String lastName = data[2];
	        String country = data[3];
	        String dob = data[4];
	        String height = data[5];
	        String weight = data[6];
	        String fullName = data[9];
	        String whoScoredId = data[10];


	        if(fullName.equals("null")) {
	            fullName = "";
	        }
	        if(whoScoredId.equals("null")) {
	            whoScoredId = "0";
	        }

	        String outputLine = String.format("  [ \"%s\", \"%s\", \"%s\", \"%s\", %s, %s, \"%s\", %s,],", firstName, lastName, country, dob, height, weight, fullName, whoScoredId);
	        System.out.println(outputLine);
	        writer.write(outputLine + "\n");
	        writer.flush();

	        line = reader.readLine();
	    }

	    reader.close();
	    writer.close();
	}

	// @Test
	public void fileTest(@All File file, WhoScoredPlayerPageParser parser) throws IOException {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		parser.setDocument(jSoupFileReader.read());
		Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

		logger.info("Information for {}:", file.getName());
		for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
			logger.info("{}", playerInformationParserObject);
		}
	}

	// @Test
	public void urlTest(@All URL url, WhoScoredPlayerPageParser parser) throws IOException {
		JSoupURLReader jSoupFileReader = new JSoupURLReader(url);
		parser.setDocument(jSoupFileReader.read());
		Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

		logger.info("Information for {}:", url);
		for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
			logger.info("{}", playerInformationParserObject);
		}
	}

	//@Test
	public void fileTest(WhoScoredPlayerPageParser parser) throws IOException {
        File playersFile = new File("src/test/resources/players.txt");
        BufferedReader reader = new BufferedReader(new FileReader(playersFile));
        String player = reader.readLine();

        int i = 1;

        while(player != null) {
            String[] split = player.split(";");

            if(Integer.parseInt(split[0]) != i) {
                System.out.println(player);
            }

            ++i;
            player = reader.readLine();
        }
        reader.close();
	}

	//@Test
	public void searchTest2(WhoScoredPlayerPageParser parser) throws IOException {
        File playersFile = new File("src/test/resources/players.txt");
        BufferedReader reader = new BufferedReader(new FileReader(playersFile));
        String player = reader.readLine();

        File output = new File("players.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        File failed = new File("failed.txt");
        BufferedWriter failedWriter = new BufferedWriter(new FileWriter(failed));

        int i = 1;

        while(player != null) {
            if(player.indexOf("NO_ID") < 0) {
                writer.write(player + "\n");
                writer.flush();

                ++i;
                player = reader.readLine();
                continue;
            }

            System.out.println(player);

            String[] split = player.split(";");
            if(split.length > 3) {
                String id = split[0];
                String firstName = split[1];
                String lastName = split[2];
                String name = (firstName + " " + lastName).trim();

                try {
                    URL url = new URL("http://www.whoscored.com/Search/?t=" + name.replace(' ', '+'));
                    JSoupURLReader jSoupURLReader = new JSoupURLReader(url);
                    Document document = jSoupURLReader.read();

                    Elements aElements = document.select("div.search-result").select("a");

                    if(!aElements.isEmpty()) {
                        for(Element aElement : aElements) {
                            if(aElement.text().equalsIgnoreCase(name)) {
                                URL playerURL = new URL("http://www.whoscored.com" + aElement.attr("href"));
                                JSoupURLReader jSoupURLReader2 = new JSoupURLReader(playerURL);
                                parser.setDocument(jSoupURLReader2.read());
                                Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

                                for(PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
                                    logger.info("{} {}", i, playerInformationParserObject);
                                    writer.write(id + ";" +
                                            firstName + ";" +
                                            lastName + ";" +
                                            playerInformationParserObject.getNationality() + ";" +
                                            playerInformationParserObject.getDateOfBirth() + ";" +
                                            playerInformationParserObject.getHeight() + ";" +
                                            playerInformationParserObject.getWeight() + ";" +
                                            playerInformationParserObject.getShirtNumber() + ";" +
                                            playerInformationParserObject.getPositions() + ";" +
                                            playerInformationParserObject.getFullName() + "\n");
                                    writer.flush();

                                    String whoScoredId = playerURL.toString().replace("http://www.whoscored.com/Players/","").replace("/","");
                                    URL imageUrl = new URL("http://164.177.157.12/img/players/" + whoScoredId + ".jpg");

                                    try {
                                        InputStream in = imageUrl.openStream();
                                        OutputStream out = new BufferedOutputStream(new FileOutputStream("whoscored-player-photos/" + playerInformationParserObject.getName() + " (" + whoScoredId + ").jpg"));
                                        for (int b; (b = in.read()) != -1;) {
                                            out.write(b);
                                        }
                                        out.close();
                                        in.close();
                                    } catch(Exception e) {
                                        // We don't care
                                    }
                                }
                            }
                        }
                    } else {
                        logger.error("Could not find: {}", name);
                        writer.write(id + ";" +
                                     firstName + ";" +
                                     lastName + ";" +
                                     "Unknown;" +
                                     null + ";" +
                                     "0;0;0;" +
                                     null + ";" +
                                     null + ";" +
                                     " * MISSING *\n");
                        failedWriter.write(name + "\n");
                    }
                    Thread.sleep(2000);

                } catch(Exception e) {
                    e.printStackTrace();
                }

            } else {
                logger.info("{} {}", i, player);
                writer.write(player + "\n");
                writer.flush();
            }

            ++i;
            player = reader.readLine();
        }

        reader.close();
//        writer.close();
//        failedWriter.flush();
//        failedWriter.close();

	}

	//@Test
	public void searchTest(WhoScoredPlayerPageParser parser) throws IOException {
        File output = new File("players.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        File failed = new File("failed.txt");
        BufferedWriter failedWriter = new BufferedWriter(new FileWriter(failed));

        int i = 0;
	    File playerDumpFile = new File("src/test/resources/player_dump.txt");
	    BufferedReader reader = new BufferedReader(new FileReader(playerDumpFile));
	    String playerDump = reader.readLine();

	    while(playerDump != null) {
	        ++i;
	        playerDump = playerDump.replaceAll("\"", "");
	        String[] values = playerDump.split(";");
	        String id = values[0];
	        String firstName = values[1];
	        String lastName = values[2];
	        String name = (firstName + " " + lastName).trim();

	        try {
    	        URL url = new URL("http://www.whoscored.com/Search/?t=" + name.replace(' ', '+'));
    	        JSoupURLReader jSoupURLReader = new JSoupURLReader(url);
    	        Document document = jSoupURLReader.read();

    	        Elements aElements = document.select("div.search-result").select("a");

    	        if(!aElements.isEmpty()) {
    	            for(Element aElement : aElements) {
    	                if(aElement.text().equalsIgnoreCase(name)) {
    	                    URL playerURL = new URL("http://www.whoscored.com" + aElement.attr("href"));
    	                    JSoupURLReader jSoupURLReader2 = new JSoupURLReader(playerURL);
    	                    parser.setDocument(jSoupURLReader2.read());
    	                    Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

    	                    for(PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
    	                        logger.info("{} {}", i, playerInformationParserObject);
    	                        writer.write(id + ";" +
    	                                firstName + ";" +
    	                                lastName + ";" +
    	                                playerInformationParserObject.getNationality() + ";" +
    	                                playerInformationParserObject.getDateOfBirth() + ";" +
    	                                playerInformationParserObject.getHeight() + ";" +
    	                                playerInformationParserObject.getWeight() + ";" +
    	                                playerInformationParserObject.getShirtNumber() + ";" +
    	                                playerInformationParserObject.getPositions() + ";" +
    	                                playerInformationParserObject.getFullName() + "\n");

    	                        String whoScoredId = playerURL.toString().replace("http://www.whoscored.com/Players/","").replace("/","");
    	                        URL imageUrl = new URL("http://164.177.157.12/img/players/" + whoScoredId + ".jpg");

    	                        try {
        	                        InputStream in = imageUrl.openStream();
        	                        OutputStream out = new BufferedOutputStream(new FileOutputStream("whoscored-player-photos/" + playerInformationParserObject.getName() + " (" + whoScoredId + ").jpg"));
        	                        for (int b; (b = in.read()) != -1;) {
        	                            out.write(b);
        	                        }
        	                        out.close();
        	                        in.close();
    	                        } catch(Exception e) {
    	                            // We don't care
    	                        }
    	                    }
    	                }
    	            }
    	        } else {
                    logger.error("Could not find: {}", name);
                    writer.write(id + ";" +
                                 firstName + ";" +
                                 lastName + ";" +
                                 "Unknown;" +
                                 null + ";" +
                                 "0;0;0;" +
                                 null + ";" +
                                 null + ";" +
                                 " * MISSING *\n");
                    failedWriter.write(name + "\n");
    	        }
    	        Thread.sleep(2000);
	        } catch(Exception e) {
                logger.error("Failed for {}:", name);
                logger.error("Exception:", e);
                writer.write(playerDump + " * FAILED *\n");
                failedWriter.write(name + "\n");
	        }
	        //if(i > 20) break;
	        playerDump = reader.readLine();
	    }
	    reader.close();
        writer.flush();
        writer.close();
        failedWriter.flush();
        failedWriter.close();
	}

    //@Test
    public void fetchIdTest(WhoScoredPlayerPageParser parser) throws IOException {
        File output = new File("whoscored_ids.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        File failed = new File("failed_ids.txt");
        BufferedWriter failedWriter = new BufferedWriter(new FileWriter(failed));

        int i = 0;
        File playerDumpFile = new File("src/test/resources/players.txt");
        BufferedReader reader = new BufferedReader(new FileReader(playerDumpFile));
        String playerDump = reader.readLine();

        int not_found = 0;
        int found_count = 0;

        while(playerDump != null) {
            ++i;
            if(playerDump.indexOf("NO_ID") < 0) {
                writer.write(playerDump + "\n");
                writer.flush();

                playerDump = reader.readLine();
                continue;
            }

            String[] values = playerDump.split(";");
            String id = values[0];
            String firstName = values[1];
            String lastName = values[2];
            String name = (firstName + " " + lastName).trim();

            try {
                URL url = new URL("http://www.whoscored.com/Search/?t=" + name.replace(' ', '+'));
                JSoupURLReader jSoupURLReader = new JSoupURLReader(url);
                Document document = jSoupURLReader.read();

                Elements aElements = document.select("div.search-result").select("a");
                int count = 0;
                for(Element e : aElements) {
                    if(e.text().equalsIgnoreCase(name)) {
                        ++count;
                    }
                }

                if(count == 1) {

                    boolean found = false;

                    for(Element aElement : aElements) {
                        if(aElement.text().equalsIgnoreCase(name)) {
                            URL playerURL = new URL("http://www.whoscored.com" + aElement.attr("href"));
                            JSoupURLReader jSoupURLReader2 = new JSoupURLReader(playerURL);
                            parser.setDocument(jSoupURLReader2.read());
                            Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

                            for(PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
                                String whoScoredId = playerURL.toString().replace("http://www.whoscored.com/Players/","").replace("/","");

                                logger.info("{} {}", i, playerInformationParserObject);
                                writer.write(id + ";" +
                                        firstName + ";" +
                                        lastName + ";" +
                                        playerInformationParserObject.getNationality() + ";" +
                                        playerInformationParserObject.getDateOfBirth() + ";" +
                                        playerInformationParserObject.getHeight() + ";" +
                                        playerInformationParserObject.getWeight() + ";" +
                                        playerInformationParserObject.getShirtNumber() + ";" +
                                        playerInformationParserObject.getPositions() + ";" +
                                        playerInformationParserObject.getFullName() + ";" +
                                        whoScoredId + "\n");
                                writer.flush();
                                found = true;
                                found_count++;
                            }
                            if(found) break;
                        }
                    }

                    if(!found) {
                        logger.error("Could not find: {}", name);
                        writer.write(id + ";" +
                                     firstName + ";" +
                                     lastName + ";" +
                                     "Unknown;" +
                                     null + ";" +
                                     "0;0;0;" +
                                     null + ";" +
                                     null + ";" +
                                     "NO_ID\n");
                        ++not_found;
                        writer.flush();
                        failedWriter.write(name + "\n");
                    }

                } else {
                    ++not_found;
                    logger.error("Could not find: {}", name);
                    writer.write(id + ";" +
                                 firstName + ";" +
                                 lastName + ";" +
                                 "Unknown;" +
                                 null + ";" +
                                 "0;0;0;" +
                                 null + ";" +
                                 null + ";" +
                                 " * MISSING *;" +
                                 "NO_ID\n");
                    writer.flush();
                    failedWriter.write(name + "\n");
                }
                Thread.sleep(2000);
            } catch(Exception e) {
                ++not_found;
                logger.error("Failed for {}:", name);
                logger.error("Exception:", e);
                writer.write(id + ";" +
                        firstName + ";" +
                        lastName + ";" +
                        "Unknown;" +
                        null + ";" +
                        "0;0;0;" +
                        null + ";" +
                        null + ";" +
                        " * MISSING *;" +
                        "NO_ID\n");
                 writer.flush();

                failedWriter.write(name + "\n");
            }
            //if(i > 20) break;
            playerDump = reader.readLine();
        }
        reader.close();
        writer.flush();
        writer.close();
        failedWriter.flush();
        failedWriter.close();

        System.out.println("Found: " + found_count);
        System.out.println("Not found: " + not_found);
    }

	//@Test
	public void createFile(WhoScoredPlayerPageParser parser) throws IOException {
		File output = new File("players.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		File failed = new File("failed.txt");
		BufferedWriter failedWriter = new BufferedWriter(new FileWriter(failed));

		for (int i = 1; i < 5000; ++i) {
			URL url = new URL("http://www.whoscored.com/Players/" + i + "/");

			try {
				JSoupURLReader jSoupFileReader = new JSoupURLReader(url);
				parser.setDocument(jSoupFileReader.read());
				Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

				logger.info("Information for {}:", url);
				for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
					logger.info("{} {}", i, playerInformationParserObject);
					writer.write(playerInformationParserObject.getName() + ";" +
							playerInformationParserObject.getNationality() + ";" +
							playerInformationParserObject.getDateOfBirth() + ";" +
							playerInformationParserObject.getHeight() + ";" +
							playerInformationParserObject.getWeight() + ";" +
							playerInformationParserObject.getShirtNumber() + ";" +
							playerInformationParserObject.getPositions() + "\n");
				}
			} catch (Exception e) {
				logger.error("Failed at {}:", url);
				logger.error("Exception:", e);
				failedWriter.write(url + "\n");
			}
		}

		writer.flush();
		writer.close();
		failedWriter.flush();
		failedWriter.close();
	}
}
