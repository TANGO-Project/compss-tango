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
package integratedtoolkit.types.request.td;

import integratedtoolkit.components.impl.TaskScheduler;
import integratedtoolkit.types.request.exceptions.ShutdownException;
import integratedtoolkit.util.CEIParser;
import integratedtoolkit.util.ResourceManager;

import java.util.concurrent.Semaphore;
import java.util.List;

public class UpdateLocalCEIRequest extends TDRequest {

    private final Class<?> ceiClass;
    private final Semaphore sem;

    public UpdateLocalCEIRequest(Class<?> ceiClass, Semaphore sem) {
        this.ceiClass = ceiClass;
        this.sem = sem;
    }

    /**
     * Returns the CoreElement Interface class
     *
     * @return
     */
    public Class<?> getCeiClass() {
        return this.ceiClass;
    }

    /**
     * Returns the semaphore where to synchronize until the operation is done
     *
     * @return Semaphore where to synchronize until the operation is done
     */
    public Semaphore getSemaphore() {
        return sem;
    }

    @Override
    public void process(TaskScheduler ts) throws ShutdownException {
        LOGGER.debug("Treating request to update core elements");

        // Load new coreElements
        List<Integer> newCores = CEIParser.loadJava(this.ceiClass);
        if (DEBUG) {
            LOGGER.debug("New methods: " + newCores);
        }
        // Update Resources structures
        ResourceManager.coreElementUpdates(newCores);
        // Update Scheduler structures
        ts.coreElementsUpdated();

        // Release
        LOGGER.debug("Data structures resized and CE-resources links updated");
        sem.release();
    }

    @Override
    public TDRequestType getType() {
        return TDRequestType.UPDATE_CEI_LOCAL;
    }

}
