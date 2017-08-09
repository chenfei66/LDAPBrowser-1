package org.symlabs.search;

import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SearchBase </p>
 * <p>Descripcion: Class used to store the data of a search node. 
 * This is an abstract class used to create instances of  search folder and a search params</p>
 * <p>Copyright: Symlabs </p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchBaseNode.java,v 1.3 2009-07-29 11:09:24 efernandez Exp $
 */
public abstract class SearchBaseNode extends DefaultMutableTreeNode{

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchBaseNode.class);
    /**Attribute that contains the name of the bookmark*/
    protected String name;
    /**Attribute that contains the description of the bookmark*/
    protected String description;
    /**Attribute that stores the bookmark name*/
    protected static final String SEARCH_NAME_KEY = "searchname:";
    /**Attribute that stores the bookmark description*/
    protected static final String SEARCH_DESCRIPTION_BEGIN_KEY = "searchdescriptionbegin:";
    /**Attribute that stores the bookmark description*/
    protected static final String SEARCH_DESCRIPTION_END_KEY = "searachdescriptionend:";
    /**Attribute that contains the icon for this node*/
    protected String iconPath;
    /**Attribute that contains the image icon for this node*/
    protected ImageIcon imageIcon;

    // <editor-fold defaultstate="collapsed" desc=" Getter and setter methods ">
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
    
    // </editor-fold>
    
    public SearchBaseNode(){
        this.iconPath = Utils.ICON_DEFAULT_ICON;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }
    
    /**Method used to save the params contained in this class in the search
     * 
     * @param buffer BufferedWriter
     * @return BufferedWriter
     * @throws java.io.IOException
     */
    public BufferedWriter saveSearch(BufferedWriter buffer) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    /**Method that prints the name of the searchBaseNode, and their child nodes
     * 
     */
    public void printTreeNodes(){
       logger.trace("Name: "+this.getName());
        for(int i=0;i<this.getChildCount();i++){
            SearchBaseNode node = (SearchBaseNode) this.getChildAt(i);
            node.printTreeNodes();
        }
    }

    @Override
    public Object clone() {
        if(this instanceof SearchNode){
            SearchNode myNode = (SearchNode) this;
            SearchNode searchNode = new SearchNode(myNode);
            return searchNode;
        }else if(this instanceof SearchFolderNode){
            SearchFolderNode myNode =(SearchFolderNode) this;
            SearchFolderNode searchFolderNode = new SearchFolderNode(myNode);
            if(myNode.getChildCount()>0){
                for(int i=0;i<myNode.getChildCount();i++){
                    SearchBaseNode childNode = (SearchBaseNode) this.getChildAt(i);
                    searchFolderNode.add((DefaultMutableTreeNode) childNode.clone());
                }
            }
            return searchFolderNode;
        }
        return null;
    }
    
}
