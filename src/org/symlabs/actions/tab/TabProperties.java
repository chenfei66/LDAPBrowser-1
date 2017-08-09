package org.symlabs.actions.tab;

import org.symlabs.actions.*;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symlabs.browser.connection.ConnectionEditWin;
import org.symlabs.util.LDAPServer;
import org.symlabs.util.Utils;

/**
 * <p>Titulo: ConnectionProperties </p>
 * <p>Descripcion: Action that shows a window with the connection properties. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TabProperties.java,v 1.4 2009-08-04 11:06:35 efernandez Exp $
 */
public class TabProperties extends DsAction {

    /**Attribute that contains the key that identifies this action*/
    public final static String HashKey = "connectionProperties";
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(TabProperties.class);

    public TabProperties() {
        super("Connection Properties", java.awt.event.KeyEvent.VK_P,Utils.createImageIcon(Utils.ICON_CONNECTION_PROPERTIES));
    }

    public void actionPerformed(ActionEvent arg0) {
        LDAPServer ldapServer = Utils.getMainWindow().getCurrentBrowserPanel().getTreeRootNode().getLdapServer();
        ConnectionEditWin panel = new ConnectionEditWin(ldapServer);
//                ldapServer.getConfigurationName(),
//                ldapServer.getHost(),
//                ldapServer.getPort() + "",
//                ldapServer.getIndexLdapVersion(),
//                ldapServer.getUserId(),
//                ldapServer.getUserPw(),
//                ldapServer.getSuffix(),
//                ldapServer.getSuffixes(),
//                ldapServer.getServerType().get(LDAPServer.VENDOR_NAME_TYPE),
//                ldapServer.getServerType().get(LDAPServer.VENDOR_VERSION_TYPE));

        panel.setSize(600, 500);
        panel.setLocationRelativeTo(Utils.getMainWindow());
        panel.setVisible(true);
    }
}
