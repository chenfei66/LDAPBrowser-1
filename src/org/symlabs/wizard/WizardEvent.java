/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.symlabs.wizard;

/**
 * Interface to encapsulate the events that might be interesting to comunicate out 
 * of a wizard
 * 
 * @author antonio
 */
public interface WizardEvent {

   /**
    * Callback to be used after the wizard finishes
    */
    public void onFinish();
    
   /**
    * Callback to be used when user selects cancel
    */
    public void onCancel();
       
}
