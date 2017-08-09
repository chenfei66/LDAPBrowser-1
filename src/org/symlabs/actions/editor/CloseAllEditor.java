package org.symlabs.actions.editor;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloseAllEditor </p>
 * <p>Descripcion: Action that closes all editors. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CloseAllEditor.java,v 1.3 2009-08-04 11:06:35 efernandez Exp $
 */
public class CloseAllEditor extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "closeAllEditor";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CloseAllEditor.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public CloseAllEditor() {
        super("Close All Editor", java.awt.event.KeyEvent.VK_A);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            //This is the number of editors displayed
            int numTabs = Utils.getMainWindow().getCurrentBrowserPanel().getRightTabbedPane().getTabCount();

            //If there are any editor 
            while (numTabs > 0) {
                //Utils.closeEditor(e);
                Utils.getAction(CloseEditor.HashKey).getAction().actionPerformed(e);

                //We update the number of editors displayed
                numTabs = Utils.getMainWindow().getCurrentBrowserPanel().getRightTabbedPane().getTabCount();
            }
        } catch (Exception ex) {
            String msg = "Error closing all editors: " + ex;
            logger.error(msg);
        }
    }
}
