package org.dromelvan.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.jsoup.JSoupURLReader;
import org.dromelvan.tools.util.parser.skysports.PlayerIdParserObject;
import org.dromelvan.tools.util.parser.skysports.SkySportsTeamLinkParser;
import org.dromelvan.tools.util.parser.skysports.SkySportsTeamPlayerIdParser;
import org.dromelvan.tools.util.parser.skysports.TeamLinkParserObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class SkySportsPlayerPhotoDownloadTests {

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			// bindManyInstances(File.class, new File("src/test/resources/Arsenal News, Fixtures, Transfers, Results   Sky Sports Football.htm"));
			// bindManyInstances(File.class, new File("src/test/resources/Football Teams - Scores, News, Fixtures & Results   Sky Sports.htm"));
			try {
			    bindManyInstances(URL.class, new URL("http://www1.skysports.com/football/competitions/premier-league"));
			} catch(MalformedURLException e) {
			    e.printStackTrace();
			}
		}
	}

	@Test
	public void downloadImagesFromURL(@All URL teamLinksUrl, SkySportsTeamLinkParser skySportsTeamLinkParser, SkySportsTeamPlayerIdParser skySportsTeamPlayerIdParser) throws IOException {
        JSoupURLReader jSoupURLReader = new JSoupURLReader(teamLinksUrl);
        Document teamLinksDocument = jSoupURLReader.read();

        skySportsTeamLinkParser.setDocument(teamLinksDocument);

        for (TeamLinkParserObject teamLinkParserObject : skySportsTeamLinkParser.parse()) {
            System.out.println("  Parsing " + teamLinkParserObject.getName());
            File photoDirectory = new File("skysports-player-photos" + File.separatorChar + teamLinkParserObject.getName());
            if (!photoDirectory.exists()) {
                photoDirectory.mkdirs();
            }

            URL teamURL = new URL("http://www1.skysports.com" + teamLinkParserObject.getLink());
            jSoupURLReader = new JSoupURLReader(teamURL);
            Document teamDocument = jSoupURLReader.read();

            for(Element element : teamDocument.select("a")) {
                if(element.text().equals("Squad")) {
                    URL squadURL = new URL("http://www1.skysports.com" + element.attr("href"));
                    jSoupURLReader = new JSoupURLReader(squadURL);

                    Document squadDocument = jSoupURLReader.read();
                    for(Element aElement : squadDocument.select("div.squad-list-cat").select("td").select("a")) {
                        String name = aElement.text();
                        String url = aElement.select("img").first().attr("data-src").replace("1-1/#{50}", "96x96");
                        if(url.trim().length() > 0) {
                            System.out.print("    Fetching " + name + "... ");

                            try {
                                URL imageUrl = new URL(url);
                                InputStream in = imageUrl.openStream();
                                OutputStream out = new BufferedOutputStream(new FileOutputStream(photoDirectory.getAbsolutePath() + File.separatorChar + name + ".jpg"));
                                for (int b; (b = in.read()) != -1;) {
                                    out.write(b);
                                }
                                out.close();
                                in.close();
                                System.out.println("Success!");
                            } catch (Exception e) {
                                System.out.println("Failed! (Exception)");
                            }
                        }
                    }

//                    skySportsTeamPlayerIdParser.setDocument(squadDocument);
//
//                    for(PlayerIdParserObject playerIdParserObject : skySportsTeamPlayerIdParser.parse()) {
//                        System.out.print("    Fetching " + playerIdParserObject.getName() + "... ");
//
//                        try {
//                            URL imageUrl = new URL("http://img.skysports.com/football/players/home/head/h_head_" + playerIdParserObject.getId() + ".png");
//                            InputStream in = imageUrl.openStream();
//                            OutputStream out = new BufferedOutputStream(new FileOutputStream(photoDirectory.getAbsolutePath() + File.separatorChar + playerIdParserObject.getName() + " (" + playerIdParserObject.getId() + ").jpg"));
//                            for (int b; (b = in.read()) != -1;) {
//                                out.write(b);
//                            }
//                            out.close();
//                            in.close();
//                            System.out.println("Success!");
//                        } catch (Exception e) {
//                            System.out.println("Failed! (Exception)");
//                        }
//                    }
                }
            }
        }
	}

	@Test
	public void downloadImagesFromAllTeams(@All File file, SkySportsTeamLinkParser skySportsTeamLinkParser, SkySportsTeamPlayerIdParser skySportsTeamPlayerIdParser) throws IOException {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		Document document = jSoupFileReader.read();

		skySportsTeamLinkParser.setDocument(document);

		for (TeamLinkParserObject teamLinkParserObject : skySportsTeamLinkParser.parse()) {
			File photoDirectory = new File("skysports-player-photos" + File.separatorChar + teamLinkParserObject.getName());
			if (!photoDirectory.exists()) {
				photoDirectory.mkdirs();
			}
			URL teamURL = new URL("http://www1.skysports.com/" + teamLinkParserObject.getLink());

			System.out.println("Handling " + teamURL + ".");

			JSoupURLReader jSoupURLReader = new JSoupURLReader(teamURL);

			document = jSoupURLReader.read();

			skySportsTeamPlayerIdParser.setDocument(document);
System.out.println(" Here we go");
			for (PlayerIdParserObject playerIdParserObject : skySportsTeamPlayerIdParser.parse()) {
			    System.out.println("Handling ");
				try {
					URL imageUrl = new URL("http://img.skysports.com/football/players/home/head/h_head_" + playerIdParserObject.getId() + ".png");

					InputStream in = imageUrl.openStream();
					OutputStream out = new BufferedOutputStream(new FileOutputStream(photoDirectory.getAbsolutePath() + File.separatorChar + playerIdParserObject.getName() + " (" + playerIdParserObject.getId() + ").jpg"));
					for (int b; (b = in.read()) != -1;) {
						out.write(b);
					}
					out.close();
					in.close();
				} catch (Exception e) {
					// We don't care
				    e.printStackTrace();
				}
			}
		}
	}

	// @Test
	public void downloadImagesFromFile(@All File file, SkySportsTeamPlayerIdParser skySportsTeamPlayerIdParser) throws IOException {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		Document document = jSoupFileReader.read();

		skySportsTeamPlayerIdParser.setDocument(document);

		for (PlayerIdParserObject playerIdParserObject : skySportsTeamPlayerIdParser.parse()) {

			try {
				URL imageUrl = new URL("http://img.skysports.com/football/players/home/head/h_head_" + playerIdParserObject.getId() + ".png");

				InputStream in = imageUrl.openStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream("skysports-player-photos/" + playerIdParserObject.getName() + " (" + playerIdParserObject.getId() + ").jpg"));
				for (int b; (b = in.read()) != -1;) {
					out.write(b);
				}
				out.close();
				in.close();
			} catch (Exception e) {
				// We don't care
			}
		}
	}
}
