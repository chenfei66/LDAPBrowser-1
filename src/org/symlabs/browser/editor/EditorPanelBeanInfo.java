package org.symlabs.browser.editor;

import java.beans.BeanDescriptor;

/**
 *
 * @author efernandez
 */
public class EditorPanelBeanInfo extends java.beans.SimpleBeanInfo{

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor desc=new BeanDescriptor(EditorPanel.class);
        desc.setValue("containerDelegate", "getEditorContainerDelegate");
        return desc;
    }
}
