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

package org.eclipse.birt.chart.device.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.eclipse.birt.chart.device.DisplayAdapter;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;

/**
 *  
 */
public class SwingDisplayServer extends DisplayAdapter
{

    /**
     *  
     */
    private transient BufferedImage _bi = null;

    /**
     *  
     */
    private transient Graphics2D _g2d = null;

    /**
     *  
     */
    private double dScale = 1;

    /**
     *  
     */
    private final java.awt.Panel p = new java.awt.Panel(); // NEEDED FOR IMAGE

    // LOADING

    /**
     * 
     * @return
     */
    public SwingDisplayServer()
    {
        final ILogger il = DefaultLoggerImpl.instance();
        _bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        _g2d = (Graphics2D) _bi.getGraphics();
        _g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        _g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        dScale = getDpiResolution() / 72d;
        _g2d.scale(dScale, dScale);
        il.log(ILogger.INFORMATION, "SWING XServer: " + System.getProperty("java.vendor") + " v"
            + System.getProperty("java.version"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#createFont(org.eclipse.birt.chart.attribute.FontDefinition)
     */
    public final Object createFont(FontDefinition fd)
    {
        final Map m = new HashMap();
        m.put(TextAttribute.FAMILY, fd.getName());
        m.put(TextAttribute.SIZE, new Float(fd.getSize()));
        if (fd.isItalic())
        {
            m.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        }
        if (fd.isBold())
        {
            m.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        }
        if (fd.isUnderline())
        {
            m.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        }
        if (fd.isStrikethrough())
        {
            m.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        }
        return new Font(m);
    }

    /**
     *  
     */
    public final Object getColor(ColorDefinition cd)
    {
        return new Color(cd.getRed(), cd.getGreen(), cd.getBlue(), cd.getTransparency());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#getMetrics(org.eclipse.birt.chart.attribute.FontDefinition,
     *      java.lang.Object)
     */
    public final Object getMetrics(FontDefinition fd)
    {
        return _g2d.getFontMetrics((Font) createFont(fd));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#getDpiResolution()
     */
    public final double getDpiResolution()
    {
        return Toolkit.getDefaultToolkit().getScreenResolution();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#loadImage(java.lang.String)
     */
    public final Object loadImage(URL url) throws ImageLoadingException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Loading image " + url);
        final Image img = (new ImageIcon(url)).getImage();
        try
        {
            final MediaTracker tracker = new MediaTracker(p);
            tracker.addImage(img, 0);
            tracker.waitForAll();

            if ((tracker.statusAll(true) & MediaTracker.ERRORED) != 0)
            {
                StringBuffer sb = new StringBuffer();
                Object[] oa = tracker.getErrorsAny();
                sb.append('[');
                for (int i = 0; i < oa.length; i++)
                {
                    sb.append(oa[i]);
                    if (i < oa.length - 1)
                    {
                        sb.append(", ");
                    }
                }
                sb.append(']');
                throw new ImageLoadingException("MediaTracker returned an error in " + sb.toString());
            }
        }
        catch (InterruptedException ex )
        {
            throw new ImageLoadingException(ex);
        }

        return img;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#getSize(java.lang.Object)
     */
    public final Size getSize(Object oImage)
    {
        final Image img = (Image) oImage;
        return SizeImpl.create(img.getWidth(p), img.getHeight(p));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.devices.XServer#getObserver()
     */
    public final Object getObserver()
    {
        return p;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.XServer#getTextMetrics(org.eclipse.birt.chart.model.component.Label)
     */
    public ITextMetrics getTextMetrics(Label la)
    {
        return new SwingTextMetrics(this, la);
    }
}