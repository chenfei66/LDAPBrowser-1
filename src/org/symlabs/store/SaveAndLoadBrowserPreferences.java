package org.symlabs.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.symlabs.browser.BrowserMainWin;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SaveAndLoadBrowserPreferences </p>
 * <p>Descripcion: Class that saves and loads the preferences file. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SaveAndLoadBrowserPreferences.java,v 1.1 2009-08-07 10:23:54 efernandez Exp $
 */
public class SaveAndLoadBrowserPreferences {
    /**Attribute that contains the editor key, used to store the editor in the file*/
    private static final String EDITOR_KEY="editor:";
    /**Attribute that contains the look and feel key used to store the look and feel in the file*/
    private static final String LOOK_AND_FEEL_KEY ="lookandfeel:";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SaveAndLoadBrowserPreferences.class);
    
    /**Method that saves the file preferences given as argument
     * 
     * @param browserPreferences Preferences. This is the preferences that we want to save
     */
    public static void saveFilePreferences(BrowserPreferences browserPreferences){
        FileWriter file = null;
        try {
            file = new FileWriter(Utils.getFullPathPreferencesFile(),false);//false means that we do not want to append the information
            BufferedWriter buffer = new BufferedWriter(file);
            
            //Editor
            buffer.write(EDITOR_KEY+browserPreferences.getEditor());
            buffer.newLine();
            
            //Look and feel
            buffer.write(LOOK_AND_FEEL_KEY+browserPreferences.getLookAndFeel());
            buffer.newLine();
            
            buffer.close();
        } catch (IOException ex) {
            logger.trace("Error writting preferences file "+ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                logger.trace("Error closing preferences file "+ex);
            }
        }
    }
    
    /**Method that returns the preferences readed from preferences file
     * 
     * @return BrowserPreferences
     */
    public static BrowserPreferences getPreferencesFromPreferencesFile(){
        FileReader reader = null;
        String line = null;
        BrowserPreferences browserPreferences= null;
        try {
                        
            File file = new File(Utils.getFullPathPreferencesFile());
            if(!file.exists()){//we have to create it
                SaveAndLoadBrowserPreferences.saveFilePreferences(new BrowserPreferences(BrowserMainWin.DEFAULT_EDITOR_TITLE, BrowserMainWin.DEFAULT_LOOK_AND_FEEL));
            }
            
            reader = new FileReader(Utils.getFullPathPreferencesFile());
            BufferedReader buffer = new BufferedReader(reader);
            //We read the editor
            line = buffer.readLine();
            String editor = line.substring(EDITOR_KEY.length(),line.length());
            
            //We readthe look and feel
            line = buffer.readLine();
            String lookAndFeel = line.substring(LOOK_AND_FEEL_KEY.length(), line.length());
            
            browserPreferences = new BrowserPreferences(editor,lookAndFeel);
                    
        } catch (IOException ex) {
            logger.trace("Error reading preferences file "+ex);
        } catch (Exception exc) {
            logger.trace("Error reading preferences file "+exc);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                logger.trace("Error closing preferences file "+ex);
            }
        }
        return browserPreferences;
    }
}
