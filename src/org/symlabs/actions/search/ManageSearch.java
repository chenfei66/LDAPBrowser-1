package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.search.ManageSearchWin;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchEntry </p>
 * <p>Descripcion: Action used to search an ldap entry.  </p>
 * <p>Copyright: Symlabs  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ManageSearch.java,v 1.6 2009-08-04 13:55:27 efernandez Exp $
 */
public class ManageSearch extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "manageSearches";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ManageSearch.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ManageSearch() {
        super("Manage Searches", java.awt.event.KeyEvent.VK_S,Utils.createImageIcon(Utils.ICON_SEARCH_MANAGE));
    }

    public void actionPerformed(ActionEvent e) {
        ManageSearchWin search = new ManageSearchWin(Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode());
        search.setLocationRelativeTo(Utils.getMainWindow());
        search.setVisible(true);        
    }
}
