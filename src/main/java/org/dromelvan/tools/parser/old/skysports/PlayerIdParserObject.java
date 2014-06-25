package org.dromelvan.tools.parser.old.skysports;

import org.dromelvan.tools.parser.old.ParserObject;

public class PlayerIdParserObject extends ParserObject {

	private String name;
	private String id;

	public PlayerIdParserObject(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("Name: %s Id: %s.", getName(), getId());
	}
}
