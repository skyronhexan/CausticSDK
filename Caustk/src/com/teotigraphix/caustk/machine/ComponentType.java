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

package com.teotigraphix.caustk.machine;

import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.mixer.MasterDelay;
import com.teotigraphix.caustk.rack.mixer.MasterEqualizer;
import com.teotigraphix.caustk.rack.mixer.MasterLimiter;
import com.teotigraphix.caustk.rack.mixer.MasterReverb;

/**
 * @author Michael Schmalle
 */
public enum ComponentType {

    /**
     * A {@link CaustkLibrary} holds {@link CaustkScene}, {@link CaustkMachine},
     * {@link CaustkPatch}, {@link CaustkEffect}, {@link CaustkPhrase},
     * {@link CastkMasterMixer} and {@link CaustkMasterSequencer} components.
     * <p>
     * Extension: <strong>.clb</strong>
     */
    Library("clb"),

    /**
     * A {@link CaustkScene} holds {@link CaustkMachine} components.
     * <p>
     * Extension: <strong>.csc</strong>
     */
    Scene("csc"),

    /**
     * A {@link CaustkMachine} holds one {@link CaustkPatch} and multiple
     * {@link CaustkPhrase} components.
     * <p>
     * Extension: <strong>.cmc</strong>
     */
    Machine("cmc"),

    /**
     * A {@link CaustkPatch} holds one {@link MachinePreset}, one
     * {@link MixerPreset} and up to two {@link CaustkEffect} components.
     * <p>
     * Extension: <strong>.cpt</strong>
     */
    Patch("cpt"),

    /**
     * A {@link CaustkEffect} hold up to 2 live or serialized {@link IEffect}
     * components.
     * <p>
     * Extension: <strong>.cef</strong>
     */
    Effect("cef"),

    /**
     * A {@link CaustkPhrase} holds a
     * <p>
     * Extension: <strong>.cph</strong>
     */
    Phrase("cph"),

    /**
     * A {@link CastkMasterMixer} holds a live or serialized {@link MasterMixer}
     * which in turn contains a {@link MasterDelay}, {@link MasterReverb},
     * {@link MasterEqualizer} and {@link MasterLimiter} component.
     * <p>
     * Extension: <strong>.cmx</strong>
     */
    MasterMixer("cmx"),

    /**
     * <p>
     * Extension: <strong>.csq</strong>
     */
    MasterSequencer("csq");

    private String extension;

    public String getExtension() {
        return extension;
    }

    ComponentType(String extension) {
        this.extension = extension;
    }
}