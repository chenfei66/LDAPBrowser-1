package org.symlabs.nodes;

import org.symlabs.nodes.schema.TreeSchemaRootNode;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.tree.TreePath;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.actions.bookmark.AddBookmark;
import org.symlabs.actions.node.AddNewNode;
import org.symlabs.actions.node.CloneNode;
import org.symlabs.actions.node.CopyDn;
import org.symlabs.actions.node.DeleteNode;
import org.symlabs.actions.node.RefreshTree;
import org.symlabs.actions.node.RenameNode;
import org.symlabs.actions.search.AddSearch;
import org.symlabs.actions.search.ManageSearch;
import org.symlabs.actions.search.NewSearch;
import org.symlabs.browser.BrowserDataStatus;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: LDAPNode </p>
 * <p>Descripcion: Class which manages a not root node of the ldap tree </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPNode.java,v 1.50 2009-09-29 14:26:16 efernandez Exp $
 */
public class LDAPNode extends LDAPBrowserNode {

    /**Attribute that contains the ldap attributes for this node*/
    protected HashMap<String, String[]> attributes[];
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPNode.class);
    /**Attribute that identify the type of a virtual node*/
    protected static final String TYPE_VIRTUAL_NODE = "VirtualNode";
    /**Attribute that identify the type of a tree root node*/
    protected static final String TYPE_ROOT_NODE = "TreeRootNode";
    /**Attribute that identify the type of a ldap node*/
    public static final String TYPE_LDAP_NODE = "LDAPNode";
    /**Attribute that identify the default name for a Tree root node*/
    public static final String DEFAULT_ROOT_NODE_NAME = "Tree Root Node";
    /**Attribute that identify the default name for a virtual node*/
    public static final String DEFAULT_VIRTUAL_NODE_NAME = "Virtual Node";
    /**Attribute that identify the default name for an LDAP node*/
    public static final String DEFAULT_LDAP_NODE_NAME = "LDAP Node";
    /**Attribute used to identify the index of the attribute and newAttribute that stores the dn*/
    public static final int INDEX_DN = 0;
    /**Attribute used to identify the index of the attribute and newAttribute that stores the objectclasses*/
    public static final int INDEX_OBJECTCLASSES = 1;
    /**Attribute used to identify the index of the attribute and newAttribute that stores other attributes*/
    public static final int INDEX_ATTRIBUTES = 2;
    /**Attribute that contains the defualt dn of the root dse*/
    protected static final String DEFAULT_DN_ROOT_DSE = "cn=rootDSE";

    /**Method that returns the ldap attributes of this node
     * 
     * @return HashMap<String, String[]>[]. This is the ldap atributes contained in this node.
     * This array of hashMap contains all the attribute for this node.
     * @see INDEX_DN
     * @see INDEX_OBJECTCLASSES
     * @see INDEX_ATTRIBUTES
     * 
     * Format: String,String[]. 
     * The first String is the attribute name and the String[] are the values for this attribute.
     * 
     */
    public HashMap<String, String[]>[] getAttributes() {
        return attributes;
    }

    /**Method that only returns the attributes found in the attributes of this ldapNode
     * 
     * @return HashMap<String,String[]> 
     * @see getAttributes()
     */
    public HashMap<String, String[]> getOnlyAttributes() {
        return this.attributes[LDAPNode.INDEX_ATTRIBUTES];
    }

    /**Method that only returns the objectclasses found in the attributes of this ldapNode
     * 
     * @return HashMap<String,String[]> 
     * @see getAttributes()
     */
    public HashMap<String, String[]> getOnlyObjectClasses() {
        return this.attributes[LDAPNode.INDEX_OBJECTCLASSES];
    }

    /**Method that only returns the dn attribute found in the attributes of this ldapNode
     * 
     * @return HashMap<String,String[]> 
     * @see getAttributes()
     */
    public HashMap<String, String[]> getOnlyDNAttribute() {
        return this.attributes[LDAPNode.INDEX_DN];
    }

    /**Mehtod that sets the attributes of this node
     * 
     * @param attributes HashMap. This is the ldap attributes that we want to set.
     * Format: String,String[]. 
     * The first element is the attribute and the second are the values.
     */
    public void setAttributes(HashMap<String, String[]> attributes[]) {
        this.attributes = attributes;
    }

    /**Constructor: Creates a new instance of LDAPNode
     * 
     */
    public LDAPNode() {
        this.type = LDAPNode.TYPE_LDAP_NODE;
        this.myName = LDAPNode.DEFAULT_LDAP_NODE_NAME;
        this.setMyIconPath(Utils.getImageIconPathForNode(this));
        this.actionsList.add(RefreshTree.HashKey);
        this.actionsList.add(CloneNode.HashKey);
        this.actionsList.add(RenameNode.HashKey);
        this.actionsList.add(DeleteNode.HashKey);
        this.actionsList.add(AddNewNode.HashKey);
        this.actionsList.add(CopyDn.HashKey);
        this.actionsList.add(ManageSearch.HashKey);
//        this.actionsList.add(AddBookmark.HashKey);
        this.actionsList.add(AddSearch.HashKey);
        this.actionsList.add(NewSearch.HashKey);
    }
