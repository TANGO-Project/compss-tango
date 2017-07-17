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
import integratedtoolkit.util.SharedDiskManager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class SharedLocation extends DataLocation {

    private final String diskName;
    private final String path;
    private final Protocol protocol;


    public SharedLocation(Protocol protocol, String sharedDisk, String path) {
        this.diskName = sharedDisk;
        this.path = path;
        this.protocol = protocol;
    }

    @Override
    public MultiURI getURIInHost(Resource host) {
        String diskPath = SharedDiskManager.getMounpoint(host, this.diskName);
        if (diskPath == null) {
            return null;
        }
        if (!diskPath.endsWith(File.separator)) {
            diskPath = diskPath + File.separator;
        }

        return new MultiURI(this.protocol, host, diskPath + this.path);
    }

    @Override
    public DataLocation.Type getType() {
        return DataLocation.Type.SHARED;
    }

    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }

    @Override
    public List<MultiURI> getURIs() {
        List<MultiURI> uris = new LinkedList<>();
        List<Resource> resList = SharedDiskManager.getAllMachinesfromDisk(diskName);
        Resource[] resources = resList.toArray(new Resource[resList.size()]);
        for (Resource host : resources) {
            String diskPath = SharedDiskManager.getMounpoint(host, diskName);
            if (!diskPath.endsWith(File.separator)) {
                diskPath = diskPath + File.separator;
            }

            uris.add(new MultiURI(this.protocol, host, diskPath + path));
        }

        return uris;
    }

    @Override
    public List<Resource> getHosts() {
        return SharedDiskManager.getAllMachinesfromDisk(diskName);
    }

    @Override
    public boolean isTarget(DataLocation target) {
        String targetDisk;
        String targetPath;
        if (target.getType().equals(DataLocation.Type.PRIVATE)) {
            PrivateLocation privateLoc = (PrivateLocation) target;
            targetDisk = null; // TODO: extract from URI
            targetPath = privateLoc.getPath();
        } else {
            SharedLocation sharedloc = (SharedLocation) target;
            targetDisk = sharedloc.diskName;
            targetPath = sharedloc.path;
        }
        return (targetDisk != null && targetDisk.contentEquals(diskName) && targetPath.contentEquals(targetPath));
    }

    @Override
    public String getSharedDisk() {
        return diskName;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getLocationKey() {
        return path + ":shared:" + diskName;
    }

    @Override
    public int compareTo(DataLocation o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (o.getClass() != SharedLocation.class) {
            return (this.getClass().getName()).compareTo(SharedLocation.class.toString());
        } else {
            SharedLocation sl = (SharedLocation) o;
            int compare = diskName.compareTo(sl.diskName);
            if (compare == 0) {
                compare = path.compareTo(sl.path);
            }
            return compare;
        }
    }

    @Override
    public String toString() {
        return "shared:" + diskName + File.separator + path;
    }

}
