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
package integratedtoolkit.types.fake;

import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.resources.MethodResourceDescription;
import integratedtoolkit.types.resources.Resource;
import integratedtoolkit.types.resources.Worker;

public class FakeWorker extends Worker<MethodResourceDescription> {

    public FakeWorker(MethodResourceDescription description, int limitOfTasks) {
        super("a", description, new FakeNode(), limitOfTasks, null);
    }

    @Override
    public Resource.Type getType() {
        return Resource.Type.WORKER;
    }

    @Override
    public int compareTo(Resource rsrc) {
        return 0;
    }

    @Override
    public String getMonitoringData(String prefix) {
        return "";
    }

    @Override
    public boolean canRun(Implementation implementation) {
        return true;
    }

    @Override
    public Integer fitCount(Implementation impl) {
        return 10;
    }

    @Override
    public boolean hasAvailable(MethodResourceDescription consumption) {
        return true;
    }

    @Override
    public boolean usesGPU(MethodResourceDescription consumption) {
        LOGGER.debug("fake worker <T>");
        return false;
    }

    @Override
    public boolean usesFPGA(MethodResourceDescription consumption) {
        return false;
    }

    @Override
    public boolean usesOthers(MethodResourceDescription consumption) {
        return false;
    }

    @Override
    public MethodResourceDescription reserveResource(MethodResourceDescription consumption) {
        return consumption;
    }

    @Override
    public void releaseResource(MethodResourceDescription consumption) {

    }

    @Override
    public void releaseAllResources() {

    }

    @Override
    public FakeWorker getSchedulingCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