//
//    /**Constructor: Creates a new instance of LDAPNode.
//     * This constructor is used to display a virtual node
//     * 
//     * @param suffix String. This is the suffix of the virtual node displayed.
//     */
//    public LDAPNode(String suffix) {
//        this.type = LDAPNode.TYPE_VIRTUAL_NODE;
//        this.myDN = suffix;
//        this.myRDN = this.myDN;
//        this.myName = Utils.getName(this.myRDN);
//        this.setMyIconPath(Utils.getImageIconPathForNode(this));
//        this.actionsList.add(CopyDn.HashKey);
//        this.actionsList.add(ManageSearch.HashKey);
//        this.setAttributes(null);
//    //this.setAttributes(this.getDefaultVirtualNodeAttributes());
//    }
    /**Contructor: Creates a new instance of ldapNode.
     * The third parameter isRootDSE indicates if this node will containthe rootDSE o no. 
     * In case of parameter isRootDSE takes the value true then the second parameter withChildNodes should take the value false.
     * 
     * @param ldapEntry LDAPEntry. This is the ldap entry of this node. It is used to initialize the data of this node
     * @param withChildNodes boolean. True to search child nodes. False to do not search child nodes.
     * @param isRootDSE boolean. True it means that this is the rootDSE node. False means that this node is not the root DSE node.
     * NOTE: If isRootDSE = true then the parameter withChildNodes = false
     */
    public LDAPNode(LDAPEntry ldapEntry, boolean withChildNodes, boolean isRootDSE) {
        this.type = LDAPNode.TYPE_LDAP_NODE;
        this.setLDAPEntry(ldapEntry, isRootDSE);
        this.setMyIconPath(Utils.getImageIconPathForNode(this));
        this.actionsList.add(RefreshTree.HashKey);
        this.actionsList.add(CloneNode.HashKey);
        this.actionsList.add(RenameNode.HashKey);
        this.actionsList.add(DeleteNode.HashKey);
        this.actionsList.add(AddNewNode.HashKey);
        this.actionsList.add(CopyDn.HashKey);
        this.actionsList.add(ManageSearch.HashKey);
//        this.actionsList.add(AddBookmark.HashKey);
        this.actionsList.add(AddSearch.HashKey);
        this.actionsList.add(NewSearch.HashKey);
        LDAPSearchResults results = null;
        String errorMessage = "";
        if (withChildNodes) {
            try {
                results = this.getLDAPServerFromTreeRootNode().getLdapOperation().getEntriesUnderBaseDN(this.myDN);
            } catch (Exception ee) {
                errorMessage = ee.getMessage();
                logger.error(errorMessage);
            }
            if (errorMessage.equals("")) {
                while (results.hasMoreElements()) {
                    //Gets the root dn
                    LDAPEntry le = null;
                    try {
                        le = results.next();
                        LDAPNode node = new LDAPNode(le, true);
                        this.addChild(node);
                    } catch (Exception ex) {
                        errorMessage += ex.getMessage() + "\n";
                        logger.error(errorMessage);
                    }
                }
            }
        }
    }

    /**Constructor: Creates a new instance of LDAPNode.
     * This constructor is used to create all nodes of the ldap connection minus the root node
     * 
     * @param ldapEntry LDAPEntry. This is the ldap entry of this node. It is used to initialize the data of this node
     * @param withChildNodes boolean. True to search child nodes. False to do not search child nodes.
     */
    public LDAPNode(LDAPEntry ldapEntry, boolean withChildNodes) {
        this(ldapEntry, withChildNodes, false);
    }
