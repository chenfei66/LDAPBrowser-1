package org.symlabs.actions.tab;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.MessageDialog;
import org.symlabs.util.Utils;

/**
 * <p>Titulo:SaveAllTabs </p>
 * <p>Descripcion: Action that save all the tabs displayed in the main window. It calles the SaveTab action. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SaveAllTabs.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class SaveAllTabs extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "saveAllTabs";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SaveAllTabs.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public SaveAllTabs() {
        super("Save All", java.awt.event.KeyEvent.VK_V, Utils.createImageIcon(Utils.ICON_SAVE_ALL));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            while (Utils.getMainWindow().getTotalTabsMainWindow()-1 >= Utils.getMainWindow().getFirstTabIndexMainWindow()) {
                Utils.getAction(SaveTab.HashKey).getAction().actionPerformed(e);
            }
        } catch (Exception ex) {
            String msg = "Error saving all tabs.";
            String msgDetails = msg + ex;
            String title = "Save All Error";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, msgDetails, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(msg);
        }
    }
}
