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


package es.bsc.compss.comm.fake;

import es.bsc.compss.comm.CommAdaptor;
import es.bsc.compss.exceptions.ConstructConfigurationException;
import es.bsc.compss.types.COMPSsWorker;
import es.bsc.compss.types.data.operation.DataOperation;
import es.bsc.compss.types.fake.FakeNode;
import es.bsc.compss.types.resources.configuration.Configuration;
import es.bsc.compss.types.resources.configuration.ServiceConfiguration;
import es.bsc.compss.types.uri.MultiURI;
import java.util.LinkedList;


public class FakeServiceAdaptor implements CommAdaptor {

    @Override
    public void init() {

    }

    @Override
    public Configuration constructConfiguration(Object project_properties, Object resources_properties) throws ConstructConfigurationException {
        return new ServiceConfiguration(this.getClass().getName(), "WSDL");
    }

    @Override
    public COMPSsWorker initWorker(String workerName, Configuration config) {
        return new FakeNode(workerName);

    }

    @Override
    public void stop() {

    }

    @Override
    public LinkedList<DataOperation> getPending() {
        return new LinkedList<>();
    }

    @Override
    public void completeMasterURI(MultiURI u) {

    }

    @Override
    public void stopSubmittedJobs() {

    }

}
