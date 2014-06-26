package org.dromelvan.tools.parser.old;

import java.io.File;

import org.dromelvan.tools.parser.Parser;
import org.dromelvan.tools.parser.ParserObject;

public interface FileParser<T extends ParserObject> extends Parser<T> {

	public void setFile(File file);

}
