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
package integratedtoolkit.types.data.location;

import integratedtoolkit.types.resources.Resource;
import integratedtoolkit.types.uri.MultiURI;
import integratedtoolkit.util.ErrorManager;

import java.util.LinkedList;
import java.util.List;

import storage.StorageException;
import storage.StorageItf;


public class PersistentLocation extends DataLocation {

    private final String id;


    public PersistentLocation(String id) {
        super();
        this.id = id;
    }

    @Override
    public DataLocation.Type getType() {
        return DataLocation.Type.PERSISTENT;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.PERSISTENT_URI;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public List<MultiURI> getURIs() {
        // Retrieve locations from Back-end
        List<String> locations = null;
        try {
            locations = StorageItf.getLocations(this.id);
        } catch (StorageException e) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }
        if (locations == null) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }

        // Retrieve URIs from hosts
        LinkedList<MultiURI> uris = new LinkedList<>();
        for (String hostName : locations) {
            Resource host = Resource.getResource(hostName);
            if (host != null) {
                uris.add(new MultiURI(Protocol.PERSISTENT_URI, host, this.id));
            } else {
                LOGGER.warn("Storage Back-End returned non-registered host " + hostName + ". Skipping URI in host");
            }
        }

        return uris;
    }

    @Override
    public List<Resource> getHosts() {
        LOGGER.debug("Get PSCO locations for " + this.id);

        // Retrieve locations from Back-end
        List<String> locations = null;
        try {
            locations = StorageItf.getLocations(this.id);
        } catch (StorageException e) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }
        if (locations == null) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }

        // Get hosts
        List<Resource> hosts = new LinkedList<>();
        for (String hostName : locations) {
            Resource host = Resource.getResource(hostName);
            if (host != null) {
                hosts.add(host);
            } else {
                LOGGER.warn("Storage Back-End returned non-registered host " + hostName);
            }
        }

        return hosts;
    }

    @Override
    public MultiURI getURIInHost(Resource targetHost) {
        // Retrieve locations from Back-end
        List<String> locations = null;
        try {
            locations = StorageItf.getLocations(this.id);
        } catch (StorageException e) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }
        if (locations == null) {
            ErrorManager.error("ERROR: Cannot retrieve locations of " + this.id + " from Storage Back-end");
        }

        // Get URIs in host
        for (String hostName : locations) {
            if (hostName.equals(targetHost.getName())) {
                return new MultiURI(Protocol.PERSISTENT_URI, targetHost, this.id);
            }
        }

        return null;
    }

    @Override
    public boolean isTarget(DataLocation target) {
        if (target.getType() != DataLocation.Type.PERSISTENT) {
            return false;
        }

        return this.id.equals(((PersistentLocation) target).id);
    }

    @Override
    public String toString() {
        return Protocol.PERSISTENT_URI.getSchema() + this.id;
    }

    @Override
    public String getSharedDisk() {
        return null;
    }

    @Override
    public String getPath() {
        return this.id;
    }

    @Override
    public String getLocationKey() {
        return this.id + ":persistent:" + "allHosts";
    }

    @Override
    public int compareTo(DataLocation o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (o.getClass() != PersistentLocation.class) {
            return (this.getClass().getName()).compareTo(PersistentLocation.class.toString());
        } else {
            return this.id.compareTo(((PersistentLocation) o).id);
        }
    }

}
