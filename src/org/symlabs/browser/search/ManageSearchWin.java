package org.symlabs.browser.search;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import org.symlabs.search.SavedSearch;
import org.symlabs.search.SearchNode;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.actions.search.LoadSearch;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.LDAPNodeTreeCellRenderer;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.search.SearchBaseNode;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.search.SearchParams;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchWin </p>
 * <p>Descripcion: Class which manages the searches of the selected connection. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ManageSearchWin.java,v 1.9 2009-08-24 09:01:06 efernandez Exp $
 */
public class ManageSearchWin extends javax.swing.JFrame implements TreeSelectionListener {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ManageSearchWin.class);
    /**Attribute that stores the old search parameters before a modification*/
    private SearchParams oldSearchParams;
    /**Attribute that stores the old name value, stores before a modification*/
    private String oldName;
    /**Attribute that stores the old description value, stores before a modification*/
    private String oldDescription;
    /**Attribute that stores the search parameters and gets the results of the search*/
    private SavedSearch savedSearch;
    /**Attribute that stores the ldap node*/
    private LDAPNode ldapNode;
    /**Attribute that identifies the view mode for this search editor*/
    private static final String VIEW_MODE = "viewSearch";
    /**Attribute that identifies the edit mode for this search editor*/
    private static final String EDIT_SEARCH_MODE = "editSearch";
    /**Attribute that identifies the edit mode for this search editor*/
    private static final String EDIT_FOLDER_MODE = "editSearchFolder";
    /**Attribute that identifies the add mode for thissearch editor*/
    private static final String ADD_SEARCH_MODE = "addSearch";
    /**Attribute that identifies the add mode for this search editor*/
    private static final String ADD_FOLDER_MODE = "addSearchFolder";
    /**Attribute that stores the mode for this editor*/
    private String editorMode = VIEW_MODE;
    /**Attribute that contains the bookmark base selected*/
    private SearchBaseNode searchBaseSelected;
    /**Attribute that contains the root node of the bookmarks displayed*/
    private SearchFolderNode rootFolder;
    /**Attribute that tell us if the window has been opened to save a search*/
    private boolean savingSearch;
    /**Attribute that contains the popup menu actions for cutting and copying a search*/
    private JPopupMenu popupMenuCopyAndCut;
    /**Attribute that contains the popup menu actions for pasting*/
    private JPopupMenu popupMenuPaste;
    /**Attribute that contains the search base selected for copy*/
    private SearchBaseNode baseToCopy;

    private MouseListener initPopup() {
        this.popupMenuCopyAndCut = new JPopupMenu();
        this.popupMenuPaste = new JPopupMenu();
        this.popupMenuCopyAndCut.add(new DsAction("Copy", java.awt.event.KeyEvent.VK_C, Utils.createImageIcon(Utils.ICON_COPY_FOLDER)) {

            public void actionPerformed(ActionEvent arg0) {
                SearchBaseNode baseSelected = (SearchBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected.getParent() != null) {
                    baseToCopy = (SearchBaseNode) baseSelected.clone();
                }
            }
        });

        this.popupMenuCopyAndCut.add(new DsAction("Cut", java.awt.event.KeyEvent.VK_P, Utils.createImageIcon(Utils.ICON_CUT)) {

            public void actionPerformed(ActionEvent arg0) {
                SearchBaseNode baseSelected = (SearchBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected.getParent() != null) {
                    SearchBaseNode parentNode = (SearchBaseNode) baseSelected.getParent();

                    SearchBaseNode rootNode = (SearchBaseNode) parentNode.getRoot();

                    baseToCopy = baseSelected;
                    int index = parentNode.getIndex(baseSelected);
                    parentNode.remove(index);
                }
            }
        });

        this.popupMenuPaste.add(new DsAction("Paste", java.awt.event.KeyEvent.VK_P, Utils.createImageIcon(Utils.ICON_PASTE)) {

            public void actionPerformed(ActionEvent arg0) {
                SearchBaseNode baseSelected = (SearchBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected instanceof SearchFolderNode) {
                    if (baseToCopy != null) {
                        int index = baseSelected.getChildCount();
                        boolean found = false;
                        SearchBaseNode childNode = null;
                        for (int i = 0; i < baseSelected.getChildCount(); i++) {
                            childNode = (SearchBaseNode) baseSelected.getChildAt(i);
                            if (childNode.getName().equalsIgnoreCase(baseToCopy.getName())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            baseSelected.insert(baseToCopy, index);

                            //We update the tree
                            Utils.getMainWindow().refreshSearchesMainWindow();
                            setNeedToSave();
                            tree.validate();
                            tree.repaint();
                            tree.updateUI();
                            baseToCopy = null;
                        } else {
                            String message = "This folder already exists.";
                            String details = "The parent folder already has an existing node with that name." +"\n"+
                                    "Please select a valid destination." + "\n"+
                                    "If you Close thisWindow or Click on Cancel Button you will lost the searches that you have cut unless you close this connection without saving.";
                            String title = "Error Moving Search";
                            showMessageDialog(title, message, details, MessageDialog.MESSAGE_WARNING);
                        }
                    }
                }
            }
        });


        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Object object = e.getSource();
                    if (object instanceof JTree) {
                        JTree tree = (JTree) object;
                        SearchBaseNode node = (SearchBaseNode) tree.getLastSelectedPathComponent();
                        if (node.getParent() != null && baseToCopy == null) {//We can cut and copy
                            logger.trace("We show the popup for copying and cutting");
                            popupMenuCopyAndCut.show(e.getComponent(), e.getX(), e.getY());
                        } else if (node instanceof SearchFolderNode && baseToCopy!=null) {//We can paste only in folder
                            logger.trace("We show the popup for pasting");
                            popupMenuPaste.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed(e);
            }
        };
    }
    
    /**Method that shows a message dialog with the arguments given
     * 
     * @param title String. This is the dialog title
     * @param message String. This is the message
     * @param details String. This is the details message
     * @param messageType int .This is the type message
     */
    private void showMessageDialog(String title, String message, String details, int messageType){
        MessageDialog dialog = new MessageDialog(this,title, message, details, messageType);
                            dialog.setLocationRelativeTo(this);
                            dialog.setVisible(true);
    }

    /** Creates new form SearchWin. Used to add a new search, and store it
     * 
     * @param node LDAPNode. This is the node base
     */
    public ManageSearchWin(LDAPNode node) {
        this.ldapNode = node;

        this.rootFolder = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getSearchRootFolder();
        this.initComponents();
        this.messageLabel.setVisible(false);
        this.setSize(750, 550);
        this.searchPanel.initProperties(this.ldapNode);
        this.setEditorMode(VIEW_MODE);
        if (rootFolder != null) {
            DefaultTreeModel model = new DefaultTreeModel(rootFolder);
            this.tree.setModel(model);
        } else {
            logger.error("Empty search tree");
        }

//We set the tree renderer
        this.tree.setCellRenderer(new LDAPNodeTreeCellRenderer());

        //We set the tooltiptext
        ToolTipManager.sharedInstance().registerComponent(this.tree);

        //We have to update the tree
        this.tree.validate();
        this.tree.repaint();

        this.tree.addTreeSelectionListener(this);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Sets selected the first row of the tree
        this.tree.setSelectionRow(0);

        //We update the search base selected
        this.searchBaseSelected = this.rootFolder;

        this.savingSearch = false;

        this.tree.addMouseListener(this.initPopup());
        this.baseToCopy = null;
    }

    /**Creates a new instance of Manage Search Win. Used to store an existing search
     * 
     * @param node LDAPNode. 
     * @param params SearchParams.
     */
    public ManageSearchWin(LDAPNode node, SearchParams params) {
        this(node);

        this.searchPanel.setSearchParamsFromPanel(params);
        this.setEditorMode(ADD_SEARCH_MODE);
        this.savingSearch = true;
    }

    /**Method that sets the editor mode
     * 
     * @param mode String
     */
    private void setEditorMode(String mode) {
        this.editorMode = mode;
        if (mode.equals(VIEW_MODE)) {
            this.removeButton.setEnabled(true);
            this.newSearchButton.setEnabled(true);
            this.newFolderButton.setEnabled(true);
            this.editButton.setEnabled(true);
            this.editButton.setText("Edit");
            this.editButton.setToolTipText("Edit the search displayed");
            this.searchButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
            this.cancelButton.setText("Close");
            this.cancelButton.setToolTipText("Close this window");
            this.searchButton.setEnabled(true);
            this.setEditableComponents(false);
            this.messageLabel.setVisible(false);
        } else if (mode.equals(EDIT_SEARCH_MODE)) {
            this.removeButton.setEnabled(false);
            this.newSearchButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setEnabled(true);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save the search displayed");
            this.searchButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore the initial values for this search");
            this.searchPanel.setVisible(true);
            this.searchButton.setEnabled(false);
            this.setEditableComponents(true);
        } else if (mode.equals(EDIT_FOLDER_MODE)) {
            this.removeButton.setEnabled(false);
            this.newSearchButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setEnabled(true);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save the search displayed");
            this.searchButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore the initial values for this search");
            this.searchPanel.setVisible(false);
            this.searchButton.setEnabled(false);
            this.setEditableComponents(true);
        } else if (mode.equals(ADD_SEARCH_MODE)) {
            this.removeButton.setEnabled(false);
            this.newSearchButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setEnabled(true);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save the search displayed");
            this.searchButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore the initial values for this search");
            this.searchPanel.setVisible(true);
            this.searchButton.setEnabled(false);
            this.setEditableComponents(true);
        } else if (mode.equals(ADD_FOLDER_MODE)) {
            this.removeButton.setEnabled(false);
            this.newSearchButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setEnabled(true);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save the search displayed");
            this.searchButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore the initial values for this search");
            this.searchPanel.setVisible(false);
            this.searchButton.setEnabled(false);
            this.setEditableComponents(true);
        }

        this.rightPanel.validate();
        this.rightPanel.repaint();
    }

    /**Method that sets the searchs node given as argument as selected.
     * Method used to edit a node of the search root node
     * 
     * @param searchNode SearchNode
     */
    public void setAsSelectedTheSearchNode(SearchNode searchNode) {
        logger.trace("We set as selected the node: " + searchNode.getName());
        TreePath treePath = new TreePath(searchNode.getPath());
//        if(this.tree.isPathSelected(treePath)){
        logger.trace("We set this node as selected");
        this.tree.setSelectionPath(treePath);
        this.setEditorMode(EDIT_SEARCH_MODE);
//        }else{
//            logger.trace("We set as selected the root node");
//            this.tree.setSelectionRow(0);
//        }
    }

    /**Method that saves the current selected tab
     * 
     */
    private void setNeedToSave() {
        ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getConnectionData().setDirty(true);
    }

    /**Method that stores the old values of the search node displayed
     * 
     */
    private void storeOldValues() {
        this.oldSearchParams = this.searchPanel.getSearchParamsFromPanel();
        this.oldName = this.nameTextField.getText();
        this.oldDescription = this.descriptionTextArea.getText();
    }

    /**Method that set the values given as argument in the search
     * 
     * @param params SearchParams
     */
    private void setValuesSearch(SearchParams params, String name, String description) {
        this.searchPanel.setSearchParamsFromPanel(params);
        this.nameTextField.setText(name);
        this.descriptionTextArea.setText(description);
    }

    /**Method that shows an error message when no folder is selected
     * 
     */
    private void showFolderNotFoundMessage() {
        String message = "Folder not found.";
        String details = "Please select a search folder to add a new search or a new folder.";
        String title = "Error Adding Search";
        MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**Method that shows an error message when already exists a folder or a bookmark with the name written in the same folder.
     * 
     */
    private void showNameAlreadyExistsMessage() {
        String message = "The name selected already exists.";
        String details = "The name selected already exists. Please select an other name..";
        String title = "Error in Search";
        MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**Method that returns an string with the fields with error
     * 
     * @param editorMode String. This is the mode to test
     * @return String. This string contains the error details.
     */
    private String getErrorCheckingSearchFields(String editorMode) {
        String error = "";
        if (editorMode.equals(EDIT_SEARCH_MODE) || editorMode.equals(ADD_SEARCH_MODE)) {
            error += this.searchPanel.getErrorCheckingSearchFields();
        }

        if (editorMode.equals(EDIT_SEARCH_MODE) || editorMode.equals(ADD_SEARCH_MODE) || editorMode.equals(EDIT_FOLDER_MODE) || editorMode.equals(ADD_FOLDER_MODE)) {
            if (this.nameTextField.getText().trim().equals("")) {
                error += "Error checking the name." + "\n";
            }

        }
        return error;
    }

    /**Method that test the fields of the bookmark type.
     * 
     * @param editorMode Stirng.
     * @return boolean. True means no error was found. False means error was found.
     */
    private boolean checkSearchFields(String editorMode) {
        String title = "";
        String message = "All the fields must be filled.";
        String detailsMessage = this.getErrorCheckingSearchFields(editorMode);
        boolean errorFound = false;
        if (editorMode.equals(EDIT_SEARCH_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Editing Search";
                errorFound =
                        true;
            }

        } else if (editorMode.equals(ADD_SEARCH_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Adding Search";
                errorFound =
                        true;
            }

        } else if (editorMode.equals(EDIT_FOLDER_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Editing Folder";
                errorFound =
                        true;
            }

        } else if (editorMode.equals(ADD_FOLDER_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Adding Folder";
                errorFound =
                        true;
            }

        }

        if (errorFound) {
            MessageDialog dialog = new MessageDialog(this,title, message, detailsMessage, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        return !errorFound;
    }

    /**Method that sets the components of the right panel as editable or no editable.
     * 
     * @param editable boolean. True= editable. False= non editable.
     */
    private void setEditableComponents(boolean editable) {
        this.nameTextField.setEditable(editable);
        this.descriptionTextArea.setEditable(editable);

        this.searchPanel.getBaseTextField().setEditable(editable);
        this.searchPanel.getFilterTextArea().setEditable(editable);
        this.searchPanel.getScopeComboBox().setEnabled(editable);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        commandPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        newFolderButton = new javax.swing.JButton();
        newSearchButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        rightPanel = new javax.swing.JPanel();
        searchInfoPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        searchPanel = new org.symlabs.browser.search.SearchPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        messagePanel = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search");
        setMinimumSize(new java.awt.Dimension(600, 400));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        commandPanel.setLayout(new java.awt.GridBagLayout());

        searchButton.setText("Go To Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(searchButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);

        newFolderButton.setText("New Folder");
        newFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFolderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(newFolderButton, gridBagConstraints);

        newSearchButton.setText("New Search");
        newSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSearchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(newSearchButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        getContentPane().add(commandPanel, gridBagConstraints);

        jSplitPane1.setDividerLocation(160);
        jSplitPane1.setDividerSize(4);

        rightPanel.setLayout(new java.awt.GridBagLayout());

        searchInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Info"));
        searchInfoPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        searchInfoPanel.add(nameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        searchInfoPanel.add(nameTextField, gridBagConstraints);

        descriptionScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        searchInfoPanel.add(descriptionScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        rightPanel.add(searchInfoPanel, gridBagConstraints);

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Params"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        rightPanel.add(searchPanel, gridBagConstraints);

        jSplitPane1.setRightComponent(rightPanel);

        treeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Tree Browser"));
        treeScrollPane.setViewportView(tree);

        jSplitPane1.setLeftComponent(treeScrollPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jSplitPane1, gridBagConstraints);

        messagePanel.setLayout(new java.awt.GridBagLayout());

        messageLabel.setText("Message Info");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        messagePanel.add(messageLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        getContentPane().add(messagePanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        logger.trace("It has been pressed Cancel Button, we are in editor mode: " + this.editorMode);
        if (this.editorMode.equals(VIEW_MODE)) {
            this.dispose();
        } else if (this.editorMode.equals(EDIT_SEARCH_MODE)) {
            this.setEditorMode(VIEW_MODE);
            this.setValuesSearch(oldSearchParams, oldName, oldDescription);
        } else if (this.editorMode.equals(EDIT_FOLDER_MODE)) {
            this.setEditorMode(VIEW_MODE);
            this.setValuesSearch(oldSearchParams, oldName, oldDescription);
        } else if (this.editorMode.equals(ADD_SEARCH_MODE)) {
            this.messageLabel.setVisible(false);
            this.setEditorMode(VIEW_MODE);
        } else if (this.editorMode.equals(ADD_FOLDER_MODE)) {
            this.setEditorMode(VIEW_MODE);
        }

        logger.trace("Now we are in mode: " + this.editorMode);

        this.valueChanged(null);

        //We set selected the new node
        this.tree.setSelectionPath(new TreePath(this.searchBaseSelected.getPath()));
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.searchBaseSelected instanceof SearchNode) {
                this.dispose();
                SearchNode searchNode = (SearchNode) this.searchBaseSelected;
                (new LoadSearch(Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode(), searchNode, true)).actionPerformed(evt);
            }

        } else {
            String message = "This option is only available in VIEW mode.";
            String title = "Error in Search";
            MessageDialog dialog = new MessageDialog(this,title, message, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            logger.trace("We are in VIEW_MODE");
            if (this.rootFolder != null) {
                this.storeOldValues();
                if (this.searchBaseSelected instanceof SearchFolderNode) {
                    logger.trace("We are in VIEW_MODE, and we want to edit a search folder");
                    this.setEditorMode(EDIT_FOLDER_MODE);
                } else if (this.searchBaseSelected instanceof SearchNode) {
                    logger.trace("We are in VIEW_MODE, and we want to edit a search");
                    this.setEditorMode(EDIT_SEARCH_MODE);
                }

            }
        } else if (this.editorMode.equals(EDIT_SEARCH_MODE)) {
            logger.trace("We are in EDIT_SEARCH_MODE, and we want to save a modification in a search");
            if (this.checkSearchFields(editorMode)) {

                SearchFolderNode parent = (SearchFolderNode) this.searchBaseSelected.getParent();
//                if (parent.canCreateChild(this.nameTextField.getText().trim())) {
                if (this.searchBaseSelected instanceof SearchNode) {
                    SearchNode search = (SearchNode) this.searchBaseSelected;
                    search.setName(this.nameTextField.getText().trim());
                    search.setDescription(this.descriptionTextArea.getText().trim());
                    search.setSearchParams(this.searchPanel.getSearchParamsFromPanel());
                } else {
                    return;
                }
//We have to update the tree
                ((DefaultTreeModel) this.tree.getModel()).nodeChanged(this.searchBaseSelected);

                //We set the view mode
                this.setEditorMode(VIEW_MODE);

                //We set selected the new node
                this.tree.setSelectionPath(new TreePath(this.searchBaseSelected.getPath()));

                this.setNeedToSave();
                Utils.getMainWindow().refreshSearchesMainWindow();
//                } else {
//                    logger.trace("Name already exists.");
//                    this.showNameAlreadyExistsMessage();
//                }
            }

        } else if (this.editorMode.equals(EDIT_FOLDER_MODE)) {
            logger.trace("We are in EDIT_FOLDER_MODE, and we want to save a modification in a search folder");
            if (this.checkSearchFields(editorMode)) {

                SearchFolderNode parent = (SearchFolderNode) this.searchBaseSelected.getParent();
//                if (parent.canCreateChild(this.nameTextField.getText().trim())) {
                if (this.searchBaseSelected instanceof SearchFolderNode) {
                    this.searchBaseSelected.setName(this.nameTextField.getText().trim());
                    this.searchBaseSelected.setDescription(this.descriptionTextArea.getText().trim());
                } else {
                    return;
                }
//We have to update the tree
                ((DefaultTreeModel) this.tree.getModel()).nodeChanged(this.searchBaseSelected);

                //We set the view mode
                this.setEditorMode(VIEW_MODE);

                //We set selected the new node
                this.tree.setSelectionPath(new TreePath(this.searchBaseSelected.getPath()));

                this.setNeedToSave();
                Utils.getMainWindow().refreshSearchesMainWindow();
//                } else {
//                    logger.trace("Name already exists.");
//                    this.showNameAlreadyExistsMessage();
//                }
            }

        } else if (this.editorMode.equals(ADD_SEARCH_MODE)) {
            logger.trace("We are in ADD_SEARCH_MODE, and we want to add a search in the parent folder");
            if (this.checkSearchFields(editorMode)) {
                SearchFolderNode parent = (SearchFolderNode) this.searchBaseSelected;
                if (parent.canCreateChild(this.nameTextField.getText().trim())) {
                    SearchNode newSearch = new SearchNode(
                            this.nameTextField.getText().trim(),
                            this.descriptionTextArea.getText(),
                            this.searchPanel.getSearchParamsFromPanel());
                    parent.add(newSearch);

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(this.searchBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(newSearch.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshSearchesMainWindow();
                    this.savingSearch = false;
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }

            }
        } else if (this.editorMode.equals(ADD_FOLDER_MODE)) {
            logger.trace("We are in ADD_FOLDER_MODE, and we want to add a search folder in the parent folder");
            if (this.checkSearchFields(editorMode)) {

                SearchFolderNode parent = (SearchFolderNode) this.searchBaseSelected;
                if (parent.canCreateChild(this.nameTextField.getText().trim())) {
                    SearchFolderNode newFolder = new SearchFolderNode(
                            this.nameTextField.getText().trim(),
                            this.descriptionTextArea.getText());
                    parent.add(newFolder);

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(this.searchBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(newFolder.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshSearchesMainWindow();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }

            }
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void newFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFolderButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.searchBaseSelected instanceof SearchFolderNode) {
                this.setEditorMode(ADD_FOLDER_MODE);
                this.searchPanel.setDefaultValues();
                this.nameTextField.setText("");
                this.descriptionTextArea.setText("");
            } else {
                logger.error("You must select a search folder!");
                this.showFolderNotFoundMessage();
            }

        }
    }//GEN-LAST:event_newFolderButtonActionPerformed

    private void newSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSearchButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.searchBaseSelected instanceof SearchFolderNode) {
                this.setEditorMode(ADD_SEARCH_MODE);
                this.searchPanel.setDefaultValues();
                this.nameTextField.setText("");
                this.descriptionTextArea.setText("");
            } else {
                logger.error("You must select a search folder!");
                this.showFolderNotFoundMessage();
            }

        }
    }//GEN-LAST:event_newSearchButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            //We get the selected tree node
            if (this.searchBaseSelected instanceof SearchFolderNode) {
                SearchFolderNode folder = (SearchFolderNode) this.searchBaseSelected;
                if (folder.getChildCount() > 0) {
                    String message = "Folder  " + folder.getName() + " can not be removed.";
                    String details = "Folder  " + folder.getName() + " can not be removed.\nYou need to remove its contained searches first.";
                    String title = "Error removing Search";
                    MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                    logger.error(details);
                    return;
                }

            }

            //We remove the selected node from the tree
            SearchBaseNode parentNode = (SearchBaseNode) this.searchBaseSelected.getParent();
            if (parentNode != null) {
                parentNode.remove(this.searchBaseSelected);
            }

//We have to update the tree
            ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(parentNode);

            this.setNeedToSave();
            Utils.getMainWindow().refreshSearchesMainWindow();
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton newFolderButton;
    private javax.swing.JButton newSearchButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchInfoPanel;
    private org.symlabs.browser.search.SearchPanel searchPanel;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
    public void valueChanged(TreeSelectionEvent arg0) {
        if (this.editorMode.equals(VIEW_MODE)) {
            Object node = (Object) this.tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            if (node instanceof SearchBaseNode) {
                this.searchBaseSelected = (SearchBaseNode) node;
                logger.trace("It has been selected the node: " + this.searchBaseSelected.getName());
                this.nameTextField.setText(this.searchBaseSelected.getName());
                this.descriptionTextArea.setText(this.searchBaseSelected.getDescription());
                if (this.searchBaseSelected instanceof SearchFolderNode) {
                    SearchFolderNode folder = (SearchFolderNode) this.searchBaseSelected;
                    this.searchPanel.setVisible(false);
                    this.searchButton.setEnabled(false);
                } else if (this.searchBaseSelected instanceof SearchNode) {
                    SearchNode search = (SearchNode) this.searchBaseSelected;
                    this.searchPanel.setSearchParamsFromPanel(search.getSearchParams());
                    this.searchPanel.setVisible(true);
                    this.searchButton.setEnabled(true);
                }

                this.rightPanel.validate();
                this.rightPanel.repaint();
            }

        } else if (this.editorMode.equals(ADD_SEARCH_MODE) && this.savingSearch) {
            Object node = (Object) this.tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            if (node instanceof SearchBaseNode) {
                this.searchBaseSelected = (SearchBaseNode) node;
            }
        }
    }
}
