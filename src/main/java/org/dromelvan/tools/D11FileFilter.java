package org.dromelvan.tools;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.google.common.io.Files;

public class D11FileFilter extends FileFilter {

    @Override
    public String getDescription() {
        return "D11 File";
    }
    
    @Override
    public boolean accept(File file) {
        if(file.isDirectory()) {
            return true;
        }        
        String extension = Files.getFileExtension(file.getName());
        if(extension.equalsIgnoreCase("html")
           || extension.equalsIgnoreCase("htm")
           || extension.equals("csv")) {
            return true;
        }
        return false;
    }
}
