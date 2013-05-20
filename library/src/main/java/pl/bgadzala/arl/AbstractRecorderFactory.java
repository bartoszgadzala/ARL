package pl.bgadzala.arl;

import android.media.AudioRecord;

import java.io.RandomAccessFile;

/**
 * File format unaware audio format factory. Handles creating {@link AudioRecord} object
 * for best quality and audio source.
 */
public abstract class AbstractRecorderFactory implements RecorderFactory {

    /**
     * {@inheritDoc}
     */
    public Recorder createRecorder(int audioSource, AudioStream out) {
        return createRecorder(AudioRecordFactory.createForBestQuality(audioSource), out);
    }

}
