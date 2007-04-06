
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

import testutil.BaseTestCase;


/**
 * 
 */

public class CubeFeaturesTest extends BaseTestCase
{
	private static String documentPath = System.getProperty( "java.io.tmpdir" );
	private static String cubeName = "cube";
	
	/**
	 * Test use all dimension levels.
	 * @throws Exception
	 */
	public void testBasic( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding3 = new Binding( "edge1level3");
		
		binding3.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level13\"]") );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	}
	
	/**
	 * Test use part of dimension levels.
	 * @throws Exception
	 */
	public void testBasic1( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	
	}
	
	/**
	 * Filter1, filter out all level11 == CN.
	 * @throws Exception
	 */
	public void testFilter1( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		cqd.addFilter( filter );
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	
	}
	
	/**
	 * Filter2, filter out all level11 = CN and level21 > 2000.
	 * @throws Exception
	 */
	public void testFilter2( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		IFilterDefinition filter1 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		IFilterDefinition filter2 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_GE,
				"2000" ) );
		
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	
	}
	/**
	 * Simple sort on 1 level
	 * @throws Exception
	 */
	public void testSort1( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		SortDefinition sorter = new SortDefinition();
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		cqd.addSort( sorter );
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	
	}
	
	/**
	 * Complex sort on multiple levels
	 * @throws Exception
	 */
	public void testSort2( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		//sort on year
		SortDefinition sorter1 = new SortDefinition();
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on country
		SortDefinition sorter2 = new SortDefinition();
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on city.
		SortDefinition sorter3 = new SortDefinition();
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3);
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1" );
	
	}
	
	/**
	 * Test grand total
	 * @throws Exception
	 */
	public void testGrandTotal( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "rowGrandTotal");
		binding6.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding6.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "level21" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "columnGrandTotal");
		binding7.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding7.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "level11" );
		binding7.addAggregateOn( "level12" );
		cqd.addBinding( binding7 );
		
		IBinding binding8 = new Binding( "grandTotal");
		binding8.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding8.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );
		
		//sort on year
		SortDefinition sorter1 = new SortDefinition();
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on country
		SortDefinition sorter2 = new SortDefinition();
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on city.
		SortDefinition sorter3 = new SortDefinition();
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3);
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor, columnEdgeBindingNames, "edge2level1", "measure1", "columnGrandTotal", "rowGrandTotal", "grandTotal" );
	
	}
	
	/**
	 * Test binding "row" reference
	 * @throws Exception
	 */
	public void testBindingRowReference( ) throws Exception
	{
		this.createCube( );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName);
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		
		cqd.createMeasure( "measure1" );
		
		IBinding binding1 = new Binding( "edge1level1");
		
		binding1.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level11\"]") );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2");
		
		binding2.setExpression( new ScriptExpression("dimension[\"dimension1\"][\"level12\"]") );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "edge2level1");
		
		binding4.setExpression( new ScriptExpression("dimension[\"dimension2\"][\"level21\"]") );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "rowGrandTotal");
		binding6.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding6.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "level21" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "columnGrandTotal");
		binding7.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding7.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "level11" );
		binding7.addAggregateOn( "level12" );
		cqd.addBinding( binding7 );
		
		IBinding binding8 = new Binding( "grandTotal");
		binding8.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		binding8.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );
		
		IBinding binding9 = new Binding( "row_rowGrandTotal");
		binding9.setExpression( new ScriptExpression("row[\"rowGrandTotal\"]*10") );
		cqd.addBinding( binding9 );
		
		IBinding binding10 = new Binding( "row_columnGrandTotal");
		binding10.setExpression( new ScriptExpression("row[\"columnGrandTotal\"]*10") );
		cqd.addBinding( binding10 );
		
		IBinding binding11 = new Binding( "row_grandTotal");
		binding11.setExpression( new ScriptExpression("row[\"grandTotal\"]*10") );
		cqd.addBinding( binding11 );
	
		IBinding binding12 = new Binding( "row_measure1" );
		binding12.setExpression( new ScriptExpression("row[\"measure1\"]*10") );
		cqd.addBinding( binding12 );
				
		//sort on year
		SortDefinition sorter1 = new SortDefinition();
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on country
		SortDefinition sorter2 = new SortDefinition();
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		
		//sort on city.
		SortDefinition sorter3 = new SortDefinition();
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3);
		DataEngine engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		this.printCube( cursor,
				columnEdgeBindingNames,
				"edge2level1",
				"row_measure1",
				"row_columnGrandTotal",
				"row_rowGrandTotal",
				"row_grandTotal" );
	
	}
		
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames, String rowEdgeBindingNames, String measureBindingNames ) throws Exception
	{
		this.printCube( cursor, columnEdgeBindingNames, rowEdgeBindingNames, measureBindingNames, null, null, null );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames, String rowEdgeBindingNames, String measureBindingNames, String columnAggr, String rowAggr, String overallAggr ) throws Exception
	{
		EdgeCursor edge1 = (EdgeCursor) (cursor.getOrdinateEdge( ).get( 0 ));
		EdgeCursor edge2 = (EdgeCursor) (cursor.getOrdinateEdge( ).get( 1 ));

		String[] lines = new String[ edge1.getDimensionCursor( ).size( ) ];
		for ( int i = 0; i < lines.length; i++ )
		{
			lines[i] = "		";
		}
		
		while( edge1.next( ) )
		{
			for ( int i = 0; i < lines.length; i++ )
			{
				lines[i] += cursor.getObject( columnEdgeBindingNames.get( i ).toString( ) ) + "		";
			}
		}
		
		if ( rowAggr != null )
			lines[lines.length-1] += "Total";
		
		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output+="\n" + lines[i] ;
		}
		
		while ( edge2.next( ) )
		{
			String line = cursor.getObject( rowEdgeBindingNames ).toString( )  + "		";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{ 
				line+= cursor.getObject( measureBindingNames ) + "		";
			}
			
			if ( rowAggr!= null )
				line+= cursor.getObject( rowAggr );
			output+="\n" + line;
		}
		
		if ( columnAggr!= null )
		{
			String line = "Total" + "		";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{ 
				line+= cursor.getObject( columnAggr ) + "		";
			}
			if ( overallAggr != null )
				line+= cursor.getObject( overallAggr );
			
			output+="\n" + line;
		}
		this.testPrint( output );

		this.checkOutputFile( );
	}
	
	private void createCube() throws BirtException, IOException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( documentPath, cubeName);

		Dimension[] dimensions = new Dimension[2];

		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L1Col );
		iterator.setLevelMember( 1, TestFactTable.DIM0_L2Col );
		iterator.setLevelMember( 2, TestFactTable.DIM0_L3Col );

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{
			"level11"
		}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{
			"level12"
		}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{
			"level13"
		}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), 13 );

		// dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.DIM1_L1Col ) );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{
			"level21"
		}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2",
				documentManager,
				iterator,
				levelDefs,
				false );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 5 );

		TestFactTable factTable2 = new TestFactTable( );
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube( cubeName, documentManager );

		cube.create( dimensions, factTable2, measureColumnName, new StopSign( ) );
		
		cube.close( );
		documentManager.close( );

	}
	
	private String[] distinct( String[] values )
	{
		String[] lValues = new String[values.length];
		System.arraycopy( values, 0, lValues, 0, values.length );
		Arrays.sort( lValues );
		List tempList = new ArrayList( );
		tempList.add( lValues[0] );
		for ( int i = 1; i < lValues.length; i++ )
		{
			if ( !lValues[i].equals( lValues[i - 1] ) )
			{
				tempList.add( lValues[i] );
			}
		}
		String[] result = new String[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = ((String)tempList.get( i ));
		}
		return result;
	}
	
}

