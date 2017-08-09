package org.symlabs.actions.search;

import java.awt.event.ActionEvent;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.actions.DsAction;
import org.symlabs.browser.BrowserDataStatus;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.browser.MessageDialog;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.search.SearchNode;
import org.symlabs.store.ConnectionData;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: LoadSearch </p>
 * <p>Descripcion: Action that loads the search contained in this action.  </p>
 * <p>Copyright: Symlabs  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: LoadSearch.java,v 1.13 2009-09-29 14:26:16 efernandez Exp $
 */
public class LoadSearch extends DsAction {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(LoadSearch.class);
    /**Attribute that contains the search of this action*/
    private SearchNode searchNode;
    /**Attribute that contains the data of this connection*/
    private ConnectionData connectionData;
    /**Attribute that contains the tree root node of this connection*/
    private TreeRootNode treeRootNode;
    /**Attribute that tells us if the search has been stored or not */
    private boolean stored;

    /**Constructor: initializes the descriptión and the mnemonic for this class
     * 
     * @param treeRootNode TreeRootNode.
     * @param search Search.
     * @param stored boolean. True- It means that the search has been stored. False- It means that this search has not been stored.
     */
    public LoadSearch(TreeRootNode treeRootNode, SearchNode search, boolean stored) {
        super(search.getName(), java.awt.event.KeyEvent.VK_H, Utils.createImageIcon(Utils.ICON_SEARCH));
        this.searchNode = search;
        this.connectionData = treeRootNode.getLdapServer().getConnectionData();
        this.treeRootNode = treeRootNode;
        this.stored = stored;
    }

    public void actionPerformed(ActionEvent e) {
        try {

            LDAPSearchResults results = this.treeRootNode.getLdapServer().getLdapOperation().LDAPSearch(this.searchNode.getSearchParams());

            if (results.getCount() > 0) {

                BrowserPanel browserPanel = Utils.getMainWindow().getBrowserPanelByTitle(this.treeRootNode.getLdapServer().getConnectionData().getConfigurationName());

                logger.trace("browserPanel:" + browserPanel);
                logger.trace("browserPanel.getSearchResultsPanel():" + browserPanel.getSearchResultsPanel());
                logger.trace("this.searchNode:" + this.searchNode);
                logger.trace("browserPanel.getSearchResultsPanel().findBrowserData(this.searchNode):" + browserPanel.getSearchResultsPanel().findBrowserData(this.searchNode));

                BrowserDataStatus browserData = browserPanel.getSearchResultsPanel().findBrowserData(this.searchNode);
                if (browserData == null) {
                    logger.trace("the browser data was not found in the searches list");
                    try {
                        logger.trace("HERE 1");
                        this.searchNode.printTreeNodes();

                        logger.trace("HERE 2");
                        //We create the tree root node search, with the connection parameters of the root node of the ldap server connection
                        TreeSearchRootNode searchRootNode = new TreeSearchRootNode(this.connectionData, this.searchNode, this.stored);

                        logger.trace("HERE 3");
                        //We store the tab title of the ldap connection, used to get the browser panel
                        String tabTitle = this.connectionData.getConfigurationName();

                        logger.trace("HERE 4");
                        //We get the select nodeof the connection
                        LDAPNode selectedNode = Utils.getMainWindow().getSelectedNodeOfTab(tabTitle);

                        logger.trace("HERE 5");
                        //We get the browser panel
                        BrowserPanel myPanel = Utils.getMainWindow().getBrowserPanelByTitle(tabTitle);


                        logger.trace("HERE 6");


                        logger.trace("HERE 7");

                        //We add to my panel the buttons of each browser panel
                        if (myPanel.searchResultsPanel.getBrowserData().size() > 0) {
                            logger.trace("HERE 8");
                            myPanel.searchResultsPanel.addBrowserPanel(searchRootNode, searchRootNode);
                        } else {

                            logger.trace("HERE 9-1");
                            myPanel.searchResultsPanel.addBrowserPanel(this.treeRootNode, selectedNode);
                            logger.trace("HERE 9-2");
                            myPanel.searchResultsPanel.addBrowserPanel(searchRootNode, searchRootNode);
                        }
                        logger.trace("HERE 10");

                        //We refresh the node
                        Utils.refreshTreeSearchRootNode(searchRootNode, selectedNode);

                        logger.trace("HERE 11");
                        
                        //We repaint the panel
                        myPanel.validate();
                        myPanel.repaint();
                    } catch (LDAPException exc) {
                        String details = exc.toString();
                        String message = "Error searching in the ldap server.";
                        String title = "Error in Search";
                        MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, details, MessageDialog.MESSAGE_ERROR);
                        dialog.setLocationRelativeTo(Utils.getMainWindow());
                        dialog.setVisible(true);
                        logger.error(message + details);
                        return;
                    } catch (Exception ex) {
                        String details = ex.toString();
                        String message = "Error searching in the ldap server.";
                        String title = "Error in Search";
                        MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, details, MessageDialog.MESSAGE_ERROR);
                        dialog.setLocationRelativeTo(Utils.getMainWindow());
                        dialog.setVisible(true);
                        logger.error(message + details);
                        return;
                    }
                } else {
                    //The search already has been shown, so we have to select it, and set is as visible
                    browserData.getButton().getAction().actionPerformed(null);
                }
            } else {
                String title ="Error in Search";
                String message= "It was not found any result.";
                MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, MessageDialog.MESSAGE_INFORMATION);
                dialog.setLocationRelativeTo(Utils.getMainWindow());
                dialog.setVisible(true);
                logger.trace(title+": "+message);
            }
        } catch (LDAPException ex) {
            logger.error("Error loading search: "+ex);
            String details=ex.toString();
            String message="Error in search params.";
            String title="Error in Search";
            MessageDialog dialog = new MessageDialog(Utils.getMainWindow(), title, message, details, MessageDialog.MESSAGE_ERROR);
            dialog.setLocationRelativeTo(Utils.getMainWindow());
            dialog.setVisible(true);
        }
    }
}

