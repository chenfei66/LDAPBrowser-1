package org.symlabs.browser.bookmark;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import org.symlabs.bookmark.*;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.actions.bookmark.GoToBookmark;
import org.symlabs.bookmark.BookmarkBaseNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.LDAPNodeTreeCellRenderer;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: BookmarkWin </p>
 * <p>Descripcion: Frame that allows you to manage the stored bookmarks in a configuration</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ManageBookmarkWin.java,v 1.13 2009-08-24 09:01:06 efernandez Exp $
 */
public class ManageBookmarkWin extends javax.swing.JFrame implements TreeSelectionListener {

    /**Attribute that identifies the view mode for this bookmark editor*/
    private static final String VIEW_MODE = "viewBookmark";
    /**Attribute that identifies the edit mode for this bookmark editor*/
    private static final String EDIT_BOOKMARK_MODE = "editBookmark";
    /**Attribute that identifies the edit mode for this bookmark editor*/
    private static final String EDIT_FOLDER_MODE = "editBookmarkFolder";
    /**Attribute that identifies the add mode for this bookmark editor*/
    private static final String ADD_BOOKMARK_MODE = "addBookmark";
    /**Attribute that identifies the add mode for this bookmark editor*/
    private static final String ADD_FOLDER_MODE = "addBookmarkFolder";
    /**Attribute that stores the mode for this editor*/
    private String editorMode = VIEW_MODE;
    /**Attribute used to store the old dn value when you edit an existing value*/
    private String oldDn;
    /**Attribute used to store the old name value when you edit an existing value*/
    private String oldName;
    /**Attribute used to store the old description value when you edit an existing value*/
    private String oldDescription;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ManageBookmarkWin.class);
    /**Attribute that contains the defaultDn setted when a new bookmark is created*/
    private String defaultDn;
    /**Attribute that contains the ldap node*/
    private LDAPNode ldapNode;
    /**Attribute that contains the bookmark base selected*/
    private BookmarkBaseNode bookmarkBaseSelected;
    /**Attribute that contains the root node of the bookmarks displayed*/
    private BookmarkFolderNode rootFolder;
    /**Attribute that contains the popup menu actions for cutting and copying a search*/
    private JPopupMenu popupMenuCopyAndCut;
    /**Attribute that contains the popup menu actions for pasting*/
    private JPopupMenu popupMenuPaste;
    /**Attribute that contains the search base selected for copy*/
    private BookmarkBaseNode baseToCopy;

    /** Creates new form BookmarkWin */
    public ManageBookmarkWin() {
        initComponents();
    }

    private MouseListener initPopup() {
        this.popupMenuCopyAndCut = new JPopupMenu();
        this.popupMenuPaste = new JPopupMenu();
        this.popupMenuCopyAndCut.add(new DsAction("Copy", java.awt.event.KeyEvent.VK_C, Utils.createImageIcon(Utils.ICON_COPY_FOLDER)) {

            public void actionPerformed(ActionEvent arg0) {
                BookmarkBaseNode baseSelected = (BookmarkBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected.getParent() != null) {
                    baseToCopy = (BookmarkBaseNode) baseSelected.clone();
                }
            }
        });

        this.popupMenuCopyAndCut.add(new DsAction("Cut", java.awt.event.KeyEvent.VK_P, Utils.createImageIcon(Utils.ICON_CUT)) {

            public void actionPerformed(ActionEvent arg0) {
                BookmarkBaseNode baseSelected = (BookmarkBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected.getParent() != null) {
                    BookmarkBaseNode parentNode = (BookmarkBaseNode) baseSelected.getParent();

                    BookmarkBaseNode rootNode = (BookmarkBaseNode) parentNode.getRoot();

                    baseToCopy = baseSelected;
                    int index = parentNode.getIndex(baseSelected);
                    parentNode.remove(index);
                }
            }
        });

        this.popupMenuPaste.add(new DsAction("Paste", java.awt.event.KeyEvent.VK_P, Utils.createImageIcon(Utils.ICON_PASTE)) {

            public void actionPerformed(ActionEvent arg0) {
                BookmarkBaseNode baseSelected = (BookmarkBaseNode) tree.getSelectionPath().getLastPathComponent();
                if (baseSelected instanceof BookmarkFolderNode) {
                    if (baseToCopy != null) {
                        int index = baseSelected.getChildCount();
                        boolean found = false;
                        BookmarkBaseNode childNode = null;
                        for (int i = 0; i < baseSelected.getChildCount(); i++) {
                            childNode = (BookmarkBaseNode) baseSelected.getChildAt(i);
                            if (childNode.getName().equalsIgnoreCase(baseToCopy.getName())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            baseSelected.insert(baseToCopy, index);

                            //We update the tree
                            Utils.getMainWindow().refreshBookmarksMainWindow();
                            setNeedToSave();
                            tree.validate();
                            tree.repaint();
                            tree.updateUI();
                            baseToCopy = null;
                        } else {
                            String message = "This folder already exists.";
                            String details = "The parent folder already has an existing node with that name." + "\n" +
                                    "Please select a valid destination." + "\n" +
                                    "If you Close thisWindow or Click on Cancel Button you will lost the bookmarks that you have cut unless you close this connection without saving.";
                            String title = "Error Moving Bookmark";
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
                        BookmarkBaseNode node = (BookmarkBaseNode) tree.getLastSelectedPathComponent();
                        if (node.getParent() != null && baseToCopy == null) {//We can cut and copy
                            logger.trace("We show the popup for copying and cutting");
                            popupMenuCopyAndCut.show(e.getComponent(), e.getX(), e.getY());
                        } else if (node instanceof BookmarkFolderNode && baseToCopy!=null) {//We can paste only in folder
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
    
    /**Method that calls to the show message with the arguments given
     * 
     * @param title Stirng. This is the title of the dialog window
     * @param message Stirng, This is the message of the dialog window
     * @param details String. this is the message details of the dialog window
     * @param messageType int. This is the message type
     */
    private void showMessageDialog(String title, String message, String details, int messageType){
        MessageDialog dialog = new MessageDialog(this,title, message, details, messageType);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /** Creates new form BookmarkWin.
     *   Use this contructor to show the manage bookmark window, with no ldap node selected.
     * 
     * @param ldapNode LDAPNode. This is the node selected
     */
    public ManageBookmarkWin(LDAPNode ldapNode) {
        this.ldapNode = ldapNode;
        this.rootFolder = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getBookMarkRootFolder();
        this.initComponents();
        this.setSize(700, 400);
        this.bookmarkPanel.setLdapNode(this.ldapNode);
        this.bookmarkPanel.loadDnComboBoxValues(this.ldapNode);
        this.setEditorMode(VIEW_MODE);
        if (rootFolder != null) {
            DefaultTreeModel model = new DefaultTreeModel(rootFolder);
            this.tree.setModel(model);
        } else {
            logger.error("Empty bookmark tree");
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

        //We add the mouse listener 
        this.tree.addMouseListener(this.initPopup());

        //To update the valeus of the panel
        this.valueChanged(null);
    }

    /**MEthod that sets visible the components needed for add or edit a bookmark
     * 
     * @param visible boolean.
     */
    private void setVisibleBookmarkComponents(boolean visible) {
        this.bookmarkPanel.getDnComboBox().setVisible(visible);
        this.bookmarkPanel.getDnLabel().setVisible(visible);
    }

    /**Method that sets editable the components displayed in the bookmark panel.
     * 
     * @param editable boolean.
     */
    private void setEditableBookmarkComponents(boolean editable) {
        this.bookmarkPanel.getDnComboBox().setEditable(editable);
        this.bookmarkPanel.getNameTextField().setEditable(editable);
        this.bookmarkPanel.getDescriptionTextArea().setEditable(editable);
    }

    /**Method that sets the editor mode given as argument.
     * 
     * @param mode String.
     */
    private void setEditorMode(String mode) {
        this.editorMode = mode;
        if (mode.equals(VIEW_MODE)) {
            this.removeButton.setEnabled(true);
            this.newBookmarkButton.setEnabled(true);
            this.newFolderButton.setEnabled(true);
            this.editButton.setEnabled(true);
            this.editButton.setText("Edit");
            this.editButton.setToolTipText("Edit the bookmark displayed");
            this.cancelButton.setText("Close");
            this.cancelButton.setToolTipText("Close this window");
            this.cancelButton.setEnabled(true);
            this.GoToBookmarkButton.setEnabled(true);
            this.setVisibleBookmarkComponents(true);
            this.setEditableBookmarkComponents(false);
            this.bookmarkPanel.validate();
            this.bookmarkPanel.repaint();
        } else if (mode.equals(EDIT_BOOKMARK_MODE)) {
            this.removeButton.setEnabled(false);
            this.newBookmarkButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save your changes");
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore your changes");
            this.editButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
            this.GoToBookmarkButton.setEnabled(false);
            this.setVisibleBookmarkComponents(true);
            this.setEditableBookmarkComponents(true);
            this.bookmarkPanel.validate();
            this.bookmarkPanel.repaint();
        } else if (mode.equals(EDIT_FOLDER_MODE)) {
            this.removeButton.setEnabled(false);
            this.newBookmarkButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save your changes");
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Restore your changes");
            this.editButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
            this.GoToBookmarkButton.setEnabled(false);
            this.setEditableBookmarkComponents(true);
            this.setVisibleBookmarkComponents(false);
            this.bookmarkPanel.validate();
            this.bookmarkPanel.repaint();
        } else if (mode.equals(ADD_BOOKMARK_MODE)) {
            this.removeButton.setEnabled(false);
            this.newBookmarkButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save your bookmark");
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Remove your changes");
            this.editButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
            this.GoToBookmarkButton.setEnabled(false);
            this.setVisibleBookmarkComponents(true);
            this.setEditableBookmarkComponents(true);
            this.bookmarkPanel.validate();
            this.bookmarkPanel.repaint();
        } else if (mode.equals(ADD_FOLDER_MODE)) {
            this.removeButton.setEnabled(false);
            this.newBookmarkButton.setEnabled(false);
            this.newFolderButton.setEnabled(false);
            this.editButton.setText("Save");
            this.editButton.setToolTipText("Save your folder");
            this.cancelButton.setText("Cancel");
            this.cancelButton.setToolTipText("Remove your changes");
            this.editButton.setEnabled(true);
            this.editButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
            this.GoToBookmarkButton.setEnabled(false);
            this.setVisibleBookmarkComponents(false);
            this.setEditableBookmarkComponents(true);
            this.bookmarkPanel.validate();
            this.bookmarkPanel.repaint();
        }
    }

    /**Method that returns an string with the fields with error
     * 
     * @param editorMode String. This is the mode to test
     * @return String. This string contains the error details.
     */
    private String getErrorCheckingBookmarksFields(String editorMode) {
        String error = "";
        if (editorMode.equals(EDIT_BOOKMARK_MODE) || editorMode.equals(ADD_BOOKMARK_MODE)) {
            if (this.bookmarkPanel.getDnComboBox().getSelectedItem() == null || this.bookmarkPanel.getDnComboBox().getSelectedItem().equals("")) {
                error += "Error checking dn." + "\n";
            }
            if (this.bookmarkPanel.getNameTextField().getText().trim().equals("")) {
                error += "Error checking name." + "\n";
            }
        } else if (editorMode.equals(EDIT_FOLDER_MODE) || editorMode.equals(ADD_FOLDER_MODE)) {
            if (this.bookmarkPanel.getNameTextField().getText().trim().equals("")) {
                error += "Error checking name." + "\n";
            }
        }
        return error;
    }

    /**Method that test the fields of the bookmark type.
     * 
     * @param editorMode Stirng.
     * @return boolean. True means no error was found. False means error was found.
     */
    private boolean checkBookmarksFields(String editorMode) {
        String title = "";
        String message = "All the fields must be filled.";
        String detailsMessage = this.getErrorCheckingBookmarksFields(editorMode);
        boolean errorFound = false;
        if (editorMode.equals(EDIT_BOOKMARK_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Editing Bookmark";
                errorFound = true;
            }
        } else if (editorMode.equals(ADD_BOOKMARK_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Adding Bookmark";
                errorFound = true;
            }
        } else if (editorMode.equals(EDIT_FOLDER_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Editing Folder";
                errorFound = true;
            }
        } else if (editorMode.equals(ADD_FOLDER_MODE)) {
            if (!detailsMessage.equals("")) {
                title = "Error Adding Folder";
                errorFound = true;
            }
        }

        if (errorFound) {
            MessageDialog dialog = new MessageDialog(this,title, message, detailsMessage, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        return !errorFound;
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
        removeButton = new javax.swing.JButton();
        newBookmarkButton = new javax.swing.JButton();
        newFolderButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        GoToBookmarkButton = new javax.swing.JButton();
        bookmarkSplitPane = new javax.swing.JSplitPane();
        folderScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        rightPanel = new javax.swing.JPanel();
        bookmarkPanel = new org.symlabs.browser.bookmark.BookmarkPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manage Bookmarks");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        commandPanel.setLayout(new java.awt.GridBagLayout());

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(removeButton, gridBagConstraints);

        newBookmarkButton.setText("New Bookmark");
        newBookmarkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBookmarkButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(newBookmarkButton, gridBagConstraints);

        newFolderButton.setText("New Folder");
        newFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFolderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(newFolderButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(editButton, gridBagConstraints);

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

        GoToBookmarkButton.setText("Go to Bookmark");
        GoToBookmarkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GoToBookmarkButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(GoToBookmarkButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(commandPanel, gridBagConstraints);

        bookmarkSplitPane.setDividerLocation(160);
        bookmarkSplitPane.setDividerSize(4);

        folderScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Bookmark Browser"));
        folderScrollPane.setViewportView(tree);

        bookmarkSplitPane.setLeftComponent(folderScrollPane);

        rightPanel.setLayout(new java.awt.GridBagLayout());

        bookmarkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bookmark Info"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        rightPanel.add(bookmarkPanel, gridBagConstraints);

        bookmarkSplitPane.setRightComponent(rightPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(bookmarkSplitPane, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) { //We are going to edit a bookmarkbase
            if (this.rootFolder != null) {
                logger.trace("We was in VIEW_MODE, and we are going to edit a bookmarkbase");
                //We update the old values
                this.storeOldValues();

                if (this.bookmarkBaseSelected.type.equals(BookmarkNode.TYPE_BOOKMARK)) {
                    this.setEditorMode(EDIT_BOOKMARK_MODE);
                } else if (this.bookmarkBaseSelected.type.equals(BookmarkFolderNode.TYPE_BOOKMARK_FOLDER)) {
                    this.setEditorMode(EDIT_FOLDER_MODE);
                }
            }
        } else if (this.editorMode.equals(EDIT_BOOKMARK_MODE)) { //We are editing a bookmark, so we have to save the data
            if (this.checkBookmarksFields(this.editorMode)) {
                BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected.getParent();
                if (folder != null && folder.canCreateChild(this.bookmarkPanel.getNameTextField().getText())) {
                    BookmarkNode bookmark = null;
                    logger.trace("We was in EDIT_BOOKMARK_MODE, we was editing a bookmark, so we have to save the data.");
                    if (this.bookmarkBaseSelected instanceof BookmarkNode) {
                        bookmark = (BookmarkNode) this.bookmarkBaseSelected;
                        bookmark.setDn((String) this.bookmarkPanel.getDnComboBox().getSelectedItem());
                        bookmark.setName(this.bookmarkPanel.getNameTextField().getText());
                        bookmark.setDescription(this.bookmarkPanel.getDescriptionTextArea().getText());
                    } else {
                        return;
                    }

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeChanged(this.bookmarkBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(bookmark.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshBookmarksMainWindow();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }
            }
        } else if (this.editorMode.equals(EDIT_FOLDER_MODE)) { //We are editing a folder, so we have to save the data
            if (this.checkBookmarksFields(this.editorMode)) {
                BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected;
                if (folder.canCreateChild(this.bookmarkPanel.getNameTextField().getText())) {
                    logger.trace("We was in EDIT_FOLDER_MODE, we was editing a bookmark folder, so we have to save the data.");
                    BookmarkFolderNode bookmark = null;
                    if (this.bookmarkBaseSelected instanceof BookmarkFolderNode) {
                        bookmark = (BookmarkFolderNode) this.bookmarkBaseSelected;
                        bookmark.setName(this.bookmarkPanel.getNameTextField().getText());
                        bookmark.setDescription(this.bookmarkPanel.getDescriptionTextArea().getText());
                    } else {
                        return;
                    }

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeChanged(this.bookmarkBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(bookmark.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshBookmarksMainWindow();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }
            }
        } else if (this.editorMode.equals(ADD_BOOKMARK_MODE)) {//we are adding a bookmark, so we have to add it
            if (this.checkBookmarksFields(this.editorMode)) {
                BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected;
                if (folder.canCreateChild(this.bookmarkPanel.getNameTextField().getText())) {
                    logger.trace("We was in ADD_BOOKMARK_MODE, we was adding a bookmark, so we have to store it.");
                    logger.trace("El dn es: " + (String) this.bookmarkPanel.getDnComboBox().getSelectedItem());
                    //We add the new bookmark to the arraylist
                    BookmarkNode newBookmark = new BookmarkNode(
                            (String) this.bookmarkPanel.getDnComboBox().getSelectedItem(),
                            this.bookmarkPanel.getNameTextField().getText(),
                            this.bookmarkPanel.getDescriptionTextArea().getText());

                    folder.add(newBookmark);

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(this.bookmarkBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(newBookmark.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshBookmarksMainWindow();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }
            }
        } else if (this.editorMode.equals(ADD_FOLDER_MODE)) {//we are adding a folder, so we have to store it
            if (this.checkBookmarksFields(this.editorMode)) {
                BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected;
                if (folder.canCreateChild(this.bookmarkPanel.getNameTextField().getText())) {
                    logger.trace("We was in ADD_FOLDER_MODE, we was adding a bookmark folder, so we have to store it.");
                    //We add the new bookmark to the arraylist
                    BookmarkFolderNode newBookmark = new BookmarkFolderNode(
                            this.bookmarkPanel.getNameTextField().getText(),
                            this.bookmarkPanel.getDescriptionTextArea().getText());

                    folder.add(newBookmark);

                    //We have to update the tree
                    ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(this.bookmarkBaseSelected);

                    //We set the view mode
                    this.setEditorMode(VIEW_MODE);

                    //We set selected the new node
                    this.tree.setSelectionPath(new TreePath(newBookmark.getPath()));

                    this.setNeedToSave();
                    Utils.getMainWindow().refreshBookmarksMainWindow();
                } else {
                    logger.trace("Name already exists.");
                    this.showNameAlreadyExistsMessage();
                }
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed

    /**Method that saves the current selected tab
     * 
     */
    private void setNeedToSave() {
        ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getConnectionData().setDirty(true);
    }

    /**Method that stores in old attributes the values setted. This is a back up.
     * 
     */
    private void storeOldValues() {
        //We store the old values
        this.oldDn = (String) this.bookmarkPanel.getDnComboBox().getSelectedItem();
        this.oldName = this.bookmarkPanel.getNameTextField().getText();
        this.oldDescription = this.bookmarkPanel.getDescriptionTextArea().getText();
    }

    /**Method that sets the values to the bookmark panel
     * 
     * @param dn String
     * @param name String
     * @param description String
     */
    private void setValuesBookmark(String dn, String name, String description) {
        this.bookmarkPanel.getDnComboBox().setEditable(true);
        boolean editable = this.bookmarkPanel.getDnComboBox().isEditable();
        this.bookmarkPanel.getDnComboBox().setEditable(true);
        this.bookmarkPanel.getDnComboBox().setSelectedItem(dn);
        this.bookmarkPanel.getDnComboBox().setEditable(editable);
        this.bookmarkPanel.getNameTextField().setText(name);
        this.bookmarkPanel.getDescriptionTextArea().setText(description);
    }

    private void newBookmarkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBookmarkButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.bookmarkBaseSelected instanceof BookmarkFolderNode) {
                this.setEditorMode(ADD_BOOKMARK_MODE);
                this.setValuesBookmark(this.defaultDn, "", "");
            } else {
                logger.error("You must select a boonarmark folder!");
                this.showFolderNotFoundMessage();
            }
        }
}//GEN-LAST:event_newBookmarkButtonActionPerformed

    /**Method that shows an error message when no folder is selected
     * 
     */
    private void showFolderNotFoundMessage() {
        String message = "Folder not found.";
        String details = "Please select a bookmark folder to add a new bookmark or a new folder.";
        String title = "Error Adding Bookmark";
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
        String title = "Error in Bookmark";
        MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            //We get the selected tree node
            if (this.bookmarkBaseSelected.type.equals(BookmarkFolderNode.TYPE_BOOKMARK_FOLDER)) {
                BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected;
                if (folder.getChildCount() > 0) {
                    String message = "Folder  " + folder.getName() + " can not be removed.";
                    String details = "Folder  " + folder.getName() + " can not be removed.\nYou need to remove its contained bookmarks first.";
                    String title = "Error removing Bookmark";
                    MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                    logger.error(details);
                    return;
                }
            }

            //We remove the selected node from the tree
            BookmarkBaseNode parentNode = (BookmarkBaseNode) this.bookmarkBaseSelected.getParent();
            if (parentNode != null) {
                parentNode.remove(this.bookmarkBaseSelected);
            }

            //We have to update the tree
            ((DefaultTreeModel) this.tree.getModel()).nodeStructureChanged(parentNode);

            this.setNeedToSave();
            Utils.getMainWindow().refreshBookmarksMainWindow();
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (this.editorMode.equals(EDIT_BOOKMARK_MODE)) {
            this.setEditorMode(VIEW_MODE);
            this.setValuesBookmark(oldDn, oldName, oldDescription);
        } else if (this.editorMode.equals(EDIT_FOLDER_MODE)) {
            this.setEditorMode(VIEW_MODE);
            boolean editable = this.bookmarkPanel.getDnComboBox().isEditable();
            this.bookmarkPanel.getDnComboBox().setEditable(true);
            this.bookmarkPanel.getDnComboBox().setSelectedItem(oldDn);
            this.bookmarkPanel.getDnComboBox().setEditable(editable);
            this.bookmarkPanel.getNameTextField().setText(oldName);
            this.bookmarkPanel.getDescriptionTextArea().setText(oldDescription);
            this.oldDn = "";
            this.oldName = "";
            this.oldDescription = "";
        } else if (this.editorMode.equals(ADD_BOOKMARK_MODE)) {
            this.setEditorMode(VIEW_MODE);
            boolean editable = this.bookmarkPanel.getDnComboBox().isEditable();
            this.bookmarkPanel.getDnComboBox().setEditable(true);
            this.bookmarkPanel.getDnComboBox().setSelectedItem(this.defaultDn);
            this.bookmarkPanel.getDnComboBox().setEditable(editable);
            this.bookmarkPanel.getNameTextField().setText("");
            this.bookmarkPanel.getDescriptionTextArea().setText("");
        } else if (this.editorMode.equals(ADD_FOLDER_MODE)) {
            this.setEditorMode(VIEW_MODE);
            boolean editable = this.bookmarkPanel.getDnComboBox().isEditable();
            this.bookmarkPanel.getDnComboBox().setEditable(true);
            this.bookmarkPanel.getDnComboBox().setSelectedItem(this.defaultDn);
            this.bookmarkPanel.getDnComboBox().setEditable(editable);
            this.bookmarkPanel.getNameTextField().setText("");
            this.bookmarkPanel.getDescriptionTextArea().setText("");
        } else if (this.editorMode.equals(VIEW_MODE)) {
            this.dispose();
        }

        //We set selected the new node
        this.tree.setSelectionPath(new TreePath(this.bookmarkBaseSelected.getPath()));
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void newFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFolderButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.bookmarkBaseSelected instanceof BookmarkFolderNode) {
                this.setEditorMode(ADD_FOLDER_MODE);
                boolean editable = this.bookmarkPanel.getDnComboBox().isEditable();
                this.bookmarkPanel.getDnComboBox().setEditable(true);
                this.bookmarkPanel.getDnComboBox().setSelectedItem(this.defaultDn);
                this.bookmarkPanel.getDnComboBox().setEditable(editable);
                this.bookmarkPanel.getNameTextField().setText("");
                this.bookmarkPanel.getDescriptionTextArea().setText("");
            } else {
                logger.error("You must select a boonarmark folder!");
                this.showFolderNotFoundMessage();
            }
        }
    }//GEN-LAST:event_newFolderButtonActionPerformed

    private void GoToBookmarkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoToBookmarkButtonActionPerformed
        if (this.editorMode.equals(VIEW_MODE)) {
            if (this.bookmarkBaseSelected instanceof BookmarkNode) {
                this.dispose();
                BookmarkNode bookmarkNode = (BookmarkNode) this.bookmarkBaseSelected;
                (new GoToBookmark(Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode(), bookmarkNode)).actionPerformed(evt);
            } else {
                String message = "Bookmark not found.";
                String details = "Please select a bookmark to go to this bookmark.";
                String title = "Error Going to Bookmark";
                MessageDialog dialog = new MessageDialog(this,title, message, details, MessageDialog.MESSAGE_ERROR);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_GoToBookmarkButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton GoToBookmarkButton;
    private org.symlabs.browser.bookmark.BookmarkPanel bookmarkPanel;
    private javax.swing.JSplitPane bookmarkSplitPane;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane folderScrollPane;
    private javax.swing.JButton newBookmarkButton;
    private javax.swing.JButton newFolderButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
    public void valueChanged(TreeSelectionEvent arg0) {
        if (this.editorMode.equals(ManageBookmarkWin.VIEW_MODE)) {
            Object node = (Object) this.tree.getLastSelectedPathComponent();
            logger.trace("Aquiiii 1");
            if (node == null) {
                return;
            }

            if (node instanceof BookmarkBaseNode) {
                this.bookmarkBaseSelected = (BookmarkBaseNode) node;

                if (this.bookmarkBaseSelected.type.equals(BookmarkFolderNode.TYPE_BOOKMARK_FOLDER)) {
                    BookmarkFolderNode folder = (BookmarkFolderNode) this.bookmarkBaseSelected;
                    logger.trace("Aquiiii 2 " + this.bookmarkBaseSelected.getName() + ",childcount:" + folder.getChildCount());
                    for (int i = 0; i < folder.getChildCount(); i++) {
                        logger.trace("child " + i + ", name: " + ((BookmarkBaseNode) folder.getChildAt(i)).getName());
                    }
                    this.bookmarkPanel.getNameTextField().setText(this.bookmarkBaseSelected.getName());
                    this.bookmarkPanel.getDescriptionTextArea().setText(this.bookmarkBaseSelected.getDescription());
                    this.bookmarkPanel.getDnComboBox().setVisible(false);
                    this.bookmarkPanel.getDnLabel().setVisible(false);
                    this.bookmarkPanel.validate();
                    this.bookmarkPanel.repaint();
                } else if (this.bookmarkBaseSelected.type.equals(BookmarkNode.TYPE_BOOKMARK)) {
                    BookmarkNode book = (BookmarkNode) this.bookmarkBaseSelected;
                    logger.trace("Aquiiii 2 " + this.bookmarkBaseSelected.getName() + ",dn:" + book.getDn() + "editable?" + this.bookmarkPanel.getDnComboBox().isEditable());
                    this.bookmarkPanel.getDnComboBox().setEditable(true);
                    this.bookmarkPanel.getDnComboBox().setSelectedItem(book.getDn());
                    this.bookmarkPanel.getDnComboBox().setEditable(false);
                    this.bookmarkPanel.getNameTextField().setText(book.getName());
                    this.bookmarkPanel.getDescriptionTextArea().setText(book.getDescription());
                    this.bookmarkPanel.getDnComboBox().setVisible(true);
                    this.bookmarkPanel.getDnLabel().setVisible(true);
                    this.bookmarkPanel.validate();
                    this.bookmarkPanel.repaint();
                }
            }
        }
    }
}
