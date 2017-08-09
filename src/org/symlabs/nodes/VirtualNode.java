package org.symlabs.nodes;

import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: VirtualNode </p>
 * <p>Descripcion: Class which manages a virtual node of the tree.  </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: VirtualNode.java,v 1.4 2009-09-29 09:45:21 efernandez Exp $
 */
public class VirtualNode extends LDAPNode{
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(VirtualNode.class);
    
    /**Constructor used to create a new instance of VirtualNode
     * 
     * @param suffix String. This is the suffix used for this node
     */
    public VirtualNode(String suffix){
        this(suffix,suffix);
    }
    
    public VirtualNode(String dn, String rdn){
        this.type = LDAPNode.TYPE_VIRTUAL_NODE;
        this.myDN = dn;
        this.myRDN = rdn;
        this.myName = Utils.getValueFromRdn(this.myRDN);
        this.setMyIconPath(Utils.getImageIconPathForNode(this));
        this.setAttributes(null);
    }

}
