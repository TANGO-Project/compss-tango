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
package integratedtoolkit.types.request.ap;

import integratedtoolkit.components.impl.AccessProcessor;
import integratedtoolkit.components.impl.DataInfoProvider;
import integratedtoolkit.components.impl.TaskAnalyser;
import integratedtoolkit.components.impl.TaskDispatcher;

import java.util.concurrent.Semaphore;

public class BarrierRequest extends APRequest {

    private Semaphore sem;
    private Long appId;

    public BarrierRequest(Long appId, Semaphore sem) {
        this.appId = appId;
        this.sem = sem;
    }

    public Long getAppId() {
        return this.appId;
    }

    public Semaphore getSemaphore() {
        return this.sem;
    }

    public void setSemaphore(Semaphore sem) {
        this.sem = sem;
    }

    @Override
    public void process(AccessProcessor ap, TaskAnalyser ta, DataInfoProvider dip, TaskDispatcher td) {
        ta.barrier(this);
    }

    @Override
    public APRequestType getRequestType() {
        return APRequestType.WAIT_FOR_ALL_TASKS;
    }

}
