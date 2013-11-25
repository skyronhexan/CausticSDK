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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.workstation.GrooveBoxDescriptor.PartDescriptor;

/**
 * @author Michael Schmalle
 */
public class PatternBank extends CaustkComponent {

    private transient Map<Integer, PartDescriptor> partDescriptors = new HashMap<Integer, PartDescriptor>();

    private Map<Integer, Pattern> patterns = new TreeMap<Integer, Pattern>();

    private transient GrooveBox grooveBox;

    public IDispatcher getDispatcher() {
        return grooveBox.getDispatcher();
    }

    // XXX temp
    RackSet getRackSet() {
        return grooveBox.getRackSet();
    }

    private transient int pendingPattern = -1;

    /**
     * The pending pattern index.
     */
    public int getPendingPattern() {
        return pendingPattern;
    }

    /**
     * Sets the pending pattern index, queues the next pattern to be played from
     * the bank.
     * 
     * @param value The next pattern index (0..63).
     * @see OnPatternBankChange
     * @see PatternBankChangeKind#PendingIndex
     */
    public void setPendingPattern(int value) {
        if (value == pendingPattern)
            return;
        pendingPattern = value;
        getDispatcher().trigger(
                new OnPatternBankChange(this, PatternBankChangeKind.PendingIndex, pendingPattern));
    }

    private transient Pattern temporaryPattern;

    public Pattern getTemporaryPattern() {
        return temporaryPattern;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(101)
    private String patternTypeId;

    @Tag(102)
    private Map<Integer, PatternReference> patternReferences = new TreeMap<Integer, PatternReference>();

    @Tag(103)
    private int selectedIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return getInfo().getName();
    }

    //----------------------------------
    // patternTypeId
    //----------------------------------

    /**
     * Returns the type of {@link Pattern} this bank holds.
     * <p>
     * A {@link Pattern} can only hold one type of {@link Part} structure, this
     * type is used when loading {@link PatternBank}s into top level machines,
     * if the pattern type does not match the top level machine's pattern type,
     * the {@link PatternBank} is incompatible and will not be able to be used
     * to load patterns into the top level machine.
     * <p>
     * The pattern type is a 4 character unique identifier used when the naming
     * the {@link Part}s initially created for the {@link Pattern}.
     */
    public String getPatternTypeId() {
        return patternTypeId;
    }

    //---------------------------------- 
    // grooveBox
    //----------------------------------

    public GrooveBox getGrooveBox() {
        return grooveBox;
    }

    void setGrooveBox(GrooveBox value) {
        grooveBox = value;
    }

    //---------------------------------- 
    // parts
    //----------------------------------

    public Collection<Part> getParts() {
        return grooveBox.getParts();
    }

    //----------------------------------
    // patterns
    //----------------------------------

