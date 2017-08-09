package org.symlabs.browser.addentry;

import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.attribute.AttributeSelectorChangeI;
import org.symlabs.browser.attribute.AttributeSelectorPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Schema;
import org.symlabs.util.Utils;
import org.symlabs.wizard.Step;

/**
 * <p>Titulo: AddNewEntryStep1 </p>
 * <p>Descripcion: Class that shows all attributes and objectclasses  available to build a new entry </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddNewEntryStep1.java,v 1.21 2009-08-24 09:01:06 efernandez Exp $
 */
public class AddNewEntryStep1 extends Step {

    /**Attribute that contains the key used to store the rdn attribute*/
    protected static final String RDN_KEY = "RDN";
    /**Attribute that contains the key used to store the parent dn attribute*/
    protected static final String PARENT_DN_KEY = "DN";
    /**Attribute that contains the key used to store the selected objectclasses*/
    protected static final String OBJECTCLASSES_SELECTED_KEY = "ObjectClasses Selected";
    /**Attribute that contains the key used to store the selected attributes*/
    protected static final String ATTRIBUTES_SELECTED_KEY = "Attributes Selected";
    /**Attribute that contains the selected ldap node*/
    protected LDAPNode ldapNode;
    /**Attribute that contains the default ldap node used to get the default values for objectclasses and attributes*/
    private LDAPNode defaultLDAPNode;
    /**Attribute that contains the all attributes found in the schema*/
    private Vector<String> allAttributes;
    /**Attribute that contains the all objectclasses found in the schema*/
    private Vector<String> allObjectclasses;
    /**Attribute that contains the schema of the current ldapNode*/
    private Schema schema;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddNewEntryStep1.class);
    /**Attribute that contains the initial default values for the selected attributes*/
    private Vector<String> defaultAttributesSelected;
    /**Attribute that contains the initial default values for the selected objectclasses*/
    private Vector<String> defaultObjectclassesSelected;
    /**Attribute used to detect the event about add and remove values from objectclasses panel*/
    private MyChange myOCChange;
    /**Attribute used to detect the event about add and remove values from attributes panel*/
    private MyChange myAttrChange;
    /**Attribute used to identify the change detected: this is the type objectclasses*/
    private final String objectclassesChangeKey = "objectclassesChange";
    /**Attribute used to identify the change detected: this is the type attributes*/
    private final String attributesChangeKey = "attributesChange";

    public class MyChange implements AttributeSelectorChangeI {

        String type;

        public MyChange(String myType) {
            this.type = myType;
        }

        public void postAddElement(Object[] attributesAdded) {
            for (int i = 0; i < attributesAdded.length; i++) {
                logger.trace("Adding attribute: " + attributesAdded[i]);
                if (this.type.equals(objectclassesChangeKey)) {
                    addingAnObjectClass((String) attributesAdded[i]);
                } else if (this.type.equals(attributesChangeKey)) {
                    addingAnAttribute((String) attributesAdded[i]);
                }
            }
        }

        public void postRemoveElement(Object[] attributesRemoved) {
            for (int i = 0; i < attributesRemoved.length; i++) {
                logger.trace("Removing attribute: " + attributesRemoved[i]);
                if (this.type.equals(objectclassesChangeKey)) {
                    removingAnObjectClass((String) attributesRemoved[i]);
                } else if (this.type.equals(attributesChangeKey)) {
                    removingAnAttribute((String) attributesRemoved[i]);
                }
            }
        }
    }

    /**Method that removes the required attributes and the optional attributes relationship with this objectclassName
     * 
     * @param objectClassName String. This is the objectclass to remove
     */
    private void removingAnObjectClass(String objectClassName) {

        // <editor-fold defaultstate="collapsed" desc=" We calculate the required attributes that we have to remove ">
        Vector<String> vRequiredAttr = new Vector<String>();
        //These are the required attributes for the removed objectclass
        vRequiredAttr = schema.getRequiredAttributes(objectClassName, vRequiredAttr);

        //There are all the required attributes for the selected attributes
        Vector<String> vAllRequiredAttr = schema.getRequiredAttributes(this.objectclassesSelectorPanel.getAttributesSelected());

        //These are the attributes that we have to remove from the attributes selected in case of not matching with optional attributes
        Vector<String> requiredAttrToRemove = Utils.getVectorRemovingValues(vRequiredAttr, vAllRequiredAttr);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We calculate the optional attributes that we have to remove ">
        Vector<String> optionalAttrToRemove = new Vector<String>();
        Vector<String> vOptionalAttr = new Vector<String>();
        //These are the optional attributes for the removed objectclass
        vOptionalAttr = schema.getOptionalAttributes(objectClassName, vOptionalAttr);

        //These are the all optional attributes for the selected objectclasses
        Vector<String> vAllOptionalAttr = schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());

        //These are the selected attributes 
        Vector<String> vAttrSelected = this.attributesSelectorPanel.getAttributesSelected();
        for (int i = 0; i < vAttrSelected.size(); i++) {
            //If the attribute selected is contained in the optional attributes
            if (vOptionalAttr.contains(vAttrSelected.elementAt(i))) {
                //If the attribute selected is contained in the all optional attributes we do not have to remove it
                //or if the attribute selected is contained in the all required attributes
                if (!vAllOptionalAttr.contains(vAttrSelected.elementAt(i)) && !vAllRequiredAttr.contains(vAttrSelected.elementAt(i))) {
                    //The we have to remove this attribute
                    optionalAttrToRemove.addElement(vAttrSelected.elementAt(i));
                }
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We update the required attributes that does not match with the optional attributes ">
        //We search for the required attribute in the all optional attributes list
        for (int i = 0; i < vAllOptionalAttr.size(); i++) {
            //If the required attribute to remove contains the optional attribute then we remove it
            if (requiredAttrToRemove.contains(vAllOptionalAttr.elementAt(i))) {
                requiredAttrToRemove.remove(vAllOptionalAttr.elementAt(i));
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the new values for the selected attributes">
        //We have to remove the requiredAttr and the optionalAttr from the attributes selected
        vAttrSelected = Utils.getVectorRemovingValues(vAttrSelected, requiredAttrToRemove);
        vAttrSelected = Utils.getVectorRemovingValues(vAttrSelected, optionalAttrToRemove);
        this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, vAttrSelected);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the all attributes ">
        this.setAllAttributes(this.allAttributes, vAllOptionalAttr, vAttrSelected);
    // </editor-fold>
    }

    /**Method that removes the required and optional attributes, that we should to remove if the attributeName is removed
     * 
     * @param attributeName String.
     */
    private void removingAnAttribute(String attributeName) {        
        Vector<String> vAllRequiredAttr = schema.getRequiredAttributes(this.objectclassesSelectorPanel.getAttributesSelected());
        for (int i = 0; i < vAllRequiredAttr.size(); i++) {
            logger.trace("Element[" + i + "]:" + vAllRequiredAttr.elementAt(i) + ",we are searching: " + attributeName);
        }
        if (!vAllRequiredAttr.contains(attributeName)) {
//            logger.trace("It does not contain between the required attributes, so we can remove it");
//            //We can remove it
//            String title = "Attribute Removed";
//            String message = "The attribute " + attributeName + " has been removed successfully." + "\n";
//            String errorMsg = "Attribute " + attributeName + " removed. This is an optional attribute. You can add it later if you wish.";
//            MessageDialog errorDialog = new MessageDialog(title, message, errorMsg, MessageDialog.MESSAGE_INFORMATION);
//            errorDialog.setLocationRelativeTo(this);
//            errorDialog.setVisible(true);
        } else {
            logger.trace("It is contained between the required attributes");
            // <editor-fold defaultstate="collapsed" desc=" We set the attributes selected and the all attributes">
            //We have to remove it 
            Vector vAttrSelected = this.attributesSelectorPanel.getAttributesSelected();
            vAttrSelected.remove(attributeName);

            //We set the selected attributes
            this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, vAttrSelected);

            //We get the all optional attributes
            Vector objectClassesToRemove = schema.getObjectclassesFromAttribute(attributeName);
            
            Vector vAllOptionalAttr = schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());

            //We set the all attributes
            this.setAllAttributes(allAttributes, vAllOptionalAttr, vAttrSelected);
            
            // <editor-fold defaultstate="collapsed" desc="We set the objectclasses, and ask for removing an objectclass that contains the attributeName">
            //Vector that will contain the objectclasses that matches with the attributeName that we want to remove
            Vector objectClassesMatched = new Vector();
            Vector objectClassesSelected = this.objectclassesSelectorPanel.getAttributesSelected();
            for (int i = 0; i < objectClassesToRemove.size(); i++) {
                if (objectClassesSelected.contains(objectClassesToRemove.elementAt(i))) {
                    objectClassesMatched.addElement(objectClassesToRemove.elementAt(i));
                }
            }

            //We show a window with teh objectclasses that contains the attributeName that we want to remove
            ChooseObjectClassDialog newDialog = new ChooseObjectClassDialog(Utils.getMainWindow(), true, objectClassesMatched);
            newDialog.setSize(500, 300);
            newDialog.setLocationRelativeTo(this);
            newDialog.setVisible(true);

            //We get the selected objectclass to remove
            String objectClassName = newDialog.getSeletectObjectClass();
            if (objectClassName != null & !objectClassName.trim().equals("")) {

                // <editor-fold defaultstate="collapsed" desc=" We have to remove all the required attributes of the objectclass selected ">
                //We have to remove from the attribute list all the mandatory attributes of this objectclass
                Vector requiredAttrs = new Vector();
                requiredAttrs = this.schema.getRequiredAttributes(objectClassName, requiredAttrs);
                //We remove the required attrs from the attributes selected
                Vector attrSelected = this.attributesSelectorPanel.getAttributesSelected();
                for (int i = 0; i < requiredAttrs.size(); i++) {
                    if (attrSelected.contains(requiredAttrs.elementAt(i))) {
                        attrSelected.remove(requiredAttrs.elementAt(i));
                    }
                }
                //We set the attributes
                Vector optionalAttrs = this.schema.getOptionalAttributes(attrSelected);
                this.setAllAttributes(allAttributes, optionalAttrs, attrSelected);

                this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, attrSelected);

                // </editor-fold>
                
                objectClassesSelected.remove(objectClassName);
                //We update the objectclasses selected, removing the objectclass that contians the attribute name
                this.setSelectorPanelSelectedAttributes(objectclassesSelectorPanel, objectClassesSelected);

                //We update the all objectclasses
                this.setSelectorPanelAllAttributes(objectclassesSelectorPanel, Utils.getVectorRemovingValues(this.allObjectclasses, this.objectclassesSelectorPanel.getAttributesSelected()));
            }else{
                // <editor-fold defaultstate="collapsed" desc=" We restore the values of the attributes ">
                //We set the attributes selected
                Vector attrSelected = this.attributesSelectorPanel.getAttributesSelected();
                attrSelected.add(attributeName);
                this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, attrSelected);

                //We set the all attributes
                Vector optionalAttrs = new Vector();
                optionalAttrs= this.schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());
                this.setAllAttributes(
                        Utils.getVectorRemovingValues(allAttributes,this.attributesSelectorPanel.getAttributesSelected()), 
                        Utils.getVectorRemovingValues(optionalAttrs, this.attributesSelectorPanel.getAttributesSelected()),
                        this.attributesSelectorPanel.getAttributesSelected());
                
                // </editor-fold>
            }
            // </editor-fold>
            
            
        // </editor-fold>
        }
        this.setRdnComboBoxModel();
    }

    /**Method called when an objectclass is added. 
     * It will add the required attributes to the selected list. 
     * And it will add the optional attributes to the all attributes list. 
     * And it willl also remove the selected attributes from all attributes list.
     * 
     * @param objectClassName String. This is the objectClass name to add
     */
    private void addingAnObjectClass(String objectClassName) {
        Vector vRequiredAttr = new Vector();
        //We get the required attributes for the selected objectclass
        vRequiredAttr = schema.getRequiredAttributes(objectClassName, vRequiredAttr);
        logger.trace("vRequiredAttr.size:" + vRequiredAttr.size());

        //We add the required attributes to the attributes selected
        Vector vNewAttrSelected = Utils.getVectorAddingValues(attributesSelectorPanel.getAttributesSelected(), vRequiredAttr);
        logger.trace("vNewAttrSelected.size:" + vNewAttrSelected.size());
        for (int i = 0; i < vNewAttrSelected.size(); i++) {
            logger.trace("NewAttrSelected[" + i + "]:" + vNewAttrSelected.elementAt(i));
        }
        //We set the new selected attributes values
        this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, vNewAttrSelected);

        //We set the all attributes list
        Vector<String> vAllOptAttr = schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());
        for (int i = 0; i < vAllOptAttr.size(); i++) {
            logger.trace("vAllOptAttr[" + i + "]:" + vAllOptAttr.elementAt(i));
        }

        logger.trace("All attr:" + this.allAttributes.size() + ",optionalAttr:" + vAllOptAttr.size() + ",attrSelected:" + this.attributesSelectorPanel.getAttributesSelected().size());
        this.setAllAttributes(this.allAttributes, vAllOptAttr, this.attributesSelectorPanel.getAttributesSelected());
        
        this.setRdnComboBoxModel();
    }

    /**Method that sets the left list of attributes in the attributesSelectorPanel
     * 
     * @param vAllAttributes Vector <String>. These are all the available attributes
     * @param vOptionalAttributes Vector <String>. These are all the optional attributes
     * @param vSelectedAttributes Vector <String>. These are all the selected attributes
     */
    private void setAllAttributes(Vector<String> vAllAttributes, Vector<String> vOptionalAttributes, Vector<String> vSelectedAttributes) {
        if (this.allAttributesCheckBox.isSelected()) {
            Vector vAllAttr = Utils.getVectorRemovingValues(vAllAttributes, vSelectedAttributes);
            this.setSelectorPanelAllAttributes(this.attributesSelectorPanel, vAllAttr);
        } else {
            this.setSelectorPanelAllAttributes(this.attributesSelectorPanel, vOptionalAttributes);
        }
        
    }

    /**Method called to add an attribute. 
     * It will get the objectclasses avilable for this attributeName given as argument.
     * You will be asked for choose one of the available objectclasses.
     * It will be added the objectclass selected using addingAnObjectClass
     * 
     * @param attributeName String. This is the attribute name to add
     * @see addingAnObjectClass(String objectClassName)
     */
    private void addingAnAttribute(String attributeName) {
        Vector vObjectClasses = schema.getObjectclassesFromAttribute(attributeName);
        //First of all we have to check if this attribute is between the optional attributes
        Vector vAllOptAttr = schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());

        //If this attribute is not between the optional attributes then we have to check this attribute to add it
        if (!vAllOptAttr.contains(attributeName)) {

            // <editor-fold defaultstate="collapsed" desc=" We create the dialog window to ask for choosing an available objectclass ">
            ChooseObjectClassDialog newDialog = new ChooseObjectClassDialog(Utils.getMainWindow(), true, vObjectClasses);
            newDialog.setSize(500, 400);
            newDialog.setLocationRelativeTo(Utils.getMainWindow());
            newDialog.setVisible(true);
            // </editor-fold>

            //We have to show a window asking for chosing one of the vObjectclasses
            String objectClassName = newDialog.getSeletectObjectClass();
            if (objectClassName != null & !objectClassName.trim().equals("")) {

                // <editor-fold defaultstate="collapsed" desc=" We set the new objectclass selected ">
                //We set the new objectclass selected
                Vector vNewAllObj = this.objectclassesSelectorPanel.getAttributesSelected();
                vNewAllObj.addElement(objectClassName);
                this.setSelectorPanelSelectedAttributes(this.objectclassesSelectorPanel, vNewAllObj);
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc=" We set the all available objectclasses list ">
                //We set the all objectclasses, we have to remove the attributes selected
                this.setSelectorPanelAllAttributes(this.objectclassesSelectorPanel, Utils.getVectorRemovingValues(this.allObjectclasses, vNewAllObj));
                // </editor-fold>

                //We add the required attributes for the new objectclass
                addingAnObjectClass(objectClassName);               
            }
        }
        this.setRdnComboBoxModel();
    }

    /** Creates new form AddNewEntryStep1 */
    public AddNewEntryStep1() {
        super("Choose attributes");
        this.myOCChange = new MyChange(this.objectclassesChangeKey);
        this.myAttrChange = new MyChange(this.attributesChangeKey);
        initComponents();
        this.objectclassesSelectorPanel.setSelectorChange(myOCChange);
        this.attributesSelectorPanel.setSelectorChange(myAttrChange);
    }

    /**Method that initializes the values of the ldap node
     * 
     */
    private void initDefaultLDAPNode() {
        if (this.ldapNode.getChildCount() > 0) {
            logger.trace("defaultnode != null");
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
            hash = this.defaultLDAPNode.getOnlyDNAttribute();
            it = hash.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                this.defaultAttributesSelected.addElement((String) entry.getKey());
            }
        // </editor-fold>
        } else {
            logger.trace("defaultnode = null, ldapNode.mydn= " + this.ldapNode.myDN + ", childcount:" + this.ldapNode.getChildCount());
            this.defaultLDAPNode = null;
            this.defaultAttributesSelected = new Vector<String>();
            this.defaultObjectclassesSelected = new Vector<String>();
        }
    }

    /**Method that initializes the values for the objectclasses panel and attributes panel
     * 
     */
    private void initAttributesPanel() {
        //We load the objectclasses from objectclass panel
        this.objectclassesSelectorPanel.setAllAttributesAndSelectedAttributes(this.allObjectclasses, this.defaultObjectclassesSelected);

        //We load the attributes from attributes panel
        this.attributesSelectorPanel.setAllAttributesAndSelectedAttributes(this.allAttributes, this.defaultAttributesSelected);
    }

    /**Method that initializes the values for comboBox rdn and parentDn
     * 
     */
    private void initComboBoxValues() {
        //We set the values for the parentDn comboBox
        Vector<String> vParentDnValues = new Vector<String>();
        vParentDnValues = Utils.getAllFirstNodeNames(this.ldapNode, vParentDnValues);
        DefaultComboBoxModel model = new DefaultComboBoxModel(vParentDnValues);
        this.parentDnComboBox.setModel(model);

        //We set the values for the rdn comboBox
        Vector<String> vRdn = this.attributesSelectorPanel.getAttributesSelected();
        if (vRdn != null && vRdn.size() > 0) {
            model = new DefaultComboBoxModel(vRdn);
        } else {
            model = new DefaultComboBoxModel();
        }
        this.rdnComboBox.setModel(model);
    }

    @Override
    protected void fetch_params() {
        if (this.parameters.containsKey(AddNewEntryStep1.RDN_KEY)) {
            this.parameters.remove(RDN_KEY);
        }
        this.parameters.put(RDN_KEY, (String) this.rdnComboBox.getSelectedItem());
        if (this.parameters.containsKey(AddNewEntryStep1.PARENT_DN_KEY)) {
            this.parameters.remove(PARENT_DN_KEY);
        }
        this.parameters.put(PARENT_DN_KEY, (String) this.parentDnComboBox.getSelectedItem());
        if (this.parameters.containsKey(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY)) {
            this.parameters.remove(OBJECTCLASSES_SELECTED_KEY);
        }
        this.parameters.put(OBJECTCLASSES_SELECTED_KEY, this.objectclassesSelectorPanel.getAttributesSelected());
        if (this.parameters.containsKey(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY)) {
            this.parameters.remove(ATTRIBUTES_SELECTED_KEY);
        }
        this.parameters.put(ATTRIBUTES_SELECTED_KEY, this.attributesSelectorPanel.getAttributesSelected());
    }

    @Override
    protected void put_params() {

        // <editor-fold defaultstate="collapsed" desc=" We set the ldapNode ">
        //We set the selected ldapnode
        if (this.parameters.containsKey(AddNewEntryDialog.LDAPNODE_KEY)) {
            this.ldapNode = (LDAPNode) this.parameters.get(AddNewEntryDialog.LDAPNODE_KEY);
        } else {
            this.ldapNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the schema, and get all attributes and all objectclasses ">
        //We get all the attributes and all the objectclasses from the schema
        this.schema = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getLdapSchema();
        this.allAttributes = Schema.removeAttributeObjectClass(this.schema.getAllAttributes());
        this.allObjectclasses = Schema.removeAttributeObjectClass(this.schema.getAllObjectclasses());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the default ldalNode and default objectclasses and attributes ">
        //We set the default ldapNode and load the default objectclasses and attributes for this ldap node
        this.initDefaultLDAPNode();
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set all objectclasses ">
        //We set all available objectclasses
        this.setSelectorPanelAllAttributes(objectclassesSelectorPanel, this.allObjectclasses);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the selected objectclasses ">
        if (this.parameters.containsKey(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY)) {
            //We set the selected objectclasses
            Vector<String> objectclassesSelected = (Vector<String>) this.parameters.get(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY);
            this.setSelectorPanelSelectedAttributes(objectclassesSelectorPanel, objectclassesSelected);
        } else {
            //We set the default objectclasses
            this.setSelectorPanelSelectedAttributes(objectclassesSelectorPanel, this.defaultObjectclassesSelected);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the selected attributes ">
        if (this.parameters.containsKey(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY)) {
            //We set the selected attributes
            Vector<String> attributesSelected = (Vector<String>) this.parameters.get(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY);
            this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, attributesSelected);
        } else {
            //We set the default attributes
            this.setSelectorPanelSelectedAttributes(attributesSelectorPanel, this.defaultAttributesSelected);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the all available attributes ">
        //We have to set the all attributes or the optional attributes
        if (this.allAttributesCheckBox.isSelected()) {//We set all attributes
            //We have to remove the selected attributes from this list and we set the all attributes
            Vector vAllAttr = new Vector();
            vAllAttr = Utils.getVectorRemovingValues(this.allAttributes, this.attributesSelectorPanel.getAttributesSelected());
            this.setSelectorPanelAllAttributes(attributesSelectorPanel, vAllAttr);
        } else {//We set optional attributes

            //We have to get the optional attributes given by the objectclasses selected
            Vector vAllOptionalAttributes = schema.getOptionalAttributes(this.objectclassesSelectorPanel.getAttributesSelected());
            logger.trace("allAttributesCheckBox is not selected 1,size:" + vAllOptionalAttributes.size());
            //We have to remove the attributes selected
            vAllOptionalAttributes = Utils.getVectorRemovingValues(vAllOptionalAttributes, this.attributesSelectorPanel.getAttributesSelected());
            logger.trace("allAttributesCheckBox is not selected 2,size:" + vAllOptionalAttributes.size());
            //We set the optional attributes
            this.setSelectorPanelAllAttributes(attributesSelectorPanel, vAllOptionalAttributes);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the rdn comboBox values ">
        if (this.parameters.containsKey(AddNewEntryStep1.RDN_KEY)) {
            this.setRdnComboBoxModel();
        } else {
            //We have to load the default attributes
            DefaultComboBoxModel model = new DefaultComboBoxModel(this.defaultAttributesSelected);
            this.rdnComboBox.setModel(model);

            //We set the default rdn attribute
            if (this.defaultLDAPNode != null) {
                String rdnAttr = Utils.getRDNAttribute(this.defaultLDAPNode.myRDN);
                if (this.defaultAttributesSelected.contains(rdnAttr)) {
                    model.setSelectedItem(rdnAttr);
                }
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" We set the parent dn values">
        if (this.parameters.containsKey(AddNewEntryStep1.PARENT_DN_KEY)) {
            //We have to set the parent dn selected
            String parentDnSelected = (String) this.parameters.get(AddNewEntryStep1.PARENT_DN_KEY);
            this.setParentDnComboBoxModel(parentDnSelected);
        } else {
            //We have to set the default parent dn selected
            String parentDnSelected = this.ldapNode.myDN;
            this.setParentDnComboBoxModel(parentDnSelected);
        }
    // </editor-fold>

    }

    /**Method that sets the default comboBox model, and sets the value given as argument as selected if this value is found
     * 
     * @param selectedValue String. This is the value to set as selected. 
     * If this attribute is not found then it is selected the first element of the model. 
     * If no element is found then it is setted as a selected element an empty string
     */
    private void setParentDnComboBoxModel(String selectedValue) {
        //We have to set the parent dn model
        Vector<String> vParentDnValues = new Vector<String>();
        vParentDnValues = Utils.getAllFirstNodeNames((LDAPNode) this.ldapNode.getRoot(), vParentDnValues);
        DefaultComboBoxModel model = new DefaultComboBoxModel(vParentDnValues);
        this.parentDnComboBox.setModel(model);

        if(model!=null){//We add to the model the selected value
            if(model.getIndexOf(selectedValue)==-1){
                model.addElement(selectedValue);
            }
        }
        if (vParentDnValues.contains(selectedValue)) {
            model.setSelectedItem(selectedValue);
        } else if (model.getSize() > 0) {
            model.setSelectedItem(model.getElementAt(0));
        } else {
            model.setSelectedItem("");
        }
    }

    /**Method that sets the all attributes in the selector panel. If tha attribute objectclass is found then it will be removed.
     * 
     * @param selectorPanel AttributeSelectorPAnel. This is the selectorPanel to set the all attributes
     * @param vAllAttributes Vector <String>. These are the all attributes to be setted
     */
    private void setSelectorPanelAllAttributes(AttributeSelectorPanel selectorPanel, Vector<String> vAllAttributes) {
        selectorPanel.setAllAttributes(Schema.removeAttributeObjectClass(vAllAttributes));
        if(selectorPanel.equals(this.attributesSelectorPanel)){
            this.setRdnComboBoxModel();
        }
    }

    /**Method that sets the selected attribute in the selector panel. If the attribute objectclass is found then it will be removed
     * 
     * @param selectorPanel AttributeSelectorPanel. This is the selectorPanel to set the selected attributes
     * @param vSelectedAttributes Vector <String>. These are the attributes to be setted
     */
    private void setSelectorPanelSelectedAttributes(AttributeSelectorPanel selectorPanel, Vector<String> vSelectedAttributes) {
        selectorPanel.setSelectedAttributes(Schema.removeAttributeObjectClass(vSelectedAttributes));
        if (selectorPanel.equals(this.attributesSelectorPanel)) {
            this.setRdnComboBoxModel();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dnPanel = new javax.swing.JPanel();
        rdnLabel = new javax.swing.JLabel();
        rdnComboBox = new javax.swing.JComboBox();
        parentDnLabel = new javax.swing.JLabel();
        parentDnComboBox = new javax.swing.JComboBox();
        attributeTabbedPane = new javax.swing.JTabbedPane();
        objectclassesSelectorPanel = new org.symlabs.browser.attribute.AttributeSelectorPanel();
        attributesPanel = new javax.swing.JPanel();
        attributesSelectorPanel = new org.symlabs.browser.attribute.AttributeSelectorPanel();
        allAttributesCheckBox = new javax.swing.JCheckBox();
        commandPanel = new javax.swing.JPanel();
        defaultButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        dnPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("DN"));
        dnPanel.setLayout(new java.awt.GridBagLayout());

        rdnLabel.setText("RDN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(rdnLabel, gridBagConstraints);

        rdnComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(rdnComboBox, gridBagConstraints);

        parentDnLabel.setText("Parent");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(parentDnLabel, gridBagConstraints);

        parentDnComboBox.setEditable(true);
        parentDnComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        parentDnComboBox.setToolTipText("These are some of the available parent dns");
        parentDnComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                parentDnComboBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        dnPanel.add(parentDnComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        add(dnPanel, gridBagConstraints);

        attributeTabbedPane.addTab("ObjectClasses", objectclassesSelectorPanel);

        attributesPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        attributesPanel.add(attributesSelectorPanel, gridBagConstraints);

        allAttributesCheckBox.setText("All Attributes");
        allAttributesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allAttributesCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        attributesPanel.add(allAttributesCheckBox, gridBagConstraints);

        attributeTabbedPane.addTab("Attributes", attributesPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        add(attributeTabbedPane, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        defaultButton.setText("Default");
        defaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(defaultButton, gridBagConstraints);

        removeButton.setText("Remove All");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        add(commandPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        this.removeAllValues();
    }//GEN-LAST:event_removeButtonActionPerformed

    /**Method that removes all the values contained in this panel
     * 
     */
    private void removeAllValues() {
        //We remove the elements of the rdn comboBox model
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.rdnComboBox.getModel();
        model.removeAllElements();

        //We load the objectclasses from objectclass panel
        this.objectclassesSelectorPanel.setAllAttributesAndSelectedAttributes(this.allObjectclasses, new Vector());

        //We load the attributes from attributes panel
        this.attributesSelectorPanel.setAllAttributesAndSelectedAttributes(this.allAttributes, new Vector());
    }

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultButtonActionPerformed
        for (int i = 0; i < this.defaultObjectclassesSelected.size(); i++) {
            logger.trace("defaultObjectClas[" + i + "]:" + this.defaultObjectclassesSelected.elementAt(i));
        }
        for (int i = 0; i < this.defaultAttributesSelected.size(); i++) {
            logger.trace("defaultAttributes[" + i + "]:" + this.defaultAttributesSelected.elementAt(i));
        }
        this.parameters.remove(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY);
        this.parameters.remove(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY);
        this.put_params();
    }//GEN-LAST:event_defaultButtonActionPerformed

    private void allAttributesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allAttributesCheckBoxActionPerformed
        if (this.allAttributesCheckBox.isSelected()) {//We have to set all available attributes
            Vector vAllAttr = new Vector();
            vAllAttr = Utils.getVectorRemovingValues(allAttributes, this.attributesSelectorPanel.getAttributesSelected());
            this.setSelectorPanelAllAttributes(attributesSelectorPanel, vAllAttr);
        } else {//we have to set only the optional attributes
            Vector vAllOptAttr = this.objectclassesSelectorPanel.getAttributesSelected();
            vAllOptAttr = schema.getOptionalAttributes(vAllOptAttr);
            //We have to remove from this list the attributes already added in the selected attributes list
            vAllOptAttr = Utils.getVectorRemovingValues(vAllOptAttr, this.attributesSelectorPanel.getAttributesSelected());
            this.setSelectorPanelAllAttributes(attributesSelectorPanel, vAllOptAttr);
        }
    }//GEN-LAST:event_allAttributesCheckBoxActionPerformed

    private void parentDnComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_parentDnComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            logger.trace("State: " + evt.getStateChange() + " Element:" + evt.getItem().toString());
            String selectedParentDn = (String) this.parentDnComboBox.getSelectedItem();
            if (!this.checkParentDnComboBox(selectedParentDn)) {
                selectedParentDn = this.ldapNode.myDN;
                this.setParentDnComboBoxModel(selectedParentDn);
            }
        }
    }//GEN-LAST:event_parentDnComboBoxItemStateChanged

    /**Method that checks the selected parent dn. If the parent dn is not found then it is shown an error message.
     * 
     * @return boolean. True means that the parent dn is right, False means the parent dn is wrong
     */
    private boolean checkParentDnComboBox(String parentDn) {
        boolean valid = false;
        String errorMsg = "";
        String title = "";
        if (parentDn !=null && !parentDn.trim().equals("")) {
            //We have to check if the selected parentDn exists
            try {
                LDAPSearchResults results = ((TreeRootNode) this.ldapNode.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(parentDn);
                if (results != null && results.getCount() == 1) {
                    valid = true;
                    logger.trace("Se ha chequedado el parent Dn:"+parentDn);
                }
            } catch (LDAPException e) {
                errorMsg = "Error searching the parentDn: " + parentDn + "," + e;
                logger.error(errorMsg);
                valid = false;
                title = "Error in Parent DN";
            }
        }
        if (!valid) {
            String message = "Error Searching the Parent Dn selected.";
            MessageDialog errorDialog = new MessageDialog(this,title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
        return valid;
    }

    /**Method that sets the values for the Rdn ComboBox. The values to be setted must be the attributes selected
     * 
     */
    private void setRdnComboBoxModel() {
        //We have to load the attributes
        if (this.rdnComboBox != null && this.rdnComboBox.getModel() != null) {
            logger.trace("We update the rdn attributes");
            DefaultComboBoxModel model = new DefaultComboBoxModel(this.attributesSelectorPanel.getAttributesSelected());
            this.rdnComboBox.setModel(model);

            //We have to set the rdn selected
            String rdnAttr = (String) this.parameters.get(AddNewEntryStep1.RDN_KEY);

            if (this.attributesSelectorPanel.getAttributesSelected().contains(rdnAttr)) {
                logger.trace("We set the attribute selected rdnAttr");
                model.setSelectedItem(rdnAttr);
            } else if (model.getSize() > 0) {
                model.setSelectedItem(model.getElementAt(0));
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allAttributesCheckBox;
    private javax.swing.JTabbedPane attributeTabbedPane;
    private javax.swing.JPanel attributesPanel;
    private org.symlabs.browser.attribute.AttributeSelectorPanel attributesSelectorPanel;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JButton defaultButton;
    private javax.swing.JPanel dnPanel;
    private org.symlabs.browser.attribute.AttributeSelectorPanel objectclassesSelectorPanel;
    private javax.swing.JComboBox parentDnComboBox;
    private javax.swing.JLabel parentDnLabel;
    private javax.swing.JComboBox rdnComboBox;
    private javax.swing.JLabel rdnLabel;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
}
