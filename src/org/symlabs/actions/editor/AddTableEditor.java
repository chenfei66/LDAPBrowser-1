package org.symlabs.actions.editor;

import org.symlabs.actions.*;
import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.editor.EditorPanel;
import org.symlabs.browser.editor.EditorTable;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.schema.SchemaNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.VirtualNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: AddTableEditor </p>
 * <p>Descripcion: Action that creates a new instance of tableEditor. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddTableEditor.java,v 1.9 2009-08-24 09:01:06 efernandez Exp $
 */
public class AddTableEditor extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "tableEditor";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddTableEditor.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public AddTableEditor() {
        super("Add Table Editor", java.awt.event.KeyEvent.VK_T);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            LDAPNode currentNode =Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
            if (currentNode != null &&
                    !(currentNode instanceof VirtualNode) &&
                    !(currentNode instanceof TreeRootNode)) {
                //if this panel it is not displayed we create it
                if (!Utils.getMainWindow().getCurrentBrowserPanel().isTableEditor()) {
                    EditorPanel editor = new EditorTable();
                    TreeRootNode rootNode = (TreeRootNode) currentNode.getRoot();
                    if(currentNode instanceof SchemaNode || rootNode instanceof TreeRootDseRootNode || rootNode instanceof TreeSchemaRootNode){
                        editor.saveButton.setVisible(false);
                        editor.cancelButton.setVisible(false);
                        editor.editButton.setVisible(false);
                        editor.refreshButton.setVisible(false);
                        editor.closeButton.setVisible(false);
                    }
                    //We add this panel to the tab panel in BrowserMainWindow
                    Utils.getMainWindow().getCurrentBrowserPanel().getRightTabbedPane().addTab(EditorTable.DEFAULT_EDITOR_TITLE, editor);
                    Utils.getMainWindow().getCurrentBrowserPanel().setTableEditor(true);
                } else {
                    String title = "Table Editor";
                    String msg = "This editor can not be displayed, it is already being shown";
                    MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, msg, MessageDialog.MESSAGE_ERROR);
                    errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                    errorDialog.setVisible(true);
                }
            }
        } catch (Exception ex) {
            String msg = "Error adding table editor: " + ex;
            logger.error(msg);
        }
    }
}
