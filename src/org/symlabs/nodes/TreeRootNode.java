package org.symlabs.nodes;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.actions.bookmark.AddBookmark;
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
import org.symlabs.actions.search.NewSearch;
import org.symlabs.actions.tab.TabProperties;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.store.ConnectionData;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: TreeRootNode </p>
 * <p>Descripcion: Class that manages each attribute of the entry of the connection.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TreeRootNode.java,v 1.40 2009-09-29 09:45:21 efernandez Exp $
 */
public class TreeRootNode extends LDAPNode {

    /**Attribute that contains the object ldapServer, this object is used to manage the ldap connections*/
    protected LDAPServer ldapServer;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(TreeRootNode.class);
    /**Attribute that contains the tree of the ldap tree*/
    protected JTree myTree;
    /**Attribute that contains the tree model*/
    protected DefaultTreeModel myModel;

    /**Method that sets the tree model
     * 
     * @param m DefaultTreeModel. This is the model to be setted
     */
    public void setModel(DefaultTreeModel m) {
        this.myModel = m;
    }

    /**Method that returns the model
     * 
     * @return DefaultTreeModel. This is the model 
     */
    public DefaultTreeModel getModel() {
        return this.myModel;
    }

    /**Method that sets the tree
     * 
     * @param tree JTree. This is the tree to be setted
     */
    public void setTree(JTree tree) {
        this.myTree = tree;
    }

    /**Method that returns the tree
     * 
     * @return JTree. This is the tree of the ldap connection
     */
    public JTree getTree() {
        return this.myTree;
    }

    /**Constructor: initializes the attributes of the connection
     * 
     * @param host String. This is the host used to connect to LDAP server. 
     * @param port String. This is the port used to connect to LDAP server.
     * @param authid String. This is the user id used to connect to LDAP server.
     * @param authpw char[]. This is the user password used to connect to LDAP server.
     * @param suffix String. This is the suffix given in the window connection
     * @param indexLdapVersion int. This is the index of the ldap version selected. This index must be contained in LDAP_SUPPORTED_VERSION
     * @param configurationName String. This is the name of the configuration name, null if the name has not been stored.
     * @param rootSearchFolder
     * @param rootBookmarkFolder
     * @param iconPath String
     * @throws java.lang.Exception If an exception is found it will be thrown an exception
     */
    public TreeRootNode(String host, String port, String authid, char[] authpw, String suffix,int indexLdapVersion,
            BookmarkFolderNode rootBookmarkFolder, SearchFolderNode rootSearchFolder, String configurationName, String iconPath) throws Exception {
        this(new ConnectionData(
                host,
                Integer.parseInt(port),
                authid,
                new String(authpw),
                suffix,
                new String[0],
                indexLdapVersion,
                rootBookmarkFolder,
                rootSearchFolder,
                configurationName,
                iconPath));
    }

    /**Method that creates a new tree root node
     * 
     * @param connectionData ConnectionData. It contains the parameters needed for the connection
     * @throws java.lang.Exception If an exception is found it will be thrown an exception
     */
    public TreeRootNode(ConnectionData connectionData) throws Exception {
        try {
            this.type = LDAPNode.TYPE_ROOT_NODE;
            this.myName = LDAPNode.DEFAULT_ROOT_NODE_NAME;
            this.ldapServer = new LDAPServer(connectionData, false);
            if (connectionData.getConfigurationName() != null) {
                this.myDN = connectionData.getConfigurationName();
            } else {
                this.myDN = this.ldapServer.getHost() + ":" + this.ldapServer.getPort();
            }
            this.myRDN = this.myDN;
            this.setAttributes(this.getDefaultTreeRootNodeAttributes());
            this.setMyIconPath(Utils.getImageIconPathForNode(this));
            this.actionsList.add(TabProperties.HashKey);
            this.actionsList.add(ShowSchema.HashKey);
            this.actionsList.add(ShowRootDse.HashKey);
            // as this extends from LDAPNode, we remove the actions that would be never be supported
            this.actionsList.remove(RefreshTree.HashKey);
            this.actionsList.remove(CloneNode.HashKey);
            this.actionsList.remove(RenameNode.HashKey);
            this.actionsList.remove(DeleteNode.HashKey);
            this.actionsList.remove(AddNewNode.HashKey);
            this.actionsList.remove(CopyDn.HashKey);
            this.actionsList.remove(MoveUp.HashKey);
            this.actionsList.remove(MoveDown.HashKey);
//            this.actionsList.remove(AddBookmark.HashKey);
            this.actionsList.remove(AddSearch.HashKey);
            this.actionsList.remove(NewSearch.HashKey);
        } catch (Exception e) {
            String error = "Error creating TreeRootNode: " + e.toString();
            logger.error(error);
            throw new Exception(error);
        }
    }

