package org.symlabs.actions.tab;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserDataStatus;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.search.SearchResultsPanel;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloseSearch </p>
 * <p>Descripcion: Action that closes the search displayed.  </p>
 * <p>Copyright: Symlabs  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloseTreeRootNode.java,v 1.3 2009-08-05 11:50:54 efernandez Exp $
 */
public class CloseTreeRootNode extends DsAction {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloseTreeRootNode.class);
    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "closeTreeWindow";

    public CloseTreeRootNode() {
        super("Close", java.awt.event.KeyEvent.VK_S, Utils.createImageIcon(Utils.ICON_SEARCH_CLOSE));
    }

    public void actionPerformed(ActionEvent arg0) {
        BrowserPanel browserPanel = Utils.getMainWindow().getCurrentBrowserPanel();
        TreeRootNode treeRootNode = browserPanel.getTreeRootNode();
        //We set as visible the tree root node of the ldap connection
        //We get the tree root node
        BrowserPanel myPanel = Utils.getMainWindow().getBrowserPanelByTitle(treeRootNode.getLdapServer().getConnectionData().getConfigurationName());

        //We get the browser data of this panel
        BrowserDataStatus browserData = myPanel.getSearchResultsPanel().getBrowserData().get(SearchResultsPanel.INDEX_CONNECTION);

        //We get the selected Node
        Utils.setTreeRootNodeInBrowser(myPanel, browserData.getTreeRootNode(), browserData.getSelectedNode());

        if (treeRootNode instanceof TreeSearchRootNode) {
            TreeSearchRootNode searchRootNode = (TreeSearchRootNode) treeRootNode;

            //We remove this search
            browserPanel.searchResultsPanel.removeSearchFromBrowserPanel(searchRootNode.getSearchNode());
        } else if (treeRootNode instanceof TreeRootDseRootNode) {
            browserPanel.searchResultsPanel.removeRootDseFromBrowserPanel();
        } else if (treeRootNode instanceof TreeSchemaRootNode) {
            browserPanel.searchResultsPanel.removeSchemaFromBrowserPanel();
        }
        browserPanel.validate();
        browserPanel.repaint();
    }
}
