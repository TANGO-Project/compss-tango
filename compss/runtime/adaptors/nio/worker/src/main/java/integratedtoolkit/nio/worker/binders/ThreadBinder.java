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
package integratedtoolkit.nio.worker.binders;

import integratedtoolkit.nio.worker.exceptions.UnsufficientAvailableComputingUnitsException;


public interface ThreadBinder {

    /**
     * Binds @numCUs computing units of a @jobId
     * 
     * @param jobId
     * @param numCUs
     * @return
     * @throws UnsufficientAvailableComputingUnitsException
     */
    public int[] bindComputingUnits(int jobId, int numCUs) throws UnsufficientAvailableComputingUnitsException;

    /**
     * Releases the computing units previously requested by the job @jobId
     * 
     * @param jobId
     */
    public void releaseComputingUnits(int jobId);

}
