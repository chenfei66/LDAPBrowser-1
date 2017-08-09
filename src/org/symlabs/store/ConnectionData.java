package org.symlabs.store;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.symlabs.actions.tab.SaveTab;
import org.symlabs.bookmark.BookmarkFolderNode;
import org.symlabs.browser.MessageDialog;
import org.symlabs.search.SearchFolderNode;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ConnectionData </p>
 * <p>Descripcion: Class which manages the connection data. When you save a configuration your data are saved in a file, this class manages these connection data </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ConnectionData.java,v 1.26 2009-08-27 10:53:38 efernandez Exp $
 */
public class ConnectionData {

    /**Attribute which contains the host of the LDAP server*/
    private String host;
    /**Attribute which contains the port of the LDAP server*/
    private int port;
    /**Attribute which contains the user identifier of the LDAP server*/
    private String authid;
    /**Attribute which contains the user password of the LDAP server*/
    private String authpw;
    /**Attribute which contains the suffix given in connection window*/
    private String suffix;
    /**Attribute that contains the suffixes found in the server*/
    private String [] suffixes;
    /**Attribute that contains the index of the ldap version selected in the current configuration*/
    private int indexLdapVersion;
    /**Attribute that stores the bookmark folder used to store a configuration*/
    private BookmarkFolderNode bookmarkRootFolder;
    /**Attribute that stores the searches. It is a folder that contains the searches stored in this configuraiton*/
    private SearchFolderNode searchRootFolder;
    /**Attribute that stores the configuration name of this connection data*/
    private String configurationName;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(ConnectionData.class);
    /**Attribute that contains the default messages shown when a save operation is cancelled by a used*/
    public static final String OPERATION_CANCELLED = "Operation Cancelled by the user";
    /**Attribute that stores the host key used to store a configuration*/
    private static final String CONFIG_NAME_KEY = "confname:";
    /**Attribute that stores the host key used to store a configuration*/
    private static final String HOST_KEY = "host:";
    /**Attribute that stores the port key used to store a configuration*/
    private static final String PORT_KEY = "port:";
    /**Attribute that stores the authid key used to store a configuration*/
    private static final String AUTHID_KEY = "authid:";
    /**Attribute that stores the authpw key used to store a configuration*/
    private static final String AUTHPW_KEY = "authpw:";
    /**Attribute that stores the suffix key used to store a configuration*/
    private static final String SUFFIX_KEY = "suffix:";
    /**Attribute that stores the suffixes found in the server*/
    private static final String SUFFIXES_KEY = "suffixes:";
    /**Attribute that stores the ldap version key used to store a configuration*/
    private static final String VERSION_KEY = "version:";
    /**Attribute that indicates the the configuration need to be saved. True= need to be saved. False= do not need to save*/
    private boolean dirty;
    /**Attribute that stores the path of the connection*/
    private String connectionIconPath;


    // <editor-fold defaultstate="collapsed" desc=" Getter and setter methods ">
    /**Method that returns the used identifier of the ldap server connection
     * 
     * @return String. 
     */
    public String getAuthid() {
        return authid;
    }

    /**Method that sets the user identifier of the ldap server connection
     * 
     * @param authid String.
     */
    public void setAuthid(String authid) {
        this.authid = authid;
    }

    /**Method that returns the user password of the ldap server connection
     * 
     * @return String. This is the user password
     */
    public String getAuthpw() {
        return authpw;
    }

    /**Method that sets the user password of the ldap server connection
     * 
     * @param authpw String.
     */
    public void setAuthpw(String authpw) {
        this.authpw = authpw;
    }

    /**MEthod that returns the configuration name. This is the name used to store this configuration
     * 
     * @return String
     */
    public String getConfigurationName() {
        return configurationName;
    }

    /**Method that sets the configuration name. This is the name used to store this configuraiton
     * 
     * @param configurationName String.
     */
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    /**Method that returns the host of the ldap conneciton
     * 
     * @return String
     */
    public String getHost() {
        return host;
    }

    /**Method that sets the host used in this ldap connection
     * 
     * @param host String
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**Method that stores the index of the ldap version. This is the version used by this ldap server
     * 
     * @return int.
     */
    public int getIndexLdapVersion() {
        return indexLdapVersion;
    }

    /**Method that sets the index of the ldap version to be used by this ldap server
     * 
     * @param indexLdapVersion int. 
     */
    public void setIndexLdapVersion(int indexLdapVersion) {
        this.indexLdapVersion = indexLdapVersion;
    }

