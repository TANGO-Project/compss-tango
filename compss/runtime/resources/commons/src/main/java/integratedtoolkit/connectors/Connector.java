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
package integratedtoolkit.connectors;

import integratedtoolkit.types.ResourceCreationRequest;
import integratedtoolkit.types.resources.CloudMethodWorker;
import integratedtoolkit.types.resources.description.CloudMethodResourceDescription;


public interface Connector {

    /**
     * Starts a resource
     * 
     * @param name
     * @param rR
     * @return
     */
    public boolean turnON(String name, ResourceCreationRequest rR);

    /**
     * Sets the stop flag
     * 
     */
    public void stopReached();

    /**
     * Returns the expected creation time for next request
     * 
     * @return
     * @throws ConnectorException
     */
    public Long getNextCreationTime() throws ConnectorException;

    /**
     * Returns the time slot size
     * 
     * @return
     */
    public long getTimeSlot();

    /**
     * Terminates an specific machine
     * 
     * @param worker
     * @param reduction
     */
    public void terminate(CloudMethodWorker worker, CloudMethodResourceDescription reduction);

    /**
     * Terminates all instances
     * 
     */
    public void terminateAll();

}
