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

import integratedtoolkit.types.implementations.MPIImplementation;

import integratedtoolkit.worker.invokers.GenericInvoker;


public class MPIInvoker extends Invoker {

    private static final String ERROR_MPI_RUNNER = "ERROR: Invalid mpiRunner";
    private static final String ERROR_MPI_BINARY = "ERROR: Invalid mpiBinary";
    private static final String ERROR_TARGET_PARAM = "ERROR: MPI Execution doesn't support target parameters";

    private final String mpiRunner;
    private final String mpiBinary;


    public MPIInvoker(NIOWorker nw, NIOTask nt, File taskSandboxWorkingDir, int[] assignedCoreUnits) throws JobExecutionException {
        super(nw, nt, taskSandboxWorkingDir, assignedCoreUnits);

        // Get method definition properties
        MPIImplementation mpiImpl = null;
        try {
            mpiImpl = (MPIImplementation) this.impl;
        } catch (Exception e) {
            throw new JobExecutionException(ERROR_METHOD_DEFINITION + this.methodType, e);
        }
        this.mpiRunner = mpiImpl.getMpiRunner();
        this.mpiBinary = mpiImpl.getBinary();
    }

    @Override
    public Object invokeMethod() throws JobExecutionException {
        checkArguments();
        return invokeMPIMethod();
    }

    private void checkArguments() throws JobExecutionException {
        if (this.mpiRunner == null || this.mpiRunner.isEmpty()) {
            throw new JobExecutionException(ERROR_MPI_RUNNER);
        }
        if (this.mpiBinary == null || this.mpiBinary.isEmpty()) {
            throw new JobExecutionException(ERROR_MPI_BINARY);
        }
        if (this.target.getValue() != null) {
            throw new JobExecutionException(ERROR_TARGET_PARAM);
        }
    }

    private Object invokeMPIMethod() throws JobExecutionException {
        logger.info("Invoked " + this.mpiBinary + " in " + this.nw.getHostName());
        try {
            return GenericInvoker.invokeMPIMethod(this.mpiRunner, this.mpiBinary, this.values, this.hasReturn, this.streams, this.prefixes,
                    this.taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            throw new JobExecutionException(iee);
        }
    }

}
