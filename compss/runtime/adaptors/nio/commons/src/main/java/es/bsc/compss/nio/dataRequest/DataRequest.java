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


package es.bsc.compss.nio.dataRequest;

import es.bsc.compss.types.annotations.parameter.DataType;
import es.bsc.compss.nio.commands.Data;


public abstract class DataRequest {

    private final DataType type;
    private final Data source;
    private final String target;


    public DataRequest(DataType type, Data source, String target) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public Data getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public DataType getType() {
        return type;
    }

}
