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
package integratedtoolkit.test.api.impl;

import static org.junit.Assert.assertEquals;
import integratedtoolkit.ITConstants;
import integratedtoolkit.api.impl.COMPSsRuntimeImpl;
import integratedtoolkit.types.implementations.AbstractMethodImplementation;
import integratedtoolkit.types.implementations.AbstractMethodImplementation.MethodType;
import integratedtoolkit.util.CoreManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class COMPSsRuntimeImplTest {

    private static final String DUMMY_ADAPTOR_CLASS = "integratedtoolkit.test.dummyAdaptor.DummyAdaptor";
    private COMPSsRuntimeImpl rt;

    static {
        System.setProperty(ITConstants.IT_COMM_ADAPTOR, DUMMY_ADAPTOR_CLASS);
    }

    @Before
    public void setUp() throws Exception {
        String resources = this.getClass().getResource("resources.xml").getPath();
        String resources_xsd = this.getClass().getResource("resources_schema.xsd").getPath();
        String project = this.getClass().getResource("project.xml").getPath();
        String project_xsd = this.getClass().getResource("project_schema.xsd").getPath();
        System.setProperty(ITConstants.IT_LANG, ITConstants.Lang.PYTHON.name());
        System.setProperty(ITConstants.IT_RES_FILE, resources);
        System.setProperty(ITConstants.IT_RES_SCHEMA, resources_xsd);
        System.setProperty(ITConstants.IT_PROJ_FILE, project);
        System.setProperty(ITConstants.IT_PROJ_SCHEMA, project_xsd);

        rt = new COMPSsRuntimeImpl();
        rt.startIT();
    }

    @Test
    public void testPythonCERegister() {
        // METHOD
        System.out.println("Registering METHOD implementation");
        String coreElementSignature = "methodClass.methodName";
        String implSignature = "methodClass.methodName";
        String implConstraints = "ComputingUnits:2";
        String implType = "METHOD";
        String[] implTypeArgs = new String[]{"methodClass", "methodName"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        AbstractMethodImplementation mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(0).get(0);
        assertEquals(2, mi.getRequirements().getProcessors().get(0).getComputingUnits());

        // MPI
        System.out.println("Registering MPI implementation");
        coreElementSignature = "methodClass1.methodName1";
        implSignature = "mpi.MPI";
        implConstraints = "StorageType:SSD";
        implType = "MPI";
        implTypeArgs = new String[]{"mpiBinary", "mpiWorkingDir", "mpiRunner"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(1).get(0);
        assertEquals(MethodType.MPI, mi.getMethodType());
        assertEquals("SSD", mi.getRequirements().getStorageType());

        // DECAF
        System.out.println("Registering DECAF implementation");
        coreElementSignature = "methodClass1.methodName1";
        implSignature = "decaf.DECAF";
        implConstraints = "StorageSize:2.0";
        implType = "DECAF";
        implTypeArgs = new String[]{"dfScript", "dfExceutor", "dfLib", "dfWorkingDir", "mpiRunner"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(1).get(1);
        assertEquals(MethodType.DECAF, mi.getMethodType());
        assertEquals(2.0, mi.getRequirements().getStorageSize(), 0.1);

        // BINARY
        System.out.println("Registering BINARY implementation");
        coreElementSignature = "methodClass2.methodName2";
        implSignature = "binary.BINARY";
        implConstraints = "MemoryType:RAM";
        implType = "BINARY";
        implTypeArgs = new String[]{"binary", "binaryWorkingDir"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(2).get(0);
        assertEquals(MethodType.BINARY, mi.getMethodType());
        assertEquals("RAM", mi.getRequirements().getMemoryType());

        // OMPSS
        System.out.println("Registering OMPSS implementation");
        coreElementSignature = "methodClass3.methodName3";
        implSignature = "ompss.OMPSS";
        implConstraints = "ComputingUnits:3";
        implType = "OMPSS";
        implTypeArgs = new String[]{"ompssBinary", "ompssWorkingDir"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(3).get(0);
        assertEquals(MethodType.OMPSS, mi.getMethodType());
        assertEquals(3, mi.getRequirements().getProcessors().get(0).getComputingUnits());

        // OPENCL
        System.out.println("Registering OPENCL implementation");
        coreElementSignature = "methodClass4.methodName4";
        implSignature = "opencl.OPENCL";
        implConstraints = "ComputingUnits:4";
        implType = "OPENCL";
        implTypeArgs = new String[]{"openclKernel", "openclWorkingDir"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(4).get(0);
        assertEquals(MethodType.OPENCL, mi.getMethodType());
        assertEquals(4, mi.getRequirements().getProcessors().get(0).getComputingUnits());

        // VERSIONING
        System.out.println("Registering METHOD implementation");
        coreElementSignature = "methodClass.methodName";
        implSignature = "anotherClass.anotherMethodName";
        implConstraints = "ComputingUnits:1";
        implType = "METHOD";
        implTypeArgs = new String[]{"anotherClass", "anotherMethodName"};
        rt.registerCoreElement(coreElementSignature, implSignature, implConstraints, implType, implTypeArgs);

        mi = (AbstractMethodImplementation) CoreManager.getCoreImplementations(0).get(1);
        assertEquals(MethodType.METHOD, mi.getMethodType());
        assertEquals(1, mi.getRequirements().getProcessors().get(0).getComputingUnits());
    }

    @After
    public void tearDown() {
        // rt.stopIT(true);
    }

}