    /**Method that returns the object LDAPServer that manages the ldap connection
     * 
     * @return LDAPServer. This is the object that manages the ldap connection
     */
    public LDAPServer getLdapServer() {
        return this.ldapServer;
    }

    /**Method that loads all entries for the current suffixes.
     * If there is no suffix selected, then it is created an empty root with all suffixes.
     * 
     */
    public void loadLDAPSuffix() {
        String rootDN = null;
        String errorMessage = "";
        String suffixes[] = null;
        String title = "Connection Error";
        String message = "";
        //First we get the suffixes stored in the ldapServer
        try {
            suffixes = this.ldapServer.getSuffixes();
        } catch (Exception e) {
            errorMessage += e.getMessage() + "\n";
            message = "Error getting the suffixes.";
            logger.error(errorMessage);
        }

        //There is only one suffix
        if (suffixes != null && suffixes.length == 1) {
            rootDN = suffixes[0];
            try {
                //We create the suffix node
                this.createSuffix(rootDN, this);
            } catch (Exception e) {
                errorMessage += e.getMessage() + "\n";
                message = "Error creating the suffix: " + rootDN;
                logger.error(errorMessage);
            }
        } else { //There are more than one suffix
            if (suffixes != null) {
                logger.trace("load suffix 1");
                Vector<String> vSuffixes = new Vector<String>();
                for (int i = 0; i < suffixes.length; i++) {
                    vSuffixes.addElement(suffixes[i]);
                }
                logger.trace("load suffix 2");
                Vector<String> commonSuffixes = this.getCommonDNFromSuffixes(vSuffixes);
                logger.trace("load suffix 3 " + commonSuffixes.size());
                if (commonSuffixes.size() != suffixes.length) {
                    logger.trace("load suffix 4");
                    try {
                        this.loadSuffixes(vSuffixes, this);
                    } catch (Exception e) {
                        errorMessage = e.toString();
                        logger.error(errorMessage);
                    }
                } else { //there are not any common suffix, it means there are not any virtual node
                    logger.trace("load suffix 5");
                    //We create a node for each suffix
                    for (int i = 0; i < suffixes.length; i++) {
                        try {
                            //We create the suffix node
                            this.createSuffix(suffixes[i], this);
                        } catch (Exception e) {
                            errorMessage += e.getMessage() + "\n";
                            message = "3Error creating suffix: " + suffixes[i];
                            logger.error(errorMessage);
                        }
                    }
                }
            }
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            message = "Error loading suffixes.";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, message, errorMessage, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }

    /**Method that loads the suffixes 
     * 
     * @param vSuffixes Vector<String>. It contains the suffixes that we have to add
     * @param parentNode LDAPNode. This is the parent node where we have to add their suffixes
     * @throws java.lang.Exception Exception.
     */
    protected void loadSuffixes(Vector<String> vSuffixes, LDAPNode parentNode) throws Exception {

        Vector<String> suffixAdded = new Vector<String>();

        // <editor-fold defaultstate="collapsed" desc=" We add the child nodes ">
        for (int i = 0; i < vSuffixes.size(); i++) {
            if (vSuffixes.elementAt(i).indexOf(",") == -1) {//it is an ldap node
                String dn = vSuffixes.elementAt(i);
                if (!(parentNode instanceof TreeRootNode)) {
                    dn = dn + "," + parentNode.myDN;
                }
                //Create the child ldap node
                try {
                    this.createSuffix(dn, parentNode);
                    suffixAdded.addElement(vSuffixes.elementAt(i));
                } catch (Exception e) {
                    String errorMessage = e + "\n";
                    String message = "2Error creating suffix: " + vSuffixes.elementAt(i);
                    logger.error(message + " " + errorMessage);
                    throw new Exception(message);
                }
            }
        }
        // </editor-fold>

        //We remove from suffixes the suffixes added
        for (int i = 0; i < suffixAdded.size(); i++) {
            if (vSuffixes.contains(suffixAdded.elementAt(i))) {
                vSuffixes.remove(suffixAdded.elementAt(i));
            }
        }

        Vector<String> commonSuffixes = this.getCommonDNFromSuffixes(vSuffixes);

        // <editor-fold defaultstate="collapsed" desc=" We add the virtual nodes found">
        for (int i = 0; i < commonSuffixes.size(); i++) {
            if (!this.isVirtualSuffix(vSuffixes, commonSuffixes.elementAt(i), parentNode)) {//it is not a virtual node
                //Create the child ldap node
                try {
                    String dn = commonSuffixes.elementAt(i);
                    if (!(parentNode instanceof TreeRootNode)) {
                        dn = dn + "," + parentNode.myDN;
                    }
                    this.createSuffix(dn, parentNode);
                    vSuffixes.remove(commonSuffixes.elementAt(i));
                } catch (Exception e) {
                    String errorMessage = e + "\n";
                    String message = "1Error creating suffix: " + vSuffixes.elementAt(i);
                    logger.error(message + " " + errorMessage);
                    throw new Exception(message);
                }
            } else { //it is a virtual node
                //Create the virtual node
                String dn = commonSuffixes.elementAt(i);
                if (!(parentNode instanceof TreeRootNode)) {
                    dn = dn + "," + parentNode.myDN;
                }
                LDAPNode virtualNode = null;
                boolean found = false;

                if (parentNode.getChildCount() > 0) {
                    for (int j = 0; j < parentNode.getChildCount(); j++) {
                        if (((LDAPBrowserNode) parentNode.getChildAt(j)).myDN.equalsIgnoreCase(dn)) {
                            virtualNode = (LDAPNode) parentNode.getChildAt(j);
                            found = true;
                            break;
                        }
                    }
                    if(!found){//there are suffixes created, but anyone has the same rdn, so we have to create it as virtualnode
                        virtualNode = new VirtualNode(dn, Utils.getRDN(dn));
                    }
                } else {
                    virtualNode = new VirtualNode(dn, Utils.getRDN(dn));
                }

                if (!found) {//We add it if the parentNode does not contain that node
                    //We have to create the node with the common dn
                    try {
                        parentNode.addChild(virtualNode);
                    } catch (Exception ex) {
                        String errorMessage = "Error creating/adding virtual node. " + "\n" + ex.getMessage();
                        logger.error(errorMessage);
                        throw new Exception(errorMessage);
                    }
                }
                //We calculate the value of the rdn of this attribute, needed to get the suffixes 
                String rdnToSearch = dn;
                if (!(parentNode instanceof TreeRootNode)) {
                    rdnToSearch = Utils.getRDN(dn);
                }

                //We make a copy to do not lose the changes for the next loop iterations (for loop)
                Vector<String> vCopy = new Vector<String>();
                for (int j = 0; j < vSuffixes.size(); j++) {
                    vCopy.addElement(vSuffixes.elementAt(j));
                }

                vCopy = this.getSuffixesUnderCommonDN(vCopy, rdnToSearch);

                this.loadSuffixes(vCopy, virtualNode);
            }
        }//end for

    // </editor-fold>
    }

    /**Method that tells us if the suffix given as argument is a virtual suffix
     * 
     * @param suffixes String. This is the suffixes loaded
     * @param virtualSuffix Stirng. This is the suffix to test
     * @return boolean. True- It means it is a virtual suffix, False- it means it is not a virtual suffix
     */
    private boolean isVirtualSuffix(Vector<String> vSuffixes, String virtualSuffix, LDAPNode parentNode) {
        if (parentNode instanceof TreeRootNode) {
            return !vSuffixes.contains(virtualSuffix);
        } else {
            for (int i = 0; i < vSuffixes.size(); i++) {
                String suffix = vSuffixes.elementAt(i) + parentNode.myDN;
                if (suffix.equalsIgnoreCase(virtualSuffix)) {
                    return false;
                }

            }
            return true;
        }

    }

    /**Method used to shows in output window the values of the current node.
     * 
     */
    @Override
    public void showLDAPNodes() {
        System.out.println("myDN: " + this.myDN + ",name: " + this.myName + ",rdn: " + this.myRDN);
        if (this.getChildCount() > 0) {
            for (int i = 0; i < this.getChildCount(); i++) {
                LDAPNode ldapNode = (LDAPNode) this.getChildAt(i);
                ldapNode.showLDAPNodes();
            }

        }
    }

    /**Method that returns the representative string for this node.
     * This is the string displayed in the Jtree
     * 
     * @return String. this is the string displayed in the jtree for this node
     */
    @Override
    public String toString() {
        return this.myDN;
    }

    /**Method that returns the suffixes that contains the virtual root.
     * It is searched the virtualDnRoot between all suffixes, if it is matched then it will be returned.
     * Example: suffixes ={"dc=clients,dc=com","dc=people,dc=com","dc=company,dc=org"}, 
     * virtualDnRoot={"dc=com"},
     * it will be returned: {"dc=clients,dc=com","dc=people,dc=com"}
     * 
     * @param suffixes String[]. This is the suffixes of the ldap server
     * @param virtualDnRoot String. This is the virtual root which we want to get the suffixes
     * @return Vector<String>. This array contains the all suffixes that contains the virtualDnRoot
     */
    private Vector<String> getSuffixesUnderCommonDN(Vector<String> vSuffixes, String virtualDnRoot) {
        Vector<String> mysuffixes = new Vector<String>();
        for (int i = 0; i < vSuffixes.size(); i++) {
            int index = vSuffixes.elementAt(i).indexOf(virtualDnRoot);
            if (index > 0) {
                String aux = vSuffixes.elementAt(i).substring(0, vSuffixes.elementAt(i).lastIndexOf(","));
                logger.trace("aux:" + aux);
                mysuffixes.add(aux);
            //mysuffixes.add(vSuffixes.elementAt(i));
            }

        }
        return mysuffixes;
    }

    /**Method that returns the virtual roots found between all suffixes.
     * It is searched a commont part between all suffixes.
     * If it is found any common part it is returned.
     * Example: suffixes ={"dc=clients,dc=com","dc=people,dc=com","dc=company,dc=org"}, 
     * it will be returned: {"dc=com","dc=org"}
     * 
     * @param suffixes String[]. This is the suffixes of the ldap server
     * @return Vecotr<String>. This array contains all the common parts found. 
     */
    protected Vector<String> getCommonDNFromSuffixes(Vector<String> suffixes) {
        Vector roots = new Vector();
        for (int i = 0; i < suffixes.size(); i++) {
            logger.trace("testing suffix:" + suffixes.elementAt(i));
            int index = suffixes.elementAt(i).lastIndexOf(",");
            if (index > 0) {
                String rdn = suffixes.elementAt(i).substring(index + 1, suffixes.elementAt(i).length());
                if (!roots.contains(rdn)) {
                    logger.trace("It is added: " + rdn);
                    roots.add(rdn);
                }

            }
        }
        return roots;
    }

    /**Method that returns the default attributes for a tree root node
     * 
     * @return HashMap with the default attributes and values
     */
    private HashMap<String, String[]>[] getDefaultTreeRootNodeAttributes() {
        HashMap<String, String[]> myHash[] = new HashMap[3];
        HashMap dnAttribute = new HashMap<String, String[]>();
        String namingAttribute = "Root";
        String[] values = new String[]{""};
        dnAttribute.put(namingAttribute, values);

        myHash[0] = dnAttribute;
        myHash[1] = null;
        myHash[2] = null;
        return myHash;
    }

    @Override
    public void setEnabledActions() {
        super.setEnabledActions();
        this.supportedActions.put(TabProperties.HashKey, "yes");
        this.supportedActions.put(ShowSchema.HashKey, "yes");
        this.supportedActions.put(ShowRootDse.HashKey, "yes");
    }

    /**Method that creates the root dn node, only the root, the nodes under the root are not added
     * NOTE: be careful with this method, you must be sure that you really want to call it. 
     * This method only should be used by a treeRootNode
     * 
     * @param rootDN String. This is the the root dn (the suffix)
     * @param node LDAPNode. This is the node where we want to add their child nodes
     * @throws netscape.ldap.LDAPException If there is an error it is thrown an exception
     */
    protected void createSuffix(String rootDN, LDAPNode node) throws LDAPException {
        LDAPSearchResults results = null;
        String errorMessage = "";
        //First we search the ldap entry about this rootDN
        try {
            logger.trace("createSuffix 1, rootDn:" + rootDN);
            logger.trace("createSuffix 1.1, rootDn:" + rootDN + ", rootNode is:" + node.getRoot().myDN + ", class:" + node.getRoot().getClass());
            TreeRootNode root = (TreeRootNode) node.getRoot();
            logger.trace("createSuffix 2");
            results =
                    root.getLdapServer().getLdapOperation().getEntryBaseDN(rootDN);
            logger.trace("createSuffix 3");
        } catch (LDAPException e) {
            errorMessage += e.getMessage() + "\n";
            logger.error(errorMessage);
        }

        logger.trace("createSuffix 4, results: " + results);
        logger.trace(", results.getcount: " + results.getCount());
        node.loadChildNodesFromLDAPServer(results);
        logger.trace("createSuffix 5");
        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            throw new LDAPException(errorMessage);
        }
    }
}
