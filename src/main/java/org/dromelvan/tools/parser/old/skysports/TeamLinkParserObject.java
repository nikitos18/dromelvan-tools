package org.dromelvan.tools.parser.old.skysports;

import org.dromelvan.tools.parser.ParserObject;

public class TeamLinkParserObject extends ParserObject {

	private String name;
	private String link;

	public TeamLinkParserObject(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return String.format("Name: %s Link: %s.", getName(), getLink());
	}
}
