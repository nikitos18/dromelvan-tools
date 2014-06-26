package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.JFileChooser;

import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.parser.old.FileParser;
import org.dromelvan.tools.writer.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.google.inject.Inject;

public class FileParserTool extends D11Tool {

	private FileParser fileParser;
	private FileWriter writer;
	public final static String FILE_PARSER_TOOL_DIRECTORY_PREFERENCE = "FILE_PARSER_TOOL_DIRECTORY_PREFERENCE";
	private final static Logger logger = LoggerFactory.getLogger(FileParserTool.class);

	public FileParser getFileParser() {
		return fileParser;
	}

	@Inject
	public void setFileParser(FileParser fileParser) {
		this.fileParser = fileParser;
	}

	public FileWriter getFileWriter() {
		return writer;
	}

	@Inject
	public void setFileWriter(FileWriter writer) {
		this.writer = writer;
	}

	@Override
	public void execute() {
		File[] files = getFiles();
		if (files != null
				&& files.length > 0) {
			for (File file : files) {
				logger.info("");
				logger.info("Handling file {} ==>", file.getName());
				getFileParser().setFile(file);
				try {
					Set<ParserObject> parserObjects = getFileParser().parse();

					File directory = file.getParentFile();
					String inputFileExtension = Files.getFileExtension(file.getName());
					File outputFile = new File(directory, file.getName().replace(inputFileExtension, "xml"));
					logger.debug("Writing to file {}.", outputFile);

					for (ParserObject parserObject : parserObjects) {
						logger.debug("Writing object {}.", parserObject);
						getFileWriter().setFile(outputFile);
						// getFileWriter().write(parserObject);
					}
					logger.info("<== Handled file {}", file.getName());
				} catch (IOException e) {
					logger.error("IOException in execute: ", e);
				}
			}
		}
	}

	protected void handleFile(File file) {
		logger.info("Override handleFile(File) in {}.", getClass().getName());
	}

	protected File[] getFiles() {
		File[] files = null;
		JFileChooser fileChooser = new JFileChooser(getPreferences().get(FILE_PARSER_TOOL_DIRECTORY_PREFERENCE, "."));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new D11FileFilter());
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			files = fileChooser.getSelectedFiles();
			if (files.length > 0) {
				getPreferences().put(FILE_PARSER_TOOL_DIRECTORY_PREFERENCE, files[0].getParent());
			}
		}
		return files;
	}
}
