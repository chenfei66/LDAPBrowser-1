package org.symlabs.util;

import org.apache.log4j.Logger;
import org.symlabs.store.ConnectionData;

/**
 * <p>Titulo: EvalParams </p>
 * <p>Descripcion: Class which evals the connection data. When you save a configuration your data are saved in a file, this class evals these connection data </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: EvalParams.java,v 1.8 2009-08-04 15:26:07 efernandez Exp $
 */
public class EvalParams {
    
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(EvalParams.class);
    
    /**Method which evaluates the connection parameters. If the parameters are correct it returns "" else it returns a String with the error found
     * 
     * @param host String. This is the host used to connect
     * @param port String. This is the port used to connect.
     * @param authid String. This is the username used to connect.
     * @param authpw char[]. This is the password used to connect
     * @param suffix String. This is the suffix given in the window connection.
     * @param indexLdapVersion int. This is the index of the ldap version
     * @return String. This string contains a message with the errors found in the parameters, if there are not any error it returns ""
     */
    public static String evalConnectionParameters(String host,String port,String authid,char[] authpw,String suffix,int indexLdapVersion){
        String errorMessage= "";
        errorMessage += evalConnectionParameters(host, port, authid, authpw);
        if(!errorMessage.equals("")){
            errorMessage += evalParameterSuffix(host,port,authid,authpw,suffix,indexLdapVersion);
        }
        if(!errorMessage.equals("")){
            logger.error(errorMessage);
        }
        return errorMessage;
    }

    
    /**Method which evalues the connection parameters. If the parameters are correct it returns "" else it returns a String with the error found
     * 
     * @param host String. This is the host used to connect
     * @param port String. This is the port used to connect.
     * @param authid String. This is the username used to connect.
     * @param authpw char[]. This is the password used to connect.
     * @return String. This string contains a message with the errors found in the parameters, if there are not any error it returns ""
     */
    public static String evalConnectionParameters(String host,String port,String authid,char[] authpw){
        String errorMessage= "";
        String userPw = new String(authpw);
        //Check host
        if(host.trim().equals("")){
            errorMessage+="Host not found.\n";
            logger.error(errorMessage);
        }
        //check port
        try{
            int myport = Integer.valueOf(port);
        }catch(Exception e){
            errorMessage += "Port not found.\n";
            logger.error(errorMessage);
        }
//        //check username
//        if(authid.trim().equals("")){
//            errorMessage+="User id not found.\n";
//            logger.error(errorMessage);
//        }
//        //check password
//        if(userPw.trim().equals("")){
//           errorMessage+="Password not found\n";
//           logger.error(errorMessage);
//        }
        
        return errorMessage;
    }
    
    /**Method that sets the correct value for the suffix given.
     * If the suffix is null or whitespace then "" is setted as suffix
     * 
     * @param suffix String. This is the suffix that we want to look for
     * @return String. This is the error message found. If any error is found then it is returned ""
     */
    private static String evalParameterSuffix(String host,String port,String authid, char[] authpw, String suffix, int ldapVersion){
        String errorMessage="";
        String [] suffixes = null;
        boolean found=false;

        try{
            ConnectionData data = new ConnectionData(host,Integer.parseInt(port),authid,new String(authpw),ldapVersion);
            LDAPServer con = new LDAPServer(data,true);
            suffixes = con.getSuffixes();
            
        }catch(Exception e){
            errorMessage = e.getMessage();
            logger.error(errorMessage);
        }
        
        if(!errorMessage.equals("") && suffixes!=null){
            for(int i=0;i<suffixes.length;i++){
                if(suffixes[i].equals(suffix)){
                    found=true;
                    break;
                }
            }
        }
        
        if(!found){
            errorMessage+="Suffix "+suffix+" not found.";
            logger.error(errorMessage);
        }

        return errorMessage;
    }
}
