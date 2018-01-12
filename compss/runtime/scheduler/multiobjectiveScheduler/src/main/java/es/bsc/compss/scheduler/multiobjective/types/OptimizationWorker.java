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


package es.bsc.compss.scheduler.multiobjective.types;

import es.bsc.compss.scheduler.multiobjective.MOResourceScheduler;
import es.bsc.compss.scheduler.multiobjective.MOScheduleOptimizer;
import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.types.resources.WorkerResourceDescription;
import java.util.PriorityQueue;


public class OptimizationWorker {

    private MOResourceScheduler<WorkerResourceDescription> resource;
    private PriorityQueue<AllocatableAction> donorActions;


    public OptimizationWorker(MOResourceScheduler<WorkerResourceDescription> resource) {
        this.resource = resource;
    }

    public void localOptimization(long optimizationTS) {
        donorActions = resource.localOptimization(optimizationTS, MOScheduleOptimizer.getSelectionComparator(),
                MOScheduleOptimizer.getDonationComparator());
    }

    public AllocatableAction pollDonorAction() {
        return donorActions.poll();
    }

    public long getDonationIndicator() {
        return resource.getLastGapExpectedStart();
    }

    public String getName() {
        return resource.getName();
    }

    public MOResourceScheduler<WorkerResourceDescription> getResource() {
        return resource;
    }

}
