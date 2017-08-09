package org.symlabs.actions.ldif;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ExportSubTree </p>
 * <p>Descripcion: Action that exports to an ldif file the sub tree selected. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ExportSubTree.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class ExportSubTree extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "exportSubTree";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ExportSubTree.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ExportSubTree() {
        super("Export Full SubTree", java.awt.event.KeyEvent.VK_S, Utils.createImageIcon(Utils.ICON_EXPORT_LDIF_SUBTREE));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            
            Utils.createExportLdifFile(currentNode);

        } catch (Exception ex) {
            String msg = "Error exporting subtree.";
            String msgDetails = msg + ex;
            String title = "Error Exporting SubTree";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, msgDetails, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(msgDetails);
        }
    }
}
