package pl.bgadzala.arl;

import android.media.AudioRecord;

import java.io.OutputStream;

/**
 * Recorder which allows to encode audio stream as WAV.
 *
 * @author Bartosz Gadzała
 */
public class WavRecorder extends AbstractRecorder {

    public WavRecorder(AudioRecord audioRecord, OutputStream out) {
        super(audioRecord, out);
    }
}
