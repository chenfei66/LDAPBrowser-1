
package org.symlabs.browser.attribute;

/**
 * <p>Titulo: AttributeSelectorChangeI </p>
 * <p>Descripcion: Interfaces that shows implements the methods needed to add and remove attributes since a panel that contains it</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: AttributeSelectorChangeI.java,v 1.1 2009-06-17 17:10:34 efernandez Exp $
 */
public interface AttributeSelectorChangeI {

    public void postAddElement(Object[] attributesAdded);
    
    public void postRemoveElement(Object [] attributesRemoved);
    
}