    /**Method that returns the port number used in the ldap connection
     * 
     * @return int
     */
    public int getPort() {
        return port;
    }

    /**Method that sets the port number used in this ldap connection
     * 
     * @param port int.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**Method that returns the suffix of this ldap connection.
     * 
     * @return String.
     */
    public String getSuffix() {
        return suffix;
    }

    /**Method that sets the suffix of this ldap connection
     * 
     * @param suffix String.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public BookmarkFolderNode getBookmarkRootFolder() {
        if (this.bookmarkRootFolder == null) {
            logger.trace("Bookmark root node is null!!!!!!!!!!!");
            this.bookmarkRootFolder = new BookmarkFolderNode(BookmarkFolderNode.DEFAULT_BOOKMARK_ROOT_NAME, BookmarkFolderNode.DEFAULT_BOOKMARK_ROOT_DESCRIPTION);
        }
        return bookmarkRootFolder;
    }

    public void setBookmarkRootFolder(BookmarkFolderNode bookmarkRootFolder) {
        this.bookmarkRootFolder = bookmarkRootFolder;
    }

    public SearchFolderNode getSearchRootFolder() {
        if (this.searchRootFolder == null) {
            logger.trace("Search root node is null!!!!!!!!!!!");
            this.searchRootFolder = new SearchFolderNode(SearchFolderNode.DEFAULT_SEARCH_ROOT_NAME, SearchFolderNode.DEFAULT_SEARCH_ROOT_DESCRIPTION);
        }
        return this.searchRootFolder;
    }

    public void setSearchRootFolder(SearchFolderNode searchRootFolder) {
        this.searchRootFolder = searchRootFolder;
    }

    /**Method that return true if the configuration need to be saved. False if the configuration do not need to be saved
     * 
     * @return boolean
     */
    public boolean isDirty() {
        return dirty;
    }

    /**Method that sets the dirty value. True- It means need to save the configuration. False- It means do not need to save.
     * 
     * @param dirty
     * @see isDirty()
     */
    public void setDirty(boolean dirty) {
        Utils.getAction(SaveTab.HashKey).getAction().setEnabled(dirty);
        this.dirty = dirty;
    }

    /**Method that returns the connection icon path. This icon is displayed in the browser tab panel
     * 
     * @return String. This is the icon path
     */
    public String getIconPath() {
        return connectionIconPath;
    }

    /**Method that return the suffixes
     *
     * @return String []
     */
    public String[] getSuffixes() {
        return suffixes;
    }

    /**Method that sets the suffixes
     *
     * @param suffixes String[]
     */
    public void setSuffixes(String [] suffixes) {
        this.suffixes = suffixes;
    }

    // </editor-fold>

    /**Method that returns the default configuration name.
     * It is returned the configuration name if it exists 
     * or it there is not it, it will be returned "host:port"
     * 
     * @return String. This is the name the default name of the configuration.
     */
    public String getDefaultConfigurationName() {
        if (this.getConfigurationName() == null || this.getConfigurationName().trim().equals("")) {
            return this.getHost() + ":" + this.getPort();
        } else {
            return this.getConfigurationName();
        }
    }

    /**Contructor: It initializes the attributes needed to store the data connection.
     * This constructor does not test the connection or the connection parameters
     * 
     * @param host String. This is the host of the ldap server
     * @param port int. This is the port of the ldap server
     * @param authid String. This is the authid of the ldap server
     * @param authpw String. This is the authpw of the ldap server
     * @param suffix String. This is the suffix of the ldap connection
     * @param suffix String. These are the suffixes found in the ldap server
     * @param indexLdapVersion int. This is the index of the ldap version comboBox
     * @param bookMark BookmarkFolder. This is the root folder that contains the bookmarks stored with this configuration
     * @param search SearchFolder. This is the root folder that contains the searches stored with this configuration
     * @param confName String. This is the configuration name. The name used to store this configuration
     * @param iconPath String, This String contains the icon of the connection browser tab panel
     */
    public ConnectionData(String host, int port, String authid, String authpw, String suffix, String [] suffixes, int indexLdapVersion,
            BookmarkFolderNode bookMark, SearchFolderNode search, String confName, String iconPath) {
        if (host != null && authid != null && authpw != null && suffix != null) {
            this.host = host;
            this.port = port;
            this.authid = authid;
            this.authpw = authpw;
            this.suffix = suffix;
            this.suffixes=suffixes;
            this.indexLdapVersion = indexLdapVersion;
            this.bookmarkRootFolder = bookMark;
            this.searchRootFolder = search;
            this.configurationName = confName;
            this.dirty = false;
            this.connectionIconPath = iconPath;
            Utils.getMainWindow().setStatusBarMessage("Save your LDAP Configuration");
        } else {
            logger.error("Error in connection parameter");
        }
    }

