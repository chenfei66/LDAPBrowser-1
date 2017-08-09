package org.symlabs.actions.bookmark;

import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.bookmark.BookmarkNode;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: GoToBookmark </p>
 * <p>Descripcion: Action used to search an ldap entry.  </p>
 * <p>Copyright: Symlabs  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: GoToBookmark.java,v 1.4 2009-08-24 09:01:06 efernandez Exp $
 */
public class GoToBookmark extends DsAction {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(GoToBookmark.class);
    private TreeRootNode treeRootNode;
    private BookmarkNode bookmark;

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     * @param treeRootNode TreeRootNode. This is the tree root node that contains the ldap connection
     * @param bookmark Bookmark. This is the bookmark that contains this action
     */
    public GoToBookmark(TreeRootNode treeRootNode, BookmarkNode bookmark) {
        super(bookmark.getName(), java.awt.event.KeyEvent.VK_G, Utils.createImageIcon(Utils.ICON_BOOKMARK));
        this.treeRootNode = treeRootNode;
        this.bookmark = bookmark;
    }

    public void actionPerformed(ActionEvent evt) {
        BrowserPanel browserPanel = Utils.getMainWindow().getBrowserPanelByTitle(treeRootNode.getLdapServer().getConnectionData().getConfigurationName());

        //If at this moment is shown a search then we have to set as current tree the connection tree
        if (browserPanel.getTreeRootNode() instanceof TreeSearchRootNode) {
            //We get the connection BrowserDataSatus and show it, so we press the button of this search
            browserPanel.getSearchResultsPanel().getBrowserData().get(0).getButton().getAction().actionPerformed(null);
        }

        String dn = bookmark.getDn();
        boolean found = false;
        try {
            LDAPSearchResults results = treeRootNode.getLdapServer().getLdapOperation().getEntryBaseDN(dn);
            if (results != null && results.getCount() == 1) {
                found = true;
            }
        } catch (LDAPException e) {
            String details = e.toString();
            String message = "Error searching the selected dn: " + dn + " in the ldap server.";
            String title = "Error Looking For Bookmark";
            MessageDialog dialog = new MessageDialog(Utils.getMainWindow(),title, message, details, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(Utils.getMainWindow());
            dialog.setVisible(true);
            logger.error(message + details);
            return;
        }
        if (found) {
            LDAPNode node = Utils.getLDAPNodeByDnCheckingNamingContexts(dn, treeRootNode);
            if (node != null) {
                logger.trace("Node:" + node);
                logger.trace("Node:" + node.myDN);
                //We set selected the new node
                treeRootNode.getTree().setSelectionPath(new TreePath(node.getPath()));
            } else {//The node was found but we have to load from ldap Server the node
                logger.trace("We have to load the suffix to get the node: " + dn);

                LDAPNode namingContextNode = Utils.containsRootNodeDnToSearch(dn, treeRootNode);
                LDAPNode bestParentNode = Utils.getTheBestParentNode(dn, namingContextNode, namingContextNode);

                if (bestParentNode == null) {
                    bestParentNode = namingContextNode;
                    logger.trace("namingContextNode=null, we set as best parentnode: " + namingContextNode.myDN);
                } else {
                    logger.trace("best parent node returned: " + bestParentNode.myDN);
                }
                LDAPNode virtualNode = Utils.getNodesBetweenDnAndLDAPNode(dn, bestParentNode, false);
                LDAPNode x = virtualNode;
                while (x.getChildCount() > 0) {
                    x = (LDAPNode) x.getChildAt(0);
                    logger.trace("dn:" + x.myDN + ", totalchild:" + x.getChildCount());
                }
                bestParentNode.add(virtualNode);

                //We have to update the tree
                treeRootNode.getTree().updateUI();
                // we need to select the last node added
                while (virtualNode.getChildCount() > 0) {
                    virtualNode = (LDAPNode) virtualNode.getChildAt(0);
                }
                treeRootNode.getTree().setSelectionPath(new TreePath(virtualNode.getPath()));
            }
        } else {//The node was not found
            String message = "Error, the selected dn: " + dn + " was not found in the ldap server.";
            String title = "Error Looking For Bookmark";
            MessageDialog dialog = new MessageDialog(Utils.getMainWindow(),title, message, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(Utils.getMainWindow());
            dialog.setVisible(true);
            logger.trace(message);
            return;
        }
    }
}
