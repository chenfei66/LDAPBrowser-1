package org.symlabs.bookmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: BookmarkBase </p>
 * <p>Descripcion: Class that manages a bookmark base. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: BookmarkBaseNode.java,v 1.3 2009-07-29 14:03:27 efernandez Exp $
 */
public abstract class BookmarkBaseNode extends DefaultMutableTreeNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(BookmarkBaseNode.class);
    /**Attribute that contains the type of this bookmark*/
    public String type;
    /**Attribute that contains the name of the bookmark*/
    protected String name;
    /**Attribute that contains the description of the bookmark*/
    protected String description;
    /**Attribute that contains the key used to identify the bookmark folder separator*/
    public static final String BOOKMARK_FOLDER_SEPARATOR = "" + File.separatorChar;
    /**Attribute that stores the bookmark name*/
    protected static final String BOOKMARK_NAME_KEY = "bookmarkname:";
    /**Attribute that stores the bookmark description*/
    protected static final String BOOKMARK_DESCRIPTION_BEGIN_KEY = "bookmarkdescriptionbegin:";
    /**Attribute that stores the bookmark description*/
    protected static final String BOOKMARK_DESCRIPTION_END_KEY = "bookmarkdescriptionend:";
    /**Attribute that stores the type of bookmark base type*/
    private static final String TYPE_BOOKMARK_BASE = "bookmarkBaseType";
    /**Attribute that contains the icon for this node*/
    protected String iconPath;
    /**Attribute that contains the image icon for this node*/
    protected ImageIcon imageIcon;

    // <editor-fold defaultstate="collapsed" desc=" Getter and setter methods ">
    /**Method that returns the description of this bookmark
     * 
     * @return String.
     */
    public String getDescription() {
        return description;
    }

    /**Method that sets the decription of this bookmark
     * 
     * @param description String.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**Method that returns the name of this bookmark. 
     * This name is shown in the menu.
     * 
     * @return String,
     */
    public String getName() {
        return name;
    }

    /**MEthod that sets the name of this bookmark.
     * This name is hown in the menu
     * 
     * @param name String.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**Method that returns the imagen icon of this node
     * 
     * @return ImageIcon
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

// </editor-fold>
    public BookmarkBaseNode() {
        this.type = TYPE_BOOKMARK_BASE;
        this.iconPath = Utils.ICON_DEFAULT_ICON;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }
    
    /**Method that write a bookmark in the buffer
     * 
     * @param buffer BufferedWriter.
     * @return BufferedWriter.
     * @throws java.io.IOException
     */
    public BufferedWriter saveBookmark(BufferedWriter buffer) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        return this.name;
    }
    

    @Override
    public Object clone() {
        if(this instanceof BookmarkNode){
            BookmarkNode myNode = (BookmarkNode) this;
            BookmarkNode searchNode = new BookmarkNode(myNode);
            return searchNode;
        }else if(this instanceof BookmarkFolderNode){
            BookmarkFolderNode myNode =(BookmarkFolderNode) this;
            BookmarkFolderNode searchFolderNode = new BookmarkFolderNode(myNode);
            if(myNode.getChildCount()>0){
                for(int i=0;i<myNode.getChildCount();i++){
                    BookmarkBaseNode childNode = (BookmarkBaseNode) this.getChildAt(i);
                    searchFolderNode.add((DefaultMutableTreeNode) childNode.clone());
                }
            }
            return searchFolderNode;
        }
        return null;
    }
    
    /**Method that updates the dn of this bookmarks and the bookmarks contained
     * 
     * @param oldDn String. This is the old dn to search
     * @param newDn String. This si the new dn  to replace
     */
    public void updateDnOfBookmarks(String oldDn,String newDn){
        if(this instanceof BookmarkNode){
            BookmarkNode node = (BookmarkNode) this;
            if(node.getDn().equals(oldDn)){
                logger.trace("We update the bookmark node:"+node.getName()+", node.dn:"+node.getDn());
                node.setDn(newDn);
            }
        }else if(this instanceof BookmarkFolderNode){
            for(int i=0;i<this.getChildCount();i++){
                BookmarkBaseNode base = (BookmarkBaseNode) this.getChildAt(i);
                logger.trace("Weare going to update the bookmark node:"+base.getName());
                base.updateDnOfBookmarks(oldDn, newDn);
            }
        }
    }
    
    /**Method that removes the bookmarks with the snToSearch given as argument
     * 
     * @param dnToSearch String. This is the dn to search
     */
    public void removeBoomarkWithDn(String dnToSearch) {
        if(this instanceof BookmarkNode){
            BookmarkNode node = (BookmarkNode) this;
            if(node.getDn().equals(dnToSearch)){
                BookmarkBaseNode parentNode = (BookmarkBaseNode) node.getParent();
                parentNode.remove(node);
            }
        }else if(this instanceof BookmarkFolderNode){
          for(int i=0;i<this.getChildCount();i++){
              BookmarkBaseNode base = (BookmarkBaseNode) this.getChildAt(i);
              base.removeBoomarkWithDn(dnToSearch);
          }  
        }
    }
}
