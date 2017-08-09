package org.symlabs.nodes.schema;

import java.util.HashMap;
import netscape.ldap.LDAPSchemaElement;
import org.symlabs.nodes.*;
import org.apache.log4j.Logger;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: SchemaNode </p>
 * <p>Descripcion: Class that manages the schema nodes.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SchemaNode.java,v 1.1 2009-08-04 07:55:55 efernandez Exp $
 */
public class SchemaNode extends LDAPNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SchemaNode.class);
    /**Attribute used to identofy the node type*/
    public static String TYPE_SCHEMA_NODE = "SchemaNode";
    /**Attribute that cotnains the string description*/
    protected static String DESCRIPTION_KEY = "Description";
    /**Attribute that contains the string name*/
    protected static String NAME_KEY = "Name";
    /**Attribute that contains the OID string*/
    protected static String OID_KEY = "OID";
    /**Attribute that cotnains the alises string*/
    protected static String ALIASES_KEY ="Alias";
    /**Attribute that contains the id string*/
    protected static String ID_KEY ="ID";
    /**Attribute that contains the value string*/
    protected static String VALUE_KEY ="Value";

    public SchemaNode() {

    }

    /**Constructor that initializes the values of this class
     * 
     * @param attributeName String
     */
    public SchemaNode(String attributeName) {
        this.type = SchemaNode.TYPE_SCHEMA_NODE;
        this.myName = LDAPNode.DEFAULT_LDAP_NODE_NAME;
        this.setMyIconPath(Utils.ICON_SCHEMA_ENTRY);
        this.myDN = attributeName;
        this.myRDN = this.myDN;
    }

    @Override
    public String toString() {
        return this.myDN;
    }

    protected void setAttributes(LDAPSchemaElement element) {
        if (this.attributes == null) {
            this.attributes = new HashMap[3];
            for (int i = 0; i < this.attributes.length; i++) {
                this.attributes[i] = new HashMap<String, String[]>();
            }
        }

        //Description
        String description = element.getDescription();
        if (description != null && !description.trim().equals("")) {
            this.attributes[0].put(DESCRIPTION_KEY, new String[]{description});
        }

        //Name
        String name = element.getName();
        if (name != null && !name.trim().equals("")) {
            this.attributes[0].put(NAME_KEY, new String[]{name});
        }
        
        //ID
        String id = element.getID();
        if(id!=null && !id.equals("")){
            this.attributes[0].put(ID_KEY, new String[]{id});
        }
        
        //Aliases
        String[] alises = element.getAliases();
        if(alises!=null && alises.length>0){
            this.attributes[2].put(ALIASES_KEY, alises);
        }
        
        //Value 
        String value = element.getValue();
        if(value!=null && !value.equals("")){
            this.attributes[2].put(VALUE_KEY, new String[]{value});
        }
        
//        //OID
//        String oid = element.getOID();
//        if(oid!=null && !oid.equals("")){
//            this.attributes[2].put(OID_KEY, new String[]{oid});
//        }
        
    }
}
