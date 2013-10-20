package org.dromelvan.tools;

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
    @Parameter(names = { "-t", "-tool"}, description = "The tool to run.", required = true)
    private String tool;
    @Parameter(names = { "-tools"}, description = "List of available tools.", help = true)
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
        } catch(ParameterException e) {
            logger.error("Invalid parameters: {}.",e.getMessage());
            jCommander = new JCommander(this);
            jCommander.setProgramName("java " + getClass().getName());
            help = true;
        }
    }

    public void execute() {
        logger.info("D11Tools v.{}. Running {} tool.", getClass().getPackage().getImplementationVersion(), this.tool);
        if(this.help) {
            this.jCommander.usage();
            return;
        }

        Module module = null;
        if(this.tool != null) {
            if(this.tool.equals("match")) {
                module = new MatchStatisticsFileParserModule();
            } else if(this.tool.equals("transfers")) {
                logger.info("Transfer tool not implemented yet.");
                return;
            }
        }

        if(this.tools
           || module == null) {
            logger.info("Available tools: match, transfers.");
            return;
        }

        Injector injector = Guice.createInjector(module);
        FileParserTool fileParserTool = injector.getInstance(FileParserTool.class);
        fileParserTool.execute();
    }
}
