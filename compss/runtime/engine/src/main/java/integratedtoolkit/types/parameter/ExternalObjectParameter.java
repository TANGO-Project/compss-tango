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
package integratedtoolkit.types.parameter;

import integratedtoolkit.types.annotations.parameter.DataType;
import integratedtoolkit.types.annotations.parameter.Direction;
import integratedtoolkit.types.annotations.parameter.Stream;
import integratedtoolkit.types.parameter.DependencyParameter;


public class ExternalObjectParameter extends DependencyParameter {

    /**
     * Serializable objects Version UID are 1L in all Runtime
     */
    private static final long serialVersionUID = 1L;

    private final int hashCode;
    private String pscoId;


    public ExternalObjectParameter(Direction direction, Stream stream, String prefix, String pscoId, int hashCode) {
        super(DataType.EXTERNAL_OBJECT_T, direction, stream, prefix);
        this.pscoId = pscoId;
        this.hashCode = hashCode;
    }

    public String getId() {
        return this.pscoId;
    }

    public void setId(String pscoId) {
        this.pscoId = pscoId;
    }

    public int getCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "ExternalObjectParameter with Id " + this.pscoId + " and HashCode " + this.hashCode;
    }

}
