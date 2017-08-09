package org.symlabs.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * <p>Titulo: TextTranfer </p>
 * <p>Descripcion: Class used to get and set information in the system clipboard. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: TextTransfer.java,v 1.2 2009-08-05 11:50:54 efernandez Exp $
 */
public final class TextTransfer implements ClipboardOwner {
    
    /**Attribute used to display the debug message*/
    private static Logger logger = Logger.getLogger(TextTransfer.class);

    /**Creates a new instance of TextTranfer used to get and set a value in the system clipboard
     * 
     */
    public TextTransfer(){
    }
    
    /**Creates a new instance of TextTransfer used to set a value in the system clipboard
     * 
     * @param toClipboard String.
     */
    public TextTransfer(String toClipboard){
        this.setClipboardContents(toClipboard);
    }
    
    /**
     * Empty implementation of the ClipboardOwner interface.
     * @param aClipboard 
     * @param aContents
     */
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
    //do nothing
    }

    /**
     * Place a String on the clipboard, and make this class the
     * owner of the Clipboard's contents.
     * @param aString 
     */
    public void setClipboardContents(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     * empty String.
     */
    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                //highly unlikely since we are using a standard DataFlavor
                logger.trace(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                logger.error(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }
} 

