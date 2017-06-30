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
package integratedtoolkit.types.request.td;

import integratedtoolkit.components.impl.TaskScheduler;
import integratedtoolkit.scheduler.types.AllocatableAction;
import integratedtoolkit.types.request.exceptions.ShutdownException;

/**
 * The ActionUpdate class represents the notification of an update on the state
 * of an allocatable action.
 */
public class ActionUpdate extends TDRequest {

    /**
     * The updated allocatable action
     */
    private final AllocatableAction action;

    /**
     * Possible Updates applied to the action.
     */
    public static enum Update {

        /**
         * There has been an error during the execution of the action.
         */
        ERROR,
        /**
         * The action execution has succedded.
         */
        COMPLETED
    }

    /**
     * Update to be notified.
     */
    private final Update update;

    /**
     * Constructs a new NotifyAllocatableActionEnd for the task
     *
     * @param action
     * @param update update to be notified
     */
    public ActionUpdate(AllocatableAction action, Update update) {
        this.action = action;
        this.update = update;
    }

    @Override
    public TDRequestType getType() {
        return TDRequestType.ACTION_UPDATE;
    }

    @Override
    public void process(TaskScheduler ts) throws ShutdownException {
        if (update == Update.COMPLETED) {
            ts.actionCompleted(action);
        } else {
            ts.errorOnAction(action);
        }
    }

}
