package org.symlabs.search;

import org.symlabs.search.SearchNode;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.browser.MessageDialog;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SavedSearch </p>
 * <p>Descripcion: Class that manages the search params and gets the results of the search. </p>
 * <p>Copyright: Symlabs </p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SavedSearch.java,v 1.5 2009-08-24 09:01:06 efernandez Exp $
 */
public class SavedSearch {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SavedSearch.class);
    /**Attribute that contains the parameters of the ldap connection*/
    private LDAPServer ldapServer;
    /**Attribute that contians the results of the search*/
    private LDAPSearchResults results;
    /**Attribute that contains the parameters needed to the search*/
    private SearchParams searchParams;
    
    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter methods ">
    public LDAPServer getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(LDAPServer ldapServer) {
        this.ldapServer = ldapServer;
    }

    public LDAPSearchResults getResults() {
        return results;
    }

    public void setResults(LDAPSearchResults results) {
        this.results = results;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }
    // </editor-fold>
        
    /** Constructor: It creates a new instance of saved search. Used to get the results of a search.
     *   You should use a setter methods to set the values needed: LDAPServer and SearchParams 
     * 
     */
    public SavedSearch(){
        this.ldapServer=null;
        this.searchParams=null;
        this.results=null;
    }
    
    /** Constructor: It creates a new instance of saved search. Used to get the results of a search
     * 
     * @param ldapServer LDAPServer. It contains the parameters required to stablish an ldap connection
     * @param searchParams SearchParams. It contains the parameters required to the search
     */
    public SavedSearch(LDAPServer ldapServer, SearchParams searchParams) {
        this.ldapServer = ldapServer;
        this.searchParams = searchParams;
        try {
            this.results = this.ldapServer.getLdapOperation().LDAPSearch(this.searchParams);
        } catch (LDAPException e) {
            this.results=null;
            String message = "Error searching in base: "+this.searchParams.getBase();
            String title = "Search Error";
            String details = message +"\n"+ e;
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(),title, message, details, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
            logger.error(message + " "+ details);
        }
    }
}
