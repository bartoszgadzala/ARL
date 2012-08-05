package pl.bgadzala.arl;

import android.media.AudioFormat;
import android.media.AudioRecord;

import java.io.OutputStream;

/**
 * Abstract audio recorder which is format unaware. Handles state switching
 * and reading audio data from platform audio recorder.
 * <p/>
 * <p>Stopped recording could be started until it's released. Such behaviour
 * allows to implement a pause function which is unavailable in {@link AudioRecord}.
 *
 * @author Bartosz Gadzała
 */
public abstract class AbstractRecorder {

    /**
     * Wrapped audio recorder.
     */
    private AudioRecord mAudioRecord;
    /**
     * Output stream for recorded audio.
     */
    private OutputStream mOutputStream;
    /**
     * Buffer for raw PCM data.
     */
    private short[] mPcmBuffer;

    public AbstractRecorder(AudioRecord audioRecord, OutputStream out) {
        if (audioRecord == null) {
            throw new NullPointerException("Audio recorder is mandatory");
        } else if (out == null) {
            throw new NullPointerException("Output stream is mandatory");
        }

        mAudioRecord = audioRecord;
        mOutputStream = out;
        mPcmBuffer = createPCMBuffer();
    }

    public void start() {
        // TODO implement start
    }

    public void stop() {
        // TODO implement stop
    }

    public void release() {
        // TODO implement release
    }

    /**
     * Creates PCM buffer capable to hold 5 seconds of audio.
     *
     * @return PCM buffer
     */
    protected short[] createPCMBuffer() {
        int audioFormat = mAudioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8;
        return new short[mAudioRecord.getSampleRate() * (audioFormat / 8) * mAudioRecord.getChannelCount() * 5];
    }

    /**
     * Computes minimum size of the buffer.
     *
     * @return minimum size of the buffer
     */
    protected int getMinBufferSize() {
        final int minBufferSize = AudioRecord.getMinBufferSize(
                mAudioRecord.getSampleRate(), mAudioRecord.getChannelConfiguration(), mAudioRecord.getAudioFormat());
        if (minBufferSize < 0) {
            throw new IllegalStateException("Invalid audio recorder - cannot compute minimum buffer size");
        }
        return minBufferSize;
    }
}
