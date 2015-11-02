package org.dromelvan.tools.parser.whoscored.fixtures;

import java.util.ArrayList;
import java.util.List;

import org.dromelvan.tools.parser.ParserObject;
import org.joda.time.LocalDate;

public class MatchDayParserObject extends ParserObject implements Comparable<MatchDayParserObject> {

	private LocalDate localDate;
	private int matchDayNumber;
	private List<MatchParserObject> matchParserObjects = new ArrayList<MatchParserObject>();

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public int getMatchDayNumber() {
		return matchDayNumber;
	}

	public void setMatchDayNumber(int matchDayNumber) {
		this.matchDayNumber = matchDayNumber;
	}

	public List<MatchParserObject> getMatchParserObjects() {
		return matchParserObjects;
	}

	public void setMatchParserObjects(List<MatchParserObject> matchParserObjects) {
		this.matchParserObjects = matchParserObjects;
	}

	@Override
	public int compareTo(MatchDayParserObject matchDayParserObject) {
		return getLocalDate().compareTo(matchDayParserObject.getLocalDate());
	}

}
