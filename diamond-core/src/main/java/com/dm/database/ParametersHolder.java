package com.dm.database;

import java.util.ArrayList;

/**
 *
 * @author zy
 *  parameters holder interface
 */
public interface ParametersHolder {

    /**
     * Get parameters array list [ ].
     *
     * @return the array list [ ]
     */
    ArrayList<Object>[] getParameters();
}