
package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMAlgorithm;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl;
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.AutoWahEffect;
import com.teotigraphix.caustk.node.effect.BitcrusherEffect;
import com.teotigraphix.caustk.node.effect.CabinetSimulatorEffect;
import com.teotigraphix.caustk.node.effect.ChorusEffect;
import com.teotigraphix.caustk.node.effect.CombFilterEffect;
import com.teotigraphix.caustk.node.effect.CompressorEffect;
import com.teotigraphix.caustk.node.effect.DelayEffect;
import com.teotigraphix.caustk.node.effect.DistortionEffect;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.effect.FlangerEffect;
import com.teotigraphix.caustk.node.effect.LimiterEffect;
import com.teotigraphix.caustk.node.effect.MultiFilterEffect;
import com.teotigraphix.caustk.node.effect.ParametricEQEffect;
import com.teotigraphix.caustk.node.effect.PhaserEffect;
import com.teotigraphix.caustk.node.effect.ReverbEffect;
import com.teotigraphix.caustk.node.effect.StaticFlangerEffect;
import com.teotigraphix.caustk.node.effect.VinylSimulatorEffect;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.FMSynthMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.node.machine.patch.beatbox.WavSamplerChannel;
import com.teotigraphix.caustk.node.machine.patch.beatbox.WavSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.FMControlsComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.FMOperatorComponent;
import com.teotigraphix.caustk.node.machine.patch.fmsynth.LFOComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerChannel;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMTunerComponent;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO2Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc2Component;
import com.teotigraphix.caustk.node.machine.sequencer.ClipComponent;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode;

/*
TaggedFieldSerializer only serializes fields that have a @Tag annotation. 
This is less flexible than FieldSerializer, which can handle most classes 
without needing annotations, but allows TaggedFieldSerializer to support 
adding new fields without invalidating previously serialized bytes. If a 
field is removed it will invalidate previously serialized bytes, so fields 
should be annotated with @Deprecated instead of being removed.
*/

public class CaustkRackSerializer implements ICaustkRackSerializer {

    private Kryo kryo;

    @Override
    public Kryo getKryo() {
        return kryo;
    }

    CaustkRackSerializer() {

        kryo = new Kryo();

        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.setRegistrationRequired(true);

        kryo.register(byte[].class);
        kryo.register(boolean[].class);
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(ArrayList.class);
        kryo.register(TreeMap.class);
        kryo.register(HashMap.class);

        kryo.register(CaustkProject.class);

        kryo.register(MachineType.class);
        kryo.register(NodeMetaData.class);

        // RackNode
        kryo.register(RackNode.class);

        kryo.register(MasterNode.class);
        kryo.register(MasterDelayNode.class);
        kryo.register(MasterReverbNode.class);
        kryo.register(MasterEqualizerNode.class);
        kryo.register(MasterLimiterNode.class);
        kryo.register(MasterVolumeNode.class);

        kryo.register(SequencerNode.class);
        kryo.register(SequencerNode.ExportLoopMode.class);
        kryo.register(SequencerNode.ExportType.class);
        kryo.register(SequencerNode.SequencerMode.class);
        kryo.register(SequencerNode.ShuffleMode.class);
        kryo.register(SequencerNode.SongEndMode.class);

        kryo.register(PatternNode.class);
        kryo.register(PatternNode.Resolution.class);
        kryo.register(PatternNode.ShuffleMode.class);
        kryo.register(NoteNode.class);

        kryo.register(VolumeComponent.class);
        kryo.register(PresetComponent.class);
        kryo.register(SynthComponent.class);
        kryo.register(PatternSequencerComponent.class);
        kryo.register(MixerChannel.class);
        kryo.register(EffectsChannel.class);
        kryo.register(TrackComponent.class);
        kryo.register(ClipComponent.class);

        // Effects
        kryo.register(EffectType.class);
        kryo.register(ChorusMode.class);
        kryo.register(DelayMode.class);
        kryo.register(DistortionProgram.class);
        kryo.register(FlangerMode.class);
        kryo.register(MultiFilterMode.class);
        kryo.register(StaticFlangerMode.class);

        kryo.register(AutoWahEffect.class);
        kryo.register(BitcrusherEffect.class);
        kryo.register(CabinetSimulatorEffect.class);
        kryo.register(ChorusEffect.class);
        kryo.register(CombFilterEffect.class);
        kryo.register(CompressorEffect.class);
        kryo.register(DelayEffect.class);
        kryo.register(DistortionEffect.class);
        kryo.register(FlangerEffect.class);
        kryo.register(LimiterEffect.class);
        kryo.register(MultiFilterEffect.class);
        kryo.register(ParametricEQEffect.class);
        kryo.register(PhaserEffect.class);
        kryo.register(ReverbEffect.class);
        kryo.register(StaticFlangerEffect.class);
        kryo.register(VinylSimulatorEffect.class);

        // SubSynthMachine
        kryo.register(SubSynthMachine.class);

        kryo.register(SynthFilterComponent.class);
        kryo.register(LFO1Component.class);
        kryo.register(LFO2Component.class);
        kryo.register(Osc1Component.class);
        kryo.register(Osc2Component.class);
        kryo.register(VolumeEnvelopeComponent.class);

        kryo.register(FilterMessage.FilterType.class);

        kryo.register(SubSynthMessage.LFO1Target.class);
        kryo.register(SubSynthMessage.LFO1Waveform.class);
        kryo.register(SubSynthMessage.LFO2Target.class);

        kryo.register(SubSynthMessage.ModulationMode.class);
        kryo.register(SubSynthMessage.Osc1Waveform.class);
        kryo.register(SubSynthMessage.Osc2Waveform.class);
        kryo.register(SubSynthMessage.CentsMode.class);

        // FMSynthMachine
        kryo.register(FMSynthMachine.class);

        kryo.register(FMAlgorithm.class);
        kryo.register(FMOperatorControl.class);

        kryo.register(FMControlsComponent.class);
        kryo.register(LFOComponent.class);
        kryo.register(FMOperatorComponent.class);

        // PCMSynthMachine
        kryo.register(PCMSynthMachine.class);

        kryo.register(PCMSamplerChannel.class);

        kryo.register(VolumeEnvelopeComponent.class);
        kryo.register(SynthFilterComponent.class);
        kryo.register(com.teotigraphix.caustk.node.machine.patch.pcmsynth.LFO1Component.class);
        kryo.register(PCMSamplerComponent.class);
        kryo.register(PCMTunerComponent.class);

        // PCMSynthMachine
        kryo.register(BeatBoxMachine.class);

        kryo.register(WavSamplerComponent.class);
        kryo.register(WavSamplerChannel.class);
    }

    @Override
    public void serialize(File target, Object node) throws IOException {
        Output output = new Output(new FileOutputStream(target.getAbsolutePath()));
        kryo.writeObject(output, node);
        output.close();
    }

    @Override
    public <T> T deserialize(File file, Class<T> type) throws IOException {
        Input input = new Input(new FileInputStream(file));
        T instance = kryo.readObject(input, type);
        return instance;
    }

    public class UUIDSerializer extends Serializer<UUID> {
        public UUIDSerializer() {
            setImmutable(true);
        }

        @Override
        public void write(final Kryo kryo, final Output output, final UUID uuid) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }

        @Override
        public UUID read(final Kryo kryo, final Input input, final Class<UUID> uuidClass) {
            return new UUID(input.readLong(), input.readLong());
        }
    }
}