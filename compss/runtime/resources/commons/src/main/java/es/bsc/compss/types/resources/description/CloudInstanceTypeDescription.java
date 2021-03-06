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


package es.bsc.compss.types.resources.description;

import es.bsc.compss.types.implementations.Implementation;
import es.bsc.compss.types.resources.MethodResourceDescription;
import es.bsc.compss.util.CoreManager;
import java.util.List;

public class CloudInstanceTypeDescription {

    private final String instanceTypeName;
    private final MethodResourceDescription rd;
    private int[] slotsCore;
    private int[][] slotsImpl;

    public CloudInstanceTypeDescription(String name, MethodResourceDescription rd) {
        this.instanceTypeName = name;
        this.rd = rd;

        int coreCount = CoreManager.getCoreCount();
        this.slotsCore = new int[coreCount];
        this.slotsImpl = new int[coreCount][];

        // Get new values
        for (int coreId = 0; coreId < coreCount; coreId++) {
            List<Implementation> impls = CoreManager.getCoreImplementations(coreId);
            int implsSize = impls.size();
            this.slotsImpl[coreId] = new int[implsSize];
            for (int implId = 0; implId < implsSize; ++implId) {
                Implementation impl = impls.get(implId);
                if (impl.getTaskType() == Implementation.TaskType.METHOD) {
                    MethodResourceDescription reqs = (MethodResourceDescription) impl.getRequirements();
                    Integer into = rd.canHostSimultaneously(reqs);
                    this.slotsCore[coreId] = Math.max(this.slotsCore[coreId], into);
                    this.slotsImpl[coreId][implId] = into;
                }
            }
        }
    }

    public String getName() {
        return this.instanceTypeName;
    }

    public MethodResourceDescription getResourceDescription() {
        return this.rd;
    }

    public int[] getSlotsCore() {
        return this.slotsCore;
    }

    public int getSpecificSlotsCore(int index) {
        return this.slotsCore[index];
    }

    public void setSlotsCore(int[] slotsCore) {
        this.slotsCore = slotsCore;
    }

    public int[][] getSlotsImpl() {
        return this.slotsImpl;
    }

    public int getSlotsImplLength() {
        return this.slotsImpl.length;
    }

    public int[] getSpecificSlotsImpl(int coreId) {
        return this.slotsImpl[coreId];
    }

    public int getSpecificSlotsImpl(int coreId, int implId) {
        return this.slotsImpl[coreId][implId];
    }

    public void setSlotsImpl(int[][] slotsImpl) {
        this.slotsImpl = slotsImpl;
    }

}
