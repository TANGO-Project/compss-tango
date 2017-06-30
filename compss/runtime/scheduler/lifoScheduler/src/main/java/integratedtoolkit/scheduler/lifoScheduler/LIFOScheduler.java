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
package integratedtoolkit.scheduler.lifoScheduler;

import java.util.LinkedList;

import integratedtoolkit.components.impl.ResourceScheduler;
import integratedtoolkit.scheduler.readyScheduler.ReadyScheduler;
import integratedtoolkit.scheduler.types.AllocatableAction;
import integratedtoolkit.scheduler.types.Score;
import integratedtoolkit.types.resources.Worker;
import integratedtoolkit.types.resources.WorkerResourceDescription;
import org.json.JSONObject;


/**
 * Representation of a Scheduler that considers only ready tasks and sorts them in LIFO mode
 *
 */
public class LIFOScheduler extends ReadyScheduler {

    /**
     * Constructs a new Ready Scheduler instance
     *
     */
    public LIFOScheduler() {
        super();
    }

    /*
     * *********************************************************************************************************
     * *********************************************************************************************************
     * ***************************** UPDATE STRUCTURES OPERATIONS **********************************************
     * *********************************************************************************************************
     * *********************************************************************************************************
     */
    @Override
    public <T extends WorkerResourceDescription> LIFOResourceScheduler<T> generateSchedulerForResource(Worker<T> w, JSONObject json) {
        // LOGGER.info("[LIFOScheduler] Generate scheduler for resource " + w.getName());
        return new LIFOResourceScheduler<>(w, json);
    }

    @Override
    public Score generateActionScore(AllocatableAction action) {
        // LOGGER.info("[LIFOScheduler] Generate Action Score for " + action);
        return new Score(action.getPriority(), action.getId(), 0, 0);
    }

    /*
     * *********************************************************************************************************
     * *********************************************************************************************************
     * ********************************* SCHEDULING OPERATIONS *************************************************
     * *********************************************************************************************************
     * *********************************************************************************************************
     */
    @Override
    public <T extends WorkerResourceDescription> void purgeFreeActions(LinkedList<AllocatableAction> dataFreeActions,
            LinkedList<AllocatableAction> resourceFreeActions, LinkedList<AllocatableAction> blockedCandidates,
            ResourceScheduler<T> resource) {

        LinkedList<AllocatableAction> unassignedReadyActions = this.unassignedReadyActions.getAllActions();
        this.unassignedReadyActions.removeAllActions();
        dataFreeActions.addAll(unassignedReadyActions);

    }

}
