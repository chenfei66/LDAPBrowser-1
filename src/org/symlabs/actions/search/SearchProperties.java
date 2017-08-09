package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.search.SearchPropertiesWin;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchProperties </p>
 * <p>Descripcion: Class that shows the Search properties window.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchProperties.java,v 1.1 2009-07-20 14:22:16 efernandez Exp $
 */
public class SearchProperties extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "searchProperties";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchProperties.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public SearchProperties() {
        super("Search Properties", java.awt.event.KeyEvent.VK_K, Utils.createImageIcon(Utils.ICON_SEARCH_PROPERTIES));
    }

    public void actionPerformed(ActionEvent e) {
        TreeRootNode treeRoot = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode();
        if(treeRoot instanceof TreeSearchRootNode){
            TreeSearchRootNode searchRootNode = (TreeSearchRootNode) treeRoot;
        SearchPropertiesWin window = new SearchPropertiesWin(searchRootNode.getSearchNode());
        window.setLocationRelativeTo(Utils.getMainWindow());
        window.setVisible(true);
        }
    }
}