package org.dromelvan.tools.parser.jsoup;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupURLReader implements JSoupDocumentReader {

	private URL url;

	public JSoupURLReader(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public Document read() throws IOException {
		Document document = Jsoup.connect(url.toString())
				// .data("query", "Java")
				.userAgent("Chrome")
				.timeout(10000)
				.get();
		return document;
	}
}
