package org.symlabs.nodes.schema;

import org.symlabs.nodes.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import org.apache.log4j.Logger;
import org.symlabs.actions.tab.CloseTreeRootNode;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Schema;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: TreeSchemaRootNode </p>
 * <p>Descripcion: Class that manages the schema of the ldap browser.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TreeSchemaRootNode.java,v 1.2 2009-08-05 13:48:10 efernandez Exp $
 */
public class TreeSchemaRootNode extends TreeRootNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(TreeSchemaRootNode.class);
    /**Attribute that contains the default rdn of the schema*/
    private final static String DEFAULT_RDN = "Schema";
    /**Attribute that contains the defualt dn of the schema*/
    private final static String DEFAULT_DN = "Schema";

    /**Contructor: Creates a new instance of a tree schema root node. Used to show the ldap server schema
     * 
     * @param ldapServer LDAPServer
     * @throws java.lang.Exception
     */
    public TreeSchemaRootNode(LDAPServer ldapServer) throws Exception {
        super(ldapServer.getConnectionData());

        //We set the rdn name for this root node
        this.myRDN = DEFAULT_RDN;
        this.myDN = DEFAULT_DN + " of " + ldapServer.getConnectionData().getConfigurationName();

        //We set the icon for this root node
        this.setMyIconPath(Utils.ICON_SCHEMA);

        this.actionsList.clear();

        this.actionsList.add(CloseTreeRootNode.HashKey);
        
    }

    @Override
    public void loadLDAPSuffix() {
//        this.removeAllChildren();
        LDAPNode node = this.ldapServer.getRootDSE();
        String[] schemaValues = null;
        for (int i = 0; i < node.getAttributes().length; i++) {
            logger.trace("attributes[" + i + "]:" + this.getAttributes()[i] + ",length:" + node.getAttributes().length + ",attributes[" + i + "].size()=" + node.getAttributes()[i].size());
            if (node.getAttributes()[i] != null) { //this is because of the rootdse does not have objectclasses
                Iterator it = node.getAttributes()[i].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String attrName = (String) entry.getKey();
                    String[] attrValues = (String[]) entry.getValue();
                    logger.trace("attrName:" + attrName);
                    if (attrName.equalsIgnoreCase(Schema.SUBSCHEMA_SUBENTRY_KEY)) {
                        logger.trace("SubSchemaSubentry!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        for (int j = 0; j < attrValues.length; j++) {
                            logger.trace(attrName + ": " + attrValues[i]);
                        }
                        schemaValues = attrValues;
                    }
                }
            }
        }

        if (schemaValues != null) {
            LDAPSearchResults results = null;
            for (int i = 0; i < schemaValues.length; i++) {
                try {
//                    results = this.ldapServer.getLdapOperation().getEntryBaseDN(schemaValues[i]);
                    results = this.ldapServer.getLdapOperation().getSchema(schemaValues[i]);

                    logger.trace("We are going to add the child Nodes of the tree root schema");
                    this.loadChildNodesFromLDAPServer(results);

                    if (this.getChildCount() > 0) {
                        LDAPNode parentNode = (LDAPNode) this.getChildAt(0);
                        this.loadChildNodesOfSchemaParentNode(parentNode);
                    }

                } catch (LDAPException ex) {
                    logger.error("Error loading child node. " + ex);
                } catch (Exception exc) {
                    logger.error("Error loading child node. " + exc);
                }
            }
        } else {//Problem searching the entry subschemasubentry in RootDSE

        }
    }

    @Override
    public void setEnabledActions() {
        super.setEnabledActions();
        this.supportedActions.put(CloseTreeRootNode.HashKey, "yes");
    }

    @Override
    public String toString() {
        return this.myRDN;
    }

    /** Method that creates the child nodes for the node given as argument
     * 
     * @param parentNode LDAPNode. This is the node where we want to add the child nodes: 
     * objectclasses, attributetypes, matching rules and syntaxes.
     */
    private void loadChildNodesOfSchemaParentNode(LDAPNode parentNode) {
        //We have to create 4 nodes: objectclasses, attributes, matching rules, syntaxes. And their child nodes
        logger.trace("Loading objectclasses");
        this.addObjectClassesNodes(parentNode);
        logger.trace("Loading attributetypes");
        this.addAttributeTypesNodes(parentNode);
        logger.trace("Loading ldap syntaxes");
        this.addLdapSyntaxesNodes(parentNode);
        logger.trace("Loading matching rules");
        this.addMatchingRulesNodes(parentNode);

    }

    /**MEthod that adds the objectclass nodes to the tree schema root
     * 
     * @param parentNode LDAPNode. This is the parentNode
     */
    private void addObjectClassesNodes(LDAPNode parentNode) {
        if (!this.findNodeInParent(Schema.SCHEMA_OBJECTCLASSES_KEY, parentNode)) {
            Schema schema = this.getLdapServer().getLdapSchema();
            LDAPNode childNode = new VirtualNode(Schema.SCHEMA_OBJECTCLASSES_KEY);
            parentNode.add(childNode);
            Vector<String> vObjs = schema.getAllObjectclasses();
            Collections.sort(vObjs);
            for (int i = 0; i < vObjs.size(); i++) {
                SchemaObjectClassNode objNode = new SchemaObjectClassNode(vObjs.elementAt(i), schema);
                childNode.add(objNode);
            }
        }
    }

    /**MEthod that adds the attribute type nodes to the tree schema root
     * 
     * @param parentNode LDAPNode. This is the parentNode
     */
    private void addAttributeTypesNodes(LDAPNode parentNode) {
        if (!this.findNodeInParent(Schema.SCHEMA_ATTRIBUTE_TYPES_KEY, parentNode)) {
            Schema schema = this.getLdapServer().getLdapSchema();
            LDAPNode childNode = new VirtualNode(Schema.SCHEMA_ATTRIBUTE_TYPES_KEY);
            parentNode.add(childNode);
            Vector<String> vAttrs = schema.getAllAttributes();
            Collections.sort(vAttrs);
            for (int i = 0; i < vAttrs.size(); i++) {
                SchemaAttributeTypeNode attrNode = new SchemaAttributeTypeNode(vAttrs.elementAt(i), schema);
                childNode.add(attrNode);
            }
        }
    }

    /**MEthod that adds the ldap syntax nodes to the tree schema root
     * 
     * @param parentNode LDAPNode. This is the parentNode
     */
    private void addLdapSyntaxesNodes(LDAPNode parentNode) {
        if (!this.findNodeInParent(Schema.SCHEMA_LDAP_SYNTAXES_KEY, parentNode)) {
            Schema schema = this.getLdapServer().getLdapSchema();
            LDAPNode childNode = new VirtualNode(Schema.SCHEMA_LDAP_SYNTAXES_KEY);
            parentNode.add(childNode);
            Vector<String> vAttrs = schema.getAllLdapSyntaxes();
            logger.trace("LDAPSyntaxes.size:" + vAttrs.size());
            Collections.sort(vAttrs);
            for (int i = 0; i < vAttrs.size(); i++) {
                SchemaLdapSyntaxNode syntaxNode = new SchemaLdapSyntaxNode(vAttrs.elementAt(i), schema);
                childNode.add(syntaxNode);
            }
        }
    }

    /**MEthod that adds the matching rule nodes to the tree schema root
     * 
     * @param parentNode LDAPNode. This is the parentNode
     */
    private void addMatchingRulesNodes(LDAPNode parentNode) {
        if (!this.findNodeInParent(Schema.SCHEMA_MATCHING_RULES_KEY, parentNode)) {
            Schema schema = this.getLdapServer().getLdapSchema();
            LDAPNode childNode = new VirtualNode(Schema.SCHEMA_MATCHING_RULES_KEY);
            parentNode.add(childNode);
            Vector<String> vAttrs = schema.getAllMatchingRules();
            Collections.sort(vAttrs);
            for (int i = 0; i < vAttrs.size(); i++) {
                SchemaMatchingRuleNode ruleNode = new SchemaMatchingRuleNode(vAttrs.elementAt(i), schema);
                childNode.add(ruleNode);
            }
        }
    }

    /**Method that search the dn given as argument as the fiuled myDN in a child of the parentNode given as argument
     * 
     * @param dnToSearch String. This is the dn to search between the child nodes of the parentNode
     * @param parentNode LDAPNode. This is the parentNode
     * @return boolean. True- It means the node was found. False- it means the node was not found
     */
    private boolean findNodeInParent(String dnToSearch, LDAPNode parentNode) {
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            LDAPNode childNode = (LDAPNode) parentNode.getChildAt(i);
            logger.trace("childNode:"+childNode.myDN+",dnToSearch:"+dnToSearch);
            if (childNode.myDN.equals(dnToSearch)) {
                return true;
            }
        }
        return false;
    }
}
