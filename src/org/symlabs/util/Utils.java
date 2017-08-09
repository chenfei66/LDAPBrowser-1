package org.symlabs.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.util.LDIFRecord;
import netscape.ldap.util.LDIFWriter;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserMainWin;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.nodes.VirtualNode;
import org.symlabs.store.ConnectionData;

/**
 * <p>Titulo: Utils </p>
 * <p>Descripcion: Class that provides of static attributes and static methods used in the application browser ldap</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: Utils.java,v 1.105 2009-09-29 14:26:16 efernandez Exp $
 */
public class Utils {

    /**Attribute that contains the title of the browser*/
    public static final String BROWSER_TITLE = "LDAP Browser";
    /**Attribute that contains the version of this product*/
    public static final String BROWSER_VERSION = "1.0";
    /**Attribute that contains the frame of the main window of the this LDAP browser application */
    private static BrowserMainWin mainWindow = null;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(Utils.class);
    /**Attribute static that contains the user folder*/
    public static final String UserFolder = System.getProperty("user.home");
    /**Attribute static that contains the preferences folder*/
    private static final String PreferencesFolder = "ldapBrowser";
    /**Attribute static that contains the preferences folder*/
    private static final String PreferencesFile = ".browser.preferences";
    /**Attribute static that contains the configurations folder. This folder contains all the confs stored.*/
    private static final String ConfigurationsFolder = "confs";
    /**Attribute static that contains the logger properties file*/
    private static final String loggerFile = ".browser.properties";
    /**Attribute used to identify the file that save the configuration*/
    public static final String configurationFile = ".browser.configfile";
    /**Attribute used to manage the first access to the preferences folder. 
     * Used to call the method to create it only the first time it is called.
     * True - if preferences folder has not been accessed
     * False - If preferences folder has been accessed*/
    private static boolean firstAccessToFolderPreferences = true;
    /**Attribute that contains the default folder to export a tree*/
    private static final String DEFAULT_EXPORT_LDIF_TREE_FOLDER = Utils.getPreferencesPath();
    /**Attribute that contains the default tree file name*/
    private static final String DEFAULT_EXPORT_LDIF_TREE_FILE = "Tree.ldif";
    /**Attribute that contains the default subtree file name*/
    private static final String DEFAULT_EXPORT_LDIF_SUBTREE_FILE = "SubTree.ldif";
    /**Attribute that contains the default folder to export a sub tree, by default the same as export ldif tree*/
    private static final String DEFAULT_EXPORT_LDIF_SUBTREE_FOLDER = DEFAULT_EXPORT_LDIF_TREE_FOLDER;
    /**Attribute that sets the editable mode. This mode is used to edit the attributes of an ldap node*/
    private static boolean editableMode = false;
    /**Attribute that contains the path of a virtual node image icon*/
    public static final String ICON_VIRTUAL_NODE = "/org/symlabs/images/icons/virtualNode16x16.png";
    /**Attribute that contains the path of a tree root node image icon*/
    public static final String ICON_ROOT_NODE = "/org/symlabs/images/icons/rootNode16x16.png";
    /**Attribute that contains the path of a dc (domain component) node image icon*/
    public static final String ICON_DC_NODE = "/org/symlabs/images/icons/dcNode16x16.png";
    /**Attribute that contains the path of a ou (organization unit) node image icon*/
    public static final String ICON_OU_NODE = "/org/symlabs/images/icons/ouNode16x16.png";
    /**Attribute that contains the path of a c (country name) node image icon*/
    public static final String ICON_C_NODE = "/org/symlabs/images/icons/cNode16x16.png";
    /**Attribute that contains the path of a cn (common name) node image icon*/
    public static final String ICON_CN_NODE = "/org/symlabs/images/icons/cnNode16x16.png";
    /**Attribute that contains the path of a uid (user identifier) node image icon*/
    public static final String ICON_UID_NODE = "/org/symlabs/images/icons/uidNode16x16.png";
    /**Attribute that contains the path of the default icon*/
    public static final String ICON_DEFAULT_NODE = "/org/symlabs/images/icons/defaultNode16x16.png";
    /**Attribute that contains the path of the remove a value icon of an attribute*/
    public static final String ICON_VALUE_REMOVE = "/org/symlabs/images/icons/removeValue16x16.png";
    /**Attribute that contains the path of the add a value icon of an attribute*/
    public static final String ICON_VALUE_NEW = "/org/symlabs/images/icons/addValue16x16.png";
    /**Attribute that contains the path of an information image*/
    public static final String MESSAGE_IMAGE_INFORMATION = "/org/symlabs/images/dialogInformation.png";
    /**Attribute that contains the path of a warning image*/
    public static final String MESSAGE_IMAGE_WARNING = "/org/symlabs/images/dialogWarning.png";
    /**Attribute that contains the path of an error image*/
    public static final String MESSAGE_IMAGE_ERROR = "/org/symlabs/images/dialogError.png";
    /**Attribute that contains the path of the connect icon*/
    public static final String ICON_CONNECT_NEW = "/org/symlabs/images/icons/connectNew16x16.png";
    /**Attribute that contains the path of the connect icon*/
    public static final String ICON_CONNECT_OPEN = "/org/symlabs/images/icons/connectOpen16x16.png";
    /**Attribute that contains the path of the disconnect icon*/
    public static final String ICON_DISCONNECT = "/org/symlabs/images/icons/disconnect16x16.png";
    /**Attribute that contains the path of the all disconnect icon*/
    public static final String ICON_DISCONNECT_ALL = "/org/symlabs/images/icons/disconnectAll16x16.png";
    /**Attribute that contains the path of the save icon*/
    public static final String ICON_SAVE = "/org/symlabs/images/icons/save16x16.png";
    /**Attribute that contains the path of the save as icon*/
    public static final String ICON_SAVE_AS = "/org/symlabs/images/icons/saveAs16x16.png";
    /**Attribute that contains the path of the save all icon*/
    public static final String ICON_SAVE_ALL = "/org/symlabs/images/icons/saveAll16x16.png";
    /**Attribute that contains the path of the icon printer*/
    public static final String ICON_PRINTER = "/org/symlabs/images/icons/printer16x16.png";
    /**Attribute that contains the path of the exit main window*/
    public static final String ICON_EXIT_MAIN_WIN = "/org/symlabs/images/icons/exitMainWin16x16.png";
    /**Attribute that contains the path of the exit main window*/
    public static final String ICON_CONNECTION_PROPERTIES = "/org/symlabs/images/icons/connectionProperties16x16.png";
    /**Attribute that contains the path of the add new entry icon*/
    public static final String ICON_ENTRY_ADD_NEW = "/org/symlabs/images/icons/addNewEntry16x16.png";
    /**Attribute that contains the path of the clone entry icon*/
    public static final String ICON_ENTRY_CLONE = "/org/symlabs/images/icons/cloneEntry16x16.png";
    /**Attribute that contains the path of the rename entry icon*/
    public static final String ICON_ENTRY_RENAME = "/org/symlabs/images/icons/renameEntry16x16.png";
    /**Attribute that contains the path of the delete entry icon*/
    public static final String ICON_ENTRY_DELETE = "/org/symlabs/images/icons/removeEntry16x16.png";
    /**Attribute that contains the path of the copy Dn entry icon*/
    public static final String ICON_ENTRY_COPY_DN = "/org/symlabs/images/icons/copyDnEntry16x16.png";
    /**Attribute that contains the path of the refresh tree icon*/
    public static final String ICON_TREE_REFRESH = "/org/symlabs/images/icons/refreshTree16x16.png";
    /**Attribute that contains the path of the move up icon*/
    public static final String ICON_ENTRY_MOVE_UP = "/org/symlabs/images/icons/moveUp16x16.png";
    /**Attribute that contains the path of the move down icon*/
    public static final String ICON_ENTRY_MOVE_DOWN = "/org/symlabs/images/icons/moveDown16x16.png";
    /**Attribute that contains the path of the search manage icon*/
    public static final String ICON_SEARCH_MANAGE = "/org/symlabs/images/icons/searchManage16x16.png";
    /**Attribute that contains the path of the search manage icon*/
    public static final String ICON_SEARCH_ADD = "/org/symlabs/images/icons/searchAdd16x16.png";
    /**Attribute that contains the path of the search closeicon*/
    public static final String ICON_SEARCH_CLOSE = "/org/symlabs/images/icons/searchClose16x16.png";
    /**Attribute that contains the path of the search icon*/
    public static final String ICON_SEARCH = "/org/symlabs/images/icons/search16x16.png";
    /**Attribute that contains the path of the search folder icon*/
    public static final String ICON_SEARCH_FOLDER = "/org/symlabs/images/icons/searchFolder16x16.png";
    /**Attribute that contains the path of the refresh search icon*/
    public static final String ICON_SEARCH_REFRESH = "/org/symlabs/images/icons/searchRefresh16x16.png";
    /**Attribute that contains the path of the search properties icon*/
    public static final String ICON_SEARCH_PROPERTIES = "/org/symlabs/images/icons/searchProperties16x16.png";
    /**Attribute that contains the path of the new search icon*/
    public static final String ICON_SEARCH_NEW = "/org/symlabs/images/icons/searchNew16x16.png";
    /**Attribute that contains the path of the save search icon*/
    public static final String ICON_SEARCH_SAVE = "/org/symlabs/images/icons/searchSave16x16.png";
    /**Attribute that contains the path of the search entry icon*/
    public static final String ICON_DEFAULT_ICON = "/org/symlabs/images/icons/defaultIcon16x16.png";
    /**Attribute that contains the path of the bookmark icon*/
    public static final String ICON_BOOKMARK = "/org/symlabs/images/icons/bookmark16x16.png";
    /**Attribute that contains the path of the bookmark folder icon*/
    public static final String ICON_BOOKMARK_FOLDER = "/org/symlabs/images/icons/bookmarkFolder16x16.png";
    /**Attribute that contains the path of the manage bookmark folder icon*/
    public static final String ICON_BOOKMARK_MANAGE = "/org/symlabs/images/icons/bookmarkManage16x16.png";
    /**Attribute that contains the path of the manage bookmark folder icon*/
    public static final String ICON_BOOKMARK_ADD = "/org/symlabs/images/icons/bookmarkAdd16x16.png";
    /**Attribute that contaisn the path of the export ldif tree icon*/
    public static final String ICON_EXPORT_LDIF_TREE = "/org/symlabs/images/icons/exportLdifTree16x16.png";
    /**Attribute that contains the path of the export ldif sub tree icon*/
    public static final String ICON_EXPORT_LDIF_SUBTREE = "/org/symlabs/images/icons/exportLdifSubTree16x16.png";
    /**Attribute that contains the path of the select text icon*/
    public static final String ICON_SELECT_TEXT = "/org/symlabs/images/icons/selectText16x16.png";
    /**Attribute that cotnains the path of the copy icon*/
    public static final String ICON_COPY = "/org/symlabs/images/icons/copy16x16.png";
    /**Attribute that contains the path of the copy folder icon*/
    public static final String ICON_COPY_FOLDER = "/org/symlabs/images/icons/copyFolder16x16.png";
    /**Attribute that contains the path of the cut icon*/
    public static final String ICON_CUT = "/org/symlabs/images/icons/cut16x16.png";
    /**Attribute that contains the path of the paste icon*/
    public static final String ICON_PASTE = "/org/symlabs/images/icons/paste16x16.png";
    /**Attribute that contains the path of the schema icon*/
    public static final String ICON_SCHEMA = "/org/symlabs/images/icons/schema16x16.png";
    /**Attribute that contains the path of the schema entry icon*/
    public static final String ICON_SCHEMA_ENTRY = "/org/symlabs/images/icons/schema16x16.png";
    /**Attribute that contains the path of the root dse entry icon*/
    public static final String ICON_ROOT_DSE = "/org/symlabs/images/icons/rootDse16x16.png";
    /**Attribute that contains the path of the root dse entry icon*/
    public static final String ICON_PREFERENCES = "/org/symlabs/images/icons/preferences16x16.png";
    /**Attribute that contains the initial html code for a bold text*/
    private static final String HTML_CODE_BOLD_INIT = "<HTML><b>";
    /**Attribute that contains the final html code for a bold text*/
    private static final String HTML_CODE_BOLD_END = "</b></HTML>";
    /**Attribute that contains the html red color*/
    public static final String HTML_RED_COLOR = "#DF0101";
    /**Attribute that contains the html blue color*/
    public static final String HTML_BLUE_COLOR = "#0101DF";
    /**Attribute that contains the html green color*/
    public static final String HTML_GREEN_COLOR = "#04B404";
    /**Attribute that contains the color white grey*/
    public static Color COLOR_WHITE_GREY = new Color(180, 180, 170);
    /**Attribute that contains the defaulot color of a button*/
    public static Color DEFAULT_BUTTON_COLOR = new Color(239, 235, 231);

