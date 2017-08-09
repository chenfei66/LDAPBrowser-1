package org.symlabs.util;

import java.util.HashMap;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import org.apache.log4j.Logger;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.store.ConnectionData;

/**
 * <p>Titulo: LDAPServer </p>
 * <p>Descripcion: Class that manages the ldap connection and the connection parameters </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPServer.java,v 1.27 2009-08-05 13:48:10 efernandez Exp $
 */
public class LDAPServer {

    /**Attribute that stores the connection data used to set the ldap connection*/
    private ConnectionData connectionData;
    /**Attribute which contains the ldap connection*/
    private LDAPConnection ldapConnection;
    /**Attribute which contains the default port value for a LDAP connection*/
    public static final int LDAP_DEFAULT_PORT = 389;
    /**Attribute which contains the default port value for a LDAPS connection*/
    public static final int LDAPS_DEFAULT_PORT = 636;
    /**Attribute which contains the ldap supported versions*/
    public static final String[] LDAP_SUPPORTED_VERSIONS = new String[]{"LDAP2", "LDAP3"};
    /**Attribute which contains the default ldap version*/
    public static final String LDAP_DEFAULT_VERSION = "LDAP3";
    /**Attribute instance of LDAPOperation, it is used to manage all ldap operations.*/
    private LDAPOperation ldapOperation;
    /**Attribute that contains the server type of the ldap connection*/
    private HashMap<String, String> serverType;
    /**Attribute that contains the schema of the ldap connection*/
    private Schema ldapSchema;
    /**Attribute that contains the root dse ldap node*/
    private LDAPNode rootDSE;
    /**Attribute that contains the suffixes getted from the ldap connection */
    private String[] suffixes;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPServer.class);

    // <editor-fold defaultstate="collapsed" desc=" Getter methods">
    /**Method that returns the ConnectionData object It contains the connection parameters
     * 
     * @return ConnectionData
     */
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    /**Method that returns a instance of an LDAPOperation
     * 
     * @return LDAPOperation. This is an instance of LDAPOperation
     */
    public LDAPOperation getLdapOperation() {
        return ldapOperation;
    }

    /**Method which returns the suffix given in the connection window.
     * If there is not suffix then the string returned is ""
     * 
     * @return String. This is the suffix.
     */
    public String getSuffix() {
        return this.connectionData.getSuffix();
    }

    /**Method which returns the user id of the ldap connetion
     * 
     * @return String. This is the user id
     */
    public String getUserId() {
        return this.connectionData.getAuthid();
    }

    /**Method which returns the user password of the ldap connection
     * 
     * @return String. This is the user password
     */
    public String getUserPw() {
        return this.connectionData.getAuthpw();
    }

    /**Method which returns the host of the ldap connection
     * 
     * @return String. This is the host
     */
    public String getHost() {
        return this.connectionData.getHost();
    }

    /**Method which returns the port of the ldap connection
     * 
     * @return int. This is th port
     */
    public int getPort() {
        return this.connectionData.getPort();
    }

    /**Method that returns the index of the ldap version selected. 
     * This index must be in the range contained in the LDAP_SUPPORTED_VERSION
     * 
     * @return int. This is the index of the ldap version selected
     */
    public int getIndexLdapVersion() {
        return this.connectionData.getIndexLdapVersion();
    }

    /**Method that returns the ldap schema of this connection
     * 
     * @return Schema. This is the ldap schema for this connection
     */
    public Schema getLdapSchema() {
        if (this.ldapSchema == null) {
            this.createSchema();
        }
        return ldapSchema;
    }

    /**Method that returns the hash that contains the attibutes of the ldap server type
     * 
     * @return HashMap. This is the hash that contains these attributes
     * @see LLDAPServer.VENDOR_NAME_TYPE
     * @see LDAPServer.VENDOR_VERSION_TYPE
     */
    public HashMap<String, String> getServerType() {
        return serverType;
    }

    /**Method that returns the root dse.
     * 
     * 
     * @return LDAPNode. This is the node that contains root dse
     * @see getRootDseConnectingToServer()
     */
    public LDAPNode getRootDSE() {
        return this.rootDSE;
    }

    /**Method that returns the suffixes found in ldap Server
     * 
     * @return String []. This array contains the ldap suffixes found in the ldap server
     */
    public String[] getSuffixes() {
        return suffixes;
    }

    /**Method that returns the default configuration name.
     * It is returned the configuration name if it exists 
     * or it there is not it, it will be returned "host:port"
     * 
     * @return String. This is the name the default name of the configuration.
     */
    public String getDefaultConfigurationName() {
        if (this.getConnectionData().getConfigurationName() == null || this.getConnectionData().getConfigurationName().trim().equals("")) {
            return this.getHost() + ":" + this.getPort();
        } else {
            return this.getConnectionData().getConfigurationName();
        }
    }

    /**Method that sets the name of the configuration.
     * 
     * @param configurationName String. This is the configuration name.
     */
    public void setConfigurationName(String configurationName) {
        this.connectionData.setConfigurationName(configurationName);
    }

    /**Method that returns all the book marks contained in this configuration
     * 
     * @return BookmarkFolder
     */
    public BookmarkFolderNode getBookMarkRootFolder() {
        return this.connectionData.getBookmarkRootFolder();
    }

    /**Method that sets all the bookmarks 
     * 
     * @param bookmarkRootFolder 
     */
    public void setBookMarkRootFolder(BookmarkFolderNode bookmarkRootFolder) {
        this.connectionData.setBookmarkRootFolder(bookmarkRootFolder);
    }

    /**Method that returns all the searches contained in this configuration
     * 
     * @return SearchFolder
     */
    public SearchFolderNode getSearchRootFolder() {
        return this.connectionData.getSearchRootFolder();
    }

    /**Method that sets all the bookmarks 
     * 
     * @param searchRootFolder 
     */
    public void setSearchRootFolder(SearchFolderNode searchRootFolder) {
        this.connectionData.setSearchRootFolder(searchRootFolder);
    }

    // </editor-fold>
    
    /**Constructor: initializes the attributes of this class
     * 
     * @param data ConnectionData. In contains the connection parameters
     * @param testOnly boolean. True - It means test mode: to eval parameters and get suffixes. 
     * False - It means it is going to create a new ldap connection, and get the rootDSE.
     * @throws java.lang.Exception Exception. It is thrown an exception if any parameter is wrong
     * @see LDAP_SUPPORTEDvERSIONS
     * @see EvalParamsEvalParams.evalConnectionParameters()
     */
    public LDAPServer(ConnectionData data, boolean testOnly) throws Exception {
        String error = "";
        if (testOnly) {
            error = EvalParams.evalConnectionParameters(
                    data.getHost(),
                    data.getPort() + "",
                    data.getAuthid(),
                    data.getAuthpw().toCharArray());
        } else {
            error = EvalParams.evalConnectionParameters(
                    data.getHost(),
                    data.getPort() + "",
                    data.getAuthid(),
                    data.getAuthpw().toCharArray(),
                    data.getSuffix(),
                    data.getIndexLdapVersion());
        }
        if (error.equals("")) {
            this.connectionData = data;
            this.ldapConnection = null;
            this.ldapOperation = new LDAPOperation(this);
            if (this.connectionData.getSuffix().equals("")) {
                this.suffixes = this.getSuffixesFromServer();
            } else {
                this.suffixes = new String[]{this.connectionData.getSuffix()};
            }
            if (!testOnly) {
                this.rootDSE = this.getRootDseConnectingToServer();
                this.serverType = this.getLdapServerTypeVersion();
            }
        } else {
            logger.error("Error creating LDAPServer: " + error + ". Test mode:" + testOnly);
            throw new Exception(error);
        }
    }

    /**Method which returns the ldapconnection. 
     * If the parameter reconnect is true: This method sets the connection or if the ldapconnection is already connected, it is returned the ldapconnection.
     * If the paremeter reconnect is false: This method disconnect from the ldap server. And returns the ldapconnection.
     * If the ldap connection is null, it is created.
     * 
     * @param reconnect boolean. It is true to set or get the connection. It is false to disconnect from the ldap server.
     * @return LDAPServer. This is the connection setted as connected or disconnected.
     * @throws LDAPException Exception.
     */
    public LDAPConnection getConnection(boolean reconnect) throws LDAPException {
        if (this.ldapConnection == null) {
            this.ldapConnection = new LDAPConnection();
        }
        if (reconnect) {
            if (!this.ldapConnection.isConnected()) {
                try {
                    this.ldapConnection.connect(this.getHost(), this.getPort(), this.getUserId(), this.getUserPw());
                } catch (LDAPException e) {
                    logger.error("Problem connecting to server " + this.getHost() + ",port " + this.getPort() + ",user " + this.getUserId() + ",passw " + this.getUserPw() + ":\n" + e.toString());
                    throw e;
                }
            }
        } else {
            if (this.ldapConnection.isConnected()) {
                try {
                    this.ldapConnection.disconnect();
                } catch (LDAPException e) {
                    logger.error("Problem disconnecting to server " + this.getHost() + " port " + this.getPort() + ":\n" + e.toString());
                    throw e;
                }
            }
        }
        return this.ldapConnection;
    }

    /** Method which test the ldap connection. 
     * If the ldapconnection is null it is created.
     * If it is connected, it tests the ldap connection.
     * If it is not connect, it connects and tests the ldap connection.
     * 
     * @param suffix String. It conteinta the suffix to look for. This parameter can value ""
     * @throws java.lang.Exception Exception. If there are any error testing the connection and exception is thrown.
     * This exception contains the error message.
     */
    public void TestConnection(String suffix) throws Exception {
        String[] attrs = {"objectclass"};
        LDAPSearchResults results = null;
        try {
            results = this.ldapOperation.LDAPSearch(suffix, LDAPConnection.SCOPE_BASE, "(objectclass=*)", attrs, false);
        } catch (Exception e) {
            logger.error("Problem searching the suffix (" + suffix + "):" + e.toString());
            throw new Exception("Problem searching the suffix (" + suffix + "):" + e.toString());
        }

        if (results.hasMoreElements()) {
            LDAPEntry entry;
            LDAPAttribute attr;
            try {
                entry = results.next();
                attr = entry.getAttribute("objectclass");
            } catch (Exception ee) {
                logger.error("Entry " + suffix + " does not contain the objectclass attribute! "+ee);
                throw new Exception("Entry " + suffix + " does not contain the objectclass attribute!");
            }
        } else {
            throw new Exception("no entry (" + suffix + ") returned from LDAP server!");
        }
    }

    /**Method which returns the suffixes founds in the ldap server
     * 
     * @return String[]. This string contains the suffixes found in the ldap server
     * @throws java.lang.Exception Exception. An exception is thrown if an error is found
     */
    private String[] getSuffixesFromServer() throws Exception {
        String sufs[] = null;
        String[] attrs = {"namingContexts"};
        LDAPSearchResults results = null;

        try {
            results = this.ldapOperation.LDAPSearch("", LDAPConnection.SCOPE_BASE, "(objectclass=*)", attrs, false);
        } catch (Exception e) {
            logger.error("Problem searching the suffix:" + e.toString());
            throw new Exception("Problem searching the suffix:" + e.toString());
        }

        if (results.hasMoreElements()) {
            LDAPEntry le;
            LDAPAttribute aa;
            try {
                le = results.next();
                aa = le.getAttribute("namingcontexts");
            } catch (Exception e) {
                logger.error("RootDSE entry does not contain the namingcontexts attribute! "+e);
                throw new Exception("RootDSE entry does not contain the namingcontexts attribute!");
            }
            if (aa == null) {
                throw new Exception("RootDSE entry does not contain the namingcontexts attribute!");
            }

            sufs = aa.getStringValueArray();
            if (sufs.length == 0) {
                throw new Exception("Empty rootDSE entry returned from LDAP server!");
            }
        } else {
            throw new Exception("No rootDSE entry returned from LDAP server!");
        }

        return sufs;
    }

    /**Method that gets the root dse entry from the ldap server
     * NOTE: This method only should be called from the LDAPServerConstructor
     * 
     * @return LDAPNode. It is returned an LDAP node that contains the rootdse entry
     * @throws java.lang.Exception If it happens an error it will be thrown an exception
     */
    private LDAPNode getRootDseConnectingToServer() throws Exception {
        String[] attrs = {"*", Schema.SUBSCHEMA_SUBENTRY_KEY, Schema.VENDOR_NAME_TYPE, Schema.VENDOR_VERSION_TYPE, Schema.NAMING_CONTEXTS_KEY};
        LDAPEntry le = null;
        LDAPSearchResults results = null;
        try {
            results = this.ldapOperation.LDAPSearch("", LDAPConnection.SCOPE_BASE, "(objectclass=*)", attrs, false);
        } catch (Exception e) {
            logger.error("Error getting the LDAP Server Type:" + e);
            throw new Exception("Error getting the LDAP Server Type:\n" + e.toString());
        }

        if (results.hasMoreElements()) {
            try {
                le = results.next();
            } catch (Exception e) {
                logger.error("RootDSE entry does not contain the attribute!");
                throw new Exception("RootDSE entry does not contain the attribute!");
            }
        }

        LDAPNode node = null;
        node = new LDAPNode(le, false, true);
        return node;
    }

    /**Method that returns the attributes that contains the info of the kind of ldap server
     * 
     * @return HasMap. It contains the attributes
     * @throws java.lang.Exception If it happens an error I will be thrown an exception
     */
    private HashMap getLdapServerTypeVersion() throws Exception {
        HashMap type = null;
        LDAPAttribute[] vendorType = new LDAPAttribute[2];

        //We get the vendor name and vendor version.
        vendorType = this.getVendorNameAndVendorVersion();
        //If vendorType = null it means that there was not found these attributes,
        //so we try to get it each one alone.
        if (vendorType == null) {
            vendorType = new LDAPAttribute[2];
            vendorType[0] = this.getVendorName();
            vendorType[1] = this.getVendorVersion();
        }

        //If vendorName and vendorVersion are not null then we store it
        if (vendorType[0] != null && vendorType[1] != null) {

            String[] typeVendorName = vendorType[0].getStringValueArray();
            String[] typeVendorVersion = vendorType[1].getStringValueArray();
            logger.trace("vendor name: " + typeVendorName.length + ", vendor version: " + typeVendorVersion.length);
            if (typeVendorName.length == 0 || typeVendorVersion.length == 0) {
                String errorMsg = "Empty rootDSE entry returned from LDAP server!" + "\n";
                logger.error(errorMsg);
                throw new Exception(errorMsg);
            } else {
                type = new HashMap<String, String>();

                //Both attributes should have only one value, so we get the first element for each array
                //Add the attribute in the hashType
                logger.trace("name:" + typeVendorName[0] + ", version:" + typeVendorVersion[0]);
                type.put(Schema.VENDOR_NAME_TYPE, typeVendorName[0]);
                type.put(Schema.VENDOR_VERSION_TYPE, typeVendorVersion[0]);
            }
        } else {
            String errorMsg = "No rootDSE entry returned from LDAP server!" + "\n";
            logger.error(errorMsg);
            //throw new Exception(errorMsg);
            type= new HashMap<String,String[]>();
            type.put(Schema.VENDOR_NAME_TYPE, "N/A");
            type.put(Schema.VENDOR_VERSION_TYPE,"N/A");
        }
        return type;
    }

    /**Method that returns the vendor name and the vendor version of this ldap server connection
     * 
     * @return LDAPAttribute[]
     * @throws java.lang.Exception
     */
    private LDAPAttribute[] getVendorNameAndVendorVersion() throws Exception {
        LDAPNode rootDse = this.getRootDSE();
        logger.trace("rootDSE:" + rootDse);
        HashMap<String, String[]>[] hash = rootDse.getAttributes();
        Utils.printHash(hash);
        String[] valuesVendorName = hash[0].get(Schema.VENDOR_NAME_TYPE);
        logger.trace("valuesVendorName:" + valuesVendorName);
        String[] valuesVendorVersion = hash[0].get(Schema.VENDOR_VERSION_TYPE);
        logger.trace("valuesVendorVersion:" + valuesVendorVersion);
        LDAPAttribute[] attrs = null;
        if (valuesVendorName.length > 0 && valuesVendorVersion.length > 0) {
            attrs = new LDAPAttribute[2];
            attrs[0] = new LDAPAttribute(Schema.VENDOR_NAME_TYPE, valuesVendorName);
            attrs[1] = new LDAPAttribute(Schema.VENDOR_VERSION_TYPE, valuesVendorVersion);
            logger.trace("VendorName and VendorVersion was found");
        } else if (valuesVendorName.length > 0) {
            attrs = new LDAPAttribute[1];
            attrs[0] = new LDAPAttribute(Schema.VENDOR_NAME_TYPE, valuesVendorName);
            logger.trace("VendorName was found");
        } else if (valuesVendorVersion.length > 0) {
            attrs = new LDAPAttribute[1];
            attrs[0] = new LDAPAttribute(Schema.VENDOR_VERSION_TYPE, valuesVendorVersion);
            logger.trace("VendorVersion was found");
        }
        return attrs;
    }

    /**Method that returns the vendor name
     * 
     * @return LDAPAttribute
     * @throws java.lang.Exception
     */
    private LDAPAttribute getVendorName() throws Exception {
        String[] attrs = {Schema.VENDOR_NAME_TYPE};
        LDAPSearchResults results = null;
        LDAPEntry le = null;
        LDAPAttribute vendorName = null;
        try {
            results = this.ldapOperation.LDAPSearch("", LDAPConnection.SCOPE_BASE, "(objectclass=*)", attrs, false);
        } catch (Exception e) {
            String error = "Error getting vendor name from LDAP Server:\n" + e.toString();
            logger.error(error);
            throw new Exception(error);
        }
        if (results.hasMoreElements()) {
            try {
                le = results.next();
                vendorName = le.getAttribute("vendorname");
            } catch (Exception e) {
                String error = "RootDSE entry does not contain the " + vendorName + " attribute!" + "\n";
                logger.error(error);
                throw new Exception(error);
            }
        }
        if (vendorName == null) {
            logger.info("vendorname attribute was not found.");
            return null;
        } else {
            logger.trace("vendorname attribute was found.");
            return vendorName;
        }
    }

    /**MEthod that returns the vendor version
     *  
     * @return LDAPAttribute
     * @throws java.lang.Exception
     */
    private LDAPAttribute getVendorVersion() throws Exception {
        String[] attrs = {Schema.VENDOR_VERSION_TYPE};
        LDAPSearchResults results = null;
        LDAPEntry le = null;
        LDAPAttribute vendorVersion = null;
        try {
            results = this.ldapOperation.LDAPSearch("", LDAPConnection.SCOPE_BASE, "(objectclass=*)", attrs, false);
        } catch (Exception e) {
            String error = "Error getting vendor version from LDAP Server:\n" + e.toString();
            logger.error(error);
            throw new Exception(error);
        }
        if (results.hasMoreElements()) {
            try {
                le = results.next();
                vendorVersion = le.getAttribute("vendorversion");
            } catch (Exception e) {
                String error = "RootDSE entry does not contain the " + vendorVersion + " attribute!" + "\n";
                logger.error(error);
                throw new Exception(error);
            }
        }
        if (vendorVersion == null) {
            logger.info("vendorversion attribute was not found.");
            return null;
        } else {
            logger.trace("vendorversion attribute was found.");
            return vendorVersion;
        }
    }

    /**Method that gets the ldap schema from the ldap server
     * 
     */
    public void createSchema() {
        this.ldapSchema = new Schema(this);
    }
}
