
package com.teotigraphix.caustk.sound;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.tone.ToneUtils;

public class SoundSourceTest {
    private ICaustkApplication application;

    private SoundSource soundSource;

    private ICaustkController controller;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        soundSource = (SoundSource)application.getController().getSoundSource();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        soundSource = null;
    }

    @Test
    public void test_loadSong() throws CausticException {

    }

    @Test
    public void test_setup() throws CausticException {
        soundSource.loadSong(new File("src/test/java/"
                + "com/teotigraphix/caustk/sound/PULSAR.caustic"));

        Assert.assertEquals(6, soundSource.getToneCount());

        Assert.assertEquals(soundSource.getTone(0).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(1).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(2).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(3).getToneType(), ToneType.Bassline);
        Assert.assertEquals(soundSource.getTone(4).getToneType(), ToneType.PCMSynth);
        Assert.assertEquals(soundSource.getTone(5).getToneType(), ToneType.Beatbox);

        //OutputPanelMessage.PLAY.send(application.getController(), 1);

        Assert.assertNotNull(controller.getSoundMixer().getChannel(0));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(1));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(2));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(3));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(4));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(5));

        //controller.getSystemSequencer().play(SequencerMode.SONG);
    }

    @Test
    public void test_Beatbox() throws CausticException {
        BeatboxTone tone = (BeatboxTone)soundSource.createTone("tone1", ToneType.Beatbox);
        Assert.assertEquals(4, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getSampler());
    }

    @Test
    public void test_PCMSynth() throws CausticException {
        PCMSynthTone tone = (PCMSynthTone)soundSource.createTone("tone1", ToneType.PCMSynth);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getSampler());
        Assert.assertNotNull(tone.getTuner());
    }

    @Test
    public void test_SubSynth() throws CausticException {
        SubSynthTone tone = (SubSynthTone)soundSource.createTone("tone1", ToneType.SubSynth);
        Assert.assertEquals(8, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getOsc2());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getLFO2());
    }

    @Test
    public void test_Bassline() throws CausticException {
        BasslineTone tone = (BasslineTone)soundSource.createTone("tone1", ToneType.Bassline);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getDistortion());
    }

}
