package org.symlabs.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;

/**
 * <p>Titulo: DsAction </p>
 * <p>Descripcion: Class which used to manages the actions. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: DsAction.java,v 1.8 2009-07-13 16:43:02 efernandez Exp $
 */
public abstract class DsAction extends AbstractAction {

    private final static String DEFAULT_DESCRIPTION = "Generic Action";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(DsAction.class);

    /** Creates a new Action
     *
     */
    public DsAction() {
        this(DsAction.DEFAULT_DESCRIPTION);

    }

    /** Creates a new update action with the provided description
     *
     * @param description Non Standard description description
     */
    public DsAction(String description) {
        super();
        if (description == null) {
            description = DsAction.DEFAULT_DESCRIPTION;
        }
        this.setDescription(description);
    }

    /** Creates a new update action with the provided description
     *
     * @param description Non Standard description description
     * @param mnemonic  Mnemonic to use for the menu item
     * @deprecated please use DSAction(String,int)
     */
    @Deprecated
    public DsAction(String description, char mnemonic) {
        this(description);
        this.setMnemonic(new Integer(KeyStroke.getKeyStroke(mnemonic).getKeyCode()));
    }

    /** Creates a new update action with the provided description
     *
     * @param description Non Standard description description
     * @param mnemonic  Mnemonic (KeyEvent.VK_*)to use for the menu item
     */
    public DsAction(String description, int mnemonic) {
        this(description);
        this.setMnemonic(mnemonic);
    }
    
    /** Creates a new update action with the provided description and icon
     *
     * @param description Non Standard description description
     * @param icon Icon. This is the icon to set to this action
     */
    public DsAction(String description, Icon icon) {
        this(description);
        this.setIcon(icon);
    }

    /** Creates a new update action with the provided description and icon
     *
     * @param description Non Standard description description
     * @param mnemonic  Mnemonic (KeyEvent.VK_*)to use for the menu item
     * @param icon Icon. This is the icon to set to this action
     */
    public DsAction(String description, int mnemonic, Icon icon) {
        this(description);
        this.setMnemonic(mnemonic);
        this.setIcon(icon);
    }

    public String getDescription() {
        return (String) this.getValue(Action.SHORT_DESCRIPTION);
    }

    public void setIcon(Icon icon) {
        this.putValue(Action.SMALL_ICON, icon);
    }

    public void setDescription(String description) {
        this.putValue(Action.SHORT_DESCRIPTION, description);
        this.putValue(Action.NAME, description);
    }

    public int getMnemonic() {
        Integer mnemo = (Integer) this.getValue(Action.MNEMONIC_KEY);
        return mnemo.intValue();
    }

    public void setMnemonic(int mnemonic) {
        this.putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
    }

    /**
     * @return the action represented by this object
     */
    public AbstractAction getAction() {
        return (AbstractAction) this;
    }

    /** Creates a new menu item that includes thie internal action, its description,
     * and the mnemonic
     *
     * @return new menu item
     */
    public JMenuItem getMenuItem() {
        JMenuItem jmi = new JMenuItem();
        jmi.setAction(this);
        return jmi;
    }
}
