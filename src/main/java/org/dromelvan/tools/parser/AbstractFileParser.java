package org.dromelvan.tools.parser;

import java.io.File;

public abstract class AbstractFileParser<T extends ParserObject> extends AbstractParser<T> implements FileParser<T> {

    private File file;

    public AbstractFileParser(ParserProperties parserProperties) {
        super(parserProperties);
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

}
