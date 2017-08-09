package org.symlabs.actions.node;

import org.symlabs.actions.*;
import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: RefreshTree </p>
 * <p>Descripcion: Action that refreshes the ldap tree displayed in the main window. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: RefreshTree.java,v 1.6 2009-08-04 13:55:27 efernandez Exp $
 */
public class RefreshTree extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "refreshTree";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(RefreshTree.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public RefreshTree() {
        super("Refresh", java.awt.event.KeyEvent.VK_R,Utils.createImageIcon(Utils.ICON_TREE_REFRESH));
    }

    public void actionPerformed(ActionEvent e) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        currentNode.refreshNode();        
        TreeRootNode rootNode = (TreeRootNode) currentNode.getRoot();
        rootNode.getTree().updateUI();
        Utils.getMainWindow().getCurrentBrowserPanel().closeAllEditor();
        Utils.getMainWindow().getCurrentBrowserPanel().openAllEditor();
    }
}



