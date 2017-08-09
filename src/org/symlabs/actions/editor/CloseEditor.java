package org.symlabs.actions.editor;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.browser.editor.EditorPanel;
import org.symlabs.browser.editor.EditorFieldValue;
import org.symlabs.browser.editor.EditorForm;
import org.symlabs.browser.editor.EditorLdif;
import org.symlabs.browser.editor.EditorTable;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloseEditor </p>
 * <p>Descripcion: Action that close the current editor. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloseEditor.java,v 1.5 2009-08-07 07:34:48 efernandez Exp $
 */
public class CloseEditor extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "closeEditor";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloseEditor.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public CloseEditor() {
        super("Close Editor", java.awt.event.KeyEvent.VK_E);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            EditorPanel editor = Utils.getMainWindow().getCurrentBrowserPanel().getCurrentEditor();
            Utils.getMainWindow().getCurrentBrowserPanel().getRightTabbedPane().remove(editor);
            if (editor.getEditorType().equalsIgnoreCase(EditorFieldValue.DEFAULT_EDITOR_TITLE)) {
                Utils.getMainWindow().getCurrentBrowserPanel().setFieldValueEditor(false);
            } else if (editor.getEditorType().equalsIgnoreCase(EditorTable.DEFAULT_EDITOR_TITLE)) {
                Utils.getMainWindow().getCurrentBrowserPanel().setTableEditor(false);
            } else if (editor.getEditorType().equalsIgnoreCase(EditorForm.DEFAULT_EDITOR_TITLE)) {
                Utils.getMainWindow().getCurrentBrowserPanel().setFormEditor(false);
            } else if (editor.getEditorType().equalsIgnoreCase(EditorLdif.DEFAULT_EDITOR_TITLE)) {
                Utils.getMainWindow().getCurrentBrowserPanel().setLdifEditor(false);
            }
        } catch (Exception ex) {
            String msg = "Error closing editor: " + ex;
            logger.error(msg);
        }
    }
}
