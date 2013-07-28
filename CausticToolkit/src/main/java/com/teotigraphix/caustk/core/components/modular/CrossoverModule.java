
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class CrossoverModule extends ModularComponentBase {

    //----------------------------------
    // frequency
    //----------------------------------

    private float frequency;

    public float getFrequency() {
        return frequency;
    }

    float getFrequency(boolean restore) {
        return getValue("frequency");
    }

    /**
     * @param value (0..1)
     */
    public void setFrequency(float value) {
        if (value == frequency)
            return;
        frequency = value;
        if (value < 0f || value > 1f)
            newRangeException("frequency", "0..1", value);
        setValue("frequency", value);
    }

    //----------------------------------
    // inGain
    //----------------------------------

    private float inGain;

    public float getInGain() {
        return inGain;
    }

    float getInGain(boolean restore) {
        return getValue("in_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setInGain(float value) {
        if (value == inGain)
            return;
        inGain = value;
        if (value < 0f || value > 1f)
            newRangeException("in_gain", "0..1", value);
        setValue("in_gain", value);
    }

    //----------------------------------
    // lowGain
    //----------------------------------

    private float lowGain;

    public float getLowGain() {
        return lowGain;
    }

    float getLowGain(boolean restore) {
        return getValue("low_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setLowGain(float value) {
        if (value == lowGain)
            return;
        lowGain = value;
        if (value < 0f || value > 1f)
            newRangeException("low_gain", "0..1", value);
        setValue("low_gain", value);
    }

    //----------------------------------
    // highGain
    //----------------------------------

    private float highGain;

    public float getHighGain() {
        return highGain;
    }

    float getHighGain(boolean restore) {
        return getValue("high_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setHighGain(float value) {
        if (value == highGain)
            return;
        highGain = value;
        if (value < 0f || value > 1f)
            newRangeException("high_gain", "0..1", value);
        setValue("high_gain", value);
    }

    public CrossoverModule() {
    }

    public CrossoverModule(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 0;
    }

    public enum CrossoverModuleJack implements IModularJack {

        InInput(0),

        OutLow(0),

        OutHigh(1);

        private int value;

        public final int getValue() {
            return value;
        }

        CrossoverModuleJack(int value) {
            this.value = value;
        }
    }
}