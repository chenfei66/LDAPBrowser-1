package org.symlabs.actions.editor;

import org.symlabs.actions.*;
import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.editor.EditorPanel;
import org.symlabs.browser.editor.EditorLdif;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.schema.SchemaNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.VirtualNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddLdifEditor </p>
 * <p>Descripcion: Action that creates a new instance of LdifEditor. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddLdifEditor.java,v 1.10 2009-08-24 09:01:06 efernandez Exp $
 */
public class AddLdifEditor extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "ldifEditor";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddLdifEditor.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public AddLdifEditor() {
        super("Add Ldif Editor", java.awt.event.KeyEvent.VK_I);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPNode currentNode =Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            TreeRootNode rootNode = (TreeRootNode) currentNode.getRoot();
            if (currentNode != null && 
                    !(currentNode instanceof VirtualNode) &&
                    !(currentNode instanceof TreeRootNode) &&
                    !(currentNode instanceof SchemaNode) &&
//                    !(rootNode instanceof TreeSchemaRootNode) &&
                    !(rootNode instanceof TreeRootDseRootNode)) {
                //if this panel it is not displayed we create it
                if (!Utils.getMainWindow().getCurrentBrowserPanel().isLdifEditor()) {
                    EditorPanel editor = new EditorLdif();
                    //We add this panel to the tab panel in BrowserMainWindow
                    Utils.getMainWindow().getCurrentBrowserPanel().getRightTabbedPane().addTab(EditorLdif.DEFAULT_EDITOR_TITLE, editor);
                    Utils.getMainWindow().getCurrentBrowserPanel().setLdifEditor(true);
                } else {
                    String title = "Ldif Editor";
                    String msg = "This editor can not be displayed, it is already being shown";
                    MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, MessageDialog.MESSAGE_ERROR);
                    errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                    errorDialog.setVisible(true);
                }
            }
        } catch (Exception ex) {
            String msg = "Error adding ldif editor: " + ex;
            logger.error(msg);
        }
    }
}
