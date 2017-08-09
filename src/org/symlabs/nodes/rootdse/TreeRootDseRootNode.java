package org.symlabs.nodes.rootdse;

import org.symlabs.nodes.*;
import org.symlabs.actions.tab.CloseTreeRootNode;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: TreeRootDseRootNode </p>
 * <p>Descripcion: Class that manages the root dse of the ldap browser.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TreeRootDseRootNode.java,v 1.1 2009-08-04 08:53:51 efernandez Exp $
 */
public class TreeRootDseRootNode extends TreeRootNode {

    /**Attribute that contains the default rdn of the schema*/
    private final static String DEFAULT_RDN = "Root DSE";
    /**Attribute that contains the defualt dn of the schema*/
    private final static String DEFAULT_DN = "Root DSE";

    /**Contructor: Creates a new instance of a tree schema root node. Used to show the ldap server schema
     * 
     * @param ldapServer LDAPServer
     * @throws java.lang.Exception
     */
    public TreeRootDseRootNode(LDAPServer ldapServer) throws Exception {
        super(ldapServer.getConnectionData());

        //We set the rdn name for this root node
        this.myRDN = DEFAULT_RDN;
        this.myDN = DEFAULT_DN + " of " + ldapServer.getConnectionData().getConfigurationName();

        //We set the icon for this root node
        this.setMyIconPath(Utils.ICON_ROOT_DSE);

        this.actionsList.clear();

        this.actionsList.add(CloseTreeRootNode.HashKey);

    }

    @Override
    public void loadLDAPSuffix() {
        LDAPNode rootDseNode = ldapServer.getRootDSE();
        if (this.getChildCount() == 0) {
            this.addChild(rootDseNode);
        }
    }

    @Override
    public void setEnabledActions() {
        super.setEnabledActions();
        this.supportedActions.put(CloseTreeRootNode.HashKey, "yes");
    }

    @Override
    public String toString() {
        return this.myDN;
    }
}
