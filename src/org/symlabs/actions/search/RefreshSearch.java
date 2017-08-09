package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import javax.swing.tree.TreePath;
import netscape.ldap.LDAPException;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: RefreshSearch </p>
 * <p>Descripcion: Action that closes the search displayed.  </p>
 * <p>Copyright: Symlabs  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: RefreshSearch.java,v 1.3 2009-08-24 09:01:06 efernandez Exp $
 */
public class RefreshSearch  extends DsAction{
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(RefreshSearch.class);
    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "refreshSearch";
    
    
    public RefreshSearch (){
        super("Refresh Search", java.awt.event.KeyEvent.VK_S,Utils.createImageIcon(Utils.ICON_SEARCH_REFRESH));
    }

    public void actionPerformed(ActionEvent arg0) {
        BrowserPanel browserPanel = Utils.getMainWindow().getCurrentBrowserPanel();
        if(browserPanel.getTreeRootNode() instanceof TreeSearchRootNode){
            try {
                TreeSearchRootNode searchRootNode = (TreeSearchRootNode) browserPanel.getTreeRootNode();
                TreePath treepath = searchRootNode.getTree().getSelectionPath();
                
                searchRootNode.removeAllChildren();
                
                Utils.refreshTreeSearchRootNode(searchRootNode, (LDAPNode) treepath.getLastPathComponent());

                browserPanel.validate();
                browserPanel.repaint();
            } catch (LDAPException ex) {
                String details = ex.toString();
                String message = "Error refreshing the search.";
                String title = "Error in Search";
                MessageDialog dialog = new MessageDialog(Utils.getMainWindow(),title, message, details, MessageDialog.MESSAGE_ERROR);
                dialog.setLocationRelativeTo(Utils.getMainWindow());
                dialog.setVisible(true);
                logger.error(message + details);
            }
        }
    }

}
