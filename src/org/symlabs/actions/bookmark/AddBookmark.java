package org.symlabs.actions.bookmark;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.bookmark.AddBookmarkWin;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddBookMark </p>
 * <p>Descripcion: Action that adds the selected dn to the bookmarks </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddBookmark.java,v 1.4 2009-08-04 13:55:27 efernandez Exp $
 */
public class AddBookmark extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "bookmarkAdd";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddBookmark.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public AddBookmark() {
        super("Add Bookmark", java.awt.event.KeyEvent.VK_K, Utils.createImageIcon(Utils.ICON_BOOKMARK_ADD));
    }

    public void actionPerformed(ActionEvent e) {
        LDAPNode ldapNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        if (ldapNode != null && ldapNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            //We have to ask for the folder to add this bookmark
            String dn = ldapNode.myDN;
            AddBookmarkWin addBookmarkWin = new AddBookmarkWin(ldapNode, dn);
            addBookmarkWin.setLocationRelativeTo(Utils.getMainWindow());
            addBookmarkWin.setVisible(true);
        }
    }
}
