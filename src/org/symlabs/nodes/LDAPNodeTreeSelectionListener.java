
package org.symlabs.nodes;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: LDAPNodeTreeSelectionListener </p>
 * <p>Descripcion: Class which manages listener used to select a node </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPNodeTreeSelectionListener.java,v 1.5 2009-08-07 07:34:48 efernandez Exp $
 */
public class LDAPNodeTreeSelectionListener implements TreeSelectionListener{

    public void valueChanged(TreeSelectionEvent evt) {
        if(evt.getNewLeadSelectionPath()!=null){
            if (!Utils.isEditableMode()) {

                //We close all editors
                Utils.getMainWindow().getCurrentBrowserPanel().closeAllEditor();

                //We open all editors
                Utils.getMainWindow().getCurrentBrowserPanel().openAllEditor();

                //We set the current the default editor as selected
                Utils.getMainWindow().getCurrentBrowserPanel().setCurrentEditor(Utils.getMainWindow().getDefaultTypeEditor());
            }
        }
    }

}
