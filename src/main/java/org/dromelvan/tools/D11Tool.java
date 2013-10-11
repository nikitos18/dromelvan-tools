package org.dromelvan.tools;

import java.util.prefs.Preferences;

import javax.swing.UIManager;

public abstract class D11Tool {

    private Preferences preferences;
    
    public D11Tool() {
        try {
            preferences = Preferences.userNodeForPackage(getClass());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract void execute();

    public Preferences getPreferences() {
        return preferences;
    }
    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }    
    
}
