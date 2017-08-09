package org.symlabs.ui;

import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * <p>Titulo: SearchList </p>
 * <p>Descripcion:Class to implement a list that includes a search text box when characters are typed.. </p>
 * <p>Copyright: Emilio Fernandez  2009</p>
 * @author Emilio J. Fernandez Rey
 * @version 1.0
 * @id $Id: SearchList.java,v 1.2 2009-07-01 16:45:04 efernandez Exp $
 */
public class SearchList extends JList {

  private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SearchList.class);
  private JPanel searchPanel = null;
  private JLabel searchLabel = null;
  private JTextField searchTextField = new JTextField() {

    @Override
    public void processKeyEvent(KeyEvent ke) {
      // The ESC key closes the search subpanel
      if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
        removeSearchField();
        ke.consume();
        SearchList.this.requestFocus();
      } else {
        super.processKeyEvent(ke);
      }
    }
  };
  final private int textFieldHeight = searchTextField.getPreferredSize().height;

  public SearchList() {
    super();
    setupSearch();
  }

  private void setupSearch() {
    // Add new key listeners
    addKeyListener(
            new KeyAdapter() {

              @Override
              public void keyPressed(KeyEvent e) {
                int modifiers = e.getModifiers();
                int keyCode = e.getKeyCode();

                if (((modifiers > 0) && (modifiers != KeyEvent.SHIFT_MASK)) || e.isActionKey()) {
                  return;
                }

                char c = e.getKeyChar();

                if (!Character.isISOControl(c) && (keyCode != KeyEvent.VK_SHIFT)) {
                  searchTextField.setText(searchTextField.getText() + String.valueOf(c));
                  displaySearchField();
                }
              }
            });

    SearchFieldListener searchFieldListener = new SearchFieldListener();
    searchTextField.addKeyListener(searchFieldListener);
    searchTextField.addFocusListener(searchFieldListener);
    searchTextField.getDocument().addDocumentListener(searchFieldListener);
  }

  /**
   * Method that searches all the elements in the list model and returns an
   * a List of positions that match the search criteria
   * An element of the model matches if the textToSearch is contained withtin the
   * toString() of the model element (case insensitive).
   * @param textToSearch Text to locate inside the string representation of the elements
   * @return List of positions that contains the text in case insensitive manner
   */
  private List<Integer> doSearch(String textToSearch) {
    String textToSearchLC = textToSearch.toLowerCase();
    List<Integer> results = new ArrayList<Integer>();
    int listSize = this.getModel().getSize();
    String potentialName;
    int startIndex = this.getSelectedIndex();
    if (startIndex == -1) {
      startIndex = 0;
    }
//    logger.trace("Starting search in " + startIndex);
    int i = (startIndex) % listSize;
    for (int count = 0; count < listSize; count++) {
      potentialName = this.getModel().getElementAt(i).toString();
      if (potentialName.toLowerCase().contains(textToSearchLC)) {
//        logger.trace("found a candidate in position " + i);
        results.add(Integer.valueOf(i));
      } else {
//        logger.trace("not good " + potentialName);
      }
      i = ((i + 1) % listSize);
    }

    return results;
  }

  private void prepareSearchPanel() {
    if (this.searchPanel == null) {
      this.searchPanel = new JPanel();
      this.searchLabel = new JLabel("Search:");

      this.searchPanel.setLayout(new BoxLayout(this.searchPanel, BoxLayout.X_AXIS));
      this.searchPanel.add(this.searchLabel);
      this.searchPanel.add(this.searchTextField);
      this.searchLabel.setLabelFor(this.searchTextField);
      this.searchPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      this.searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    }
  }

  /**
   * Adds the Search Field to this element.
   * The real position of the element is done in the @link doLayout method
   */
  protected void displaySearchField() {
    if ((getModel().getSize() > 0) && !searchTextField.isDisplayable()) {

      //create the panel if not already init
      prepareSearchPanel();
      add(searchPanel);
      revalidate();
      repaint();
    }
  }

  @Override
  public void doLayout() {
    super.doLayout();
    final int searchInset = 2;
    final int desiredSearchWidth = 120;
    if ((searchPanel != null) && searchPanel.isDisplayable()) {
      Rectangle visibleRect = getVisibleRect();
      int searchWidth = Math.min(
              visibleRect.width - (2 * searchInset),
              desiredSearchWidth);

      //x,y,width,height
      searchPanel.setBounds(
              visibleRect.x + Math.max(searchInset, visibleRect.width - searchWidth),
              visibleRect.y + searchInset,
              searchWidth,
              textFieldHeight);
    }
  }

  /**
   * Removes the search field from the container
   */
  protected void removeSearchField() {
    if ((searchPanel != null) && searchPanel.isDisplayable()) {
      remove(searchPanel);
      this.repaint(searchPanel.getBounds());
      requestFocus();
    }
  }

  private class SearchFieldListener extends KeyAdapter implements DocumentListener, FocusListener {

    /** The search results of the text of the search field*/
    private List results = new ArrayList();
    /** The index that we are curently displaying of the result list. */
    private int currentSelectionIndex;

    SearchFieldListener() {
    }

    public void changedUpdate(DocumentEvent e) {
      searchForNode();
    }

    public void insertUpdate(DocumentEvent e) {
      searchForNode();
    }

    public void removeUpdate(DocumentEvent e) {
      searchForNode();
    }

    @Override
    public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();

      if (keyCode == KeyEvent.VK_ESCAPE) {
        removeSearchField();
        searchTextField.setText("");
        SearchList.this.requestFocus();
      } else if (keyCode == KeyEvent.VK_UP) {
        currentSelectionIndex--;
        displaySearchResult();
        e.consume();
      } else if (keyCode == KeyEvent.VK_DOWN) {
        currentSelectionIndex++;
        displaySearchResult();
        e.consume();
      } else if (keyCode == KeyEvent.VK_ENTER) {
        removeSearchField();
        SearchList.this.requestFocus();
        //we send the enter to the list itself
        SearchList.this.dispatchEvent(e);
      }
    }

    /** Main functionality
     * only place that calls calls doSearch(). */
    private void searchForNode() {
      currentSelectionIndex = 0;
      results.clear();

      String text = searchTextField.getText();

      if (text.length() > 0) {
        results = doSearch(text);
        displaySearchResult();
      }
    }

    private void displaySearchResult() {
      int resultSize = results.size();
//      logger.trace("My result is of size " + resultSize);
      if (resultSize > 0) {
        //Up and Down keys modify currentSelectionIndex, here we implement
        //the wrap around
        if (currentSelectionIndex < 0) { //press up at the top of the list
          currentSelectionIndex = resultSize - 1;
          //press dow at the bottom of the list
        } else if (currentSelectionIndex >= resultSize) {
          currentSelectionIndex = 0;
        }

        Integer index = (Integer) results.get(currentSelectionIndex);
//        logger.trace("Selecting (" + currentSelectionIndex + "): " + index.intValue());

        SearchList.this.setSelectedIndex(index.intValue());
        SearchList.this.ensureIndexIsVisible(index.intValue());
        // force the redisplay of the search panel in case the viewport has scrolled
        SearchList.this.doLayout();
      } else {
        SearchList.this.clearSelection();
      }
    }

    public void focusGained(FocusEvent e) {
      // Do nothing
    }

    public void focusLost(FocusEvent e) {
      removeSearchField();
    }
  }

}

