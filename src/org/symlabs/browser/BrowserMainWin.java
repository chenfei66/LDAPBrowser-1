package org.symlabs.browser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.symlabs.actions.tab.CloseAllTabs;
import org.symlabs.actions.tab.CloseTab;
import org.symlabs.actions.tab.TabProperties;
import org.symlabs.actions.DsAction;
import org.symlabs.actions.config.ShowPreferences;
import org.symlabs.actions.ldif.ExportSubTree;
import org.symlabs.actions.ldif.ExportTree;
import org.symlabs.actions.node.AddNewNode;
import org.symlabs.actions.node.CloneNode;
import org.symlabs.actions.node.CopyDn;
import org.symlabs.actions.node.DeleteNode;
import org.symlabs.actions.node.RefreshTree;
import org.symlabs.actions.node.RenameNode;
import org.symlabs.actions.rootdse.ShowRootDse;
import org.symlabs.actions.schema.ShowSchema;
import org.symlabs.actions.search.AddSearch;
import org.symlabs.actions.search.NewSearch;
import org.symlabs.actions.search.SaveSearch;
import org.symlabs.actions.tab.NewTab;
import org.symlabs.actions.tab.OpenTab;
import org.symlabs.actions.tab.SaveAsTab;
import org.symlabs.actions.tab.SaveTab;
import org.symlabs.bookmark.BookmarkBaseNode;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.bookmark.BookmarkNode;
import org.symlabs.browser.editor.EditorFieldValue;
import org.symlabs.browser.help.AboutWin;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.search.SearchBaseNode;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.search.SearchNode;
import org.symlabs.store.BrowserPreferences;
import org.symlabs.store.ConnectionData;
import org.symlabs.store.SaveAndLoadBrowserPreferences;
import org.symlabs.util.Actions;
import org.symlabs.util.Utils;
 
/**
 * <p>Titulo: BrowserMainWin </p>
 * <p>Descripcion: Class which manages the browser window. This is the main window of the project. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BrowserMainWin.java,v 1.73 2009-09-29 09:45:21 efernandez Exp $
 */
public class BrowserMainWin extends javax.swing.JFrame {

