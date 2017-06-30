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
package integratedtoolkit.test.dummyAdaptor;

import java.util.LinkedList;

import integratedtoolkit.comm.CommAdaptor;
import integratedtoolkit.exceptions.ConstructConfigurationException;
import integratedtoolkit.types.data.operation.DataOperation;
import integratedtoolkit.types.resources.configuration.Configuration;
import integratedtoolkit.types.resources.configuration.MethodConfiguration;


/**
 * Dummy Adaptor for testing purposes. Defined in main package because it is used in integration tests
 *
 */
public class DummyAdaptor implements CommAdaptor {

    private static final String ID = DummyAdaptor.class.getCanonicalName();


    /**
     * Instantiates a new Dummy Adaptor
     */
    public DummyAdaptor() {
        // Nothing to do since there are no attributes to initialize
    }

    @Override
    public void init() {
    }

    @Override
    public MethodConfiguration constructConfiguration(Object project_properties, Object resources_properties)
            throws ConstructConfigurationException {

        MethodConfiguration config = new MethodConfiguration(ID);
        return config;
    }

    @Override
    public DummyWorkerNode initWorker(String name, Configuration config) {
        return new DummyWorkerNode(name, (MethodConfiguration) config);
    }

    @Override
    public void stop() {
    }

    @Override
    public void stopSubmittedJobs() {
    }

    @Override
    public void completeMasterURI(integratedtoolkit.types.uri.MultiURI uri) {
    }

    @Override
    public LinkedList<DataOperation> getPending() {
        return null;
    }

}
