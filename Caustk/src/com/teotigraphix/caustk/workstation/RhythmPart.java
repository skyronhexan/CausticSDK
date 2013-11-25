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

package com.teotigraphix.caustk.workstation;

import java.io.File;
import java.io.IOException;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.ShuffleMode;
import com.teotigraphix.caustk.rack.tone.components.beatbox.WavSamplerChannel;

/**
 * @author Michael Schmalle
 */
public class RhythmPart extends Part {

    private transient int root = 48;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private int selectedChannel;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedChannel
    //----------------------------------

    public int getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(int value) {
        selectedChannel = value;
    }

    @Override
    public boolean isRhythm() {
        return true;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    RhythmPart() {
    }

    RhythmPart(ComponentInfo info, GrooveBox grooveBox, Machine machine, int index) {
        super(info, grooveBox, machine, index);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public void triggerOn(int channel, int step, float velocity) {
        // need to use add/remove note since the beat box is polyphonic
        // can use "triggers" because we are working with pseudo channels in the sequencer
        float beat = Resolution.toBeat(step, getPhrase().getResolution());
        float gate = 0.25f;
        int pitch = toPitch(channel);
        if (getPhrase().hasNote(pitch, beat)) {
            getPhrase().getNote(pitch, beat).setSelected(true);
            int flags = 0;
            //getPhrase().triggerOn(step);
            RackTone rackTone = getMachine().getRackTone();
            rackTone.getPatternSequencer().addNote(pitch, beat, beat + gate, velocity, flags);
        } else {
            getPhrase().addNote(pitch, beat, gate, 1f, 0).setSelected(true);
        }
    }

    public void triggerOff(int channel, int step) {
        // same as triggerOn()
        float beat = Resolution.toBeat(step, getPhrase().getResolution());
        int pitch = toPitch(channel);
        if (getPhrase().hasNote(pitch, beat)) {
            getPhrase().getNote(pitch, beat).setSelected(false);
            //getPhrase().triggerOff(step);
            RackTone rackTone = getMachine().getRackTone();
            rackTone.getPatternSequencer().removeNote(pitch,
                    Resolution.toBeat(step, getPhrase().getResolution()));
        } else {

        }
    }

    @Override
    public int toPitch(int channel) {
        return root + channel;
    }

    public void setSwing(int bank, float value) {
        BeatboxTone tone = (BeatboxTone)getMachine().getRackTone();
        tone.getPatternSequencer().setShuffleMode(ShuffleMode.SIXTEENTH);
        tone.getPatternSequencer().setShuffleAmount(value);
    }

    public void loadPreset(int bank, File presetFile) throws IOException {
        RackTone rackTone = getMachine().getRackTone();
        rackTone.getSynth().loadPreset(presetFile);
        // XXX should this be allowed? getPatch().loadPreset(presetFile);
    }

    public void loadChannel(int bank, int channel, File sampleFile) {
        BeatboxTone tone = (BeatboxTone)getMachine().getRackTone();
        tone.getSampler().loadChannel(channel, sampleFile.getAbsolutePath());
    }

    public void preview(int channel) {
        BeatboxTone tone = (BeatboxTone)getMachine().getRackTone();
        //tone.getSynth().notePreview(toPitch(channel), true);
        tone.getSynth().noteOn(toPitch(channel), 1f);
    }

    public float getChannelProperty(int bank, int channel, ChannelProperty property) {
        BeatboxTone tone = (BeatboxTone)getMachine().getRackTone();
        WavSamplerChannel samplerChannel = tone.getSampler().getChannel(channel);

        if (samplerChannel == null)
            throw new IllegalStateException("WavSamplerChannel null for channel:" + channel);

        float result = Float.NaN;
        switch (property) {
            case Decay:
                result = samplerChannel.getDecay();
                break;
            case Mute:
                result = samplerChannel.isMute() ? 1f : 0f;
                break;
            case Pan:
                result = samplerChannel.getPan();
                break;
            case Punch:
                result = samplerChannel.getPunch();
                break;
            case Solo:
                result = samplerChannel.isSolo() ? 1f : 0f;
                break;
            case Tune:
                result = samplerChannel.getTune();
                break;
            case Volume:
                result = samplerChannel.getVolume();
                break;
        }
        return result;
    }

    public void setChannelProperty(int bank, int channel, ChannelProperty property, float value) {
        BeatboxTone tone = (BeatboxTone)getMachine().getRackTone();
        WavSamplerChannel samplerChannel = tone.getSampler().getChannel(channel);

        if (samplerChannel == null)
            throw new IllegalStateException("WavSamplerChannel null for channel:" + channel);

        switch (property) {
            case Decay:
                samplerChannel.setDecay(value);
                break;
            case Mute:
                samplerChannel.setMute(value == 1f ? true : false);
                break;
            case Pan:
                samplerChannel.setPan(value);
                break;
            case Punch:
                samplerChannel.setPunch(value);
                break;
            case Solo:
                samplerChannel.setSolo(value == 1f ? true : false);
                break;
            case Tune:
                samplerChannel.setTune(value);
                break;
            case Volume:
                samplerChannel.setVolume(value);
                break;
        }
    }

    public enum ChannelProperty {
        Tune,

        Punch,

        Decay,

        Pan,

        Volume,

        Mute,

        Solo;
    }

}
