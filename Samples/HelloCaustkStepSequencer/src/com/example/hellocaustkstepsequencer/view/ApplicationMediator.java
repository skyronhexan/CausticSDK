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

package com.example.hellocaustkstepsequencer.view;

import android.app.Activity;

import com.example.hellocaustkstepsequencer.model.SoundModel;

/**
 * @author Michael Schmalle
 */
public class ApplicationMediator {

    @SuppressWarnings("unused")
    private Activity activity;

    private SoundModel soundModel;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationMediator(Activity activity, SoundModel soundModel) {
        this.activity = activity;
        this.soundModel = soundModel;
    }

    public void onAttach() {
        // load a preset we know exists from the defaults
        soundModel.loadPreset(0, "CLASSIC GROWL");
    }
}