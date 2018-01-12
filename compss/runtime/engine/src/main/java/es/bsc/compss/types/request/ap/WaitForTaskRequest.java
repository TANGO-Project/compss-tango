/*         
 *  Copyright 2002.2.rc1710017 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


package es.bsc.compss.types.request.ap;

import es.bsc.compss.components.impl.AccessProcessor;
import es.bsc.compss.components.impl.DataInfoProvider;
import es.bsc.compss.components.impl.TaskAnalyser;
import es.bsc.compss.components.impl.TaskDispatcher;
import es.bsc.compss.types.data.AccessParams.AccessMode;
import java.util.concurrent.Semaphore;

public class WaitForTaskRequest extends APRequest {

    private int dataId;
    private final AccessMode am;
    private Semaphore sem;

    public WaitForTaskRequest(int dataId, AccessMode mode, Semaphore sem) {
        this.dataId = dataId;
        this.am = mode;
        this.sem = sem;
    }

    public Semaphore getSemaphore() {
        return sem;
    }

    public void setSemaphore(Semaphore sem) {
        this.sem = sem;
    }

    public int getDataId() {
        return dataId;
    }

    public AccessMode getAccessMode() {
        return am;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    @Override
    public void process(AccessProcessor ap, TaskAnalyser ta, DataInfoProvider dip, TaskDispatcher td) {
        ta.findWaitedTask(this);
    }

    @Override
    public APRequestType getRequestType() {
        return APRequestType.WAIT_FOR_TASK;
    }

}
