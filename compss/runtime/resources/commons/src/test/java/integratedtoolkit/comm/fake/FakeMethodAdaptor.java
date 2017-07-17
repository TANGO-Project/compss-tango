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
package integratedtoolkit.comm.fake;

import integratedtoolkit.comm.CommAdaptor;
import integratedtoolkit.exceptions.ConstructConfigurationException;
import integratedtoolkit.types.COMPSsWorker;
import integratedtoolkit.types.data.operation.DataOperation;
import integratedtoolkit.types.fake.FakeNode;
import integratedtoolkit.types.resources.configuration.Configuration;
import integratedtoolkit.types.resources.configuration.MethodConfiguration;
import integratedtoolkit.types.uri.MultiURI;
import java.util.LinkedList;

/**
 *
 * @author flordan
 */
public class FakeMethodAdaptor implements CommAdaptor {

    @Override
    public void init() {

    }

    @Override
    public Configuration constructConfiguration(Object project_properties, Object resources_properties) throws ConstructConfigurationException {
        return new MethodConfiguration(this.getClass().getName());
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