    /**Method that returns a hashmap with the attributes contained in the root dse
     * 
     * @param ldapEntry LDAPEntry. This is the ldap Entry where we want to get their attributes
     * @return HashMap<String, String[]>[]. This array has length 1
     */
    public static HashMap<String, String[]>[] getHashRootDseFromLDAPEntry(LDAPEntry ldapEntry) {
        int indexAttributesRootDSE = 0;
        HashMap<String, String[]>[] hash = new HashMap[1];
        hash[indexAttributesRootDSE] = new HashMap<String, String[]>();
        LDAPAttributeSet attrSet = ldapEntry.getAttributeSet();
        for (Enumeration e = attrSet.getAttributes(); e.hasMoreElements();) {
            LDAPAttribute attr = (LDAPAttribute) e.nextElement();
            String attrName = attr.getBaseName();
            String[] attrValues = attr.getStringValueArray();
            logger.trace("RootDSE, attrName:" + attrName + ",attrValues.length:" + attrValues.length);
            hash[indexAttributesRootDSE].put(attrName, attrValues);
        }

        return hash;
    }

    //NOTE: Posibilidad de cambiarlo y depender del objectclass en lugar del rdn
    //de donde coger imagenes: /usr/share/icons/crystalsvg/16x16/actions 
    /**Method that returns if we are in editable mode or we are not
     * 
     * @return boolean. True= editable mode. False= not editable mode.
     */
    public static boolean isEditableMode() {
        return editableMode;
    }

    /**Method that sets the editable mode.
     * True = editable mode.
     * Fase= no editable mode.
     * @param editableMode boolean. This is the editableMode.
     */
    public static void setEditableMode(boolean editableMode) {
        Utils.editableMode = editableMode;
    }

