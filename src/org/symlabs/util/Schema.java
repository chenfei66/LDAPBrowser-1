package org.symlabs.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import netscape.ldap.LDAPAttributeSchema;
import netscape.ldap.LDAPMatchingRuleSchema;
import netscape.ldap.LDAPObjectClassSchema;
import netscape.ldap.LDAPSchema;
import netscape.ldap.LDAPSyntaxSchema;
import org.apache.log4j.Logger;

/**
 * <p>Titulo: Schema </p>
 * <p>Descripcion: Class that manages the schema of the ldap connection 
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: Schema.java,v 1.15 2009-08-05 13:48:10 efernandez Exp $
 */
public class Schema {

    /**Attribute that contains the schema of the ldap server given as argument in the constructor*/
    private LDAPSchema ldapSchema;
    /**Attribute that contains the ldapServer that manages this connection*/
    private LDAPServer ldapServer;
    /**Attribute that contains the objectclasses contained in the schema*/
    private HashMap<String, LDAPObjectClassSchema> hashObjectclasses;
    /**Attribute that contains the attributes contained in the schema*/
    private HashMap<String, LDAPAttributeSchema> hashAttributes;
    /**Attribute that contains the syntaxes found in the schema*/
    private HashMap<String, LDAPSyntaxSchema> hashSyntaxes;
    /**Attribute that contains the matching rules found in the schema*/
    private HashMap<String, LDAPMatchingRuleSchema> hashMatchingRules;
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(Schema.class);
    /**Attribute that contains the String of the attribute Objectclass*/
    public static final String OBJECTCLASS_KEY = "objectClass";
    /**Attribute that contains the String if the objectclass extensibleObject*/
    public static final String OBJECTCLASS_EXTENSIBLE_OBJECT_KEY = "extensibleObject";
    /**Attribute that contains the string of th subschemaSubentry used to where can we get the schema*/
    public static final String SUBSCHEMA_SUBENTRY_KEY = "subschemaSubentry";
    /**Attribute that contains the vendorname type*/
    public static final String VENDOR_NAME_TYPE = "vendorName";
    /**Attribute that contains the vendorversion type*/
    public static final String VENDOR_VERSION_TYPE = "vendorVersion";
    /**Attribute that contains the naming contexts*/
    public static final String NAMING_CONTEXTS_KEY ="namingcontexts";
    /**Attribute that contains the text of the attribute used to store the objectclasses in the schema*/
    public static final String SCHEMA_OBJECTCLASSES_KEY = "objectclasses";
    /**Attribute that contains the text of the attribute used to store the attributetypes in the schema*/
    public static final String SCHEMA_ATTRIBUTE_TYPES_KEY = "attributetypes";
    /**Attribute that contains the text of the attribute used to store the matchingrules in the schema*/
    public static final String SCHEMA_MATCHING_RULES_KEY = "matchingrules";
    /**Attribute that contains the text of the attribute used to store the ldapsyntaxes in the schema*/
    public static final String SCHEMA_LDAP_SYNTAXES_KEY = "ldapsyntaxes";

// <editor-fold defaultstate="collapsed" desc=" Getter and Setter methods ">
    /**Method that returns the attribute types contained in the schema
     * 
     * @return HashMap<String, LDAPAttributeSchema> 
     */
    public HashMap<String, LDAPAttributeSchema> getHashAttributes() {
        return hashAttributes;
    }

    /**Method that returns the matching rules found in the schema
     * 
     * @return HashMap<String, LDAPMatchingRuleSchema>
     */
    public HashMap<String, LDAPMatchingRuleSchema> getHashMatchingRules() {
        return hashMatchingRules;
    }

    /**Method that returns the objectclasses found in the schema
     * 
     * @return HashMap<String, LDAPObjectClassSchema>
     */
    public HashMap<String, LDAPObjectClassSchema> getHashObjectclasses() {
        return hashObjectclasses;
    }

    /**Method that returns the ldap syntaxes found in the schema
     * 
     * @return HashMap<String, LDAPSyntaxSchema> 
     */
    public HashMap<String, LDAPSyntaxSchema> getHashSyntaxes() {
        return hashSyntaxes;
    }
// </editor-fold>

    
    /**Constructor: Initializes the attributes of this class
     * 
     * @param ldapServer LDAPServer. This is the ldapServer that manages this connection
     */
    public Schema(LDAPServer ldapServer) {
        this.ldapServer = ldapServer;
        this.ldapSchema = new LDAPSchema();
        try {
            this.ldapSchema.fetchSchema(this.ldapServer.getConnection(true));
            this.hashObjectclasses = this.getObjectClasses();
            this.hashAttributes = this.getAttributes();
            this.hashMatchingRules = this.getMatchingRules();
            this.hashSyntaxes = this.getSyntaxes();
        } catch (Exception e) {
            logger.error("Error creating schema: " + e);
        }
    }

