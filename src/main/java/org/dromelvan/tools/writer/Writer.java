package org.dromelvan.tools.writer;

import org.dromelvan.tools.parser.ParserObject;

public interface Writer<T extends ParserObject> {

    public void write(T parserObject);
    
}
