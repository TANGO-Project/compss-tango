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
package integratedtoolkit.types.job;

import integratedtoolkit.types.allocatableactions.ExecutionAction;

public class JobStatusListener implements JobListener {

    private final ExecutionAction execution;

    public JobStatusListener(ExecutionAction ex) {
        this.execution = ex;
    }

    @Override
    public void jobCompleted(Job<?> job) {
        // Mark execution as completed
        execution.completedJob(job);
    }

    @Override
    public void jobFailed(Job<?> job, JobEndStatus endStatus) {
        // Mark execution as failed
        execution.failedJob(job, endStatus);
    }

}
