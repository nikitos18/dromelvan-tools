package org.dromelvan.tools.writer;

import java.io.File;

import org.dromelvan.tools.parser.old.ParserObject;

public interface FileWriter<T extends ParserObject> extends Writer<T> {

    public void setFile(File file);
    
}
