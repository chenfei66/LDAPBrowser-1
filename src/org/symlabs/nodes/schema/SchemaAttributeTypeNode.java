package org.symlabs.nodes.schema;

import org.symlabs.nodes.*;
import netscape.ldap.LDAPAttributeSchema;
import netscape.ldap.LDAPSchemaElement;
import org.apache.log4j.Logger;
import org.symlabs.util.Schema;

/**
 * <p>Titulo: SchemaAttributeTypeNode </p>
 * <p>Descripcion: Class that manages the attribute type schema node.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SchemaAttributeTypeNode.java,v 1.1 2009-08-04 07:55:55 efernandez Exp $
 */
public class SchemaAttributeTypeNode extends SchemaNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SchemaAttributeTypeNode.class);
    /**Attribute that contains the string of the superior attribute*/
    private static final String SUPERIOR_KEY = "Superior";
    /**Attribute that contains the string of the required attributes*/
    private static final String SYNTAX_STRING_KEY = "Syntax String";
    /**Attribute that contains the string of the optional attributes*/
    private static final String SINGLE_VALUED_KEY = "Single Valued";

    /**Contructor that creates a new instance of an schema attribute node
     * 
     * @param attributeName String. This is the objectclass Schema to set in this node
     * @param schema Schema. This is the ldap schema of this node
     */
    public SchemaAttributeTypeNode(String attributeName, Schema schema) {
        super(attributeName);
        LDAPAttributeSchema attrSchema = schema.getAttributeSchema(attributeName);
        this.setAttributes(attrSchema);
    }

    /**MEthod that sets the valeus for the attributes
     * 
     * @param element LDAPSchemaElement.
     */
    @Override
    protected void setAttributes(LDAPSchemaElement element ) {
        super.setAttributes(element);
        LDAPAttributeSchema attrSchema = (LDAPAttributeSchema) element;

        //Superior
        String superior = attrSchema.getSuperior();
        if (superior !=null && !superior.trim().equals("")) {
            this.attributes[1].put(SUPERIOR_KEY, new String[]{superior});
        }

        //Syntax string
        String syntaxString = attrSchema.getSyntaxString();
        if (syntaxString!=null &&!syntaxString.trim().equals("")) {
            this.attributes[1].put(SYNTAX_STRING_KEY, new String[]{syntaxString});
        }

        //Is single valued
        if (attrSchema.isSingleValued()) {
            this.attributes[1].put(SINGLE_VALUED_KEY, new String[]{"Yes"});
        } else {
            this.attributes[1].put(SINGLE_VALUED_KEY, new String[]{"No"});
        }
    }
}
