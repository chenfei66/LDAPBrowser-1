package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import org.apache.log4j.Logger;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: DeleteNode </p>
 * <p>Descripcion: Action that removes the selected node. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: DeleteNode.java,v 1.10 2009-08-24 09:01:06 efernandez Exp $
 */
public class DeleteNode extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "deleteNode";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(DeleteNode.class);

    public DeleteNode() {
        super("Delete Entry", java.awt.event.KeyEvent.VK_L, Utils.createImageIcon(Utils.ICON_ENTRY_DELETE));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        LDAPNode parentNode = (LDAPNode) currentNode.getParent();
        String currentDn = currentNode.myDN;
        String errorMsg = "";
        String title = "Delete Entry";

        if (parentNode.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE)) {
            try {
                title = "Delete entry";
                String question = "Are you sure you want to remove the selected entry?";
                int answer = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    //We delete the ldap node from the ldap server
                    LDAPConnection connection = ((TreeRootNode) currentNode.getRoot()).getLdapServer().getConnection(true);
                    connection.delete(currentDn);

                    //We delete the ldap node from our tree
                    currentNode.removeFromParent();

                    //We update the ldap tree
                    TreeRootNode rootNode = (TreeRootNode) parentNode.getRoot();
                    rootNode.getTree().updateUI();

                    //Update the selected node
                    rootNode.getTree().setSelectionPath(new TreePath(parentNode.getPath()));

                    //Updates the editors
                    Utils.getMainWindow().getCurrentBrowserPanel().closeAllEditor();
                    Utils.getMainWindow().getCurrentBrowserPanel().openAllEditor();

//                    question = "Do you want to remove all bookmarks with the dn: " + currentDn + "?" + "\n" +
//                            "If there are any bookmark with the old dn you will not be able to use it.";
//                    title = "Delete entry";
//                    answer = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
//                    if (answer == JOptionPane.YES_OPTION) {
//                        BookmarkFolderNode rootFolder = rootNode.getLdapServer().getConnectionData().getBookmarkRootFolder();
//                        rootFolder.removeBoomarkWithDn(currentDn);
//                        Utils.getMainWindow().refreshBookmarksMainWindow();
//                    }
                }
            } catch (LDAPException e) {
                errorMsg = e.toString() + "\n";
                logger.error(errorMsg);
            }

            if (!errorMsg.equals("")) {
                String message = "Error deleting the ldap node: " + currentDn;
                MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
                errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                errorDialog.setVisible(true);
            }
        }
    }
}
