package org.symlabs.nodes;

import java.util.Vector;
import org.apache.log4j.Logger;
import org.symlabs.actions.rootdse.ShowRootDse;
import org.symlabs.actions.schema.ShowSchema;
import org.symlabs.actions.tab.CloseTreeRootNode;
import org.symlabs.actions.search.RefreshSearch;
import org.symlabs.actions.search.SaveSearch;
import org.symlabs.actions.search.SearchProperties;
import org.symlabs.actions.tab.TabProperties;
import org.symlabs.browser.MessageDialog;
import org.symlabs.search.SearchNode;
import org.symlabs.store.ConnectionData;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: TreeSearchRootNode </p>
 * <p>Descripcion: Class that manages the searches nodes af an ldap connection.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TreeSearchRootNode.java,v 1.13 2009-09-29 09:45:21 efernandez Exp $
 */
public class TreeSearchRootNode extends TreeRootNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(TreeSearchRootNode.class);
    /**Attribute that contains the search shown*/
    private SearchNode searchNode;
    /**Attribute that tells us if the search has been stored or not */
    private boolean stored;
    /**Attribute that contains the dn found in the search*/
    private Vector<String> searchResults;

    /**Method that returns the dn results found for this search
     * 
     * @return Vector<String>
     */
    public Vector<String> getSearchResults() {
        return searchResults;
    }

    /**Method that sets the elements of this search
     * 
     * @param searchResults Vector<String>. This vector contains the dn founds for this search
     */
    public void setSearchResults(Vector<String> searchResults) {
        this.searchResults = searchResults;
    }

    /**Method that adds the dn given as argument as a new dn search result
     * 
     * @param dnResult String. 
     */
    public void addSearchResults(String dnResult){
        if(!this.searchResults.contains(dnResult)){
            this.searchResults.addElement(dnResult);
        }
    }
    
    
    /**Method that tells us if the search has been stored or not
     * 
     * @return boolean. True- the search has been stored, False- has not been stored.
     */
    public boolean isStored() {
        return stored;
    }

    /**Method that sets if the search has been stored.
     * 
     * @param stored boolean. True- the search has been stored. False- the search has not been stored.
     */
    public void setStored(boolean stored) {
        this.stored = stored;
    }

    /**Method that returns the search node contained in this tree search root node
     * 
     * @return SearchNode
     */
    public SearchNode getSearchNode() {
        return searchNode;
    }

    /**Constructor: Creates a new instance if TreeSearchRootNode
     * 
     * @param connectionData ConnectionData
     * @param search SearchNode.
     * @param stored boolean. True- It means that the search has been stored. False- It means that this search has not been stored.
     * @throws java.lang.Exception
     */
    public TreeSearchRootNode(ConnectionData connectionData, SearchNode search,boolean stored) throws Exception {
        super(connectionData);
        this.searchResults = new Vector<String>();
        this.searchNode = search;
        this.stored=stored;
        //We set the rdn name for this root node
        this.myRDN = search.getName();
        this.myDN = search.getName();

        //We set the icon for this root node
        this.setMyIconPath(Utils.ICON_SEARCH);

        this.actionsList.add(CloseTreeRootNode.HashKey);
        this.actionsList.add(RefreshSearch.HashKey);
        
        if(!stored){
            this.actionsList.add(SaveSearch.HashKey);
        }else{
            this.actionsList.add(SearchProperties.HashKey);
        }
        this.actionsList.remove(TabProperties.HashKey);
        this.actionsList.remove(ShowSchema.HashKey);
        this.actionsList.remove(ShowRootDse.HashKey);
    }

    @Override
    public void setEnabledActions() {
        super.setEnabledActions();
        this.supportedActions.put(CloseTreeRootNode.HashKey, "yes");
        this.supportedActions.put(RefreshSearch.HashKey, "yes");
        
        if(!stored){
            this.supportedActions.put(SaveSearch.HashKey, "yes");
        }else{
            this.supportedActions.put(SearchProperties.HashKey, "yes");
        }
    }
    
    /**Method that loads all entries for the current suffixes.
     * If there is no suffix selected, then it is created an empty root with all suffixes.
     * 
     */
    @Override
    public void loadLDAPSuffix() {
        String rootDN = null;
        String errorMessage = "";
        String suffixes[] = null;
        String title = "Connection Error";
        String message = "";
        //First we get the suffixes stored in the ldapServer
        try {
            suffixes = this.ldapServer.getSuffixes();
        } catch (Exception e) {
            errorMessage += e.getMessage() + "\n";
            message = "Error getting the suffixes.";
            logger.error(errorMessage);
        }

        //There is only one suffix
        if (suffixes != null && suffixes.length == 1) {
            rootDN = suffixes[0];
            try {
                //We create the suffix node
                this.createSuffix(rootDN,this);
            } catch (Exception e) {
                errorMessage += e.getMessage() + "\n";
                message = "Error creating the suffix: " + rootDN;
                logger.error(errorMessage);
            }
        } else { //There are more than one suffix
            if (suffixes != null) {
                logger.trace("load suffix 1");
                Vector<String> vSuffixes = new Vector<String>();
                for (int i = 0; i < suffixes.length; i++) {
                    vSuffixes.addElement(suffixes[i]);
                }
                logger.trace("load suffix 2");
                Vector<String> commonSuffixes = this.getCommonDNFromSuffixes(vSuffixes);
                logger.trace("load suffix 3 " + commonSuffixes.size());
                if (commonSuffixes.size() != suffixes.length) {
                    //We have to remove from vSuffixes the suffixes that does not match with the searchResults
                    vSuffixes = this.removeSuffixesFoundInSearchResults(vSuffixes,this.searchResults);
                    logger.trace("load suffix 4, ");
                    try {
                        this.loadSuffixes(vSuffixes, this);
                    } catch (Exception e) {
                        errorMessage = e.getMessage();
                        logger.error(errorMessage);
                    }
                } else { //there are not any common suffix, it means there are not any virtual node
                    logger.trace("load suffix 5");
                    //We create a node for each suffix
                    for (int i = 0; i < suffixes.length; i++) {
                        try {
                            //We create the suffix node
                            this.createSuffix(suffixes[i],this);
                        } catch (Exception e) {
                            errorMessage += e.getMessage() + "\n";
                            message = "Error creating suffix: " + suffixes[i]+" in search.";
                            logger.error(errorMessage);
                        }
                    }
                }
            }
        }

        if (!errorMessage.equals("")) {
            logger.error(errorMessage);
            message = "Error loading suffixes.";
            MessageDialog errorDialog = new MessageDialog(Utils.getMainWindow(), title, message, errorMessage, MessageDialog.MESSAGE_ERROR);
            errorDialog.setLocationRelativeTo(Utils.getMainWindow());
            errorDialog.setVisible(true);
        }
    }

    private Vector<String> removeSuffixesFoundInSearchResults(Vector<String> vSuffixes, Vector<String> searchResults) {
        Vector <String> vFound = new Vector<String>();
        for(int i=0;i<searchResults.size();i++){
            for(int j=0;j<vSuffixes.size();j++){
                if(searchResults.elementAt(i).endsWith(vSuffixes.elementAt(j))){
                    logger.trace("We add the suffix: "+vSuffixes.elementAt(j));
                    vFound.addElement(vSuffixes.elementAt(j));
                }
            }
        }
        return vFound;
    }
}

