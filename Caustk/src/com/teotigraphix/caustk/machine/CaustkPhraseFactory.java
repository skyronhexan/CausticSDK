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

import java.util.UUID;

import com.teotigraphix.caustk.rack.IRack;

public class CaustkPhraseFactory {

    private IRack rack;

    public CaustkPhraseFactory(IRack rack) {
        this.rack = rack;
    }

    public CaustkPhrase createPhrase(MachineType machineType, int bankIndex, int patternIndex) {
        final int index = bankIndex * patternIndex;
        CaustkPhrase caustkPhrase = new CaustkPhrase(UUID.randomUUID(), index, machineType);
        return caustkPhrase;
    }

    public CaustkPhrase createPhrase(CaustkMachine caustkMachine, int bankIndex, int patternIndex) {
        final int index = bankIndex * patternIndex;
        CaustkPhrase caustkPhrase = new CaustkPhrase(UUID.randomUUID(), index, caustkMachine);
        return caustkPhrase;
    }
}