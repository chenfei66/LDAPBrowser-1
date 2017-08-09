package org.symlabs.actions.tab;

import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserDataStatus;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: LoadTreeRootNode </p>
 * <p>Descripcion: Action that loads the treeRootNode in its panel. 
 * This action is called when it is pressed a button to switch searches</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LoadTreeRootNode.java,v 1.3 2009-07-30 18:26:09 efernandez Exp $
 */
public class LoadTreeRootNode extends DsAction {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LoadTreeRootNode.class);
    /**Attribute that stores the data of a browser*/
    private BrowserDataStatus browserData;

    /**Contructor: Creates a new instance of LoadTreeRootNode.
     * 
     * @param browserData BrowserDataSatus
     */
    public LoadTreeRootNode(BrowserDataStatus browserData) {
        super(browserData.getTreeRootNode().myDN, java.awt.event.KeyEvent.VK_A, browserData.getTreeRootNode().getImageIcon());
        this.browserData = browserData;
    }

    public void actionPerformed(ActionEvent e) {
        
        // <editor-fold defaultstate="collapsed" desc=" We update the selected node ">

        //We get the browser panel
        BrowserPanel browserPanel =
                Utils.getMainWindow().getBrowserPanelByTitle(this.browserData.getTreeRootNode().getLdapServer().getConnectionData().getConfigurationName());

        //We get the current data displayed in the browser panel
        BrowserDataStatus currentData = browserPanel.getSearchResultsPanel().getBrowserData(browserPanel.getTreeRootNode());

        //We get the selected path
        TreePath currentSelectedPath = browserPanel.getTreeRootNode().getTree().getSelectionPath();
        LDAPNode currentSelectedNode = (LDAPNode) currentSelectedPath.getLastPathComponent();

        if (currentSelectedPath != null) {
            //We update the selected node for this tree
            currentData.setSelectedNode(currentSelectedNode);
            logger.trace("current selected node: " + currentSelectedNode.myDN);
        }

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the tree root node in the browser panel of this connection ">
        //We set the tree data
        browserPanel.setTreeData(this.browserData.getTreeRootNode());

        browserPanel.getLdapTree().setDragEnabled(true);
        browserPanel.getLdapTree().setExpandsSelectedPaths(true);

        //We set the selected node
        logger.trace("selectedNode.myDN:" + this.browserData.getSelectedNode().myDN + ",rdn:" + this.browserData.getSelectedNode().myRDN);
        LDAPNode selected = Utils.findNodeByDN(this.browserData.getSelectedNode().myDN, this.browserData.getTreeRootNode());
        if (selected != null) {
            browserPanel.getLdapTree().setSelectionPath(new TreePath(selected.getPath()));
        } else {
            browserPanel.getLdapTree().setSelectionRow(0);
        }

        //We repaint the browser
        browserPanel.validate();
        browserPanel.repaint();

    // </editor-fold>
        
        browserPanel.getSearchResultsPanel().setSelectedButton(this.browserData);
    }
}
