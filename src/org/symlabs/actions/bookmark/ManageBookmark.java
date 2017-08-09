package org.symlabs.actions.bookmark;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.bookmark.ManageBookmarkWin;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ManageBookMark </p>
 * <p>Descripcion: Action that shows the window to manage the bookmarks </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ManageBookmark.java,v 1.8 2009-08-04 13:55:27 efernandez Exp $
 */
public class ManageBookmark extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "bookmarkTab";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ManageBookmark.class);

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     */
    public ManageBookmark() {
        super("Bookmark", java.awt.event.KeyEvent.VK_B,Utils.createImageIcon(Utils.ICON_BOOKMARK_MANAGE));
    }

    public void actionPerformed(ActionEvent e) {
        ManageBookmarkWin bookmarkWin = new ManageBookmarkWin(Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode());
        bookmarkWin.setLocationRelativeTo(Utils.getMainWindow());
        bookmarkWin.setVisible(true);
    }
}
