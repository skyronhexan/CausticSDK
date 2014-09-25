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

package com.teotigraphix.gdx.app;

public abstract class CaustkBehavior extends Behavior {

    @Override
    public ICaustkApplication getApplication() {
        return (ICaustkApplication)super.getApplication();
    }

    public CaustkBehavior() {
    }

    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onBeatChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    public void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    public void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior child : getChildren()) {
            ((CaustkBehavior)child).onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
        }
    }

}