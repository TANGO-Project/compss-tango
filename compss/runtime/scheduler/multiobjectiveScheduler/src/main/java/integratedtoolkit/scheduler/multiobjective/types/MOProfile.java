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
package integratedtoolkit.scheduler.multiobjective.types;

import integratedtoolkit.scheduler.types.Profile;
import integratedtoolkit.types.implementations.Implementation;
import integratedtoolkit.types.resources.Worker;
import integratedtoolkit.types.resources.WorkerResourceDescription;
import org.json.JSONException;
import org.json.JSONObject;


public class MOProfile extends Profile {

    private static final double DEFAULT_PRICE = 0;
    private static final double DEFAULT_POWER = 0;

    private double power;
    private double price;


    public MOProfile() {
        super();
        power = DEFAULT_POWER;
        price = DEFAULT_PRICE;
    }

    private MOProfile(MOProfile p) {
        super(p);
        this.power = p.power;
        this.price = p.price;
    }

    public MOProfile(JSONObject json) {
        super(json);
        if (json != null) {
            try {
                power = json.getDouble("power");
            } catch (JSONException je) {
                power = DEFAULT_POWER;
            }
            try {
                price = json.getDouble("price");
            } catch (JSONException je) {
                price = DEFAULT_PRICE;
            }
        } else {
            power = DEFAULT_POWER;
            price = DEFAULT_PRICE;
        }
    }

    public <T extends WorkerResourceDescription> MOProfile(Implementation impl, Worker<T> resource) {
        power = 0;
        price = 0;
    }

    public double getPower() {
        return power;
    }

    public double getPrice() {
        return price;
    }

    public void accumulate(MOProfile profile) {
        super.accumulate(profile);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = super.toJSONObject();
        jo.put("power", this.power);
        jo.put("price", this.price);
        return jo;
    }

    @Override
    public Profile copy() {
        return new MOProfile(this);
    }

    @Override
    public String toString() {
        return "[MOProfile " + getContent() + "]";
    }

    @Override
    protected String getContent() {
        return super.getContent() + " power=" + power + " price=" + price;
    }


    public static class Builder extends Profile.Builder {

        private double power = DEFAULT_POWER;
        private double price = DEFAULT_PRICE;


        public Builder() {
            super();
        }

        public MOProfile build() {
            MOProfile p = new MOProfile();
            update(p);
            return p;
        }

        public void setPower(double power) {
            this.power = power;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        protected <P extends MOProfile> void update(P p) {
            super.update(p);
            MOProfile prof = (MOProfile) p;
            prof.power = this.power;
            prof.price = this.price;
        }
    }
}
