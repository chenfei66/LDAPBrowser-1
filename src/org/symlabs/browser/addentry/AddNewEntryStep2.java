package org.symlabs.browser.addentry;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.symlabs.browser.editor.EditorPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Schema;
import org.symlabs.util.Utils;
import org.symlabs.wizard.Step;

/**
 * <p>Titulo: AddNewEntryStep1 </p>
 * <p>Descripcion: Class that shows all attributes and objectclasses  available to build a new entry </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddNewEntryStep2.java,v 1.10 2009-07-27 16:55:15 efernandez Exp $
 */
public class AddNewEntryStep2 extends Step {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddNewEntryStep2.class);
    /**HashMap that contains the attributes shown*/
    private HashMap<String, String[]> attributes[];

    @Override
    protected void fetch_params() {
    //Nothing required
    }

    @Override
    protected void put_params() {
        String rdn = "";
        String parentDn = "";
        Vector<String> vObjectclasses = null;
        Vector<String> vAttributes = null;
        if (this.parameters.containsKey(AddNewEntryStep1.RDN_KEY)) {
            rdn = (String) this.parameters.get(AddNewEntryStep1.RDN_KEY);
        }
        if (this.parameters.containsKey(AddNewEntryStep1.PARENT_DN_KEY)) {
            parentDn = (String) this.parameters.get(AddNewEntryStep1.PARENT_DN_KEY);
        }
        this.editorFieldValue.dnLabel.setText(rdn + "=XXXXXX," + parentDn);
        if (this.parameters.containsKey(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY)) {
            vObjectclasses = (Vector<String>) this.parameters.get(AddNewEntryStep1.OBJECTCLASSES_SELECTED_KEY);
        }
        if (this.parameters.containsKey(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY)) {
            vAttributes = (Vector<String>) this.parameters.get(AddNewEntryStep1.ATTRIBUTES_SELECTED_KEY);
        }

        this.attributes = this.createAttributeHash(vObjectclasses, vAttributes, rdn, parentDn);
        this.editorFieldValue.setAttributesHashMap(this.attributes);
        this.editorFieldValue.setEditableEditor(true);
        this.editorFieldValue.setAddingNewEntryMode(true);
    }

    /**Method that creates the attribute hashmap used to store all the attributes contained in the new ldap entry
     * 
     * @param vObjectClasses Vector <String>. This attribute contains all the objectclasses selected
     * @param vAttributes Vector <String>. This attribute contains all the attributes selected
     * @param rdn String. This is the rdn for this new ldap entry
     * @param parentDn String. This is the parent dn for this new parent dn
     * @return HashMap<String, String[]>[] .This hash contains the attributes used for this new ldap entry
     */
    private HashMap<String, String[]>[] createAttributeHash(Vector<String> vObjectClasses, Vector<String> vAttributes, String rdn, String parentDn) {
        //We create the hashmap that will contains the attributes
        HashMap<String, String[]>[] hash = new HashMap[3];
        hash[LDAPNode.INDEX_DN] = new HashMap<String, String[]>();
        hash[LDAPNode.INDEX_OBJECTCLASSES] = new HashMap<String, String[]>();
        hash[LDAPNode.INDEX_ATTRIBUTES] = new HashMap<String, String[]>();

        //We create the attribute dn
        hash[LDAPNode.INDEX_DN].put(rdn, new String[]{""});

        //We create the objectclasses
        String[] objs = new String[vObjectClasses.size()];
        for (int i = 0; i < vObjectClasses.size(); i++) {
            objs[i] = vObjectClasses.elementAt(i);
        }
        hash[LDAPNode.INDEX_OBJECTCLASSES].put(Schema.OBJECTCLASS_KEY, objs);

        //We create the attributes
        for (int i = 0; i < vAttributes.size(); i++) {
            if(!vAttributes.elementAt(i).equals(rdn))//We put the attribute only if it is distinct of rdn attribute
                hash[LDAPNode.INDEX_ATTRIBUTES].put(vAttributes.elementAt(i), new String[]{""});
        }
        return hash;
    }

    /** Creates new form AddNewEntryStep2 */
    public AddNewEntryStep2() {
        super("Fill the attributes values");
        initComponents();
        this.editorFieldValue.closeButton.setVisible(false);
        this.editorFieldValue.refreshButton.setVisible(false);
        this.editorFieldValue.saveButton.setVisible(false);
    }

    @Override
    public boolean finish() {
        boolean exit = false;
        
        // <editor-fold defaultstate="collapsed" desc=" We set the required attributes to add a new entry with the editor ">
        String rdn = "";
        String parentDn = "";
        String rdnValue = "";
        //We get the rdn 
        if (this.parameters.containsKey(AddNewEntryStep1.RDN_KEY)) {
            rdn = (String) this.parameters.get(AddNewEntryStep1.RDN_KEY);
        }
        //We get the parent dn
        if (this.parameters.containsKey(AddNewEntryStep1.PARENT_DN_KEY)) {
            parentDn = (String) this.parameters.get(AddNewEntryStep1.PARENT_DN_KEY);
        }
        //We get the fisrt element od this array, this is the attribute DN. We get the value for the rdn attribute
        Object[] object = (Object[]) this.editorFieldValue.vTextField[LDAPNode.INDEX_DN].firstElement();
        JLabel label = (JLabel) object[0];
        JTextField textField = (JTextField) object[1];
        rdnValue = textField.getText();
        //We set the attributes to the hash used to add a new entry
        if (rdn.equals(label.getText())) {
            this.editorFieldValue.setAddingNewEntryValue(EditorPanel.ADD_NEW_ENTRY_RDN_KEY, rdn + "=" + rdnValue);
            this.editorFieldValue.setAddingNewEntryValue(EditorPanel.ADD_NEW_ENTRY_PARENT_DN_KEY, parentDn);
        }
        // </editor-fold>
        
        exit = this.editorFieldValue.saveChanges();
        logger.trace("We are going to finish the wizard?:" + exit);
        if (exit) {
            this.editorFieldValue.setEditableEditor(false);
        } else {
            this.editorFieldValue.setEditableEditor(true);
            this.put_params();
        }
        return exit;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        editorFieldValue = new org.symlabs.browser.editor.EditorFieldValue();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(editorFieldValue, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.symlabs.browser.editor.EditorFieldValue editorFieldValue;
    // End of variables declaration//GEN-END:variables
}
