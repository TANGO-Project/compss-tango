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
package integratedtoolkit.types.data;

import integratedtoolkit.comm.Comm;


public class DataVersion {

    private final DataInstanceId dataInstanceId;
    private int readers;
    private int writters;
    private boolean toDelete;


    public DataVersion(int dataId, int versionId) {
        this.readers = 0;
        this.dataInstanceId = new DataInstanceId(dataId, versionId);
        this.writters = 0;
        this.toDelete = false;
        Comm.registerData(dataInstanceId.getRenaming());
    }

    public DataInstanceId getDataInstanceId() {
        return this.dataInstanceId;
    }

    public void willBeRead() {
        readers++;
    }

    public void willBeWritten() {
        writters++;
    }

    public boolean hasPendingLectures() {
        return readers > 0;
    }

    public boolean hasBeenRead() {
        readers--;
        return checkDeletion();
    }

    public boolean hasBeenWritten() {
        writters--;
        return checkDeletion();
    }

    public boolean delete() {
        toDelete = true;
        if (readers == 0 && writters == 0) {
            Comm.removeData(dataInstanceId.getRenaming());
            return true;
        }
        return false;
    }

    private boolean checkDeletion() {
        if (toDelete // deletion requested
                && writters == 0 // version has been generated
                && readers == 0 // version has been read
        ) {
            Comm.removeData(dataInstanceId.getRenaming());
            return true;
        }
        return false;
    }

    public boolean isToDelete() {
        return toDelete;
    }

}
