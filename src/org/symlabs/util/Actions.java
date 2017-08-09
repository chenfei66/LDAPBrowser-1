package org.symlabs.util;

import java.util.ArrayList;
import org.symlabs.actions.bookmark.AddBookmark;
import org.symlabs.actions.bookmark.ManageBookmark;
import org.symlabs.actions.config.ShowPreferences;
import org.symlabs.actions.tab.OpenTab;
import org.symlabs.actions.editor.AddFieldValueEditor;
import org.symlabs.actions.editor.AddLdifEditor;
import org.symlabs.actions.editor.AddTableEditor;
import org.symlabs.actions.editor.CloseAllEditor;
import org.symlabs.actions.editor.CloseEditor;
import org.symlabs.actions.ldif.ExportSubTree;
import org.symlabs.actions.ldif.ExportTree;
import org.symlabs.actions.node.AddNewNode;
import org.symlabs.actions.node.CloneNode;
import org.symlabs.actions.node.CopyDn;
import org.symlabs.actions.node.DeleteNode;
import org.symlabs.actions.node.MoveDown;
import org.symlabs.actions.node.MoveUp;
import org.symlabs.actions.node.RefreshTree;
import org.symlabs.actions.node.RenameNode;
import org.symlabs.actions.rootdse.ShowRootDse;
import org.symlabs.actions.schema.ShowSchema;
import org.symlabs.actions.search.AddSearch;
import org.symlabs.actions.tab.CloseTreeRootNode;
import org.symlabs.actions.tab.CloseAllTabs;
import org.symlabs.actions.tab.CloseTab;
import org.symlabs.actions.tab.TabProperties;
import org.symlabs.actions.tab.NewTab;
import org.symlabs.actions.tab.SaveAllTabs;
import org.symlabs.actions.tab.SaveAsTab;
import org.symlabs.actions.tab.SaveTab;
import org.symlabs.actions.search.ManageSearch;
import org.symlabs.actions.search.NewSearch;
import org.symlabs.actions.search.RefreshSearch;
import org.symlabs.actions.search.SaveSearch;
import org.symlabs.actions.search.SearchProperties;
import org.symlabs.browser.BrowserMainWin;
import org.symlabs.nodes.LDAPBrowserNode;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.VirtualNode;

/**
 * <p>Titulo: Actions </p>
 * <p>Descripcion: Class which manages the actions used by the browser. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: Actions.java,v 1.23 2009-09-29 09:45:21 efernandez Exp $
 */
public class Actions {

    /**Method that sets the actions of the browser main window
     * 
     */
    public static void initBrowserMainWinActions() {
        BrowserMainWin window = Utils.getMainWindow();

        // <editor-fold defaultstate="collapsed" desc=" Nodes Actions ">
        //Actions in Nodes
        window.putAction(MoveUp.HashKey, new MoveUp());
        window.putAction(MoveDown.HashKey, new MoveDown());
        window.putAction(RefreshTree.HashKey, new RefreshTree());
        window.putAction(CloneNode.HashKey, new CloneNode());
        window.putAction(RenameNode.HashKey, new RenameNode());
        window.putAction(DeleteNode.HashKey, new DeleteNode());
        window.putAction(AddNewNode.HashKey, new AddNewNode());
        window.putAction(CopyDn.HashKey, new CopyDn());
//        window.putAction(ManageBookmark.HashKey, new ManageBookmark());
//        window.putAction(AddBookmark.HashKey, new AddBookmark());
        window.putAction(AddSearch.HashKey, new AddSearch());
        window.putAction(ManageSearch.HashKey, new ManageSearch());
        window.putAction(NewSearch.HashKey, new NewSearch());
        window.putAction(SaveSearch.HashKey, new SaveSearch());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Editor Actions ">
        //Editor actions
        window.putAction(AddFieldValueEditor.HashKey, new AddFieldValueEditor());
        window.putAction(AddTableEditor.HashKey, new AddTableEditor());
        window.putAction(CloseEditor.HashKey, new CloseEditor());
//        window.putAction(AddFormEditor.HashKey, new AddFormEditor());
        window.putAction(AddLdifEditor.HashKey, new AddLdifEditor());
        window.putAction(CloseAllEditor.HashKey, new CloseAllEditor());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Tabs Actions ">
        //Actions in Tabs
        window.putAction(CloseTab.HashKey, new CloseTab());
        window.putAction(CloseAllTabs.HashKey, new CloseAllTabs());
        window.putAction(SaveAsTab.HashKey, new SaveAsTab());
        window.putAction(SaveTab.HashKey, new SaveTab());
        window.putAction(SaveAllTabs.HashKey, new SaveAllTabs());
        window.putAction(OpenTab.HashKey, new OpenTab());
        window.putAction(NewTab.HashKey, new NewTab());
//        window.putAction(ManageBookmark.HashKey, new ManageBookmark());
        window.putAction(CloseTreeRootNode.HashKey, new CloseTreeRootNode());
        window.putAction(RefreshSearch.HashKey, new RefreshSearch());
        window.putAction(SearchProperties.HashKey, new SearchProperties());
        window.putAction(ExportSubTree.HashKey, new ExportSubTree());
        window.putAction(ExportTree.HashKey, new ExportTree());
        window.putAction(TabProperties.HashKey, new TabProperties());
        window.putAction(ShowSchema.HashKey, new ShowSchema());
        window.putAction(ShowRootDse.HashKey,new ShowRootDse());
        window.putAction(ShowPreferences.HashKey, new ShowPreferences());
    // </editor-fold>
    }

