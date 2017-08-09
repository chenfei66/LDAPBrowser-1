package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.util.RDN;
import org.apache.log4j.Logger;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.LDAPOperation;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: RenameNode </p>
 * <p>Descripcion: Action that renames the selected node. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: RenameNode.java,v 1.12 2009-08-24 09:01:06 efernandez Exp $
 */
public class RenameNode extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "renameNode";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(RenameNode.class);

    public RenameNode() {
        super("Rename Entry", java.awt.event.KeyEvent.VK_R, Utils.createImageIcon(Utils.ICON_ENTRY_RENAME));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        String oldDn = currentNode.myDN;
        String rdnAttribute = Utils.getRDNAttribute(currentNode.myRDN);
        rdnAttribute += "=";
        LDAPOperation operation = ((TreeRootNode) currentNode.getRoot()).getLdapServer().getLdapOperation();
        String errorMsg = "";
        //We have to ask for a valid rdn
        String newRdn = "";
        String title = "Rename Entry";
        String message = "Enter a valid "+Utils.getRDNAttribute(currentNode.myRDN)+":";
        boolean valid = false;
        while (!valid) {
            newRdn = JOptionPane.showInputDialog(Utils.getMainWindow(), message, title, JOptionPane.INFORMATION_MESSAGE);
            if (newRdn != null) {
                newRdn = rdnAttribute + newRdn;
                logger.trace("se ha pulsado: " + newRdn);
                if (!newRdn.equals("") && RDN.isRDN(newRdn)) {
                    try {
                        LDAPSearchResults results = operation.getEntryBaseDN(newRdn);
                        if (results != null && results.getCount() > 0) {
                            logger.trace("Aqui 1");
                            newRdn = "";
                        }
                    } catch (LDAPException e) {
                        int errCode = e.getLDAPResultCode();

                        if (errCode == LDAPException.NO_SUCH_OBJECT) {
                            valid = true;
                        } else {
                            errorMsg = e.toString();
                            logger.trace("Aqui 2");
                            newRdn = "";
                            valid = true;
                        }
                    }
                } else {//the new rdn.equals("")
                    valid = false;
                    errorMsg = "The rdn: " + newRdn + " is not a valid RDN.";
                    MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, errorMsg, MessageDialog.MESSAGE_ERROR);
                    errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                    errorDialog.setVisible(true);
                    return;
                }
            } else {//the new rdn=null then the operation has been cancelled by the user
                valid = true;
                errorMsg = "Operation cancelled by the user.";
                MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, errorMsg, MessageDialog.MESSAGE_ERROR);
                errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                errorDialog.setVisible(true);
                return;
            }
        }//end while

        try {
            Utils.renameLDAPNode(currentNode, newRdn);
            Utils.getMainWindow().getCurrentBrowserPanel().closeAllEditor();
            Utils.getMainWindow().getCurrentBrowserPanel().openAllEditor();
            String newDn = oldDn.substring(oldDn.indexOf(","),oldDn.length());
            newDn= newRdn + newDn;
            logger.trace("The old dn is:"+oldDn+", the new dn is:"+newDn);
//            String question = "Do you want to update the bookmarks with dn: "+oldDn+"?"+"\n"+
//                    "If there are any bookmark with this dn you will not be able to use it.";
//            title = "Rename entry";
//            int answer = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
//            if (answer == JOptionPane.YES_OPTION) {
//                TreeRootNode rootNode = ((TreeRootNode) currentNode.getRoot());
//                BookmarkFolderNode rootFolder = rootNode.getLdapServer().getConnectionData().getBookmarkRootFolder();
//                rootFolder.updateDnOfBookmarks(oldDn, newDn);
//                Utils.getMainWindow().refreshBookmarksMainWindow();
//            }

        } catch (Exception e) {
            errorMsg += "Error renaming ldap node:" + currentNode.myRDN + ", new rdn: " + newRdn + ".\nError:" + e;
            logger.error(errorMsg);
            title = "Rename error";
            message = "Error renaming an ldap node: " + currentNode.myRDN;// + ", new rdn: " + newRdn;
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }
}
