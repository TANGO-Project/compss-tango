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

package es.bsc.compss.types.data.operation;

import es.bsc.compss.types.annotations.parameter.DataType;
import es.bsc.compss.types.data.Transferable;


public class FileTransferable implements Transferable {

    private Object source;
    private String target;


    public FileTransferable() {
    }

    @Override
    public Object getDataSource() {
        return source;
    }

    @Override
    public void setDataSource(Object dataSource) {
        this.source = dataSource;
    }

    @Override
    public String getDataTarget() {
        return target;
    }

    @Override
    public void setDataTarget(String target) {
        this.target = target;
    }

    @Override
    public DataType getType() {
        return DataType.FILE_T;
    }

}
