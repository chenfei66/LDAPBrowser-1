package org.symlabs.nodes.schema;

import org.symlabs.nodes.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import netscape.ldap.LDAPObjectClassSchema;
import netscape.ldap.LDAPSchemaElement;
import org.apache.log4j.Logger;
import org.symlabs.util.Schema;

/**
 * <p>Titulo: SchemaObjectClassesNode </p>
 * <p>Descripcion: Class that manages the objectclass schema node.</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SchemaObjectClassNode.java,v 1.1 2009-08-04 07:55:55 efernandez Exp $
 */
public class SchemaObjectClassNode extends SchemaNode {

    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(SchemaObjectClassNode.class);
    /**Attribute that contains the string of the superior attribute*/
    private static final String SUPERIOR_KEY = "Superior";
    /**Attribute that contains the string of the required attributes*/
    private static final String REQUIRED_KEY = "Required";
    /**Attribute that contains the string of the optional attributes*/
    private static final String OPTIONAL_KEY = "Optional";
    /**Attribute that contains the string of the Superiors attributes*/
    private static final String SUPERIORS_KEY = "Superiors";

    /**Contructor that creates a new instance of an schema objectclass node
     * 
     * @param objectClassName String. This is the objectclass Schema to set in this node
     * @param schema Schema. This is the ldap schema of this node
     */
    public SchemaObjectClassNode(String objectClassName, Schema schema) {
        super(objectClassName);
        logger.trace("Adding objectclass: " + objectClassName);
        LDAPObjectClassSchema objSchema = schema.getObjectClassSchema(objectClassName);
        this.setAttributes(objSchema);
    }

    /**MEthod that sets the valeus for the attributes
     * 
     * @param element LDAPSchemaElement
     */
    @Override
    protected void setAttributes(LDAPSchemaElement element) {
        super.setAttributes(element);
        LDAPObjectClassSchema objSchema = (LDAPObjectClassSchema) element;

        //Required attrs
        Vector<String> vAttributes = new Vector();
        Enumeration e = objSchema.getRequiredAttributes();
        while (e.hasMoreElements()) {
            String attrName = (String) e.nextElement();
            vAttributes.addElement(attrName);
        }
        if (vAttributes.size() > 0) {
            String[] reqAttrs = new String[vAttributes.size()];
            for (int i = 0; i < vAttributes.size(); i++) {
                reqAttrs[i] = vAttributes.elementAt(i);
            }
            this.attributes[1].put(REQUIRED_KEY, reqAttrs);
            logger.trace("Added: " + REQUIRED_KEY + ",value: " + vAttributes);
        }

        //Optional attrs 
        vAttributes.removeAllElements();
        e = objSchema.getOptionalAttributes();
        while (e.hasMoreElements()) {
            String attrName = (String) e.nextElement();
            vAttributes.addElement(attrName);
        }
        if (vAttributes.size() > 0) {
            String[] optAttrs = new String[vAttributes.size()];
            for (int i = 0; i < vAttributes.size(); i++) {
                optAttrs[i] = vAttributes.elementAt(i);
            }
            this.attributes[1].put(OPTIONAL_KEY, optAttrs);
            logger.trace("Added: " + OPTIONAL_KEY + ",value: " + vAttributes);
        }

        //Superiors
        String superiors[] = objSchema.getSuperiors();
        if (superiors != null && superiors.length > 0) {
            this.attributes[1].put(SUPERIORS_KEY, superiors);
        }
    }
}
