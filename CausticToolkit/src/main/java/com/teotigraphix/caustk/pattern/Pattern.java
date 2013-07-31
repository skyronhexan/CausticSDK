
package com.teotigraphix.caustk.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;

import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPattern;
import com.teotigraphix.caustk.sequencer.SystemSequencer;

public class Pattern {
    
    private transient ICaustkController controller;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    /**
     * Dispatch an event through the {@link ICaustkController#getDispatcher()}.
     * 
     * @param event
     */
    public void dispatch(Object event) {
        controller.getDispatcher().trigger(event);
    }

    //----------------------------------
    // patternItem
    //----------------------------------

    private LibraryPattern patternItem;

    /**
     * The {@link LibraryPattern} data.
     * <p>
     * The info will be set as the pattern is being created by the provider.
     * 
     * @see PatternProvider#get(int)
     */
    public LibraryPattern getPatternItem() {
        return patternItem;
    }

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The location within the memory's bank that this pattern is located.
     */
    public int getIndex() {
        return patternItem.getIndex();
    }

    //----------------------------------
    // parts
    //----------------------------------

    private ArrayList<Part> parts = new ArrayList<Part>();

    public int getPartCount() {
        return parts.size();
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public Part getPart(int index) {
        return parts.get(index);
    }

    //----------------------------------
    // tempo
    //----------------------------------

    private float tempo;

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float value) {
        tempo = value;
        //controller.api(SequencerAPI.class).setTempo(value);
        controller.getSystemSequencer().setTempo(value);
    }

    //----------------------------------
    // length
    //----------------------------------
    
    // start off with invalid length, must be set so Phrase is updated with num steps
    private int length = -1;

    public int getLength() {
        return length;
    }

    public void setLength(int value) {
        if (length == value)
            return;

        length = value;

        for (Part part : parts) {
            part.getPhrase().setLength(value);
        }
    }

    //----------------------------------
    // selectedPart
    //----------------------------------

    private Part selectedPart;

    /**
     * The selected part of the pattern.
     */
    public Part getSelectedPart() {
        return selectedPart;
    }

    /**
     * Sets the selected part of the pattern using the index.
     * 
     * @param index The part index to be selected.
     * @see OnPatternSelectedPartChange
     * @see Pattern#setSelectedPart(Part)
     */
    public void setSelectedPart(int index) {
        setSelectedPart(getPart(index));
    }

    /**
     * Sets the selected part of the pattern.
     * 
     * @param value The part to be selected.
     * @see OnPatternSelectedPartChange
     */
    public void setSelectedPart(Part value) {
        if (selectedPart == value)
            return;

        Part oldPart = selectedPart;
        selectedPart = value;

        dispatch(new OnPatternSelectedPartChange(selectedPart, oldPart));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Pattern(ICaustkController controller, LibraryItem patternItem) {
        this.controller = controller;
// XXX        this.patternItem = patternItem;
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public void addPart(Part part) {
        if (parts.contains(part))
            return;
        parts.add(part);
    }

    //--------------------------------------------------------------------------
    // Observer Event API
    //--------------------------------------------------------------------------

    /**
     * Dispatched when the {@link Pattern#setSelectedPart(Part)} is called.
     * 
     * @see SystemSequencer#getDispatcher()
     */
    public static class OnPatternSelectedPartChange {
        private Part part;

        private Part oldPart;

        public final Part getPart() {
            return part;
        }

        public final Part getOldPart() {
            return oldPart;
        }

        public OnPatternSelectedPartChange(Part part, Part oldPart) {
            this.part = part;
            this.oldPart = oldPart;
        }
    }

}