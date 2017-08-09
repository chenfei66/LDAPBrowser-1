package org.symlabs.browser.connection;

import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.store.ConnectionData;
import org.symlabs.util.EvalParams;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Schema;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: EditConnectionPanel </p>
 * <p>Descripcion: Panel that shows the connection parameters and allos you to edit them. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ConnectionEditWin.java,v 1.16 2009-08-24 10:27:56 efernandez Exp $
 */
public class ConnectionEditWin extends javax.swing.JFrame {

    /**Attribute that contains the ldapserver connection, if no ldap server was used this attribute will be null*/
    private ConnectionData connectionData;
    /**Attribute that contains the window that calls this frame. If this values is null then it is called from tab properties*/
    private ConnectionOpenWin openWin;
    /**Attribute that contains the configuration name*/
    private String configurationName;
    /**Attribute that contains the vendor name of the ldap connection*/
    private String vendorName;
    /**Attribute that contains the vendor version of the ldap connection*/
    private String vendorVersion;
    /**Attribute which contains the host of the LDAP server*/
    private String host;
    /**Attribute which contains the port of the LDAP server*/
    private String port;
    /**Attribute which contains the user identifier of the LDAP server*/
    private String authid;
    /**Attribute which contains the user password of the LDAP server*/
    private String authpw;
    /**Attribute which contains the suffix given in connection window*/
    private String suffix;
    /**Attribute that contains the index of the ldap version selected in the current configuration*/
    private int indexLdapVersion;
    /**Attribute that contains the suffixes getted from the ldap connection */
    private String[] suffixes;
    /**Attribute that stores the bookmark key used to store a configuration*/
    private BookmarkFolderNode bookmarkRootNode;
    /**Attribute that stores the searches of this configuration*/
    private SearchFolderNode searchRootFolder;
    /**Attribute used to identify the editor viewing mode*/
    protected static final String VIEW_MODE = "Connection Params VIEW Mode";
    /**Attribute used to identify the editor editting mode*/
    protected static final String EDIT_MODE = "Connection Params EDIT Mode";
    /**Attribute used to store the editor mode used at this moment*/
    protected String editorMode;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ConnectionEditWin.class);

    // <editor-fold defaultstate="collapsed" desc=" Getter methods ">
    public String getConfigurationName() {
        return configurationName;
    }

    public String getAuthid() {
        return authid;
    }

    public String getAuthpw() {
        return authpw;
    }

    public String getHost() {
        return host;
    }

    public int getIndexLdapVersion() {
        return indexLdapVersion;
    }

    public String getPort() {
        return port;
    }

    public String getSuffix() {
        return suffix;
    }
    // </editor-fold>

    /**Constructor: Creates a new form ConnectionProperties Panel
     * This panel shows the information of the ldapServer connection parameters
     *
     * @param ldapServer LDAPServer
     */
    public ConnectionEditWin(LDAPServer ldapServer){
        this.initComponents();
        this.openWin=null;
        this.connectionData=ldapServer.getConnectionData();
        this.initProperties(ldapServer.getConnectionData().getConfigurationName(),
                ldapServer.getHost(),
                ldapServer.getPort()+"",
                ldapServer.getIndexLdapVersion(),
                ldapServer.getUserId(),
                ldapServer.getUserPw(),
                ldapServer.getSuffix(),
                ldapServer.getSuffixes(),
                ldapServer.getServerType().get(Schema.VENDOR_NAME_TYPE),
                ldapServer.getServerType().get(Schema.VENDOR_VERSION_TYPE),
                ldapServer.getBookMarkRootFolder(),
                ldapServer.getSearchRootFolder());
        this.setEditorMode(VIEW_MODE);
    }

    /**Contructor: Creates a new form ConnectionPropertiesPanel.
     * This panel shows the information of the ldapServer connection parameters
     * 
     * @param ConnectionData data
     */
    public ConnectionEditWin(ConnectionData data, ConnectionOpenWin openWin) {
        this.initComponents();
        this.connectionData=data;
        this.openWin=openWin;
        this.initProperties(connectionData.getConfigurationName(),
                connectionData.getHost(),
                connectionData.getPort() + "",
                connectionData.getIndexLdapVersion(),
                connectionData.getAuthid(),
                connectionData.getAuthpw(),
                connectionData.getSuffix(),
                connectionData.getSuffixes(),
                "",
                "",
                connectionData.getBookmarkRootFolder(),
                data.getSearchRootFolder());
        this.setEditorMode(VIEW_MODE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        connectionPanel = new org.symlabs.browser.connection.ConnectionPanel();
        vendorInfoPanel = new javax.swing.JPanel();
        vendorNameTextLabel = new javax.swing.JLabel();
        vendorVersionTextLabel = new javax.swing.JLabel();
        vendorNameValueLabel = new javax.swing.JLabel();
        vendorVersionValueLabel = new javax.swing.JLabel();
        confNamePanel = new javax.swing.JPanel();
        confNameLabel = new javax.swing.JLabel();
        confNameTextField = new javax.swing.JTextField();
        commandPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        testButton = new javax.swing.JButton();

        setTitle("Connection Properties");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        connectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection Parameters"));
        this.connectionPanel.setComponent(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(connectionPanel, gridBagConstraints);

        vendorInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Vendor Information"));
        vendorInfoPanel.setLayout(new java.awt.GridBagLayout());

        vendorNameTextLabel.setText("Vendor Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        vendorInfoPanel.add(vendorNameTextLabel, gridBagConstraints);

        vendorVersionTextLabel.setText("Vendor Version ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        vendorInfoPanel.add(vendorVersionTextLabel, gridBagConstraints);

        vendorNameValueLabel.setText("Value for the vendor name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        vendorInfoPanel.add(vendorNameValueLabel, gridBagConstraints);

        vendorVersionValueLabel.setText("Value for the vendor version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        vendorInfoPanel.add(vendorVersionValueLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(vendorInfoPanel, gridBagConstraints);

        confNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection Name"));
        confNamePanel.setLayout(new java.awt.GridBagLayout());

        confNameLabel.setText("Configuration Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        confNamePanel.add(confNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        confNamePanel.add(confNameTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(confNamePanel, gridBagConstraints);

        commandPanel.setLayout(new java.awt.GridBagLayout());

        okButton.setText("OK");
        okButton.setToolTipText("Save your changes");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(okButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(cancelButton, gridBagConstraints);

        testButton.setText("Test");
        testButton.setToolTipText("Test your conection");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        commandPanel.add(testButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 2, 4);
        getContentPane().add(commandPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        this.setEditorMode(EDIT_MODE);
    }//GEN-LAST:event_editButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        logger.trace("Mode:" + editorMode);
        if (this.editorMode.equals(EDIT_MODE)) {
            this.setEditorMode(VIEW_MODE);
            this.confNameTextField.setText(this.configurationName);
            this.connectionPanel.hostTextField.setText(this.host);
            this.connectionPanel.portTextField.setText(this.port);
            this.connectionPanel.versionComboBox.setSelectedIndex(this.indexLdapVersion);
            this.connectionPanel.userIdTextField.setText(this.authid);
            this.connectionPanel.userPwPasswordField.setText(this.authpw);
            this.connectionPanel.setSuffixesComboBoxModel(this.suffixes, this.suffix);
            this.vendorNameValueLabel.setText(this.vendorName);
            this.vendorVersionValueLabel.setText(this.vendorVersion);
        } else if (this.editorMode.equals(VIEW_MODE)) {
            this.dispose();
            Utils.getMainWindow().clearStatusBarMessage();
        }
}//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String errorMsg = "";
        String title = "Connection Parameters";
        this.setEditorMode(VIEW_MODE);
        this.configurationName = this.confNameTextField.getText();
        this.host = this.connectionPanel.hostTextField.getText();
        this.port = this.connectionPanel.portTextField.getText();
        this.indexLdapVersion = this.connectionPanel.versionComboBox.getSelectedIndex();
        this.authid = this.connectionPanel.userIdTextField.getText();
        char[] passw = this.connectionPanel.userPwPasswordField.getPassword();
        this.authpw = new String(passw);
        this.suffix = this.connectionPanel.getSuffixFromComboBox();
        errorMsg = this.storeConnectionParameters();

        if (errorMsg.equals("")) {

            //The configuration name is opened
            if (Utils.getMainWindow().getBrowserPanelByTitle(this.connectionData.getConfigurationName()) != null) {

                String msg = "Your configuration was saved correctly.\nDo you want to reconnect your current connection with this parameters?";
                title = "Reconnect your configuration";
                int answer = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
                if (answer == JOptionPane.YES_OPTION) {
                    errorMsg = EvalParams.evalConnectionParameters(this.host, this.port, this.authid, this.authpw.toCharArray(), this.suffix, this.indexLdapVersion);

                    if (errorMsg.equals("")) {
                        //We close the current connection
                        Utils.getMainWindow().closeCurrentTabWithoutAsking();

                        //We create the new connection
                        errorMsg = Utils.getMainWindow().addNewTab(
                                this.host,
                                this.port,
                                this.authid,
                                this.authpw.toCharArray(),
                                this.suffix,
                                this.indexLdapVersion,
                                this.bookmarkRootNode,
                                this.searchRootFolder,
                                this.configurationName,
                                Utils.ICON_CONNECT_OPEN);

                        if (!errorMsg.equals("")) {
                            errorMsg += "Error reconnecting with the new connection parameters." + "\n";
                            title = "Create connection";
                            logger.error(errorMsg);
                        }
                    } else {
                        errorMsg += "Error testing the connection parameters. Could not connect to the ldap server." + "\n";
                        title = "Data error";
                        logger.error(errorMsg);
                    }
                }
            } else { //the configuration is not opened
                String message = "Your configuration was saved correctly.";
                MessageDialog dialog = new MessageDialog(this, title, message, MessageDialog.MESSAGE_INFORMATION);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
                logger.trace(message);
            }
            if(this.openWin!=null){
                this.openWin.refreshConnectionList();
            }
        } else if (errorMsg.equals(ConnectionData.OPERATION_CANCELLED)) {
            title = "Operation cancelled";
            errorMsg += "Your configuration was not saved." + "\n";
            logger.info(errorMsg);
        } else {
            title = "Data error";
            errorMsg += "Error saving your connection parameters." + "\n";
            logger.error(errorMsg);
        }

        if (!errorMsg.equals("")) {
            logger.error(errorMsg);
            MessageDialog errorDialog = new MessageDialog(this, title, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        } else {
            //If no error was found then we close this window
            this.dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    /**Method that stores the connection parameters in the saved connections file. 
     * If the confName already exists it is sets the new conf name written by the user.
     * 
     */
    private String storeConnectionParameters() {
        String errorMsg = "";
        try {
            int myPort = Integer.parseInt(this.port);
            String newConfName = ConnectionData.saveAsConnectionData(
                    this.configurationName,
                    this.host,
                    myPort,
                    this.authid,
                    this.authpw,
                    this.suffix,
                    this.suffixes,
                    this.indexLdapVersion,
                    this.bookmarkRootNode,
                    this.searchRootFolder,
                    this);
            if (newConfName.equals(ConnectionData.OPERATION_CANCELLED)) {
                errorMsg = ConnectionData.OPERATION_CANCELLED;
            } else if (!newConfName.equals(this.configurationName)) {
                //We update the configuration Name
                this.configurationName = newConfName;
            }
        } catch (Exception ex) {
            errorMsg += "Error storing connection parameters" + "\n" + ex + "\n";
            logger.error(errorMsg);
        }
        return errorMsg;
    }

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        String errorMessage = "";
        LDAPServer con = null;
        try {
            con = new LDAPServer(new ConnectionData(connectionPanel.hostTextField.getText(), Integer.parseInt(connectionPanel.portTextField.getText()),
                    connectionPanel.userIdTextField.getText(), new String(connectionPanel.userPwPasswordField.getPassword()),
                    connectionPanel.versionComboBox.getSelectedIndex()), true);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            logger.error(errorMessage);
        }

        //If there are not any error found, we continue checking the fields
        if (errorMessage.equals("")) {
            String newSuffix = this.connectionPanel.getSuffixFromComboBox();
            try {
                con.TestConnection(newSuffix);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                logger.error(errorMessage);
            }
        }
        String title = "";
        String message = "";
        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            title = "Connection error";
            message = "Error testing the connection parameters.";
            MessageDialog errorDialog = new MessageDialog(this, title, message, errorMessage, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        } else {
            title = "Information message";
            message = "Test successfull!";
            logger.trace(errorMessage);
            MessageDialog errorDialog = new MessageDialog(this, title, message, MessageDialog.MESSAGE_INFORMATION);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        }
    }//GEN-LAST:event_testButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JLabel confNameLabel;
    private javax.swing.JPanel confNamePanel;
    private javax.swing.JTextField confNameTextField;
    private org.symlabs.browser.connection.ConnectionPanel connectionPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton testButton;
    private javax.swing.JPanel vendorInfoPanel;
    private javax.swing.JLabel vendorNameTextLabel;
    private javax.swing.JLabel vendorNameValueLabel;
    private javax.swing.JLabel vendorVersionTextLabel;
    private javax.swing.JLabel vendorVersionValueLabel;
    // End of variables declaration//GEN-END:variables
    /**Method that initializes the attributes given as argument. And sets its values in their components
     * 
     * @param confName String. This is the configuration name. This is the stored name of the configuration
     * @param host String.
     * @param port Stirng.
     * @param indexLdapVersion int. This is the index of the item of the combobox
     * @param authid String. This is the user identifier
     * @param authpw String, This is the user password
     * @param suffix Stirng. This is the selected suffix from the combobox
     * @param suffixes String, This is the suffixes contained int the combobox
     * @param vendorName String.
     * @param vendorVersion  Stirng.
     */
    private void initProperties(String confName, String host, String port, int indexLdapVersion,
            String authid, String authpw, String suffix, String[] suffixes, String vendorName,
            String vendorVersion, BookmarkFolderNode bookMark, SearchFolderNode search) {
        this.configurationName = confName;
        this.host = host;
        this.port = port;
        this.indexLdapVersion = indexLdapVersion;
        this.authid = authid;
        this.authpw = authpw;
        this.suffix = suffix;
        this.suffixes = suffixes;
        this.vendorName = vendorName;
        this.vendorVersion = vendorVersion;
        this.bookmarkRootNode = bookMark;
        this.searchRootFolder = search;

        this.confNameTextField.setText(this.configurationName);
        this.connectionPanel.hostTextField.setText(this.host);
        this.connectionPanel.portTextField.setText(this.port);
        this.connectionPanel.versionComboBox.setSelectedIndex(this.indexLdapVersion);
        this.connectionPanel.userIdTextField.setText(this.authid);
        this.connectionPanel.userPwPasswordField.setText(this.authpw);
        this.connectionPanel.setSuffixesComboBoxModel(this.suffixes, this.suffix);
        this.vendorNameValueLabel.setText(this.vendorName);
        this.vendorVersionValueLabel.setText(this.vendorVersion);
    }

    /**Method that sets the components of this panel as editable
     * 
     * @param editable boolean. True - It means editable mode. False - It means no editable mode
     */
    private void setEditablePanelComponents(boolean editable) {
        this.confNameTextField.setEditable(editable);
        this.connectionPanel.hostTextField.setEditable(editable);
        this.connectionPanel.portTextField.setEditable(editable);
        this.connectionPanel.versionComboBox.setEnabled(editable);
        this.connectionPanel.userIdTextField.setEditable(editable);
        this.connectionPanel.userPwPasswordField.setEditable(editable);
        this.connectionPanel.suffixesComboBox.setEnabled(editable);
        this.connectionPanel.showPasswordButton.setEnabled(editable);
        this.connectionPanel.suffixesButton.setEnabled(editable);
    }

    /**Method that sets the mode of this editor connection properties
     * 
     * @param mode String. This mode will be VIEW_MODE or EDIT_MODE
     */
    public void setEditorMode(String mode) {
        if (mode.equalsIgnoreCase(VIEW_MODE)) {
            this.setEditablePanelComponents(false);
            this.editorMode = VIEW_MODE;
            this.editButton.setEnabled(true);
            this.testButton.setEnabled(false);
            this.okButton.setEnabled(false);
            this.cancelButton.setToolTipText("Close this window");
            this.cancelButton.setText("Close");
        } else if (mode.equalsIgnoreCase(EDIT_MODE)) {
            this.setEditablePanelComponents(true);
            this.editorMode = EDIT_MODE;
            this.editButton.setEnabled(false);
            this.testButton.setEnabled(true);
            this.okButton.setEnabled(true);
            this.cancelButton.setToolTipText("Restore the previous values");
            this.cancelButton.setText("Cancel");
        }
    }
}
