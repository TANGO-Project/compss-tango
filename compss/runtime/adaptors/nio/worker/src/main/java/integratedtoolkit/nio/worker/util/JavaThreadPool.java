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

import integratedtoolkit.nio.worker.NIOWorker;
import integratedtoolkit.nio.worker.exceptions.InitializationException;
import integratedtoolkit.nio.worker.executors.JavaExecutor;


public class JavaThreadPool extends JobsThreadPool {

    public JavaThreadPool(NIOWorker nw, int size) {
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
            JavaExecutor executor = new JavaExecutor(nw, this, queue);
            t = new Thread(executor);
            t.setName(JOB_THREADS_POOL_NAME + " pool thread # " + i++);
            t.start();
        }

        sem.acquireUninterruptibly(this.size);
    }

    /**
     * Stops specific language components
     * 
     */
    protected void specificStop() {
        // Nothing to do
    }

}