    /**Method that returns the full file path of the logger properties
     * 
     * @return String. This is the full file path used to get the properties of the logger
     */
    public static final String getFullPathLoggerProperties() {
        return Utils.getPreferencesPath() + File.separatorChar + Utils.loggerFile;
    }

    /**Method that returns the configurations folder. This folder contains all the configuration stored
     * 
     * @return String. This is the full path
     */
    public static final String getFullPathConfigurationsFolder() {
        return Utils.getPreferencesPath() + File.separatorChar + Utils.ConfigurationsFolder;
    }

    /**Method that returns the full path of the preferences file
     * 
     * @return String. This is the full path of preferences file
     */
    public static final String getFullPathPreferencesFile() {
        return Utils.getPreferencesPath() + File.separatorChar + Utils.PreferencesFile;
    }

    /**Method that returns the full file path of the configuration file name. This files depends of the confName given as argument.
     * 
     * @param confName String. This is the configuration file name
     * @return String.
     */
    public static final String getFullPathConfNameFile(String confName) {
        return Utils.getFullPathConfigurationsFolder() + File.separatorChar + "." + confName + Utils.configurationFile;
    }

    /**Method that returns the confName given the full path file name
     * 
     * @param pathFile String. This is the full path file name of the configuration
     * @return String. It is returned the configuration name.
     */
    public static final String getConfNameFromFullPathFile(String pathFile) {
        if (pathFile.endsWith(Utils.configurationFile)) {
            return pathFile.substring(1, pathFile.indexOf(Utils.configurationFile));
        } else {
            return null;
        }
    }

    /**Method that returns the frame of the ldap browser main window
     * 
     * @return JFrame. This is the main window of the browser
     */
    public static BrowserMainWin getMainWindow() {
        return mainWindow;
    }

    /**Method that sets the ldap browser main window
     * 
     * @param mainWin BrowserMainWin. This is the main window
     */
    public static void setMainWindow(BrowserMainWin mainWin) {
        mainWindow = mainWin;
    }

    /**Method that returns the RDN of the dn given as argument
     * 
     * @param dn String. This is the dn where we want to get the rdn
     * @return String. This is the requested rdn
     */
    public static String getRDN(String dn) {
        String rdn = null;
        int index = dn.indexOf(",");
        if (index != -1) {
            rdn = dn.substring(0, index);
        } else {
            rdn = dn;
        }
        return rdn;
    }

    /**Method that returns the value of an rdn given.
     * Example: "dc=symlabs" it would be returned "symlabs"
     * 
     * @param rdn String. This is the rdn
     * @return String. This is the name to return
     */
    public static String getValueFromRdn(String rdn) {
        String name = null;
        int index = rdn.indexOf("=");
        if (index != -1) {
            name = rdn.substring(index + 1, rdn.length());
        } else {
            name = rdn;
        }
        return name;
    }

    /**Method that returns the DsAction of the string given by the argument
     * 
     * @param ActionID String. This is the hashkey of the DsAction
     * @return DsAction. It is returned the DsAction which you are asking for
     */
    public static DsAction getAction(String ActionID) {
        return mainWindow.getAction(ActionID);
    }

    /**Method that returns a tree with the rootnode, the naming context, the middle nodes (as virtual nodes) and the result nodes.
     * This method is used to create the tree of a search.
     * 
     * @param resultsVector<String>. It contains the results of the search
     * @param treeRootNode TreeRootNode. This is the root of the tree which will contains the search results
     * @return TreeRootNode. It is returned the treeRootNode given as argument with the search results
     * @throws netscape.ldap.LDAPException
     */
    private static TreeSearchRootNode getTreeFromLDAPSearchResults(Vector<String> results, TreeSearchRootNode treeRootNode) throws LDAPException {
        logger.trace("getTreeFromLDAPSearchResults, childcount:" + treeRootNode.getChildCount() + "," + treeRootNode + ",TOTAL RESULTS: " + results.size());
        for (int i = 0; i < results.size(); i++) {
            String dn = results.elementAt(i);
            treeRootNode.addSearchResults(dn);
            logger.trace("results, entry:" + dn);

            LDAPNode namingContextNode = Utils.containsRootNodeDnToSearch(dn, treeRootNode);
            logger.trace("namingContext: " + namingContextNode.myDN + ",childcount:" + namingContextNode.getChildCount());
            LDAPNode bestParentNode = Utils.getTheBestParentNode(dn, namingContextNode, namingContextNode);
            if (bestParentNode == null) {
                bestParentNode = namingContextNode;
                logger.trace("best parent node=null, we set as best parentnode: " + namingContextNode.myDN);
            } else {
                logger.trace("best parent node returned: " + bestParentNode.myDN);
            }
            //We get the virtual nodes between de suffix and the results an finally we get the result nodes of the search
            LDAPNode virtualNode = Utils.getNodesBetweenDnAndLDAPNode(dn, bestParentNode, true);
            logger.trace("virtualNode:" + virtualNode.myDN);
            bestParentNode.add(virtualNode);
        }
        logger.trace("We show the ldap nodes");
        treeRootNode.showLDAPNodes();

        return treeRootNode;
    }

    /**Method that returns the best parent node found for the dn searched. 
     * Before call this method it must be called the method containsRootNodeDnToSearch
     * 
     * @param dnToSearch String. This is the dn to search between the child nodes
     * @param ldapNode LDAPNode. This is the base from the search starts. So this LDAPNode must have the same base
     * @param bestLDAPNode LDAPNode
     * @return LDAPNode. If it is not found a best parent node then it is returned null.
     * It means that the node that better matches with the dnToSearch is the ldapNode given as argument. 
     * This ldapNode should be one of the naming context.
     * @see containsRootNodeDnToSearch(String dnToSearch, LDAPNode ldapNode)
     */
    public static LDAPNode getTheBestParentNode(String dnToSearch, LDAPNode ldapNode, LDAPNode bestLDAPNode) {
        int index = -1;
        int best = -1;
        int childNumber = -1;
        logger.trace("We are getting the best parent node, dn:" + dnToSearch + ",node:" + ldapNode.myDN);
        for (int i = 0; i < ldapNode.getChildCount(); i++) {
            LDAPNode childNode = (LDAPNode) ldapNode.getChildAt(i);
            logger.trace("dnToSearch:" + dnToSearch + ",childNode:" + childNode.myDN);
            index = dnToSearch.indexOf(childNode.myDN);
            logger.trace("index:" + index + ",best:" + best + ",childNumber:" + childNumber);
            if (index > best) {
                best = index;
                childNumber = i;
            }
        }
        logger.trace("index:" + index + ",best:" + best + ",childNumber:" + childNumber);
        if (childNumber != -1) {
            return Utils.getTheBestParentNode(dnToSearch, (LDAPNode) ldapNode.getChildAt(childNumber), (LDAPNode) ldapNode.getChildAt(childNumber));
        } else {
            return bestLDAPNode;
        }
    }

