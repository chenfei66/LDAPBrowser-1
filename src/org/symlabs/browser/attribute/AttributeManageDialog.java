package org.symlabs.browser.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ManageAttributeDialog </p>
 * <p>Descripcion: Dialog that shows the attributes availables to add to this node. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AttributeManageDialog.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class AttributeManageDialog extends javax.swing.JDialog {
    
    /**Attribute that contains the required attributes found*/
    private Vector requiredAttributes;
    /**Attribute that contains the optional attributes found*/
    private Vector optionalAttributes;
    /**Attribute that contains the all objectclasses found in the schema*/
    private Vector allObjectClasses;
    /**Attribute that contains the all attributes existing in this node*/
    private Vector<String> existingAttributes;
    /**Attribute that contains the html color for a required attribute*/
    private final static String REQUIRED_COLOR = Utils.HTML_RED_COLOR;
    /**Attribute that contains the html color for an optional attribute*/
    private final static String OPTIONAL_COLOR = Utils.HTML_BLUE_COLOR;
    /**Attribute that contains the html color for an existing attribute*/
    private final static String EXISTING_COLOR = Utils.HTML_GREEN_COLOR;
    /**Attribute that contains the init html code to set a color*/
    private final static String HTML_INIT = "<html><FONT COLOR='";
    /**Attribute that contains the end html code to set a color*/
    private final static String HTML_END = "</FONT></html>";
    /**Attribute that contains the init html code to set a required color*/
    private final static String HTML_INIT_REQUIRED_COLOR = HTML_INIT + REQUIRED_COLOR + "'>";
    /**Attribute that contians the init html code to set an optional color*/
    private final static String HTML_INIT_OPTIONAL_COLOR = HTML_INIT + OPTIONAL_COLOR + "'>";
    /**Attribute that contains the init html code to set an existing color*/
    private final static String HTML_INIT_EXISTING_COLOR = HTML_INIT + EXISTING_COLOR + "'>";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AttributeManageDialog.class);
    /**Attribute that identifies if this dialog is used to display an objectclasses or an attributes. True = manage objectclasses. False = manage other attributes*/
    private boolean manageObjectclasses;
    /**Attribute that contains the allAttributes displayed in the left list*/
    private Vector<String> allAtributes;
    /**Attribute that contains the selected attributes displayed in the right list*/
    private Vector<String> selectedAttributes;
    /**Attribute that contains the attributes displayed in the table editor*/
    private HashMap <String,String[]>[] attributesHash;

    /**Method that returns the existing attributes displayed at the init of this window
     * 
     * @return Vector. It is returned a vector of string that contains the attribute names displayed
     */
    public Vector<String> getExistingAttributes() {
        return existingAttributes;
    }

    /** Creates new form AddAttribute. This form is used to add an attribute.
     * @param ldapNodeOtherAttributes Vector <String>. These are the other attributes of the selected node
     * @param requiredAttributes Vector. This vector contains the required attributes found in the schema
     * @param optionalAttributes Vector. This vector contains the optional attributes found in the schema
     * @param attributeHash HashMap<String,String[]>. It contains the attributes displayed in the table editor
     */
    public AttributeManageDialog(Vector<String> ldapNodeOtherAttributes, Vector requiredAttributes, Vector optionalAttributes,HashMap <String,String[]>[] attributeHash) {
        super(Utils.getMainWindow(), true);
        initComponents();
        this.initAttributes(ldapNodeOtherAttributes, requiredAttributes, optionalAttributes,attributeHash);
        this.setTitle("Manage Attributes");
        this.infoMsgLabel.setText("Adds and removes the attributes");
        this.initLabels();
    }

    /** Creates new form AddAttribute. This form is used to add an objectclass.
     * @param ldapNodeObjectClasses Vector<String>. These are the objectclasses of the node
     * @param objectclasses Vector. This vector contains the objectclasses attributes found in the schema
     * @param attributeHash HashMap<String,String[]>. It contains the attributes displayed in the table editor
     */
    public AttributeManageDialog(Vector<String> ldapNodeObjectClasses, Vector objectclasses,HashMap <String,String[]>[] attributeHash) {
        super(Utils.getMainWindow(), true);
        initComponents();
        this.initObjectClasses(ldapNodeObjectClasses, objectclasses,attributeHash);
        this.setTitle("Manage ObjectClasses");
        this.infoMsgLabel.setText("Adds and removes the objectClasses");
        this.initLabels();
    }

    /**Method that initializes the values of this dialog: ldap node, and init List values
     * 
     * @param ldapNode LDAPNode. This is the ldap node where we are going to add attributes
     * @param requiredAttributes Vector. These are the required attributes for this node
     * @param optionalAttributes Vector. There are the optional attributes for this node
     */
    private void initAttributes(Vector<String> ldapNodeOtherAttributes, Vector requiredAttributes, Vector optionalAttributes, HashMap <String,String[]>[] attributeHash) {
        this.manageObjectclasses = false;
        this.attributesHash=attributeHash;
        this.requiredAttributes = requiredAttributes;
        Collections.sort(this.requiredAttributes);
        this.optionalAttributes = optionalAttributes;
        Collections.sort(this.optionalAttributes);
        this.allObjectClasses = null;
        this.existingAttributes = ldapNodeOtherAttributes;
        this.removeAttributesFound();
        logger.trace("We init the  available attributes to add");
        this.initValuesAttributesSelectorPanel();
    }

    /**Mehtod that initializes the valies of this dialog: ldap node and the init list values
     * 
     * @param ldapNode LDAPNode. This is the ldap node where we are going to add attributes
     * @param objectclasses Vector. These are the avalaible objectclasses for this node
     */
    private void initObjectClasses(Vector<String> ldapNodeObjectClasses, Vector objectclasses, HashMap <String,String[]>[] attributeHash) {
        this.manageObjectclasses = true;
        this.attributesHash=attributeHash;
        this.allObjectClasses = objectclasses;
        Collections.sort(this.allObjectClasses);
        this.requiredAttributes = null;
        this.optionalAttributes = null;
        this.existingAttributes = ldapNodeObjectClasses;
        this.removeAttributesFound();
        logger.trace("We init the  available objectclasses to add");
        this.initValuesAttributesSelectorPanel();
    }

    /**Method that initializes the vectors used to display the attributes contained in the all attributes list and the selected attributes list
     * 
     */
    private void initValuesAttributesSelectorPanel() {
        //Sets the model of the all attributes list
        this.allAtributes = new Vector<String>();
        if (this.requiredAttributes != null && this.optionalAttributes != null) { //We want to add an attribute
            logger.trace("We add to the list the required and optional attributes");
            //Adds required attributes
            for (int i = 0; i < this.requiredAttributes.size(); i++) {
                this.allAtributes.addElement(this.getRequiredAttributeWithColor((String) this.requiredAttributes.elementAt(i)));
            }

            //Adds optional attributes
            for (int i = 0; i < this.optionalAttributes.size(); i++) {
                this.allAtributes.addElement(this.getOptionalAttributeWithColor((String) this.optionalAttributes.elementAt(i)));
            }

        } else if (this.allObjectClasses != null) { //We want to add an objectclass
            logger.trace("We add to the list the objectclasses");
            for (int i = 0; i < this.allObjectClasses.size(); i++) {
                this.allAtributes.addElement(this.getOptionalAttributeWithColor((String) this.allObjectClasses.elementAt(i)));
            }
        }

        //Sets the model of the selected attributes list
        this.selectedAttributes = new Vector<String>();
        if (this.existingAttributes != null && !this.existingAttributes.isEmpty()) {
            for (int i = 0; i < this.existingAttributes.size(); i++) {
                this.selectedAttributes.addElement(this.getExistingAttributeWithColor((String) this.existingAttributes.elementAt(i)));
            }
        }

        //We set these values
        attributeSelectorPanel.setAllAttributes(allAtributes);
        attributeSelectorPanel.setSelectedAttributes(this.selectedAttributes);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        infoMsgLabel = new javax.swing.JLabel();
        attributeSelectorPanel = new org.symlabs.browser.attribute.AttributeSelectorPanel();
        commandPanel = new javax.swing.JPanel();
        finishButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        emptyPanel = new javax.swing.JPanel();
        infoMessagePanel = new javax.swing.JPanel();
        advise1Label = new javax.swing.JLabel();
        advise2Label = new javax.swing.JLabel();

        setTitle("");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        infoMsgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoMsgLabel.setText("Info Msg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(infoMsgLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(attributeSelectorPanel, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(finishButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        commandPanel.add(emptyPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(commandPanel, gridBagConstraints);

        infoMessagePanel.setLayout(new java.awt.GridBagLayout());

        advise1Label.setText("* If the attribute appears in...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        infoMessagePanel.add(advise1Label, gridBagConstraints);

        advise2Label.setText("* If the attribute appears in...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        infoMessagePanel.add(advise2Label, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(infoMessagePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        Vector vAttrsSelected = this.attributeSelectorPanel.getAttributesSelected();
        //If this panel is used to manage objectclasses we have to be sure that it must exist at least one objectclass
        if (this.manageObjectclasses && vAttrsSelected.isEmpty()) {
            String message = "ObjectClass was not found. Please select at least one objectClass from the left list.";
            String title = "Manage Attribute";
            MessageDialog errorDialog = new MessageDialog(this,title, message, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        } else {
            this.dispose();
        }
}//GEN-LAST:event_finishButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advise1Label;
    private javax.swing.JLabel advise2Label;
    private org.symlabs.browser.attribute.AttributeSelectorPanel attributeSelectorPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JButton finishButton;
    private javax.swing.JPanel infoMessagePanel;
    private javax.swing.JLabel infoMsgLabel;
    // End of variables declaration//GEN-END:variables
    /**Method that initializes the components of this Dialog
     * 
     */
    private void initLabels() {
        //Sets the label text in required attribute color
        if (this.requiredAttributes != null && !this.requiredAttributes.isEmpty()) {
            String message = "* If the attribute appears in red it is an required attribute";
            this.advise1Label.setText(AttributeManageDialog.HTML_INIT_REQUIRED_COLOR + message + AttributeManageDialog.HTML_END);
        } else {
            this.advise1Label.setVisible(false);
        }

        //Sets the label text in optional attribute color
        if (this.optionalAttributes != null && !this.optionalAttributes.isEmpty()) {
            String message2 = "* If the attribute appears in blue it is an optional attribute";
            this.advise2Label.setText(AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR + message2 + AttributeManageDialog.HTML_END);
        } else {
            this.advise2Label.setVisible(false);
        }

        //Sets the label text in optional attribute color
        if (this.allObjectClasses != null) {
            String message = "These are the objectclasses found in the schema";
            this.advise1Label.setText(AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR + message + AttributeManageDialog.HTML_END);
            this.advise1Label.setVisible(true);
            this.advise2Label.setVisible(false);
        }
    }

    /**Method that returns the required attribute given as argument with the required color
     * 
     * @param requiredAttribute. This is the attribute to add the html code
     * @return String. This is the string with the html code for required attribute
     */
    private String getRequiredAttributeWithColor(String requiredAttribute) {
        return AttributeManageDialog.HTML_INIT_REQUIRED_COLOR + requiredAttribute + AttributeManageDialog.HTML_END;
    }

    /**Method that returns the optional attribute given as argument with the optional color
     * 
     * @param optionalAttribute String. This is the attribute to add the html code
     * @return String. This is the String with the html code for an optional attribute
     */
    private String getOptionalAttributeWithColor(String optionalAttribute) {
        return AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR + optionalAttribute + AttributeManageDialog.HTML_END;
    }

    /**Method that returns the existing attribute given as argument with the existing color
     * 
     * @param existingAttribute String. This is the attribute to add the html code
     * @return String. This is the string with the html code for an existing attribute
     */
    private String getExistingAttributeWithColor(String existingAttribute) {
        return AttributeManageDialog.HTML_INIT_EXISTING_COLOR + existingAttribute + AttributeManageDialog.HTML_END;
    }

    /**Method that returns the existing attribute given as argument without the existing color. 
     * If existing color html was not found, it will be returned the same argument given
     * 
     * @param attributeWithColor String. This is the attribute with the html code
     * @return String. This is the attribute name without the html code
     */
    private String getExistingAttributeWithoutColor(String attributeWithColor) {
        String attributeName = "";
        if (attributeWithColor.indexOf(AttributeManageDialog.HTML_INIT_EXISTING_COLOR) != -1) {
            attributeName = attributeWithColor.substring(AttributeManageDialog.HTML_INIT_EXISTING_COLOR.length(), attributeWithColor.indexOf(AttributeManageDialog.HTML_END));
        } else {
            attributeName = attributeWithColor;
        }
        return attributeName;
    }

    /**Method that returns the existing attribute given as argument without the required color. 
     * If existing color html was not found, it will be returned the same argument given
     * 
     * @param attributeWithColor String. This is the attribute with the html code
     * @return String. This is the attribute name without the html code
     */
    private String getRequiredAttributeWithoutColor(String attributeWithColor) {
        String attributeName = "";
        if (attributeWithColor.indexOf(AttributeManageDialog.HTML_INIT_REQUIRED_COLOR) != -1) {
            attributeName = attributeWithColor.substring(AttributeManageDialog.HTML_INIT_REQUIRED_COLOR.length(), attributeWithColor.indexOf(AttributeManageDialog.HTML_END));
        } else {
            attributeName = attributeWithColor;
        }
        return attributeName;
    }

    /**Method that returns the existing attribute given as argument without the optional color. 
     * If existing color html was not found, it will be returned the same argument given
     * 
     * @param attributeWithColor String. This is the attribute with the html code
     * @return String. This is the attribute name without the html code
     */
    private String getOptionalAttributeWithoutColor(String attributeWithColor) {
        String attributeName = "";
        if (attributeWithColor.indexOf(AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR) != -1) {
            attributeName = attributeWithColor.substring(AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR.length(), attributeWithColor.indexOf(AttributeManageDialog.HTML_END));
        } else {
            attributeName = attributeWithColor;
        }
        return attributeName;
    }

    /**Method that returns the attributes selected in this dialog to be added
     * 
     * @return Vector <String[]> It is returned an array with the attributes selected
     */
    public Vector<String> getAttributesSelected() {
        Vector vAttrSelected = this.attributeSelectorPanel.getAttributesSelected();
        Vector<String> attributesSelected = new Vector();
        for (int i = 0; i < vAttrSelected.size(); i++) {
            String attribute = (String) vAttrSelected.elementAt(i);
            if (attribute.indexOf(AttributeManageDialog.HTML_INIT_REQUIRED_COLOR) != -1) {
                attributesSelected.addElement(this.getRequiredAttributeWithoutColor(attribute));
            } else if (attribute.indexOf(AttributeManageDialog.HTML_INIT_OPTIONAL_COLOR) != -1) {
                attributesSelected.addElement(this.getOptionalAttributeWithoutColor(attribute));
            } else if (attribute.indexOf(AttributeManageDialog.HTML_INIT_EXISTING_COLOR) != -1) {
                attributesSelected.addElement(this.getExistingAttributeWithoutColor(attribute));
            } else {
                attributesSelected.addElement(attribute);
            }
        }
        return attributesSelected;
    }

    /**Method that removes from requiredAttribute and optionalAttributes vectors the elements already exists in ldapNode
     * 
     */
    private void removeAttributesFound() {
        if (this.requiredAttributes != null && this.requiredAttributes.size() > 0 && this.optionalAttributes != null && this.optionalAttributes.size() > 0 && this.allObjectClasses == null) {
            // <editor-fold defaultstate="collapsed" desc=" Removes attributes stored in INDEX_DN ">
            //Remove attributes stored in INDEX_DN
            Iterator it = this.attributesHash[LDAPNode.INDEX_DN].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.requiredAttributes.contains(attrName)) {
                    this.requiredAttributes.removeElement(attrName);
                } else if (this.optionalAttributes.contains(attrName)) {
                    this.optionalAttributes.removeElement(attrName);
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Removes attributes stored in INDEX_OBJECTCLASSES ">
            //Remove attributes stored in INDEX_OBJECTCLASSES
            it = this.attributesHash[LDAPNode.INDEX_OBJECTCLASSES].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.requiredAttributes.contains(attrName)) {
                    this.requiredAttributes.removeElement(attrName);
                } else if (this.optionalAttributes.contains(attrName)) {
                    this.optionalAttributes.removeElement(attrName);
                }

                String[] attrValues = (String[]) entry.getValue();
                for (int i = 0; i < attrValues.length; i++) {
                    if (this.requiredAttributes.contains(attrValues[i])) {
                        this.requiredAttributes.removeElement(attrValues[i]);
                    } else if (this.optionalAttributes.contains(attrValues[i])) {
                        this.optionalAttributes.removeElement(attrValues[i]);
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Removes attibutes stored in INDEX_ATTRIBUTES ">
            //Remove attributes stored in INDEX_ATTRIBUTES
            it = this.attributesHash[LDAPNode.INDEX_ATTRIBUTES].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.requiredAttributes.contains(attrName)) {
                    this.requiredAttributes.removeElement(attrName);
                } else if (this.optionalAttributes.contains(attrName)) {
                    this.optionalAttributes.removeElement(attrName);
                }
            }
        // </editor-fold>
        } else if (this.allObjectClasses != null && this.allObjectClasses.size() > 0 && this.optionalAttributes == null && this.requiredAttributes == null) {
            // <editor-fold defaultstate="collapsed" desc=" Removes attributes stored in INDEX_DN ">
            //Remove attributes stored in INDEX_DN
            Iterator it = this.attributesHash[LDAPNode.INDEX_DN].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.allObjectClasses.contains(attrName)) {
                    this.allObjectClasses.removeElement(attrName);
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Removes attributes stored in INDEX_OBJECTCLASSES ">
            //Remove attributes stored in INDEX_OBJECTCLASSES
            it = this.attributesHash[LDAPNode.INDEX_OBJECTCLASSES].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.allObjectClasses.contains(attrName)) {
                    this.allObjectClasses.removeElement(attrName);
                }

                String[] attrValues = (String[]) entry.getValue();
                for (int i = 0; i < attrValues.length; i++) {
                    if (this.allObjectClasses.contains(attrValues[i])) {
                        this.allObjectClasses.removeElement(attrValues[i]);
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Removes attibutes stored in INDEX_ATTRIBUTES ">
            //Remove attributes stored in INDEX_ATTRIBUTES
            it = this.attributesHash[LDAPNode.INDEX_ATTRIBUTES].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String attrName = (String) entry.getKey();
                if (this.allObjectClasses.contains(attrName)) {
                    this.allObjectClasses.removeElement(attrName);
                }
            }
        // </editor-fold>
        } else {
            logger.error("No attributes required or optional was found");
        }
    }
}

