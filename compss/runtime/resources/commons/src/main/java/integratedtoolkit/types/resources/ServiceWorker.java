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
package integratedtoolkit.types.resources;

import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.resources.configuration.ServiceConfiguration;

public class ServiceWorker extends Worker<ServiceResourceDescription> {

    private String wsdl;

    public ServiceWorker(String wsdl, ServiceResourceDescription description, ServiceConfiguration conf) {
        super(wsdl, description, conf, null);
        this.wsdl = wsdl;
    }

    public ServiceWorker(ServiceWorker sw) {
        super(sw);
        this.wsdl = sw.getWsdl();
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }

    public String getWsdl() {
        return wsdl;
    }

    public String getServiceName() {
        return description.getServiceName();
    }

    public String getNamespace() {
        return description.getNamespace();
    }

    public String getPort() {
        return description.getPort();
    }

    @Override
    public String getName() {
        return wsdl;
    }

    @Override
    public Integer fitCount(Implementation impl) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasAvailable(ServiceResourceDescription consumption) {
        return true;
    }

    @Override
    public boolean usesGPU(ServiceResourceDescription consumption) {
        return false;
    }

    @Override
    public boolean usesFPGA(ServiceResourceDescription consumption) {
        return false;
    }

    @Override
    public boolean usesOthers(ServiceResourceDescription consumption) {
        return false;
    }

    @Override
    public ServiceResourceDescription reserveResource(ServiceResourceDescription consumption) {
        // Always can be hosted and uses the same amount of resource than asked
        return consumption;
    }

    @Override
    public void releaseResource(ServiceResourceDescription consumption) {
    }

    @Override
    public void releaseAllResources() {
        super.resetUsedTaskCounts();
    }

    @Override
    public Type getType() {
        return Type.SERVICE;
    }

    @Override
    public String getMonitoringData(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append("<TotalComputingUnits>").append(this.getMaxCPUTaskCount()).append("</TotalComputingUnits>").append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Resource t) {
        if (t == null) {
            throw new NullPointerException();
        }
        switch (t.getType()) {
            case SERVICE:
                return getName().compareTo(t.getName());
            case WORKER:
                return -1;
            case MASTER:
                return -1;
            default:
                return getName().compareTo(t.getName());
        }
    }

    @Override
    public boolean canRun(Implementation implementation) {
        switch (implementation.getTaskType()) {
            case SERVICE:
                ServiceResourceDescription s = (ServiceResourceDescription) implementation.getRequirements();
                return (this.description.getNamespace().compareTo(s.getNamespace()) == 0
                        && this.description.getServiceName().compareTo(s.getServiceName()) == 0
                        && this.description.getPort().compareTo(s.getPort()) == 0);
            default:
                return false;
        }
    }

    @Override
    public String getResourceLinks(String prefix) {
        StringBuilder sb = new StringBuilder(super.getResourceLinks(prefix));
        sb.append(prefix).append("TYPE = SERVICE").append("\n");

        return sb.toString();
    }

    @Override
    public ServiceWorker getSchedulingCopy() {
        return new ServiceWorker(this);
    }

}
