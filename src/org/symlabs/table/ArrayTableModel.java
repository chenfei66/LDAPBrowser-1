package org.symlabs.table;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * <p>Titulo: ArrayTableModel </p>
 * <p>Descripcion: Class which manage a table model</p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: ArrayTableModel.java,v 1.1 2009-05-13 15:16:09 efernandez Exp $
 */

public class ArrayTableModel extends AbstractTableModel{
    /**Attribute which contains the columns name of the table*/
    private String[] columnNames = null;
    /** Attribute which contains the class of the objects stored in the table **/
    private Class[] classTypes=null;
    /**Attribute which contains the data of the table*/
    private ArrayList<Object[]> data=null;
    /**Attribute which indicates the editable cells*/
    private ArrayList<Boolean[]> editable=null;
    /**Attribute which contains the default data of the row*/
    private Object[] defaultValuesRow = null;
    /**Attribute which contains the default editable value of the each cell in the table*/
    private Boolean[] defaultEditableRow= null;
    

    /**Method that returns the data of this table model
     * @return ArrayList. This is the data of the table
     */
    public ArrayList<Object[]> getData(){
        return this.data;
    }
    
    /**
     *  Constructor which initializes the attributes of the class
     */
    public ArrayTableModel()
    {
        this.defaultValuesRow = new Object[0];
        this.columnNames = new String[0];
        this.classTypes=new Class[0];
        data = new ArrayList<Object[]>();
        editable = new ArrayList<Boolean[]>();
        this.defaultEditableRow = new Boolean[0];
    }
    
    /** Constructor which initializes the attributes of the class
     * 
     * @param columnNames String[] which contains the columns name of the table
     */
    public ArrayTableModel(String [] columnNames)
    {
        this.columnNames = columnNames;
        this.defaultValuesRow = new Object[this.columnNames.length];
        this.classTypes = new Class[columnNames.length];
        data = new ArrayList<Object[]>();
        editable = new ArrayList<Boolean[]>();        
        this.defaultEditableRow = new Boolean[columnNames.length];
    }

    /** Constructor which initializes the attributes of the class
     * 
     * @param columnNames String[] which contents the columns name of the table
     * @param classTypes Class[] which contents the columns type of the table
     */
    public ArrayTableModel(String[] columnNames, Class[] classTypes) {
        this.columnNames = columnNames;
        this.defaultValuesRow = new Object [this.columnNames.length];
        this.classTypes = classTypes;
        data = new ArrayList<Object[]>();
        editable = new ArrayList<Boolean[]>();        
        this.defaultEditableRow = new Boolean[columnNames.length];
    }
    
    /** Constructor which initializes the attributes of the class
     * 
     * @param columnNames String[] which contains the columns name of the table
     * @param classTypes Class[] which contains the class type of each column
     * @param defaultValues Object[] which contains the default values of each column
     */
    public ArrayTableModel(String[] columnNames, Class[] classTypes, Object[] defaultValues) {
        this.defaultValuesRow = defaultValues;
        this.columnNames = columnNames;
        data = new ArrayList<Object[]>();
        editable = new ArrayList<Boolean[]>();        
        this.classTypes = classTypes;
        this.defaultEditableRow = new Boolean[columnNames.length];
    }
    
    /** Constructor which initializes the attributes of the class
     * 
     * @param data ArrayList<Object[]>] which contents the data of the table
     * @param colNames String[] which contains the columns name of the table
     * @param editable ArrayList<Boolean[]> which indicates the editable cells
     */
    public ArrayTableModel(ArrayList<Object[]> data, String [] colNames, ArrayList<Boolean[]> editable)
    {
        this.defaultValuesRow = new Object[this.columnNames.length];
        this.columnNames = colNames;
        this.data = data;
        this.editable = editable;
        this.classTypes = new Class[this.columnNames.length];
        for(int i =0;i<this.classTypes.length;i++)
                this.classTypes[i] =data.get(i).getClass();
        this.defaultEditableRow = new Boolean[colNames.length];
    }