class TestFactTable implements IDatasetIterator
{

	int ptr = -1;
	static String[] DIM0_L1Col = {
			"CN", "CN", "CN","CN", "CN",
			"CN", "CN", "CN","CN", "CN",
			"CN", "CN", "CN","CN", "CN",
			"CN", "CN", "CN","CN", "CN",
			"CN", "CN", "CN","CN", "CN",
			"CN", "CN", "CN","CN", "CN",
			"US", "US", "US","US", "US", 
			"US", "US", "US","US", "US",
			"US", "US", "US","US", "US",
			"UN", "UN", "UN", "UN","UN",
			"UN", "UN", "UN", "UN","UN",
			"JP", "JP","JP", "JP","JP", 
			"JP","JP", "JP","JP", "JP"
	};
	static String[] DIM0_L2Col = {
			"SH", "SH","SH", "SH","SH",
			"SH", "SH","SH", "SH","SH",
			"BJ", "BJ","BJ", "BJ","BJ",
			"BJ", "BJ","BJ", "BJ","BJ",
			"SZ", "SZ","SZ", "SZ","SZ",
			"SZ", "SZ","SZ", "SZ","SZ",
			"LA", "LA","LA","LA","LA",
			"CS", "CS","CS","CS","CS",
			"NY", "NY","NY","NY","NY",
			"LD","LD","LD","LD","LD",
			"LP","LP","LP","LP","LP",
			"TK","TK","TK","TK","TK",
			"IL","IL","IL","IL","IL"
	};
	static String[] DIM0_L3Col = {
		    "PD","PD","PD","PD","PD",
		    "ZJ", "ZJ", "ZJ", "ZJ", "ZJ",
		    "HD","HD","HD","HD","HD",
		    "CP","CP","CP","CP","CP",
		    "S1","S1","S1","S1","S1",
		    "S2","S2","S2","S2","S2",
		    "A1","A1","A1","A1","A1",
		    "B1","B1","B1","B1","B1",
		    "C1","C1","C1","C1","C1",
		    "D1","D1","D1","D1","D1",
		    "E1","E1","E1","E1","E1",
		    "F1","F1","F1","F1","F1",
		    "P1","P1","P1","P1","P1"
	};
	static String[] DIM1_L1Col = {
		"1998","1999","2000","2001","2002",
		"1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002",
	    "1998","1999","2000","2001","2002"
    };
	
