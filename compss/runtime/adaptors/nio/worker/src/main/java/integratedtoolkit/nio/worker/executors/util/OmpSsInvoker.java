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
package integratedtoolkit.nio.worker.executors.util;

import java.io.File;

import integratedtoolkit.exceptions.InvokeExecutionException;
import integratedtoolkit.nio.NIOTask;
import integratedtoolkit.nio.exceptions.JobExecutionException;
import integratedtoolkit.nio.worker.NIOWorker;

import integratedtoolkit.types.implementations.OmpSsImplementation;

import integratedtoolkit.worker.invokers.GenericInvoker;


public class OmpSsInvoker extends Invoker {

    private final String ompssBinary;


    public OmpSsInvoker(NIOWorker nw, NIOTask nt, File taskSandboxWorkingDir, int[] assignedCoreUnits) throws JobExecutionException {
        super(nw, nt, taskSandboxWorkingDir, assignedCoreUnits);

        // Get method definition properties
        OmpSsImplementation ompssImpl = null;
        try {
            ompssImpl = (OmpSsImplementation) this.impl;
        } catch (Exception e) {
            throw new JobExecutionException(ERROR_METHOD_DEFINITION + this.methodType, e);
        }
        this.ompssBinary = ompssImpl.getBinary();
    }

    @Override
    public Object invokeMethod() throws JobExecutionException {
        logger.info("Invoked " + ompssBinary + " in " + nw.getHostName());
        try {
            return GenericInvoker.invokeOmpSsMethod(this.ompssBinary, this.values, this.hasReturn, this.streams, this.prefixes,
                    this.taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            throw new JobExecutionException(iee);
        }
    }

}
