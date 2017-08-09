package org.symlabs.nodes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.symlabs.actions.node.MoveDown;
import org.symlabs.actions.node.MoveUp;
import org.symlabs.util.Actions;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: LDAPBrowserNode </p>
 * <p>Descripcion: Class that implements the methods and attributes of the ldap tree </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPBrowserNode.java,v 1.33 2009-08-25 17:22:26 efernandez Exp $
 */
public class LDAPBrowserNode extends DefaultMutableTreeNode implements TreeNode {

    /**Attribute that contains the name of the node*/
    public String myName;
    /**Attribute that contains the type of the node*/
    public String type;
    /**Attribute which contains the Distinguished Name*/
    public String myDN;
    /**Attribute which contains the Relative Distinguished Name*/
    public String myRDN;
    /**Attribute that contains the actions displayed*/
    public Hashtable<String, String> supportedActions = null;
    /**Attribute that contains the actions supported for this node*/
    public ArrayList<String> actionsList = null;
    /**Attribute used to manage the popup menu for this node*/
    public JPopupMenu popupMenu = null;
    /**Attribute that contains the default icon for this node*/
    private ImageIcon myIcon;
    /**Attribute that contains the path for this icon*/
    protected String myIconPath = Utils.ICON_DEFAULT_NODE;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPBrowserNode.class);

    /**Contructor: creates a new instance of LDAPBrowserNode.
     * 
     */
    public LDAPBrowserNode() {
        this.type = "LDAPBrowserNode";
        this.children = new Vector();
        this.parent = null;
        this.actionsList = new ArrayList<String>();
        this.supportedActions = new Hashtable();
        // <editor-fold defaultstate="collapsed" desc=" Nodes Actions ">
        //Nodes actions
        this.actionsList.add(MoveUp.HashKey);
        this.actionsList.add(MoveDown.HashKey);

        // </editor-fold>
        this.myIcon = createImageIcon(this.myIconPath);
    }

    public ArrayList<String> getDisabledActionKeys() {
        ArrayList<String> disabledActionKeys = new ArrayList<String>();
        for (int i = 0; i < this.actionsList.size(); i++) {
            if (!(this.supportedActions.containsKey(this.actionsList.get(i))) ||
                    !(this.supportedActions.get(this.actionsList.get(i)).equalsIgnoreCase("yes"))) {
                disabledActionKeys.add(this.actionsList.get(i));
            }
        }
        return disabledActionKeys;
    }

    public ArrayList<String> getEnabledActionKeys() {
        ArrayList<String> enabledActionKeys = new ArrayList<String>();
        for (int i = 0; i < this.actionsList.size(); i++) {
            if (this.supportedActions.containsKey(this.actionsList.get(i)) && this.supportedActions.get(this.actionsList.get(i)).equalsIgnoreCase("yes")) {
                enabledActionKeys.add(this.actionsList.get(i));
            }
        }
        return enabledActionKeys;
    }

    /**Method that returns the path that contains the image icon for this node
     * 
     * @return String. this is the image icon path
     */
    public String getMyIconPath() {
        return myIconPath;
    }

    /**Method that sets a new path and a new image icon for this node
     * 
     * @param myIconPath String. This is the path that contains the image icon to be setted
     */
    public void setMyIconPath(String myIconPath) {
        this.myIconPath = myIconPath;
        this.setImageIcon(createImageIcon(this.myIconPath));
    }

    /**Method that sets the renderer of this jtree node, it is used to set the tooltiptext and the node image icon
     * 
     */
    public void setTreeCellRenderer() {
        ((TreeRootNode) this.getRoot()).getTree().setCellRenderer(new LDAPNodeTreeCellRenderer());
    }

    /**Method that returns the image icon of this node
     * 
     * @return Icon. This is the image icon of this node
     */
    public Icon getImageIcon() {
        if (this.myIcon == null) {
            this.myIcon = createImageIcon(Utils.ICON_DEFAULT_NODE);
        }
        return (Icon) this.myIcon;
    }

    /**Method that sets the new image icon
     * 
     * @param newImageIcon ImageIcon. This is the new image icon to be setted
     */
    private void setImageIcon(ImageIcon newImageIcon) {
        this.myIcon = newImageIcon;
    }

    /**Method that sets the listener of this node. It is used to set the popup menu
     * 
     */
    public void setTreeListener() {
        if (((TreeRootNode) this.getRoot()).getTree() != null) {

            ((TreeRootNode) this.getRoot()).getTree().addTreeWillExpandListener(new LDAPNodeTreeWillExpandListener());
            ((TreeRootNode) this.getRoot()).getTree().addTreeSelectionListener(new LDAPNodeTreeSelectionListener());


            MouseListener ml = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (!Utils.isEditableMode()) {
                        int selRow = ((TreeRootNode) getRoot()).getTree().getRowForLocation(e.getX(), e.getY());

                        TreePath path = ((TreeRootNode) getRoot()).getTree().getPathForLocation(e.getX(), e.getY());
                        if (path == null || path.getPathCount() < 1) {
                            return;
                        }

                        LDAPNode node = (LDAPNode) path.getLastPathComponent();
                        ((TreeRootNode) getRoot()).getTree().setSelectionPath(path);
                        //Set enabled and disabled the actions
                        Actions.setEnabledActionsNode(node);

                        //Add the popup menu if it is called
                        if (e.isPopupTrigger()) {
                            logger.trace("We show the LDAPNode popup menu");
                            if (node.hasPopupMenu()) {
//                                node.createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mousePressed(e);
                }
                };
                logger.trace("Aqui!!!!!!!! ");
            ((TreeRootNode) getRoot()).getTree().addMouseListener(ml);
        }
    }

    /**Method that adds a new child to the current node
     * 
     * @param child LDAPBrowserNode. This is the child node to be added
     */
    public void addChild(LDAPBrowserNode child) {
        for(int i=0;i<this.getChildCount();i++){
            if(((LDAPBrowserNode) this.getChildAt(i)).myDN.equals(child.myDN)){
                return;
            }
        }
        children.add(child);
        child.parent = this;
    }

    /**Method that returns the number of children
     * 
     * @return int. This is the number of children
     */
    @Override
    public int getChildCount() {
        return this.children.size();
    }

    /**Method that returns a TreeNode. This is the child given by the argument param
     * 
     * @param param int. This is the index of the child
     * @return TreeNode. This is the node of the child 
     */
    @Override
    public TreeNode getChildAt(int param) {
        return (TreeNode) children.get(param);
    }

    /**Method that returns the index of this node
     * 
     * @param treeNode TreeNode. this is the child node which you want to get its index
     * @return int. This is the index of the treeNode
     */
    @Override
    public int getIndex(TreeNode treeNode) {
        return children.indexOf(treeNode);
    }

    /**Method that remove all children of this node
     * 
     */
    public void deleteAll() {
        children.removeAllElements();
    }

    /**Method that returns the tree root.
     * The parent node is the node which parent is null.
     * 
     * @return TreeNode. This is the tree root node
     */
    @Override
    public TreeNode getRoot() {
        LDAPBrowserNode t, lastt;
        if (parent == null) {
            return this;
        }
        t = (LDAPBrowserNode) parent;
        lastt = t;
        while (t != null) {
            lastt = t;
            t = (LDAPBrowserNode) t.parent;
        }
        return lastt;
    }

    /**Method that returns a boolean that tells you if this node is a leaf (true), or if it is not (false).
     * This node is a leaf is it does not have anyone child
     * 
     * @return boolean. True if it is a leaf node, False if it is not.
     */
    @Override
    public boolean isLeaf() {
        if (getChildCount() != 0) {
            return false;
        } else {
            return true;
        }
    }

    /**Method that returns the string of the node that is going to be displayed in the JTree.
     * 
     * @return String. This is the string to be displayed for this node.
     */
    @Override
    public String toString() {
        return this.myName;
    }

    /**Method that returns true if this node has a popup menu. It returns false if it is not.
     * 
     * @return boolean. True - it has a popupmenu. False - It does not have a popup menu
     */
    public boolean hasPopupMenu() {
        return (canCreateChildren() || isRemovable() || actionsList != null);
    }

    /**Method that returns True is this node can create children, false if it is not.
     * 
     * @return boolean. True - this node can create children. False - it can not.
     */
    public boolean canCreateChildren() {
        return false;
    }

    /**Method that returns True if this node can be removed. False if it can not.
     * 
     * @return boolean. True - this node can be removed. False - it can not.
     */
    public boolean isRemovable() {
        return false;
    }

    /**Method that sets enabled tha actions for the current node.
     * It enables the MoveUp/MoveDown based on the position in the parent child group
     * 
     */
    public void setEnabledActions() {
        supportedActions = new Hashtable();

        TreeNode parentNode = this.getParent();

        if (parentNode != null) {
            int idx = parentNode.getIndex(this);
            int count = parentNode.getChildCount();
             logger.trace("parentNode has " + count +" childs, and we are number "+idx);
            if (idx > 0) {
                supportedActions.put(MoveUp.HashKey, "yes");
            }
            if (idx < count - 1) {
                supportedActions.put(MoveDown.HashKey, "yes");
            }
        } else {
            logger.trace("parentNode is null!");
        }
    }

    /**Method that creates the popup menu and it is returned. 
     * The actions of the current node are enabled.
     * 
     * @return JPopupMenu. This is the popup menu of the current node.
     */
    public JPopupMenu createPopupMenu() {
        popupMenu = new JPopupMenu();

        for (int i = 0; i < actionsList.size(); i++) {
            String actionHashKey = actionsList.get(i);
            popupMenu.add(Utils.getAction(actionHashKey).getMenuItem());
        }

        return popupMenu;
    }


    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path String wih the full path of the resource to load as an Icon
     * @return new ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LDAPBrowserNode.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.error("Couldn't find file: " + path);
            return null;
        }
    }
}
