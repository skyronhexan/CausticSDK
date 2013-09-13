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

package com.teotigraphix.caustk.gs.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.system.SystemSequencer;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

// XXX You have to decide if you are going to proxy the patternsequencer
// api through the Tone or call it straight from this class
// what makes more sense with UML and no access violations?

public class Phrase {

    public enum Scale {
        SIXTEENTH, SIXTEENTH_TRIPLET, THIRTYSECOND, THIRTYSECOND_TRIPLET
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // libraryPhrase
    //----------------------------------

    private LibraryPhrase libraryPhrase;

    /**
     * The {@link LibraryPhrase} assigned when the {@link Phrase}
     * was created.
     */
    public LibraryPhrase getLibraryPhrase() {
        return libraryPhrase;
    }

    //----------------------------------
    // scale
    //----------------------------------

    private Scale scale = Scale.SIXTEENTH;

    public Scale getScale() {
        return scale;
    }

    /**
     * Sets the new scale for the phrase.
     * <p>
     * The scale is used to calculate the {@link Resolution} of input. This
     * property mainly relates the the view of the phrase, where the underlying
     * pattern sequencer can have a higher resolution but the view is showing
     * the scale.
     * <p>
     * So you can have a phrase scale of 16, but the resolution could be 64th,
     * but the view will only show the scale notes which would be 16th.
     * 
     * @param value
     * @see OnPhraseScaleChange
     */
    public void setScale(Scale value) {
        if (scale == value)
            return;
        //Scale oldScale = scale;
        scale = value;
        //        getPart().getPattern().dispatch(new OnPhraseScaleChange(scale, oldScale));
    }

    //----------------------------------
    // part
    //----------------------------------

    private Part part;

    public Part getPart() {
        return part;
    }

    //----------------------------------
    // position
    //----------------------------------

    private int position = 1;

    /**
     * Returns the current position in the phrase based on the rules of the
     * view.
     * <p>
     * Depending on the resolution and scale, the position can mean different
     * things.
     * <p>
     * 16th note with a length of 4, has 4 measures and thus 4 positions(1-4).
     * When the position is 2, the view triggers are 16-31(0 index).
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the current position of the phrase.
     * 
     * @param value The new phrase position.
     * @see OnPhrasePositionChange
     */
    void setPosition(int value) {
        if (position == value)
            return;
        // if p = 1 and len = 1
        if (value < 0 || value > getLength())
            return;
        //int oldPosition = position;
        position = value;
        //        getPart().getPattern().dispatch(new OnPhrasePositionChange(position, oldPosition));
    }

    //----------------------------------
    // length
    //----------------------------------

    public int getLength() {
        return getPatternSequencer().getLength();
    }

    /**
     * Sets the phrase's new length.
     * <p>
     * The {@link Pattern#setLength(int)} will set this and when a
     * {@link Phrase} is committed, the length of the owning
     * pattern is used.
     * <p>
     * This should never be set from other than the {@link Pattern}.
     * 
     * @param value
     * @see OnPhraseLengthChange
     */
    public void setLength(int value) {
        int oldValue = getLength();
        if (oldValue == value && map.size() != 0)
            return;

        if (position > value)
            setPosition(value);

        // we 'could' do an event back the the sequencer here but it will
        // just complicate the friggin code right now so I have decided to
        // create a Facade on the SynthTone and call it directly.
        // Doing this would in the future, if I ever wanted to abstract and
        // create events for this easy, we just take all facade methods and
        // create events for them
        // --> ((SynthTone) getPart().getTone()).setLength(value);

        getPatternSequencer().setLength(value);

        updateTriggers(value, oldValue);

        //        getPart().getPattern().dispatch(new OnPhraseLengthChange(value, oldValue));
    }

    /**
     * Update the length of the tirggers.
     * <p>
     * Only add to the map, triggers are never removed in this version.
     * 
     * @param oldLength
     * @param length
     */
    private void updateTriggers(int length, int oldLength) {
        if (length <= oldLength && map.size() != 0)
            return;

        if (oldLength == -1 || map.size() == 0) {
            int len = Resolution.toSteps(getResolution()) * length;
            // initialize all triggers to defaults using the current note scale
            for (int i = 0; i < len; i++) {
                float beat = Resolution.toBeat(i, getResolution());
                //System.out.println("b = " + beat);
                Trigger trigger = new Trigger(beat, 60, 0.25f, 1f, 0);
                map.put(beat, trigger);
            }
        } else if (length > oldLength) {
            int startLen = Resolution.toSteps(getResolution()) * oldLength;
            int len = Resolution.toSteps(getResolution()) * length;
            // initialize all triggers to defaults using the current note scale
            for (int i = startLen; i < len; i++) {
                float beat = Resolution.toBeat(i, getResolution());
                //System.out.println("b = " + beat);
                Trigger trigger = new Trigger(beat, 60, 0.25f, 1f, 0);
                map.put(beat, trigger);
            }
        }
    }

    //----------------------------------
    // resolution
    //----------------------------------

