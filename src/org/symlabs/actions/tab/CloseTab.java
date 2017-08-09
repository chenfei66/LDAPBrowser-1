package org.symlabs.actions.tab;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloseTab </p>
 * <p>Descripcion: Action that close the tab selected. This action will be displayed when you clicked in the popup menu from the tab panel connection </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloseTab.java,v 1.8 2009-08-04 15:26:07 efernandez Exp $
 */
public class CloseTab extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "closeTab";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloseTab.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public CloseTab() {
        super("Close Connection", java.awt.event.KeyEvent.VK_C,Utils.createImageIcon(Utils.ICON_DISCONNECT));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPServer ldapServer = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer();
            String confName = ldapServer.getConnectionData().getConfigurationName();
            String title = "";
            String question = "";
            int option = JOptionPane.YES_OPTION;
            if (confName == null || confName.trim().equals("")) {
                confName = ldapServer.getDefaultConfigurationName();
                title = "Close unsaving connection";
                question = "Your configuration " + confName + " has not been saved.\nAre you sure you want to close this connection?";
                option = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            } else if(ldapServer.getConnectionData().isDirty()){
                title="Close connection";
                question ="Your configuration "+confName +" has changes without saving.\nAre you sure you want to close this connection?\nYou will lost the changes.";
                option = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            }else{
                option = JOptionPane.YES_OPTION;
            }
            if (option == JOptionPane.YES_OPTION) {
                Utils.getMainWindow().closeCurrentTabWithoutAsking();
                Utils.getMainWindow().refreshBookmarksMainWindow();
                Utils.getMainWindow().refreshSearchesMainWindow();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                //We set a name to this configuration to allow to close it without saving.
                ldapServer.setConfigurationName(ldapServer.getDefaultConfigurationName());
            }
        } catch (Exception ex) {
            String msg = "Error closing the tab: " + ex;
            logger.error(msg);
        }
    }
}
