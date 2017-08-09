package org.symlabs.actions.ldif;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ExportSubTree </p>
 * <p>Descripcion: Action that exports to an ldif file the sub tree selected. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ExportTree.java,v 1.4 2009-08-24 09:01:06 efernandez Exp $
 */
public class ExportTree extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "exportTree";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ExportSubTree.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ExportTree() {
        super("Export Full Tree", java.awt.event.KeyEvent.VK_T, Utils.createImageIcon(Utils.ICON_EXPORT_LDIF_TREE));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            TreeRootNode rootNode = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode();//(TreeRootNode) Utils.getCurrentNode().getRoot();
            
            Utils.createExportLdifFile(rootNode);
            
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
