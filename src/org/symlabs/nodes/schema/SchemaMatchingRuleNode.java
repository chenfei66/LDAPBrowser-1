package org.symlabs.nodes.schema;

import org.symlabs.nodes.*;
import netscape.ldap.LDAPMatchingRuleSchema;
import netscape.ldap.LDAPSchemaElement;
import org.apache.log4j.Logger;
import org.symlabs.util.Schema;

/**
 * <p>Titulo: SchemaAttributeTypesNode </p>
 * <p>Descripcion: Class that manages the attribute type schema node.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SchemaMatchingRuleNode.java,v 1.1 2009-08-04 07:55:55 efernandez Exp $
 */
public class SchemaMatchingRuleNode extends SchemaNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SchemaAttributeTypeNode.class);
    /**Attribute that contains the string of the attributes*/
    private static final String ATTRIBUTES_KEY = "Attributes";
//    /**Attribute that contains the string of the value*/
//    private static final String VALUE_KEY = "Value";
    /**Attribute that contains the string of the use value*/
    private static final String USE_VALUE_KEY = "Use Value";
    /**Attribute that contains the string of the optional attributes*/
    private static final String SINGLE_VALUED_KEY = "Single Valued";

    /**Contructor that creates a new instance of an schema attribute node
     * 
     * @param matchingRuleName String. This is the objectclass Schema to set in this node
     * @param schema Schema. This is the ldap schema of this node
     */
    public SchemaMatchingRuleNode(String matchingRuleName, Schema schema) {
        super(matchingRuleName);
        LDAPMatchingRuleSchema ruleSchema = schema.getMatchingRule(matchingRuleName);
        this.setAttributes(ruleSchema);
    }

    /**MEthod that sets the valeus for the attributes
     * 
     * @param element LDAPSchemaElement;
     */
    @Override
    protected void setAttributes(LDAPSchemaElement element) {
        super.setAttributes(element);
        LDAPMatchingRuleSchema ruleSchema = (LDAPMatchingRuleSchema) element;

        //Attributes
        String[] attrs = ruleSchema.getAttributes();
        if (attrs != null && attrs.length > 0) {
            this.attributes[1].put(ATTRIBUTES_KEY, attrs);
        }
//
//        //Value
//        String value = ruleSchema.getValue();
//        if (value != null && !value.trim().equals("")) {
//            this.attributes[1].put(VALUE_KEY, new String[]{value});
//        }

        //Use value
        String useValue = ruleSchema.getUseValue();
        if (useValue != null && !useValue.trim().equals("")) {
            this.attributes[1].put(USE_VALUE_KEY, new String[]{useValue});
        }

    }
}
