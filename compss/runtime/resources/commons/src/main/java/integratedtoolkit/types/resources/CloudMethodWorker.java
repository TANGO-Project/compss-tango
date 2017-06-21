package integratedtoolkit.types.resources;

import integratedtoolkit.types.COMPSsWorker;
import integratedtoolkit.types.CloudImageDescription;
import integratedtoolkit.types.resources.configuration.MethodConfiguration;
import integratedtoolkit.types.resources.description.CloudMethodResourceDescription;
import integratedtoolkit.types.resources.updates.PendingReduction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class CloudMethodWorker extends MethodWorker {

    // Pending removals
    private final LinkedList<PendingReduction<MethodResourceDescription>> pendingReductions;
    private final CloudMethodResourceDescription toRemove;


    public CloudMethodWorker(CloudMethodResourceDescription description, COMPSsWorker worker, int limitOfTasks,
            HashMap<String, String> sharedDisks) {

        super(description.getName(), description, worker, limitOfTasks, sharedDisks);
        this.toRemove = new CloudMethodResourceDescription();
        this.pendingReductions = new LinkedList<>();
    }

    public CloudMethodWorker(String name, CloudMethodResourceDescription description, MethodConfiguration config,
            HashMap<String, String> sharedDisks) {

        super(name, description, config, sharedDisks);

        if (this.description != null) {
            // Add name
            ((CloudMethodResourceDescription) this.description).setName(name);
        }

        this.toRemove = new CloudMethodResourceDescription();
        this.pendingReductions = new LinkedList<>();
    }

    public CloudMethodWorker(CloudMethodWorker cmw) {
        super(cmw);
        this.toRemove = cmw.toRemove.copy();
        this.pendingReductions = cmw.pendingReductions;
    }

    @Override
    public Type getType() {
        return Type.WORKER;
    }

    @Override
    public String getMonitoringData(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(super.getMonitoringData(prefix));

        String providerName = ((CloudMethodResourceDescription) description).getProviderName();
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
            description.increase(increment);
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
        MethodResourceDescription reduction = (MethodResourceDescription) pRed.getModification();
        synchronized (description) {
            description.reduce(reduction);
        }
        synchronized (available) {
            if (!hasAvailable(reduction) && this.getUsedCPUTaskCount() > 0) {

                // This resource is still running tasks. Wait for them to finish...
                // Mark to remove and enqueue pending reduction
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
