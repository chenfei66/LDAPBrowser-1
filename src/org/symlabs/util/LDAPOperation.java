package org.symlabs.util;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;
import org.apache.log4j.Logger;
import org.symlabs.search.SearchParams;

/**
 * <p>Titulo: LDAPOperation </p>
 * <p>Descripcion: Class that manages the ldap connection and makes all the available operations </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LDAPOperation.java,v 1.11 2009-08-05 11:50:53 efernandez Exp $
 */
public class LDAPOperation {

    /**Attribute which contains the ldap connection*/
    private LDAPServer ldapServer;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LDAPOperation.class);

    /**Constructor: Initializes the attributes of this class
     * 
     * @param ldapServer LDAPServer. This attribute is used to manage the ldap connection
     */
    public LDAPOperation(LDAPServer ldapServer) {
        this.ldapServer = ldapServer;

    }

    private void LDAPCompare() {

    }

    public LDAPSearchResults LDAPSearch(SearchParams searchParams) throws LDAPException {
        return LDAPSearch(searchParams.getBase(),
                searchParams.getScope(),
                searchParams.getFilter(),
                searchParams.getAttributes(),
                searchParams.isAttributesOnly());
    }

    /**	Method that searches the attributes specified by the parameters. If the connection is diabled, it is enabled.
     * 
     * @param suffix String. This is the base distinguished name from which to search
     * @param scope int. This is the scope of the entries to search. 
     * You can specify one of the following: 
     * LDAPv2.SCOPE_BASE (search only the base DN), 
     * LDAPv2.SCOPE_ONE  (search only entries under the base DN),
     * LDAPv2.SCOPE_SUB  (search the base DN and all entries within its subtree) 
     * 
     * @param filter String. This is search filter specifying the search criteria
     * @param attrs String[]. This is list of attributes that you want returned in the search results.
     * @param attrsOnly boolean. If true, returns the names but not the values of the attributes found. 
     * If false, returns the names and values for attributes found
     * @return LDAPSearchResults. These are the search results.
     * @throws LDAPException Exception. This is the esception thrown if any error is found.
     */
    public LDAPSearchResults LDAPSearch(String suffix, int scope, String filter, String[] attrs, boolean attrsOnly) throws LDAPException {
        LDAPSearchResults results = null;
        try {
            logger.trace("LDAPSearch, suffix:"+suffix+",scope:"+scope+"(0-Base,1-ONE,2-SUB) ,filter:"+filter+",attrsOnly:"+attrsOnly);
            for(int i=0;i<attrs.length;i++){
                logger.trace("LDAPSearch attrs:"+attrs[i]);
            }
            LDAPConnection con = this.ldapServer.getConnection(true);
            //Gets the ldap connection and makes sure that we are connected to server
            results = con.search(suffix, scope, filter, attrs, attrsOnly);
            logger.trace("LDAPSearch, total results: "+results.getCount());
        } catch (LDAPException e) {
            logger.error("Problem searching the suffix (" + suffix + "):" + e.toString());
            throw e;
        }
        return results;
    }

    private void LDAPModify() {

    }

    private void LDAPAdd() {

    }

    private void LDAPDelete() {

    }

    /**Method that resurns all the objectclasses of this base
     * 
     * @param baseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getObjectClasses(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"objectclasses"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }

    /**Method that resurns all the attribute types of this base
     * 
     * @param baseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getAttributeTypes(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"attributetypes"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }

    /**Method that resurns all the matching rules of this base
     * 
     * @param baseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getMatchingRules(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"matchingrules"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }

    /**Method that resurns all the syntaxes of this base
     * 
     * @param baseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getSyntaxes(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"syntaxes"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }

    
        /**Method that resurns all the schema of this base: objectclasses, attributetypes, matchingrules and syntaxes
     * 
     * @param schemaBaseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getFullSchema(String schemaBaseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"objectclasses", "attributetypes", "matchingrules", "ldapsyntaxes"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(schemaBaseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
            logger.trace("The schema contains: "+results.getCount()+" entries");
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }
    /**Method that resurns the schema of this base
     * 
     * @param schemaBaseDn String. This is the base to search
     * @return LDAPSearchResults
     * @throws netscape.ldap.LDAPException
     */
    public LDAPSearchResults getSchema(String schemaBaseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"*"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(schemaBaseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
            logger.trace("The schema contains: "+results.getCount()+" entries");
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }
        return results;
    }

    /**Method that returns the entry associated to the baseDN given as argument
     * 
     * @param baseDn String. This is a dn of an ldap node, where we want to search
     * @return LDAPSearchResults. It returns a result of an LDAPSearch
     * @throws LDAPException If there is an error It is thrown an exception
     */
    public LDAPSearchResults getEntryBaseDN(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"*"};
        String filter = "(objectclass=*)";
        try {
            results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_BASE, filter, attrs, false);
        } catch (LDAPException e) {
            logger.error("Search failed: " + e.getLDAPResultCode() + ", Exception:" + e);
            throw e;
        }

        return results;
    }

    /**Method that return the entries under the baseDN given as argument
     * 
     * @param baseDn String. This is a dn of an ldap node, where we want to search
     * @return LDAPSearchResults. It returns a result of an LDAPSearch
     * @throws LDAPException If there is an error It is thrown an exception
     */
    public LDAPSearchResults getEntriesUnderBaseDN(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"*"};
        String filter = "(objectclass=*)";
        results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_ONE, filter, attrs, false);
        return results;
    }

    /**Method that returns the entries associated to the baseDN given as argument. 
     * It is returned the entry of this dn node and it is also returned the nodes under baseDn
     * 
     * @param baseDn String. This is a dn of an ldap node, where we want to search
     * @return LDAPSearchResults. It returns a result of an LDAPSearch
     * @throws LDAPException If there is an error It is thrown an exception
     */
    public LDAPSearchResults getAllEntriesBaseDN(String baseDn) throws LDAPException {
        LDAPSearchResults results = null;
        String[] attrs = {"*"};
        String filter = "(objectclass=*)";
        results = this.LDAPSearch(baseDn, LDAPv2.SCOPE_SUB, filter, attrs, false);
        return results;
    }
}
