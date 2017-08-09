package org.symlabs.browser;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.apache.log4j.Logger;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.apache.log4j.Logger;
import org.symlabs.actions.tab.CloseTreeRootNode;
import org.symlabs.actions.search.RefreshSearch;
import org.symlabs.actions.search.SaveSearch;
import org.symlabs.actions.search.SearchProperties;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: BrowserPanelStatus </p>
 * <p>Descripcion: Class that stores the status of a connection</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BrowserDataStatus.java,v 1.14 2009-08-27 10:53:38 efernandez Exp $
 */
public class BrowserDataStatus {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BrowserDataStatus.class);
    /**Attribute that stores the tree root node displayed in the browser panel*/
    private TreeRootNode treeRootNode;
    /**Attribute that stores the selected node in the browser panel*/
    private LDAPNode selectedNode;
    /**Attribute that identifies this tree data with a button of the panel*/
    private JButton button;
    /**Attribute used to store the popup menu of the button*/
    private JPopupMenu popupMenu;

// <editor-fold defaultstate="collapsed" desc=" Getter and Setter methods ">
    public LDAPNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(LDAPNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeRootNode getTreeRootNode() {
        return treeRootNode;
    }

    public void setTreeRootNode(TreeRootNode treeRootNode) {
        this.treeRootNode = treeRootNode;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    /**Method that changes the color of the button if it is enabled, if it is disabled then it is setted the default color
     * 
     * @param enabled boolean
     */
    public void setButtonEnabled(boolean enabled){
        if(enabled){
            this.button.setBackground(Utils.COLOR_WHITE_GREY);
        }else{
            this.button.setBackground(Utils.DEFAULT_BUTTON_COLOR); //this is the default color
        }
    }
// </editor-fold>
    /**Constructor: creates a new instance of BrowserStatusPanel.
     * It stores the values of the treeRootNode and the selected node given as argument.
     * 
     * @param treeRootNode TreeRootNode
     * @param selectedNodePath LDAPNode
     */
    public BrowserDataStatus(TreeRootNode treeRootNode, LDAPNode selectedNodePath) {
        this.treeRootNode = treeRootNode;
        this.selectedNode = selectedNodePath;
        this.button = new JButton(this.treeRootNode.myRDN);
        this.button.setAction(new org.symlabs.actions.tab.LoadTreeRootNode(this));
        if (!(treeRootNode instanceof TreeSearchRootNode)) {
            this.button.setText("");
            logger.trace("WE SET THE TEXT OF THIS BUTTON AS AN EMPTY STRING !!!!!!!!!!!" + treeRootNode.myDN);
        }
        logger.trace("selectedNode.mydn:" + selectedNodePath.myDN + ",rdn:" + selectedNodePath.myRDN);

        this.button.addMouseListener(this.initPopup());
    }

    private MouseListener initPopup() {
        this.popupMenu = new JPopupMenu();
        this.popupMenu.add(Utils.getAction(CloseTreeRootNode.HashKey));
        if (this.treeRootNode instanceof TreeSearchRootNode) {
            this.popupMenu.add(Utils.getAction(RefreshSearch.HashKey));
            TreeSearchRootNode searchRootNode = (TreeSearchRootNode) this.treeRootNode;
            if (!searchRootNode.isStored()) {
                this.popupMenu.add(Utils.getAction(SaveSearch.HashKey));
            }else{
                this.popupMenu.add(Utils.getAction(SearchProperties.HashKey));
            }
        }

        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (treeRootNode instanceof TreeSearchRootNode || 
                        treeRootNode instanceof TreeSchemaRootNode ||
                        treeRootNode instanceof TreeRootDseRootNode) {
                    if (e.isPopupTrigger()) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                    mousePressed(e);
            }
        };
    }
}
