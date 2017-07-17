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
import integratedtoolkit.util.ResourceManager;


/**
 * The DeleteIntermediateFilesRequest represents a request to delete the intermediate files of the execution from all
 * the worker nodes of the resource pool.
 */
public class PrintCurrentLoadRequest extends TDRequest {

    /**
     * Constructs a PrintCurrentLoadRequest
     *
     */
    public PrintCurrentLoadRequest() {
    }

    @Override
    public void process(TaskScheduler ts) throws ShutdownException {
        RESOURCES_LOGGER.info(ts.getWorkload().toString());
        ResourceManager.printResourcesState();
    }

    @Override
    public TDRequestType getType() {
        return TDRequestType.PRINT_CURRENT_GRAPH;
    }

}
