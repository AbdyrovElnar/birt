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

package org.eclipse.birt.chart.computation.withoutaxes;

import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.exception.NotFoundException;
import org.eclipse.birt.chart.exception.NullValueException;
import org.eclipse.birt.chart.exception.OutOfSyncException;
import org.eclipse.birt.chart.exception.UndefinedValueException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.emf.common.util.EList;

/**
 *  
 */
public final class PlotWithoutAxes
{

    /**
     *  
     */
    private final IDisplayServer xs;

    /**
     *  
     */
    private final ChartWithoutAxes cwoa;

    private final Locale lcl;

    /**
     *  
     */
    private transient double dPointToPixel = 0;

    private transient Size szCell = null;

    private transient int iRows = 0, iColumns = 0, iSeries = 0;

    private transient Bounds boPlot = null;

    private transient Insets insCA = null;

    /**
     * 
     * @param xs
     * @param cwoa
     */
    public PlotWithoutAxes(IDisplayServer xs, ChartWithoutAxes cwoa, Locale lcl)
    {
        this.xs = xs;
        this.cwoa = cwoa;
        this.lcl = lcl;
        dPointToPixel = xs.getDpiResolution() / 72d;
    }

    /**
     * 
     * @param boPlot
     */
    public final void compute(Bounds bo)
    {
        //bo.adjustDueToInsets(cwoa.getPlot().getInsets()); // INSETS DEFINED
        // IN POINTS: ALREADY COMPENSATED IN GENERATOR!
        boPlot = bo.scaledInstance(dPointToPixel); // CONVERSION TO PIXELS
        //final Series[] sea = cwoa.getRunTimeSeries();

        EList el = cwoa.getSeriesDefinitions();
        ArrayList al = new ArrayList();
        ((ChartWithoutAxesImpl) cwoa).recursivelyGetSeries(el, al, 0, 0);
        final Series[] sea = (Series[]) al.toArray(Series.EMPTY_ARRAY);

        iSeries = sea.length;
        iColumns = cwoa.getGridColumnCount();
        iRows = (iSeries - 1) / iColumns + 1;

        szCell = SizeImpl.create(boPlot.getWidth() / iColumns, boPlot.getHeight() / iRows);
        insCA = cwoa.getPlot().getClientArea().getInsets().scaledInstance(dPointToPixel);
    }

    /**
     * 
     * @return
     */
    public final Size getCellSize()
    {
        return szCell;
    }

    /**
     * 
     * @return
     */
    public final Insets getCellInsets()
    {
        return insCA;
    }

    /**
     * 
     * @param iCell
     * @return
     */
    public final Coordinates getCellCoordinates(int iCell)
    {
        return new Coordinates(iCell % iColumns, iCell / iColumns);
    }

    /**
     * 
     * @return
     */
    public final int getColumnCount()
    {
        return iColumns;
    }

    /**
     * 
     * @return
     */
    public final int getRowCount()
    {
        return iRows;
    }

    /**
     * 
     * @return
     */
    public final Bounds getBounds()
    {
        return boPlot;
    }

    /**
     * 
     * @param seOrthogonal
     * @return
     */
    public final SeriesRenderingHints getSeriesRenderingHints(Series seOrthogonal) throws NullValueException,
        DataFormatException, NotFoundException, OutOfSyncException, UndefinedValueException, UnexpectedInputException
    {
        final EList elCategories = cwoa.getSeriesDefinitions();
        if (elCategories.size() != 1)
        {
            throw new DataFormatException("Charts without axes may contain a single series definition only");
        }
        final SeriesDefinition sd = (SeriesDefinition) elCategories.get(0);
        final ArrayList al = sd.getRunTimeSeries();
        if (al.size() != 1)
        {
            throw new DataFormatException("Charts without axes may contain a single runtime series only");
        }
        final Series seBase = (Series) al.get(0);
        final DataSetIterator dsiBaseValues = new DataSetIterator(seBase.getDataSet());
        final DataSetIterator dsiOrthogonalValues = new DataSetIterator(seOrthogonal.getDataSet());
        if (dsiBaseValues.size() != dsiOrthogonalValues.size())
        {
            throw new OutOfSyncException("Input data is out-of-sync; base contains " + dsiBaseValues.size()
                + " values; orthogonal contains " + dsiOrthogonalValues.size() + " values.");
        }

        final int iCount = dsiOrthogonalValues.size();
        final DataPointHints[] dpha = new DataPointHints[iCount];
        for (int i = 0; i < iCount; i++)
        {
            dpha[i] = new DataPointHints(dsiBaseValues.next(), dsiOrthogonalValues.next(), seOrthogonal.getDataPoint(),
                seBase.getFormatSpecifier(), seOrthogonal.getFormatSpecifier(), null, null, -1, lcl);
        }

        return new SeriesRenderingHints(dpha);
    }
}