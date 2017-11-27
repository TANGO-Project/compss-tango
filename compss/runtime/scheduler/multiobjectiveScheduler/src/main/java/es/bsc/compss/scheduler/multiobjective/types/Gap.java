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

import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.types.resources.ResourceDescription;


public class Gap {

    private final long initialTime;
    private long endTime;
    private final AllocatableAction origin;
    private final ResourceDescription resources;
    private final int capacity;


    public Gap(long start, long endTime, AllocatableAction origin, ResourceDescription resources, int capacity) {
        this.initialTime = start;
        this.endTime = endTime;
        this.origin = origin;
        this.resources = resources;
        this.capacity = capacity;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public AllocatableAction getOrigin() {
        return origin;
    }

    public ResourceDescription getResources() {
        return resources;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "<" + initialTime + "->" + endTime + ", " + origin + ", " + resources.getDynamicDescription() + ", with " + capacity
                + " slots >";
    }
}
