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

package com.teotigraphix.caustk.controller.core;

import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;

/**
 * @author Michael Schmalle
 */
public final class CaustkApplication implements ICaustkApplication {

    //--------------------------------------------------------------------------
    // Public ICaustkApplication API
    //--------------------------------------------------------------------------

    //----------------------------------
    // applicationHandler
    //----------------------------------

    private IApplicationHandler applicationHandler;

    @Override
    public void setApplicationHandler(IApplicationHandler value) {
        applicationHandler = value;
    }

    //----------------------------------
    // configuration
    //----------------------------------

    private final ICaustkConfiguration configuration;

    @Override
    public final ICaustkConfiguration getConfiguration() {
        return configuration;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private final CaustkController controller;

    @Override
    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param configuration The main application's configuration instance.
     */
    public CaustkApplication(ICaustkConfiguration configuration) {
        this.configuration = configuration;
        controller = (CaustkController)getConfiguration().createController(this);
    }

    //--------------------------------------------------------------------------
    // Public ICaustkApplication API
    //--------------------------------------------------------------------------

    @Override
    public final void create() {
        CtkDebug.log("Application", "create()");
        // creates all sub components of the controller
        controller.create();
        if (applicationHandler != null)
            applicationHandler.commitCreate();
        fireStateChange(StateChangeKind.Create);
    }

    @Override
    public final void save() throws IOException {
        CtkDebug.log("Application", "save()");
        controller.save();
        if (applicationHandler != null)
            applicationHandler.commitSave();
        fireStateChange(StateChangeKind.Save);
    }

    @Override
    public final void close() {
        CtkDebug.log("Application", "close()");
        controller.close();
        if (applicationHandler != null)
            applicationHandler.commitClose();
        fireStateChange(StateChangeKind.Close);
    }

    private void fireStateChange(StateChangeKind kind) {
        getController().trigger(new OnCausticApplicationStateChange(kind));
    }

}
