/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 *  
 */
public class IntegerSpinControl extends Composite implements SelectionListener, KeyListener
{

    private int iSize = 16;

    private int iMinValue = 0;

    private int iMaxValue = 100;

    private int iCurrentValue = 0;

    private int iIncrement = 1;

    private Composite cmpContentOuter = null;

    private Composite cmpContentInner = null;

    private Composite cmpBtnContainer = null;

    Button btnIncrement = null;

    Button btnDecrement = null;

    Text txtValue = null;

    Vector vListeners = null;

    public static final int VALUE_CHANGED_EVENT = 1;

    /**
     * @param parent
     * @param style
     */
    public IntegerSpinControl(Composite parent, int style, int iCurrentValue)
    {
        super(parent, style);
        this.iCurrentValue = iCurrentValue;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        FillLayout fl = new FillLayout();
        fl.marginHeight = 0;
        fl.marginWidth = 0;
        setLayout(fl);

        // THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
        // ITS CONTENT NORTH)
        cmpContentOuter = new Composite(this, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.verticalSpacing = 0;
        gl.horizontalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.numColumns = 1;
        cmpContentOuter.setLayout(gl);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentOuter.setLayoutData(gd);

        // THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
        // THE CANVAS + BUTTON)
        cmpContentInner = new Composite(cmpContentOuter, SWT.NONE);
        gl = new GridLayout();
        gl.verticalSpacing = 0;
        gl.horizontalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.numColumns = 2;
        cmpContentInner.setLayout(gl);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentInner.setLayoutData(gd);

        txtValue = new Text(cmpContentInner, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        gd.verticalAlignment = GridData.BEGINNING;
        gd.heightHint = iSize;
        txtValue.setLayoutData(gd);
        txtValue.setText(String.valueOf(iCurrentValue));
        txtValue.addKeyListener(this);

        cmpBtnContainer = new Composite(cmpContentInner, SWT.NONE);
        gd = new GridData();
        gd.verticalAlignment = GridData.BEGINNING;
        gd.horizontalAlignment = SWT.END;
        cmpBtnContainer.setLayoutData(gd);
        cmpBtnContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        gl = new GridLayout();
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        cmpBtnContainer.setLayout(gl);

        final int iHalf = (iSize + 8) / 2;
        btnIncrement = new Button(cmpBtnContainer, SWT.ARROW | SWT.UP);
        gd = new GridData();
        gd.grabExcessVerticalSpace = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = iHalf;
        gd.widthHint = iHalf;
        btnIncrement.setLayoutData(gd);
        btnIncrement.addSelectionListener(this);

        btnDecrement = new Button(cmpBtnContainer, SWT.ARROW | SWT.DOWN);
        gd = new GridData();
        gd.grabExcessVerticalSpace = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = iHalf;
        gd.widthHint = iHalf;
        btnDecrement.setLayoutData(gd);
        btnDecrement.addSelectionListener(this);
    }

    public void setMinimum(int iMin)
    {
        this.iMinValue = iMin;
    }

    public void setMaximum(int iMax)
    {
        this.iMaxValue = iMax;
    }

    public void setIncrement(int iIncrement)
    {
        this.iIncrement = iIncrement;
    }

    public void setValue(int iCurrent)
    {
        this.iCurrentValue = iCurrent;
        this.txtValue.setText(String.valueOf(iCurrentValue));
    }

    public int getValue()
    {
        return this.iCurrentValue;
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnIncrement))
        {
            if (iCurrentValue < iMaxValue)
            {
                iCurrentValue = (Integer.valueOf(txtValue.getText()).intValue() + iIncrement);
                txtValue.setText(String.valueOf(iCurrentValue));
            }
        }
        else if (oSource.equals(btnDecrement))
        {
            if (iCurrentValue > iMinValue)
            {
                iCurrentValue = (Integer.valueOf(txtValue.getText()).intValue() - iIncrement);
                txtValue.setText(String.valueOf(iCurrentValue));
            }
        }
        // Notify Listeners that a change has occurred in the value
        fireValueChangedEvent();
    }

    private void fireValueChangedEvent()
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            se.data = new Integer(iCurrentValue);
            se.type = IntegerSpinControl.VALUE_CHANGED_EVENT;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

    public Point getPreferredSize()
    {
        return new Point(80, 24);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
        {
            int iValue = (Integer.valueOf(txtValue.getText()).intValue());
            if (iValue >= iMinValue && iValue <= iMaxValue)
            {
                iCurrentValue = iValue;
                fireValueChangedEvent();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }
}