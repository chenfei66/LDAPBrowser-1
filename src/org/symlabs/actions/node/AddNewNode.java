package org.symlabs.actions.node;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.browser.addentry.AddNewEntryDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo:AddNewNode </p>
 * <p>Descripcion: Action that adds a new ldap entry to the ldap server. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AddNewNode.java,v 1.2 2009-08-04 13:55:27 efernandez Exp $
 */
public class AddNewNode extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "addNewEntry";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddNewNode.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public AddNewNode() {
        super("New Entry", java.awt.event.KeyEvent.VK_N,Utils.createImageIcon(Utils.ICON_ENTRY_ADD_NEW));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        AddNewEntryDialog dialog = new AddNewEntryDialog(Utils.getMainWindow(),true,currentNode);
        dialog.setLocationRelativeTo(Utils.getMainWindow());
        dialog.setVisible(true);
        
    }
}
