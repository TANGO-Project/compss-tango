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


package es.bsc.compss.types.colors;

public class ColorNode {

    private final String fillColor;
    private final String fontColor;


    public ColorNode(String fillColor, String fontColor) {
        this.fillColor = fillColor;
        this.fontColor = fontColor;
    }

    public String getFillColor() {
        return this.fillColor;
    }

    public String getFontColor() {
        return this.fontColor;
    }

}
