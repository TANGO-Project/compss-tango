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
package integratedtoolkit.gat.master.exceptions;

import integratedtoolkit.exceptions.CopyException;


/**
 * Exception for GAT Copies
 *
 */
public class GATCopyException extends CopyException {

    /**
     * Exception Version UID are 2L in all Runtime
     */
    private static final long serialVersionUID = 2L;


    /**
     * Creates a new nested Exception @e
     * 
     * @param e
     */
    public GATCopyException(Exception e) {
        super(e);
    }

    /**
     * Creates a new Exception with a given message @msg
     * 
     * @param msg
     */
    public GATCopyException(String msg) {
        super(msg);
    }

}
