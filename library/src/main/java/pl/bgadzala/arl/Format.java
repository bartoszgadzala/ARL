package pl.bgadzala.arl;

import android.media.AudioRecord;

import java.io.RandomAccessFile;

/**
 * Holds all supported formats.
 */
public enum Format {
    /**
     * Waveform Audio File Format.
     */
    WAV(new WavRecorderFactory());

    /**
     * Factory for format specific audio recorders.
     */
    private RecorderFactory mFactory;

    private Format(RecorderFactory factory) {
        mFactory = factory;
    }

    /**
     * Creates audio recorder for specified audio source ({@link android.media.MediaRecorder.AudioSource#MIC} for example).
     * It automatically creates {@link AudioRecord} for specified audio source and best quality available on a platform.
     *
     * @param audioSource audio source for new audio recorder
     * @param out         output file
     * @return audio recorder for specified audio source
     */
    public Recorder createRecorder(int audioSource, RandomAccessFile out) {
        return mFactory.createRecorder(audioSource, out);
    }

    /**
     * Creates audio recorder for specified {@link AudioRecord} object.
     *
     * @param audioRecord source of the audio for new recorder
     * @param out         output file
     * @return audio recorder
     */
    public Recorder createRecorder(AudioRecord audioRecord, RandomAccessFile out) {
        return mFactory.createRecorder(audioRecord, out);
    }
}
