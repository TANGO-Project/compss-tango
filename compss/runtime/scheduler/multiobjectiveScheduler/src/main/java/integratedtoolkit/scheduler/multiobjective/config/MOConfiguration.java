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
package integratedtoolkit.scheduler.multiobjective.config;

public class MOConfiguration {

    public static enum OptimizationParameter {

        TIME,
        COST,
        ENERGY
    }

    //Optimization Parameter
    private static final OptimizationParameter OP_PARAMETER = OptimizationParameter.TIME;

    /*
     * ***************************************************************************************************************
     * BOUNDARIES MANAGEMENT
     * ***************************************************************************************************************
     */
    private static final long TIME_BOUNDARY = Long.MAX_VALUE;
    private static final long ENERGY_BOUNDARY = Long.MAX_VALUE;
    private static final long MONETARY_BOUNDARY = Long.MAX_VALUE;
    private static final long POWER_BOUNDARY = Long.MAX_VALUE;
    private static final long PRICE_BOUNDARY = Long.MAX_VALUE;

    public static OptimizationParameter getSchedulerOptimization() {
        return OP_PARAMETER;
    }

    public static long getTimeBoundary() {
        return TIME_BOUNDARY;
    }

    public static double getEconomicBoundary() {
        return ENERGY_BOUNDARY;
    }

    public static double getMonetaryBoundary() {
        return MONETARY_BOUNDARY;
    }

    public static double getPowerBoundary() {
        return POWER_BOUNDARY;
    }

    public static double getPriceBoundary() {
        return PRICE_BOUNDARY;
    }
}