//
//    /**Method that creates a new child in this node
//     * 
//     * @return LDAPBrowserNode. This is the child node.
//     */
//    public LDAPBrowserNode createChild() {
//        LDAPNode g = new LDAPNode();
//        return g;
//    }
    /**Method that sets the values of this node.
     * 
     * @param ldapEntry LDAPEntry. This entry contains the values read from the ldap server.
     * @param isRootDSE boolean. True means that this node is the rootDSE, False meansthar this is not the rootDSE.
     */
    public void setLDAPEntry(LDAPEntry ldapEntry, boolean isRootDSE) {
        if (isRootDSE) {
            this.myDN = this.getDNRootDSE();
            this.myRDN = this.myDN;
            this.attributes = Utils.getHashRootDseFromLDAPEntry(ldapEntry);//Utils.getHashFromLDAPEntry(ldapEntry, this.myRDN);//Here we have all attributes of this entry
            this.setMyIconPath(Utils.ICON_ROOT_NODE);
        } else {
            this.myDN = ldapEntry.getDN();
            this.myRDN = Utils.getRDN(this.myDN);
            this.attributes = Utils.getHashFromLDAPEntry(ldapEntry, this.myRDN);//Here we have all attributes of this entry
        }
        this.myRDN = Utils.getRDN(this.myDN);
        this.myName = Utils.getValueFromRdn(this.myRDN);
    }

    /**Method used to shows in output window the values of the current node.
     * 
     */
    public void showLDAPNodes() {
        System.out.println("myDN: " + this.myDN + ",name: " + this.myName + ",rdn: " + this.myRDN);
        if (this.getChildCount() > 0) {
            for (int i = 0; i < this.getChildCount(); i++) {
                LDAPNode ldapNode = (LDAPNode) this.getChildAt(i);
                ldapNode.showLDAPNodes();
            }
        }
    }

    /**Method that return the root node.
     * 
     * @return LDAPNode.
     */
    @Override
    public LDAPNode getRoot() {
        if (this.getParent() == null) {
            //It is returned null if the node has no parent
            //Note the following problem: If the node has just been created then the root node will be null, 
            //because this child node has not been added to the parent node still.
            return this;
        } else {
            return ((LDAPNode) this.getParent()).getRoot();
        }
    }

    /**Method that returns the patern ldapServer. The patern node should be an instance of TreeRootNode
     * If there is an error then it will be returned null.
     * It returns the ldap server found in the current ldap root
     * 
     * @return LDAPServer. This is the ldap server object used to manage the ldap connections
     */
    public LDAPServer getLDAPServerFromTreeRootNode() {
        LDAPServer ldapServer = null;
        try {
            ldapServer = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer();

        // I can not use this because it needs the parent node must have been added and 
        //when a node is created, first it is created and after that it is added to the parent node, 
        //so the TreeRootNode could not be getted
        //ldapServer=((TreeRootNode) this.getRoot()).getLdapServer();
        } catch (Exception e) {
            ldapServer = null;
            logger.error("Error getting ldap server from treeRootNode " + e);
        }
        return ldapServer;
    }

    /**Method that returns the dn of the root DSE
     * 
     * @return String. This is the dn of the root DSE
     */
    private String getDNRootDSE() {
        return LDAPNode.DEFAULT_DN_ROOT_DSE;
    }

    /**Method that returns the child nodes contained under this node in the ldap server
     * 
     * @return LDAPSearchResults. It contains the children nodes. It is returned null if it does not contain anyone node
     * @throws java.lang.Exception Exception. If it is found an error it is thrown an exception
     */
    public LDAPSearchResults getChildNodesFromLDAPServer() throws Exception {
        LDAPSearchResults results = null;
        String errorMessage = "";
        try {
            TreeRootNode root = (TreeRootNode) this.getRoot();
            results = root.getLdapServer().getLdapOperation().getEntriesUnderBaseDN(this.myDN);
        } catch (Exception e) {
            errorMessage += "Error searching child nodes" + "\n" + e.getMessage() + "\n";
            logger.error(errorMessage);
        }
        if (!errorMessage.equals("")) {
            throw new Exception(errorMessage);
        }
        return results;
    }

    /**Method that loads the child nodes at this node
     * 
     * @param results LDAPSearchResults. This is the results found in the ldap server
     * @throws netscape.ldap.LDAPException It it is found an error it is thrown an exception 
     */
    protected void loadChildNodesFromLDAPServer(LDAPSearchResults results) throws LDAPException {
        String errorMessage = "";
        if (errorMessage.equals("")) {
            while (results.hasMoreElements()) {
                //Gets the root dn
                LDAPEntry le = null;
                try {
                    le = results.next();
                    logger.trace("Adding child node: " + le.getDN());
                    TreeRootNode rootNode = (TreeRootNode) this.getRoot();
                    if (rootNode instanceof TreeSearchRootNode) {
                        TreeSearchRootNode searchRoot = (TreeSearchRootNode) rootNode;
                        if (this.matchesDn(searchRoot.getSearchResults(), le.getDN())) {
                            if (Utils.findNodeByDN(le.getDN(), this) == null) {
                                String [] suffixes = rootNode.getLdapServer().getSuffixes();
                                boolean isSuffix= false;
                                for(int i=0;i<suffixes.length;i++){
                                    if(suffixes[i].equalsIgnoreCase(le.getDN())){
                                        isSuffix=true;
                                        break;
                                    }
                                }
                                LDAPNode node = null;
                                if(isSuffix){
                                    node = new VirtualNode(le.getDN(),Utils.getRDN(le.getDN()));
                                }else{
                                    node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
                                }
                                this.addChild(node);
                            } else {
                                logger.trace("Not adding entry already exists: " + le.getDN() + " in search node");
                            }
                        }

//                        TreeSearchRootNode searchRoot = (TreeSearchRootNode) rootNode;
//                        LDAPNode parentNode = (LDAPNode) this.getParent();
//                        logger.trace("PARENT NODE = " + parentNode);
//                        if (parentNode != null) {
////                            if (!(this.getChildCount() > 0 && (this.getChildAt(0) instanceof VirtualNode))) 
////                            {
//                            logger.trace("parentNode!=null");
//                                if (this.matchesDn(searchRoot.getSearchResults(), le.getDN())) {
//                                    if (Utils.findNodeByDN(le.getDN(), this) == null) {
//                                        LDAPNode node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
//                                        this.addChild(node);
//                                    } else {
//                                        logger.trace("Not adding entry already exists: " + le.getDN() + " in search node");
//                                    }
//                                } else {
//                                    logger.trace("Not adding entry: " + le.getDN() + " in search node");
//                                }
////                            }
//                        } else {//To load the suffixes
//                            logger.trace("parentNode==null");
//                            if (Utils.findNodeByDN(le.getDN(), this) == null) {
//                                LDAPNode node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
//                                this.addChild(node);
//                            } else {
//                                logger.trace("Not adding entry already exists: " + le.getDN() + " int search node");
//                            }
//                        }
                    } else if (rootNode instanceof TreeSchemaRootNode) {
                        if (Utils.findNodeByDN(le.getDN(), this) == null) {
                            LDAPNode node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
                            this.addChild(node);
                        } else {
                            logger.trace("Not adding entry already exists: " + le.getDN() + " int search node");
                        }
                    } else {
                        //We search this treeRootNode
                        BrowserPanel browser = Utils.getMainWindow().getBrowserPanelByTitle(rootNode.getLdapServer().getConnectionData().getConfigurationName());
                        if (browser == null) {//It means that this configuration is not still stored, so we get the default configuration name
                            logger.info("It means that this configuration is not still stored, so we get the default configuration name");
                            browser = Utils.getMainWindow().getBrowserPanelByTitle(rootNode.getLdapServer().getConnectionData().getDefaultConfigurationName());
                        }

                        BrowserDataStatus data = browser.getSearchResultsPanel().getBrowserData(rootNode);

                        //To avoid to create existing nodes, when the tree is refreshed by clicking on the connection button of the bottom toolbar  (searches)
                        if (data == null) {
                            LDAPNode node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
                            this.addChild(node);
                        } else {//We have to check if this entry exists
                            if (this.findNodeByDN(le.getDN(), rootNode) == null) {
                                LDAPNode node = new LDAPNode(le, false);//false = it does not look for the child nodes - true= it looks for the child nodes
                                this.addChild(node);
                            }

                        }
                    }
                } catch (LDAPException e) {
                    errorMessage += "Error creating/adding child node: " + "\n" + e.getMessage() + "\n";
                    logger.error(errorMessage);
                }
            }
        }
        if (!errorMessage.equals("")) {
            throw new LDAPException(errorMessage);
        }
    }

    /**Method that tests if the dn given as argument ends with one of the search results
     * 
     * @param vSearches çvector<String>. This is the list of the search results, it only contains the dn
     * @param dn Stirng. This is the dn to match
     * @return boolean. True- It means the the string dn matches with one of them. False-It means it does not match
     */
    private boolean matchesDn(Vector<String> vSearches, String dn) {
        logger.trace("matchesDn, searches.size:" + vSearches.size());
        for (int i = 0; i < vSearches.size(); i++) {
            logger.trace("MatchesDN, dn: " + dn + ", vSearches[" + i + "]:" + vSearches.elementAt(i));
            if (dn.endsWith(vSearches.elementAt(i))) {
                logger.trace("matchesDn dn endswith vSearches.elementAt: return true");
                return true;
            }else if(vSearches.elementAt(i).endsWith(dn)){
                logger.trace("matchesDn vSearches.elementAt endswith dn: return true");
                return true;
            }
        }
        logger.trace("matchesDn return false");
        return false;
    }

