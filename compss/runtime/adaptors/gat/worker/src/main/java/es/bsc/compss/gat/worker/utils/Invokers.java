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

package es.bsc.compss.gat.worker.utils;

import java.io.File;
import java.lang.reflect.Method;

import es.bsc.compss.util.ErrorManager;
import es.bsc.compss.exceptions.InvokeExecutionException;
import es.bsc.compss.types.annotations.parameter.Stream;

import es.bsc.compss.worker.invokers.GenericInvoker;


public class Invokers {

    private static final String ERROR_INVOKE = "Error invoking requested method";


    public static Object invokeJavaMethod(String className, String methodName, Object target, Class<?>[] types, Object[] values) {
        // Use reflection to get the requested method
        Method method = null;
        try {
            Class<?> methodClass = Class.forName(className);
            method = methodClass.getMethod(methodName, types);
        } catch (ClassNotFoundException e) {
            ErrorManager.error("Application class not found");
        } catch (SecurityException e) {
            ErrorManager.error("Security exception");
        } catch (NoSuchMethodException e) {
            ErrorManager.error("Requested method not found");
        }

        if (method == null) {
            ErrorManager.error("Requested method is null");
        }

        // Invoke the requested method
        Object retValue = null;
        try {
            retValue = method.invoke(target, values);
        } catch (Exception e) {
            ErrorManager.error(ERROR_INVOKE, e);
        }

        return retValue;
    }

    public static Object invokeMPIMethod(String mpiRunner, String mpiBinary, Object target, Object[] values, Stream[] streams,
            String[] prefixes, File taskSandboxWorkingDir) {

        Object retValue = null;
        try {
            retValue = GenericInvoker.invokeMPIMethod(mpiRunner, mpiBinary, values, streams, prefixes, taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            ErrorManager.error(ERROR_INVOKE, iee);
        }

        return retValue;
    }

    public static Object invokeDecafMethod(String dfRunner, String dfScript, String dfExecutor, String dfLib, String mpiRunner,
            Object target, Object[] values, Stream[] streams, String[] prefixes, File taskSandboxWorkingDir) {

        Object retValue = null;
        try {
            retValue = GenericInvoker.invokeDecafMethod(dfRunner, dfScript, dfExecutor, dfLib, mpiRunner, values, streams, prefixes,
                    taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            ErrorManager.error(ERROR_INVOKE, iee);
        }

        return retValue;
    }

    public static Object invokeOmpSsMethod(String ompssBinary, Object target, Object[] values, Stream[] streams, String[] prefixes,
            File taskSandboxWorkingDir) {
        Object retValue = null;
        try {
            retValue = GenericInvoker.invokeOmpSsMethod(ompssBinary, values, streams, prefixes, taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            ErrorManager.error(ERROR_INVOKE, iee);
        }

        return retValue;
    }

    public static Object invokeOpenCLMethod(String kernel, Object target, Object[] values, Stream[] streams, String[] prefixes,
            File taskSandboxWorkingDir) {
        ErrorManager.error("ERROR: OpenCL is not supported");

        return null;
    }

    public static Object invokeBinaryMethod(String binary, Object target, Object[] values, Stream[] streams, String[] prefixes,
            File taskSandboxWorkingDir) {
        Object retValue = null;
        try {
            retValue = GenericInvoker.invokeBinaryMethod(binary, values, streams, prefixes, taskSandboxWorkingDir);
        } catch (InvokeExecutionException iee) {
            ErrorManager.error(ERROR_INVOKE, iee);
        }

        return retValue;
    }

}
