package org.dromelvan.tools.parser.transfer;

import org.dromelvan.tools.parser.ParserObject;

public class TransferParserObject extends ParserObject {

	private String d11Team;
	private String playerOut;
	private String playerIn;
	private int fee;

	public TransferParserObject(String d11Team, String playerOut, String playerIn, int fee) {
		this.d11Team = d11Team;
		this.playerOut = playerOut;
		this.playerIn = playerIn;
		this.fee = fee;
	}

	public String getD11Team() {
		return d11Team;
	}

	public void setD11Team(String d11Team) {
		this.d11Team = d11Team;
	}

	public String getPlayerOut() {
		return playerOut;
	}

	public void setPlayerOut(String playerOut) {
		this.playerOut = playerOut;
	}

	public String getPlayerIn() {
		return playerIn;
	}

	public void setPlayerIn(String playerIn) {
		this.playerIn = playerIn;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "TransferParserObject [d11Team=" + d11Team + ", playerOut=" + playerOut + ", playerIn=" + playerIn + ", fee=" + fee + "]";
	}

}
