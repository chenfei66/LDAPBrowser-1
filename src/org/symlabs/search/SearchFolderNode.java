package org.symlabs.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchBase </p>
 * <p>Descripcion: Class used to store the data of a search folder.</p>
 * <p>Copyright: Symlabs </p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchFolderNode.java,v 1.2 2009-07-29 11:09:24 efernandez Exp $
 */
public class SearchFolderNode extends SearchBaseNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchFolderNode.class);
    /**Attribute that stores the bookmark key used to store a configuration*/
    public static final String FOLDER_BEGIN_KEY = "searchfolderbegin:";
    /**Attribute that stores the bookmark key used to store a configuration*/
    private static final String FOLDER_END_KEY = "searchfolderend:";
    /**Attribute that contains the name of the root*/
    public static final String DEFAULT_SEARCH_ROOT_NAME = "Searches";
    /**Attribute that contains the description of the root node*/
    public static final String DEFAULT_SEARCH_ROOT_DESCRIPTION = "It contains all the Searches stored in this ldap connection.";

    public SearchFolderNode() {
        this("", "");
    }

    public SearchFolderNode(SearchFolderNode searchNode) {
        this(searchNode.getName(), searchNode.getDescription());
    }

    /**Constructor: Create a new folder
     * 
     * @param name
     * @param description
     */
    public SearchFolderNode(String name, String description) {
        this.name = name;
        this.description = description;
        this.iconPath = Utils.ICON_SEARCH_FOLDER;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }

    /**Method that creates a search foder. This bookmark is read from the buffer given as argument
     * 
     * @param buffer BufferedReader. This buffer contains the bookmark to create
     * @throws java.io.IOException 
     */
    public SearchFolderNode(BufferedReader buffer) throws IOException {
        this.iconPath = Utils.ICON_SEARCH_FOLDER;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
        String line = buffer.readLine();
        String errorMsg = "";
        if (line.startsWith(SearchFolderNode.FOLDER_BEGIN_KEY)) {

            // <editor-fold defaultstate="collapsed" desc=" Read Name ">
            //Name
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchBaseNode.SEARCH_NAME_KEY)) {
                this.name = line.substring(SearchBaseNode.SEARCH_NAME_KEY.length(), line.length());
            } else {
                errorMsg = "Error reading name from Search Folder.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Description ">
            //Description
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchFolderNode.SEARCH_DESCRIPTION_BEGIN_KEY)) {
                line = buffer.readLine();
                logger.trace("line:" + line);
                this.description = "";
                while (!line.equals(SearchBaseNode.SEARCH_DESCRIPTION_END_KEY)) {
                    this.description += line;
                    line = buffer.readLine();
                }
            } else {
                errorMsg = "Error reading description from Bookmark Folder.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read child nodes ">
            buffer.mark(256);
            line = buffer.readLine();
            logger.trace("line:" + line);
            while (((line.startsWith(SearchFolderNode.FOLDER_BEGIN_KEY)) || (line.startsWith(SearchNode.SEARCH_BEGIN_KEY))) && !(line.startsWith(SearchFolderNode.FOLDER_END_KEY))) {
                buffer.reset();
                SearchBaseNode newChild = null;
                if (line.startsWith(SearchFolderNode.FOLDER_BEGIN_KEY)) {
                    newChild = new SearchFolderNode(buffer);
                } else if (line.startsWith(SearchNode.SEARCH_BEGIN_KEY)) {
                    newChild = new SearchNode(buffer);
                }
                this.add(newChild);
                buffer.mark(256);
                line = buffer.readLine();
                logger.trace("line:" + line);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Search End Key ">
            //Read bookmark End Key
            buffer.reset();
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (!line.startsWith(SearchFolderNode.FOLDER_END_KEY)) {
                errorMsg = "Error reading end of Bookmark Folder.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
        // </editor-fold>
        } else {
            errorMsg = "Error reading Search.";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }
    }

    /**Method that write a search in the buffer
     * 
     * @param buffer BufferedWriter.
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public BufferedWriter saveSearch(BufferedWriter buffer) throws IOException {
        buffer.write(SearchFolderNode.FOLDER_BEGIN_KEY);
        buffer.newLine();
        buffer.write(SearchBaseNode.SEARCH_NAME_KEY + this.getName());
        buffer.newLine();
        buffer.write(SearchBaseNode.SEARCH_DESCRIPTION_BEGIN_KEY);
        buffer.newLine();
        buffer.write(this.getDescription());
        if (!this.getDescription().endsWith(System.getProperty("line.separator"))) {
            buffer.newLine();
        }
        buffer.write(SearchBaseNode.SEARCH_DESCRIPTION_END_KEY);
        buffer.newLine();
        //We have to write the contents of this folder
        for (int i = 0; i < this.getChildCount(); i++) {
            SearchBaseNode base = (SearchBaseNode) this.getChildAt(i);
            buffer = base.saveSearch(buffer);
        }
        buffer.write(SearchFolderNode.FOLDER_END_KEY);
        buffer.newLine();
        return buffer;
    }

    /**Method that tell us if this folder can create a new child with that name
     * 
     * @param childName String. This is the Name field of the child node
     * @return boolean. True can create it. False it can not.
     */
    public boolean canCreateChild(String childName) {
        for (int i = 0; i < this.getChildCount(); i++) {
            SearchBaseNode base = (SearchBaseNode) this.getChildAt(i);
            if (base.getName().equals(childName)) {
                return false;
            }
        }
        return true;
    }
}