    public Resolution getResolution() {
        switch (getScale()) {
            case SIXTEENTH:
                return Resolution.SIXTEENTH;
            case THIRTYSECOND:
                return Resolution.THIRTYSECOND;
            default:
                return Resolution.SIXTYFOURTH;
        }
    }

    public void setResolution(Resolution value) {
        //getPatternSequencer().setResolution(value);
    }

    //----------------------------------
    // stepCount
    //----------------------------------

    /**
     * Returns the full number of steps in all measures.
     * <p>
     * IE 4 measures of 32nd resolution has 128 steps.
     */
    public int getStepCount() {
        int numStepsInMeasure = Resolution.toSteps(getResolution());
        return numStepsInMeasure * getLength();
    }

    /**
     * Returns all the steps in the phrase, no view calculations(position).
     * <p>
     * A copy of the collection is returned.
     */
    public List<Trigger> getSteps() {
        return new ArrayList<Trigger>(map.values());
    }

    public List<Trigger> getViewSteps() {
        // find the start (position - 1) * resolution
        int fromStep = (position - 1) * indciesInView;
        // find the end fromIndex + scale
        int toStep = endStepInView(fromStep);
        return new ArrayList<Phrase.Trigger>(map.values()).subList(fromStep, toStep);
    }

    /**
     * Returns the non view step, does not calculate its location within the
     * view.
     * 
     * @param step The absolute step position within the phrase.
     */
    public Trigger getStep(int step) {
        return map.get(Resolution.toBeat(step, getResolution()));
    }

