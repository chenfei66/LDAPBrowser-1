package org.symlabs.actions.tab;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.browser.MessageDialog;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloseAllTabs </p>
 * <p>Descripcion: Action that close all tabs displayed in the browser main window. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloseAllTabs.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class CloseAllTabs extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "closeAllTabs";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloseAllTabs.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public CloseAllTabs() {
        super("Close All", java.awt.event.KeyEvent.VK_A, Utils.createImageIcon(Utils.ICON_DISCONNECT_ALL));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            while (Utils.getMainWindow().getTotalTabsMainWindow()-1 >= Utils.getMainWindow().getFirstTabIndexMainWindow()) {
                Utils.getAction(CloseTab.HashKey).getAction().actionPerformed(e);
            }
        } catch (Exception ex) {
            String msg = "Error closing all tabs.";
            String msgDetails = msg + ex;
            String title = "Close All Error";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, msgDetails, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(msgDetails);
        }
    }
}

