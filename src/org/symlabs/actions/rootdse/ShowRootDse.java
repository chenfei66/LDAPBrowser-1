package org.symlabs.actions.rootdse;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ShowRootDse </p>
 * <p>Descripcion: Action that shows the root dse of the current connection. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ShowRootDse.java,v 1.3 2009-08-05 11:50:54 efernandez Exp $
 */
public class ShowRootDse extends DsAction{

        /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "showRootDse";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ShowRootDse.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ShowRootDse() {
        super("Root DSE Info", java.awt.event.KeyEvent.VK_W, Utils.createImageIcon(Utils.ICON_ROOT_DSE));
    }
    
    public void actionPerformed(ActionEvent arg0) {
        LDAPNode selectedNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        TreeRootNode rootNode = (TreeRootNode) selectedNode.getRoot();//TreeRootNode) selectedNode.getRoot();
        BrowserPanel browserPanel =
                Utils.getMainWindow().getBrowserPanelByTitle(rootNode.getLdapServer().getConnectionData().getConfigurationName());

        if (!browserPanel.getSearchResultsPanel().isShownRootDse()) {
            try {
                TreeRootDseRootNode rootDseRootNode = new TreeRootDseRootNode(rootNode.getLdapServer());

                if (browserPanel.getSearchResultsPanel().getBrowserData().size() > 0) {
                    browserPanel.searchResultsPanel.addBrowserPanel(rootDseRootNode, rootDseRootNode);
                } else {
                    browserPanel.searchResultsPanel.addBrowserPanel(rootNode, selectedNode);
                    browserPanel.searchResultsPanel.addBrowserPanel(rootDseRootNode, rootDseRootNode);
                }
                browserPanel.setTreeData(rootDseRootNode);

                browserPanel.validate();
                browserPanel.repaint();
            } catch (Exception ex) {
                logger.error("Error showing root dse " + ex);
            }
        }
    }

}
