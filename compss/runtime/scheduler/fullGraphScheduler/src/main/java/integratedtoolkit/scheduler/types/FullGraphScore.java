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

import integratedtoolkit.scheduler.fullGraphScheduler.FullGraphSchedulingInformation;
import integratedtoolkit.scheduler.types.AllocatableAction;
import integratedtoolkit.scheduler.types.Profile;
import integratedtoolkit.scheduler.types.Score;
import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.resources.WorkerResourceDescription;

import java.util.LinkedList;


public class FullGraphScore<P extends Profile, T extends WorkerResourceDescription, I extends Implementation<T>> extends Score {

    /*
     * ActionScore -> task Priority expectedDataAvailable -> expected time when data dependencies will be ready (take
     * into account transfers) resourceScore -> Expected ResourceAvailability implementationScore -> ExecutionTime
     */
    private final double expectedDataAvailable;
    private double expectedStart;


    public FullGraphScore(double actionScore, double dataAvailability, double waiting, double res, double impl) {
        super(actionScore, res, waiting, impl);
        expectedDataAvailable = dataAvailability;
        expectedStart = Math.max(resourceScore, expectedDataAvailable);
    }

    public FullGraphScore(FullGraphScore<P, T, I> actionScore, double transferTime, double waiting, double resourceTime, double impl) {
        super(actionScore.getActionScore(), resourceTime, waiting, impl);
        expectedDataAvailable = actionScore.expectedDataAvailable + transferTime;
        expectedStart = Math.max(resourceScore, expectedDataAvailable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isBetter(Score other) {
        FullGraphScore<P, T, I> otherDS = (FullGraphScore<P, T, I>) other;
        if (this.actionScore != other.actionScore) {
            return this.actionScore > other.actionScore;
        }
        double ownEnd = this.expectedStart + this.implementationScore;
        double otherEnd = otherDS.expectedStart + other.implementationScore;
        return ownEnd < otherEnd;
    }

    public static <P extends Profile, T extends WorkerResourceDescription, I extends Implementation<T>> long getActionScore(
            AllocatableAction<P, T, I> action) {
        return action.getPriority();
    }

    public long getDataPredecessorTime(LinkedList<AllocatableAction<P, T, I>> predecessors) {
        long dataTime = 0;
        for (AllocatableAction<P, T, I> pred : predecessors) {
            dataTime = Math.max(dataTime, ((FullGraphSchedulingInformation<P, T, I>) pred.getSchedulingInfo()).getExpectedEnd());
        }
        return dataTime;
    }

    public double getExpectedDataAvailable() {
        return this.expectedDataAvailable;
    }

    public double getExpectedStart() {
        return this.expectedStart;
    }

    @Override
    public String toString() {
        return "[FGScore = [action: " + this.actionScore + ", availableData: " + this.expectedDataAvailable + ", resource: "
                + this.resourceScore + ", expectedStart: " + this.expectedStart + ", implementation:" + this.implementationScore + "]";
    }

}
