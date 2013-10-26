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

import java.io.File;
import java.util.UUID;

import com.teotigraphix.caustk.rack.IRack;

public class CaustkSceneFactory {

    private IRack rack;

    public CaustkSceneFactory(IRack rack) {
        this.rack = rack;
    }

    public CaustkScene createScene(String name) {
        CaustkScene caustkScene = new CaustkScene(UUID.randomUUID(), name);
        return caustkScene;
    }

    public CaustkScene createScene(File absoluteCausticFile) {
        CaustkScene caustkScene = new CaustkScene(UUID.randomUUID(), absoluteCausticFile);
        return caustkScene;
    }

}