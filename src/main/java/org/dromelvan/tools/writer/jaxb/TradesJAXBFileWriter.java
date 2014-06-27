package org.dromelvan.tools.writer.jaxb;

import javax.xml.bind.JAXBElement;

import org.dromelvan.jaxb.trades.ObjectFactory;
import org.dromelvan.jaxb.trades.Trade;
import org.dromelvan.jaxb.trades.Trades;
import org.dromelvan.tools.parser.trade.TradeParserObject;

public class TradesJAXBFileWriter extends JAXBFileWriter<TradeParserObject> {

	public TradesJAXBFileWriter() {
		setXmlRootClass(Trades.class);
	}

	@Override
	protected JAXBElement buildDocument(TradeParserObject matchParserObject) {
		Trades trades = new Trades();

		Trade trade = new Trade();
		trade.setD11Team(matchParserObject.getD11Team());
		trade.setPlayerOut(matchParserObject.getPlayerOut());
		trade.setPlayerIn(matchParserObject.getPlayerIn());
		trade.setFee(matchParserObject.getFee());
		trades.getTrade().add(trade);

		ObjectFactory objectFactory = new ObjectFactory();
		return objectFactory.createTrades(trades);
	}

}
