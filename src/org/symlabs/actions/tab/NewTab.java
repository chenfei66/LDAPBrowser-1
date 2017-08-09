package org.symlabs.actions.tab;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserMainWin;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.connection.ConnectionNewWin;
import org.symlabs.util.Actions;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: NewTab </p>
 * <p>Descripcion: Action that shows the panel that allows you to create a new connection</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: NewTab.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class NewTab extends DsAction{
    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "newTab";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(NewTab.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public NewTab() {
        super("New Connection", java.awt.event.KeyEvent.VK_T,Utils.createImageIcon(Utils.ICON_CONNECT_NEW));
    }

    public void actionPerformed(ActionEvent e) {
        if (!Utils.getMainWindow().canCreateMoreTabConnection()) {
            String errorMsg = "Could not create more connections. The maximum number of connections are " + (BrowserMainWin.MaxTabMainWindow) + ".";
            String title = "Connection Error";
            String message = "Could not create more connections.";
            logger.info(errorMsg);
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        } else {
            ConnectionNewWin connectionWin = new ConnectionNewWin();
            connectionWin.setVisible(true);
            Actions.setEnabledActionsTab();
//            Utils.getAction(CloseTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
//            Utils.getAction(CloseAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
//            Utils.getAction(SaveTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
//            Utils.getAction(SaveAsTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
//            Utils.getAction(SaveAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
//            Utils.getAction(TabProperties.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0);
            
        }
    }
}
