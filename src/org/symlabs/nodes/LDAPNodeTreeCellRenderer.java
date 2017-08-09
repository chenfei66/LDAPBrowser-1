package org.symlabs.nodes;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Logger;
import org.symlabs.bookmark.BookmarkBaseNode;
import org.symlabs.search.SearchBaseNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: MyTreeCellRenderer </p>
 * <p>Descripcion: Class used to set the tooltiptext and the image icon for each node. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPNodeTreeCellRenderer.java,v 1.3 2009-07-17 12:29:25 efernandez Exp $
 */
public class LDAPNodeTreeCellRenderer extends DefaultTreeCellRenderer {

    /**Attribute used to store the default image icon*/
    private ImageIcon defaultIcon;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPNodeTreeCellRenderer.class);

    /** Constructor: It is created a new instance of MyTreeCellRenderer.
     * 
     */
    public LDAPNodeTreeCellRenderer() {
        this.defaultIcon = LDAPBrowserNode.createImageIcon(Utils.ICON_DEFAULT_NODE);
    }

    /**Method used to get the tree cell renderer. It sets the tooltiptext and the node image icon
     * 
     * @param tree JTree. This is the JTree
     * @param value Object. This is the object, in this case this is the ldap node
     * @param sel boolean. If it is selected
     * @param expanded boolean. If it is expanded
     * @param leaf boolean. If it is a leaf
     * @param row int. This is the row number
     * @param hasFocus boolen. It this node has the focus.
     * @return Component. It is returned the object MyTreeCellRenderer
     */
    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);

        //If the object is an instance of LDAPNode we set its tooltiptext  like: node.myDN;
        if (value instanceof LDAPNode) {
            LDAPNode node = (LDAPNode) value;
            this.setToolTipText(node.myDN);
            this.setIcon(node.getImageIcon());
        } else if (value instanceof BookmarkBaseNode) {
            BookmarkBaseNode base = (BookmarkBaseNode) value;
            this.setToolTipText(base.getName());
            this.setIcon(base.getImageIcon());
        }  else if (value instanceof SearchBaseNode) {
            SearchBaseNode base = (SearchBaseNode) value;
            this.setToolTipText(base.getName());
            this.setIcon(base.getImageIcon());
        } else {
            this.setToolTipText(null);
            this.setIcon(this.defaultIcon);
        }
        return this;
    }
}
