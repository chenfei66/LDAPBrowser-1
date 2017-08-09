package org.symlabs.browser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import netscape.ldap.util.DN;
import netscape.ldap.util.RDN;
import org.apache.log4j.Logger;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Schema;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddEntryDialog </p>
 * <p>Descripcion: Class which manages attributes and objectclasses to be added to a new ldap entry </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddEntryDialog.java,v 1.6 2009-08-24 09:01:06 efernandez Exp $
 */
public class AddEntryDialog extends javax.swing.JDialog {

    /**Attribute that contains the ldap node to add attributes*/
    private LDAPNode ldapNode;
    /**Attribute that contains the default ldap node used to get the default values for objectclasses and attributes*/
    private LDAPNode defaultLDAPNode;
    /**Attribute that contains the all attributes found in the schema*/
    private Vector<String> allAttributes;
    /**Attribute that contains the all objectclasses found in the schema*/
    private Vector<String> allObjectclasses;
    /**Attribute that contains the schema of the current ldapNode*/
    private Schema schema;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddEntryDialog.class);
    /**Attribute that contains the initial default values for the selected attributes*/
    private Vector<String> defaultAttributesSelected;
    /**Attribute that contains the initial default values for the selected objectclasses*/
    private Vector<String> defaultObjectclassesSelected;
    /**Attribute that contains the selected objectclasses*/
    private Vector<String> objectclassesSelected;
    /**Attribute that cotnains the selected attributes*/
    private Vector<String> attributesSelected;

    /** Creates new form AddEntryDialog
     * 
     * @param ldapNode LDAPNode. This is the ldap node that we have to add a new child
     */
    public AddEntryDialog(LDAPNode ldapNode) {
        super(Utils.getMainWindow(), true);
        initComponents();
        this.ldapNode = ldapNode;
        this.ldapNode.getAttributesNumber();
        this.schema = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getLdapSchema();
        this.allAttributes = this.schema.getAllAttributes();
        this.allObjectclasses = this.schema.getAllObjectclasses();
        if (this.ldapNode.getChildCount() > 0) {
            this.defaultLDAPNode = (LDAPNode) this.ldapNode.getChildAt(0);
            // <editor-fold defaultstate="collapsed" desc=" We get the default objectclasses ">
            //We get the default objectclasses
            this.defaultObjectclassesSelected = new Vector<String>();
            HashMap<String, String[]> hash = this.defaultLDAPNode.getOnlyObjectClasses();
            Iterator it = hash.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                //We get the values for objectclass
                String[] attrValues = (String[]) entry.getValue();
                for (int i = 0; i < attrValues.length; i++) {
                    this.defaultObjectclassesSelected.addElement(attrValues[i]);
                }
            }
            // </editor-fold>  

            // <editor-fold defaultstate="collapsed" desc=" We get the default attributes ">
            //We get the default attributes
            this.defaultAttributesSelected = new Vector<String>();
            hash = this.defaultLDAPNode.getOnlyAttributes();
            it = hash.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                this.defaultAttributesSelected.addElement((String) entry.getKey());
            }
        // </editor-fold>
        } else {
            this.defaultLDAPNode = null;
            this.defaultAttributesSelected = new Vector<String>();
            this.defaultObjectclassesSelected = new Vector<String>();
        }
        this.initProperties();
    }

    /**Method that initializes the values of the components of this dialog
     * 
     */
    public void initProperties() {
        this.parentDnValueLabel.setText(this.ldapNode.myDN);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        if (this.ldapNode.getChildCount() > 0) {
            LDAPNode child = (LDAPNode) this.ldapNode.getChildAt(0);
            String attributeRdn = Utils.getRDNAttribute(child.myRDN);
            model.addElement(attributeRdn);
        }
        this.rdnComboBox.setModel(model);

        //We load the objectclasses from objectclass panel
        this.objectclassesPanel.setAllAttributes(this.allObjectclasses);
        this.objectclassesPanel.setSelectedAttributes(this.defaultObjectclassesSelected);

        //We load the attributes from attributes panel
        this.attributesPanel.setAllAttributes(this.allAttributes);
        this.attributesPanel.setSelectedAttributes(this.defaultAttributesSelected);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dnPanel = new javax.swing.JPanel();
        RDnLabel = new javax.swing.JLabel();
        parentDnValueLabel = new javax.swing.JLabel();
        rdnComboBox = new javax.swing.JComboBox();
        parentDnLabel = new javax.swing.JLabel();
        containerTabbedPane = new javax.swing.JTabbedPane();
        objectclassesPanel = new org.symlabs.browser.attribute.AttributeSelectorPanel();
        attributesPanel = new org.symlabs.browser.attribute.AttributeSelectorPanel();
        commandPanel = new javax.swing.JPanel();
        defaultButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Entry");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        dnPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("DN"));
        dnPanel.setLayout(new java.awt.GridBagLayout());

        RDnLabel.setText("RDN:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(RDnLabel, gridBagConstraints);

        parentDnValueLabel.setText("dc= xxx, dc = xxx");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(parentDnValueLabel, gridBagConstraints);

        rdnComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        dnPanel.add(rdnComboBox, gridBagConstraints);

        parentDnLabel.setText("Parent:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(parentDnLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(dnPanel, gridBagConstraints);

        containerTabbedPane.setDoubleBuffered(true);
        containerTabbedPane.setOpaque(true);

        objectclassesPanel.setAllAttributes(null);
        objectclassesPanel.setMinimumSize(new java.awt.Dimension(179, 181));
        containerTabbedPane.addTab("ObjectClasses", objectclassesPanel);

        attributesPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        containerTabbedPane.addTab("Attributes", attributesPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(containerTabbedPane, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        defaultButton.setText("Default");
        defaultButton.setToolTipText("Restore the default values");
        defaultButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(defaultButton, gridBagConstraints);

        okButton.setText("OK");
        okButton.setToolTipText("Create the new ldap entry");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel this action");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(deleteButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(commandPanel, gridBagConstraints);

        pack();
    }// </editor-fold>                        
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String errorMsg = "";
        String title = "";
        String parentDn = this.parentDnValueLabel.getText();
        LDAPNode root = (LDAPNode) this.ldapNode.getRoot();
        if (DN.isDN(parentDn) && this.findDn(parentDn, root)) {
            if (RDN.isRDN((String) this.rdnComboBox.getSelectedItem())) {
                this.attributesSelected = this.attributesPanel.getAttributesSelected();
                this.objectclassesSelected = this.objectclassesPanel.getAttributesSelected();
            } else {//the rdn given is not a valid rdn
                errorMsg = "Please provide a valir RDN (Relative Distinguished Name)";
                title = "Error in RDN";
            }
        } else {//the dn given is not a valid dn
            errorMsg = "Please provide a valid DN(Distinguished Name). ";
            title = "Error in DN";
        }
        if (errorMsg.equals("")) {
            MessageDialog errorDialog = new MessageDialog(this,title, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        } else {//No error was found so we close this window
            this.dispose();
        }
    }

    /**Method that searches the dn given as argument in the ldapnode and the child nodes 
     * 
     * @param dnToSearch String. This is the dn to search
     * @param root LDAPNode. This is the ldap node where we want to start to search
     * @return boolean. True - It means it has been found  the dn given as argument. False- It means it has not been found.
     */
    private boolean findDn(String dnToSearch, LDAPNode root) {
        boolean found = false;
        if (root.myDN.equals(dnToSearch)) {
            return true;
        } else {
            for (int i = 0; i < root.getChildCount(); i++) {
                found = this.findDn(dnToSearch, (LDAPNode) root.getChildAt(i));
                if (found) {
                    return found;
                }
            }
        }
        return found;
    }

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.initProperties();
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.removeAllValues();
    }

    /**Method that removes from this panel all the attributes selected and the values for the rdn attribute
     * 
     */
    private void removeAllValues() {
        this.objectclassesPanel.setSelectedAttributes(new Vector<String>());
        this.attributesPanel.setSelectedAttributes(new Vector<String>());
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.rdnComboBox.getModel();
        model.removeAllElements();
    }
    // Variables declaration - do not modify                     
    private javax.swing.JLabel RDnLabel;
    private org.symlabs.browser.attribute.AttributeSelectorPanel attributesPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JTabbedPane containerTabbedPane;
    private javax.swing.JButton defaultButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel dnPanel;
    private org.symlabs.browser.attribute.AttributeSelectorPanel objectclassesPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel parentDnLabel;
    private javax.swing.JLabel parentDnValueLabel;
    private javax.swing.JComboBox rdnComboBox;
    // End of variables declaration                   
}
