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

package es.bsc.compss.types;

import org.apache.logging.log4j.Logger;

import es.bsc.compss.types.resources.description.CloudInstanceTypeDescription;
import es.bsc.compss.types.resources.description.CloudMethodResourceDescription;
import es.bsc.compss.util.CoreManager;

public class ResourceCreationRequest {

    private final CloudProvider provider;
    private final CloudMethodResourceDescription requested;
    private int[][] requestedSimultaneousTaskCount;
    private final long requestedTime;

    public ResourceCreationRequest(CloudMethodResourceDescription requestedResource, int[][] simultaneousTasks, CloudProvider cp) {
        requested = requestedResource;
        this.provider = cp;
        requestedSimultaneousTaskCount = simultaneousTasks;
        requestedTime = System.currentTimeMillis();
    }

    public long getRequestedTime() {
		return requestedTime;
	}

	public int[][] requestedSimultaneousTaskCount() {
        return requestedSimultaneousTaskCount;
    }

    public void updateRequestedSimultaneousTaskCount(int[][] newRequestedSimultaneousTaskCount) {
        requestedSimultaneousTaskCount = newRequestedSimultaneousTaskCount;
    }

    public CloudMethodResourceDescription getRequested() {
        return requested;
    }

    public CloudProvider getProvider() {
        return provider;
    }
    
    public void print(Logger resourcesLogger, boolean debug){
    	StringBuilder compositionString = new StringBuilder();
        for (java.util.Map.Entry<CloudInstanceTypeDescription, int[]> entry : this.getRequested().getTypeComposition().entrySet()) {
            compositionString.append(" \t\tTYPE = [\n").append("\t\t\tNAME = ").append(entry.getKey().getName())
                    .append("\t\t\tCOUNT= ").append(entry.getValue()[0]).append("\t\t]\n");
        }
        
		resourcesLogger.info("ORDER_CREATION = [\n" + "\tTYPE_COMPOSITION = [" + compositionString.toString() + "]\n"
                + "\tPROVIDER = " + this.getProvider() + "\n" + "]");
        if (debug) {
            StringBuilder sb = new StringBuilder();
            sb.append("EXPECTED_SIM_TASKS = [").append("\n");
            for (int i = 0; i < CoreManager.getCoreCount(); i++) {
                for (int j = 0; j < this.requestedSimultaneousTaskCount()[i].length; ++j) {
                    sb.append("\t").append("IMPLEMENTATION_INFO = [").append("\n");
                    sb.append("\t\t").append("COREID = ").append(i).append("\n");
                    sb.append("\t\t").append("IMPLID = ").append(j).append("\n");
                    sb.append("\t\t").append("SIM_TASKS = ").append(this.requestedSimultaneousTaskCount()[i][j]).append("\n");
                    sb.append("\t").append("]").append("\n");
                }
            }
            sb.append("]");
            resourcesLogger.debug(sb.toString());
        }
    }

}
