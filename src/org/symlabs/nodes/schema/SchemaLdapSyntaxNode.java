package org.symlabs.nodes.schema;

import org.symlabs.nodes.*;
import netscape.ldap.LDAPSchemaElement;
import netscape.ldap.LDAPSyntaxSchema;
import org.apache.log4j.Logger;
import org.symlabs.util.Schema;

/**
 * <p>Titulo: SchemaLdapSyntaxNode </p>
 * <p>Descripcion: Class that manages the ldap syntax schema node.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SchemaLdapSyntaxNode.java,v 1.1 2009-08-04 07:55:55 efernandez Exp $
 */
public class SchemaLdapSyntaxNode extends SchemaNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SchemaLdapSyntaxNode.class);
    /**Attribute that contains the string of the required attributes*/
    private static final String SYNTAX_STRING_KEY = "Syntax String";
//    /**Attribute that contains the string of the optional attributes*/
//    private static final String VALUE_KEY = "Value";

    /**Contructor that creates a new instance of an schema attribute node
     * 
     * @param ldapSyntaxesName String. This is the objectclass Schema to set in this node
     * @param schema Schema. This is the ldap schema of this node
     */
    public SchemaLdapSyntaxNode(String ldapSyntaxesName, Schema schema) {
        super(ldapSyntaxesName);
        logger.trace("Adding syntax schema:"+ldapSyntaxesName);
        LDAPSyntaxSchema syntaxSchema = schema.getLdapSyntaxSchema(ldapSyntaxesName);
        this.setAttributes(syntaxSchema);
    }

    /**MEthod that sets the valeus for the attributes
     * 
     * @param element LDAPSchemaElement
     */
    @Override
    protected void setAttributes(LDAPSchemaElement element) {
        super.setAttributes(element);
        LDAPSyntaxSchema syntaxSchema = (LDAPSyntaxSchema) element;

        //Syntax string
        String syntaxString = syntaxSchema.getSyntaxString();
        if(syntaxString!=null && !syntaxString.trim().equals("")){
            this.attributes[1].put(SYNTAX_STRING_KEY,new String[]{syntaxString});
        }
//        
//        //Value
//        String value = syntaxSchema.getValue();
//        if(value !=null && !value.trim().equals("")){
//            this.attributes[1].put(VALUE_KEY,new String [] {value});
//        }
        
    }
}