	static int[] MEASURE_Col = {
		1,2,3,4,5,
		6,7,8,9,10,
		11,12,13,14,15,
		16,17,18,19,20,
		21,22,23,24,25,
		26,27,28,29,30,
		31,32,33,34,35,
		36,37,38,39,40,
		41,42,43,44,45,
		46,47,48,49,50,
		51,52,53,54,55,
		56,57,58,59,60,
		61,62,63,65,65
    };
	
	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	public int getFieldIndex( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return 0;
		}
		else if ( name.equals( "level12" ) )
		{
			return 1;
		}
		else if ( name.equals( "level13" ) )
		{
			return 2;
		}
		else if ( name.equals( "level21" ) )
		{
			return 3;
		}
		else if ( name.equals( "measure1" ) )
		{
			return 4;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level12" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level13" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level21" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "measure1" ) )
		{
			return DataType.INTEGER_TYPE;
		}

		return -1;
	}

	public Integer getInteger( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getString( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue( int fieldIndex ) throws BirtException
	{
		if ( fieldIndex == 0 )
		{
			return DIM0_L1Col[ptr];
		}
		else if ( fieldIndex == 1 )
		{
			return  DIM0_L2Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return DIM0_L3Col[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return DIM1_L1Col[ptr];
		}
		else if ( fieldIndex == 4 )
		{
			return new Integer( MEASURE_Col[ptr] );
		}

		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= MEASURE_Col.length )
		{
			return false;
		}
		return true;
	}
}

