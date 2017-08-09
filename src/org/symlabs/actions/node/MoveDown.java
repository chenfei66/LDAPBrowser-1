package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: MoveDown </p>
 * <p>Descripcion: Class which manages the action move down from an ldap node in the jtree. This action is displayed when you clicked on a ldap node in the JTree. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: MoveDown.java,v 1.5 2009-08-05 11:50:54 efernandez Exp $
 */
public class MoveDown extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "moveDown";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(MoveDown.class);
    
    /**Constructor: initializes the description and the mnemonic of this action
     * 
     */
    public MoveDown() {
        super("Down", java.awt.event.KeyEvent.VK_D,Utils.createImageIcon(Utils.ICON_ENTRY_MOVE_DOWN));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            LDAPNode parentNode = (LDAPNode) currentNode.getParent();
            TreeRootNode rootNode = (TreeRootNode) currentNode.getRoot();

            int idx = parentNode.getIndex(currentNode);
            int count = parentNode.getChildCount();
            
            logger.trace("parent:"+parentNode.myDN+",childcount:"+count+",index of "+currentNode.myDN+": "+idx);
            
            if (idx < count - 1) {
                rootNode.getModel().removeNodeFromParent(currentNode);
                rootNode.getModel().insertNodeInto(currentNode, parentNode, idx + 1);
            }
        } catch (NullPointerException ex) {
            logger.error("MoveDown Action: "+ex);
        //We might not have config, or node or parent, but we do not care :)
        //do nothing
        }
    }
}
