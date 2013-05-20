package pl.bgadzala.arl;

import android.media.AudioRecord;

import java.io.RandomAccessFile;

/**
 * Factory of audio recorders for specific file format.
 */
public interface RecorderFactory {

    /**
     * Creates audio recorder for specified audio source ({@link android.media.MediaRecorder.AudioSource#MIC} for example).
     * It automatically creates {@link AudioRecord} for specified audio source and best quality available on a platform.
     *
     * @param audioSource audio source for new audio recorder
     * @param out         output stream
     * @return audio recorder for specified audio source
     */
    Recorder createRecorder(int audioSource, AudioStream out);

    /**
     * Creates audio recorder for specified {@link AudioRecord} object.
     *
     * @param audioRecord source of the audio for new recorder
     * @param out         output stream
     * @return audio recorder
     */
    Recorder createRecorder(AudioRecord audioRecord, AudioStream out);

}
