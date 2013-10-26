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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.mixer.MasterMixer;

public class CastkMasterMixer {

    @Tag(0)
    private CaustkScene caustkScene;

    @Tag(1)
    private MasterMixer masterMixer;

    public CaustkScene getCaustkScene() {
        return caustkScene;
    }

    CastkMasterMixer() {
    }

    CastkMasterMixer(CaustkScene caustkScene) {
        this.caustkScene = caustkScene;
    }

    public void load(CaustkLibraryFactory factory) {
        final IRack rack = factory.getRack();

        masterMixer = new MasterMixer(rack);
        masterMixer.restore(rack);
        rack.getSoundMixer().setMasterMixer(masterMixer);
    }
}