package pl.bgadzala.arl;

import android.media.AudioRecord;

import java.io.RandomAccessFile;

/**
 * Audio recorder factory for WAV format.
 */
public class WavRecorderFactory extends AbstractRecorderFactory {

    /**
     * Creates WAV audio recorder for specified {@link android.media.AudioRecord} object.
     *
     * @param audioRecord source of the audio for new recorder
     * @param out         output WAV file
     * @return audio recorder for WAV file format
     */
    public WavRecorder createRecorder(AudioRecord audioRecord, RandomAccessFile out) {
        return new WavRecorder(audioRecord, out);
    }
}
