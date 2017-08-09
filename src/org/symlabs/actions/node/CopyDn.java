package org.symlabs.actions.node;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.clipboard.TextTransfer;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: CloneDn </p>
 * <p>Descripcion: Action that copy the dn of the selected node. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: CopyDn.java,v 1.5 2009-09-29 09:45:21 efernandez Exp $
 */
public class CopyDn extends DsAction{

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "copyDn";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(CopyDn.class);

    public CopyDn() {
        super("Copy dn", java.awt.event.KeyEvent.VK_Y,Utils.createImageIcon(Utils.ICON_ENTRY_COPY_DN));
    }
    
    public void actionPerformed(ActionEvent arg0) {
        LDAPNode currentNode = Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode();
        if(currentNode.type.equals(LDAPNode.TYPE_LDAP_NODE)){
            TextTransfer text = new TextTransfer(currentNode.myDN);
            logger.trace("Copied to the clipboard the dn:"+currentNode.myDN);
        }
    }

}
