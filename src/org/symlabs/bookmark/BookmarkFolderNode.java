package org.symlabs.bookmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: BookmarkFolder </p>
 * <p>Descripcion: Class that manages a bookmark folder </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BookmarkFolderNode.java,v 1.2 2009-07-29 11:09:24 efernandez Exp $
 */
public class BookmarkFolderNode extends BookmarkBaseNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BookmarkFolderNode.class);
    /**Attribute that stores the bookmark key used to store a configuration*/
    public static final String BOOKMARK_FOLDER_BEGIN_KEY = "bookmarkfolderbegin:";
    /**Attribute that stores the bookmark key used to store a configuration*/
    private static final String BOOKMARK_FOLDER_END_KEY = "bookmarkfolderend:";
    /**Attribute that stores the type of bookmark base type*/
    public static final String TYPE_BOOKMARK_FOLDER = "bookmarkFolderType";
    /**Attribute that contains the name of the root*/
    public static final String DEFAULT_BOOKMARK_ROOT_NAME = "Bookmark";
    /**Attribute that contains the description of the root node*/
    public static final String DEFAULT_BOOKMARK_ROOT_DESCRIPTION = "It contains all the bookmarks stored in this ldap connection.";

    public BookmarkFolderNode() {
        this("", "");
    }

    public BookmarkFolderNode(BookmarkFolderNode folder) {
        this(folder.getName(),folder.getDescription());
    }
    
    /**Constructor: Create a new folder
     * 
     * @param name
     * @param description
     */
    public BookmarkFolderNode(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = TYPE_BOOKMARK_FOLDER;
        this.iconPath = Utils.ICON_BOOKMARK_FOLDER;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }

    /**Method that creates a bookmark. This bookmark is read from the buffer given as argument
     * 
     * @param buffer BufferedReader. This buffer contains the bookmark to create
     * @throws java.io.IOException 
     */
    public BookmarkFolderNode(BufferedReader buffer) throws IOException {
        this.type = TYPE_BOOKMARK_FOLDER;
        this.iconPath = Utils.ICON_BOOKMARK_FOLDER;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
        String line = buffer.readLine();
        String errorMsg = "";
        if (line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_BEGIN_KEY)) {

            // <editor-fold defaultstate="collapsed" desc=" Read Name ">
            //Name
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(BookmarkFolderNode.BOOKMARK_NAME_KEY)) {
                this.name = line.substring(BookmarkFolderNode.BOOKMARK_NAME_KEY.length(), line.length());
            } else {
                errorMsg = "Error reading name from Bookmark Folder.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Description ">
            //Description
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(BookmarkFolderNode.BOOKMARK_DESCRIPTION_BEGIN_KEY)) {
                line = buffer.readLine();
                logger.trace("line:" + line);
                this.description = "";
                while (!line.equals(BookmarkBaseNode.BOOKMARK_DESCRIPTION_END_KEY)) {
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
            while (((line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_BEGIN_KEY)) || (line.startsWith(BookmarkNode.BOOKMARK_BEGIN_KEY))) && !(line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_END_KEY))) {
                buffer.reset();
                BookmarkBaseNode newChild = null;
                if (line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_BEGIN_KEY)) {
                    newChild = new BookmarkFolderNode(buffer);
                } else if (line.startsWith(BookmarkNode.BOOKMARK_BEGIN_KEY)) {
                    newChild = new BookmarkNode(buffer);
                }
                this.add(newChild);
                buffer.mark(256);
                line = buffer.readLine();
                logger.trace("line:" + line);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Bookmark End Key ">
            //Read bookmark End Key
            buffer.reset();
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (!line.startsWith(BookmarkFolderNode.BOOKMARK_FOLDER_END_KEY)) {
                errorMsg = "Error reading end of Bookmark Folder.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
        // </editor-fold>
        } else {
            errorMsg = "Error reading Bookmark.";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }
    }

    /**Method that write a bookmark in the buffer
     * 
     * @param buffer BufferedWriter.
     * @throws java.io.IOException
     */
    @Override
    public BufferedWriter saveBookmark(BufferedWriter buffer) throws IOException {
        buffer.write(BookmarkFolderNode.BOOKMARK_FOLDER_BEGIN_KEY);
        buffer.newLine();
        buffer.write(BookmarkNode.BOOKMARK_NAME_KEY + this.getName());
        buffer.newLine();
        buffer.write(BookmarkNode.BOOKMARK_DESCRIPTION_BEGIN_KEY);
        buffer.newLine();
        buffer.write(this.getDescription());
        if (!this.getDescription().endsWith(System.getProperty("line.separator"))) {
            buffer.newLine();
        }
        buffer.write(BookmarkNode.BOOKMARK_DESCRIPTION_END_KEY);
        buffer.newLine();
        //We have to write the contents of this folder
        for (int i = 0; i < this.getChildCount(); i++) {
            BookmarkBaseNode base = (BookmarkBaseNode) this.getChildAt(i);
            buffer = base.saveBookmark(buffer);
        }
        buffer.write(BookmarkFolderNode.BOOKMARK_FOLDER_END_KEY);
        buffer.newLine();
        return buffer;
    }
    
    
    /**Method that tell us if this folder can create a new child with that name
     * 
     * @param childName String. This is the Name field of the child node
     * @return boolean. True can create it. False it can not.
     */
    public boolean canCreateChild(String childName){
        for(int i=0;i<this.getChildCount();i++){
            BookmarkBaseNode base = (BookmarkBaseNode) this.getChildAt(i);
            if(base.getName().equals(childName)){
                return false;
            }
        }
        return true;
    }
}
