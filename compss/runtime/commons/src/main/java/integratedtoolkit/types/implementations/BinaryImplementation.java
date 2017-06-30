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
package integratedtoolkit.types.implementations;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import integratedtoolkit.types.resources.MethodResourceDescription;


public class BinaryImplementation extends AbstractMethodImplementation implements Externalizable {

    private String binary;
    private String workingDir;


    public BinaryImplementation() {
        // For externalizable
        super();
    }

    public BinaryImplementation(String binary, String workingDir, Integer coreId, Integer implementationId,
            MethodResourceDescription annot) {

        super(coreId, implementationId, annot);

        this.binary = binary;
        this.workingDir = workingDir;
    }

    public String getBinary() {
        return this.binary;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.BINARY;
    }

    @Override
    public String getMethodDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("[BINARY=").append(binary);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " Binary Method with binary " + binary;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        binary = (String) in.readObject();
        workingDir = (String) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(binary);
        out.writeObject(workingDir);
    }

}
