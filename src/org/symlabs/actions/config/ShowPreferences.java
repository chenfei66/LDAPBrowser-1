package org.symlabs.actions.config;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.config.PreferencesWin;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ShowConfig </p>
 * <p>Descripcion: Class that shows the configuration window. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ShowPreferences.java,v 1.1 2009-08-07 07:34:48 efernandez Exp $
 */
public class ShowPreferences extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "showConfigurationWindow";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ShowPreferences.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ShowPreferences() {
        super("Preferences", java.awt.event.KeyEvent.VK_E, Utils.createImageIcon(Utils.ICON_PREFERENCES));
    }

    public void actionPerformed(ActionEvent arg0) {
        PreferencesWin configurationWin = new PreferencesWin();
        configurationWin.setLocationRelativeTo(Utils.getMainWindow());
        configurationWin.setVisible(true);
    }
}
