package org.symlabs.actions.tab;

import org.symlabs.actions.*;
import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.store.ConnectionData;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SaveAsTab </p>
 * <p>Descripcion: Action that saves the current tab displayed in the browser main window. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SaveAsTab.java,v 1.10 2009-08-24 09:01:06 efernandez Exp $
 */
public class SaveAsTab extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "saveAsTab";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SaveAsTab.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public SaveAsTab() {
        super("Save as", java.awt.event.KeyEvent.VK_A,Utils.createImageIcon(Utils.ICON_SAVE_AS));
    }

    public void actionPerformed(ActionEvent e) {
        String errorMessage = "";
        String title="";
        String confName="";
        TreeRootNode rootNode = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode();//(TreeRootNode) Utils.getCurrentNode().getRoot();
        LDAPServer ldapServer = rootNode.getLdapServer();
        //If this connection has not a configuration name
        try {
            confName = ConnectionData.saveAsConnectionData(ldapServer.getConnectionData(), Utils.getMainWindow());
            if(!confName.equals(ConnectionData.OPERATION_CANCELLED)){
                Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer().setConfigurationName(confName);
            }else{
                errorMessage=ConnectionData.OPERATION_CANCELLED+".\n"+"Your configuration was not saved.";
                title="Operation cancelled";
            }
        
        } catch (Exception ex) {
            title="Data error";
            errorMessage += ex.getMessage()+"\n";
            logger.trace(errorMessage);
        }
        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            String message="Error trying to save as the configuration. ";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMessage,MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }else{
            //OJO aqui habria que actualizar el arbol con el nombre actual de la configuration
            //We update the root node name
            rootNode.myDN = confName;
            rootNode.getModel().nodeChanged(rootNode);
            rootNode.getTree().repaint();
            
            //We update the tab connection name
            int indexTabSelected= Utils.getMainWindow().getContainerTabbedPane().getSelectedIndex();
            Utils.getMainWindow().setTabTitle(indexTabSelected, confName);            
            Utils.getMainWindow().getContainerTabbedPane().repaint();
            
        }
        Utils.getMainWindow().clearStatusBarMessage();
    }
}


