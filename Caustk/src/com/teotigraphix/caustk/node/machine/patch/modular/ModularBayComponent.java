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

package com.teotigraphix.caustk.node.machine.patch.modular;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The modular synth bay component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ModularBayComponent extends MachineComponent {

    // TODO think about the further impl of ModularBayComponent
    // for now the Modular supports preset loading and dynamic creation but no
    // reserialization of node graph

    private Map<Integer, IModularComponent> components = new HashMap<Integer, IModularComponent>();

    public Map<Integer, IModularComponent> getComponents() {
        return components;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ModularBayComponent() {
    }

    public ModularBayComponent(MachineNode machineNode) {
        super(machineNode);
    }

    /**
     * @param type
     * @param bay
     * @return
     */
    public ModularComponentBase create(ModularComponentType type, int bay) {
        ModularComponentBase component = null;
        int machineIndex = getMachineIndex();
        switch (type) {
            case TwoToOneMixerModulator:
                component = new TwoInputMixer(bay);
                component.setMachineIndex(machineIndex);
                break;
            case ThreeToOneMixer:
                component = new ThreeInputMixer(bay);
                component.setMachineIndex(machineIndex);
                break;
            case SixToOneMixer:
                component = new SixInputMixer(bay);
                component.setMachineIndex(machineIndex);
                break;
            case WaveformGenerator:
                component = new WaveformGenerator(bay);
                component.setMachineIndex(machineIndex);
                break;
            case SubOscillator:
                component = new SubOscillator(bay);
                component.setMachineIndex(machineIndex);
                break;
            case PulseGenerator:
                component = new PulseGenerator(bay);
                component.setMachineIndex(machineIndex);
                break;
            case DADSREnvelope:
                component = new DADSREnvelope(bay);
                component.setMachineIndex(machineIndex);
                break;
            case AREnvelope:
                component = new AREnvelope(bay);
                component.setMachineIndex(machineIndex);
                break;
            case DecayEnvelope:
                component = new DecayEnvelope(bay);
                component.setMachineIndex(machineIndex);
                break;
            case SVFilter:
                component = new SVFilter(bay);
                component.setMachineIndex(machineIndex);
                break;
            case ResonantLP:
                component = new ResonantLP(bay);
                component.setMachineIndex(machineIndex);
                break;
            case FormantFilter:
                component = new FormantFilter(bay);
                component.setMachineIndex(machineIndex);
                break;
            case MiniLFO:
                component = new MiniLFO(bay);
                component.setMachineIndex(machineIndex);
                break;
            case NoiseGenerator:
                component = new NoiseGenerator(bay);
                component.setMachineIndex(machineIndex);
                break;
            case PanModule:
                component = new PanModule(bay);
                component.setMachineIndex(machineIndex);
                break;
            case CrossFade:
                component = new Crossfader(bay);
                component.setMachineIndex(machineIndex);
                break;
            case LagProcessor:
                component = new LagProcessor(bay);
                component.setMachineIndex(machineIndex);
                break;
            case Delay:
                component = new DelayModule(bay);
                component.setMachineIndex(machineIndex);
                break;
            case SampleAndHold:
                component = new SampleAndHold(bay);
                component.setMachineIndex(machineIndex);
                break;
            case CrossOver:
                component = new CrossoverModule(bay);
                component.setMachineIndex(machineIndex);
                break;
            case Saturator:
                component = new Saturator(bay);
                component.setMachineIndex(machineIndex);
                break;
            case FMPair:
                component = new FMPair(bay);
                component.setMachineIndex(machineIndex);
                break;
            case Arpeggiator:
                component = new Arpeggiator(bay);
                component.setMachineIndex(machineIndex);
                break;

            default:
                break;
        }
        ModularMessage.CREATE.send(getRack(), machineIndex, bay, type.getValue());
        return component;
    }

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 16; i++) {
            int component = (int)ModularMessage.TYPE.send(getRack(), 0, i);
            if (component != 0) {
                ModularComponentType type = ModularComponentType.fromInt(component);
                ModularComponentBase comp = create(type, i);
                components.put(i, comp);
                if (comp.getNumBays() > 1)
                    i++;
            }
        }
        for (IModularComponent component : components.values()) {
            component.restore();
        }
    }
}
