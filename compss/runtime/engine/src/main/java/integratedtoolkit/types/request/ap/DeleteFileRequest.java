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
package integratedtoolkit.types.request.ap;

import java.io.File;

import integratedtoolkit.components.impl.AccessProcessor;
import integratedtoolkit.components.impl.DataInfoProvider;
import integratedtoolkit.components.impl.TaskAnalyser;
import integratedtoolkit.components.impl.TaskDispatcher;
import integratedtoolkit.types.data.FileInfo;
import integratedtoolkit.types.data.location.DataLocation;

public class DeleteFileRequest extends APRequest {

    private final DataLocation loc;

    public DeleteFileRequest(DataLocation loc) {
        this.loc = loc;
    }

    public DataLocation getLocation() {
        return loc;
    }

    @Override
    public void process(AccessProcessor ap, TaskAnalyser ta, DataInfoProvider dip, TaskDispatcher td) {
        FileInfo fileInfo = dip.deleteData(loc);

        if (fileInfo == null) {
            // File is not used by any task
            File f = new File(loc.getPath());
            if (f.delete()) {
                LOGGER.info("File " + loc.getPath() + "deleted");
            } else {
                LOGGER.error("Error on deleting file " + loc.getPath());
            }

        } else { // file is involved in some task execution
            // File Won't be read by any future task or from the main code.
            // Remove it from the dependency analysis and the files to be transferred back
            ta.deleteFile(fileInfo);
        }
    }

    @Override
    public APRequestType getRequestType() {
        return APRequestType.DELETE_FILE;
    }

}
