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


package es.bsc.compss.types.resources;

import es.bsc.compss.types.COMPSsWorker;
import es.bsc.compss.types.CloudProvider;
import es.bsc.compss.types.resources.description.CloudImageDescription;
import es.bsc.compss.types.resources.configuration.MethodConfiguration;
import es.bsc.compss.types.resources.description.CloudMethodResourceDescription;
import es.bsc.compss.types.resources.updates.PendingReduction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class CloudMethodWorker extends MethodWorker {

    // Pending removals
    private final LinkedList<PendingReduction<MethodResourceDescription>> pendingReductions;
    private final CloudMethodResourceDescription toRemove;
    private final CloudProvider provider;


    public CloudMethodWorker(String name, CloudProvider provider, CloudMethodResourceDescription description, COMPSsWorker worker,
            int limitOfTasks, int limitGPUTasks, int limitFPGATasks, int limitOTHERTasks, Map<String, String> sharedDisks) {

        super(name, description, worker, limitOfTasks, limitGPUTasks, limitFPGATasks, limitOTHERTasks, sharedDisks);
        this.provider = provider;
        this.toRemove = new CloudMethodResourceDescription();
        this.pendingReductions = new LinkedList<>();
    }

    public CloudMethodWorker(String name, CloudProvider provider, CloudMethodResourceDescription description, MethodConfiguration config,
            Map<String, String> sharedDisks) {

        super(name, description, config, sharedDisks);
        this.provider = provider;

        if (this.description != null) {
            // Add name
            ((CloudMethodResourceDescription) this.description).setName(name);
        }

        this.toRemove = new CloudMethodResourceDescription();
        this.pendingReductions = new LinkedList<>();
    }

    public CloudMethodWorker(CloudMethodWorker cmw) {
        super(cmw);
        this.provider = cmw.provider;
        this.toRemove = cmw.toRemove.copy();
        this.pendingReductions = cmw.pendingReductions;
    }

    public CloudProvider getProvider() {
        return provider;
    }

    @Override
    public Type getType() {
        return Type.WORKER;
    }

    @Override
    public CloudMethodResourceDescription getDescription() {
        return (CloudMethodResourceDescription) super.getDescription();
    }

    @Override
    public String getMonitoringData(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(super.getMonitoringData(prefix));

        String providerName = provider.getName();
        if (providerName == null) {
            providerName = "";
        }
        sb.append(prefix).append("<Provider>").append(providerName).append("</Provider>").append("\n");

        CloudImageDescription image = ((CloudMethodResourceDescription) description).getImage();
        String imageName = "";
        if (image != null) {
            imageName = image.getImageName();
        }
        sb.append(prefix).append("<Image>").append(imageName).append("</Image>").append("\n");

        return sb.toString();
    }

    public void increaseFeatures(CloudMethodResourceDescription increment) {
        synchronized (available) {
            available.increase(increment);
        }
        synchronized (description) {
            ((CloudMethodResourceDescription) this.description).increase(increment);
        }
        updatedFeatures();
    }

    @Override
    public MethodResourceDescription reserveResource(MethodResourceDescription consumption) {
        if (!hasAvailable(consumption)) {
            return null;
        }

        return super.reserveResource(consumption);
    }

    @Override
    public synchronized void releaseResource(MethodResourceDescription consumption) {
        LOGGER.debug("Checking cloud resources to release...");
        // Freeing task constraints
        synchronized (available) {
            super.releaseResource(consumption);

            // Performing as many as possible reductions
            synchronized (pendingReductions) {
                if (!pendingReductions.isEmpty()) {
                    Iterator<PendingReduction<MethodResourceDescription>> prIt = pendingReductions.iterator();
                    while (prIt.hasNext()) {
                        PendingReduction<MethodResourceDescription> pRed = prIt.next();
                        if (available.containsDynamic(pRed.getModification())) {
                            // Perform reduction
                            available.reduce(pRed.getModification());

                            // Untag pending to remove reduction
                            synchronized (toRemove) {
                                toRemove.reduce(pRed.getModification());
                            }
                            // Reduction is done, release sem
                            LOGGER.debug("Releasing cloud resource " + this.getName());
                            pRed.notifyCompletion();
                            prIt.remove();
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public synchronized void applyReduction(PendingReduction<MethodResourceDescription> pRed) {
        CloudMethodResourceDescription reduction = (CloudMethodResourceDescription) pRed.getModification();
        synchronized (description) {
            ((CloudMethodResourceDescription) this.description).reduce(reduction);
        }
        synchronized (available) {
            if (!hasAvailable(reduction) && this.getUsedCPUTaskCount() > 0) {

                // This resource is still running tasks. Wait for them to finish...
                // Mark to remove and enqueue pending reduction
            	LOGGER.debug("Resource in use. Adding pending reduction");
                synchronized (toRemove) {
                    toRemove.increase(reduction);
                }
                synchronized (pendingReductions) {
                    pendingReductions.add(pRed);
                }
            } else {
                // Resource is not executing tasks. We can erase it, nothing to do
                available.reduce(reduction);
                pRed.notifyCompletion();
            }
        }

        updatedFeatures();
    }

    @Override
    public boolean hasAvailable(MethodResourceDescription consumption) {
    	synchronized (available) {
            synchronized (toRemove) {
                consumption.increaseDynamic(toRemove);
                boolean fits = super.hasAvailable(consumption);
                consumption.reduceDynamic(toRemove);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Cloud Method Worker received:");
                    LOGGER.debug("With result: " + fits);
                }
                return fits;
            }
        }
    }

    public boolean shouldBeStopped() {
        synchronized (available) {
            synchronized (toRemove) {
                return ((available.getTotalCPUComputingUnits() == 0) && (toRemove.getTotalCPUComputingUnits() == 0));
            }
        }
    }

    @Override
    public CloudMethodWorker getSchedulingCopy() {
        return new CloudMethodWorker(this);
    }

}
