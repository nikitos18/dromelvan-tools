package org.dromelvan.tools.writer;

import java.util.Set;

import org.dromelvan.tools.parser.ParserObject;

public interface Writer<T extends ParserObject> {

	public void write(Set<T> parserObject);

}
