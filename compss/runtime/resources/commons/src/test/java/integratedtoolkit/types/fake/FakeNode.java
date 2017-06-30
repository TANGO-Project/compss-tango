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
package integratedtoolkit.types.fake;

import integratedtoolkit.exceptions.AnnounceException;
import integratedtoolkit.exceptions.InitNodeException;
import integratedtoolkit.exceptions.UnstartedNodeException;
import integratedtoolkit.types.COMPSsWorker;
import integratedtoolkit.types.TaskDescription;
import integratedtoolkit.types.annotations.parameter.DataType;
import integratedtoolkit.types.data.LogicalData;
import integratedtoolkit.types.data.Transferable;
import integratedtoolkit.types.data.listener.EventListener;
import integratedtoolkit.types.data.location.DataLocation;
import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.job.Job;
import integratedtoolkit.types.job.JobListener;
import integratedtoolkit.types.resources.ExecutorShutdownListener;
import integratedtoolkit.types.resources.Resource;
import integratedtoolkit.types.resources.ShutdownListener;
import integratedtoolkit.types.uri.MultiURI;
import integratedtoolkit.types.uri.SimpleURI;
import java.util.List;

public class FakeNode extends COMPSsWorker {

    private final String name;

    public FakeNode(String name) {
        super(name, null);
        this.name = name;
    }

    @Override
    public String getUser() {
        return "";
    }

    @Override
    public String getClasspath() {
        return "";
    }

    @Override
    public String getPythonpath() {
        return "";
    }

    @Override
    public void updateTaskCount(int processorCoreCount) {

    }

    @Override
    public void announceDestruction() throws AnnounceException {

    }

    @Override
    public void announceCreation() throws AnnounceException {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void start() throws InitNodeException {

    }

    @Override
    public void setInternalURI(MultiURI u) throws UnstartedNodeException {

    }

    @Override
    public Job<?> newJob(int taskId, TaskDescription taskparams, Implementation impl, Resource res, List<String> slaveWorkersNodeNames, JobListener listener) {
        return null;
    }

    @Override
    public void sendData(LogicalData srcData, DataLocation loc, DataLocation target, LogicalData tgtData, Transferable reason, EventListener listener) {

    }

    @Override
    public void obtainData(LogicalData srcData, DataLocation source, DataLocation target, LogicalData tgtData, Transferable reason, EventListener listener) {

    }

    @Override
    public void stop(ShutdownListener sl) {

    }

    @Override
    public SimpleURI getCompletePath(DataType type, String name) {
        return null;
    }

    @Override
    public void deleteTemporary() {
    }

    @Override
    public boolean generatePackage() {
        return false;
    }

    @Override
    public void shutdownExecutionManager(ExecutorShutdownListener sl) {
    }

    @Override
    public boolean generateWorkersDebugInfo() {
        return false;
    }

}
