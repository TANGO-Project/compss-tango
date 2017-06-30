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
package integratedtoolkit.nio.worker.util;

import java.io.File;
import java.util.Map;

import integratedtoolkit.ITConstants;
import integratedtoolkit.nio.worker.NIOWorker;
import integratedtoolkit.nio.worker.exceptions.InitializationException;
import integratedtoolkit.nio.worker.executors.ExternalExecutor;
import integratedtoolkit.nio.worker.executors.PythonExecutor;
import integratedtoolkit.util.ErrorManager;
import integratedtoolkit.nio.NIOTracer;



public class PythonThreadPool extends ExternalThreadPool {

    private static final String WORKER_PY_RELATIVE_PATH = File.separator + "pycompss" + File.separator + "worker" + File.separator
            + "piper_worker.py";

    public PythonThreadPool(NIOWorker nw, int size) {
        super(nw, size);
    }

    /**
     * Starts the threads of the pool
     * 
     */
    public void startThreads() throws InitializationException {
        logger.info("Start threads of ThreadPool");
        int i = 0;
        for (Thread t : workerThreads) {
            PythonExecutor executor = new PythonExecutor(nw, this, queue, writePipeFiles[i], taskResultReader[i]);
            t = new Thread(executor);
            t.setName(JOB_THREADS_POOL_NAME + " pool thread # " + i);
            t.start();
            i = i + 1;
        }

        sem.acquireUninterruptibly(this.size);
    }

    @Override
    public String getLaunchCommand() {
        // Specific launch command is of the form: binding bindingExecutor bindingArgs
        // The bindingArgs are of the form python -u piper_worker.py debug tracing storageConf #threads cmdPipes resultPipes

        StringBuilder cmd = new StringBuilder();

        cmd.append(ITConstants.Lang.PYTHON).append(ExternalExecutor.TOKEN_SEP);
        cmd.append(NIOWorker.isTracingEnabled()).append(ExternalExecutor.TOKEN_SEP);

        cmd.append("python").append(ExternalExecutor.TOKEN_SEP).append("-u").append(ExternalExecutor.TOKEN_SEP);
        cmd.append(installDir).append(PythonExecutor.PYCOMPSS_RELATIVE_PATH).append(WORKER_PY_RELATIVE_PATH)
                .append(ExternalExecutor.TOKEN_SEP);

        cmd.append(NIOWorker.isWorkerDebugEnabled()).append(ExternalExecutor.TOKEN_SEP);
        cmd.append(NIOWorker.isTracingEnabled()).append(ExternalExecutor.TOKEN_SEP);
        cmd.append(NIOWorker.getStorageConf()).append(ExternalExecutor.TOKEN_SEP);
        cmd.append(size).append(ExternalExecutor.TOKEN_SEP);

        for (int i = 0; i < writePipeFiles.length; ++i) {
            cmd.append(writePipeFiles[i]).append(ExternalExecutor.TOKEN_SEP);
        }

        for (int i = 0; i < readPipeFiles.length; ++i) {
            cmd.append(readPipeFiles[i]).append(ExternalExecutor.TOKEN_SEP);
        }

        return cmd.toString();
    }

    @Override
    public Map<String, String> getEnvironment(NIOWorker nw) {
        return PythonExecutor.getEnvironment(nw);
    }

    @Override
    protected String getPBWorkingDir(){
        String workingDir = nw.getWorkingDir();
        if (NIOTracer.isActivated()){
            workingDir += "python";
            if(! new File(workingDir).mkdirs()){
                ErrorManager.error("Could not create working dir for python tracefiles, path: " + workingDir);
            }
        }
        return workingDir;
    }
}
