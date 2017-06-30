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
package integratedtoolkit.nio.commands;

import es.bsc.comm.Connection;
import integratedtoolkit.nio.NIOAgent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class CommandExecutorShutdown extends Command implements Externalizable {

    public CommandExecutorShutdown() {
    }

    public CommandExecutorShutdown(NIOAgent agent) {
        super(agent);
    }

    @Override
    public CommandType getType() {
        return CommandType.STOP_EXECUTOR;
    }

    @Override
    public void handle(Connection c) {
        agent.shutdownExecutionManager(c);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    }

    @Override
    public String toString() {
        return "ExecutorShutdown";
    }

}
