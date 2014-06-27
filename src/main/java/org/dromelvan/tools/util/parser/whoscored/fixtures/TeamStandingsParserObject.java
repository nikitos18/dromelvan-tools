package org.dromelvan.tools.util.parser.whoscored.fixtures;

import org.dromelvan.tools.parser.ParserObject;

public class TeamStandingsParserObject extends ParserObject {

	private int id;
	private String name;

	public TeamStandingsParserObject(String id, String name) {
		this.id = Integer.parseInt(id);
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Id: %s Name: %s", getId(), getName());
	}
}
