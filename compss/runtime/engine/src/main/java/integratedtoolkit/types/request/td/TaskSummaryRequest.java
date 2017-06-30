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

import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.Logger;

/**
 * The MonitoringDataRequest class represents a request to obtain the current
 * resources and cores that can be run
 */
public class TaskSummaryRequest extends TDRequest {

    /**
     * Semaphore where to synchronize until the operation is done
     */
    private Semaphore sem;

    /**
     * Logger where to print information
     */
    private final Logger logger;

    /**
     * Constructs a new TaskStateRequest
     *
     * @param logger
     * @param sem semaphore where to synchronize until the current state is
     * described
     */
    public TaskSummaryRequest(Logger logger, Semaphore sem) {
        this.logger = logger;
        this.sem = sem;
    }

    /**
     * Returns the semaphore where to synchronize until the current state is
     * described
     *
     * @return the semaphore where to synchronize until the current state is
     * described
     */
    public Semaphore getSemaphore() {
        return sem;
    }

    /**
     * Sets the semaphore where to synchronize until the current state is
     * described
     *
     * @param sem the semaphore where to synchronize until the current state is
     * described
     */
    public void setSemaphore(Semaphore sem) {
        this.sem = sem;
    }

    @Override
    public void process(TaskScheduler ts) throws ShutdownException {
        ts.getTaskSummary(logger);
        sem.release();
    }

    @Override
    public TDRequestType getType() {
        return TDRequestType.MONITORING_DATA;
    }

}