    /**Method that disable other the other actions do not supported by a Root node
     * 
     */
    private static void setDisabledActionsTreeRootNode() {
        Utils.getAction(CopyDn.HashKey).getAction().setEnabled(false);
        Utils.getAction(AddNewNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(RefreshTree.HashKey).getAction().setEnabled(false);
//        Utils.getAction(AddBookmark.HashKey).getAction().setEnabled(false);
        Utils.getAction(AddSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(NewSearch.HashKey).getAction().setEnabled(false);
    }

    /**Method that disable other the other actions do not supported by a Virtual node
     * 
     */
    private static void setDisabledActionsVirtualNode() {
        Utils.getAction(AddNewNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(RefreshTree.HashKey).getAction().setEnabled(false);
        Utils.getAction(ManageSearch.HashKey).getAction().setEnabled(false);
//        Utils.getAction(AddBookmark.HashKey).getAction().setEnabled(false);
        Utils.getAction(AddSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(NewSearch.HashKey).getAction().setEnabled(false);
    }

    /**Method that sets disabled the actions when the Main Window is started at the first time. Or when all tabs are closed.
     * 
     */
    public static void setEnabledActionsWithoutAnyTab() {
//        Actions.setEnabledActionsTab();
        Utils.getAction(CloseTab.HashKey).setEnabled(false);
        Utils.getAction(CloseAllTabs.HashKey).setEnabled(false);
        Utils.getAction(SaveTab.HashKey).setEnabled(false);
        Utils.getAction(SaveAsTab.HashKey).setEnabled(false);
        Utils.getAction(SaveAllTabs.HashKey).setEnabled(false);
        Utils.getAction(TabProperties.HashKey).setEnabled(false);
        
        Utils.getAction(AddNewNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(CloneNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(RenameNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(DeleteNode.HashKey).getAction().setEnabled(false);
        Utils.getAction(CopyDn.HashKey).getAction().setEnabled(false);
        Utils.getAction(RefreshTree.HashKey).getAction().setEnabled(false);
        Utils.getAction(ManageSearch.HashKey).getAction().setEnabled(false);
//        Utils.getAction(ManageBookmark.HashKey).getAction().setEnabled(false);
//        Utils.getAction(AddBookmark.HashKey).getAction().setEnabled(false);
        Utils.getAction(AddSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(NewSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(SaveSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(ManageSearch.HashKey).getAction().setEnabled(false);
        Utils.getAction(ExportSubTree.HashKey).getAction().setEnabled(false);
        Utils.getAction(ExportTree.HashKey).getAction().setEnabled(false);
        Utils.getAction(ShowSchema.HashKey).getAction().setEnabled(false);
        Utils.getAction(ShowRootDse.HashKey).getAction().setEnabled(false);
    }

    /**Method that sets enabled and disabled the actions used with connection tabs
     * 
     */
    public static void setEnabledActionsTab() {
        Utils.getAction(CloseTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(CloseAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        
        //Save Action need to test the value of the connection, and if it needs to be saved or it does not
        if(Utils.getMainWindow().getContainerTabbedPane().getTabCount()>0){
            boolean isDirty = ((TreeRootNode) Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode().getRoot()).getLdapServer().getConnectionData().isDirty();
            if(isDirty){
                Utils.getAction(SaveTab.HashKey).setEnabled(true);
            }else{
                Utils.getAction(SaveTab.HashKey).setEnabled(false);
            }
        }
        
        Utils.getAction(SaveAsTab.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(SaveAllTabs.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(TabProperties.HashKey).setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
//        Utils.getAction(ManageBookmark.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(ManageSearch.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(ExportSubTree.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(ExportTree.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(ShowSchema.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
        Utils.getAction(ShowRootDse.HashKey).getAction().setEnabled(Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0);
    }

    /**Method that sets enabled and disabled the actions for this node
     * 
     * @param node LDAPNode. This is the node to set its actions
     */
    public static void setEnabledActionsNode(LDAPBrowserNode node) {
        node.setEnabledActions();
        ArrayList<String> actionsToEnable = node.getEnabledActionKeys();
        ArrayList<String> actionsToDisable = node.getDisabledActionKeys();
        for (int i = 0; i < actionsToEnable.size(); i++) {
            Utils.getAction(actionsToEnable.get(i)).getAction().setEnabled(true);
        }
        for (int i = 0; i < actionsToDisable.size(); i++) {
            Utils.getAction(actionsToDisable.get(i)).getAction().setEnabled(false);
        }

        if (node instanceof TreeRootNode){//.type.equalsIgnoreCase(LDAPNode.TYPE_ROOT_NODE)) {
            Actions.setDisabledActionsTreeRootNode();
        } else if (node instanceof VirtualNode){//.type.equalsIgnoreCase(LDAPNode.TYPE_VIRTUAL_NODE)) {
            Actions.setDisabledActionsVirtualNode();
        } else if (node instanceof LDAPNode){//.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE)) {

        }
        
        //We tests if the selected connection needs to be saved
        TreeRootNode rootNode = (TreeRootNode) node.getRoot();
        if(rootNode.getLdapServer().getConnectionData().isDirty()){
            Utils.getAction(SaveTab.HashKey).setEnabled(true);
        }
        
    }
}
