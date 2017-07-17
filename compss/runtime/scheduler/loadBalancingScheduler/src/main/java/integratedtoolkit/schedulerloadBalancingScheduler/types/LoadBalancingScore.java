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
package integratedtoolkit.schedulerloadBalancingScheduler.types;

import integratedtoolkit.scheduler.types.Score;

public class LoadBalancingScore extends Score {

    /**
     * Creates a new ResourceEmptyScore with the given values
     *
     * @param actionScore
     * @param waiting
     * @param res
     * @param impl
     */
    public LoadBalancingScore(long actionScore, long res, long waiting, long impl) {
        super(actionScore, res, waiting, impl);
    }

    /**
     * Creates a copy of the @clone ResourceEmptyScore
     *
     * @param clone
     */
    public LoadBalancingScore(LoadBalancingScore clone) {
        super(clone);
    }

    @Override
    public boolean isBetter(Score reference) {
        LoadBalancingScore other = (LoadBalancingScore) reference;
        if (this.actionScore != other.actionScore) {
            return this.actionScore > other.actionScore;
        }
        if (this.resourceScore != other.resourceScore) {
            return this.resourceScore > other.resourceScore;
        }
        if (this.implementationScore != other.implementationScore) {
            return this.implementationScore > other.implementationScore;
        }
        return this.waitingScore > other.waitingScore;
    }

    @Override
    public String toString() {
        return "[LoadBalancingScore = [action:" + actionScore + ", resource:" + resourceScore + ", load:" + waitingScore
                + ", implementation:" + implementationScore + "]" + "]";
    }

}
