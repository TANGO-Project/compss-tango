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
package integratedtoolkit.scheduler.multiobjective.types;

import integratedtoolkit.components.impl.ResourceScheduler;
import integratedtoolkit.scheduler.exceptions.BlockedActionException;
import integratedtoolkit.scheduler.exceptions.FailedActionException;
import integratedtoolkit.scheduler.exceptions.UnassignedActionException;
import integratedtoolkit.scheduler.multiobjective.MOSchedulingInformation;
import integratedtoolkit.scheduler.types.AllocatableAction;
import integratedtoolkit.scheduler.types.Score;
import integratedtoolkit.types.implementations.Implementation;

import integratedtoolkit.types.resources.Worker;
import integratedtoolkit.types.resources.WorkerResourceDescription;
import java.util.LinkedList;


public class OptimizationAction extends AllocatableAction {

    public OptimizationAction() {
        super(new MOSchedulingInformation(null), null);
        MOSchedulingInformation asi = (MOSchedulingInformation) this.getSchedulingInfo();
        asi.scheduled();
        asi.setOnOptimization(true);
        asi.setExpectedStart(0);
        asi.setExpectedEnd(Long.MAX_VALUE);
    }

    @Override
    protected void doAction() {

    }

    @Override
    protected void doCompleted() {

    }

    @Override
    protected void doError() throws FailedActionException {

    }

    @Override
    protected void doFailed() {

    }

    @Override
    public Integer getCoreId() {
        return null;
    }

    @Override
    public LinkedList<ResourceScheduler<? extends WorkerResourceDescription>> getCompatibleWorkers() {
        return null;
    }

    @Override
    public Implementation[] getImplementations() {
        return null;
    }

    @Override
    public <W extends WorkerResourceDescription> boolean isCompatible(Worker<W> r) {
        return true;
    }

    @Override
    public <T extends WorkerResourceDescription> LinkedList<Implementation> getCompatibleImplementations(ResourceScheduler<T> r) {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public <T extends WorkerResourceDescription> Score schedulingScore(ResourceScheduler<T> targetWorker, Score actionScore) {
        return null;
    }

    @Override
    public void schedule(Score actionScore) throws BlockedActionException, UnassignedActionException {

    }

    @Override
    public <T extends WorkerResourceDescription> void schedule(ResourceScheduler<T> targetWorker, Score actionScore)
            throws BlockedActionException, UnassignedActionException {

    }

    @Override
    public <T extends WorkerResourceDescription> void schedule(ResourceScheduler<T> targetWorker, Implementation impl)
            throws BlockedActionException, UnassignedActionException {

    }

    @Override
    public boolean isToReserveResources() {
        return true;
    }

    @Override
    public boolean isToReleaseResources() {
        return true;
    }

    @Override
    public String toString() {
        return "Scheduling blocking action";
    }
}
