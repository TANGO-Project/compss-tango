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
package integratedtoolkit.connectors.fake;

import integratedtoolkit.connectors.Connector;
import integratedtoolkit.connectors.ConnectorException;
import integratedtoolkit.connectors.Cost;
import integratedtoolkit.types.CloudProvider;
import integratedtoolkit.types.ExtendedCloudMethodWorker;
import integratedtoolkit.types.ResourceCreationRequest;
import integratedtoolkit.types.resources.CloudMethodWorker;
import integratedtoolkit.types.resources.description.CloudMethodResourceDescription;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FakeConnector implements Connector, Cost {

    private static final Set<ResourceCreationRequest> PROCESSED_REQUESTS = new HashSet<>();


    public FakeConnector(CloudProvider provider, String connectorJarPath, String connectorMainClass,
            Map<String, String> connectorProperties) throws ConnectorException {
    }

    @Override
    public boolean turnON(String name, ResourceCreationRequest rR) {
        PROCESSED_REQUESTS.add(rR);
        return true;
    }

    public static Set<ResourceCreationRequest> getProcessedRequests() {
        return PROCESSED_REQUESTS;
    }

    @Override
    public void stopReached() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public Long getNextCreationTime() throws ConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public long getTimeSlot() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public void terminate(CloudMethodWorker worker, CloudMethodResourceDescription reduction) {
        if (worker instanceof CloudMethodWorker) {
            ((ExtendedCloudMethodWorker) worker).terminate();
        }
    }

    @Override
    public void terminateAll() {

    }

    @Override
    public Float getTotalCost() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public Float currentCostPerHour() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public Float getMachineCostPerHour(CloudMethodResourceDescription rc) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}