    /** Constructor which initializes the attributes of the class
     * 
     * @param columnNames String[] which contains the columns name of the table
     * @param classTypes Class[] which contains the class type of each column
     * @param defaultValuesRow Object[] which contains the default values of each column
     * @param defaultEditableRow boolean[] which contains the default editable value for each cell
     */
    public ArrayTableModel(String[] columnNames, Class[] classTypes, Object[] defaultValuesRow, Boolean[] defaultEditableRow) {
        this.defaultValuesRow = defaultValuesRow;
        this.columnNames = columnNames;
        data = new ArrayList<Object[]>();
        editable = new ArrayList<Boolean[]>();        
        this.classTypes = classTypes;
        this.defaultEditableRow =defaultEditableRow;
    }
    
    /** Constructor which initializes the attributes of the class
     * 
     * @param data ArrayList<Object[]>] which contains the data of the table
     * @param colNames String[] which contains the columns name of the table
     * @param editable ArrayList<Boolean[]> which indicates the editable cells
     * @param classTypes Class[] which contains the class type of each column
     */
    public ArrayTableModel(ArrayList<Object[]> data, String [] colNames, ArrayList<Boolean[]> editable,Class[]classTypes)
    {
        this.defaultValuesRow = new Object[this.columnNames.length];
        this.columnNames = colNames;
        this.data = data;
        this.editable = editable;
        this.classTypes = classTypes;
        this.defaultEditableRow = new Boolean[colNames.length];
    }

    /** Constructor which initializes the attributes of the class
     * 
     * @param data ArrayList<Object[]>] which contains the data of the table
     * @param colNames String[] which contains the columns name of the table
     * @param editable ArrayList<Boolean[]> which indicates the editable cells
     * @param classTypes Class[] which contains the class type of each column
     * @param defaultValues Object[] which contains the default values of each column
     */
    public ArrayTableModel(ArrayList<Object[]> data, String [] colNames, ArrayList<Boolean[]> editable,Class[]classTypes,Object[] defaultValues)
    {
        this.defaultValuesRow = defaultValues;
        this.columnNames = colNames;
        this.data = data;
        this.editable = editable;
        this.classTypes = classTypes;
        this.defaultEditableRow = new Boolean[colNames.length];
    }
    
    /** Constructor which initializes the attributes of the class
     * 
     * @param data ArrayList<Object[]>] which contains the data of the table
     * @param colNames String[] which contains the columns name of the table
     * @param editable ArrayList<Boolean[]> which indicates the editable cells
     * @param classTypes Class[] which contains the class type of each column
     * @param defaultValues Object[] which contains the default values of each column
     * @param defaultEditable boolean[] which contains the defalt editable value of each
     */
    public ArrayTableModel(ArrayList<Object[]> data, String [] colNames, ArrayList<Boolean[]> editable,Class[]classTypes,Object[] defaultValues,Boolean[] defaultEditable)
    {
        this.defaultValuesRow = defaultValues;
        this.columnNames = colNames;
        this.data = data;
        this.editable = editable;
        this.classTypes = classTypes;
        this.defaultEditableRow = defaultEditable;
    }
    
    /** Method which sets the defauls value of a row in the table
     * 
     * @param values Object[] which contains the default values of the row
     */
    public void setDefaultValuesRow(Object [] values)
    {
        this.defaultValuesRow = values;
    }
    
    /** Method which returns the default values of the row
     * 
     * @return Object[] which contains the default values of the row
     */
    public Object [] getDefaultValuesRow()
    {
        return this.defaultValuesRow;
    }
    
    /** Method which allows to set the default value of a column
     * 
     * @param column int which contains the number of the column
     * @param value Object which contains the defaulot value of the column
     */
    public void setDefaultValueColumn(int column, Object value)
    {
        this.defaultValuesRow[column] = value;
    }
    
