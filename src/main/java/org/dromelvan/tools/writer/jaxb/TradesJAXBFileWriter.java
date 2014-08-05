package org.dromelvan.tools.writer.jaxb;

import java.math.BigInteger;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.dromelvan.jaxb.trades.ObjectFactory;
import org.dromelvan.jaxb.trades.Trade;
import org.dromelvan.jaxb.trades.Trades;
import org.dromelvan.tools.parser.trade.TradeParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradesJAXBFileWriter extends JAXBFileWriter<TradeParserObject> {

    private final static Logger logger = LoggerFactory.getLogger(TradesJAXBFileWriter.class);

	public TradesJAXBFileWriter() {
		setXmlRootClass(Trades.class);
	}

	@Override
	protected JAXBElement buildDocument(Set<TradeParserObject> tradeParserObjects) {
		Trades trades = new Trades();

		if(System.getProperty("tradeDay") != null) {
		    trades.setTradeDay(new BigInteger(System.getProperty("tradeDay")));
		} else {
		    logger.error("TradeDay system property missing.");
		}


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
