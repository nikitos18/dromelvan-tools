package org.dromelvan.tools.writer.jaxb;

import java.util.Set;

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
	protected JAXBElement buildDocument(Set<TradeParserObject> tradeParserObjects) {
		Trades trades = new Trades();

		for (TradeParserObject tradeParserObject : tradeParserObjects) {
			Trade trade = new Trade();
			trade.setD11Team(tradeParserObject.getD11Team());
			trade.setPlayerOut(tradeParserObject.getPlayerOut());
			trade.setPlayerIn(tradeParserObject.getPlayerIn());
			trade.setFee(tradeParserObject.getFee());
			trades.getTrade().add(trade);
		}

		ObjectFactory objectFactory = new ObjectFactory();
		return objectFactory.createTrades(trades);
	}

}
