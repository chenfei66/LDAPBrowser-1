package org.symlabs.browser.editor;

import org.symlabs.browser.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPModification;
import org.apache.log4j.Logger;
import org.symlabs.actions.editor.CloseEditor;
import org.symlabs.actions.node.RefreshTree;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.VirtualNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: EditPanel </p>
 * <p>Descripcion: Class that manages the edit panel of the current node. 
 * This editor is used to show the attributes of each node.
 * This panel will contain an editor type, like: field-value, table, ldif, form or search.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: EditorPanel.java,v 1.18 2009-08-24 09:01:06 efernandez Exp $
 */
public class EditorPanel extends javax.swing.JPanel {

    /**Attribute that stores the default value for this editor*/
    private static String DefaultEditorTitle = "Editor panel";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(EditorPanel.class);
    /**Attribute that contains the type of editor used: field-value, form, ldif, table or search*/
    protected String editorType;
    /**Attribute that contains the attributes and its values from the ldap node. 
     * Position: attributes[0] = dn , attributes[1]=objectclasses, attributes[2]=other_attributes*/
    protected HashMap<String, String[]> attributes[];
    /**Attribute that contains the attributes and its values from this editor */
    protected HashMap<String, String[]> newAttributes[];
    /**Attribute that contains the current node*/
    protected LDAPNode ldapNode;
    /**Attribute used to identify the editor mode: viewing mode*/
    public static final String EDITOR_MODE_VIEW = "Editor VIEW Mode";
    /**Attribute used to identify the editor mode: editting mode*/
    public static final String EDITOR_MODE_EDIT = "Editor EDIT Mode";
    /**Attribute used to identify the editor mode: adding a new entry mode*/
    public static final String EDITOR_MODE_ADD_ENTRY = "Editor ADD ENTRY Mode";
    /**Attribute used to store the editor mode used at this moment*/
    protected String editorMode;
    /**Attribute used to indicate if we are adding a new entry. 
     * This attribute only should be used in Step2 of wizard add new entry.*/
    private boolean addingNewEntryMode;
    /**Attribute used to store the required attributes to store a new entry*/
    private HashMap<String, String> addingNewEntry;
    /**Attribute used to store the dn of the new node when adding a new entry*/
    public static final String ADD_NEW_ENTRY_RDN_KEY = "Add New Entry RDN";
    /**Attribute used to store the parent node then adding a new entry*/
    public static final String ADD_NEW_ENTRY_PARENT_DN_KEY = "Add New Entry Parent DN";

    /**Method that gets the value contained of the key given as argument in the addingNewEntry hashMap.
     * This method is only used to add a new entry.
     * At this moment this hash only stores the rdn (as string) of the new node to add, and the parentDn (as string).
     * 
     * @param attributeKey String. This is the attribute key.
     * @return String. This is the attribute contianed by the key. If the key was not found it is returned null.
     */
    public String getAddingNewEntryValue(String attributeKey) {
        if (attributeKey.equals(EditorPanel.ADD_NEW_ENTRY_RDN_KEY) && this.addingNewEntry.containsKey(EditorPanel.ADD_NEW_ENTRY_RDN_KEY)) {
            return this.addingNewEntry.get(EditorPanel.ADD_NEW_ENTRY_RDN_KEY);
        } else if (attributeKey.equals(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY) && this.addingNewEntry.containsKey(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY)) {
            return this.addingNewEntry.get(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY);
        } else {
            return null;
        }
    }

    /**Method that sets the value given as argument in the key specific.
     * This method is only used to add a new entry.
     * At this moment this hash only stores the rdn (as string) of the new node to add, and the parentDn (as string).
     * 
     * @param attributeKey String. This is the key
     * @param attributeValue String. This is the value to store in the key
     */
    public void setAddingNewEntryValue(String attributeKey, String attributeValue) {
        if (attributeKey.equals(EditorPanel.ADD_NEW_ENTRY_RDN_KEY)) {
            this.addingNewEntry.put(EditorPanel.ADD_NEW_ENTRY_RDN_KEY, attributeValue);
        } else if (attributeKey.equals(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY)) {
            this.addingNewEntry.put(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY, attributeValue);
        }
    }

    /**Method that returns True: It means the editor is used to add a new entry, False: It means the editor is used to edit an existing entry.
     * 
     * @return boolean.
     */
    public boolean isAddingNewEntryMode() {
        return addingNewEntryMode;
    }