    /**Method that returns the nodes between the dn and the node given as argument.
     * This method creates a tree of nodes with the nodes needed to show the origDn given as argument.
     * It is returned the root node of this tree.
     * 
     * @param origDn String. This is the original dn, that we wants to have as a leaf of this tree
     * @param rootNode LDAPNode. This is the rootNode from the tree should start. 
     * This node should be the result of calling the method getTheBestParentNode.
     * @param withNodesAsVirtualNodes boolean. True - return the middle nodes as virtual nodes. False - return the middle nodes as LDAPNodes
     * @return LDAPNode. It is returned the node that it should be added to the ldapNode given as argument.
     * @see getTheBestParentNode(String dnToSearch, LDAPNode ldapNode) 
     * @see containsRootNodeDnToSearch(String dnToSearch, LDAPNode ldapNode)
     */
    public static LDAPNode getNodesBetweenDnAndLDAPNode(String origDn, LDAPNode rootNode, boolean withNodesAsVirtualNodes) {
        logger.trace("getNodesBetweenDnAndNamingContext, dn:" + origDn + ",rootNode:" + rootNode.myDN);
        //We get the dn between dnToAdd and the naming context given as argument
        ArrayList<String> attrList = new ArrayList<String>();
        String tmpDn = origDn.substring(origDn.indexOf(",") + 1, origDn.length());
        while (!tmpDn.equalsIgnoreCase(rootNode.myDN)) {
            logger.trace("Dn to add:" + tmpDn);
            attrList.add(tmpDn);
            logger.trace("index: " + tmpDn.indexOf(",") + "length:" + tmpDn.length());
            tmpDn = tmpDn.substring(tmpDn.indexOf(",") + 1, tmpDn.length());
            logger.trace("dn:" + tmpDn);
        }

        for (int i = 0; i < attrList.size(); i++) {
            logger.trace("dn attrList[" + i + "]:" + attrList.get(i));
        }

        //We create the virtual nodes between both nodes: namingContext and dn node
        logger.trace("size:" + attrList.size());
        LDAPNode auxParentNode = null;
        LDAPNode auxRootNode = null;
        LDAPNode childNode = null;
        LDAPEntry entry = null;
        for (int i = attrList.size() - 1; i >= 0; i--) {
            try {
                if (!withNodesAsVirtualNodes) {
                    LDAPSearchResults results = ((TreeRootNode) rootNode.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(attrList.get(i));
                    if (results.getCount() == 1) {
                        entry = results.next();
                    }
                    childNode = new LDAPNode(entry, false);//it is an ldap  node
                } else {
                    String dn = attrList.get(i);
                    String rdn = Utils.getRDN(dn);
                    childNode = new VirtualNode(rdn);//it is a virtual node
                    childNode.myDN = dn;

                }
                if (auxParentNode == null) {
                    auxParentNode = childNode;
                    auxRootNode = auxParentNode;
                    logger.trace("rootNode:" + auxRootNode.myDN);
                } else {
                    auxParentNode.addChild(childNode);
                    logger.trace("added:" + childNode.myDN);
                    logger.trace("parentNode:" + auxParentNode.myDN);
                    auxParentNode = childNode;
                    logger.trace("new parentNode:" + auxParentNode.myDN);
                }
            } catch (Exception e) {
                String message = "Error searching the selected dn: " + tmpDn + " in the ldap server.";
                String title = "Error Looking For Bookmark";
                MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, MessageDialog.MESSAGE_ERROR);
                dialog.setLocationRelativeTo(Utils.getMainWindow());
                dialog.setVisible(true);
                logger.error(message + " " + e);
                return null;
            }
        }

        //We create the dn node, and add it to the virtual node
        try {
            LDAPSearchResults results = ((TreeRootNode) rootNode.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(origDn);
            if (results.getCount() == 1) {
                entry = results.next();
            }

        } catch (Exception e) {
            String message = "Error searching the selected dn: " + origDn + " in the ldap server.";
            String title = "Error Looking For Bookmark";
            MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(Utils.getMainWindow());
            dialog.setVisible(true);
            logger.error(message + " " + e);
            return null;
        }

        LDAPNode myNewNode = new LDAPNode(entry, false);
        logger.trace("mynewnode:" + myNewNode.myDN + ",rdn:" + myNewNode.myRDN);
        if (childNode != null) {
            childNode.addChild(myNewNode);
        } else {
            auxRootNode = myNewNode;
        }
        logger.trace("auxRootNode:" + auxRootNode.myDN + ",childcount:" + auxRootNode.getChildCount());
        return auxRootNode;
    }

    /**Method that checks if the naming context that contains the dnToSearch is loaded in the rootNode.
     * If it is loaded then it is returned. If it is not loaded then it is returned null.
     * 
     * @param dnToSearch String. This is the dn to search in the ldap nodes
     * @param rootNode LDAPnode. This is the rootNode of the tree displayed in the main window
     * @return LDAPNode. This is the node that matches with the dnToSearch.
     */
    public static LDAPNode getLDAPNodeByDnCheckingNamingContexts(String dnToSearch, LDAPNode rootNode) {
        LDAPNode foundNode = Utils.containsRootNodeDnToSearch(dnToSearch, rootNode);
        if (foundNode != null) {
            return Utils.findNodeByDN(dnToSearch, rootNode);
        }
        return null;
    }

    /**Method that search the dn in the rootNode, it is only matched the end of the dn
     * 
     * @param dnToSearch String.
     * @param ldapNode LDAPNode.
     * @return LDAPNode. 
     */
    public static LDAPNode containsRootNodeDnToSearch(String dnToSearch, LDAPNode ldapNode) {
        logger.trace("AQUI 1, childcount:" + ldapNode.getChildCount() + ", dnToSearch:" + dnToSearch + ",ldapNode:" + ldapNode);
        if (ldapNode != null) {
            if (ldapNode.getChildCount() > 0) {
                for (int i = 0; i < ldapNode.getChildCount(); i++) {
                    LDAPNode childNode = (LDAPNode) ldapNode.getChildAt(i);
                    logger.trace("AQUI 2 childNode:" + childNode.myDN + ",dnToSearch:" + dnToSearch);
                    if (dnToSearch.endsWith(childNode.myDN)) {
                        logger.trace("AQUI 3, " + ldapNode.getChildCount());
                        if (childNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
                            if (dnToSearch.endsWith(childNode.myDN)) {
                                logger.trace("AQUI 4, node:" + childNode.myDN);
                                return childNode;
                            }
                        } else {//the node is a VirtualNode or a TreeRootNode
                            logger.trace("AQUI 6, it is a virtual node or a tree root node");
                            return Utils.containsRootNodeDnToSearch(dnToSearch, childNode);
                        }
                    } else{//It means that this node has not matched with anyone, so we have to create a new virtual node as a child of ldapNode
                        return ldapNode;
                    }
                }//end for
            } else if (ldapNode.getChildCount() == 0) {
                if ((ldapNode.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE) || ldapNode instanceof VirtualNode) && dnToSearch.endsWith(ldapNode.myDN)) {
                    return ldapNode;
                }
            }
        }
        logger.trace("AQUI 7");
        return null;
    }

