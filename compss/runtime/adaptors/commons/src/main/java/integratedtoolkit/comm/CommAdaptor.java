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
package integratedtoolkit.comm;

import java.util.List;

import integratedtoolkit.exceptions.ConstructConfigurationException;
import integratedtoolkit.types.COMPSsWorker;
import integratedtoolkit.types.data.operation.DataOperation;
import integratedtoolkit.types.resources.configuration.Configuration;
import integratedtoolkit.types.uri.MultiURI;


/**
 * Abstract definition of a Communication Adaptor for the Runtime
 *
 */
public interface CommAdaptor {

    /**
     * Initializes the Communication Adaptor
     */
    public void init();

    /**
     * Creates a configuration instance for the specific adaptor
     * 
     * @param project_properties
     * @param resources_properties
     * @return
     * @throws Exception
     */
    public Configuration constructConfiguration(Object project_properties, Object resources_properties)
            throws ConstructConfigurationException;

    /**
     * Initializes a worker through an adaptor
     * 
     * @param workerName
     * @param config
     * @return
     */
    public COMPSsWorker initWorker(String workerName, Configuration config);

    /**
     * Stops the Communication Adaptor
     */
    public void stop();

    /**
     * Retrieves all the pending operations
     * 
     * @return
     */
    public List<DataOperation> getPending();

    /**
     * Returns the complete Master URI
     * 
     * @param u
     */
    public void completeMasterURI(MultiURI u);

    /**
     * Stops all the pending jobs inside the Communication Adaptor
     */
    public void stopSubmittedJobs();

}
