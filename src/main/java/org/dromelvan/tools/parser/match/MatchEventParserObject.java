package org.dromelvan.tools.parser.match;

import org.dromelvan.tools.parser.ParserObject;

public class MatchEventParserObject extends ParserObject {

	private int time;

	public MatchEventParserObject() {
	}

	public MatchEventParserObject(int time) {
		setTime(time);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
		this.time = (time > 90 ? 90 : time);
	}

}