    /** Method which returns the default value of a column
     * 
     * @param column int which contains the number of the column
     * @return Object which contains the default value of the column
     */
    public Object getDefaultValueColumn(int column)
    {
        return this.defaultValuesRow[column];
    }
    
    
    public void setDefaultEditableValuesRow(Boolean[] defaultEditable)
    {
        this.defaultEditableRow = defaultEditable;
    }
    
    public Boolean[] getDefaultEditableValuesRow()
    {
        return this.defaultEditableRow;
    }
    
    public void setDefaultEditableValueColumn(int column, boolean value)
    {
        this.defaultEditableRow[column]= value;
    }
    
    public boolean getDefaultEditableValueColumn(int column)
    {
        return this.defaultEditableRow[column];
    }
            
    /** Method which returns the number of rows in the table
     * 
     * @return int it contents the number of rows
     */
    public int getRowCount() {
        return data.size()+1;
    }

    /** Method which returns the number of columns in the table
     * 
     * @return int it contents the number of columns
     */
    public int getColumnCount() {
       return this.columnNames.length;
    }

    /** Method which returns the specific object
     * 
     * @param row int which contents the row 
     * @param col int which contents the column
     * @return Objetc which contents the specific objetc of the row row and the column col.
     */
    public Object getValueAt(int row, int col) {
        Object defaultRowCol;
            if (row >= data.size()) {
                 defaultRowCol=new Object();
                 defaultRowCol= this.defaultValuesRow[col];
                return defaultRowCol;
            }
            return data.get(row)[col];
    }

    public void setPosEditable(int row, int column, boolean editable) 
    {
        if(column >= this.columnNames.length)
            return;
        if(row< this.editable.size())
            this.editable.get(row)[column] = editable;
        else
        {
            Boolean[] editAry = new Boolean[this.columnNames.length];
            editAry[column] = editable;
            this.editable.add(row, editAry);
        }
    }
    
    /** Method which sets a new row into the table
     * 
     * @param rowData Object[] which content the rows data
     * @param row int which contents the rows number
     * @param edit boolean which specific if the row is editable
     */
    public void setRow(Object[] rowData, int row, boolean edit){
        Boolean[] editAry;
        editAry=new Boolean[columnNames.length];
        for (int i=0; i< columnNames.length; i++){
            editAry[i]=edit;
        }
        if (row >= data.size()){
            data.add(row, rowData);
        } else {
            data.set(row, rowData);
        }
        if (row>=editable.size()) {
            editable.add(row,editAry);
        } else {
            editable.set(row,editAry);
        }
        this.fireTableRowsUpdated(row, row);
    }
    
    /** Method which sets the classes of the objects into the table
     * 
     * @param classData Class[] which content the class data
     */
    public void setClass(Class[] classData){
        this.classTypes=classData;
    }    

    /** Method which sets a new row into the table
     * 
     * @param rowData Object[] which content the rows data
     * @param row int which contents the rows number
     */
    public void setRow(Object[] rowData, int row){
        setRow(rowData,row,true);
    }
    
    /** Method which sets the columns name of the table
     * 
     * @param newColumns String[] contents the columns name of the table
     */
    public void setColumns(String[] newColumns){
        columnNames = newColumns;
    }
    
    /** Method which returns the column name of the column number col
     * 
     * @param col int which specifics the column number
     * @return String with the column name
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /** Method which returns Objects[] which contents the data of the row
     * 
     * @param row int number of the row you can get the data
     * @return Objects[] with the row data
     */
    public Object[] getRow(int row){
        Object[] newRow;
        if (row >= data.size()) {
            newRow=new Object[this.columnNames.length];
            newRow=this.defaultValuesRow;
            return  newRow;
        }
        else
            return data.get(row);
    }

    /** Method which returns the class of the column specific
     * 
     * @param c int the columns number
     * @return Class return the Class of the specific column
     */
    @Override
    public Class getColumnClass(int c) {
        return this.classTypes[c];
    }

