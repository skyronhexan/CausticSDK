
package com.teotigraphix.caustk.sequencer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.sequencer.track.PhraseNote;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.caustk.sequencer.track.TrackItem;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.sequencer.track.TrackSong;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

public interface ITrackSequencer extends IControllerComponent {

    TrackSong getTrackSong();

    /**
     * Returns the current track/tone index.
     */
    int getCurrentTrack();

    /**
     * Sets the current track index.
     * 
     * @param value Valid values are existing tracks 0-13.
     * @throws IllegalArgumentException
     */
    void setCurrentTrack(int value);

    /**
     * Returns the selected bank of the {@link #getSelectedTrack()}.
     */
    int getCurrentBank();

    int getCurrentBank(int trackIndex);

    int getCurrentPattern();

    int getCurrentPattern(int trackIndex);

    /**
     * Returns whether the sequencer contains tracks.
     * <p>
     * A track is created in response to the {@link OnSoundSourceToneAdd} event,
     * a track is removed in response to the {@link OnSoundSourceToneRemove}
     * Event.
     */
    boolean hasTracks();

    /**
     * Returns the selected/focused track in the sequencer.
     */
    Track getSelectedTrack();

    /**
     * Returns a collection of {@link Track}s that have been created due to
     * {@link Tone} creation in the {@link ISoundSource}.
     */
    Collection<Track> getTracks();

    /**
     * Returns a {@link Track} at the specified index.
     * <p>
     * If the channel has not been created, a new {@link Track} instance is
     * created and placed in the sequencer.
     * 
     * @param index The tone index
     */
    Track getTrack(int index);

    Track getTrack(Tone tone);

    /**
     * @see #createSong(File)
     * @param relativePath
     * @throws IOException
     */
    TrackSong createSong(String relativePath) throws IOException;

    /**
     * Creates a new {@link TrackSong}.
     * 
     * @param songFile The absolute path to the file, the <code>.caustic</code>
     *            file is saved in the same directory with the same name as this
     *            song file.
     * @see OnTrackSongChange
     * @throws IOException
     */
    TrackSong createSong(File songFile) throws IOException;

    /**
     * Loads the complete state of a <code>.caustic</code> file into the current
     * {@link TrackSong}.
     * 
     * @param absoluteCausticFile The absolute location of the
     *            <code>.caustic</code> file.
     * @throws IOException
     */
    void load(File absoluteCausticFile) throws IOException;

    public enum TrackChangeKind {

        /**
         * @see Track#setCurrentBank(int)
         */
        Bank,

        /**
         * @see Track#setCurrentPattern(int)
         */
        Pattern,

        Add,

        Remove;
    }

    public static class OnTrackChange {

        final private TrackChangeKind kind;

        public TrackChangeKind getKind() {
            return kind;
        }

        Track track;

        public Track getTrack() {
            return track;
        }

        private TrackItem trackItem;

        public final TrackItem getItem() {
            return trackItem;
        }

        public OnTrackChange(TrackChangeKind kind, Track track) {
            this.kind = kind;
            this.track = track;
        }

        public OnTrackChange(TrackChangeKind kind, Track track, TrackItem trackItem) {
            this.kind = kind;
            this.track = track;
            this.trackItem = trackItem;
        }
    }

    public enum TrackPhraseChangeKind {

        /**
         * Dispatched every beat when the phrase is active.
         */
        Beat,

        /**
         * @see TrackPhrase#setLength(int)
         */
        Length,

        /**
         * @see TrackPhrase#setNoteData(String)
         */
        NoteData,

        /**
         * @see TrackPhrase#addNote(int, float, float, float, int)
         */
        NoteAdd,

        /**
         * @see TrackPhrase#removeNote(int, float)
         */
        NoteRemove,

        ClearMeasure,

        /**
         * @see TrackPhrase#setPlayMeasure(int)
         */
        PlayMeasure,

        /**
         * @see TrackPhrase#setEditMeasure(int)
         */
        EditMeasure,

        Scale,

        Position;
    }

    public static class OnTrackPhraseChange {

        private final TrackPhraseChangeKind kind;

        public TrackPhraseChangeKind getKind() {
            return kind;
        }

        private final TrackPhrase trackPhrase;

        public TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

        private final PhraseNote phraseNote;

        public PhraseNote getPhraseNote() {
            return phraseNote;
        }

        public OnTrackPhraseChange(TrackPhraseChangeKind kind, TrackPhrase trackPhrase,
                PhraseNote phraseNote) {
            this.kind = kind;
            this.trackPhrase = trackPhrase;
            this.phraseNote = phraseNote;
        }
    }

    public enum TrackSongChangeKind {

        Create,

        Load,

        Save
    }

    public static class OnTrackSongChange {

        private TrackSong trackSong;

        private TrackSongChangeKind kind;

        public TrackSongChangeKind getKind() {
            return kind;
        }

        public TrackSong getTrackSong() {
            return trackSong;
        }

        public OnTrackSongChange(TrackSongChangeKind kind, TrackSong trackSong) {
            this.kind = kind;
            this.trackSong = trackSong;
        }
    }

}
