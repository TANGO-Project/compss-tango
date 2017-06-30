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
package integratedtoolkit.loader;

import integratedtoolkit.loader.total.ObjectRegistry;
import integratedtoolkit.types.annotations.parameter.Direction;


public interface LoaderAPI {

    /**
     * Returns the renaming of the file version opened
     * 
     * @param fileName
     * @param mode
     * @return
     */
    public String openFile(String fileName, Direction mode);

    /**
     * Returns the renaming of the last file version just transferred
     * 
     * @param fileName
     * @param destDir
     * @return
     */
    String getFile(String fileName, String destDir);

    /**
     * Returns a copy of the last object version
     * 
     * @param o
     * @param hashCode
     * @param destDir
     * @return
     */
    Object getObject(Object o, int hashCode, String destDir);

    /**
     * Serializes the given object
     * 
     * @param o
     * @param hashCode
     * @param destDir
     */
    void serializeObject(Object o, int hashCode, String destDir);

    /**
     * Sets the object Registry instance
     *
     * @param oReg
     */
    void setObjectRegistry(ObjectRegistry oReg);

    /**
     * Returns the directory where to store temporary files
     * 
     * @return
     */
    String getTempDir();

}
