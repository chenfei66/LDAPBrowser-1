package org.symlabs.store;

import org.apache.log4j.Logger;

/**
 * <p>Titulo: BrowserPreferences </p>
 * <p>Descripcion: Class that manages the preferences stored. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BrowserPreferences.java,v 1.1 2009-08-07 10:23:54 efernandez Exp $
 */
public class BrowserPreferences {
    /**Attribute that contains the editor selected*/
    private String editor;
    /**Attribute that containes the look and feel selected*/
    private String lookAndFeel;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BrowserPreferences.class);

    /**Method that returns the editor selected
     * 
     * @return String. This is the editor title of the editor selected
     */
    public String getEditor() {
        return editor;
    }

    /**Method that returns the look and feel selected
     * 
     * @return String, This is the class name of the look and feel selected
     */
    public String getLookAndFeel() {
        return lookAndFeel;
    }   
    
    /**Constructor: Creates a new instance of the object preferences
     * 
     * @param editor String. This is the editor selected by default
     * @param lookAndFeel String. This is the look and feel selected by default
     */
    public BrowserPreferences(String editor, String lookAndFeel){
        this.editor = editor;
        this.lookAndFeel = lookAndFeel;
    }

}
