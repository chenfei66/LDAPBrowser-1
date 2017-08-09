package org.symlabs.browser.search;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import org.apache.log4j.Logger;
import org.symlabs.browser.BrowserDataStatus;
import org.symlabs.browser.BrowserPanel;
import org.symlabs.nodes.LDAPNode;
import org.symlabs.nodes.TreeRootNode;
import org.symlabs.nodes.schema.TreeSchemaRootNode;
import org.symlabs.nodes.TreeSearchRootNode;
import org.symlabs.nodes.rootdse.TreeRootDseRootNode;
import org.symlabs.search.SearchNode;

/**
 * <p>Titulo: SearchResultsPanel </p>
 * <p>Descripcion: Panel that manages the searches opened in this connection</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchResultsPanel.java,v 1.17 2009-09-29 09:45:21 efernandez Exp $
 */
public class SearchResultsPanel extends javax.swing.JPanel {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SearchResultsPanel.class);
    /**Attribute that store the maximum number of searches displayed for each connection tab*/
    public static final int MAX_SEARCHES_FOR_CONNECTION = 100;
    /**Attribute used to store the panel where we are going to show the searches and the connection*/
    private BrowserPanel browserPanel;
    /**Attribute that contains the data od all the browser opened*/
    private ArrayList<BrowserDataStatus> browserData;
    /**Attribute that contains the default search name */
    private static final String DEFAULT_SEARCH_NAME = "Search";
    /**Attribute that contains the index of where is stored the connection*/
    public static final int INDEX_CONNECTION = 0;
    /**Attribute that contains the index of where is it stored the schema*/
    public static final int INDEX_SCHEMA = 1;
    /**Attribute that contains the index of where is is stored the root dse*/
    public static final int INDEX_ROOT_DSE = 2;

    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter Methods ">
    public ArrayList<BrowserDataStatus> getBrowserData() {
//        if(this.browserData==null){
//            return new ArrayList<BrowserDataStatus>();
//        }
        return browserData;
    }

    /**Method that tells us if the schema is shown or not is shown
     * 
     * @return boolean
     */
    public boolean isShownSchema() {
        for (int i = 0; i < this.browserData.size(); i++) {
            BrowserDataStatus data = this.browserData.get(i);
            TreeRootNode rootNode = data.getTreeRootNode();
            if (rootNode instanceof TreeSchemaRootNode) {
                return true;
            }
        }
        return false;
    }

    /**Method that returns if the root dse is shown 
     * 
     * @return boolean
     */
    public boolean isShownRootDse() {
        for (int i = 0; i < this.browserData.size(); i++) {
            BrowserDataStatus data = this.browserData.get(i);
            TreeRootNode rootNode = data.getTreeRootNode();
            if (rootNode instanceof TreeRootDseRootNode) {
                return true;
            }
        }
        return false;
    }

    public void setBrowserData(ArrayList<BrowserDataStatus> browserData) {
        this.browserData = browserData;
    }

    public BrowserPanel getBrowserPanel() {
        return browserPanel;
    }

    public void setBrowserPanel(BrowserPanel browserPanel) {
        this.browserPanel = browserPanel;
    }
    // </editor-fold>
    /** Creates new form SearchResultsPanel */
    public SearchResultsPanel() {
        initComponents();
        this.browserData = new ArrayList<BrowserDataStatus>();
        this.setLayout(new GridBagLayout());
    }

    /**Method that adds a new button to the panel and to the arraylist
     * 
     * @param treeRootNode TreeRootNode.
     * @param selectedNode LDAPNode.
     */
    public void addBrowserPanel(TreeRootNode treeRootNode, LDAPNode selectedNode) {//, SearchNode searchNode
//        GridBagConstraints gridBagConstraints = null;
        BrowserDataStatus data = new BrowserDataStatus(treeRootNode, selectedNode);
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
//        gridBagConstraints.weightx = 0.0;
//        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
//        gridBagConstraints.gridx = this.browserData.size();
//        gridBagConstraints.gridy = 0;
        if (treeRootNode instanceof TreeSearchRootNode) {
            if (this.browserData.size() < MAX_SEARCHES_FOR_CONNECTION) {
                this.browserData.add(data);
                this.add(data.getButton());
            }
        } else if (treeRootNode instanceof TreeSchemaRootNode) {//it is the schema show it must be the second position of the array list
            this.browserData.add(INDEX_SCHEMA, data);
            this.removeAll();
            for (int i = 0; i < this.browserData.size(); i++) {
                this.add(this.browserData.get(i).getButton());
            }
        } else if (treeRootNode instanceof TreeRootDseRootNode) {
            if (this.browserData.size() > 1) {
                this.browserData.add(INDEX_ROOT_DSE, data);
            } else {
                this.browserData.add(1, data);
            }
            this.removeAll();
            for (int i = 0; i < this.browserData.size(); i++) {
                this.add(this.browserData.get(i).getButton());
            }
        } else { //Is a tree root node so this must be the first position of the arraylist
            this.browserData.add(INDEX_CONNECTION, data);
            this.removeAll();
            for (int i = 0; i < this.browserData.size(); i++) {
                this.add(this.browserData.get(i).getButton());
            }
        }

        this.setSelectedButton(data);
    }

    /**Mehod that removed from the list the search displayed
     * 
     * @param searchNode SearchNode.
     */
    public void removeSearchFromBrowserPanel(SearchNode searchNode) {
        BrowserDataStatus data = this.findBrowserData(searchNode);
        this.removeBrowserDataFromPanel(data);
    }

    /**Method that removes from the list the schema displayed
     * 
     */
    public void removeSchemaFromBrowserPanel() {
        if (this.browserData.size() > 0) {
            BrowserDataStatus data = this.browserData.get(INDEX_SCHEMA);
            if (data.getTreeRootNode() instanceof TreeSchemaRootNode) {
                this.removeBrowserDataFromPanel(data);
            }
        }
    }

    /**Method that removes from the list the root dse displayed
     * 
     */
    public void removeRootDseFromBrowserPanel() {
        if (this.browserData.size() > 0) {
            for (int i = 0; i < this.browserData.size(); i++) { //We have to make a loop because the index of the root dse must be 0 or 1
                BrowserDataStatus data = this.browserData.get(i);
                if (data.getTreeRootNode() instanceof TreeRootDseRootNode) {
                    this.removeBrowserDataFromPanel(data);
                    return;
                }
            }
        }
    }

    /**Method that removes from the panel the BrowserDataStatus data given as argument
     * 
     * @param data BrowserDataStatus
     */
    private void removeBrowserDataFromPanel(BrowserDataStatus data) {
        if (this.browserData.size() > 1) {
            logger.trace("We remove the search, button=" + data.getButton().toString() + "," + data.getButton().getText());

            //We have to remove the button from this panel
            this.remove(data.getButton());

            //We have to remove this BrowserData from the arrayList
            this.browserData.remove(data);
        }

        if (this.browserData.size() == 1) {
            //We also have to remove the button and the browserData of connection tree

            //We remove the button
            this.remove(this.browserData.get(0).getButton());

            //We remove this element from the arraylist
            this.browserData.remove(this.browserData.get(0));
        }
    }

    /**Method that returns the browser data of the button selected
     * 
     * @param button JButton. This is the button pressed
     * @return BrowserDataStatus. This is the browser data of the button given as argument. 
     * If the button was not found then it would be returned null.
     */
    public BrowserDataStatus getBrowserData(JButton button) {
        for (int i = 0; i < this.browserData.size(); i++) {
            if (this.browserData.get(i).getButton().equals(button)) {
                return this.browserData.get(i);
            }
        }
        return null;
    }

    /**Method that returns the browser data owhich matches with the treeRootNode
     * 
     * @param treeRootNode TreeRootNode.
     * @return BrowserDataStatus. This is the browser data of the button given as argument. 
     * If the button was not found then it would be returned null.
     */
    public BrowserDataStatus getBrowserData(TreeRootNode treeRootNode) {
        for (int i = 0; i < this.browserData.size(); i++) {
            if (this.browserData.get(i).getTreeRootNode().equals(treeRootNode)) {
                return this.browserData.get(i);
            }
        }
        return null;
    }

    /**Method that returns the browser data which matches with the searchNode given
     * 
     * @param searchNode SearchNode. This is the search that we want to get its browser data
     * @return BrowserDataSatus. It is returned the browser data that contains its search
     */
    public BrowserDataStatus findBrowserData(SearchNode searchNode) {
        if (searchNode == null) {
            return this.browserData.get(0);//We return the first element, this is the ldap server connection tree
        }

        for (int i = 1; i < this.browserData.size(); i++) {
            TreeRootNode rootNode = this.browserData.get(i).getTreeRootNode();
            if (rootNode instanceof TreeSearchRootNode) {
                if (((TreeSearchRootNode) rootNode).getSearchNode().equals(searchNode)) {
                    return this.browserData.get(i);
                }
            }
        }

        return null;
    }

    /**Method that returns the next available name to create a default search
     * 
     * @return String. This is the next available default search name
     */
    public String getDefaultSearchName() {
        String defaultName = SearchResultsPanel.DEFAULT_SEARCH_NAME;
        int cont = 1;
        defaultName += cont;
        boolean found = false;
        boolean exit = false;
        while (!exit) {
            for (int i = 1; i < this.browserData.size(); i++) {//i= 1 because the first index is the tree root node of the ldap server connection
                BrowserDataStatus data = this.browserData.get(i);
                TreeRootNode rootNode = data.getTreeRootNode();
                if (rootNode instanceof TreeSearchRootNode) {
                    TreeSearchRootNode searchNode = (TreeSearchRootNode) rootNode;
                    if (searchNode.getSearchNode().getName().equals(defaultName)) {
                        found = true;
                    }
                }
            }
            if (!found) {
                exit = true;
            } else {
                cont++;
                defaultName = SearchResultsPanel.DEFAULT_SEARCH_NAME;
                defaultName += cont;
                logger.trace("we update the default name to: " + defaultName);
                found = false;
            }
        }
        return defaultName;
    }

    /**Method that sets as selected the button specific by the argument, The other buttons are not selected
     * 
     * @param browserData BrowsreDataStatus. This is the browser data that contains the button to set as selected
     */
    public void setSelectedButton(BrowserDataStatus browserData) {
        for (int i = 0; i < this.browserData.size(); i++) {
            BrowserDataStatus data = this.browserData.get(i);
            if (data.equals(browserData)) {
                data.getButton().setSelected(true);
                data.setButtonEnabled(true);
            } else {
                data.getButton().setSelected(false);
                data.setButtonEnabled(false);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
