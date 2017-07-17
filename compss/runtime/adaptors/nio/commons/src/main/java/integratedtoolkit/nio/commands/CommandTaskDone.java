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

import integratedtoolkit.nio.NIOAgent;
import integratedtoolkit.nio.NIOTaskResult;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import es.bsc.comm.Connection;


public class CommandTaskDone extends Command implements Externalizable {

    private boolean successful;
    private NIOTaskResult tr;


    public CommandTaskDone() {
    }

    public CommandTaskDone(NIOAgent ng, NIOTaskResult tr, boolean successful) {
        super(ng);
        this.tr = tr;
        this.successful = successful;
    }

    @Override
    public CommandType getType() {
        return CommandType.TASK_DONE;
    }

    @Override
    public void handle(Connection c) {
        NIOAgent nm = (NIOAgent) agent;
        nm.receivedTaskDone(c, tr, successful);
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        successful = in.readBoolean();
        tr = (NIOTaskResult) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(successful);
        out.writeObject(tr);
    }

    @Override
    public String toString() {
        return "Job" + tr.getTaskId() + " finishes " + (successful ? "properly" : "with some errors");
    }

}
