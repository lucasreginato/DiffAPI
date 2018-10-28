package com.assignment.utils;

/**
 * The enumeration that contains the RIGHT and LEFT properties.<br/>
 * These values facilitates to understand the identify the RIGHT and LEFT values.<br/>
 * 
 * @author lucas.reginato@gmail.com
 */
public enum DataEnum {
	
	RIGHT,
	LEFT;
	
	/**
	 * Returns the name of the enumeration value.
	 * 
	 * @return A String that represents the name of the enumeration value.
	 */
    public String value() {
        return name();
    }

    /**
     * Returns the DataEnum value from a String value.
     * 
     * @param v The String object that represents the DataEnum value.
     * @return The DataEnum value.
     */
    public static DataEnum fromValue(String v) {
        return valueOf(v);
    }
}
