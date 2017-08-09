package org.symlabs.browser.addentry;

import java.util.Vector;
import javax.swing.DefaultListModel;
import org.symlabs.ui.SearchList;

/**
 * <p>Titulo: ChooseObjectClassDialog </p>
 * <p>Descripcion: Class used to choose one of the objectclasses displayed  </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ChooseObjectClassDialog.java,v 1.4 2009-07-24 06:42:08 efernandez Exp $
 */
public class ChooseObjectClassDialog extends javax.swing.JDialog {

    /**Attribute that contians all the objectClasses displayed in the list*/
    private Vector<String> allObjectClasses;

    /** Creates new form chooseObjectClassDialog
     * @param parent
     * @param modal
     * @param allObjectClasses Vector <String>. It contains the all objectclasses to load in the list
     */
    public ChooseObjectClassDialog(java.awt.Frame parent, boolean modal, Vector allObjectClasses) {
        super(parent, modal);
        initComponents();
        this.allObjectClasses = allObjectClasses;
        this.initList();
    }

    /**Method that initializes the list values
     * 
     */
    private void initList() {
        DefaultListModel model = new DefaultListModel();
        if (this.allObjectClasses != null && this.allObjectClasses.size() > 0) {
            for (int i = 0; i < this.allObjectClasses.size(); i++) {
                model.addElement(this.allObjectClasses.elementAt(i));
            }
        }
        this.objectclassList.setModel(model);
    }

    /**Method that returns the objectclass selected in this panel
     * 
     * @return String. It is returned the objectclass selected.
     */
    public String getSeletectObjectClass(){
        return this.objectclassTextField.getText();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        chooserPanel = new javax.swing.JPanel();
        objectclassTextField = new javax.swing.JTextField();
        selectButton = new javax.swing.JButton();
        objectclassScrollPane = new javax.swing.JScrollPane();
        objectclassList = new SearchList();
        commandPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        finishButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select an ObjectClass");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Please select one of the following ObjectClasses");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(jLabel1, gridBagConstraints);

        chooserPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        chooserPanel.add(objectclassTextField, gridBagConstraints);

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        chooserPanel.add(selectButton, gridBagConstraints);

        objectclassList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        objectclassList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        objectclassScrollPane.setViewportView(objectclassList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        chooserPanel.add(objectclassScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(chooserPanel, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        commandPanel.add(jPanel1, gridBagConstraints);

        finishButton.setText("Close");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(commandPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if(this.objectclassList.getSelectedValue()!=null){
            this.objectclassTextField.setText((String) this.objectclassList.getSelectedValue());
        }
    }//GEN-LAST:event_selectButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
        this.objectclassTextField.setText("");
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_finishButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel chooserPanel;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JButton finishButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JList objectclassList;
    private javax.swing.JScrollPane objectclassScrollPane;
    private javax.swing.JTextField objectclassTextField;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
}
