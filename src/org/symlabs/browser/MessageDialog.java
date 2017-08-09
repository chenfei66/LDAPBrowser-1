package org.symlabs.browser;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ShowErrorMessageDialog </p>
 * <p>Descripcion: Dialog that shows an error message, and it also show the details error message. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: MessageDialog.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class MessageDialog extends javax.swing.JDialog {

    /**Attribute that contains the error message displayed in the dialog*/
    private String errorMessage;
    /**Attribute that contains the details of the error message*/
    private String errorDetails;
    /**Attribute that tells us if the error details message is shown or it is not*/
    private boolean showDetails;
    /**Attribute that contains the width size of this dialog*/
    private final int windowWidth = 550;
    /**Attribute that contains the height size of this dialog when details are shown*/
    private final int windowHeightWithDetails = 340;
    /**Attribute that contains the height size of this dialog when details are not shown*/
    private final int windowHeightWitoutDetails = 140;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(MessageDialog.class);
    /**Attribute that tells us the maximum number of characters shown in a line*/
    private final int maxNumberCharacters = 70;
    /**Attribute used to indentify an error message*/
    public static final int MESSAGE_ERROR = 2;
    /**Attribute used to identify a warning message*/
    public static final int MESSAGE_WARNING = 1;
    /**Attribute used to identify an information message*/
    public static final int MESSAGE_INFORMATION = 0;
    /**Attribute used to shows this frame when this dialog was closed*/
    private Component mainFrame;

    /** Creates new form ErrorDialog
     * @param mainFrame JFrame. This is the fram shown when this dialog is closed
     * @param title String. This is the title of the dialog
     * @param errorMsg String. This is the error message shown in the label
     * @param errorDetails String. This is the error details messages shown in the textarea
     * @param messageType int. This is the type of message displayed: ERROR, WARNING, INFORMATION.
     */
    public MessageDialog(Component mainFrame, String title, String errorMsg, String errorDetails, int messageType) {
        super(Utils.getMainWindow(), true);
        initComponents();
        this.setTitle(title);
        this.errorDetails = errorDetails;
        this.errorMessage = errorMsg;
        this.showDetails = false;
        this.messageTextArea.setText(errorMsg);
        this.setDetailsText(errorDetails, this.maxNumberCharacters);
        this.setDetailsVisible(showDetails);
        this.setMessageType(messageType);
        if(mainFrame == null){
            this.mainFrame = Utils.getMainWindow();
        }else{
            this.mainFrame = mainFrame;
        }
    }

    /** Creates new form ErrorDialog
     * @param mainFrame JFrame. This is the fram shown when this dialog is closed
     * @param title String. This is the title of the dialog
     * @param errorMsg String. This is the error message shown in the label
     * @param messageType int. This is the type of message displayed: ERROR, WARNING, INFORMATION.
     */
    public MessageDialog(Component mainFrame, String title, String errorMsg, int messageType) {
        this(mainFrame, title, errorMsg, "", messageType);
        this.detailsButton.setEnabled(false);
    }

    /**Method that sets the text of the details textarea. This text will be formatted to do not allow horizontal scroll
     * 
     * @param text String. This is the text to be setted
     * @param maxChars int. This is the maximum number of characters in a line
     */
    private void setDetailsText(String text, int maxChars) {
        int total = text.length();
        int init = 0;
        int end = maxChars;
        if (end > text.length()) {
            end = text.length();
        }
        String line = "";
        this.detailsTextArea.setText("");
        while (init < total) {
            //this is the original value of the line
            line = text.substring(init, end);
            if (end >= text.length() || text.charAt(end) == ' ') {
            //if it is an empty space or it is the las character of the text
            } else { //in otherwise we search for the last white space
                //We get the last white space position
                end = init + this.getLastWhiteSpacePosition(line);
                //We update the line value
                line = text.substring(init, end);
            }
            this.detailsTextArea.append(line + "\n");
            //We update the indixes
            init = end;
            end += maxChars;
            if (end > text.length()) {
                end = text.length();
            }
        }//end while
    }

    /**Method that returns the position of the last white space found in the line given as argument. 
     * If no white space was found, then it will be returned the position of the last character.
     * 
     * @param line String. This is the line where we want to get the last white space. 
     * @return int.This is the position of the last white space
     */
    private int getLastWhiteSpacePosition(String line) {
        int position = line.length() - 1;//This is the default position if no empty space was found
        for (int i = line.length() - 1; i > 0; i--) {
            if (line.charAt(i) == ' ') {
                position = i;
                break;
            }
        }
        return position;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        messageTextArea = new javax.swing.JTextArea();
        iconLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        detailsButton = new javax.swing.JButton();
        detailsjScrollPane = new javax.swing.JScrollPane();
        detailsTextArea = new javax.swing.JTextArea();
        emptyPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        messageTextArea.setColumns(20);
        messageTextArea.setEditable(false);
        messageTextArea.setRows(2);
        messageTextArea.setText("This is the message");
        messageTextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        messageTextArea.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        getContentPane().add(messageTextArea, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(iconLabel, gridBagConstraints);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        getContentPane().add(okButton, gridBagConstraints);

        detailsButton.setText("Show details");
        detailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        getContentPane().add(detailsButton, gridBagConstraints);

        detailsjScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Error Details"));
        detailsjScrollPane.setMinimumSize(new java.awt.Dimension(250, 200));
        detailsjScrollPane.setPreferredSize(new java.awt.Dimension(250, 200));

        detailsTextArea.setColumns(20);
        detailsTextArea.setRows(5);
        detailsjScrollPane.setViewportView(detailsTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(detailsjScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(emptyPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void detailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailsButtonActionPerformed
        if (this.detailsButton.isEnabled()) {
            this.showDetails = !this.showDetails;
            this.setDetailsVisible(showDetails);
        }
    }//GEN-LAST:event_detailsButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.dispose();
        this.mainFrame.setVisible(true);
    }//GEN-LAST:event_okButtonActionPerformed

    /**Method that sets the error details to visible true or false
     * 
     * @param visible boolean.
     */
    private void setDetailsVisible(boolean visible) {
        this.detailsTextArea.setVisible(visible);
        this.detailsjScrollPane.setVisible(visible);
        if (visible) {
            this.detailsButton.setText("Hide details");
            this.setSize(this.windowWidth, this.windowHeightWithDetails);
        } else {
            this.detailsButton.setText("Show details");
            this.setSize(this.windowWidth, this.windowHeightWitoutDetails);
        }
        this.invalidate();
        this.validate();
        this.repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton detailsButton;
    private javax.swing.JTextArea detailsTextArea;
    private javax.swing.JScrollPane detailsjScrollPane;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    /**Method that sets the icon image to the message shown.
     * 
     * @param messageType int. This is the type of message: ERROR, WARNING, INFORMATION
     */
    private void setMessageType(int messageType) {
        ImageIcon icon = null;
        if (messageType == MessageDialog.MESSAGE_ERROR) {
            icon = Utils.createImageIcon(Utils.MESSAGE_IMAGE_ERROR);
        } else if (messageType == MessageDialog.MESSAGE_WARNING) {
            icon = Utils.createImageIcon(Utils.MESSAGE_IMAGE_WARNING);
        } else if (messageType == MessageDialog.MESSAGE_INFORMATION) {
            icon = Utils.createImageIcon(Utils.MESSAGE_IMAGE_INFORMATION);
        }
        if (icon != null) {
            this.iconLabel.setIcon(icon);
        } else {
            this.iconLabel.setVisible(false);
        }
    }
}
