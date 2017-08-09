package org.symlabs.actions.tab;

import org.symlabs.actions.*;
import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SaveTab </p>
 * <p>Descripcion: Action that saves the current tab displayed in the browser main window. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SaveTab.java,v 1.7 2009-08-04 13:55:27 efernandez Exp $
 */
public class SaveTab extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "saveTab";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SaveAsTab.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public SaveTab() {
        super("Save", java.awt.event.KeyEvent.VK_S,Utils.createImageIcon(Utils.ICON_SAVE));
    }

    public void actionPerformed(ActionEvent e) {
        String errorMessage = "";

        LDAPServer ldapServer = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer();
        Utils.saveTab(ldapServer);
    }
}


