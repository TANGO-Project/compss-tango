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

package es.bsc.compss.types.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.bsc.compss.types.annotations.parameter.Type;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.parameter.Stream;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
/**
 * Parameters description
 *
 */
public @interface Parameter {

    /**
     * Returns the type of the parameter
     * 
     * @return the type of the parameter
     */
    Type type() default Type.UNSPECIFIED;

    /**
     * Returns the direction of the parameter
     * 
     * @return the direction of the parameter
     */
    // Set default direction=IN for basic types
    Direction direction() default Direction.IN;

    /**
     * Returns if the parameter has been annotated as an stream entry
     * 
     * @return the stream entry of the parameter
     */
    Stream stream() default Stream.UNSPECIFIED;

    /**
     * Returns the prefix of the parameter
     * 
     * @return the prefix of the parameter
     */
    String prefix() default Constants.PREFIX_EMTPY;

}
