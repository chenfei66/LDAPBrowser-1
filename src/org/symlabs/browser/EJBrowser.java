package org.symlabs.browser;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: EjBrowser </p>
 * <p>Descripcion: Class which executes the browser ldap. 
 * This is the main class of the project. 
 * This class calls to the main window of the proyect: BrowserWin.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: EJBrowser.java,v 1.6 2009-04-02 16:08:42 efernandez Exp $
 */
public class EJBrowser {

    /**Method Main. This method starts the ldap browser execution
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // BasicConfigurator replaced with PropertyConfigurator.
        File propertyFile=new File(Utils.getFullPathLoggerProperties());
        //PropertyConfigurator.configure(args[0]);
        if (propertyFile.canRead()) {
            PropertyConfigurator.configure(Utils.getFullPathLoggerProperties());        
        } else {
            BasicConfigurator.configure();
            org.apache.log4j.Logger.getRootLogger().setLevel((Level) Level.ERROR);
        }
            
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BrowserMainWin browser = new BrowserMainWin();
                browser.setVisible(true);                        
            }
        });
    }

}
