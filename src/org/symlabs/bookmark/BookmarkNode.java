package org.symlabs.bookmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: Bookmark </p>
 * <p>Descripcion: Class that manages a bookmark </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BookmarkNode.java,v 1.2 2009-07-29 11:09:24 efernandez Exp $
 */
public class BookmarkNode extends BookmarkBaseNode {

    /**Attribute that contains the dn of the bookmark*/
    private String dn;
    /**Attribute that stores the bookmark key used to store a configuration*/
    public static final String BOOKMARK_BEGIN_KEY = "bookmarkbegin:";
    /**Attribute that stores the bookmark key used to store a configuration*/
    private static final String BOOKMARK_END_KEY = "bookmarkend:";
    /**Attribute that stores the bookmark dn*/
    private static final String BOOKMARK_DN_KEY = "bookmarkdn:";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BookmarkNode.class);
    /**Attribute that stores the type of bookmark base type*/
    public static final String TYPE_BOOKMARK = "bookmarkType";

    public BookmarkNode(BookmarkNode node) {
        this(node.getDn(),node.getName(),node.getDescription());
    }

    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter methods ">
    /**Method that returns the dn of this bookmark
     * 
     * @return Stirng.
     */
    public String getDn() {
        return dn;
    }

    /**MEthod that sets the dn of this bookmark
     * 
     * @param dn String.
     */
    public void setDn(String dn) {
        this.dn = dn;
    }

    // </editor-fold>
    /**Constructor: Creates a new empty instance of Bookmark
     * 
     */
    public BookmarkNode() {
        this("", "", "");
    }

    /**Creates a new instance of bookmark with a dn and a name
     * 
     * @param dn String. This is the dn of this bookmark
     * @param name String, This is the name used to identify this bookmark. It is displayed in the menu options
     */
    public BookmarkNode(String dn, String name) {
        this(dn, name, "");
    }

    /**Creates a new instance of bookmark
     * 
     * @param dn String. This is the dn of this bookmark
     * @param name String, This is the name used to identify this bookmark. It is displayed in the menu options
     * @param description String.
     */
    public BookmarkNode(String dn, String name, String description) {
        this.dn = dn;
        this.name = name;
        this.description = description;
        this.type = TYPE_BOOKMARK;
        this.iconPath = Utils.ICON_BOOKMARK;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }

    /**Method that creates a bookmark. This bookmark is read from the buffer given as argument
     * 
     * @param buffer BufferedReader. This buffer contains the bookmark to create
     * @throws java.io.IOException 
     */
    public BookmarkNode(BufferedReader buffer) throws IOException {
        this.type = TYPE_BOOKMARK;
        this.iconPath = Utils.ICON_BOOKMARK;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
        String line = buffer.readLine();
        String errorMsg = "";
        if (line.startsWith(BookmarkNode.BOOKMARK_BEGIN_KEY)) {

            // <editor-fold defaultstate="collapsed" desc=" Read DN ">
            //Dn
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(BookmarkNode.BOOKMARK_DN_KEY)) {
                this.dn = line.substring(BookmarkNode.BOOKMARK_DN_KEY.length(), line.length());
            } else {
                errorMsg = "Error reading dn from Bookmark.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Name ">
            //Name
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(BookmarkBaseNode.BOOKMARK_NAME_KEY)) {
                this.name = line.substring(BookmarkNode.BOOKMARK_NAME_KEY.length(), line.length());
            } else {
                errorMsg = "Error reading name from Bookmark.";
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

            // <editor-fold defaultstate="collapsed" desc=" Read Bookmark end key ">
            //Read bookmark end key
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (!line.startsWith(BookmarkNode.BOOKMARK_END_KEY)) {
                errorMsg = "Error reading identifier from Bookmark.";
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
     * @return BufferedWriter.
     * @throws java.io.IOException
     */
    @Override
    public BufferedWriter saveBookmark(BufferedWriter buffer) throws IOException {
        buffer.write(BookmarkNode.BOOKMARK_BEGIN_KEY);
        buffer.newLine();
        buffer.write(BookmarkNode.BOOKMARK_DN_KEY + this.getDn());
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
        buffer.write(BookmarkNode.BOOKMARK_END_KEY);
        buffer.newLine();
        return buffer;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
