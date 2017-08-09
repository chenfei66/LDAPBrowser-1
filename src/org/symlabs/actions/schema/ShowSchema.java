package org.symlabs.actions.schema;

import java.util.logging.Level;
import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ShowSchema </p>
 * <p>Descripcion: Action that schows the schema of the current connection. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ShowSchema.java,v 1.4 2009-08-04 13:55:27 efernandez Exp $
 */
public class ShowSchema extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "showSchema";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ShowSchema.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ShowSchema() {
        super("Show schema", java.awt.event.KeyEvent.VK_W, Utils.createImageIcon(Utils.ICON_SCHEMA));
    }

    public void actionPerformed(ActionEvent e) {
        LDAPNode selectedNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        TreeRootNode rootNode = (TreeRootNode) selectedNode.getRoot();
        BrowserPanel browserPanel =
                Utils.getMainWindow().getBrowserPanelByTitle(rootNode.getLdapServer().getConnectionData().getConfigurationName());

        if (!browserPanel.getSearchResultsPanel().isShownSchema()) {
            try {
                TreeSchemaRootNode schemaRootNode = new TreeSchemaRootNode(rootNode.getLdapServer());

                if (browserPanel.getSearchResultsPanel().getBrowserData().size() > 0) {
                    browserPanel.searchResultsPanel.addBrowserPanel(schemaRootNode, schemaRootNode);
                } else {
                    browserPanel.searchResultsPanel.addBrowserPanel(rootNode, selectedNode);
                    browserPanel.searchResultsPanel.addBrowserPanel(schemaRootNode, schemaRootNode);
                }
                browserPanel.setTreeData(schemaRootNode);

                browserPanel.validate();
                browserPanel.repaint();
            } catch (Exception ex) {
                logger.error("Error showing schema " + ex);
            }
        }
    }
}

