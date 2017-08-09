package org.symlabs.browser.search;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import org.symlabs.actions.search.LoadSearch;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.LDAPNodeTreeCellRenderer;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.search.SearchBaseNode;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.search.SearchNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddSearchWin </p>
 * <p>Descripcion: This Frame allos you to add a search in a existing folder, and execute it</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddSearchWin.java,v 1.6 2009-08-24 09:01:06 efernandez Exp $
 */
public class AddSearchWin extends javax.swing.JFrame implements TreeSelectionListener {

    /**Attribute that contains the ldap node*/
    private LDAPNode ldapNode;
    /**Attribute that contains the search base selected*/
    private SearchBaseNode searchBaseSelected;
    /**Attribute that contains the root node of the searches displayed*/
    private SearchFolderNode rootFolder;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddSearchWin.class);

    /** Creates new form AddSearch */
    public AddSearchWin() {
        initComponents();
    }

    /** Creates new form AddSearchWin
     * 
     * @param ldapNode
     */
    public AddSearchWin(LDAPNode ldapNode) {
        this.ldapNode = ldapNode;
        this.rootFolder = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getSearchRootFolder();
        initComponents();
        this.setSize(780, 500);
        this.searchPanel.initProperties(this.ldapNode);
        

        //We should set the values search params
        logger.trace("We should set HERE the values of the search");

        //We set the tree model
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
    }

    private void refreshFolders(SearchBaseNode base) {
        if (base == null) {
            base = this.rootFolder;
        }
        SearchBaseNode parent = null;
        if (base.getParent() == null) {
            parent = base;
        } else {
            parent = (SearchBaseNode) base.getParent();
        }
        //We have to update the tree
        ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(parent);

        //We set selected the new node
        this.tree.setSelectionPath(new TreePath(base.getPath()));
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

    /**Method that saves the current selected tab
     * 
     */
    private void setNeedToSave() {
        ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getConnectionData().setDirty(true);
    }

    /**Method that shows an error message when already exists a folder or a search with the name written in the same folder.
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        splitPane = new javax.swing.JSplitPane();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        rightPanel = new javax.swing.JPanel();
        searchInfoPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        searchPanel = new org.symlabs.browser.search.SearchPanel();
        commandPanel = new javax.swing.JPanel();
        manageSearchButton = new javax.swing.JButton();
        addSearchButton = new javax.swing.JButton();
        showSearchButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add Search");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        splitPane.setDividerLocation(160);
        splitPane.setDividerSize(4);

        treeScrollPane.setViewportView(tree);

        splitPane.setLeftComponent(treeScrollPane);

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightPanel.add(searchInfoPanel, gridBagConstraints);

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Params"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightPanel.add(searchPanel, gridBagConstraints);

        splitPane.setRightComponent(rightPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(splitPane, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        manageSearchButton.setText("Manage Search");
        manageSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSearchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(manageSearchButton, gridBagConstraints);

        addSearchButton.setText("Add Search");
        addSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSearchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(addSearchButton, gridBagConstraints);

        showSearchButton.setText("Search");
        showSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSearchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(showSearchButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(commandPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void manageSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSearchButtonActionPerformed
        ManageSearchWin searchWin = new ManageSearchWin(this.ldapNode);
        searchWin.addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent arg0) {
            }

            public void windowLostFocus(WindowEvent evt) {
                refreshFolders(rootFolder);
                logger.trace("Window focus lost " + evt.paramString());
            }
        });
        searchWin.setLocationRelativeTo(this);
        searchWin.setVisible(true);
    }//GEN-LAST:event_manageSearchButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSearchButtonActionPerformed
        if (this.searchBaseSelected != null && this.searchBaseSelected instanceof SearchFolderNode) {
            SearchFolderNode folder = (SearchFolderNode) this.searchBaseSelected;

            logger.trace("search selected:" + searchBaseSelected.getName());
            String errorInFields = this.searchPanel.getErrorCheckingSearchFields();
            if (!this.nameTextField.getText().trim().equals("") && errorInFields.equals("")) {
                if (folder.canCreateChild(this.nameTextField.getText())) {
                    //We add the new search to the arraylist
                    SearchNode newSearch = new SearchNode(
                            this.nameTextField.getText(),
                            this.descriptionTextArea.getText(),
                            this.searchPanel.getSearchParamsFromPanel());

                    folder.add(newSearch);

                    this.refreshFolders(newSearch);

                    this.setNeedToSave();

                    Utils.getMainWindow().refreshSearchesMainWindow();

                    this.dispose();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }

            } else {
                String message = "Please fill the mandatory fields.";
                String details = "The following field are mandatory: DN, Name." + "\n" + errorInFields;
                String title = "Error Adding Search";
                MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } else {
            logger.error("Search folder not found.");
            this.showFolderNotFoundMessage();
        }
    }//GEN-LAST:event_addSearchButtonActionPerformed

    private void showSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSearchButtonActionPerformed
        String errorInFields = this.searchPanel.getErrorCheckingSearchFields();
        if (errorInFields.equals("")) {
            if (this.searchBaseSelected instanceof SearchNode) {
                this.dispose();
                SearchNode searchNode = (SearchNode) this.searchBaseSelected;
                (new LoadSearch(Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode(), searchNode,true)).actionPerformed(evt);
            } else {
                String message = "You must select a search.";
                String details = "You have selected a search folder. Please select a search to go to it.";
                String title = "Error Going to Search";
                MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } else {
            String message = "You must fill the fields.";
            String details = "The following fields are mandatory: DN." + "\n" + "And the search params: " + errorInFields;
            String title = "Error Going to Search";
            MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_showSearchButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSearchButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton manageSearchButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel searchInfoPanel;
    private org.symlabs.browser.search.SearchPanel searchPanel;
    private javax.swing.JButton showSearchButton;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
    public void valueChanged(TreeSelectionEvent arg0) {
        Object node = (Object) this.tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node instanceof SearchFolderNode) {
            this.searchBaseSelected = (SearchBaseNode) node;
            logger.trace("total child: " + this.searchBaseSelected.getChildCount());
        } else {
            this.searchBaseSelected = null;
        }
    }
}
