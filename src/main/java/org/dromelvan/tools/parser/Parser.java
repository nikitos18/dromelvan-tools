package org.dromelvan.tools.parser;

import java.util.Set;

public interface Parser<T extends ParserObject> {

    public Set<T> parse();
    
}
