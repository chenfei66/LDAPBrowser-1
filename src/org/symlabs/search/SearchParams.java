package org.symlabs.search;

import netscape.ldap.LDAPv2;
import org.apache.log4j.Logger;

/**
 * <p>Titulo: SearchParams </p>
 * <p>Descripcion: Class that stores the attributes needed to start a search. These parameters are used in the SearchWin panel. </p>
 * <p>Copyright: Symlabs </p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchParams.java,v 1.3 2009-07-15 16:01:39 efernandez Exp $
 */
public class SearchParams {
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchParams.class);
    /**Attribute that stores the base of the search*/
    private String base;
    /**Attribute that stores the scope of the search, it must be one of the following:
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree) */
    private int scope;
    /**Attribute that stores the search filer*/
    private String filter;
    /**Attribute that stores the attributes that we want to show in the search*/
    private String[] attributes;
    /**Attribute that stores if we want only the attributes or the attributes and their values. 
     * True- the names but not the values of the attributes found. 
     * False- the names and values for attributes found*/
    private boolean attributesOnly;
        /**Attribute that contains the default filter used*/
    private static final String DEFAULT_FILTER = "(objectclass=*)";
    /**Attribute that contains the default attributes shown*/
    private static final String[] DEFAULT_ATTRIBUTES = new String[]{"*"};
    /**Attribute that contains the default value for attributes only parameter*/
    private static final boolean DEFAULT_ATTRS_ONLY = false;
    /**Attribute that contains the default scope*/
    private static final int DEFAULT_SCOPE = LDAPv2.SCOPE_BASE;
    
    // <editor-fold defaultstate="collapsed" desc=" Getter and setters methods ">
    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public boolean isAttributesOnly() {
        return attributesOnly;
    }

    public void setAttributesOnly(boolean attributesOnly) {
        this.attributesOnly = attributesOnly;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    // </editor-fold>
    
        /**Constructor that creates a new instance of search params
     * 
     * @param base String. This is the base distinguished name from which to search
     */
    public SearchParams(String base) {
        this(base, DEFAULT_SCOPE, DEFAULT_FILTER, DEFAULT_ATTRIBUTES, DEFAULT_ATTRS_ONLY);
    }

    /**Constructor that creates a new instance of search params
     * 
     * @param base String. This is the base distinguished name from which to search
     * @param scope int. This is the scope of the entries to search. 
     * You can specify one of the following: 
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree) 
     */
    public SearchParams(String base, int scope) {
        this(base, scope, DEFAULT_FILTER, DEFAULT_ATTRIBUTES, DEFAULT_ATTRS_ONLY);
    }

    /**Constructor that creates a new instance of search params
     * 
     * @param base String. This is the base distinguished name from which to search
     * @param scope int. This is the scope of the entries to search. 
     * You can specify one of the following: 
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree)
     * @param filter String. This is search filter specifying the search criteria
     */
    public SearchParams(String base, int scope, String filter) {
        this(base, scope, filter, DEFAULT_ATTRIBUTES, DEFAULT_ATTRS_ONLY);
    }

    /**Constructor that creates a new instance of search params
     * 
     * @param base String. This is the base distinguished name from which to search
     * @param scope int. This is the scope of the entries to search. 
     * You can specify one of the following: 
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree) 
     * 
     * @param filter String. This is search filter specifying the search criteria
     * @param attributes String[]. This is list of attributes that you want returned in the search results.
     */
    public SearchParams(String base, int scope, String filter, String[] attributes) {
        this(base, scope, filter, attributes, DEFAULT_ATTRS_ONLY);
    }

    /**Constructor that creates a new instance of search params
     * 
     * @param base String. This is the base distinguished name from which to search
     * @param scope int. This is the scope of the entries to search. 
     * You can specify one of the following: 
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree) 
     * 
     * @param filter String. This is search filter specifying the search criteria
     * @param attributes String[]. This is list of attributes that you want returned in the search results.
     * @param attributesOnly boolean. If true, returns the names but not the values of the attributes found. 
     * If false, returns the names and values for attributes found
     */
    public SearchParams(String base, int scope, String filter, String[] attributes, boolean attributesOnly) {
        this.base = base;
        this.scope = scope;
        this.filter = filter;
        this.attributes = attributes;
        this.attributesOnly = attributesOnly;
    }
    
    /**Method that sets the defualt values for a search
     * 
     */
    public void setDefaultValues(){
        this.base = "";
        this.filter = SearchParams.DEFAULT_FILTER;
        this.scope = SearchParams.DEFAULT_SCOPE;
        this.attributes = SearchParams.DEFAULT_ATTRIBUTES;
        this.attributesOnly = SearchParams.DEFAULT_ATTRS_ONLY;
    }
}