    /**Contructor: It initializes the attributes needed to store the data connection.
     * This constructor is used to test the connection or the connection parameters
     * 
     * @param host String. This is the host of the ldap server
     * @param port int. This is the port of the ldap server
     * @param authid String. This is the authid of the ldap server
     * @param authpw String. This is the authpw of the ldap server
     * @param ldapVersion int. This is the index of the ldap version comboBox
     */
    public ConnectionData(String host, int port, String authid, String authpw, int ldapVersion) {
        if (host != null && authid != null && authpw != null) {
            this.host = host;
            this.port = port;
            this.authid = authid;
            this.authpw = authpw;
            this.indexLdapVersion = ldapVersion;
            this.suffix = "";
            this.bookmarkRootFolder = null;
            this.searchRootFolder = null;
            this.configurationName = null;
            this.dirty = false;
            Utils.getMainWindow().setStatusBarMessage("Save your LDAP Configuration");
        } else {
            logger.error("Error in connection parameter");
        }
    }

    /**Method that saves the data of this configuration in a file
     * 
     * @param confName String. This is the name of the configuration
     * @throws java.lang.Exception Exception. If an error happens it will be thrown an exception
     */
    public void saveData(String confName) throws Exception {
        String errorMsg = "";
        if (!ConnectionData.configurationNameFound(confName)) {
            try {

                FileWriter file = null;

                //We creates a new configuration file
                try {
                    File folder = new File(Utils.getFullPathConfigurationsFolder());
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    file = new FileWriter(Utils.getFullPathConfNameFile(confName));//true - it means that we want to add the configuration at the end of the file
                } catch (IOException io) {
                    //The file does not exist
                    file = null;
                    logger.error("Save Data error: " + io);
                }
                if (file != null) {
                    BufferedWriter buffer = new BufferedWriter(file);
                    buffer.write(CONFIG_NAME_KEY + confName);
                    buffer.newLine();
                    buffer.write(HOST_KEY + this.host);
                    buffer.newLine();
                    buffer.write(PORT_KEY + this.port);
                    buffer.newLine();
                    buffer.write(AUTHID_KEY + this.authid);
                    buffer.newLine();
                    buffer.write(AUTHPW_KEY + this.authpw);
                    buffer.newLine();
                    buffer.write(SUFFIX_KEY + this.suffix);
                    buffer.newLine();

                    String s="";
                    if(this.suffixes!=null && this.suffixes.length>0){
                        s=this.suffixes[0];
                        for(int i=1;i< this.suffixes.length;i++){
                            s+="$"+this.suffixes[i];
                        }
                    }
                    buffer.write(SUFFIXES_KEY +s);
                    buffer.newLine();

                    buffer.write(VERSION_KEY + this.indexLdapVersion);
                    buffer.newLine();
//                    if (this.bookmarkRootFolder != null) {
//                        buffer = this.bookmarkRootFolder.saveBookmark(buffer);
//                    }
//                    buffer.newLine();
                    if (this.searchRootFolder != null) {
                        buffer = this.searchRootFolder.saveSearch(buffer);
                    }
                    buffer.newLine();
                    buffer.close();
                } else {
                    errorMsg += "Error opening configuration file." + "\n";
                    logger.error(errorMsg);
                }
            } catch (Exception e) {
                errorMsg += "Error saving new configuration data." + "\n" + e.getMessage() + "\n";
                logger.error(errorMsg);
            }
        } else {
            try {
                logger.trace("Eliminamos conf: " + confName);
                removeConfNameFromConfigurationFile(confName);
                logger.trace("Añadimos conf: " + confName);
                this.saveData(confName);
            } catch (Exception e) {
                errorMsg += "Error saving overwritten configuration." + "\n" + e.getMessage() + "\n";
                logger.error(errorMsg);
            }
        }
        if (!errorMsg.equals("")) {
            logger.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    /**Method that removes from the file the specific configuration given by argument
     * 
     * @param confToRemove String. This is the configuration that you want to remove
     * @throws java.lang.Exception Exception. If an error happens it will be returned an exception
     */
    public static void removeConfNameFromConfigurationFile(String confToRemove) throws Exception {
        String errorMsg = "";
        try {
            //We remove the confNameFile
            File myFile = new File(Utils.getFullPathConfNameFile(confToRemove));
            if (myFile.exists() && myFile.canRead() && myFile.isFile()) {
                myFile.delete();
            }
        } catch (Exception e) {
            errorMsg += "Error removing configuration from configuration file. " + "\n" + e.getMessage() + "\n";
            logger.error(errorMsg);
        }
        if (!errorMsg.equals("")) {
            logger.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    /**Method that loads the data of the configuration given as argument. 
     * This configuration is loaded in an instance of LDAPServer that it is returned
     * 
     * @param confName String. This is the configuration name which we want to load
     * @return LDAPServer. It is returned an ldapServer with contains this configuration
     * @throws java.lang.Exception Exception. If it happens an error it will be thrown an exception
     */
    public static ConnectionData loadData(String confName) throws Exception {
        String vhost = "";
        String vport = "";
        String vauthid = "";
        String vauthpw = "";
        String vsuffix = "";
        String [] vsuffixes = null;
        String vversion = "";
        ConnectionData data = null;
        String errorMessage = "";
        BookmarkFolderNode bookmarkFolder = null;
        SearchFolderNode searchFolder = null;
        if (configurationNameFound(confName)) {
            logger.trace("The " + confName + " file: " + Utils.getFullPathConfNameFile(confName) + "was found");
            try {
                FileReader file = new FileReader(Utils.getFullPathConfNameFile(confName));
                BufferedReader buffer = new BufferedReader(file);
                boolean exit = false;
                buffer.mark(256);
                String line = buffer.readLine();
                while (line != null && !exit) {
                    if (line.startsWith(HOST_KEY)) {
                        vhost = line.substring(HOST_KEY.length(), line.length());
                        logger.trace("host:" + vhost + ",line:" + line);
                    } else if (line.startsWith(PORT_KEY)) {
                        vport = line.substring(PORT_KEY.length(), line.length());
                        logger.trace("port:" + vport + ",line:" + line);
                    } else if (line.startsWith(AUTHID_KEY)) {
                        vauthid = line.substring(AUTHID_KEY.length(), line.length());
                        logger.trace("authid:" + vauthid + ",line:" + line);
                    } else if (line.startsWith(AUTHPW_KEY)) {
                        vauthpw = line.substring(AUTHPW_KEY.length(), line.length());
                        logger.trace("authpw:" + vauthpw + ",line:" + line);
                    } else if (line.startsWith(SUFFIX_KEY)) {
                        vsuffix = line.substring(SUFFIX_KEY.length(), line.length());
                        logger.trace("suffix:" + vsuffix + ",line:" + line);
                    }else if(line.startsWith(SUFFIXES_KEY)){
                        String s=line.substring(SUFFIXES_KEY.length(),line.length());
                        logger.trace("suffixes:"+s);
                        vsuffixes = s.split("\\$");
                    } else if (line.startsWith(VERSION_KEY)) {
                        vversion = line.substring(VERSION_KEY.length(), line.length());
                        logger.trace("version:" + vversion + ",line:" + line);
                    } else if (line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_BEGIN_KEY)) {
                        buffer.reset();
                        bookmarkFolder = new BookmarkFolderNode(buffer);
                    } else if (line.startsWith(SearchFolderNode.FOLDER_BEGIN_KEY)) {
                        buffer.reset();
                        searchFolder = new SearchFolderNode(buffer);
                    }
                    buffer.mark(256);
                    line = buffer.readLine();
                }
                buffer.close();

            } catch (Exception e) {
                errorMessage += "Error loading data from saved configuration file. " + "\n" + e + "\n";
                logger.error(errorMessage);
            }

            //We create a new instance of ldapServer with this params
            try {
                int vers = Integer.parseInt(vversion);
                int port = Integer.parseInt(vport);
                data = new ConnectionData(vhost, port, vauthid, vauthpw, vsuffix, vsuffixes,vers, bookmarkFolder, searchFolder, confName, Utils.ICON_CONNECT_OPEN);
            } catch (Exception e) {
                errorMessage += "Error creating configuration ldap." + "\n" + e + "\n";
                logger.error(errorMessage);
            }
        } else {
            errorMessage += "Configuration name was not found." + "\n";
            logger.info(errorMessage);
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }

        return data;
    }

    /**Method that returns all configurations names loaded in a config file
     * 
     * @return Vector. This vector contains all the configuration's name created
     * @throws java.lang.Exception Exception. If it happens an error it will be thrown an exception
     */
    public static Vector getConnectionNames() throws Exception {
        String errorMessage = "";
        Vector vNames = new Vector<String>();
        try {
            File confFolder = new File(Utils.getFullPathConfigurationsFolder());
            logger.trace("confFolder:" + confFolder);
            File[] confs = confFolder.listFiles();
            logger.trace("confs:" + confs);
            if (confs != null) {
                for (int i = 0; i < confs.length; i++) {
                    logger.trace("confs:" + confs.length + "," + confs[i].exists() + "," + confs[i].canRead() + "," + confs[i].isFile() + "," + confs[i].getName() + "," + confs[i].getName().endsWith(Utils.configurationFile));
                    if (confs[i].exists() && confs[i].canRead() && confs[i].isFile() &&
                            confs[i].getName().endsWith(Utils.configurationFile)) {
                        String confName = Utils.getConfNameFromFullPathFile(confs[i].getName());
                        if (confName != null) {
                            vNames.addElement(confName);
                        }
                    }
                }
            } else {
                vNames = null; //there are not any configuration stored
            }
        } catch (Exception e) {
            errorMessage += "Error loading data from saved configuration file. " + "\n" + e + "\n";
            logger.error(errorMessage);
        }

        if (!errorMessage.equals("") && vNames != null) {
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }

        return vNames;
    }

    /**Method that searches this confName between all configurations. 
     * Returns true if you can create a configuration with this name, or false if you can not.
     * 
     * @param confName String. This is the configuraiton name
     * @return boolean. True can create it, False you can not create it
     * @throws java.lang.Exception Exception. If an error happens it will be thrown an exception
     */
    public static boolean configurationNameFound(String confName) throws Exception {
        Vector vNames = getConnectionNames();
        if (vNames == null || vNames.size() == 0) {
            //it means that the file was not found
            return false;
        } else {
            for (int i = 0; i < vNames.size(); i++) {
                logger.trace("vNames[" + i + "]:" + vNames.elementAt(i));
            }
            if (vNames.contains(confName)) {
                logger.trace("The configuration name: " + confName + " was found");
                return true;
            } else {
                logger.trace("The configuration name: " + confName + " was not found");
                return false;
            }
        }
    }

    /**Method that save the connection data of the ldap server in a connection file. 
     * This method will be used when you clicked on "save as" button.
     * This method is called from main window menu and the popup from each connection tab.
     * 
     * @param ConnectionData data. This is the connetction data to save.
     * @param panel Component. This is the component that is showing this message.
     * @return String. It is returned the configuration name. 
     * If this operation was cancelled by the user then it is returned ConnectionData.OPERATION_CANCELLED
     * @throws java.lang.Exception Exception. If an error happens it will be thrown an exception
     */
    public static String saveAsConnectionData(ConnectionData data, Component panel) throws Exception {
        String errorMessage = "";
        String name = "";
        if (data != null) {
            try {
//                ConnectionData data = ldapServer.getConnectionData();
                while (name != null && name.trim().equals("")) {
                    String text = "Please write a configuration name";
                    String defaultName = "";
                    if (data.getConfigurationName() != null && !data.getConfigurationName().trim().equals("")) {
                        defaultName = data.getConfigurationName();
                    }
                    name = JOptionPane.showInputDialog(Utils.getMainWindow(), text, defaultName);
                }
                if (name != null) {//the name has been written
                    //the conf name has been written but his name already exists
                    if (configurationNameFound(name)) {
                        String text = "This configuration name already exist.\nAre you sure you want to overwrite this configuration?";
                        String title = "Overwrite Configuration";
                        int option = JOptionPane.showConfirmDialog(panel, text, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            data.saveData(name);
                        } else {
                            text = "Operation cancelled by the user.";
                            title = "Information";
                            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, text, MessageDialog.MESSAGE_INFORMATION);
                            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                            errorDialog.setVisible(true);
                            logger.info(text + "\n" + errorMessage);
                            name = ConnectionData.OPERATION_CANCELLED;
                        }
                    } else {
                        data.saveData(name);
                    }
                } else {//It has been clicked on cancel so we advise to save your configuration
//                    errorMessage += "Warning: your configuration is not saved." + "\n";
                    name = ConnectionData.OPERATION_CANCELLED;
                    logger.info(errorMessage);
                }
            } catch (Exception e) {
                errorMessage += e.getMessage() + "\n";
                logger.error(errorMessage);
            }
        } else {
//            errorMessage += "Error: Your configuration was not saved.";
            name = ConnectionData.OPERATION_CANCELLED;
            logger.error(errorMessage + ", LDAPServer = null");
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }
        return name;
    }

    /**Method that saves this configuration asking for the configuration name to be stored as
     * 
     * @param confName String. This is the configuration name
     * @param host String. This is the host of the ldap server connection
     * @param port int. This is the port of the ldap server connection
     * @param authid String. This is the user identifier of the ldap server connection
     * @param authpw String. This is the user password of the ldap server connection
     * @param suffix String. This is the selected suffix
     * @param suffixes String[]. These are the naming contexts found in the server
     * @param indexLdapVersion int. this is the index of the ldap version selected
     * @param bookmark BookmarkFolder.This is a folder that contains the boomarks stored with this configuraiton
     * @param search SearchFolder. It is a folder that contains the searches stored with this configuraiton
     * @param panel Component. This is the component that is showing this message.
     * @return String. It is returned the configuration name. 
     * If this operation was cancelled by the user then it is returned ConnectionData.OPERATION_CANCELLED
     * @throws java.lang.Exception Exception. If an error happens it will be thrown an exception
     */
    public static String saveAsConnectionData(String confName, String host, int port, String authid, String authpw,
            String suffix, String [] suffixes, int indexLdapVersion, BookmarkFolderNode bookmark, SearchFolderNode search, Component panel) throws Exception {
        String errorMessage = "";
        String name = confName;
        try {

            while (name != null && name.trim().equals("")) {
                String text = "Please write a configuration name";
                String defaultName = name;
                name = JOptionPane.showInputDialog(Utils.getMainWindow(), text, defaultName);
            }
            if (name != null) {//the name has been written
                ConnectionData data = new ConnectionData(host, port, authid, authpw, suffix, suffixes,indexLdapVersion, bookmark, search, name, Utils.ICON_CONNECT_OPEN);
                //the conf name has been written but its name already exists
                if (configurationNameFound(name)) {
                    String text = "This configuration name already exist.\nAre you sure you want to overwrite this configuration?";
                    String title = "Overwrite Configuration";
                    int option = JOptionPane.showConfirmDialog(panel, text, title, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        data.saveData(name);
                    } else {
                        text = "Operation cancelled by the user.";
                        title = "Information";
                        MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, text, MessageDialog.MESSAGE_INFORMATION);
                        errorDialog.setLocationRelativeTo(Utils.getMainWindow());
                        errorDialog.setVisible(true);
                        logger.info(text + "\n" + errorMessage);
                        name = ConnectionData.OPERATION_CANCELLED;
                    }
                } else {
                    data.saveData(name);
                }
            } else {//It has been clicked on cancel so we advise to save your configuration
                errorMessage += "Warning: your configuration is not saved." + "\n";
                logger.info(errorMessage);
            }
        } catch (Exception e) {
            errorMessage += e.getMessage() + "\n";
            logger.error(errorMessage);
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }
        return name;
    }

    /**Method that save the connection data of the ldap server in a connection file.
     * This method will be used when you clicked on "save" button
     * 
     * @param ldapServer LDAPServer. This is the ldap connection to save.
     * @throws java.lang.Exception 
     */
    public static void saveConnectionData(LDAPServer ldapServer) throws Exception {
        String errorMessage = "";
        if (ldapServer != null) {
            ConnectionData data = ldapServer.getConnectionData();
            String connectionName = ldapServer.getConnectionData().getConfigurationName();
            if (connectionName != null) {//the name has been written
                data.saveData(connectionName);
            } else {//It has been clicked on cancel so we advise to save your configuration
                errorMessage += "Warning: your configuration is not saved." + "\n";
                logger.info(errorMessage);
            }
        } else {
            errorMessage += "Error: Your configuration was not saved.";
            logger.error(errorMessage + ", LDAPServer = null");
        }
        if (!errorMessage.trim().equals("")) {
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }
    }
}