    /**Attribute that contains the default tab title, this is the default title to be setted for each tab*/
    public static final String DefaultTabTitle = "New Connection";
    /**Attribute that contains the width of the main window screen*/
    public static final int Width = 800;
    /**Attribute that contains the height of the main window screen*/
    public static final int Height = 600;
    /**Attribute that contains the actions displayed in the popup menu*/
    private HashMap<String, DsAction> actions;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BrowserMainWin.class);
    /**Attribute that contains the number of the total menuitems initially displayed in the Search menu option*/
    public static final int TOTAL_SEARCHES_MENUITEMS = 4;//Add, Manage, Search and Save
    /**Attribute that contains the number of the total menuitems initiallly displayed in the Bookmark menu option*/
    public static final int TOTAL_BOOKMARKS_MENUITEMS = 2;//Add, Manage
    /**Attribute that contains the index of the first panel displayed. This index always will be 0*/
    private static int firstTabIndex = 0;
    /**Attribute that sets the max number of tabs displayed in the main window. This number includes the number 0.
    If this number is 3, it will be possible to create 3 connections, indexes 0,1,2.*/
    public static final int MaxTabMainWindow = 100;
    /**Attribute that contains the default editor shown*/
    public static final String DEFAULT_EDITOR_TITLE = EditorFieldValue.DEFAULT_EDITOR_TITLE;
    /**Attribute that contains the default editor shown*/
    private String defaultEditorTitle;
    /**Attribute that contains the default look and feel*/
    public static final String DEFAULT_LOOK_AND_FEEL = UIManager.getSystemLookAndFeelClassName();
    /**Attribute that contains the default look and feel*/
    private String defaultLookAndFeel;
    /**Attribute that contains the list of all available look and feel*/
    public final String[][] listAvailableLookAndFeel = BrowserMainWin.getListAvailableLookAndFeel();

    /**Method that returns the default editor title.
     * At this moment this is the Field-Value editor
     * 
     * @return String. This is the editor title
     */
    public String getDefaultTypeEditor() {
        if (defaultEditorTitle != null) {
            return this.defaultEditorTitle;
        } else {
            return DEFAULT_EDITOR_TITLE;
        }
    }

    /**Method that sets the default editor type
     * 
     * @param editorTitle String. This is the editor to set as default editor
     */
    public void setDefaultTypeEditor(String editorTitle) {
        this.defaultEditorTitle = editorTitle;
    }

    /**Method that returns the look and feel setted, if no look and feel was detected it is returned the default look and feel
     * 
     * @return String. This is the classname of the look and feel
     */
    public String getDefaultLookAndFeel() {
        if (this.defaultLookAndFeel != null) {
            return defaultLookAndFeel;
        } else {
            return BrowserMainWin.DEFAULT_LOOK_AND_FEEL;
        }
    }

    /**Method that sets the look and feel classname given as argument
     * 
     * @param lookAndFeelClassName String. This is the classname of the look and feel that we want to set
     */
    public void setDefaultLookAndFeel(String lookAndFeelClassName) {
        this.defaultLookAndFeel = lookAndFeelClassName;
        logger.trace("It has been setted the look and feel: " + lookAndFeelClassName);
        this.setLookAndFeel();
    }

    /**Method that returns the index of the first tab displayed. This index always should be 0.
     * 
     * @return int. This is the index of the first tab
     */
    public int getFirstTabIndexMainWindow() {
        return BrowserMainWin.firstTabIndex;
    }

    /**Method that returns the total number of tabs displayed in the main window
     * 
     * @return int. This is the number of total tbas displayed in the main window
     */
    public int getTotalTabsMainWindow() {
        return this.getContainerTabbedPane().getTabCount();
    }

    /**Method that tells us if it is possible to create a new ldap connection
     * 
     * @return boolean. True if it is possible to create a new connection. False if it is not possible to create a new connection.
     */
    public boolean canCreateMoreTabConnection() {
        //If the total number of tabs created is less than max number of tabs
        if (this.getContainerTabbedPane().getTabCount() < BrowserMainWin.MaxTabMainWindow) {
            return true;
        } else {
            return false;
        }
    }

    /**Method that returns the selected node of the panel given as argument.
     * 
     * @param tabTitle String. This is the title of the tab displayed
     * @return LDAPNode. This is the current node that it is being selected at this moment in this panel
     */
    public LDAPNode getSelectedNodeOfTab(String tabTitle) {
        BrowserPanel panel = this.getBrowserPanelByTitle(tabTitle);
        TreePath path = panel.getLdapTree().getSelectionPath();
        if (path == null || path.getPathCount() < 1) {
            logger.info("There is not any node selected");
            return null;
        } else {

        }
        return (LDAPNode) path.getLastPathComponent();
    }

    /**Method that sets the message given asargument in the status bar
     * 
     * @param message String
     */
    public void setStatusBarMessage(String message) {
        this.statusLabel.setText(message);
    }

    /**MEthod that clears the status bar message
     * 
     */
    public void clearStatusBarMessage() {
        this.setStatusBarMessage("");
    }

    /**Method that disconnects the current connection from the ldap server
     * 
     */
    public void disconnectCurrentConnectionFromLDAPServer() {
        try {
            this.getCurrentBrowserPanel().getTreeRootNode().getLdapServer().getConnection(false);
        } catch (Exception e) {
            String msg = "Error trying to disconnect from ldap server: ";
            String errorMsg = e.getMessage();
            String title = "Disconnecting Error";
            logger.error(msg);
            MessageDialog errorDialog = new MessageDialog(this, title, msg, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(this);
            errorDialog.setVisible(true);
        }
    }

    /**Method that removes the current tab from the browser main window.
     * 
     */
    private void removeCurrentTab() {
        this.getContainerTabbedPane().remove(this.getSelectedIndexOfBrowserPanel());
        Actions.setEnabledActionsTab();
//        Actions.setEnabledActionsWithoutAnyTab();
        if (Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0) {
            Actions.setEnabledActionsNode(Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode());
        } else {
            Actions.setEnabledActionsWithoutAnyTab();
        }
    }

    /**Method that closes the current ldap connection, and remove the current tab.
     * 
     */
    public void closeCurrentTabWithoutAsking() {
        //Disconnect from the ldap server
        this.disconnectCurrentConnectionFromLDAPServer();
        //Remove the current tab
        this.removeCurrentTab();
    }

    /**Method that close all tabs asking for each one
     * 
     */
    public void closeAllTabs() {
        try {
            while (this.getTotalTabsMainWindow() - 1 >= this.getFirstTabIndexMainWindow()) {
                Utils.getAction(CloseTab.HashKey).getAction().actionPerformed(null);
            }
        } catch (Exception ex) {
            String msg = "Error closing all tabs: " + ex;
            logger.error(msg);
        }
    }

    /**Method that adds a new tab to the browser main window.It is created a new ldap connection. 
     * If an error was found it is returned the error message.
     * 
     * @param host String. This is the host used to connect to LDAP server. 
     * @param port String. This is the port used to connect to LDAP server.
     * @param userId String. This is the user id used to connect to LDAP server.
     * @param userPw char[]. This is the user password used to connect to LDAP server.
     * @param suffix String. This is the suffix given in the window connection
     * @param indexLdapVersion int. This is the index of the ldap version selected. This index must be contained in LDAP_SUPPORTED_VERSION
     * @param bookmarkRootFolder BookmarkFolder.
     * @param searchRootFolder SearchFolder.
     * @param confName String. This is the name of the configuration saved. If the conf has not been saved then it will be null
     * @param iconPath String.
     * @return String. If an error was found it is returned the error message. If no error was found then it is returned an empty string
     */
    public String addNewTab(String host, String port, String userId, char[] userPw, String suffix,int indexLdapVersion,
            BookmarkFolderNode bookmarkRootFolder, SearchFolderNode searchRootFolder, String confName, String iconPath) {
        String errorMsg = "";
        try {
            //We create and add the new tab panel
            BrowserPanel tabPanel = new BrowserPanel(
                    host,
                    port,
                    userId,
                    userPw,
                    suffix,
                    indexLdapVersion,
                    bookmarkRootFolder,
                    searchRootFolder,
                    confName,
                    iconPath);

            //If there are any bookmark then we add this to the menu
            if (bookmarkRootFolder != null) {
                logger.trace("We add the bookmarks menu option 1");
                TreeRootNode rootNode = tabPanel.getTreeRootNode();
                BookmarkFolderNode rootFolder = rootNode.getLdapServer().getConnectionData().getBookmarkRootFolder();

                if (rootFolder != null && rootFolder.getChildCount() > 0) {
                    logger.trace("We add the bookmarks menu option 2");
                    this.refreshBookmarksMainWindow();
                }
            }

            //If there are any search then we add it to the menu
            if (searchRootFolder != null) {
                TreeRootNode rootNode = tabPanel.getTreeRootNode();
                SearchFolderNode rootFolder = rootNode.getLdapServer().getConnectionData().getSearchRootFolder();
                if (rootFolder != null && rootFolder.getChildCount() > 0) {
                    this.refreshSearchesMainWindow();
                }
            }

        } catch (Exception e) {
            errorMsg += "Error adding new Tab. " + "\n" + e.getMessage() + "\n";
            logger.error(errorMsg);
        }
        return errorMsg;
    }

    /**Method that refresh the menu bookmarks from the main window.
     * All the bookmarks will be removed and the opened connections will read the bookmarks and will be added to the menu option again
     * 
     */
    public void refreshBookmarksMainWindow() {
//        logger.trace("total items:" + this.bookMarkMenu.getItemCount());

//        int index = BrowserMainWin.TOTAL_BOOKMARKS_MENUITEMS;//The menu item already has two options: Add bookmark and Manage bookmark

//        // <editor-fold defaultstate="collapsed" desc=" Remove all the bookmarks menus and items ">
//        //If there are any bookmark displayed in the menu
//        //We remove all the bookmark menu items
//        while (bookMarkMenu.getItemCount() > BrowserMainWin.TOTAL_BOOKMARKS_MENUITEMS) {
//            bookMarkMenu.remove(index);
//            logger.trace("We remove the item: " + index + ",count:" + bookMarkMenu.getItemCount() + ",components:" + bookMarkMenu.getComponents().length);
//        }
//        // </editor-fold>
//
//        // <editor-fold defaultstate="collapsed" desc=" Add all the bookmarks menus and items ">
//        //We add all bookmarks from the opened connections
//        for (int i = 0; i < this.getContainerTabbedPane().getTabCount(); i++) {
//            BrowserPanel browser = (BrowserPanel) this.getContainerTabbedPane().getComponentAt(i);
//            TreeRootNode rootNode = browser.getTreeRootNode();
//            BookmarkFolderNode rootFolder = browser.getTreeRootNode().getLdapServer().getConnectionData().getBookmarkRootFolder();
//
//            //We add a separator to the menu
//            JSeparator separator = new JSeparator();
//            bookMarkMenu.add(separator);
//
//            //We add a new menu with the configuration name of this connection
//            JMenu browserMenu = new JMenu(rootNode.getLdapServer().getConnectionData().getConfigurationName());
//            bookMarkMenu.add(browserMenu);
//
//            //We adds to the configuration name menu option its bookmark menu items
//            this.addBookmarksToMenu(rootFolder, browserMenu, rootNode);
//        }
//    // </editor-fold>
    }

    /**Method that adds the bookmarks jmenuitem and jmenu contained in the rootNode given as argument
     * 
     * @param rootFolder BookmarkFolder. It contains the bookmarks to add to the menu
     * @param bookmarkMenu JMenu. This is the menu where we want to ass these bookmarks
     * @param rootNode TreeRootNode. Used to implements the action for each bookmark
     */
    private void addBookmarksToMenu(BookmarkFolderNode rootFolder, JMenu bookmarkMenu, final TreeRootNode rootNode) {
        for (int i = 0; i < rootFolder.getChildCount(); i++) {
            BookmarkBaseNode base = (BookmarkBaseNode) rootFolder.getChildAt(i);
            if (base instanceof BookmarkFolderNode) {
                BookmarkFolderNode folder = (BookmarkFolderNode) base;
                JMenu newMenu = new JMenu(folder.getName());
                newMenu.setIcon(Utils.createImageIcon(Utils.ICON_BOOKMARK_FOLDER));
                bookmarkMenu.add(newMenu);
                this.addBookmarksToMenu(folder, newMenu, rootNode);
            } else if (base instanceof BookmarkNode) {
                final BookmarkNode book = (BookmarkNode) base;
                JMenuItem myNewMenuItem = new JMenuItem(book.getName());
                myNewMenuItem.setAction(new org.symlabs.actions.bookmark.GoToBookmark(rootNode, book));
                bookmarkMenu.add(myNewMenuItem);
            }
        }
    }

    /**Method that refresh the searches contained in the main window
     * All the searches will be removed and the opened connections will read the searches and will be added to the menu option again
     * 
     */
    public void refreshSearchesMainWindow() {
        //If there are any search then we add it to the menu        
        logger.trace("total items:" + searchMenu.getItemCount());

        int index = BrowserMainWin.TOTAL_SEARCHES_MENUITEMS;

        // <editor-fold defaultstate="collapsed" desc=" Remove all the search menus and items ">
        //If there are any search displayed in the menu 
        //We remove all the search menu items
        while (searchMenu.getItemCount() > BrowserMainWin.TOTAL_SEARCHES_MENUITEMS) {
            searchMenu.remove(index);
            logger.trace("We remove the item: " + index + ",count:" + searchMenu.getItemCount() + ",components:" + searchMenu.getComponents().length);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Add all the bookmarks menus and items ">
        //We add all bookmarks from the opened connections
        for (int i = 0; i < this.getContainerTabbedPane().getTabCount(); i++) {
            BrowserPanel browser = (BrowserPanel) this.getContainerTabbedPane().getComponentAt(i);
            TreeRootNode treeRootNode = browser.getTreeRootNode();
            SearchFolderNode searchRootFolder = browser.getTreeRootNode().getLdapServer().getConnectionData().getSearchRootFolder();

            //We add a separator to the menu
            JSeparator separator = new JSeparator();
            searchMenu.add(separator);

            //We add a new menu with the configuration name of this connection
            JMenu browserMenu = new JMenu(treeRootNode.getLdapServer().getConnectionData().getConfigurationName());
            searchMenu.add(browserMenu);

            //We adds to the configuration name menu option its bookmark menu items
            this.addSearchesToMenu(searchRootFolder, browserMenu, treeRootNode);
        }
    // </editor-fold>
    }

    /**Method that adds the searches jmenuitem and jmenu contained in the rootNode given as argument
     * 
     * @param searchRootFolder SearchFolder. It contains the bookmarks to add to the menu
     * @param searchMenu JMenu. This is the menu where we want to ass these bookmarks
     * @param treeRootNode TreeRootNode. Used to implements the action for each bookmark
     */
    private void addSearchesToMenu(SearchFolderNode searchRootFolder, JMenu searchMenu, final TreeRootNode treeRootNode) {
        for (int i = 0; i < searchRootFolder.getChildCount(); i++) {
            SearchBaseNode base = (SearchBaseNode) searchRootFolder.getChildAt(i);
            if (base instanceof SearchFolderNode) {
                SearchFolderNode folder = (SearchFolderNode) base;
                JMenu newMenu = new JMenu(folder.getName());
                newMenu.setIcon(Utils.createImageIcon(Utils.ICON_SEARCH_FOLDER));
                searchMenu.add(newMenu);
                this.addSearchesToMenu(folder, newMenu, treeRootNode);
            } else if (base instanceof SearchNode) {
                final SearchNode search = (SearchNode) base;
                JMenuItem myNewMenuItem = new JMenuItem(search.getName());
                myNewMenuItem.setAction(new org.symlabs.actions.search.LoadSearch(treeRootNode, search, true));
                searchMenu.add(myNewMenuItem);
            }
        }
    }

    /**Method that returns the status label.
     * This is the status bar displayed at the bottom of the main window.
     * 
     * @return JLabel. This is the status bar JLabel
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    /**Method that returns the tab container. 
     * This is the JtabbedPane where is going to be added the tab panel for each connection
     * 
     * @return JTabbedPane. This is the container of the Tabs displayed in the main window
     */
    public JTabbedPane getContainerTabbedPane() {
        return containerTabbedPane;
    }

    /**Method that sets the title of the tab panel given as argument by the index
     * 
     * @param index int . This is the index of the tab pane to change its title
     * @param title String. This is the title of the tab pane to change its title
     */
    public void setTitleTabPanel(int index, String title) {
        this.containerTabbedPane.setTitleAt(index, title);
    }

    /**Method that sets the title of the current  tab panel
     * 
     * @param title String. This is the title of the current tab pane
     */
    public void setTitleCurrentTabPanel(String title) {
        this.containerTabbedPane.setTitleAt(this.getContainerTabbedPane().getSelectedIndex(), title);
    }

    /**Method that returns the selected index of browser panel
     * 
     * @return int
     */
    public int getSelectedIndexOfBrowserPanel() {
        return this.getContainerTabbedPane().getSelectedIndex();
    }

    /**Method that returns the current tab panel displayed in the main window
     * 
     * @return BrowserPanel. This is the current tab panel
     */
    public BrowserPanel getCurrentBrowserPanel() {
        return (BrowserPanel) this.containerTabbedPane.getSelectedComponent();//.getComponentAt(Utils.getCurrentTabIndexMainWindow());
    }

    /**Method that returns the tab panel which titile is the given as argument
     * 
     * @param title String. This is the title of the tab that we want to get
     * @return BrowserPanel
     */
    public BrowserPanel getBrowserPanelByTitle(String title) {
        for (int i = 0; i < this.containerTabbedPane.getTabCount(); i++) {
//            logger.trace("title of tab("+i+"):"+this.containerTabbedPane.getTitleAt(i));
            if (this.containerTabbedPane.getTitleAt(i).equals(title)) {
                logger.trace("It is returned the browser panel: " + title);
                return (BrowserPanel) this.containerTabbedPane.getComponentAt(i);
            }
        }
        logger.trace("It is returned null");
        return null;
    }

    /**Method that returns the tab panel contained of the index given as argument
     * 
     * @param indexPanel int. This is the index of the panel to be returned
     * @return BrowserPanel. This is the tab panel of the index given as argument
     */
    public BrowserPanel getBrowserPanel(int indexPanel) {
        return (BrowserPanel) this.containerTabbedPane.getComponentAt(indexPanel);
    }

    /**Method that adds an action in the actions hashmap
     * 
     * @param actionName String. This is the action name
     * @param action DsAction. This is the Dsaction created and displayed in the popup menu
     */
    public void putAction(String actionName, DsAction action) {
        this.actions.put(actionName.trim().toLowerCase(), action);
    }

    /**Method that adds a new connection to the connection tabbed pane
     * 
     * @param configurationName String. This is the configuration name
     * @param connectionPanel BrowserPanel
     */
    public void addNewConnection(String configurationName, BrowserPanel connectionPanel) {
        ConnectionData data = connectionPanel.getTreeRootNode().getLdapServer().getConnectionData();
        String toolTipText = "<HTML>host: " + data.getHost() + "<br>port:" + data.getPort() + "<br>credentials:" + data.getAuthid() + "</HTML>";
        this.getContainerTabbedPane().addTab(configurationName, Utils.createImageIcon(data.getIconPath()), connectionPanel, toolTipText);
    }

    /**Method that sets the the title in the tab given by the argument index
     * 
     * @param tabIndex int. This is the index of the tab to change its title
     * @param title String. This is the new title to set
     */
    public void setTabTitle(int tabIndex, String title) {
        this.getContainerTabbedPane().setTitleAt(tabIndex, title);
    }

    /**Method that sets as selected the panel given as argument
     * 
     * @param connectionPanel BrowserPanel
     */
    public void setSelectedConnection(BrowserPanel connectionPanel) {
        this.getContainerTabbedPane().setSelectedComponent(connectionPanel);
    }

    /**Method that sets as selected the panel with the string title given as argument
     * 
     * @param configurationName String. This is the title of the tab panne
     */
    public void setSelectedConnection(String configurationName) {
        for (int i = 0; i < this.getContainerTabbedPane().getTabCount(); i++) {
            if (this.getContainerTabbedPane().getTitleAt(i).equals(configurationName)) {
                this.getContainerTabbedPane().setSelectedIndex(i);
                return;
            }
        }
    }

    /**Method that returns the list of available lok and feels
     * 
     * @return String[][]. It contains the name and the classname
     */
    private static String[][] getListAvailableLookAndFeel() {
        LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();

        String[][] available = new String[lafi.length][2];
        for (int i = 0; i < available.length; i++) {
            available[i][0] = lafi[i].getName();
            available[i][1] = lafi[i].getClassName();
        }

        for (int i = 0; i < available.length; i++) {
            logger.trace("Available LookAndFeel " + i + ", Name: " + available[i][0] + " ,classname: " + available[i][1]);
        }

        return available;
    }

    /**Method that initializes the object actions.
     * It contains the options displayed in the browser.
     * 
     */
    private void initActions() {
        this.actions = new HashMap<String, DsAction>();
        Actions.initBrowserMainWinActions();
    }

    /**Method that returns the DsAction given by the argument actionName.
     * 
     * @param actionName String. This is the key of the DsAction.haskey
     * @return DsAction. This is the dsaction which you are asking for
     */
    public DsAction getAction(String actionName) {
        actionName = actionName.toLowerCase();
        if (this.actions.containsKey(actionName.trim())) {
            return this.actions.get(actionName.trim());
        } else {
            String msg = "The action " + actionName + " was not found.";
            logger.error(msg);
            return null;
        }
    }

    /**Constructor: initializes and creates the components of the main window
     * 
     */
    public BrowserMainWin() {
        //Set the main window
        Utils.setMainWindow(this);
        //We create all the actions used in this application
        this.initActions();
        //We init components
        initComponents();
        //load the properties required
        initProperties();
        //set disable the actions that we do not should use
        Actions.setEnabledActionsWithoutAnyTab();
        //We clear the status bar
        this.clearStatusBarMessage();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        browserToolBar = new javax.swing.JToolBar();
        connectNewButton = new javax.swing.JButton();
        connectOpenButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        conPropertiesButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        addNewEntryButton = new javax.swing.JButton();
        cloneEntryButton = new javax.swing.JButton();
        copyDnButton = new javax.swing.JButton();
        removeEntryButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        addSearchButton = new javax.swing.JButton();
        showSchemaButton = new javax.swing.JButton();
        showRootDseButton = new javax.swing.JButton();
        containerPanel = new javax.swing.JPanel();
        containerTabbedPane = new javax.swing.JTabbedPane();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        browserMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JSeparator();
        connectionPropertiesMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        closeMenuItem = new javax.swing.JMenuItem();
        closeAllMenuItem = new javax.swing.JMenuItem();
        separator3 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        separator4 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        newEntryMenuItem = new javax.swing.JMenuItem();
        cloneEntryMenuItem = new javax.swing.JMenuItem();
        renameEntryMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        separator5 = new javax.swing.JSeparator();
        copyDNMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        refreshMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        showSchemaMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        showRootDseMenuItem = new javax.swing.JMenuItem();
        searchMenu = new javax.swing.JMenu();
        searchWithoutSavingMenuItem = new javax.swing.JMenuItem();
        addSearchMenuItem = new javax.swing.JMenuItem();
        manageSearchMenuItem = new javax.swing.JMenuItem();
        saveSearchMenuItem = new javax.swing.JMenuItem();
        ldifMenu = new javax.swing.JMenu();
        exportFullTreeMenuItem = new javax.swing.JMenuItem();
        exportSubtreeMenuItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        configurationMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Utils.BROWSER_TITLE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        browserToolBar.setFloatable(false);
        browserToolBar.setRollover(true);

        connectNewButton.setAction(Utils.getAction(NewTab.HashKey));
        connectNewButton.setFocusable(false);
        connectNewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        connectNewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.connectNewButton.setText("");
        browserToolBar.add(connectNewButton);

        connectOpenButton.setAction(Utils.getAction(OpenTab.HashKey));
        connectOpenButton.setFocusable(false);
        connectOpenButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        connectOpenButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.connectOpenButton.setText("");
        browserToolBar.add(connectOpenButton);

        disconnectButton.setAction(Utils.getAction(CloseTab.HashKey));
        disconnectButton.setFocusable(false);
        disconnectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        disconnectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.disconnectButton.setText("");
        browserToolBar.add(disconnectButton);

        conPropertiesButton.setAction(Utils.getAction(TabProperties.HashKey));
        conPropertiesButton.setFocusable(false);
        conPropertiesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        conPropertiesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.conPropertiesButton.setText("");
        browserToolBar.add(conPropertiesButton);

        saveButton.setAction(Utils.getAction(SaveTab.HashKey));
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.saveButton.setText("");
        browserToolBar.add(saveButton);

        addNewEntryButton.setAction(Utils.getAction(AddNewNode.HashKey));
        addNewEntryButton.setFocusable(false);
        addNewEntryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addNewEntryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.addNewEntryButton.setText("");
        browserToolBar.add(addNewEntryButton);

        cloneEntryButton.setAction(Utils.getAction(CloneNode.HashKey));
        cloneEntryButton.setFocusable(false);
        cloneEntryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cloneEntryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.cloneEntryButton.setText("");
        browserToolBar.add(cloneEntryButton);

        copyDnButton.setAction(Utils.getAction(CopyDn.HashKey));
        copyDnButton.setFocusable(false);
        copyDnButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyDnButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.copyDnButton.setText("");
        browserToolBar.add(copyDnButton);

        removeEntryButton.setAction(Utils.getAction(DeleteNode.HashKey));
        removeEntryButton.setFocusable(false);
        removeEntryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeEntryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.removeEntryButton.setText("");
        browserToolBar.add(removeEntryButton);

        refreshButton.setAction(Utils.getAction(RefreshTree.HashKey));
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.refreshButton.setText("");
        browserToolBar.add(refreshButton);

        addSearchButton.setAction(Utils.getAction(AddSearch.HashKey));
        addSearchButton.setFocusable(false);
        addSearchButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSearchButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.addSearchButton.setText("");
        browserToolBar.add(addSearchButton);

        showSchemaButton.setAction(Utils.getAction(ShowSchema.HashKey));
        showSchemaButton.setFocusable(false);
        showSchemaButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showSchemaButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.showSchemaButton.setText("");
        browserToolBar.add(showSchemaButton);

        showRootDseButton.setAction(Utils.getAction(ShowRootDse.HashKey));
        showRootDseButton.setFocusable(false);
        showRootDseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showRootDseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.showRootDseButton.setText("");
        browserToolBar.add(showRootDseButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(browserToolBar, gridBagConstraints);

        containerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        containerPanel.add(containerTabbedPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(containerPanel, gridBagConstraints);

        statusPanel.setLayout(new java.awt.GridBagLayout());

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText("status bar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        statusPanel.add(statusLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(statusPanel, gridBagConstraints);

        fileMenu.setText("File");

        newMenuItem.setAction(Utils.getAction(NewTab.HashKey));
        newMenuItem.setText("New...");
        fileMenu.add(newMenuItem);

        openMenuItem.setAction(Utils.getAction(org.symlabs.actions.tab.OpenTab.HashKey));
        openMenuItem.setText("Open...");
        fileMenu.add(openMenuItem);
        fileMenu.add(separator1);

        connectionPropertiesMenuItem.setAction(Utils.getAction(TabProperties.HashKey));
        connectionPropertiesMenuItem.setText("Properties...");
        fileMenu.add(connectionPropertiesMenuItem);
        fileMenu.add(jSeparator1);

        closeMenuItem.setAction(Utils.getAction(CloseTab.HashKey));
        closeMenuItem.setText("Close");
        fileMenu.add(closeMenuItem);

        closeAllMenuItem.setAction(Utils.getAction(CloseAllTabs.HashKey));
        closeAllMenuItem.setText("Close All");
        fileMenu.add(closeAllMenuItem);
        fileMenu.add(separator3);

        saveMenuItem.setAction(Utils.getAction(SaveTab.HashKey));
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(Utils.getAction(SaveAsTab.HashKey));
        saveAsMenuItem.setText("Save As...");
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(separator4);

        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/symlabs/images/icons/exitMainWin16x16.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        browserMenuBar.add(fileMenu);

        editMenu.setText("Edit");

        newEntryMenuItem.setAction(Utils.getAction(org.symlabs.actions.node.AddNewNode.HashKey));
        newEntryMenuItem.setText("New Entry");
        editMenu.add(newEntryMenuItem);

        cloneEntryMenuItem.setAction(Utils.getAction(CloneNode.HashKey));
        cloneEntryMenuItem.setText("Clone Entry");
        editMenu.add(cloneEntryMenuItem);

        renameEntryMenuItem.setAction(Utils.getAction(RenameNode.HashKey));
        renameEntryMenuItem.setText("Rename Entry");
        editMenu.add(renameEntryMenuItem);

        deleteMenuItem.setAction(Utils.getAction(DeleteNode.HashKey));
        deleteMenuItem.setText("Delete Entry");
        editMenu.add(deleteMenuItem);
        editMenu.add(separator5);

        copyDNMenuItem.setAction(Utils.getAction(CopyDn.HashKey));
        copyDNMenuItem.setText("Copy dn");
        editMenu.add(copyDNMenuItem);

        browserMenuBar.add(editMenu);

        viewMenu.setText("View");

        refreshMenuItem.setAction(Utils.getAction(RefreshTree.HashKey));
        refreshMenuItem.setText("Refresh");
        viewMenu.add(refreshMenuItem);
        viewMenu.add(jSeparator2);

        showSchemaMenuItem.setAction(Utils.getAction(ShowSchema.HashKey));
        showSchemaMenuItem.setText("Show Schema");
        viewMenu.add(showSchemaMenuItem);
        viewMenu.add(jSeparator4);

        showRootDseMenuItem.setAction(Utils.getAction(ShowRootDse.HashKey));
        showRootDseMenuItem.setText("Root Dse Info");
        viewMenu.add(showRootDseMenuItem);

        browserMenuBar.add(viewMenu);

        searchMenu.setText("Search");

        searchWithoutSavingMenuItem.setAction(Utils.getAction(NewSearch.HashKey));
        searchWithoutSavingMenuItem.setText("New Search");
        searchMenu.add(searchWithoutSavingMenuItem);

        addSearchMenuItem.setAction(Utils.getAction(AddSearch.HashKey));
        addSearchMenuItem.setText("Add Search");
        searchMenu.add(addSearchMenuItem);

        manageSearchMenuItem.setAction(Utils.getAction(org.symlabs.actions.search.ManageSearch.HashKey));
        manageSearchMenuItem.setText("Manage Searches");
        searchMenu.add(manageSearchMenuItem);

        saveSearchMenuItem.setAction(Utils.getAction(SaveSearch.HashKey));
        saveSearchMenuItem.setText("Save Search As...");
        searchMenu.add(saveSearchMenuItem);

        browserMenuBar.add(searchMenu);

        ldifMenu.setText("LDIF");

        exportFullTreeMenuItem.setAction(Utils.getAction(ExportTree.HashKey));
        exportFullTreeMenuItem.setText("Export Full Tree");
        ldifMenu.add(exportFullTreeMenuItem);

        exportSubtreeMenuItem.setAction(Utils.getAction(ExportSubTree.HashKey));
        exportSubtreeMenuItem.setText("Export Full SubTree");
        ldifMenu.add(exportSubtreeMenuItem);

        browserMenuBar.add(ldifMenu);

        optionsMenu.setText("Options");

        configurationMenuItem.setAction(Utils.getAction(ShowPreferences.HashKey));
        configurationMenuItem.setText("Preferences");
        optionsMenu.add(configurationMenuItem);

        browserMenuBar.add(optionsMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        browserMenuBar.add(helpMenu);

        setJMenuBar(browserMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**Method that sets the same look and feel that the operative system
     * 
     */
    /**Method that initializes the components, listeners and properties required
     * 
     */
    private void initProperties() {
        //Create a new mouse listener for the connection tabs
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                //Add the popup menu if it is called
                if (e.isPopupTrigger()) {
                    logger.trace("We show the Tab popup menu");
                    getCurrentBrowserPanel().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }

                logger.trace("It has been clicked on a tab title, so we update the actions state");
                //It has been clicked on a tab, show we have to update the actions for the selected tab
                Actions.setEnabledActionsTab();
                if (Utils.getMainWindow().getContainerTabbedPane().getTabCount() > 0) {
                    Actions.setEnabledActionsNode(Utils.getMainWindow().getCurrentBrowserPanel().getSelectedNode());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed(e);
            }
        };

        //Add mouse listener
        this.containerTabbedPane.addMouseListener(ml);

        //We get the default preferences
        BrowserPreferences preferences = SaveAndLoadBrowserPreferences.getPreferencesFromPreferencesFile();
        this.defaultEditorTitle = preferences.getEditor();
        this.defaultLookAndFeel = preferences.getLookAndFeel();

        //Sets the native look and feel
        this.setLookAndFeel();

        //We set the window position
        this.setLocationRelativeTo(null);
    }

    /**Method that sets the native lok and feel.
     * This look and feel will be the same as your Operative System
     * 
     */
    private void setLookAndFeel() {
        logger.trace("Setting native look and feel: " + this.defaultLookAndFeel);
        try {
            UIManager.setLookAndFeel(this.defaultLookAndFeel);
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();

        } catch (Exception e) {
            logger.error("Setting Native Look and Feel. " + e);
            try {
                //Sets the Java look and feel 
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                String errorMsg = "Could not load the native look and feel: " + ex + "\n";
                String msg = "Could not load the native look and feel.";
                String title = "Error in Look And Feel";
                logger.error(msg);
                MessageDialog errorDialog = new MessageDialog(this, title, msg, errorMsg, MessageDialog.MESSAGE_ERROR);
                errorDialog.setLocationRelativeTo(this);
                errorDialog.setVisible(true);
            }
        }
    }

    /**Method that returns the class name of the look and feel name given as argument
     * 
     * @param lookAndFeelName String. This is the name of the look and feel
     * @return String. It is returned the class name
     */
    public String getClassNameOfLookAndFeelName(String lookAndFeelName) {
        String[][] list = Utils.getMainWindow().listAvailableLookAndFeel;
        for (int i = 0; i < list.length; i++) {
            logger.trace("Getting String lookAndFeelName = " + lookAndFeelName + ", buscando en list[" + i + "][0]: " + list[i][0]);
            if (list[i][0].equalsIgnoreCase(lookAndFeelName)) {
                return list[i][1];
            }
        }
        return list[0][1];
    }

    /**MEthod that returns the int of the element lookAndFeelClassName contained in the list of look and feels
     * 
     * @param lookAndFeelClassName String. This is the look and feel to search
     * @return int. It is returned its index
     */
    public int getIndexOfLookAndFeelClassName(String lookAndFeelClassName) {
        String[][] list = Utils.getMainWindow().listAvailableLookAndFeel;
        for (int i = 0; i < list.length; i++) {
            logger.trace("Getting int lookAndFeelName = " + lookAndFeelClassName + ", buscando en list[" + i + "][0]: " + list[i][1]);
            if (list[i][1].equalsIgnoreCase(lookAndFeelClassName)) {
                return i;
            }
        }
        return 0;
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.closeAllTabs();
        System.exit(0);  //this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutWin win = new AboutWin();
        win.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addNewEntryButton;
    private javax.swing.JButton addSearchButton;
    private javax.swing.JMenuItem addSearchMenuItem;
    private javax.swing.JMenuBar browserMenuBar;
    private javax.swing.JToolBar browserToolBar;
    private javax.swing.JButton cloneEntryButton;
    private javax.swing.JMenuItem cloneEntryMenuItem;
    private javax.swing.JMenuItem closeAllMenuItem;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JButton conPropertiesButton;
    private javax.swing.JMenuItem configurationMenuItem;
    private javax.swing.JButton connectNewButton;
    private javax.swing.JButton connectOpenButton;
    private javax.swing.JMenuItem connectionPropertiesMenuItem;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JTabbedPane containerTabbedPane;
    private javax.swing.JMenuItem copyDNMenuItem;
    private javax.swing.JButton copyDnButton;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem exportFullTreeMenuItem;
    private javax.swing.JMenuItem exportSubtreeMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenu ldifMenu;
    private javax.swing.JMenuItem manageSearchMenuItem;
    private javax.swing.JMenuItem newEntryMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JButton refreshButton;
    private javax.swing.JMenuItem refreshMenuItem;
    private javax.swing.JButton removeEntryButton;
    private javax.swing.JMenuItem renameEntryMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveSearchMenuItem;
    public javax.swing.JMenu searchMenu;
    private javax.swing.JMenuItem searchWithoutSavingMenuItem;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator3;
    private javax.swing.JSeparator separator4;
    private javax.swing.JSeparator separator5;
    private javax.swing.JButton showRootDseButton;
    private javax.swing.JMenuItem showRootDseMenuItem;
    private javax.swing.JButton showSchemaButton;
    private javax.swing.JMenuItem showSchemaMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}
