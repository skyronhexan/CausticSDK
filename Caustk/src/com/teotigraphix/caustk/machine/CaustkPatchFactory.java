
package com.teotigraphix.caustk.machine;

import java.io.IOException;
import java.util.UUID;

import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;

public class CaustkPatchFactory {

    private IRack rack;

    public CaustkPatchFactory(IRack rack) {
        this.rack = rack;
    }

    /**
     * Creates a {@link CaustkPatch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    public CaustkPatch createPatch(MachineType machineType) {
        CaustkPatch livePatch = new CaustkPatch(UUID.randomUUID(), machineType);
        return livePatch;
    }

    /**
     * Creates a new {@link CaustkPatch}, assigns the {@link CaustkMachine}.
     * 
     * @param machine A {@link CaustkMachine} that does not exist in the native
     *            rack.
     */
    public CaustkPatch createPatch(CaustkMachine machine) {
        CaustkPatch livePatch = new CaustkPatch(UUID.randomUUID(), machine);
        return livePatch;
    }

    /**
     * Activates the patch, creating the {@link MachinePreset} and
     * <p>
     * - Creates and assigns the bytes for the {@link MachinePreset}.
     * <p>
     * - Creates and assigns the {@link CaustkPatch} which will then create 0-2
     * {@link CaustkEffect}s. When the {@link CaustkEffect} is created, only the
     * {@link EffectType} is saved and slot index. The {@link IEffect} instance
     * is not restored at this point.
     * 
     * @param livePatch
     * @throws IOException
     */
    public void activatePatch(CaustkPatch caustkPatch) throws IOException {
        CaustkLibraryUtils.assignAndUpdatePresetFile(caustkPatch.getMachine(), caustkPatch, rack);
        CaustkLibraryUtils.assignEffects(caustkPatch.getMachine(), caustkPatch, rack);
    }
}