    /**Method that returns the objectclasses contained in this schema
     * 
     * @return HashMap String,LDAPObjectClassSchema. This is the objectclasses found in the schema
     */
    private HashMap<String, LDAPObjectClassSchema> getObjectClasses() {
        HashMap<String, LDAPObjectClassSchema> hashObjectClass = new HashMap<String, LDAPObjectClassSchema>();
        for (Enumeration<LDAPObjectClassSchema> e = this.ldapSchema.getObjectClasses(); e.hasMoreElements();) {
            LDAPObjectClassSchema objectClass = e.nextElement();
//            System.out.println("type:" + objectClass.getType() + ", superior:" + objectClass.getSuperior() + ", toString:" + objectClass.toString() + ", value: " + objectClass.getValue());
//            String superior[] = objectClass.getSuperiors();
//            if (superior != null) {
//                for (int i = 0; i < superior.length; i++) {
//                    System.out.println("superior[" + i + "]:" + superior[i]);
//                }
//            }
            hashObjectClass.put(objectClass.getName(), objectClass);
        }
        return hashObjectClass;
    }

    /**Method that returns the systaxes found in the schema.
     * 
     * @return HashMap String, LDAPSyntaxSchema. This is the syntax found  in the schema.
     */
    private HashMap<String, LDAPSyntaxSchema> getSyntaxes() {
        HashMap<String, LDAPSyntaxSchema> hashSyntax = new HashMap<String, LDAPSyntaxSchema>();
        for (Enumeration<LDAPSyntaxSchema> e = this.ldapSchema.getSyntaxes(); e.hasMoreElements();) {
            LDAPSyntaxSchema syntax = (LDAPSyntaxSchema) e.nextElement();
//            System.out.println("value:" + syntax.getValue() + ", getSyntaxString:" + syntax.getSyntaxString() + ", toString:" + syntax.toString() +
//                    ",id:" + syntax.getID() + ",description: " + syntax.getDescription() + ",name:" + syntax.getName());
            logger.trace("It is created the syntax of:"+syntax.getDescription()+"fin");
            hashSyntax.put(syntax.getDescription(), syntax);
        }
        return hashSyntax;
    }

    /**Method that returns the attributes found in this schema.
     * 
     * @return HashMap String, LDAPAttributeSchema. These are the attributes found in the schema.
     */
    private HashMap<String, LDAPAttributeSchema> getAttributes() {
        HashMap<String, LDAPAttributeSchema> hashAttribute = new HashMap<String, LDAPAttributeSchema>();
        for (Enumeration<LDAPAttributeSchema> e = this.ldapSchema.getAttributes(); e.hasMoreElements();) {
            LDAPAttributeSchema attribute = e.nextElement();
            hashAttribute.put(attribute.getName(), attribute);
        }
        return hashAttribute;
    }

    /**Method that returns the matching rules found in the schema.
     *  
     * @return HashMap String, LDAPMatchingRuleSchema. These are the matching rules found in the schema.
     */
    private  HashMap<String, LDAPMatchingRuleSchema> getMatchingRules() {
        HashMap<String, LDAPMatchingRuleSchema> hashRules = new HashMap<String, LDAPMatchingRuleSchema>();
        for (Enumeration e = this.ldapSchema.getMatchingRules(); e.hasMoreElements();) {
            LDAPMatchingRuleSchema rule = (LDAPMatchingRuleSchema) e.nextElement();
//            System.out.println("value:" + rule.getValue() + ",useValue:" + rule.getUseValue());
            hashRules.put(rule.getName(), rule);
        }
        return hashRules;
    }

    /**Method that returns the ldap attribute schema given by the argument attributeName
     * 
     * @param attributeName String. This is the attribute name to search in the schema
     * @return LDAPAttributeSchema.
     */
    public LDAPAttributeSchema getAttributeSchema(String attributeName) {
        if (this.hashAttributes.containsKey(attributeName)) {
            return this.hashAttributes.get(attributeName);
        } else {
            logger.debug("Attribute name: " + attributeName + " was not found in schema");
            return null;
        }
    }

    /**Method that returns the ldap objectclass schema given by the argument objectclassName
     * 
     * @param objectclassName String. This is the objectclass to search in the schema
     * @return LDAPObjectClassSchema.
     */
    public LDAPObjectClassSchema getObjectClassSchema(String objectclassName) {
        if (this.hashObjectclasses.containsKey(objectclassName)) {
            return this.hashObjectclasses.get(objectclassName);
        } else {
            logger.debug("Objectclass name: " + objectclassName + " was not found in schema");
            return null;
        }
    }
    
