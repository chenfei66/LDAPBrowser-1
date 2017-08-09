package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.search.SearchWin;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: NewSearch </p>
 * <p>Descripcion: Action used to shows the Search window, that allows to make a search without saving </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: NewSearch.java,v 1.4 2009-08-24 09:01:06 efernandez Exp $
 */
public class NewSearch extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "searchwithoutSaving";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddSearch.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public NewSearch() {
        super("New Search", java.awt.event.KeyEvent.VK_H, Utils.createImageIcon(Utils.ICON_SEARCH_NEW));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPNode ldapNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        if (ldapNode != null && ldapNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            SearchWin searchWin = new SearchWin(ldapNode);
            searchWin.setLocationRelativeTo(Utils.getMainWindow());
            searchWin.setVisible(true);
        }else{
            String title="Error in Search";
            String msg="Please select a valid node.";
            String errorMsg="The node selected "+ldapNode.myDN+" is not a valid dn. Please select a valid entry of the ldap server.";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }
}
