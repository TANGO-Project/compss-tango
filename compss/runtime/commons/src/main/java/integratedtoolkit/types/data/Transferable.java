/*
 *  Copyright 2.1.rc17062-2.1.rc17067 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.1.rc1706 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.1.rc1706
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package integratedtoolkit.types.data;

import integratedtoolkit.types.annotations.parameter.DataType;


public interface Transferable {

    /**
     * Returns the source data
     * 
     * @return
     */
    public Object getDataSource();

    /**
     * Sets the source data
     * 
     * @param dataSource
     */
    public void setDataSource(Object dataSource);

    /**
     * Returns the target data
     * 
     * @return
     */
    public String getDataTarget();

    /**
     * Sets the target data
     * 
     * @param target
     */
    public void setDataTarget(String target);

    /**
     * Returns the data Transfer type
     * 
     * @return
     */
    public DataType getType();

}
