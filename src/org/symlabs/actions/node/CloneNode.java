package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloneNode </p>
 * <p>Descripcion: Action that clones an entry of the ldap node. It is created a new entry like a sibling. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloneNode.java,v 1.9 2009-08-24 09:01:06 efernandez Exp $
 */
public class CloneNode extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "cloneNode";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloneNode.class);

    public CloneNode() {
        super("Clone Entry", java.awt.event.KeyEvent.VK_C,Utils.createImageIcon(Utils.ICON_ENTRY_CLONE));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        LDAPNode parentNode = (LDAPNode) currentNode.getParent();
        String currentRdn = currentNode.myRDN;
        String parentDn = parentNode.myDN;
        String errorMsg = "";
        int cont = 1;
        boolean valid = false;
        String dnToSearch = "";

        try {
            //We get the current ldap connection
            LDAPConnection connection = ((TreeRootNode) currentNode.getRoot()).getLdapServer().getConnection(true);

            //We check if the selected node is an ldap node or a tree root node
            if (parentNode.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE)) {

                //We search for a valid dn, We test if the dn selected exist or not
                if (parentDn != null) {

                    while (!valid) {
                        //We update the dn to search in the ldap server
                        dnToSearch = currentRdn + cont + "," + parentDn;

                        try {
                            LDAPSearchResults results = ((TreeRootNode) currentNode.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(dnToSearch);
                            if (results != null && results.getCount() == 0) {
                                valid = true;
                            }
                        } catch (LDAPException e) {
                            int errCode = e.getLDAPResultCode();

                            if (errCode == LDAPException.NO_SUCH_OBJECT) {
                                valid = true;
                            }
                        }
                        if (!valid) {
                            cont++;
                        }
                        if(cont>10){
                            valid=true;
                            errorMsg="Could not clone the entry"+"\n";
                            logger.error(errorMsg);
                        }
                    }//end while
                }

                //This is the valid dn
                String newDn = dnToSearch;

                //We get the ldap attributes contained in the selected node
                LDAPAttributeSet attrs = currentNode.getData();

                Utils.createNewLDAPEntry(newDn, attrs, parentNode);

                //We update the tree
                ((TreeRootNode) currentNode.getRoot()).getTree().updateUI();

            }

        } catch (Exception ex) {
            errorMsg += "Cloning node error: " + currentNode.myDN + "\n" + ex;
            logger.error(errorMsg);
        }

        //Show input message
        if (!errorMsg.equals("")) {
            String title = "Clone Error";
            String message = "Cloning node error";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, errorMsg,MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }
}
