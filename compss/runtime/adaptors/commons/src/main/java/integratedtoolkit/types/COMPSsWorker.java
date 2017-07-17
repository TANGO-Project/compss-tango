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
package integratedtoolkit.types;

import integratedtoolkit.exceptions.AnnounceException;
import integratedtoolkit.types.resources.configuration.Configuration;


/**
 * Abstract definition of a COMPSs Worker
 *
 */
public abstract class COMPSsWorker extends COMPSsNode {

    /**
     * New worker with name @name and configuration @config
     * 
     * @param name
     * @param config
     */
    public COMPSsWorker(String name, Configuration config) {
        super();
    }

    /**
     * Returns the worker user
     * 
     * @return
     */
    public abstract String getUser();

    /**
     * Returns the worker classpath
     * 
     * @return
     */
    public abstract String getClasspath();

    /**
     * Returns the worker pythonpath
     * 
     * @return
     */
    public abstract String getPythonpath();

    /**
     * Updates the task count to @processorCoreCount
     * 
     * @param processorCoreCount
     */
    public abstract void updateTaskCount(int processorCoreCount);

    /**
     * Announces the worker destruction
     * 
     * @throws AnnounceException
     */
    public abstract void announceDestruction() throws AnnounceException;

    /**
     * Announces the worker creation
     * 
     * @throws AnnounceException
     */
    public abstract void announceCreation() throws AnnounceException;

}
