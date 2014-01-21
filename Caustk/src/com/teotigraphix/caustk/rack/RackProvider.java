////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.rack;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class RackProvider {

    private static Rack rack;

    static void setRack(Rack rack) {
        RackProvider.rack = rack;
    }

    /**
     * Returns the single instance of the {@link Rack}.
     */
    public static Rack getRack() {
        return rack;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public RackProvider() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Factory method, creates a {@link Rack} with {@link CaustkRuntime}.
     * 
     * @param runtime The platform runtime.
     */
    public static Rack createRack(CaustkRuntime runtime) {
        return new Rack(runtime);
    }
}
