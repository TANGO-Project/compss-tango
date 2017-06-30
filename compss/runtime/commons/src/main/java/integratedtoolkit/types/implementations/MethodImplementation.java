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

public class MethodImplementation extends AbstractMethodImplementation implements Externalizable {

    private String declaringClass;
    // In C implementations could have different method names
    private String alternativeMethod;

    public MethodImplementation() {
        // For externalizable
        super();
    }

    public MethodImplementation(String methodClass, String altMethodName, Integer coreId, Integer implementationId,
            MethodResourceDescription annot) {

        super(coreId, implementationId, annot);

        this.declaringClass = methodClass;
        this.alternativeMethod = altMethodName;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getAlternativeMethodName() {
        return alternativeMethod;
    }

    public void setAlternativeMethodName(String alternativeMethod) {
        this.alternativeMethod = alternativeMethod;
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.METHOD;
    }

    @Override
    public String getMethodDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("[DECLARING CLASS=").append(declaringClass);
        sb.append(", METHOD NAME=").append(alternativeMethod);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " Method declared in class " + declaringClass + "." + alternativeMethod + ": " + requirements.toString();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        declaringClass = (String) in.readObject();
        alternativeMethod = (String) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(declaringClass);
        out.writeObject(alternativeMethod);
    }

}