    /**Method that returns the ldap syntax schema given by the argument ldapSyntaxName
     * 
     * @param ldapSyntaxName String.
     * @return LDAPSyntaxSchema
     */
    public LDAPSyntaxSchema getLdapSyntaxSchema(String ldapSyntaxName){
        if(this.hashSyntaxes.containsKey(ldapSyntaxName)){
            return this.hashSyntaxes.get(ldapSyntaxName);
        }else{
            logger.debug("Ldap syntax name: "+ldapSyntaxName+" was not found in schema");
            return null;
        }
    }
    
    /**Method that retutns the matching tule schema given by the argument matchingRuleNAme
     * 
     * @param matchingRuleName String
     * @return LDAPMatchingRule
     */
    public LDAPMatchingRuleSchema getMatchingRule(String matchingRuleName){
        if(this.hashMatchingRules.containsKey(matchingRuleName)){
            return this.hashMatchingRules.get(matchingRuleName);
        }else{
            logger.debug("Matching rule name: "+matchingRuleName +" was not found in the schema");
            return null;
        }
    }

    /**Method that returns the required attributes for the attributeName given as argument
     * 
     * @param objectclassName String, This is the attribute name to search in the schema
     * @param vRequiredAttributes Vector. This vector stores the required attributes
     * @return Vector. This are the required attributes found
     */
    public Vector getRequiredAttributes(String objectclassName, Vector vRequiredAttributes) {
        if (!objectclassName.equals(Schema.OBJECTCLASS_EXTENSIBLE_OBJECT_KEY)) {
            LDAPObjectClassSchema objSchema = this.getObjectClassSchema(objectclassName);
            if (objSchema != null) {
                if (vRequiredAttributes == null) {
                    vRequiredAttributes = new Vector();
                }
                if (objSchema.getSuperior() != null) {
                    vRequiredAttributes = this.getRequiredAttributes(objSchema.getSuperior(), vRequiredAttributes);
                }
                if (vRequiredAttributes != null) {
                    for (Enumeration<String> e = objSchema.getRequiredAttributes(); e.hasMoreElements();) {
                        String element = e.nextElement();
                        if (!vRequiredAttributes.contains(element)) {
                            vRequiredAttributes.addElement(element);
                        }
                    }
                    return vRequiredAttributes;
                } else {
                    logger.debug("Required attributes were not found 1");
                    return vRequiredAttributes;
                }
            } else {
                logger.debug("Required attributes were not found 2");
                return vRequiredAttributes;
            }
        } else {//the objectclass is an extensible object
            return new Vector();
        }
    }

    /**Method that returns a vector with all the required attributes found for the objectclasses contained in the vector given as argument
     * 
     * @param objectClasses Vector<String>. It contains all the objectclasses that we want to get their required attributes
     * @return Vector<String>. It contains all the required attributes
     */
    public Vector<String> getRequiredAttributes(Vector<String> objectClasses) {
        Vector<String> allRequiredAttrs = new Vector<String>();
        for (int i = 0; i < objectClasses.size(); i++) {
            allRequiredAttrs = this.getRequiredAttributes(objectClasses.elementAt(i), allRequiredAttrs);
        }
        return allRequiredAttrs;
    }

    /**Method that returns a vector with all the optional attributes found for the objectclasses contained in the vector given as argument
     * 
     * @param objectClasses Vector<String>. It contains all the objectclasses that we want to get their optional attributes
     * @return Vector<String>. It contains all the optional attributes
     */
    public Vector<String> getOptionalAttributes(Vector<String> objectClasses) {
        Vector<String> allRequiredAttrs = new Vector<String>();
        for (int i = 0; i < objectClasses.size(); i++) {
            allRequiredAttrs = this.getOptionalAttributes(objectClasses.elementAt(i), allRequiredAttrs);
        }
        return allRequiredAttrs;
    }

