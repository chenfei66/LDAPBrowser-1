package org.symlabs.browser;

import org.symlabs.browser.editor.EditorTable;
import org.symlabs.browser.editor.EditorPanel;
import org.symlabs.browser.editor.EditorForm;
import org.symlabs.browser.editor.EditorLdif;
import org.symlabs.browser.editor.EditorFieldValue;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.symlabs.actions.editor.AddFieldValueEditor;
import org.symlabs.actions.editor.AddLdifEditor;
import org.symlabs.actions.editor.AddTableEditor;
import org.symlabs.actions.editor.CloseAllEditor;
import org.symlabs.actions.rootdse.ShowRootDse;
import org.symlabs.actions.schema.ShowSchema;
import org.symlabs.actions.tab.CloseAllTabs;
import org.symlabs.actions.tab.CloseTab;
import org.symlabs.actions.tab.TabProperties;
import org.symlabs.actions.tab.SaveAsTab;
import org.symlabs.actions.tab.SaveTab;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.search.SearchResultsPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: BrowserPanel </p>
 * <p>Descripcion: Class that manages the browser panel. This is the panel that contains the tree of the current connection. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BrowserPanel.java,v 1.39 2009-09-29 09:45:21 efernandez Exp $
 */
public class BrowserPanel extends javax.swing.JPanel {

    /**Attribute that contains the root node of the tree displayed in the JTree of this panel*/
    private TreeRootNode treeRootNode;
    /**Attribute used to manage the popup menu for this panel*/
    public JPopupMenu popupMenu = null;
    /**Attribute that contains the actions supported for this panel*/
    private ArrayList<String> actionsList = null;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BrowserPanel.class);
    /**Attribute used to know if the fieldValueEditor is displayed*/
    private boolean fieldValueEditor;
    /**Attribute used to know if the formEditor is displayed*/
    private boolean formEditor;
    /**Attribute used to know if the TableEditor is displayed*/
    private boolean tableEditor;
    /**Attribute used to know if the LDIFEditor is displayed*/
    private boolean ldifEditor;
    /**Attribute used to know if the search panel is displayed*/
    private boolean searchPanel;

    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter Methods ">
    /**Method that returns the TreeRootNode of this node
     * 
     * @return TreeRootNode. This is the root node of the tree displayed
     */
    public TreeRootNode getTreeRootNode() {
        return treeRootNode;
    }

    /**Method that returns the JTree of the connection
     * 
     * @return JTree. This is the JTree of the connection
     */
    public JTree getLdapTree() {
        return ldapTree;
    }

    /**Method that sets a JTree as the tree of the connection
     * 
     * @param tree JTree. This is the JTree to set as tree
     */
    public void setLdapTree(JTree tree) {
        this.ldapTree = tree;
    }

    /**Method that returns if the field-value editor is being displayed.
     * 
     * @return boolean. True it exists. False it does not exist.
     */
    public boolean isFieldValueEditor() {
        return fieldValueEditor;
    }

    /**Method that sets the value. 
     * True it means that this panel is displayed or going to be displayed.
     * False it means that it is not,
     * 
     * @param fieldValueEditor boolean. True -it is displayed. False it does not.
     */
    public void setFieldValueEditor(boolean fieldValueEditor) {
        this.fieldValueEditor = fieldValueEditor;
    }

    /**Method that returns if the form editor is being displayed.
     * 
     * @return boolean. It exists. False it does not,
     */
    public boolean isFormEditor() {
        return formEditor;
    }

    /**Method that sets the value.
     * 
     * @param formEditor boolean. 
     * True it means the this editor is displayed or going to be displayed.
     * False it means that it is not.
     */
    public void setFormEditor(boolean formEditor) {
        this.formEditor = formEditor;
    }

    /**Method that returns if the ldif editor is being displayed
     * 
     * @return boolean. True - It exists. False it is not.
     */
    public boolean isLdifEditor() {
        return ldifEditor;
    }

    /**Method that sets the value.
     * 
     * @param ldifEditor boolean.
     * True - it means that this editor is diaplyed or going to be displayed.
     * False - It means that it is not.
     */
    public void setLdifEditor(boolean ldifEditor) {
        this.ldifEditor = ldifEditor;
    }

    /**Method that returns the right tabbed panel.
     * This panel contains the editors.
     * 
     * @return JTabbedPane. It is returned the right panel
     */
    public JTabbedPane getRightTabbedPane() {
        return rightTabbedPane;
    }

    /**Method that returns if the search panel is displayed.
     * 
     * @return boolean. 
     * True it means that this panel is displayed.
     * False it means that this panel is not displayed.
     */
    public boolean isSearchPanel() {
        return searchPanel;
    }

    /**Method that sets the value.
     * 
     * @param searchPanel boolean. 
     * True it means that this panel is displayed or going to be displayed.
     * False it menas that it is not,
     */
    public void setSearchPanel(boolean searchPanel) {
        this.searchPanel = searchPanel;
    }

    /**Method that returns if the table editor is displayed.
     * 
     * @return boolean. 
     * True it means that this editor is displayed.
     * False it means that it is not.
     */
    public boolean isTableEditor() {
        return tableEditor;
    }

    /**Method that sets the value.
     * 
     * @param tableEditor boolean.
     * True - it means that the table editor is displayed or going to be displayed.
     * False- it means that it is not.
     */
    public void setTableEditor(boolean tableEditor) {
        this.tableEditor = tableEditor;
    }

    public SearchResultsPanel getSearchResultsPanel() {
        return searchResultsPanel;
    }

