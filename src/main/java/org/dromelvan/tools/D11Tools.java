package org.dromelvan.tools;

import org.dromelvan.tools.trade.TradeParserModule;
import org.dromelvan.tools.trade.TradeParserTool;
import org.dromelvan.tools.whoscored.WhoScoredMatchParserModule;
import org.dromelvan.tools.whoscored.WhoScoredMatchParserTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class D11Tools {

	@Parameter(names = { "-help", "-h" }, description = "Display this help message.", help = true)
	private boolean help;
	@Parameter(names = { "-t", "-tool" }, description = "The tool to run.", required = true)
	private String tool;
    @Parameter(names = { "-i", "-id" }, description = "Id of whatever persistent object will be manipulated.", required = false)
    private String id;
	@Parameter(names = { "-tools" }, description = "List of available tools.", help = true)
	private boolean tools;

	private JCommander jCommander = null;
	private final static Logger logger = LoggerFactory.getLogger(D11Tools.class);

	public static void main(String[] args) {
		D11Tools d11Tools = new D11Tools(args);
		d11Tools.execute();
	}

	public D11Tools(String[] parameters) {
		try {
			jCommander = new JCommander(this, parameters);
			jCommander.setProgramName("java " + getClass().getName());
		} catch (ParameterException e) {
			logger.error("Invalid parameters: {}.", e.getMessage());
			jCommander = new JCommander(this);
			jCommander.setProgramName("java " + getClass().getName());
			help = true;
		}
	}

	public void execute() {
		logger.info("D11Tools v.{}. Running {} tool.", getClass().getPackage().getImplementationVersion(), this.tool);
		if (this.help) {
			this.jCommander.usage();
			return;
		}

		Class clazz = null;
		Module module = null;
		if (this.tool != null) {
			if (this.tool.equals("match")) {
				clazz = WhoScoredMatchParserTool.class;
				module = new WhoScoredMatchParserModule();
			} else if (this.tool.equals("trades")) {
				clazz = TradeParserTool.class;
				module = new TradeParserModule();
				if(this.id != null) {
				    System.setProperty("tradeDay", this.id);
				}
			}
		}

		if (this.tools
				|| module == null) {
			logger.info("Available tools: match, trades.");
			return;
		}

		Injector injector = Guice.createInjector(module);
		D11Tool d11Tool = (D11Tool)injector.getInstance(clazz);
		d11Tool.execute();
	}
}