    /**Method that returns the objectclasses found with the attribute name given as argument
     * 
     * @param attributeName String. This is the attributeName that we want to search their objectclasses
     * @return Vector
     */
    public Vector getObjectclassesFromAttribute(String attributeName) {
        Vector vObjectclasses = new Vector();
        Iterator it = this.hashObjectclasses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String objName = (String) entry.getKey();
            LDAPObjectClassSchema objSchema = (LDAPObjectClassSchema) entry.getValue();
            if (this.isfoundAttributeNameInEnumeration(attributeName, objSchema.getRequiredAttributes()) ||
                    this.isfoundAttributeNameInEnumeration(attributeName, objSchema.getOptionalAttributes())) {
                vObjectclasses.addElement(objName);
            }
        }
        return vObjectclasses;
    }

    /**Method that returns if the attribute name given as argument exists in the enumeration given as argument
     * 
     * @param attributeName String. This is the attribute name to search
     * @param enumeration Enumeration <String>. This is the enumeration where we want to search
     * @return boolean. True - It means the attribute was found. False - It means the attribute was not found.
     */
    private boolean isfoundAttributeNameInEnumeration(String attributeName, Enumeration<String> enumeration) {
        boolean found = false;
        for (Enumeration<String> e = enumeration; e.hasMoreElements();) {
            String element = e.nextElement();
            if (element.equals(attributeName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**Method that returns the optional attributes for the attributeName given as argument
     * 
     * @param attributeName String, This is the attribute name to search in the schema
     * @param vOptionalAttributes Vector. These are the vector that stores the required attributes
     * @return Vector. This vector contains the optional attributes found
     */
    public Vector getOptionalAttributes(String attributeName, Vector vOptionalAttributes) {
        if (!attributeName.equals(Schema.OBJECTCLASS_EXTENSIBLE_OBJECT_KEY)) {
            LDAPObjectClassSchema objSchema = this.getObjectClassSchema(attributeName);
            if (objSchema != null) {
                if (vOptionalAttributes == null) {
                    vOptionalAttributes = new Vector();
                }
                if (objSchema.getSuperior() != null) {
                    vOptionalAttributes = this.getOptionalAttributes(objSchema.getSuperior(), vOptionalAttributes);
                }
                if (vOptionalAttributes != null) {
                    for (Enumeration<String> e = objSchema.getOptionalAttributes(); e.hasMoreElements();) {
                        String element = e.nextElement();
                        if (!vOptionalAttributes.contains(element)) {
                            vOptionalAttributes.addElement(element);
                        }
                    }
                    return vOptionalAttributes;
                } else {
                    logger.debug("Optional attributes were not found 1");
                    return vOptionalAttributes;
                }
            } else {
                logger.debug("Optional attributes were not found 2");
                return vOptionalAttributes;
            }
        } else {//the attribute is an extensibleobject
            return this.getAllAttributes();
        }
    }

    /**Method that returns all the attributes found in the ldap schema
     * 
     * @return Vector<String>. This vector contains all attributes found in the ldap schema
     */
    public Vector<String> getAllAttributes() {
        Vector <String>vAllAttributes = new Vector<String>();
        Iterator it = this.hashAttributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String attrName = (String) entry.getKey();
            vAllAttributes.addElement(attrName);
        }
        return vAllAttributes;
    }

    /**Method that returns all the objectclasses names found in the ldap schema
     * 
     * @return Vector<String>. This vector contains all attributes found in the ldap schema
     */
    public Vector<String> getAllObjectclasses() {
        Vector<String> vAllObjectclasses = new Vector<String>();
        Iterator it = this.hashObjectclasses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String attrName = (String) entry.getKey();
            vAllObjectclasses.addElement(attrName);
        }
        return vAllObjectclasses;
    }
    
    /**MEthod that returns all the ldapsyntaxes found in the schema
     * 
     * @return Vector <String>
     */
    public Vector<String> getAllLdapSyntaxes(){
        Vector<String> vAllLdapSyntaxes = new Vector<String>();
        Iterator it =this.hashSyntaxes.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            String attrName = (String) entry.getKey();
            vAllLdapSyntaxes.addElement(attrName); 
        }
        return vAllLdapSyntaxes;
    }
    
    /**Method that returns all the matching rules in the schema
     * 
     * @return Vector <String>
     */
    public Vector<String> getAllMatchingRules(){
        Vector <String> vAllMatchingRules = new Vector<String>();
        Iterator it = this.hashMatchingRules.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            String attrName = (String) entry.getKey();
            vAllMatchingRules.addElement(attrName);
        }
        return vAllMatchingRules;
    }

    /**Method that returns if the attribute given as argument is single valued or multivalued
     * 
     * @param attributeName String. This is the attribute name to search
     * @return boolean. True means it is singled valued. False means it is multivalued
     */
    public boolean isSingleValued(String attributeName) {
        return this.getAttributeSchema(attributeName).isSingleValued();
    }

    /**Method that removes the objectclass attribute if it is found in the vector given as argument.
     * 
     * @param vAttributes Vector <String. 
     * @return Vector <String>
     */
    public static Vector<String> removeAttributeObjectClass(Vector<String> vAttributes) {
        if (vAttributes.contains(Schema.OBJECTCLASS_KEY)) {
            vAttributes.remove(Schema.OBJECTCLASS_KEY);
        }

        return vAttributes;
    }
}
