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

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

/**
 * <!-- begin-user-doc --> The <b>Resource Factory </b> associated with the package. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.util.ModelResourceImpl
 * @generated
 */
public class ModelResourceFactoryImpl extends XMLResourceFactoryImpl
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ExtendedMetaData extendedMetaData;

    /**
     * Creates an instance of the resource factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     */
    public ModelResourceFactoryImpl()
    {
        super();
        extendedMetaData = ExtendedMetaData.INSTANCE;
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModel", ModelPackage.eINSTANCE);
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModelAttribute", AttributePackage.eINSTANCE);
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModelComponent", ComponentPackage.eINSTANCE);
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModelData", DataPackage.eINSTANCE);
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModelLayout", LayoutPackage.eINSTANCE);
        extendedMetaData.putPackage("http://www.birt.eclipse.org/ChartModelType", TypePackage.eINSTANCE);
    }

    /**
     * Creates an instance of the resource. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Resource createResource(URI uri)
    {
        XMLResource result = new ModelResourceImpl(uri);
        result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
        result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

        result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
        result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        return result;
    }

} //ModelResourceFactoryImpl
