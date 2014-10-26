////////////////////////////////////////////////////////////////////////////////
//Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectControls;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link AutoWahEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class AutoWahEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float cutoff = 2.23f;

    @Tag(201)
    private float depth = 1f;

    @Tag(202)
    private float resonance = 0.5f;

    @Tag(203)
    private float speed = 0.4f;

    @Tag(204)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    /**
     * @see EffectControls#Autowah_Cutoff
     */
    public float getCutoff() {
        return cutoff;
    }

    public float queryCutoff() {
        return get(EffectControls.Autowah_Cutoff);
    }

    /**
     * @see EffectControls#Autowah_Cutoff
     */
    public void setCutoff(float cutoff) {
        if (!EffectControls.Autowah_Cutoff.set(cutoff, this.cutoff))
            return;
        this.cutoff = cutoff;
        set(EffectControls.Autowah_Cutoff, cutoff);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see EffectControls#Autowah_Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(EffectControls.Autowah_Depth);
    }

    /**
     * @see EffectControls#Autowah_Depth
     */
    public void setDepth(float depth) {
        if (!EffectControls.Autowah_Depth.set(depth, this.depth))
            return;
        this.depth = depth;
        set(EffectControls.Autowah_Depth, depth);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    /**
     * @see EffectControls#Autowah_Resonance
     */
    public float getResonance() {
        return resonance;
    }

    public float queryResonance() {
        return get(EffectControls.Autowah_Resonance);
    }

    /**
     * @see EffectControls#Autowah_Resonance
     */
    public void setResonance(float resonance) {
        if (!EffectControls.Autowah_Resonance.set(resonance, this.resonance))
            return;
        this.resonance = resonance;
        set(EffectControls.Autowah_Resonance, resonance);
    }

    //----------------------------------
    // speed
    //----------------------------------

    /**
     * @see EffectControls#Autowah_Speed
     */
    public float getSpeed() {
        return speed;
    }

    public float querySpeed() {
        return get(EffectControls.Autowah_Speed);
    }

    /**
     * @see EffectControls#Autowah_Speed
     */
    public void setSpeed(float speed) {
        if (!EffectControls.Autowah_Speed.set(speed, this.speed))
            return;
        this.speed = speed;
        set(EffectControls.Autowah_Speed, speed);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#Autowah_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Autowah_Wet);
    }

    /**
     * @see EffectControls#Autowah_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Autowah_Wet.set(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Autowah_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public AutoWahEffect() {
    }

    public AutoWahEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Autowah);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Autowah_Cutoff, cutoff);
        set(EffectControls.Autowah_Depth, depth);
        set(EffectControls.Autowah_Resonance, resonance);
        set(EffectControls.Autowah_Speed, speed);
        set(EffectControls.Autowah_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setCutoff(queryCutoff());
        setDepth(queryDepth());
        setResonance(queryResonance());
        setSpeed(querySpeed());
        setWet(queryWet());
    }
}
