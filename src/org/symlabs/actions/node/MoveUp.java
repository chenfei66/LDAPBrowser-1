package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: MoveUp </p>
 * <p>Descripcion: Class which manages the action move up from an ldap node in the jtree. This action is displayed when you clicked on a ldap node in the JTree. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: MoveUp.java,v 1.5 2009-08-05 11:50:54 efernandez Exp $
 */
public class MoveUp extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "moveUp";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(MoveUp.class);

    /**Constructor: initializes the description and the mnemonic of this action
     * 
     */
    public MoveUp() {
        super("Up", java.awt.event.KeyEvent.VK_U,Utils.createImageIcon(Utils.ICON_ENTRY_MOVE_UP));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            LDAPNode parentNode = (LDAPNode) currentNode.getParent();
            TreeRootNode rootNode = (TreeRootNode) currentNode.getRoot();

            int idx = parentNode.getIndex(currentNode);
            
            logger.trace("parent:"+parentNode.myDN+",index of "+currentNode.myDN+": "+idx);
            
            if (idx > 0) {
                rootNode.getModel().removeNodeFromParent(currentNode);
                rootNode.getModel().insertNodeInto(currentNode, parentNode, idx - 1);
            }
        } catch (NullPointerException ex) {
            logger.trace("MoveUp Action:"+ex);
        //do nothing
        }
    }
}