// </editor-fold>
    /**Method that returns the selected current node
     * 
     * @return LDAPNode. This is the current node that it is being selected at this moment
     */
    public LDAPNode getSelectedNode() {
        TreePath path = this.getLdapTree().getSelectionPath();

        if (path == null || path.getPathCount() < 1) {
            logger.info("There is not any node selected");
            this.getLdapTree().setSelectionRow(0);
            path = this.getLdapTree().getSelectionPath();
//            return null;
        }
        return (LDAPNode) path.getLastPathComponent();
    }

    /**Method that returns the current editor displayed
     * 
     * @return EditorPanel. This is the editor displayed
     */
    public EditorPanel getCurrentEditor() {
        EditorPanel panel = (EditorPanel) this.getRightTabbedPane().getSelectedComponent();
        return panel;
    }

    /**Method that sets as current editor the DefaultTitleEditor given by the argument typeEditor.
     * 
     * @param typeEditor String. This is the editor type: field-value, table, form, ldif.
     * This field must be take a value given by the field DefaultEditorTitle in each editor class.
     */
    public void setCurrentEditor(String typeEditor) {
        JTabbedPane tabPane = this.getRightTabbedPane();
        for (int i = 0; i < tabPane.getTabCount(); i++) {
            if (tabPane.getTitleAt(i).equalsIgnoreCase(typeEditor)) {
                tabPane.setSelectedIndex(i);
                break;
            }
        }
    }

    /**Method that close all editors
     * 
     */
    public void closeAllEditor() {
        Utils.getAction(CloseAllEditor.HashKey).getAction().actionPerformed(null);
    }

    /**Method that open all the editors
     * 
     */
    public void openAllEditor() {
        if (!this.isFieldValueEditor()) {
            Utils.getAction(AddFieldValueEditor.HashKey).getAction().actionPerformed(null);
        }

        if (!this.isTableEditor()) {
            Utils.getAction(AddTableEditor.HashKey).getAction().actionPerformed(null);
        }

//        if (!this.isFormEditor()) {
//            Utils.getAction(AddFormEditor.HashKey).getAction().actionPerformed(e);
//        }

        if (!this.isLdifEditor()) {
            Utils.getAction(AddLdifEditor.HashKey).getAction().actionPerformed(null);
        }
    }


    
        /**Method that searches in the current root the dn given as argument
     * 
     * @param dnToSearch String.  this is the dn to seach
     * @return LDAPNode. This is the dn given. If the node was not found it is returned null.
     */
    public LDAPNode searchNodeInTreeRootNode(String dnToSearch) {
        return this.treeRootNode.findNodeByDN(dnToSearch, this.treeRootNode);
    }

    /**Constructor: Initializes the components and attributes needed to load the browser panel
     * 
     * @param host String. This is the host used to connect to LDAP server. 
     * @param port String. This is the port used to connect to LDAP server.
     * @param authid String. This is the user id used to connect to LDAP server.
     * @param authpw char[]. This is the user password used to connect to LDAP server.
     * @param suffix String. This is the suffix given in the window connection
     * @param suffixes String[]. These are the naming contexts found in the ldap server
     * @param indexLdapVersion int. This is the index of the ldap version selected. This index must be contained in LDAP_SUPPORTED_VERSION
     * @param rootBookmarkFolder BookmarkFolder.
     * @param rootSearchFolder SearchFolder.
     * @param configurationName String. This is the name of the configuration saved. If the conf has not been saved then it will be null
     * @param iconPath String.
     * @throws java.lang.Exception If an exception is found it will be thrown an exception
     */
    public BrowserPanel(String host, String port, String authid, char[] authpw, String suffix, int indexLdapVersion,
            BookmarkFolderNode rootBookmarkFolder, SearchFolderNode rootSearchFolder, String configurationName,String iconPath) throws Exception {
        initComponents();

        // <editor-fold defaultstate="collapsed" desc=" Adds the accions ">
        //Actions supported
        this.actionsList = new ArrayList<String>();
        this.actionsList.add(CloseTab.HashKey);
        this.actionsList.add(CloseAllTabs.HashKey);
        this.actionsList.add(SaveAsTab.HashKey);
        this.actionsList.add(SaveTab.HashKey);
        this.actionsList.add(TabProperties.HashKey);
        this.actionsList.add(ShowSchema.HashKey);
        this.actionsList.add(ShowRootDse.HashKey);

        // </editor-fold>

        //We create the root node 
        this.treeRootNode = new TreeRootNode(host, port, authid, authpw, suffix, indexLdapVersion, rootBookmarkFolder, rootSearchFolder, configurationName,iconPath);
        
        if(configurationName==null){//It means that this configuration was not still been stored
            configurationName = this.treeRootNode.getLdapServer().getDefaultConfigurationName();
            this.treeRootNode.getLdapServer().getConnectionData().setConfigurationName(configurationName);
            this.treeRootNode.getLdapServer().getConnectionData().setDirty(true);
        }

        //We add this panel to the tab panel in BrowserMainWindow
        Utils.getMainWindow().addNewConnection(configurationName, this);

        //Sets as selected this panel
        Utils.getMainWindow().setSelectedConnection(this);

        //We call to initProperties to load the tree nodes from the ldap server and set the model to the current ldapTree
        initProperties();
    }

    private void setEnabledActions() {

    }

    private void enableActions() {
        this.setEnabledActions();
    }

    public JPopupMenu getPopupMenu() {
        return this.createPopUpMenu();
    }

    private JPopupMenu createPopUpMenu() {
        enableActions();
        popupMenu = new JPopupMenu();
        
        for (int i = 0; i < actionsList.size(); i++) {
            String actionHashKey = actionsList.get(i);
            popupMenu.add(Utils.getAction(actionHashKey).getMenuItem());
        }
        return popupMenu;
    }

    /**Method that adds the tabpanel given as argument to the rigth tabpane
     * 
     * @param title String. This is the title of the tab 
     * @param panel Panel. This is the panel to add
     */
    public void addTabPane(String title, Panel panel) {
        this.rightTabbedPane.addTab(title, panel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        browserSplitPane1 = new javax.swing.JSplitPane();
        browserScrollPane = new javax.swing.JScrollPane();
        ldapTree = new javax.swing.JTree();
        rightPanel = new javax.swing.JPanel();
        rightTabbedPane = new javax.swing.JTabbedPane();
        searchResultsPanel = new org.symlabs.browser.search.SearchResultsPanel();

        setLayout(new java.awt.GridBagLayout());

        browserSplitPane1.setDividerLocation(170);
        browserSplitPane1.setDividerSize(4);

        browserScrollPane.setViewportView(ldapTree);

        browserSplitPane1.setLeftComponent(browserScrollPane);

        rightPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightPanel.add(rightTabbedPane, gridBagConstraints);

        browserSplitPane1.setRightComponent(rightPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(browserSplitPane1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(searchResultsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane browserScrollPane;
    private javax.swing.JSplitPane browserSplitPane1;
    private javax.swing.JTree ldapTree;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTabbedPane rightTabbedPane;
    public org.symlabs.browser.search.SearchResultsPanel searchResultsPanel;
    // End of variables declaration//GEN-END:variables
    
    /**Method that initializes the attributes and call to the methods needed to create a treeRootNode.
     * 
     */
    private void initProperties() {
        boolean isSearch=false;
        if(this.treeRootNode instanceof TreeSearchRootNode){
            isSearch=true;
        }
        // <editor-fold defaultstate="collapsed" desc=" Initializes the boolean editors field ">
        //We initializes the value of the editors
        this.fieldValueEditor = false;
        this.formEditor = false;
        this.ldifEditor = false;
        this.tableEditor = false;
        this.searchPanel = false;

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Sets the tree model ">
        //We create two new model, for the model and the tree attribute in treeRootNode
        DefaultTreeModel model = new DefaultTreeModel(this.treeRootNode, true);
        DefaultTreeModel model2 = new DefaultTreeModel(this.treeRootNode, true);

        //We set the model
        this.getLdapTree().setModel(model);
        this.getLdapTree().setShowsRootHandles(true);

        this.treeRootNode.setTree(this.getLdapTree());
        this.treeRootNode.setModel(model2);

        // </editor-fold>

        
        if(!isSearch){
            //We load the suffixes from the ldap server
            this.treeRootNode.loadLDAPSuffix();
        }

        //We get the schema 
        this.treeRootNode.getLdapServer().createSchema();

        //We set the tooltiptext
        ToolTipManager.sharedInstance().registerComponent(this.getLdapTree());

        //We set the treecellrenderer to set the nodes icons and the tooltiptext icons
        this.treeRootNode.setTreeCellRenderer();

        //We add the listener to the tree
        this.treeRootNode.setTreeListener();

        //We set the rootnode tree
        this.getLdapTree().setModel(this.treeRootNode.getModel());

        // <editor-fold defaultstate="collapsed" desc=" Set the connection tab title ">
        //We set the title for the new tab
        String tabTitle = null;
        if (this.treeRootNode.getLdapServer().getConnectionData().getConfigurationName() != null) {
            tabTitle = this.treeRootNode.getLdapServer().getConnectionData().getConfigurationName();
        } else {
            tabTitle = this.getTreeRootNode().getLdapServer().getHost() + ":" + this.getTreeRootNode().getLdapServer().getPort();
        }
        Utils.getMainWindow().setTitleCurrentTabPanel(tabTitle);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Adds all editors panel ">
        //Adds the currentPanel.
        //Each action adds this panel to the current browser panel and sets the boolean field of each editor true.
        Utils.getAction(AddFieldValueEditor.HashKey).getAction().actionPerformed(null);
        Utils.getAction(AddTableEditor.HashKey).getAction().actionPerformed(null);
//        Utils.getAction(AddFormEditor.HashKey).getAction().actionPerformed(null);
        Utils.getAction(AddLdifEditor.HashKey).getAction().actionPerformed(null);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Set the mouseListener for the editors ">

        // <editor-fold defaultstate="collapsed" desc=" EditorPanel mouseListener ">

        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!Utils.isEditableMode()) {
                    EditorPanel editor = null;
                    try {
                        int index = getRightTabbedPane().indexAtLocation(e.getX(), e.getY());
                        if (index >= 0 && index < getRightTabbedPane().getTabCount()) {
                            editor = (EditorPanel) getRightTabbedPane().getComponentAt(index);
                        }
                    } catch (Exception ex) {
                        logger.error("Exception found in mousePressed: " + ex);
                    }
                    if (editor == null) {
                        return;
                    }

                    if (editor.getEditorType().equalsIgnoreCase(EditorFieldValue.DEFAULT_EDITOR_TITLE)) {
                        getRightTabbedPane().setSelectedComponent(editor);
                    } else if (editor.getEditorType().equalsIgnoreCase(EditorTable.DEFAULT_EDITOR_TITLE)) {
                        getRightTabbedPane().setSelectedComponent(editor);
                    } else if (editor.getEditorType().equalsIgnoreCase(EditorForm.DEFAULT_EDITOR_TITLE)) {
                        getRightTabbedPane().setSelectedComponent(editor);
                    } else if (editor.getEditorType().equalsIgnoreCase(EditorLdif.DEFAULT_EDITOR_TITLE)) {
                        getRightTabbedPane().setSelectedComponent(editor);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed(e);
            }
        };

        // </editor-fold>

        //We remove all mouse listeners
        MouseListener[] mouseListeners = this.getRightTabbedPane().getMouseListeners();
        if (mouseListeners != null) {
            for (int i = 0; i < mouseListeners.length; i++) {
                this.getRightTabbedPane().removeMouseListener(mouseListeners[i]);
            }
        }
        //We set our mouse listener
        this.getRightTabbedPane().addMouseListener(ml);

        // </editor-fold>

        //Sets selected the first row of the tree
        this.getLdapTree().setSelectionRow(0);
    }

    /**Method that sets the tree given as argument in the panel, so the information displayed is replaced by the tree info
     * 
     * @param treeRootNode
     */
    public void setTreeData(TreeRootNode treeRootNode) {

        //We create the root node 
        this.treeRootNode = treeRootNode;

        //Sets as selected this panel
        Utils.getMainWindow().setSelectedConnection(this);

        //We call to initProperties to load the tree nodes from the ldap server and set the model to the current ldapTree
        initProperties();
    }
}
