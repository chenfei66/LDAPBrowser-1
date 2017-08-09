package org.symlabs.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: Search </p>
 * <p>Descripcion: Class that stores the attributes needed for a search node. These parameters are used in the SearchWin panel. </p>
 * <p>Copyright: Symlabs </p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchNode.java,v 1.2 2009-07-29 11:09:24 efernandez Exp $
 */
public class SearchNode extends SearchBaseNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchNode.class);
    /**Attribute used to identify the end of the search params*/
    private static final String BASE_KEY = "searchparamsbasekey:";
    /**Attribute used to identify the end of the search params*/
    private static final String SCOPE_KEY = "searchparamsscopekey:";
    /**Attribute used to identify the end of the search params*/
    private static final String FILTER_KEY = "searchparamsfilterkey:";
    /**Attribute used to identify the end of the search params*/
    private static final String ATTRIBUTES_BEGIN_KEY = "searchparamsattributesbeginkey:";
    /**Attribute used to identify the end of the search params*/
    private static final String ATTRIBUTES_END_KEY = "searchparamsattributesendkey:";
    /**Attribute used to identify the end of the search params*/
    private static final String ATTRS_ONLY_KEY = "searchparamsattrsonlykey:";
    /**Attribute that stores the bookmark key used to store a configuration*/
    public static final String SEARCH_BEGIN_KEY = "searchparamsbeginkey:";
    /**Attribute that stores the bookmark key used to store a configuration*/
    private static final String SEARCH_END_KEY = "searchparamsendkey:";
    /**Attribute that contains the search params needed for a search*/
    private SearchParams searchParams;

    public SearchNode(SearchNode searchNode) {
        this(searchNode.getName(),searchNode.getDescription(),searchNode.searchParams);
    }

    // <editor-fold defaultstate="collapsed" desc=" Getter ans setter methods ">
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }
    // </editor-fold>    
    /**Constructor that creates a new instance of search params
     * 
     * @param name String. This is the name of the search
     * @param description String. This is the description of this search
     * @param params SearchParams. It contains the search params needed for the search
     */
    public SearchNode(String name, String description, SearchParams params) {
        this.searchParams = params;
        this.name = name;
        this.description = description;
        this.iconPath = Utils.ICON_SEARCH;
        this.imageIcon = Utils.createImageIcon(this.iconPath);
    }

    public SearchNode(BufferedReader buffer) throws IOException {
        String line = buffer.readLine();
        String errorMsg = "";
        this.searchParams = new SearchParams("");

        if (line.startsWith(SearchNode.SEARCH_BEGIN_KEY)) {

            // <editor-fold defaultstate="collapsed" desc=" Read Base ">
            //Base
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchNode.BASE_KEY)) {
                this.searchParams.setBase(line.substring(SearchNode.BASE_KEY.length(), line.length()));
            } else {
                errorMsg = "Error reading base from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Scope ">
            //Scope
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchNode.SCOPE_KEY)) {
                this.searchParams.setScope(Integer.parseInt(line.substring(SearchNode.SCOPE_KEY.length(), line.length())));
            } else {
                errorMsg = "Error reading scope from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Filter ">
            //Filter
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchNode.FILTER_KEY)) {
                this.searchParams.setFilter(line.substring(SearchNode.FILTER_KEY.length(), line.length()));
            } else {
                errorMsg = "Error reading filter from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Attributes ">
            //Attributes
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchNode.ATTRIBUTES_BEGIN_KEY)) {
                line = buffer.readLine();
                logger.trace("line:" + line);
                ArrayList<String> attrs = new ArrayList<String>();
                while (!line.equals(SearchNode.ATTRIBUTES_END_KEY)) {
                    attrs.add(line);
                    line = buffer.readLine();
                }
                //We copy the elements of the arraylist to the String[] attributes
                String attributes[] = this.searchParams.getAttributes();
                attributes = new String[attrs.size()];
                for (int i = 0; i < attrs.size(); i++) {
                    attributes[i] = attrs.get(i);
                }
            } else {
                errorMsg = "Error reading attributes from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Attrs only ">
            //Attrs only
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchNode.ATTRS_ONLY_KEY)) {
                String attrsOnly = line.substring(SearchNode.ATTRS_ONLY_KEY.length(), line.length());
                if (attrsOnly.equalsIgnoreCase("yes")) {
                    this.searchParams.setAttributesOnly(true);
                } else {
                    this.searchParams.setAttributesOnly(false);
                }
            } else {
                errorMsg = "Error reading attributes only from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Name ">
            //Name
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (line.startsWith(SearchBaseNode.SEARCH_NAME_KEY)) {
                this.name = line.substring(SearchBaseNode.SEARCH_NAME_KEY.length(), line.length());
            } else {
                errorMsg = "Error reading name from Search.";
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
                errorMsg = "Error reading description from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" Read Search end key ">
            //Read bookmark end key
            line = buffer.readLine();
            logger.trace("line:" + line);
            if (!line.startsWith(SearchNode.SEARCH_END_KEY)) {
                errorMsg = "Error reading search params end key from Search.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            // </editor-fold>

            this.iconPath = Utils.ICON_SEARCH;
            this.imageIcon = Utils.createImageIcon(this.iconPath);
        } else {
            errorMsg = "Error reading Search.";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }
    }

    /**Method that writes in the buffer given as argument the params of this Search
     * 
     * @param buffer BufferedWriter
     * @return BufferedWriter. It is added the parameters od this Search
     * @throws java.io.IOException 
     */
    @Override
    public BufferedWriter saveSearch(BufferedWriter buffer) throws IOException {
        buffer.write(SearchNode.SEARCH_BEGIN_KEY);
        buffer.newLine();
        buffer.write(SearchNode.BASE_KEY + this.searchParams.getBase());
        buffer.newLine();
        buffer.write(SearchNode.SCOPE_KEY + this.searchParams.getScope());
        buffer.newLine();
        buffer.write(SearchNode.FILTER_KEY + this.searchParams.getFilter());
        buffer.newLine();
        buffer.write(SearchNode.ATTRIBUTES_BEGIN_KEY);
        buffer.newLine();
        String[] attributes = this.searchParams.getAttributes();
        for (int i = 0; i < attributes.length; i++) {
            buffer.write(attributes[i]);
            buffer.newLine();
        }
        buffer.write(SearchNode.ATTRIBUTES_END_KEY);
        buffer.newLine();
        String attrsOnly = "";
        if (this.searchParams.isAttributesOnly()) {
            attrsOnly = "yes";
        } else {
            attrsOnly = "no";
        }
        buffer.write(SearchNode.ATTRS_ONLY_KEY + attrsOnly);
        buffer.newLine();
        buffer.write(SearchNode.SEARCH_NAME_KEY + this.getName());
        buffer.newLine();
        buffer.write(SearchNode.SEARCH_DESCRIPTION_BEGIN_KEY);
        buffer.newLine();
        buffer.write(this.getDescription());
        if (!this.getDescription().endsWith(System.getProperty("line.separator"))) {
            buffer.newLine();
        }
        buffer.write(SearchNode.SEARCH_DESCRIPTION_END_KEY);
        buffer.newLine();
        buffer.write(SearchNode.SEARCH_END_KEY);
        buffer.newLine();
        return buffer;
    }
}