    public void setNoteData(String data) {
        part.getTone().getComponent(PatternSequencerComponent.class).assignNoteData(data);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Phrase(Part part, LibraryPhrase phraseItem) {
        this.part = part;
        this.libraryPhrase = phraseItem;
        part.setPhrase(this);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public int toAbsoluteStep(int viewStep) {
        return viewStep + (16 * getPosition()) - 16; // 8 phrase.getResolutin()
    }

    /**
     * Triggers a note at the specified step and will not calculate the view
     * step.
     * <p>
     * This method assumes the step passed is NOT relative or the view step.
     * 
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        float beat = Resolution.toBeat(step, getResolution());

        getPatternSequencer().triggerOn(getResolution(), step, pitch, gate, velocity, flags);

        Trigger trigger = getTrigger(step);
        if (trigger == null) {
            trigger = new Trigger(beat, pitch, gate, velocity, flags);
            map.put(beat, trigger);
        } else {
            trigger.update(beat, pitch, gate, velocity, flags);
        }
        trigger.selected = true;
    }

    protected void fireChange(TriggerChangeKind kind, Trigger trigger) {
        //        getPart().getPattern().dispatch(new OnPhraseTriggerChange(kind, trigger));
    }

    Map<Float, Trigger> map = new TreeMap<Float, Trigger>();

    private Trigger getTrigger(float beat) {
        return map.get(beat);
    }

    private Trigger getTrigger(int step) {
        final float beat = Resolution.toBeat(step, getResolution());
        return getTrigger(beat);
    }

    public static class OnPhraseTriggerChange {

        private TriggerChangeKind kind;

        private Trigger trigger;

        public final TriggerChangeKind getKind() {
            return kind;
        }

        public final Trigger getTrigger() {
            return trigger;
        }

        public OnPhraseTriggerChange(TriggerChangeKind kind, Trigger trigger) {
            this.trigger = trigger;
        }
    }

    public enum TriggerChangeKind {
        RESET, PITCH, GATE, VELOCITY, FLAGS, SELECTED
    }

    public class Trigger {

        private float beat;

        private int pitch;

        private float gate;

        private float velocity;

        private int flags = 0;

        public final float getBeat() {
            return beat;
        }

        public final int getPitch() {
            return pitch;
        }

        public final float getGate() {
            return gate;
        }

        public final float getVelocity() {
            return velocity;
        }

        public final int getFlags() {
            return flags;
        }

        private boolean selected = false;

        public Trigger(float beat, int pitch, float gate, float velocity, int flags) {
            this.beat = beat;
            this.pitch = pitch;
            this.gate = gate;
            this.velocity = velocity;
            this.flags = flags;
        }

        public void update(float beat, int pitch, float gate, float velocity, int flags) {
            this.beat = beat;
            this.pitch = pitch;
            this.gate = gate;
            this.velocity = velocity;
            this.flags = flags;
        }

        /**
         * Returns the step value relative to the containing
         * {@link Phrase#getResolution()}.
         */
        public int getStep() {
            return Resolution.toStep(beat, getResolution());
        }

        public int getStep(Resolution resolution) {
            return Resolution.toStep(beat, resolution);
        }

        public boolean isSelected() {
            return selected;
        }

        @Override
        public String toString() {
            return "[" + getStep() + "] " + beat + ":" + pitch + "-" + selected;
        }
    }

    public void triggerOn(int step) {
        Trigger trigger = getStep(step);
        triggerOn(step, trigger.getPitch(), trigger.getGate(), trigger.getVelocity(),
                trigger.getFlags());
    }

    public void triggerUpdate(int step, int pitch, float gate, float velocity, int flags) {
        triggerOff(step);
        triggerOn(step, pitch, gate, velocity, flags);
    }

    public void triggerUpdatePitch(int step, int pitch) {
        Trigger trigger = getStep(step);
        triggerUpdate(step, pitch, trigger.getGate(), trigger.getVelocity(), trigger.getFlags());
    }

    public void triggerUpdateGate(int step, float gate) {
        Trigger trigger = getStep(step);
        triggerUpdate(step, trigger.getPitch(), gate, trigger.getVelocity(), trigger.getFlags());
    }

    public void triggerUpdateVelocity(int step, float velocity) {
        Trigger trigger = getStep(step);
        triggerUpdate(step, trigger.getPitch(), trigger.getGate(), velocity, trigger.getFlags());
    }

    public void triggerUpdateFlags(int step, int flags) {
        Trigger trigger = getStep(step);
        triggerUpdate(step, trigger.getPitch(), trigger.getGate(), trigger.getVelocity(), flags);
    }

    /**
     * Triggers a note off at the specified step, this is an absolute step
     * within the known trigger map's length of measures.
     * 
     * @param step The absolute step to unselect.
     */
    public void triggerOff(int step) {
        Trigger trigger = getTrigger(step);
        getPatternSequencer().triggerOff(getResolution(), step, trigger.getPitch());
        trigger.selected = false;
    }

    /**
     * Transposes ALL triggers in the phrase by the delta.
     * <p>
     * Note; This is a 'dumb' method in that it does NOT track the last
     * transposition. The calling client must keep track of the current and last
     * transpose values to correctly calculate the delta change.
     * 
     * @see OnPhraseTransposeChange
     */
    public void transpose(int delta) {
        // XXX Its going to matter if the parent is rhythm or synth eventually
        for (Trigger trigger : getSteps()) {
            triggerUpdatePitch(trigger.getStep(), trigger.getPitch() + delta);
        }
        //        getPart().getPattern().dispatch(new OnPhraseTransposeChange(delta));
    }

    /**
     * Returns whether the absolute(non view) current step is selected.
     * <p>
     * Clients MUST calculate the absolute step, this does not factor in length
     * or position.
     * 
     * @param step The step to test selection.
     */
    public boolean isSelected(int step) {
        Trigger trigger = getTrigger(step);
        if (trigger != null)
            return trigger.isSelected();
        return false;
    }

    /**
     * Increments the internal pointer of the measure position.
     */
    public void incrementPosition() {
        int len = getLength();
        int value = position + 1;
        if (value > len)
            value = len;
        setPosition(value);
    }

    /**
     * Decrement the internal pointer of the measure position.
     */
    public void decrementPosition() {
        int value = position - 1;
        if (value < 1)
            value = 1;
        setPosition(value);
    }

    public void configure() {
        setResolution(getLibraryPhrase().getResolution());
        setNoteData(getLibraryPhrase().getNoteData());
    }

    public void commit() {
        // TODO Auto-generated method stub

    }

    /**
     * @see SystemSequencer#getDispatcher()
     */
    public static class OnPhraseScaleChange {
        private Scale scale;

        private Scale oldScale;

        public Scale getScale() {
            return scale;
        }

        public Scale getOldScale() {
            return oldScale;
        }

        public OnPhraseScaleChange(Scale scale, Scale oldScale) {
            this.scale = scale;
            this.oldScale = oldScale;
        }
    }

    /**
     * @see SystemSequencer#getDispatcher()
     */
    public static class OnPhrasePositionChange {
        private int position;

        private int oldPosition;

        public int getPosition() {
            return position;
        }

        public int getOldPosition() {
            return oldPosition;
        }

        public OnPhrasePositionChange(int position, int oldPosition) {
            this.position = position;
            this.oldPosition = oldPosition;
        }
    }

    /**
     * @see SystemSequencer#getDispatcher()
     */
    public static class OnPhraseLengthChange {
        private int length;

        private int oldLength;

        public int getLength() {
            return length;
        }

        public int getOldPosition() {
            return oldLength;
        }

        public OnPhraseLengthChange(int length, int oldLength) {
            this.length = length;
            this.oldLength = oldLength;
        }
    }

    /**
     * @see SystemSequencer#getDispatcher()
     */
    public static class OnPhraseTransposeChange {
        private int delta;

        public int getDelta() {
            return delta;
        }

        public OnPhraseTransposeChange(int delta) {
            this.delta = delta;
        }
    }

    protected final PatternSequencerComponent getPatternSequencer() {
        return getPart().getTone().getComponent(PatternSequencerComponent.class);
    }

    private int indciesInView = 16;

    private int endStepInView(int fromStep) {
        if (scale == Scale.SIXTEENTH)
            return fromStep + indciesInView;
        if (scale == Scale.THIRTYSECOND)
            return fromStep + indciesInView;
        return -1;
    }

    @Override
    public String toString() {
        //        return "[Phrase("
        //                + getPart().getIndex()
        //                + ","
        //                + getPart().getTone().getMachine().getSequencer().getActiveStepPhrase()
        //                        .getStepMap().toString() + ")]";
        return "[Phrase(" + getPart().getIndex() + ")]";
    }

}