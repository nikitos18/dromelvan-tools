package org.dromelvan.tools.parser.old;

import java.util.Set;

public interface Parser<T extends ParserObject> {

    public Set<T> parse();
    
}
