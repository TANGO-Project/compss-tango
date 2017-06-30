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
package integratedtoolkit.types.annotations.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import integratedtoolkit.types.annotations.Constants;
import integratedtoolkit.types.annotations.Constraints;
import integratedtoolkit.types.annotations.task.repeatables.Methods;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Methods.class)
/**
 * Methods definition
 *
 */
public @interface Method {

    /*
     * METHOD DEFINITION
     * 
     */

    /**
     * Returns the declaring class of the method
     * 
     * @return method's declaring class
     */
    String declaringClass();

    /**
     * Returns the name of the method
     * 
     * @return method's name
     */
    String name() default Constants.UNASSIGNED;

    /*
     * METHOD PROPERTIES
     * 
     */

    /**
     * Returns whether the method is modifier or not. Avoids synchronization on implicit parameter
     * 
     * @return boolean indicating whether the method is modifier or not
     */
    String isModifier() default Constants.IS_MODIFIER;

    /*
     * COMMON PROPERTIES
     * 
     */

    /**
     * Returns if the method has priority or not
     * 
     * @return if the method has priority or not
     */
    String priority() default Constants.IS_NOT_PRIORITARY_TASK;

    /**
     * Returns the method specific constraints
     * 
     * @return the method specific constraints
     */
    Constraints constraints() default @Constraints();

}
