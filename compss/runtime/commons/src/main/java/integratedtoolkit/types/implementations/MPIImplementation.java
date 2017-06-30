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


public class MPIImplementation extends AbstractMethodImplementation implements Externalizable {

    private String mpiRunner;
    private String binary;
    private String workingDir;


    public MPIImplementation() {
        // For externalizable
        super();
    }

    public MPIImplementation(String binary, String workingDir, String mpiRunner, Integer coreId, Integer implementationId,
            MethodResourceDescription annot) {

        super(coreId, implementationId, annot);

        this.mpiRunner = mpiRunner;
        this.workingDir = workingDir;
        this.binary = binary;
    }

    public String getBinary() {
        return this.binary;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public String getMpiRunner() {
        return this.mpiRunner;
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.MPI;
    }

    @Override
    public String getMethodDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("[MPI RUNNER=").append(mpiRunner);
        sb.append(", BINARY=").append(binary);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " MPI Method with binary " + binary + " and MPIrunner " + mpiRunner;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        mpiRunner = (String) in.readObject();
        binary = (String) in.readObject();
        workingDir = (String) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(mpiRunner);
        out.writeObject(binary);
        out.writeObject(workingDir);
    }

}