    /**Method that sets the editor mode:  True: It means the editor is used to add a new entry, False: It means the editor is used to edit an existing entry.
     * 
     * @param addingNewEntryMode boolean.
     */
    public void setAddingNewEntryMode(boolean addingNewEntryMode) {
        if (addingNewEntryMode) {
            this.addingNewEntry = new HashMap<String, String>();
        }

        this.addingNewEntryMode = addingNewEntryMode;
    }

    /**Method that returns the editor type. This field contains the title of the editor displayed
     * 
     * @return String. This is the title of the editor displayed
     */
    public String getEditorType() {
        return editorType;
    }

    /**Contructor: Initializes the elements of the edit panel.
     * 
     */
    public EditorPanel() {
        initComponents();
        try {
            this.ldapNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            this.attributes = ldapNode.getAttributes();
        } catch (Exception e) {
            this.loadDefaultAttributes();
            logger.error("No attribute found for this node!");
        }
        //We set the dn of the current node
        if (this.ldapNode != null) {
            this.dnLabel.setText(this.ldapNode.myDN);
        }
        initProperties();
        this.editorType = DefaultEditorTitle;
        this.editorMode = EDITOR_MODE_VIEW;
        this.setEditableEditor(false);
        this.addingNewEntryMode = false;
        this.addingNewEntry = null;
        this.closeButton.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dnLabel = new javax.swing.JLabel();
        containerScrollPane = new javax.swing.JScrollPane();
        containerPanel = new javax.swing.JPanel();
        commandPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        emptyPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        dnLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        dnLabel.setText("dn=...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(dnLabel, gridBagConstraints);

        containerPanel.setLayout(new java.awt.GridLayout(1, 1));
        containerScrollPane.setViewportView(containerPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(containerScrollPane, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        saveButton.setText("Save");
        saveButton.setToolTipText("Save your changes");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(saveButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Validate your changes");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(editButton, gridBagConstraints);

        closeButton.setText("Close");
        closeButton.setToolTipText("Close this editor without saving");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(closeButton, gridBagConstraints);

        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("Connects to the ldap server and updates the ldap nodes information");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(refreshButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        commandPanel.add(emptyPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(commandPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (this.ldapNode != null &&
                !(this.ldapNode instanceof TreeRootNode) &&
                !(this.ldapNode instanceof VirtualNode)) {
            this.setEditableEditor(true);
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (this.ldapNode != null &&
                !(this.ldapNode instanceof TreeRootNode) &&
                !(this.ldapNode instanceof VirtualNode)) {
            this.saveChanges();
            this.setEditableEditor(false);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (this.ldapNode != null &&
                !(this.ldapNode instanceof TreeRootNode) &&
                !(this.ldapNode instanceof VirtualNode)) {
            this.setEditableEditor(false);
            this.initProperties();

            //We update the values of the table...
            //We close all editors
            Utils.getMainWindow().getCurrentBrowserPanel().closeAllEditor();

            //We open all editors
            Utils.getMainWindow().getCurrentBrowserPanel().openAllEditor();

            logger.trace("EditorType:" + this.editorType);

            //We set the current the default editor as selected
            Utils.getMainWindow().getCurrentBrowserPanel().setCurrentEditor(this.editorType);
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        Utils.getAction(CloseEditor.HashKey).getAction().actionPerformed(null);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        //It calls to the action refresh
        Utils.getAction(RefreshTree.HashKey).getAction().actionPerformed(evt);
    }//GEN-LAST:event_refreshButtonActionPerformed

    /**Method that initializes the attributes of this class
     * 
     */
    public void initProperties() {
    }

    /**Method that sets this editor to edit
     * 
     * @param editable boolean. This parameter indicates if the editor is setted as editable
     */
    public void setEditableEditor(boolean editable) {
        Utils.setEditableMode(editable);
        if (editable) {
            this.editorMode = EditorPanel.EDITOR_MODE_EDIT;
        } else {
            this.editorMode = EditorPanel.EDITOR_MODE_VIEW;
        }

        this.saveButton.setEnabled(editable);
        this.cancelButton.setEnabled(editable);
        this.editButton.setEnabled(!editable);
        this.closeButton.setEnabled(!editable);
        this.refreshButton.setEnabled(!editable);
    }

    /**Method that save this changes in the ldap server. It sets the newAttributes as the attributes of this editor
     * 
     * @return boolean.
     */
    public boolean saveChanges() {
        String errorMsg = "";
        boolean saved = false;

        if (!this.addingNewEntryMode && this.editorMode.equals(EditorPanel.EDITOR_MODE_EDIT)) {
            errorMsg = this.thereAreChanges();
            logger.trace("errorMsg=" + errorMsg);
        } else if (this.addingNewEntryMode && this.editorMode.equals(EditorPanel.EDITOR_MODE_EDIT)) {
            logger.trace("Adding new entry mode");
            String rdn = this.addingNewEntry.get(EditorPanel.ADD_NEW_ENTRY_RDN_KEY);
            String parentDn = this.addingNewEntry.get(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY);

            try {
                Utils.createNewLDAPEntry(rdn, parentDn, Utils.getLDAPAttributeSetFromHash(this.newAttributes));
            } catch (LDAPException e) {
                errorMsg = "Error trying to create the new ldap entry: " + rdn + "," + parentDn + "\n" + e;
                logger.error(errorMsg);
            }
        }

        if (!errorMsg.equals("")) {
            String title = "Error found";
            String msg = "Unable to modify attributes";
            MessageDialog errorDialog = new MessageDialog(this,title, msg, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(errorMsg);

            // <editor-fold defaultstate="collapsed" desc=" Restore the initial values ">
            //To restore to default values
            this.initProperties();
            this.validate();
            this.repaint();
            // </editor-fold>
            saved = false;
        } else { //No error was found so we update the attributes
            this.attributes = this.newAttributes;
            this.ldapNode.setAttributes(attributes);
            logger.trace("Save Changes 1");
            Utils.printHash(ldapNode.getAttributes());
            logger.trace("Save Changes 2");
            this.dnLabel.setText(this.ldapNode.myDN);
            logger.trace("el dn es: " + this.ldapNode.myDN);

            //We update the tree
            ((TreeRootNode) this.ldapNode.getRoot()).getTree().updateUI();
            
            this.ldapNode.refreshNode();
            
            this.validate();
            this.repaint();
            saved = true;
        }
        this.editorMode = EditorPanel.EDITOR_MODE_VIEW;
        Utils.setEditableMode(false);
        return saved;
    }

    /**Method that checks if there are any difference between attributes and newAttributes. And modifies the attributes in the ldap server if no error was found.
     * 
     * @return String - If there is an error it is returned the error message. If there are not then it is returned ""
     */
    private String thereAreChanges() {
        String errorMsg = "";
        if (this.attributes != null && this.newAttributes != null) {
            //0 -Dn, 1- Objectclasses, 2- attributes
            Vector<LDAPModification> vChange = null;//DN
            boolean thereAreChanges = false;
            try {
                LDAPConnection ldapConnection = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getConnection(true);

                // <editor-fold defaultstate="collapsed" desc=" Changes in DN ">
                Vector vChangeInDn = this.thereAreChangesInDN();
                if (vChangeInDn != null && vChangeInDn.size() == 1) {
                    String newRdn = (String) vChangeInDn.firstElement();
                    try {
                        Utils.renameLDAPNode(this.ldapNode, newRdn);
                        thereAreChanges = true;
                    } catch (Exception e) {
                        errorMsg = e.toString() + "\n";
                        logger.error(errorMsg);
                    }
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc=" Changes in ObjectClasses ">
                logger.trace("We look for the objectclasses");
                vChange = this.thereAreChangesInAttributes(LDAPNode.INDEX_OBJECTCLASSES);//Objectclasses
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc=" Changes in Other Attributes ">
                logger.trace("We look for the attributes");
                Vector<LDAPModification> vAux = null;
                vAux = this.thereAreChangesInAttributes(LDAPNode.INDEX_ATTRIBUTES);//Other attributes
                for (int i = 0; i < vAux.size(); i++) {
                    vChange.addElement(vAux.elementAt(i));
                }
                // </editor-fold>

                
                if (vChange != null) { // It means that at least there is a modification
                    
                    //We copy the changes to an array of ldap modification to make in the same modify all the modifications
                    LDAPModification[] changes = new LDAPModification[vChange.size()];
                    for (int i = 0; i < vChange.size(); i++) {
                        changes[i] = vChange.elementAt(i);
                    }

                    //We make a single modify for all the ldap modifications
                    try {
                        ldapConnection.modify(this.ldapNode.myDN, changes);
                        thereAreChanges = true;
                    } catch (Exception ex) {
                        errorMsg = "Error trying to modify an attribute. " + "\n" + ex;
                        logger.error(errorMsg);
                    }
                }
                if (!thereAreChanges && errorMsg.equals("")) {
                    errorMsg = "No change was found" + "\n";
                    logger.trace(errorMsg);
                }

            } catch (Exception e) {
                errorMsg = "Error searching changes in attributes. " + "\n" + e;
                logger.error(errorMsg);
            }
        }
        return errorMsg;
    }

    /**Method that returns the changes in dn
     * 
     * @return Vector. This vector contains the new rdn to set
     * @throws java.lang.Exception Exception. If there is an error, it is thrown an exception
     */
    private Vector thereAreChangesInDN() throws Exception {
        Vector vChanges = new Vector();
        if (this.attributes[LDAPNode.INDEX_DN] != null && this.newAttributes[LDAPNode.INDEX_DN] != null) {
            if (this.attributes[LDAPNode.INDEX_DN].size() == this.newAttributes[LDAPNode.INDEX_DN].size()) {
                Iterator it = this.attributes[LDAPNode.INDEX_DN].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String attributeName = (String) entry.getKey();
                    logger.trace("attributeName:" + attributeName);
                    String[] attrValues = (String[]) entry.getValue();
                    if (this.newAttributes[LDAPNode.INDEX_DN].containsKey(attributeName)) {
                        String[] newAttrValues = this.newAttributes[LDAPNode.INDEX_DN].get(attributeName);
                        if (attrValues.length == newAttrValues.length) {
                            for (int i = 0; i < attrValues.length; i++) {
                                if (!attrValues[i].equals(newAttrValues[i])) {
                                    vChanges.addElement(attributeName + "=" + newAttrValues[i]);//We add the new rdn
                                    logger.trace("We add: " + newAttrValues[i]);
                                }
                            }
                        }
                    }
                }//End while
            }
        }
        return vChanges;
    }

    /** Method that returns a vector with the ldap modification found in the editor
     * 
    @param index int. This index is the value of the arrays attributes and newAttributes. 
     * Index 0: dn, index 1: objectclasses, index 2: other attributes
     * @return Vector LDAPModification. It is returned a Vector with the ldapmodification found in the editor
     * @throws java.lang.Exception An exception it is thrown if it is found an error.
     */
    private Vector<LDAPModification> thereAreChangesInAttributes(int index) throws Exception {
        logger.trace("thereAreChangesInAttributes index: " + index + ", oldAttributes:" + this.attributes[index].size() + ", newAttributes:" + this.newAttributes[index].size());
        Vector vChanges = new Vector();
        if (this.attributes[index] != null && this.newAttributes[index] != null) {

            //We check the new attributes. In this case we will add new attributes and we will add and remove values of an attribute
            Iterator it = this.newAttributes[index].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attributeName = (String) entry.getKey();
                String[] newAttrValues = (String[]) entry.getValue();

                //This attribute name is not contained in the old hash of attributes so we have to add a new attributename
                if (!this.attributes[index].containsKey(attributeName)) {
                    // <editor-fold defaultstate="collapsed" desc=" Attributes Added ">
                    //ADD A NEW ATTRIBUTE
                    vChanges.addElement(new LDAPModification(LDAPModification.ADD, new LDAPAttribute(attributeName, newAttrValues)));
                    logger.trace("elemento:" + vChanges.indexOf(vChanges.lastElement()) + ",added" + ", attrName:" + attributeName + ",value:" + newAttrValues);
                // </editor-fold>      

                } else { //This attribute is contained in the old hash of attributes, so we have to check the attribute values

                    //We get the attribute values of the old hash
                    String[] oldAttrValues = this.attributes[index].get(attributeName);

                    // <editor-fold defaultstate="collapsed" desc=" Check the attribute values of the new hash. Attributes Added ">
                    //We check the attribute values of the new hash. These are the attributes added
                    for (int i = 0; i < newAttrValues.length; i++) {
                        if (!newAttrValues[i].equals("") && !this.findValueInValues(newAttrValues[i], oldAttrValues)) {//If this attribute does not exist inside the oldAttributes, we have to add this value
                            //ADD A NEW VALUE OF AN ATTRIBUTE
                            vChanges.addElement(new LDAPModification(LDAPModification.ADD, new LDAPAttribute(attributeName, newAttrValues[i])));
                            logger.trace("elemento:" + vChanges.indexOf(vChanges.lastElement()) + ",added" + ", attrName:" + attributeName + ",value:" + newAttrValues[i]);
                        }
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc=" Chech the attribute values of the old hash. Attributes Removed ">
                    //We check the attribute values of the old hash. These are the attribute removed
                    for (int i = 0; i < oldAttrValues.length; i++) {
                        if (!oldAttrValues[i].equals("") && !this.findValueInValues(oldAttrValues[i], newAttrValues)) {
                            //REMOVE A VALUE OF AN ATTRIBUTE
                            vChanges.addElement(new LDAPModification(LDAPModification.DELETE, new LDAPAttribute(attributeName, oldAttrValues[i])));
                            logger.trace("elemento:" + vChanges.indexOf(vChanges.lastElement()) + ",delete" + ", attrName:" + attributeName + ",value:" + oldAttrValues[i]);
                        }
                    }
                // </editor-fold>
                }
            }

            logger.trace("These are the attributes:");
            Utils.printHash(this.attributes);
            logger.trace("These are the newAttributes:");
            Utils.printHash(this.newAttributes);

            if (index != LDAPNode.INDEX_OBJECTCLASSES) {
                //We check the old attributes hash. In this case we will remove the attributes that was not found
                it = this.attributes[index].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String attributeName = (String) entry.getKey();

                    // <editor-fold defaultstate="collapsed" desc=" Check the old attribute name in the new attribute name. Attributes Removed ">
                    //We check if the old attribute name is contained in the new attributes, if this attribute was not found then we will remove this attribute name
                    if (!this.newAttributes[index].containsKey(attributeName)) {
                        String[] newAttrValues = this.newAttributes[index].get(attributeName);

                        //REMOVE AN ATTRIBUTE
                        vChanges.addElement(new LDAPModification(LDAPModification.DELETE, new LDAPAttribute(attributeName, newAttrValues)));
                        logger.trace("elemento:" + vChanges.indexOf(vChanges.lastElement()) + ",delete" + ", attrName:" + attributeName + ",value:" + newAttrValues);
                    }
                // </editor-fold>
                }
            }
        }
        return vChanges;
    }

    /**Method that returns a boolean that tells us if the valueToSeach is contained inside the array values given as argument
     * 
     * @param valueToSearch String. This is the value to search inside the array.
     * @param values String[]. This is the values where we have to search the valueToSearch.
     * @return boolean. True- It means the value was found in the array. False- It means that the value was not found.
     */
    private boolean findValueInValues(String valueToSearch, String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (valueToSearch.equals(values[i])) {
                return true;
            }
        }
        return false;
    }

    /**Method that prints at the standard output the content of the hash given as argument
     * 
     * @param myHash HashMap <String,String[]>. This is the hash that contains the attributes what we want to show at the standard output
     */
    public void printHash(HashMap<String, String[]>[] myHash) {
        if (myHash != null) {
            for (int i = 0; i < myHash.length; i++) {
                if (myHash[i] != null) {
                    Iterator it = myHash[i].entrySet().iterator();
                    while (it.hasNext()) {
                        //Add the attribute 
                        Map.Entry entry = (Map.Entry) it.next();
                        String attributeName = (String) entry.getKey();
                        String[] values = (String[]) entry.getValue();
                        for (int j = 0; j < values.length; j++) {
                            System.out.println("attribute:" + attributeName + ",value:" + values[j]);
                        }
                    }
                }
            }
        }
    }

    /**Method that loads the default vallues in the editor. This values are created as init values in case of no node was selected
     * 
     */
    protected void loadDefaultAttributes() {
        this.attributes = new HashMap[3];
        this.attributes[0] = new HashMap<String, String[]>();
        this.attributes[0].put("None attribute found", new String[]{""});
        this.attributes[1] = null;
        this.attributes[2] = null;
    }

    /** Method that returns the panel that subclasses of this editor can add componenets to
     * 
     * @return the jPanel that subclasses are supposed to modify
     */
    public java.awt.Container getEditorContainerDelegate() {
        return this.containerPanel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton cancelButton;
    public javax.swing.JButton closeButton;
    private javax.swing.JPanel commandPanel;
    public javax.swing.JPanel containerPanel;
    protected javax.swing.JScrollPane containerScrollPane;
    public javax.swing.JLabel dnLabel;
    public javax.swing.JButton editButton;
    private javax.swing.JPanel emptyPanel;
    public javax.swing.JButton refreshButton;
    public javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
