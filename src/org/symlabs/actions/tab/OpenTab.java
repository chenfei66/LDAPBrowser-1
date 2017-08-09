package org.symlabs.actions.tab;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserMainWin;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.connection.ConnectionOpenWin;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: OpenTab </p>
 * <p>Descripcion: Action that opens the panel that allows you to open a new connection.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: OpenTab.java,v 1.6 2009-08-24 10:27:56 efernandez Exp $
 */
public class OpenTab extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "openConnection";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(OpenTab.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public OpenTab() {
        super("Open Connection", java.awt.event.KeyEvent.VK_E,Utils.createImageIcon(Utils.ICON_CONNECT_OPEN));
    }

    public void actionPerformed(ActionEvent e) {
        String errorMsg = "";
        String title = "";
        String message = "";
        if (!Utils.getMainWindow().canCreateMoreTabConnection()) {
            errorMsg = "Could not open more connections. The maximum number of connections are " + (BrowserMainWin.MaxTabMainWindow) + "." + "\n";
            message = "Could not open more connections.";
            title = "Error";
            logger.debug(errorMsg);
        } else {
            try {
                ConnectionOpenWin openWin = new ConnectionOpenWin();
                openWin.setLocationRelativeTo(Utils.getMainWindow());
                openWin.setVisible(true);
            } catch (Exception ex) {
                errorMsg += ex.getMessage();
                message = "Could not open the connection.";
                logger.error(errorMsg);
            }
            if (!errorMsg.equals("")) {
                title = "Error opening file";
                logger.info(errorMsg);
            }
        }
        if (!errorMsg.equals("")) {
            logger.error(errorMsg);
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        } else {
            Utils.getAction(CloseTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            Utils.getAction(CloseAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            Utils.getAction(SaveTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            Utils.getAction(SaveAsTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            Utils.getAction(SaveAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            Utils.getAction(TabProperties.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
        }
    }
}
