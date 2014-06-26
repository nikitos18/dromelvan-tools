package org.dromelvan.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.jsoup.JSoupURLReader;
import org.dromelvan.tools.parser.skysports.PlayerIdParserObject;
import org.dromelvan.tools.parser.skysports.SkySportsTeamLinkParser;
import org.dromelvan.tools.parser.skysports.SkySportsTeamPlayerIdParser;
import org.dromelvan.tools.parser.skysports.TeamLinkParserObject;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class SkySportsPlayerPhotoDownloadTest {

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			// bindManyInstances(File.class, new File("src/test/resources/Arsenal News, Fixtures, Transfers, Results   Sky Sports Football.htm"));
			bindManyInstances(File.class, new File("src/test/resources/Football Teams - Scores, News, Fixtures & Results   Sky Sports.htm"));
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

			JSoupURLReader jSoupURLReader = new JSoupURLReader(teamURL);

			document = jSoupURLReader.read();

			skySportsTeamPlayerIdParser.setDocument(document);

			for (PlayerIdParserObject playerIdParserObject : skySportsTeamPlayerIdParser.parse()) {
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
