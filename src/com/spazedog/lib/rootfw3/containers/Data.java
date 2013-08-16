/*
 * This file is part of the RootFW Project: https://github.com/spazedog/rootfw
 *  
 * Copyright (c) 2013 Daniel Bergløv
 *
 * RootFW is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * RootFW is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with RootFW. If not, see <http://www.gnu.org/licenses/>
 */

package com.spazedog.lib.rootfw3.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * This container is used to store any kind of data. All of the data is located within a String Array, where each index is considered a line. 
 */
public class Data {
	protected String[] mLines;
	
	/**
	 * This interface is used as the argument for the <code>sort()</code> and <code>assort()</code> methods. It can be used to create custom sorting of the data array.
	 */
	public static interface DataSorting {
		/**
		 * The method which checks the line and determines whether or not it should be added or removed from the array. 
		 * <br />
		 * Note that the <code>sort()</code> method will remove the line upon false, whereas <code>assort()</code> will remove it upon true.
		 * 
		 * @param input
		 *     One line of the data array
		 */
		public Boolean test(String input);
	}
	
	/**
	 * Create a new Data instance.
	 * 
	 * @param lines
	 *     An array representing the lines of the data
	 */
	public Data(String[] lines) {
		mLines = lines;
	}
	
	/**
	 * This is used to determine whether or not to remove lines from the data array. Each line will be parsed to the custom <code>DataSorting</code> instance and then removed upon a true return.
	 * 
	 * @param DataSorting
	 *     An instance of the <code>DataSorting</code> interface which should determine whether or not to remove the line
	 *     
	 * @return
	 *     This instance
	 */
	public Data assort(DataSorting test) {
		if (size() > 0) {
			List<String> list = new ArrayList<String>();
			
			for (int i=0; i < mLines.length; i++) {
				if (!test.test(mLines[i])) {
					list.add(mLines[i]);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return this;
	}
	
	/**
	 * This is used to determine whether or not to remove lines from the data array. Each line will be compared to the argument. If the line contains anything from the argument, it will be removed from the data array.
	 * 
	 * @param contains
	 *     A string to locate within each line to determine whether or not to remove the line
	 *     
	 * @return
	 *     This instance
	 */
	public Data assort(final String contains) {
		return assort(new DataSorting() {
			public Boolean test(String input) {
				return input.contains( contains );
			}
		});
	}
	
	/**
	 * This is used to determine whether or not to keep lines in the data array. Each line will be parsed to the custom <code>DataSorting</code> instance and then removed upon a false return.
	 * 
	 * @param DataSorting
	 *     An instance of the <code>DataSorting</code> interface which should determine whether or not to keep the line
	 *     
	 * @return
	 *     This instance
	 */
	public Data sort(DataSorting test) {
		if (size() > 0) {
			List<String> list = new ArrayList<String>();
			
			for (int i=0; i < mLines.length; i++) {
				if (test.test(mLines[i])) {
					list.add(mLines[i]);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return this;
	}
	
	/**
	 * This is used to determine whether or not to keep lines in the data array. Each line will be compared to the argument. If the line contains anything from the argument, it will not be removed from the data array.
	 * 
	 * @param contains
	 *     A string to locate within each line to determine whether or not to remove the line
	 *     
	 * @return
	 *     This instance
	 */
	public Data sort(final String contains) {
		return sort(new DataSorting() {
			public Boolean test(String input) {
				return input.contains( contains );
			}
		});
	}
	
	/**
	 * @see Data#sort(Integer, Integer)
	 */
	public Data sort(Integer start) {
		return sort(start, mLines.length);
	}
	
	/**
	 * This is used to determine whether or not to keep lines in the data array. The method will keep each index within the <code>start</code> and <code>stop</code> indexes parsed via the arguments.
	 * <br />
	 * Note that the method will also except negative values. 
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 1:</span></dt>
	 * <dd>Remove the first and last index in the array</dd>
	 * <dd><code>sort(1, -1)</code></dd>
	 * </dl>
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 2:</span></dt>
	 * <dd>Only keep the first and last index in the array</dd>
	 * <dd><code>sort(-1, 1)</code></dd>
	 * </dl>
	 * 
	 * @param start
	 *     Where to start
	 *     
	 * @param stop
	 *     Where to stop
	 *     
	 * @return
	 *     This instance
	 */
	public Data sort(Integer start, Integer stop) {
		if (size() > 0) {
			List<String> list = new ArrayList<String>();
			Integer begin = start < 0 ? (mLines.length + start) : start;
			Integer end = stop < 0 ? (mLines.length + stop) : stop;
			
			Integer[] min = null, max = null;
			
			if (begin > end) {
				if (end == 0) {
					min = new Integer[]{ begin };
					max = new Integer[]{ mLines.length };
					
				} else {
					min = new Integer[]{ 0, begin };
					max = new Integer[]{ end, mLines.length };
				}
				
			} else {
				min = new Integer[]{ begin };
				max = new Integer[]{ end };
			}
			
			for (int i=0; i < min.length; i++) {
				for (int x=min[i]; x < max[i]; x++) {
					list.add(mLines[x]);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return this;
	}
	
	/**
	 * This is used to determine whether or not to remove lines from the data array. The method will remove each index within the <code>start</code> and <code>stop</code> indexes parsed via the arguments.
	 * <br />
	 * Note that the method will also except negative values. 
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 1:</span></dt>
	 * <dd>Remove the first and last index in the array</dd>
	 * <dd><code>sort(-1, 1)</code></dd>
	 * </dl>
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 2:</span></dt>
	 * <dd>Only keep the first and last index in the array</dd>
	 * <dd><code>sort(1, -1)</code></dd>
	 * </dl>
	 * 
	 * @param start
	 *     Where to start
	 *     
	 * @param stop
	 *     Where to stop
	 *     
	 * @return
	 *     This instance
	 */
	public Data assort(Integer start, Integer stop) {
		return sort(stop, start);
	}
	
	/**
	 * @see Data#assort(Integer, Integer)
	 */
	public Data assort(Integer start) {
		return assort(mLines.length, start);
	}
	
	/**
	 * This method will remove all of the empty lines from the data array
	 *     
	 * @return
	 *     This instance
	 */
	public Data trim() {
		if (size() > 0) {
			List<String> list = new ArrayList<String>();
			
			for (int i=0; i < mLines.length; i++) {
				if (mLines[i].trim().length() > 0) {
					list.add(mLines[i]);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return this;
	}
	
	/**
	 * This will return the data array
	 *     
	 * @return
	 *     The data array
	 */
	public String[] getArray() {
		return size() > 0 ? mLines : null;
	}
	
	/**
	 * This will return a string of the data array with added line breakers
	 *     
	 * @return
	 *     The data array as a string
	 */
	public String getString() {
		return getString("\n");
	}
	
	/**
	 * This will return a string of the data array with custom characters used as line breakers
	 * 
	 * @param start
	 *     A separator character used to separate each line
	 *     
	 * @return
	 *     The data array as a string
	 */
	public String getString(String separater) {
		return mLines == null ? null : TextUtils.join(separater, Arrays.asList(mLines));
	}
	
	/**  
	 * @return
	 *     The last non-empty line of the data array
	 */
	public String getLine() {
		return getLine(-1, true);
	}
	
	/**  
	 * @see Data#getLine(Integer, Boolean)
	 */
	public String getLine(Integer aLineNumber) {
		return getLine(aLineNumber, false);
	}
	
	/**
	 * This will return one specified line of the data array.
	 * <br />
	 * Note that this also takes negative number to get a line from the end and up
	 * 
	 * @param aLineNumber
	 *     The line number to return
	 *     
	 * @param aSkipEmpty
	 *     Whether or not to include empty lines
	 *     
	 * @return
	 *     The specified line
	 */
	public String getLine(Integer aLineNumber, Boolean aSkipEmpty) {
		if (size() > 0) {
			Integer count = aLineNumber < 0 ? (mLines.length + aLineNumber) : aLineNumber;
			
			while(count >= 0 && count < mLines.length) {
				if (!aSkipEmpty || mLines[count].trim().length() > 0) {
					return mLines[count].trim();
				}
				
				count = aLineNumber < 0 ? (count - 1) : (count + 1);
			}
		}
		
		return null;
	}
	
	/**  
	 * Count the lines in the data array
	 * 
	 * @return
	 *     The number of lines
	 */
	public Integer size() {
		return mLines == null ? 0 : mLines.length;
	}
}
