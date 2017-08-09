package org.symlabs.nodes;

import org.symlabs.nodes.schema.TreeSchemaRootNode;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;

/**
 * <p>Titulo: MyTreeWillExpandListener </p>
 * <p>Descripcion: Class which manages listener used to display and load the child nodes of a parent ldap node </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPNodeTreeWillExpandListener.java,v 1.5 2009-08-25 17:22:26 efernandez Exp $
 */
public class LDAPNodeTreeWillExpandListener implements TreeWillExpandListener {
    
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPNodeTreeWillExpandListener.class);

    public void treeWillExpand(TreeExpansionEvent evt) throws ExpandVetoException {
        // Get the path that will be expanded
        TreePath path = evt.getPath();
        if (path == null || path.getPathCount() < 1) {
            return;
        }
        LDAPNode node = (LDAPNode) path.getLastPathComponent();
        TreeRootNode rootNode = (TreeRootNode) node.getRoot();
        if (!(rootNode instanceof TreeSchemaRootNode)  && !(rootNode instanceof TreeRootDseRootNode)) {
            if (node.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
                logger.trace("loading child nodes of: "+node.myDN);
                node.loadChildNodes();

            }
        }
        // Cancel the operation if desired
        boolean veto = false;
        if (veto) {
            throw new ExpandVetoException(evt);
        }

    }

    public void treeWillCollapse(TreeExpansionEvent evt) throws ExpandVetoException {
        // Cancel the operation if desired
        boolean veto = false;
        if (veto) {
            throw new ExpandVetoException(evt);
        }

    }
}
