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
package integratedtoolkit.scheduler.types;

import integratedtoolkit.scheduler.types.AllocatableAction;
import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.resources.ResourceDescription;
import integratedtoolkit.types.resources.WorkerResourceDescription;


public class Gap<P extends Profile, T extends WorkerResourceDescription, I extends Implementation<T>> {

    private final long initialTime;
    private long endTime;
    private final AllocatableAction<P, T, I> origin;
    private final ResourceDescription resources;
    private final int capacity;


    public Gap(long start, AllocatableAction<P, T, I> origin, ResourceDescription resources, int capacity) {
        this.initialTime = start;
        this.origin = origin;
        this.resources = resources.copy();
        this.capacity = capacity;
    }

    public Gap(long start, long endTime, AllocatableAction<P, T, I> origin, ResourceDescription resources, int capacity) {
        this.initialTime = start;
        this.endTime = endTime;
        this.origin = origin;
        this.resources = resources.copy();
        this.capacity = capacity;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public AllocatableAction<P, T, I> getOrigin() {
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
        return "<" + initialTime + "->" + endTime + ", " + origin + ", " + resources + ", amb " + capacity + " slots >";
    }

}
