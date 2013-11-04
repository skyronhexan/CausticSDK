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

package com.teotigraphix.caustk.live;

public class MachineFactory extends CaustkSubFactoryBase {

    public MachineFactory() {
    }

    public Machine createMachine(ComponentInfo info, int machineIndex, MachineType machineType,
            String machineName) {
        Machine caustkMachine = new Machine(info, machineIndex, machineType, machineName);
        return caustkMachine;
    }

    public Machine createMachine(RackSet rackSet, int index, MachineType machineType,
            String machineName) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Machine);
        Machine caustkMachine = new Machine(info, rackSet, index, machineType, machineName);
        return caustkMachine;
    }
}