    /** Method which returns a boolean which indicates if the specific cell is editable
     * 
     * @param row int row number
     * @param col int column number
     * @return boolean which indicates if the specific cell is editable
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        
        if (row>=editable.size()) 
            return this.defaultEditableRow[col];  
        else
        {
            if(this.editable.get(row)[col])
                return true;
            else 
                return false;
        }
    }

    /** Method which sets a value in the cell specific by the row and column
     * 
     * @param value contents the value to inserts in the cell
     * @param row int rows number
     * @param col int columns number
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        Object[] oldRowData;
        Boolean[] oldRowEditable;
        if (row>=data.size())  {
            oldRowData=new Object[columnNames.length];
            oldRowEditable = new Boolean[columnNames.length];
            for(int i = 0; i< this.columnNames.length;i++)
            {
                oldRowData[i] = this.defaultValuesRow[i];
                oldRowEditable[i]= this.defaultEditableRow[i];
            }
            oldRowData[col]=value;
            this.data.add(row,oldRowData);
            this.editable.add(row, oldRowEditable);
        }
        else { 
            oldRowData=new Object[columnNames.length];
            oldRowEditable = new Boolean[columnNames.length];
            for(int i = 0;i<this.getColumnCount();i++){
                oldRowData[i]=data.get(row)[i];
                oldRowEditable[i]=this.editable.get(row)[i];
            }
            oldRowData[col]=value;
            this.data.set(row,oldRowData);
            this.editable.set(row, oldRowEditable);
        }
        fireTableCellUpdated(row, col);
    }
    
    /** Method which deletes the rows data in the table
     * 
     */
    public void removeRows()
    {
        this.data = null;
        this.data = new ArrayList<Object[]>();
        this.fireTableDataChanged();
    }
    
    /** Method which removes the row given as argument
     * 
     * @param row int. This is the row's number to remove
     */
    public void deleteRow(int row){       
        if(row+1<this.getRowCount()){
            this.data.remove(row);
            this.editable.remove(row);
            this.fireTableCellUpdated(row, 0);        
        }
    }
    
    /** Method which inserts a row after a row given as argument
     * 
     * @param row int. This is the row's number to insert after
     */
    public void insertRowAfter(int row){
        if(row+2<this.getRowCount()){
            this.data.add(row+1, this.defaultValuesRow);
            this.editable.add(row+1,this.defaultEditableRow);
        }else{
            if(row+1<this.getRowCount()){
                this.setRow(this.defaultValuesRow, row+1);
            }else{
                this.setRow(this.defaultValuesRow, row);
            }
        }
    }
    
    /** Method which inserts a row before a row given as argument
     * 
     * @param row int. This is the row's number to insert before
     */
    public void insertRowBefore(int row){
        if(row+1<this.getRowCount()){
            this.data.add(row, this.defaultValuesRow);
            this.editable.add(row,this.defaultEditableRow);
        }else{
            this.setRow(this.defaultValuesRow, row);
        }
    }
    
    /** Method which moves a group of rows one position. If the initial position were 3, the final position after execute this method would be 4.
     * 
     * @param init int. This is the initial position 
     * @param end int. This is the end position. This position is not included in the group, for sample to use: getRowCount()
     */
    private void moveRows(int init,int end){
        ArrayList <Object[]>newData = new ArrayList<Object[]>();
        ArrayList <Boolean[]>newEditable = new ArrayList <Boolean[]>();
        
        //Copy from row 0 to row init-1 to the new ArrayList
        for(int i = 0;i<init;i++){
            newData.add(this.data.get(i));
            newEditable.add(this.editable.get(i));
        }
        
        //Add a new row
        newData.add(this.defaultValuesRow);
        newEditable.add(this.defaultEditableRow);
        
        //Copy from row init to row end
        for(int i=init;i<end-1;i++){
            newData.add(this.data.get(i));
            newEditable.add(this.editable.get(i));
        }
        
        this.data=newData;
        this.editable=newEditable;
        
        //We update the table 
        fireTableCellUpdated(init, 0);
    }
    
}