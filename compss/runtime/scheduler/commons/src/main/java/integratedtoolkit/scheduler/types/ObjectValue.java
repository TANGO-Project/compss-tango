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
package integratedtoolkit.scheduler.types;

public class ObjectValue<T> implements Comparable<ObjectValue<T>> {

    private final T obj;
    private final Score value;


    public ObjectValue(T o, Score value) {
        this.obj = o;
        this.value = value;
    }

    public T getObject() {
        return this.obj;
    }

    public Score getScore() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjectValue) {
            ObjectValue<T> ov = (ObjectValue<T>) obj;
            return this.hashCode() == ov.hashCode();
        }

        return false;
    }

    @Override
    public int compareTo(ObjectValue<T> o) {
        if (Score.isBetter(this.value, o.value)) {
            return -1;
        } else if (Score.isBetter(o.value, this.value)) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
