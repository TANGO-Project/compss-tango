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

import integratedtoolkit.ITConstants;
import integratedtoolkit.ITConstants.Lang;

import integratedtoolkit.types.parameter.Parameter;
import integratedtoolkit.types.resources.MethodResourceDescription;
import integratedtoolkit.types.annotations.parameter.DataType;

public abstract class AbstractMethodImplementation extends Implementation implements Externalizable {

    private static final Lang LANG;

    public enum MethodType {
        METHOD, // For native methods
        MPI, // For MPI methods
        OMPSS, // For OmpSs methods
        OPENCL, // For OpenCL methods
        BINARY, // For binary methods
        DECAF // For decaf methods
    }

    static {
        // Compute language
        Lang l = Lang.JAVA;

        String langProperty = System.getProperty(ITConstants.IT_LANG);
        if (langProperty != null) {
            if (langProperty.equalsIgnoreCase("python")) {
                l = Lang.PYTHON;
            } else if (langProperty.equalsIgnoreCase("c")) {
                l = Lang.C;
            }
        }

        LANG = l;
    }

    public AbstractMethodImplementation() {
        // For externalizable
        super();
    }

    public AbstractMethodImplementation(Integer coreId, Integer implementationId, MethodResourceDescription annot) {
        super(coreId, implementationId, annot);
    }

    public static String getSignature(String declaringClass, String methodName, boolean hasTarget, boolean hasReturn,
            Parameter[] parameters) {

        StringBuilder buffer = new StringBuilder();
        buffer.append(methodName).append("(");

        switch (LANG) {
            case JAVA:
            case C:
                int numPars = parameters.length;
                if (hasTarget) {
                    numPars--;
                }
                if (hasReturn) {
                    numPars--;
                }
                if (numPars > 0) {
                    DataType type = parameters[0].getType();
                    type = (type == DataType.PSCO_T) ? DataType.OBJECT_T : type;
                    buffer.append(type);
                    for (int i = 1; i < numPars; i++) {
                        type = parameters[i].getType();
                        type = (type == DataType.PSCO_T) ? DataType.OBJECT_T : type;
                        buffer.append(",").append(type);
                    }
                }
                break;
            case PYTHON:
                // There is no function overloading in Python
                break;
        }

        buffer.append(")").append(declaringClass);
        return buffer.toString();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.METHOD;
    }

    public abstract MethodType getMethodType();

    public abstract String getMethodDefinition();

    @Override
    public MethodResourceDescription getRequirements() {
        return (MethodResourceDescription) requirements;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

}