//    /**Method that creates the root dn node, only the root, the nodes under the root are not added
//     * NOTE: be careful with this method, you must be sure that you really want to call it. 
//     * This method only should be used by a treeRootNode
//     * 
//     * @param rootDN String. This is the the root dn (the suffix)
//     * @throws netscape.ldap.LDAPException If there is an error it is thrown an exception
//     */
//    protected void createSuffix(String rootDN) throws LDAPException {
//        LDAPSearchResults results = null;
//        String errorMessage = "";
//        //First we search the ldap entry about this rootDN
//        try {
//            logger.trace("createSuffix 1, rootDn:" + rootDN);
//            logger.trace("createSuffix 1.1, rootDn:" + rootDN + ", rootNode is:" + this.getRoot().myDN+", class:"+this.getRoot().getClass());
//            TreeRootNode root = (TreeRootNode) this.getRoot();
//            logger.trace("createSuffix 2");
//            results = root.getLdapServer().getLdapOperation().getEntryBaseDN(rootDN);
//            logger.trace("createSuffix 3");
//        } catch (LDAPException e) {
//            errorMessage += e.getMessage() + "\n";
//            logger.error(errorMessage);
//        }
//        logger.trace("createSuffix 4, results: " + results);
//        logger.trace(", results.getcount: " + results.getCount());
//        this.loadChildNodesFromLDAPServer(results);
//        logger.trace("createSuffix 5");
//        if (!errorMessage.equals("")) {
//            logger.error(errorMessage);
//            throw new LDAPException(errorMessage);
//        }
//    }
    
    /**Method that returns the number of attibutes contained in this node
     * 
     * @return int. This is the number of attributes contained int this node
     */
    public int getAttributesNumber() {
        int totalAttributes = 0;
        for (int i = 0; i < this.attributes.length; i++) {
            if (this.attributes[i] != null) {
                totalAttributes += this.attributes[i].size();
            }
        }
        return totalAttributes;
    }

    /**Method that returns the default attributes for a virtual node
     * 
     * @return HashMap with the default attributes and values
     */
    private HashMap<String, String[]>[] getDefaultVirtualNodeAttributes() {
        HashMap<String, String[]> myHash[] = new HashMap[3];
        HashMap dnAttribute = new HashMap<String, String[]>();
        HashMap objectclasses = new HashMap<String, String[]>();
        String namingAttribute = "";
        String[] values = null;

        namingAttribute = Utils.getRDNAttribute(this.myRDN);
        values = new String[]{"" + this.myName};
        dnAttribute.put(namingAttribute, values);
        myHash[0] = dnAttribute;

        namingAttribute = LDAPNode.DEFAULT_VIRTUAL_NODE_NAME;
        values = new String[]{""};
        objectclasses.put(namingAttribute, values);
        myHash[1] = objectclasses;

        myHash[2] = null;

        return myHash;
    }

    /**Method that returns a boolean that tells you if this node has been expanded or not.
     * 
     * @return boolean. True = this node has been expanded. False means that this node has not been expanded
     */
    public boolean hasBeenExpanded() {
        TreeRootNode root = (TreeRootNode) this.getRoot();
        return root.getTree().hasBeenExpanded(new TreePath(this.getPath()));
    }

    /**Method that refresh this node. If the child nodes has been expanded then they will be refresting too
     * 
     */
    public void refreshNode() {
        String errorMsg = "";
        LDAPEntry ldapEntry = null;
        String title = " Error refreshing node";
        //We refresh this node
        try {
            LDAPSearchResults results = ((TreeRootNode) this.getRoot()).getLdapServer().getLdapOperation().getEntryBaseDN(this.myDN);
            if (results != null && results.getCount() == 1) {
                ldapEntry = results.next();
            } else {
                logger.trace("Total entries returned: " + results.getCount());
                errorMsg = "Could not refresh node " + this.myDN;
                logger.error(errorMsg);
            }
        } catch (Exception e) {
            errorMsg = "Error refreshing this node " + this.myDN + "\n" + e.getMessage();
            logger.error(errorMsg);
        }

        if (!errorMsg.equals("")) {
            String message = "Error refreshing the node: " + this.myDN;
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        } else {
            boolean isRootDSE = this.type.equals(LDAPNode.TYPE_ROOT_NODE);
            this.setLDAPEntry(ldapEntry, isRootDSE);
            this.setMyIconPath(Utils.getImageIconPathForNode(this));

            //We have to refresh the child nodes
            if (this.hasBeenExpanded()) {
                for (Enumeration e = this.children(); e.hasMoreElements();) {
                    LDAPNode childNode = (LDAPNode) e.nextElement();
                    childNode.refreshNode();
                }
            }
        }
    }

    /**Method that loads from the ldap server the child nodes
     * 
     */
    public void loadChildNodes() {
        try {
            logger.trace("We are going to add the child Nodes of " + this.myDN);
            TreeRootNode rootNode = (TreeRootNode) this.getRoot();
            LDAPSearchResults results = null;
            if (!(rootNode instanceof TreeSchemaRootNode) && !(rootNode instanceof TreeRootDseRootNode)) {
                results = this.getChildNodesFromLDAPServer();
                logger.trace("Loading Child nodes: " + this.myDN + ", results:" + results.getCount());
                if (results != null && results.getCount() > 0) {
                    this.loadChildNodesFromLDAPServer(results);
                }
            }
        } catch (Exception exc) {
            String title = "Connection Error";
            String message = "Error searching child nodes.";
            String errorMsg = "Error searching child nodes: " + exc;
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, message, errorMsg, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(errorMsg);
        }
    }

    /**Method that returns a vector of string that contains the objectclasses found in this node
     * 
     * @return Vector <String> . It contains the objectclasses name found
     */
    public Vector<String> getContainedObjectclassesName() {
        return this.getAttributesName(LDAPNode.INDEX_OBJECTCLASSES);
    }

    /**Method that returns a vector of string that contains the other type of attributes found in this node
     * 
     * @return  Vector <String>. It contains the attributes name found
     */
    public Vector<String> getContainedOtherAttributesName() {
        return this.getAttributesName(LDAPNode.INDEX_ATTRIBUTES);
    }

    /**Method that returns a vector of strings that contains the attributes contained in this node
     * 
     * @param index int. This is the index of the hashmap attributes: Dn, Objectclasses, OtherAttributes
     * @return Vector String. It is returned an string vector with the attributes found in this node
     */
    private Vector<String> getAttributesName(int index) {
        Vector<String> vAttributes = new Vector();
        Iterator it = this.attributes[index].entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String attributeName = (String) entry.getKey();
            String[] attrValues = (String[]) entry.getValue();
            if (index == LDAPNode.INDEX_OBJECTCLASSES) {
                for (int i = 0; i < attrValues.length; i++) {
                    vAttributes.addElement(attrValues[i]);
                }
            } else {
                vAttributes.addElement(attributeName);
            }
        }
        return vAttributes;
    }

    /**Method that returns the data of this node. It is returned the values of the attribute "attributes" in format LDAPAttributeSet
     * 
     * @return LDAPAttributeSet. These are the attributes contained in this node
     */
    public LDAPAttributeSet getData() {
        return Utils.getLDAPAttributeSetFromHash(this.attributes);
    }

    /** Method which returns an LDAPNode with the node of the dn given.
     *  This method searches in the child nodes associate to the object that invokes it.
     *   If the dn is not found, it will be returned null
     * 
     * @param dn String with the dn to find
     * @param node LDAPNode
     * @return configurationNode. It is returned the LDAPNode found or null if the dn was not found
     */
    public LDAPNode findNodeByDN(String dn, LDAPNode node) {
        if (this.myDN != null && this.myDN.equalsIgnoreCase(dn)) {
            return this;
        } else {
            if (this.getChildCount() > 0) {
                for (int i = 0; i < this.getChildCount(); i++) {
                    node = ((LDAPNode) this.getChildAt(i)).findNodeByDN(dn, node);
                    if (node != null) { //It means that the node was found
                        return node;
                    }
                }
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void setEnabledActions() {
        super.setEnabledActions();
        this.supportedActions.put(CopyDn.HashKey, "yes");
        this.supportedActions.put(ManageSearch.HashKey, "yes");
        LDAPBrowserNode parentNode = (LDAPBrowserNode) this.getParent();
        //If the parent node is an ldap node
        if ((parentNode != null) && (parentNode.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE))) {
            this.supportedActions.put(CloneNode.HashKey, "yes");
            this.supportedActions.put(RenameNode.HashKey, "yes");
            this.supportedActions.put(DeleteNode.HashKey, "yes");
        }
        if (this.type.equalsIgnoreCase(LDAPNode.TYPE_LDAP_NODE)) {
            this.supportedActions.put(RefreshTree.HashKey, "yes");
            this.supportedActions.put(AddNewNode.HashKey, "yes");
//            this.supportedActions.put(AddBookmark.HashKey, "yes");
            this.supportedActions.put(AddSearch.HashKey, "yes");
            this.supportedActions.put(NewSearch.HashKey, "yes");
        }
    }

    /**Method that returns the ldif text of this node
     * 
     * @return String
     */
    public String getLDIF() {
        return Utils.getLdifFromLDAPEntry(this);
    }

    /**Method that returns an ldap entry with all data of this node.
     * It this node is a tree root node or a virtual node then it is returned null.
     * 
     * @return LDAPEntry.
     */
    public LDAPEntry getLDAPEntry() {
        if (this.type.equals(LDAPNode.TYPE_LDAP_NODE)) {
            LDAPAttributeSet attrSet = Utils.getLDAPAttributeSetFromHash(this.attributes);
            LDAPEntry entry = new LDAPEntry(this.myDN, attrSet);
            return entry;
        } else {
            return null;
        }
    }
}