    /**Method that returns the ldap node which dn matches with the dnToSearch.
     * 
     * @param dnToSearch String. This is the dn to search.
     * @param rootNode LDAPNode. 
     * @return LDAPNode.This is the ldapNode that matches with dnToSearch.
     * It is returned null if no matches is found.
     */
    public static LDAPNode findNodeByDN(String dnToSearch, LDAPNode rootNode) {
        logger.trace("We are in findNodeByDN, dnToSearch:" + dnToSearch + ",rootNode.dn:" + rootNode.myDN);
        if (rootNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            if (dnToSearch.endsWith(rootNode.myDN)) {
                for (int i = 0; i < rootNode.getChildCount(); i++) {
                    LDAPNode node = (LDAPNode) rootNode.getChildAt(i);
                    logger.trace("dnToSearch:" + dnToSearch + ",node.dn:" + node.myDN);
                    if (dnToSearch.endsWith(node.myDN)) {
                        if (node.myDN.equals(dnToSearch)) {
                            return node;
                        } else if (node.getChildCount() > 0) {
                            return Utils.findNodeByDN(dnToSearch, node);
                        }
                    }
                }
            }
        } else {
            if (dnToSearch.equals(rootNode.myDN)) {
                return rootNode;
            } else if (rootNode.getChildCount() > 0) {
                for (int i = 0; i < rootNode.getChildCount(); i++) {
                    LDAPNode node = (LDAPNode) rootNode.getChildAt(i);
                    if (dnToSearch.endsWith(node.myDN)) {
                        if (node.myDN.equals(dnToSearch)) {
                            return node;
                        } else if (node.getChildCount() > 0) {
                            return Utils.findNodeByDN(dnToSearch, node);
                        }
                    }
                }
            }
        }
        logger.trace("findNodeByDN: IT RETURNS NULL");
        return null;
    }

    /**Method that creates the preferences folder.
     * If the preferences folder does not exist it will be created, 
     * if it exists then it does nothing.
     * 
     */
    private static String getPreferencesPath() {
        String prefFolder = Utils.UserFolder + File.separatorChar + Utils.PreferencesFolder;

        //It is only executed once, the first time this method is called
        if (Utils.firstAccessToFolderPreferences) {
            File dir = new File(prefFolder);
            dir.mkdir();
            //We mark this parameter to false to set this folder as created
            Utils.firstAccessToFolderPreferences = false;
        }

        return prefFolder;
    }

    /**Method that returns the rdn attribute. 
     * Example: "ou=people,dc=symlabs, dc= com", it will be returned "ou"
     * 
     * @param rdn String. This is the rdn attribute
     * @return String. This is the rdn attribute
     */
    public static String getRDNAttribute(String rdn) {
        String attr = null;
        int index = rdn.indexOf("=");
        if (index != -1) {
            attr = rdn.substring(0, index);
        }

        return attr;
    }

    /**Method that recieves an ldap entry and return a hashmap String,String[] in format: attribute, values[]
     * 
     * @param ldapEntry LDAPEntry. This is the ldap entry where we want to get its attributes and values
     * @param rdn String. This is the ldap entry
     * @return HashMap String, String[]. It is returned an hashmap, where each record contains an attributes with its values.
     */
    public static HashMap<String, String[]>[] getHashFromLDAPEntry(LDAPEntry ldapEntry, String rdn) {
        LDAPAttributeSet ldapAttributeSet = ldapEntry.getAttributeSet(); //Here we have all attributes of this entry
        HashMap<String, String[]> attributes = new HashMap<String, String[]>();
        HashMap<String, String[]> objectClasses = new HashMap<String, String[]>();
        HashMap<String, String[]> dnAttribute = new HashMap<String, String[]>();
        HashMap<String, String[]> hash[] = new HashMap[3];

        String rdnAttribute = null;
        if (rdn.equalsIgnoreCase("rootDSE")) {
            rdnAttribute = Utils.getRDNAttribute(Utils.getRDN(ldapEntry.getDN()));
        } else {
            rdnAttribute = Utils.getRDNAttribute(rdn);
        }

        for (Enumeration e = ldapAttributeSet.getAttributes(); e.hasMoreElements();) {
            LDAPAttribute attr = (LDAPAttribute) e.nextElement();
            if (attr != null) {
                String name = attr.getBaseName();
                String values[] = attr.getStringValueArray();
                if (name.equalsIgnoreCase(rdnAttribute)) {
                    dnAttribute.put(name, values);
                } else if (name.equalsIgnoreCase(Schema.OBJECTCLASS_KEY)) {
                    objectClasses.put(name, values);
                } else {
                    attributes.put(name, values);
                }

            }
        }
        hash[0] = dnAttribute;
        hash[1] = objectClasses;
        hash[2] = attributes;
        return hash;
    }

