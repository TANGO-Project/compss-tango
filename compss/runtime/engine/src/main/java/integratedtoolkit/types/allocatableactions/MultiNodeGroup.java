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
package integratedtoolkit.types.allocatableactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import integratedtoolkit.log.Loggers;

/**
 * Represents a group of Execution Actions that allow the multi-node execution
 *
 */
public class MultiNodeGroup {

    public static final int MASTER_GROUP_ID = 1;
    public static final int UNASSIGNED_ID = -1;

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);

    private final int groupSize;
    private int nextProcessId;

    private final HashMap<Integer, MultiNodeExecutionAction> registeredSlaves;
    private MultiNodeExecutionAction registeredMaster;

    /**
     * Creates a new group of @groupSize size
     *
     * @param groupSize
     */
    public MultiNodeGroup(int groupSize) {
        LOGGER.debug("[MultiNodeGroup] Creating new group of size " + groupSize);
        this.groupSize = groupSize;
        this.nextProcessId = groupSize;

        this.registeredSlaves = new HashMap<>();
        this.registeredMaster = null;
    }

    /**
     * Registers a new process into the group and returns its assigned process
     * Id
     *
     * @param action
     * @return
     */
    public int registerProcess(MultiNodeExecutionAction action) {
        int actionId = this.nextProcessId--;
        if (actionId == MASTER_GROUP_ID) {
            // Register process as master
            LOGGER.debug("[MultiNodeGroup] Register action " + action.getId() + " as master of group " + this);
            registeredMaster = action;
        } else {
            // Register process as slave
            LOGGER.debug("[MultiNodeGroup] Register action " + action.getId() + " as slave of group " + this);
            this.registeredSlaves.put(actionId, action);
        }

        return actionId;
    }

    /**
     * Returns the group size
     *
     * @return
     */
    public int getGroupSize() {
        return this.groupSize;
    }

    /**
     * Returns the action associated to the master of the group
     *
     * @return
     */
    public MultiNodeExecutionAction getMasterAction() {
        return this.registeredMaster;
    }

    /**
     * Returns the resources ' names of all the slaves registered into the group
     *
     * @return
     */
    public List<String> getSlavesNames() {
        List<String> slavesNames = new ArrayList<>();
        for (Entry<Integer, MultiNodeExecutionAction> slave : registeredSlaves.entrySet()) {
            slavesNames.add(slave.getValue().getAssignedResource().getName());
        }

        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[MultiNodeGroup] SlaveNames of group ").append(this).append(" are:");
            for (String slaveName : slavesNames) {
                sb.append(" ").append(slaveName);
            }
            LOGGER.debug(sb.toString());
        }

        return slavesNames;
    }

    /**
     * Triggers an action completion to all the slaves registered into the group
     *
     */
    public void actionCompletion() {
        LOGGER.debug("[MultiNodeGroup] Notify action completion to all slaves of group " + this);
        for (Entry<Integer, MultiNodeExecutionAction> entry : registeredSlaves.entrySet()) {
            entry.getValue().notifyCompleted();
        }
    }

    /**
     * Triggers an action failure to all the slaves registered into the group
     *
     */
    public void actionError() {
        LOGGER.debug("[MultiNodeGroup] Notify action error to all slaves of group " + this);
        for (Entry<Integer, MultiNodeExecutionAction> entry : registeredSlaves.entrySet()) {
            entry.getValue().notifyError();
        }
    }

    @Override
    public String toString() {
        return "MultiNodeGroup@" + this.hashCode();
    }

}
