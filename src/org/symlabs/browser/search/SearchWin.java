package org.symlabs.browser.search;

import org.symlabs.actions.search.LoadSearch;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.search.SearchNode;
import org.symlabs.search.SearchParams;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchWin </p>
 * <p>Descripcion: Window that manage a search. This window allows you to make a search without storing it. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchWin.java,v 1.15 2009-08-24 09:01:06 efernandez Exp $
 */
public class SearchWin extends javax.swing.JFrame {

    /** Creates new form SearchWin
     * @param ldapNode LDAPNode
     */
    public SearchWin(LDAPNode ldapNode) {
        initComponents();
        this.searchPanel.setLdapNode(ldapNode);
        this.searchPanel.initProperties(ldapNode);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        searchPanel = new org.symlabs.browser.search.SearchPanel();
        commangPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Search");
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(searchPanel, gridBagConstraints);

        commangPanel.setLayout(new java.awt.GridBagLayout());

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commangPanel.add(searchButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commangPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(commangPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        String errorDetails = this.searchPanel.getErrorCheckingSearchFields();
        if (errorDetails.equals("")) {//No error was found
            this.dispose();
            SearchParams params= this.searchPanel.getSearchParamsFromPanel();
            BrowserPanel browserPanel= Utils.getMainWindow().getCurrentBrowserPanel();
            String defaultName= browserPanel.getSearchResultsPanel().getDefaultSearchName();
            SearchNode searchNode = new SearchNode(defaultName,"",params);
            (new LoadSearch(browserPanel.getTreeRootNode(),searchNode,false)).actionPerformed(evt);
        } else {
            String message = "Error cheching search fields.";
            String title = "Error in Search";
            MessageDialog dialog = new MessageDialog(this,title, message, errorDetails, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commangPanel;
    private javax.swing.JButton searchButton;
    private org.symlabs.browser.search.SearchPanel searchPanel;
    // End of variables declaration//GEN-END:variables
}
