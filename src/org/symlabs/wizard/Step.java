/*
 * Step.java
 *
 * Created on October 20, 2008, 7:52 PM
 */

package org.symlabs.wizard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * class that abstracts the steps needed inside a wizard
 * @author  antonio
 */
public class Step extends javax.swing.JPanel {
   
    /**
     *  Title of this step, it is visible in the left hand side of the display window
     */
    private String title;
    /**
     *  Steps that are logically after this step. null implies this is the last step.
     */
    private Step next=null;
    /**
     *  Steps that are logically before this step. null implies this is the first step.
     */ 
    private Step previous=null;
    /**
     *  Parameters that are used in the current wizard, they are pased from step to step
     * Each step has to prepopulate its configuration options from this hash map, and
     * update them for the next step.
     * The last step implements finish() and performs all the required actions for the
     * wizard using the parameters that have been obtained from all the wizards.
     */
    protected HashMap<String,Object> parameters;
    
    /**
     * Default Panel Color for the steps
     */
    public static Color DEFAULT_COLOR=new Color(238,233,229);
    /** Creates new form Step
     * @param title Title of the wizard step
     */
    public Step(String title) {
        this.title =title;
        this.parameters = new HashMap<String,Object>();
        initComponents();
    }
    
    public Step(){
        this("No title for Step");
    }
    
    
    /**
     *  Sets the argument as the next step of this. Also tries to set the previous 
     * step of next to be this
     * @param next Step that goes after the current step
     */
    public void newNext(Step next) {
        if (this.next == null) {
            this.next = next;
            next.newPrev(this);
        }
    }
    /**
     * Sets the argument as the next step of this. Also tries to set the next 
     * step of prev to be this
     * @param prev Wizard that goes before the current Wizard
     */
    public void newPrev(Step prev) {
        if (this.previous == null) {
            this.previous = prev;
            prev.newNext(this);
        }
    }
    
    /**
     * Function that actually commints all user choices during the wizard
     * It must be implemented by the specific step
     * @return true if the wizard finished successfully (and should call the onfinish() event) or false
     * to stay in the Last step
     */
    public boolean finish(){
        throw new UnsupportedOperationException();
    }
    
    /**
     *  Creates a ordered List of this title and the previous titles of all previous steps
     * @param currentTitles Current List of titles 
     * @return currentTitles with all previous steps prepended to the List
     */
    public ArrayList<String> getPreviousTitles(ArrayList<String> currentTitles) {
        currentTitles.add(0,this.title); //add at the begining
        if (this.previous != null){
            this.previous.getPreviousTitles(currentTitles);
        }
        return currentTitles;
    }
    /**
     *  Creates a ordered List of this title and the next titles of all next steps
     * @param currentTitles Current List of titles 
     * @return currentTitles with all next steps appended to the List
     */
    public ArrayList<String> getNextTitles(ArrayList<String> currentTitles) {
        currentTitles.add(this.title); //add at the end
        if (this.next != null){
            this.next.getNextTitles(currentTitles);
        }
        return currentTitles;
    }        
    
    /**
     * 
     * @return true is this step is the last of the wizard
     */
    public Boolean isLast(){
        return (this.next==null);
    }        
    /**
     * 
     * @return true is this step is the first one of the wizard
     */
    public Boolean isFirst(){
        return (this.previous==null);
    }

    /**
     * 
     * @return current next step or null if not set (usually meaning the last step)
     */
    public Step getNext() {
        return next;
    }
    /**
     * 
     * @return current previous step or null if not set (usually menaing the first step)
     */
    public Step getPrevious() {
        return previous;
    }
    /**
     * 
     * @return current title of the step (null if not set)
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets (overwrites) the next  Step
     * @param next 
     */
    public void setNext(Step next) {
        this.next = next;
    }
    /**
     * Sets (overwrites) the previous Step
     * @param previous
     */
    public void setPrevious(Step previous) {
        this.previous = previous;
    }
    /**
     * Sets (overwrites) the title of this Step
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return hte HashMap of parameters that this current step has
     */
    public HashMap<String, Object> getParameters() {
        this.fetch_params();
        return this.parameters;
    }

    /**
     *  Updates the parameters in this step with the argument provided
     * @param parameters to be stored as the new parameters
     */
    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters=parameters;
        this.put_params();
    }

    protected void fetch_params() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *  Iterates the previous steps until it reaches the first one.
     * 
     * @return the first step in teh current list
     */
    public Step getFirst() {
        if (this.isFirst())
            return this;
        else
            return this.previous.getFirst();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());
        add(jPanel1, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    protected void put_params() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
