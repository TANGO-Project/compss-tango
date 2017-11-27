/*         
 *  Copyright 2002.2.rc1710017 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package es.bsc.compss.nio.worker.util;

import java.util.LinkedList;
import java.util.List;

import es.bsc.compss.types.annotations.parameter.DataType;


/**
 * Stores the task status information for bindings
 *
 */
public class ExternalTaskStatus {

    private final Integer exitValue;
    private final List<DataType> updatedParameterTypes;
    private final List<String> updatedParameterValues;


    /**
     * Creates a new task status instance with exitValue
     * 
     * @param exitValue
     */
    public ExternalTaskStatus(Integer exitValue) {
        this.exitValue = exitValue;
        this.updatedParameterTypes = new LinkedList<>();
        this.updatedParameterValues = new LinkedList<>();
    }

    /**
     * Returns the exitValue of the task (null if it has not ended yet)
     * 
     * @return
     */
    public Integer getExitValue() {
        return this.exitValue;
    }

    /**
     * Returns the number of parameters of the task
     * 
     * @return
     */
    public int getNumParameters() {
        return this.updatedParameterValues.size();
    }

    /**
     * Returns all the parameters' types
     * 
     * @return
     */
    public List<DataType> getParameterTypes() {
        return this.updatedParameterTypes;
    }

    /**
     * Returns all the parameters' values
     * 
     * @return
     */
    public List<String> getParameterValues() {
        return this.updatedParameterValues;
    }

    /**
     * Returns the i-th parameter type. Null if i is out of the parameters range
     * 
     * @param i
     * @return
     */
    public DataType getParameterType(int i) {
        if (i >= 0 && i < this.updatedParameterTypes.size()) {
            return this.updatedParameterTypes.get(i);
        }
        return null;
    }

    /**
     * Returns the i-th parameter value. Null if i is out of the parameters range
     * 
     * @param i
     * @return
     */
    public String getParameterValue(int i) {
        if (i >= 0 && i < this.updatedParameterValues.size()) {
            return this.updatedParameterValues.get(i);
        }
        return null;
    }

    /**
     * Adds a new parameter
     * 
     * @param obj
     */
    public void addParameter(DataType type, String value) {
        this.updatedParameterTypes.add(type);
        this.updatedParameterValues.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExternalTaskStatus [ ");

        sb.append("ExitValue = ").append(this.exitValue).append(", ");
        sb.append("NumParameters = ").append(getNumParameters()).append(", ");
        sb.append("ParameterTypes = [");
        for (DataType type : this.updatedParameterTypes) {
            sb.append(type.ordinal()).append(" ");
        }
        sb.append("], ");
        sb.append("ParameterValues = [");
        for (String value : this.updatedParameterValues) {
            sb.append(value).append(" ");
        }
        sb.append("]");

        sb.append(" ]");

        return sb.toString();
    }

}
