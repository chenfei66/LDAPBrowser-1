package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.search.AddSearchWin;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddSearch </p>
 * <p>Descripcion: Action used to add searches to an existing folder. It calls AddSearchWin </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddSearch.java,v 1.3 2009-08-04 13:55:27 efernandez Exp $
 */
public class AddSearch extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "searchAdd";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddSearch.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public AddSearch() {
        super("Add Search", java.awt.event.KeyEvent.VK_K, Utils.createImageIcon(Utils.ICON_SEARCH_ADD));
    }

    public void actionPerformed(ActionEvent e) {
        LDAPNode ldapNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        if (ldapNode != null && ldapNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            //We have to ask for the folder to add this search
            AddSearchWin addBookmarkWin = new AddSearchWin(ldapNode);
            addBookmarkWin.setLocationRelativeTo(Utils.getMainWindow());
            addBookmarkWin.setVisible(true);
        }
    }
}