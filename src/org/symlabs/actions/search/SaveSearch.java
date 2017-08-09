package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.browser.search.ManageSearchWin;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SaveSearch </p>
 * <p>Descripcion: Action used to store a search that has not been stored before </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SaveSearch.java,v 1.4 2009-08-24 09:01:06 efernandez Exp $
 */
public class SaveSearch extends DsAction{

        /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "saveSearch";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(AddSearch.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public SaveSearch() {
        super("Save Search", java.awt.event.KeyEvent.VK_R, Utils.createImageIcon(Utils.ICON_SEARCH_SAVE));
    }
    
    public void actionPerformed(ActionEvent arg0) {
        BrowserPanel browser= Utils.getMainWindow().getCurrentBrowserPanel();
        TreeRootNode treeRootNode = browser.getTreeRootNode();
        if(treeRootNode instanceof TreeSearchRootNode){
            TreeSearchRootNode searchRootNode = (TreeSearchRootNode) treeRootNode;
            ManageSearchWin search = 
                    new ManageSearchWin(Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode(),
                                                         searchRootNode.getSearchNode().getSearchParams());
        search.setLocationRelativeTo(Utils.getMainWindow());
        search.setVisible(true);        
        }else{
            String message = "Search was not found.";
            String title = "Error in Search";
            String details="Please select a search tree";
            MessageDialog dialog = new MessageDialog(Utils.getMainWindow(),title, message, details, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(Utils.getMainWindow());
            dialog.setVisible(true);
        }
    }

}