    /**Method that returns an LDAPAttributeSet with the attributes contained in the hash given as argument
     * 
     * @param hashMap HashMap <String,String[]> hashMap[]. This hash contains the attributes 
     * @return LDAPAttributeSet
     */
    public static LDAPAttributeSet getLDAPAttributeSetFromHash(HashMap<String, String[]> hashMap[]) {
        LDAPAttribute ldapAttribute = null;
        LDAPAttributeSet ldapAttributeSet = null;
        String attribute = "";
        String[] attrValues = null;

        //We read all the attributes
        for (int i = 0; i < hashMap.length; i++) {
            if (i != LDAPNode.INDEX_DN) {//To avoid to add the attribute with the dn
                Iterator it = hashMap[i].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    attribute =
                            (String) entry.getKey();
                    attrValues =
                            (String[]) entry.getValue();
                    ldapAttribute =
                            new LDAPAttribute(attribute, attrValues);

                    //Add the attribute 
                    if (ldapAttributeSet == null) {
                        ldapAttributeSet = new LDAPAttributeSet();
                    }

                    ldapAttributeSet.add(ldapAttribute);
                }

            }
        }
        return ldapAttributeSet;
    }

    /**Method that returns a string with the ldap entry in format ldif
     * 
     * @param ldapNode LDAPNode.
     * @return String. This is the ldif text
     */
    public static String getLdifFromLDAPEntry(LDAPNode ldapNode) {
        if (ldapNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            try {
                StringBuffer tmpldif = new StringBuffer();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                LDAPEntry entry = ldapNode.getLDAPEntry();
                PrintWriter printWriter = new PrintWriter(baos);
                LDIFWriter ldifWriter = new LDIFWriter(printWriter);
                ldifWriter.printEntry(entry);
                printWriter.close();
                tmpldif.append(baos);

                return tmpldif.toString();
            } catch (IOException ex) {
                logger.error("Error getting the ldif of the ldap entry: " + ldapNode.myDN + " " + ex);
                return "";
            }
        } else {
            return "";
        }
    }

    /**Method that returns the image for the node given as argument
     * 
     * @param ldapNode LDAPNode. This is the node that we want to get its icon path
     * @return String. It is returned the icon path for the node given as argument
     */
    public static String getImageIconPathForNode(LDAPNode ldapNode) {
        String path = Utils.ICON_DEFAULT_NODE;
        if (ldapNode instanceof TreeRootNode) {
            path = Utils.ICON_ROOT_NODE;
        } else if (ldapNode instanceof VirtualNode) {
            path = Utils.ICON_VIRTUAL_NODE;
        } else {
            if (ldapNode.myRDN != null && !ldapNode.myRDN.equals("")) {
                String prefix = ldapNode.myRDN.substring(0, ldapNode.myRDN.indexOf("="));
                if (prefix.equalsIgnoreCase("DC")) {
                    path = Utils.ICON_DC_NODE;
                } else if (prefix.equalsIgnoreCase("OU")) {
                    path = Utils.ICON_OU_NODE;
                } else if (prefix.equalsIgnoreCase("C")) {
                    path = Utils.ICON_C_NODE;
                } else if (prefix.equalsIgnoreCase("CN")) {
                    path = Utils.ICON_CN_NODE;
                } else if (prefix.equalsIgnoreCase("UID")) {
                    path = Utils.ICON_UID_NODE;
                }
            }
        }
        return path;
    }

    /**Method that returns the same attribute in bold. It is added an html label.
     * 
     * @param attributeName String. This is the attribute name.
     * @return String. It is returned the same attribute in bold. It is added an html label
     */
    public static String getAttributeInBold(String attributeName) {
        return Utils.HTML_CODE_BOLD_INIT + attributeName + Utils.HTML_CODE_BOLD_END;
    }

    /**Method that returns the attribute given as argument without the bold html label
     * 
     * @param attributeInBold String. This attribute contains the attribute with html labels
     * @return String. It is returned the same attribute without the html labels
     */
    public static String getAttributeWithoutBold(String attributeInBold) {
        String attributeName = "";
        if (attributeInBold.indexOf(Utils.HTML_CODE_BOLD_INIT) != -1) {
            attributeName = attributeInBold.substring(Utils.HTML_CODE_BOLD_INIT.length(), attributeInBold.indexOf(Utils.HTML_CODE_BOLD_END));
        } else {
            attributeName = attributeInBold;
        }

        return attributeName;
    }

    /**Method that renames the node given as argument with the rdn given as argument
     * 
     * @param ldapNode LDAPNode. This is the ldap node to add
     * @param newRdn String. This is the rdn to set to the new ldap node
     * @throws LDAPException Exception
     */
    public static void renameLDAPNode(LDAPNode ldapNode, String newRdn) throws LDAPException {
        String errorMsg = "";
        try {
            LDAPConnection connection = ((TreeRootNode) ldapNode.getRoot()).getLdapServer().getConnection(true);
            String dn = ldapNode.myDN;
            String newDn = dn.substring(dn.indexOf(","), dn.length());
            newDn =
                    newRdn + newDn;
            connection.rename(dn, newRdn, true);
            LDAPSearchResults results = ((TreeRootNode) ldapNode.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(newDn);
            LDAPEntry ldapEntry = null;
            try {
                ldapEntry = results.next();
                ldapNode.setLDAPEntry(ldapEntry, false);
            } catch (LDAPException e) {
                errorMsg = "Error renaming the rdn attribute for node:" + dn + "\n" + e + "\n";
                logger.error(errorMsg);
                throw e;
            }

        } catch (LDAPException e) {
            errorMsg += "Failed to rename the specified entry. " + "\n" + e + "\n";
            logger.error(errorMsg);
            throw e;
        }

    }

    /**Method that returns the all first node founds in the tree given by the root
     * 
     * @param root LDAPNode. This is the node where the search starts
     * @param vector Vector <LDAPNode>. This is the vector where the nodes are added
     * @return Vector <LDAPNode> 
     */
    public static Vector<LDAPNode> getAllFirstNodes(LDAPNode root, Vector<LDAPNode> vector) {
        if (vector == null) {
            vector = new Vector<LDAPNode>();
        }

        if (root.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            vector.addElement(root);
            if (root.getChildCount() > 0) {
                vector = Utils.getAllFirstNodes((LDAPNode) root.getChildAt(0), vector);
            }

        } else if (root.getChildCount() > 0) {
            vector = Utils.getAllFirstNodes((LDAPNode) root.getChildAt(0), vector);
        }

        return vector;
    }

    /**Method that returns the all first node names found in the tree given by the root
     * 
     * @param root LDAPNode. This is the node where the search starts
     * @param vector Vector <LDAPNode>. This is the vector where the nodes are added
     * @return Vector <LDAPNode> 
     */
    public static Vector<String> getAllFirstNodeNames(LDAPNode root, Vector<String> vector) {
        if (vector == null) {
            vector = new Vector<String>();
        }

        if (root.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            vector.addElement(root.myDN);
            if (root.getChildCount() > 0) {
                return Utils.getAllFirstNodeNames((LDAPNode) root.getChildAt(0), vector);
            }

        } else if (root.getChildCount() > 0) {
            vector = Utils.getAllFirstNodeNames((LDAPNode) root.getChildAt(0), vector);
        }

        return vector;
    }

    /**Method that returns a vector like vAllValues but it does not contain the elements given by vValuesToRemove
     * 
     * @param vAllValues Vector<String>. It contains all the elements 
     * @param vValuesToRemove Vector<String>. It contains the elements to remove
     * @return Vector<String>. Vector without the elements to remove
     */
    public static Vector<String> getVectorRemovingValues(Vector<String> vAllValues, Vector<String> vValuesToRemove) {
        //We create a new vector with the same elements
        Vector<String> vNewAllAttr = new Vector<String>();
        for (int i = 0; i < vAllValues.size(); i++) {
            vNewAllAttr.addElement(vAllValues.elementAt(i));
        }

        //We remove the elements from the new vector
        if (vValuesToRemove != null) {
            for (int i = 0; i < vValuesToRemove.size(); i++) {
                logger.trace("We remove the element: " + vValuesToRemove.elementAt(i) + ", is found the element: " + vNewAllAttr.contains(vValuesToRemove.elementAt(i)));
                vNewAllAttr.remove(vValuesToRemove.elementAt(i));
            }
        }
        return vNewAllAttr;
    }

    /**Method that returns a vector with the same elements of vAllValues and it also contains the elements of the vector vValuesToAdd.
     * If the element to add already exist then it is not added.
     * 
     * @param vAllValues Vector <String>. This vector contains all values
     * @param vValuesToAdd Vector <String>. This vector contains the elements to add
     * @return Vector <String>.
     */
    public static Vector<String> getVectorAddingValues(Vector<String> vAllValues, Vector<String> vValuesToAdd) {
        Vector<String> vNewAttr = new Vector<String>();
        for (int i = 0; i < vAllValues.size(); i++) {
            vNewAttr.addElement(vAllValues.elementAt(i));
        }

        if (vValuesToAdd != null) {
            for (int i = 0; i < vValuesToAdd.size(); i++) {
                logger.trace("Checking value to add: " + vValuesToAdd.elementAt(i) + ", is found the element:" + vNewAttr.contains(vValuesToAdd.elementAt(i)));
                if (!vNewAttr.contains(vValuesToAdd.elementAt(i))) {
                    vNewAttr.addElement(vValuesToAdd.elementAt(i));
                }

            }
        }
        return vNewAttr;
    }

    /**Method that creates a new ldap entry in the dn and with attributes given.
     * And adds this ldap entry as a child of the parentNode given as argument.
     * This node will be added to the current ldap node
     * 
     * @param dn String. This is the dn of the new node
     * @param attributes LDAPAttributesSet. These are the attributes of the new node
     * @param parentNode LDAPNode. This is the parent node to add this new node
     * @throws netscape.ldap.LDAPException Exception. If an error happens when creating the ldap node then it is thrown an ldap exception
     */
    public static void createNewLDAPEntry(String dn, LDAPAttributeSet attributes, LDAPNode parentNode) throws LDAPException {
        //We create the entry
        LDAPEntry entry = new LDAPEntry(dn, attributes);

        LDAPConnection connection = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer().getConnection(true);

        //We add the entry to the ldap server
        connection.add(entry);

        //Create the LDAPNode
        LDAPNode newLDAPNode = new LDAPNode(entry, false);

        //Add the new ldap node
        parentNode.addChild(newLDAPNode);

        newLDAPNode.refreshNode();
    }

    /**Method that creates a new ldap entry
     * 
     * @param rdn String. This is the rdn of the new node
     * @param parentDn String. This is the parent dn of the new node
     * @param attributes LDAPAttributesSet. These are the attributes of the new ldap node
     * @throws netscape.ldap.LDAPException Exception is thrown in case of error
     */
    public static void createNewLDAPEntry(String rdn, String parentDn, LDAPAttributeSet attributes) throws LDAPException {
        //We set the dn of the new node
        String dn = rdn + "," + parentDn;

        //We get the parent node
        LDAPNode parentNode = Utils.getMainWindow().getCurrentBrowserPanel().searchNodeInTreeRootNode(parentDn);

        //We create the new ldap entry
        Utils.createNewLDAPEntry(dn, attributes, parentNode);
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path String wih the full path of the resource to load as an Icon
     * @return new ImageIcon, or null if the path was invalid.
     */
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.error("Couldn't find file: " + path);
            imgURL = Utils.class.getResource(Utils.ICON_DEFAULT_ICON);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                logger.error("Couldn't find file: " + path);
                return null;
            }
        }
    }

    /**Method that saves the configuration of the ldapServer given as argument
     * 
     * @param ldapServer LDAPServer. This is the configuration that we want to store
     */
    public static void saveTab(LDAPServer ldapServer) {
        String confName = ldapServer.getConnectionData().getConfigurationName();
        String errorMessage = "";
        //It there is not configuration name, then we set the default configuraiton name
        if (confName == null || confName.trim().equals("")) {
            confName = ldapServer.getDefaultConfigurationName();
            ldapServer.setConfigurationName(confName);
        }

        try {
            ConnectionData.saveConnectionData(ldapServer);
            ldapServer.getConnectionData().setDirty(false);
        } catch (Exception ex) {
            errorMessage += "Error saving configuration: " + ex.getMessage() + "\n";
            logger.error(errorMessage);
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            String message = "Error trying to save the configuration: " + confName;
            String title = "Data Error";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, message, errorMessage, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }

    /**Method that updates the tree search root node given as argument
     * 
     * @param searchRootNode TreeSearchRootNode. This is the tree search root node that we want to refresh
     * @param selectedNode LDAPNode. This is the ldap node to set as selected
     * @throws netscape.ldap.LDAPException
     */
    public static void refreshTreeSearchRootNode(TreeSearchRootNode searchRootNode, LDAPNode selectedNode) throws LDAPException {

        //We get the results of the search
        LDAPSearchResults results = searchRootNode.getLdapServer().getLdapOperation().LDAPSearch(searchRootNode.getSearchNode().getSearchParams());

        logger.trace("before load suffixes, search results: " + results.getCount());

        Vector<String> vSearchResults = new Vector<String>();
        while (results.hasMoreElements()) {
            LDAPEntry entry = results.next();
            logger.trace("Adding search results:" + entry.getDN());
            vSearchResults.addElement(entry.getDN());
        }

        searchRootNode.setSearchResults(vSearchResults);

        //We load the suffix
        searchRootNode.loadLDAPSuffix();

        logger.trace("after load suffixes: ");
        searchRootNode.showLDAPNodes();
        logger.trace("");

        for (int i = 0; i < searchRootNode.getChildCount(); i++) {
            LDAPNode node = (LDAPNode) searchRootNode.getChildAt(i);
            logger.trace("suffix, child(" + i + "):" + node.myDN + ",total childs:" + node.getChildCount());
        }

        logger.trace("SEARCH, CREATING VIRTUAL NODES, child count:" + searchRootNode.getChildCount());

        //We get the tree of the results found for this search
        searchRootNode = Utils.getTreeFromLDAPSearchResults(vSearchResults, searchRootNode);

        logger.trace("before creating search nodes of tree");

        //We get the tab by the title
        BrowserPanel myPanel = Utils.getMainWindow().getBrowserPanelByTitle(searchRootNode.getLdapServer().getConnectionData().getConfigurationName());

        logger.trace("refreshTreeSearchRootNode 1");

        //We set the tree to my panel
        myPanel.setTreeData(searchRootNode);

        logger.trace("refreshTreeSearchRootNode 2");

        //We set the rdn name for this root node
        searchRootNode.myRDN = searchRootNode.getSearchNode().getName();

        logger.trace("refreshTreeSearchRootNode 3");

        //We set the naming context nodes as a virtual nodes
        String[] suffixes = searchRootNode.getLdapServer().getSuffixes();
        for (int i = 0; i < suffixes.length; i++) {
            LDAPNode suffixNode = Utils.findNodeByDN(suffixes[i], searchRootNode);
            if (suffixNode != null) {
                logger.trace("Suffix: " + suffixNode.myDN + ",type" + suffixNode.type);
            }
        }

        logger.trace("refreshTreeSearchRootNode 4");

        //We select the node
        searchRootNode.getTree().setSelectionPath(new TreePath(selectedNode.getPath()));

        logger.trace("refreshTreeSearchRootNode 5");
    }

    /**Method that sets the tree root node given as argument as the tree displayed in the browser panel
     * 
     * @param browserPanel BrowserPanel. This is the browser panel that shows the tree
     * @param treeRootNode TreeRootNode. This is the treeRootNode that we want to show
     * @param selectedNode LDAPNode. This is the selected node in the tree
     */
    public static void setTreeRootNodeInBrowser(BrowserPanel browserPanel, TreeRootNode treeRootNode, LDAPNode selectedNode) {
        //We set the tree to my panel
        browserPanel.setTreeData(treeRootNode);

        treeRootNode.getTree().setSelectionPath(new TreePath(selectedNode.getPath()));
    }

    /**Method that returns the ldif text of the nodes given as argument and their children
     * 
     * @param subTreeNode LDAPNode. This is the node there the ldif text starts
     * @param ldifWriter LdifWriter. This is the output stream used to store the ldif information
     * @return String. This is the ldif text
     * @throws netscape.ldap.LDAPException 
     */
    private static LDIFWriter getLdifWriterOfSubTree(LDAPNode subTreeNode, LDIFWriter ldifWriter) throws LDAPException, IOException {
        logger.trace("We are getting the subTree of: " + subTreeNode.myDN);
        TreeRootNode rootNode = (TreeRootNode) subTreeNode.getRoot();
        ArrayList<LDIFRecord> records = new ArrayList<LDIFRecord>();
        if (subTreeNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            LDAPSearchResults results = rootNode.getLdapServer().getLdapOperation().getAllEntriesBaseDN(subTreeNode.myDN);
            while (results.hasMoreElements()) {
                LDAPEntry entry = results.next();
                logger.trace("entry:" + entry.getDN());
                //records.add(Utils.getLdifRecord(entry));
                ldifWriter.printEntry(entry);
            }
            for (int i = 0; i < records.size(); i++) {
                logger.trace("Results: " + records.get(i).getDN() + ",value" + records.get(i).toString());
            }
        } else { //subTreeNode must be a virtual node or a tree root node
            for (int i = 0; i < subTreeNode.getChildCount(); i++) {
                LDAPNode childNode = (LDAPNode) subTreeNode.getChildAt(i);
                if (childNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
                    ldifWriter = Utils.getLdifWriterOfSubTree(childNode, ldifWriter);
                } else {
                    for (int j = 0; j < childNode.getChildCount(); j++) {
                        LDAPNode node = (LDAPNode) childNode.getChildAt(j);
                        if (childNode.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
                            ldifWriter = Utils.getLdifWriterOfSubTree(node, ldifWriter);
                        } else {
                            logger.error("This case never should happens!");
                        }
                    }
                }
            }
        }
        return ldifWriter;
    }

//    /**Method that returns the default full path to export an ldif Tree
//     * 
//     * @return String. 
//     */
//    public static String getDefaultFullPathToExportLdifTree() {
//        return Utils.DEFAULT_EXPORT_LDIF_TREE_FOLDER + File.separatorChar + Utils.DEFAULT_EXPORT_LDIF_TREE_FILE;
//    }
//
//    /**Method that returns the default full path to export an ldif sub tree
//     * 
//     * @return String.
//     */
//    public static String getDefaultFullPathToExportLdifSubTree() {
//        return Utils.DEFAULT_EXPORT_LDIF_SUBTREE_FOLDER + File.separatorChar + Utils.DEFAULT_EXPORT_LDIF_SUBTREE_FILE;
//    }
//
//    /**Method that returns the full path to export an ldif tree
//     * 
//     * @param confName String. This is the configuration name
//     * @return String
//     */
//    public static String getDefaultFullFileNameToExportLdifTree(String confName) {
//        return confName + Utils.DEFAULT_EXPORT_LDIF_TREE_FILE;
//    }
//
//    /**Method that returns the full path to export an ldif subtree
//     * 
//     * @param confName String. This is the configuration name
//     * @return String
//     */
//    public static String getDefaultFullFileNameToExportLdifSubTree(String confName) {
//        return confName + Utils.DEFAULT_EXPORT_LDIF_SUBTREE_FILE;
//    }
    /**Method that prints at the standard output the content of the hash given as argument
     * 
     * @param myHash HashMap <String,String[]>. This is the hash that contains the attributes what we want to show at the standard output
     */
    public static void printHash(HashMap<String, String[]>[] myHash) {
        if (myHash != null) {
            for (int i = 0; i < myHash.length; i++) {
                if (myHash[i] != null) {
                    Iterator it = myHash[i].entrySet().iterator();
                    while (it.hasNext()) {
                        //Add the attribute 
                        Map.Entry entry = (Map.Entry) it.next();
                        String attributeName = (String) entry.getKey();
                        String[] values = (String[]) entry.getValue();
                        for (int j = 0; j < values.length; j++) {
                            System.out.println("attribute:" + attributeName + ",value:" + values[j]);
                        }
                    }
                }
            }
        }
    }

    /**Method that returns a copy of the hashmap given as argument
     * 
     * @param hashToCopy  HashMap<String, String[]>[] This is the hash that we want to make a copy
     * @return HashMap<String, String[]>[] This is the copy returned
     */
    public static HashMap<String, String[]>[] getCopyOfHashMap(HashMap<String, String[]>[] hashToCopy) {
        HashMap<String, String[]>[] copy = null;
        if (hashToCopy != null) {
            copy = new HashMap[hashToCopy.length];
            for (int i = 0; i < hashToCopy.length; i++) {
                copy[i] = new HashMap<String, String[]>();
                Iterator it = hashToCopy[i].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String attributeName = (String) entry.getKey();
                    String[] attrValues = (String[]) entry.getValue();
                    copy[i].put(attributeName, attrValues);
                }
            }
        }
        return copy;
    }

    /**Method that returns a vector with the attributes contained in the index of the hashmap
     * 
     * @param hash HashMap <String,String[]>. This hashmap contains the attributes required
     * @param index int. This is the index of the hashmap
     * @return Vector <String>. It is returned the attributes contained in the hash given by the index
     */
    public static Vector<String> getVectorAttributesFromHashMap(HashMap<String, String[]>[] hash, int index) {
        Vector<String> attrs = new Vector<String>();
        Iterator it = hash[index].entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String attrName = (String) entry.getKey();
            String[] attrValues = (String[]) entry.getValue();
            if (index == LDAPNode.INDEX_OBJECTCLASSES) {
                for (int i = 0; i < attrValues.length; i++) {
                    attrs.addElement(attrValues[i]);
                }
            } else {
                attrs.addElement(attrName);
            }
        }
        return attrs;
    }

    /**Method that creates a file to export the ldif information of the ldap node given as argument
     * 
     * @param ldapNode LDAPNode. This is the rootNode where we want to start to get the LDIF
     * @throws java.io.IOException
     * @throws netscape.ldap.LDAPException 
     */
    public static void createExportLdifFile(LDAPNode ldapNode) throws IOException, LDAPException {

        JFileChooser fc = new JFileChooser(Utils.getFullPathConfigurationsFolder());

        fc.setDialogTitle("Select Destination Path");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(Utils.getMainWindow());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File outPath = fc.getSelectedFile();
            File outFullPath = null;
            if (!outPath.getName().endsWith(".ldif")) {
                outFullPath = new File(outPath.getPath() + ".ldif");
            } else {
                outFullPath = outPath;
            }
            boolean createFile = true;
            logger.trace("outFullPath:" + outFullPath + ",exists:" + outFullPath.exists() + ",canwrite:" + outFullPath.canWrite());
            if (!outFullPath.exists()) {
                File folder = new File(outFullPath.getParent());
                logger.trace("outFullPath:" + folder + ",exists:" + folder.exists() + ",canwrite:" + folder.canWrite());

                if (createFile && folder.canWrite()) {
                    createFile = true;
                } else {
                    //We show an error message: folder was not found.
                    String details = "The folder " + outFullPath.getPath() + " was not found or you do not have permissions on it .";
                    String msg = "Can not create the file.";
                    String title = "Error Exporting SubTree";
                    MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, msg, details, MessageDialog.MESSAGE_ERROR);
                    errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                    errorDialog.setVisible(true);
                    logger.error(msg + details);
                    createFile = false;
                    return;
                }
            } else {
                String title = "Existing File";
                String question = "The file: " + outFullPath + " already exists." + "\n" +
                        "Are you sure you want to overwrite it?";
                int answer = JOptionPane.showConfirmDialog(Utils.getMainWindow(), question, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    createFile = false;
                }
            }
            if (createFile) {
                PrintWriter printWriter = new PrintWriter(outFullPath);
                LDIFWriter ldifWriter = new LDIFWriter(printWriter);
                ldifWriter = Utils.getLdifWriterOfSubTree(ldapNode, ldifWriter);
                printWriter.close();

                String details = "The file " + outFullPath.getName() + " is stored in " + outFullPath.getPath() + ".";
                String msg = "The file was created succesfully.";
                String title = "Error Exporting SubTree";
                MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, msg, details, MessageDialog.MESSAGE_INFORMATION);
                errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                errorDialog.setVisible(true);
                logger.info(msg);
            }
        } else {
            String msg = "Operation cancelled by the user.";
            String title = "Error Exporting SubTree";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, msg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(msg);
            return;
        }
    }
}