    public Collection<Pattern> getPatterns() {
        return patterns.values();
    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param value
     * @see OnPatternBankChange
     * @see PatternBankChangeKind#SelectedIndex
     */
    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        //        int oldIndex = selectedIndex;
        selectedIndex = value;
        //        getDispatcher().trigger(
        //                new OnPatternBankChange(this, PatternBankChangeKind.SelectedIndex, selectedIndex,
        //                        oldIndex));
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    PatternBank() {
    }

    PatternBank(ComponentInfo info, GrooveBox grooveBox) {
        setInfo(info);
        this.grooveBox = grooveBox;
        this.patternTypeId = grooveBox.getPatternTypeId();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns the {@link Part} at the specified index.
     * 
     * @param index The part index (0..13).
     */
    public Part getPart(int index) {
        return grooveBox.getPart(index);
    }

    /**
     * Returns the {@link Pattern} at the linear index (0..62).
     * 
     * @param index The linear index.
     */
    //    public Pattern getPattern(int index) {
    //        return patterns.get(index);
    //    }

    /**
     * Returns the {@link Pattern} at the specific bank and pattern index.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    //    public Pattern getPattern(int bankIndex, int patternIndex) {
    //        return patterns.get(PatternUtils.getIndex(bankIndex, patternIndex));
    //    }

    /**
     * Returns the {@link Pattern} as the specific named position, e.g.
     * <code>A01</code> or <code>C13</code> etc.
     * 
     * @param patternName The named pattern position.
     */
    //    public Pattern getPattern(String patternName) {
    //        return patterns.get(PatternUtils.getIndex(PatternUtils.toBank(patternName),
    //                PatternUtils.toPattern(patternName)));
    //    }

    /**
     * Increments and returns the next pattern (0..63), when 64 is reached, the
     * index wraps around to 0.
     * 
     * @see #setPendingPattern(int)
     */
    public void incrementIndex() {
        int target = selectedIndex;
        if (pendingPattern != -1)
            target = pendingPattern;
        int index = target + 1;
        if (index > 63)
            index = 0;
        setPendingPattern(index);
    }

    /**
     * Decrements and returns the next pattern (0..63), when 0 is reached, the
     * index wraps around to 63.
     * 
     * @see #setPendingPattern(int)
     */
    public void decrementIndex() {
        int target = selectedIndex;
        if (pendingPattern != -1)
            target = pendingPattern;
        int index = target - 1;
        if (index < 0)
            index = 63;
        setPendingPattern(index);
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onLoad(ICaustkApplicationContext context) {
        super.onLoad(context);
    }

    @Override
    public void onSave(ICaustkApplicationContext context) {
        super.onSave(context);
        // XXX
        //        // Update the saveToPattern with the temporaryPattern's Patch and Phrase
        //        Pattern saveToPattern = patterns.get(temporaryPattern.getIndex());
        //        // the partReference's Patch and Phrase reference the Part's 
        //        for (Part part : grooveBox.getParts()) {
        //            saveToPattern.getPartReference(part).update(context, part, saveToPattern.getIndex());
        //        }
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Connect:
                break;
            case Create:
                @SuppressWarnings("unused")
                final ICaustkFactory factory = context.getFactory();

                // we copy the parts from the groove baxk to pattern bank
                for (PartDescriptor descriptor : grooveBox.getDescriptor().getParts()) {
                    PartDescriptor newDescriptor = new PartDescriptor(descriptor);
                    partDescriptors.put(descriptor.getIndex(), newDescriptor);
                }

                // create all 64 PatternReference instances for the initial set
                // no Parts are defined until createPart is called
                for (int i = 0; i < 64; i++) {
                    PatternReference patternReference = new PatternReference(i, this);
                    patternReferences.put(i, patternReference);
                }

                break;

            case Load:

                break;
            case Update:
                // send BLANKRACK message
                //                context.getRack().setRackSet(rackSet);
                //                for (Part part : parts.values()) {
                //                    part.update(context);
                //                }
                for (Pattern pattern : patterns.values()) {
                    pattern.update(context);
                }
                break;
            case Restore:
                break;
            case Disconnect:
                break;
        }
    }

    public void setNextPattern(int index) {
        pendingPattern = index;
        commitPendingPattern(grooveBox.getRackSet().getFactory());
        pendingPattern = -1;
    }

    private void commitPendingPattern(ICaustkFactory factory) {
        Pattern lastTempPattern = temporaryPattern;

        int pending = pendingPattern;

        // Get the Pattern that has the previously saved data
        Pattern sourcePattern = patterns.get(pending);
        // temporarily disconnect the Pattern
        sourcePattern.setPatternBank(null);

        // Copy the source Pattern into the temporary Pattern
        temporaryPattern = factory.getKryo().copy(sourcePattern);

        // set references
        sourcePattern.setPatternBank(this);
        temporaryPattern.setPatternBank(this);

        // Pattern bank/pattern
        int bankIndex = temporaryPattern.getBankIndex();
        int patternIndex = temporaryPattern.getPatternIndex();

        if (lastTempPattern != null) {
            // Give back the original Patch and Phrase
            for (Part part : grooveBox.getParts()) {
                Machine machine = part.getMachine();
                PartReference partReference = patterns.get(lastTempPattern.getIndex())
                        .getPartReference(part);
                machine.replacePatch(partReference.getPatch());
                machine.replacePhrase(partReference.getPhrase());
            }
        }

        // For each Part in the GrooveBox use the part reference to update 
        // the Part's Machine Patch, Phrase
        for (Part part : grooveBox.getParts()) {
            Machine machine = part.getMachine();
            PartReference partReference = temporaryPattern.getPartReference(part);

            machine.setCurrentBankPattern(bankIndex, patternIndex);

            machine.setPatch(partReference.getPatch());
            machine.setPhrase(partReference.getPhrase());
        }

        setSelectedIndex(pendingPattern);

        getDispatcher().trigger(
                new OnPatternBankChange(this, PatternBankChangeKind.PatternChange, selectedIndex));
    }

    void addPattern(Pattern pattern) {
        patterns.put(pattern.getIndex(), pattern);
        patternAdd(pattern);
    }

    private void patternAdd(Pattern pattern) {
        // TODO Auto-generated method stub

    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    public enum PatternBankChangeKind {

        PatternAdd,

        PatternRemove,

        PatternReplace,

        /**
         * Dispatched when the pending pattern has been committed to the
         * {@link PatternBank#getTemporaryPattern()}.
         */
        PatternChange,

        /**
         * Dispatched when the pending pattern is changed but has not been
         * committed.
         */
        PendingIndex,
    }

    /**
     * @author Michael Schmalle
     * @see GrooveBox#getDispatcher()
     */
    public static class OnPatternBankChange {

        private PatternBank patternBank;

        private PatternBankChangeKind kind;

        private int index;

        private int oldIndex;

        public PatternBank getPatternBank() {
            return patternBank;
        }

        public PatternBankChangeKind getKind() {
            return kind;
        }

        /**
         * @see PatternBankChangeKind#SelectedIndex
         */
        public int getIndex() {
            return index;
        }

        /**
         * @see PatternBankChangeKind#SelectedIndex
         */
        public int getOldIndex() {
            return oldIndex;
        }

        public OnPatternBankChange(PatternBank patternBank, PatternBankChangeKind kind) {
            this.patternBank = patternBank;
            this.kind = kind;
        }

        public OnPatternBankChange(PatternBank patternBank, PatternBankChangeKind kind,
                int pendingPattern) {
            this.patternBank = patternBank;
            this.kind = kind;
            this.index = pendingPattern;
        }

        public OnPatternBankChange(PatternBank patternBank, PatternBankChangeKind kind, int index,
                int oldIndex) {
            this.patternBank = patternBank;
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }
    }

}
