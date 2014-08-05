package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.JFileChooser;

import org.dromelvan.tools.parser.FileParser;
import org.dromelvan.tools.parser.ParserObject;
import org.dromelvan.tools.writer.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public abstract class FileParserTool<T extends FileParser, U extends FileWriter, V extends ParserObject> extends D11Tool {

	private final T parser;
	private final U fileWriter;
	private final static Logger logger = LoggerFactory.getLogger(FileParserTool.class);

	public FileParserTool(T parser, U fileWriter) {
		this.parser = parser;
		this.fileWriter = fileWriter;
	}

	public T getParser() {
		return parser;
	}

	public U getFileWriter() {
		return fileWriter;
	}

	@Override
	public void execute() {
		File[] files = getFiles();
		if (files != null
				&& files.length > 0) {
			for (File file : files) {
				logger.info("");
				logger.info("Handling file {} ==>", file.getName());

				try {
					Set<V> parserObjects = parseFile(file);

					File directory = file.getParentFile();
					String inputFileExtension = Files.getFileExtension(file.getName());
					File outputFile = new File(directory, file.getName().replace(inputFileExtension, "xml"));
					logger.debug("Writing to file {}.", outputFile);

					getFileWriter().setFile(outputFile);
					getFileWriter().write(parserObjects);

				} catch (IOException e) {
					logger.error("IOException in execute: ", e);
				}
				logger.info("<== Handled file {}", file.getName());
			}
		}
	}

	protected Set<V> parseFile(File file) throws IOException {
		getParser().setFile(file);
		Set<V> parserObjects = getParser().parse();
		return parserObjects;
	}

	protected File[] getFiles() {
		File[] files = null;
		JFileChooser fileChooser = new JFileChooser(getPreferences().get(getClass().getName() + "_DIRECTORY_PREFERENCE", "."));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new D11FileFilter());
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			files = fileChooser.getSelectedFiles();
			if (files.length > 0) {
				getPreferences().put(getClass().getName() + "_DIRECTORY_PREFERENCE", files[0].getParent());
			}
		}
		return files;
	}

}
