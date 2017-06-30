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
package integratedtoolkit.types.data.operation.copy;

import integratedtoolkit.types.data.LogicalData;
import integratedtoolkit.types.data.Transferable;
import integratedtoolkit.types.data.listener.EventListener;
import integratedtoolkit.types.data.location.DataLocation;
import integratedtoolkit.types.data.operation.DataOperation;


public class StorageCopy extends DataOperation {

    private final LogicalData srcData;
    private final DataLocation srcLoc;
    private final LogicalData tgtData;
    private DataLocation tgtLoc;
    private Transferable reason;


    public StorageCopy(LogicalData srcData, DataLocation prefSrc, DataLocation prefTgt, LogicalData tgtData, Transferable reason,
            EventListener listener) {

        super(srcData, listener);
        this.srcData = srcData;
        this.srcLoc = prefSrc;
        this.tgtData = tgtData;
        this.tgtLoc = prefTgt;
        this.reason = reason;
        if (DEBUG) {
            LOGGER.debug("Created replica " + this.getName() + " (id: " + this.getId() + ")");
        }
    }

    public LogicalData getSourceData() {
        return srcData;
    }

    public DataLocation getPreferredSource() {
        return srcLoc;
    }

    public DataLocation getTargetLoc() {
        return tgtLoc;
    }

    public LogicalData getTargetData() {
        return tgtData;
    }

    public void setFinalTarget(String targetAbsolutePath) {
        if (DEBUG) {
            LOGGER.debug(" Setting StorageCopy final target to : " + targetAbsolutePath);
        }
        reason.setDataTarget(targetAbsolutePath);
    }

    public boolean isRegistered() {
        return tgtData != null;
    }

}
