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

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.RenderingException;

/**
 *  
 */
public class DeviceAdapter extends EventObjectCache implements IDeviceRenderer
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String sProperty, Object oValue)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#getGraphicsContext()
     */
    public Object getGraphicsContext()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#getXServer()
     */
    public IDisplayServer getDisplayServer()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#before()
     */
    public void before() throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
     */
    public void after() throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#setClip(org.eclipse.birt.chart.event.ClipRenderEvent)
     */
    public void setClip(ClipRenderEvent cre)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawImage(org.eclipse.birt.chart.event.ImageRenderEvent)
     */
    public void drawImage(ImageRenderEvent ire) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawLine(org.eclipse.birt.chart.event.LineRenderEvent)
     */
    public void drawLine(LineRenderEvent lre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
     */
    public void drawRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
     */
    public void fillRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
     */
    public void drawPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
     */
    public void fillPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawArc(org.eclipse.birt.chart.event.ArcRenderEvent)
     */
    public void drawArc(ArcRenderEvent are) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillArc(org.eclipse.birt.chart.event.ArcRenderEvent)
     */
    public void fillArc(ArcRenderEvent are) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#enableInteraction(org.eclipse.birt.chart.event.InteractionEvent)
     */
    public void enableInteraction(InteractionEvent ie) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void drawArea(AreaRenderEvent are) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void fillArea(AreaRenderEvent are) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void drawOval(OvalRenderEvent ore) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void fillOval(OvalRenderEvent ore) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
     */
    public void drawText(TextRenderEvent tre) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.eclipse.birt.chart.event.TransformationEvent)
     */
    public void applyTransformation(TransformationEvent tev) throws RenderingException
    {
        // TODO Auto-generated method stub

    }

}