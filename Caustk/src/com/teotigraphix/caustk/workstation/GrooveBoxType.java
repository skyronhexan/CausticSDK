////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.workstation;

/**
 * @author Michael Schmalle
 */
public enum GrooveBoxType {

    /**
     * A groove machine with the bassline parts.
     */
    BasslineMachine2("bm2", "bassline_machine2"),

    /**
     * A groove machine with 2 beatbox parts.
     */
    DrumMachine2("dm2", "drum_machine2");

    private final String type;

    public String getType() {
        return type;
    }

    private final String patternType;

    public String getPatternType() {
        return patternType;
    }

    GrooveBoxType(String type, String patternType) {
        this.type = type;
        this.patternType = patternType;
    }
}