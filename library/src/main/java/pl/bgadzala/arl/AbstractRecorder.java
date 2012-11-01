package pl.bgadzala.arl;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;

import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract audio recorder which is format unaware. Handles state switching
 * and reading audio data from platform audio recorder.
 * <p/>
 * Stopped recording could be started until recorder is released. Such behaviour
 * allows to implement a pause function which is unavailable in {@link AudioRecord}.
 *
 * @author Bartosz Gadzała
 */
public abstract class AbstractRecorder implements Recorder, Runnable {

    /**
     * <code>true</code> if recording is started
     */
    private AtomicBoolean mStarted = new AtomicBoolean(false);
    /**
     * Controls recording task - setting to <code>false</code> finishes tasks.
     */
    private AtomicBoolean mRecording = new AtomicBoolean(false);

    /**
     * Wrapped audio recorder.
     */
    protected AudioRecord mAudioRecord;
    /**
     * Output stream for recorded audio.
     */
    protected RandomAccessFile mOutput;
    /**
     * Buffer for raw PCM data.
     */
    protected byte[] mPcmBuffer;

    public AbstractRecorder(AudioRecord audioRecord, RandomAccessFile out) {
        if (audioRecord == null) {
            throw new NullPointerException("Audio recorder is mandatory");
        } else if (out == null) {
            throw new NullPointerException("Output file is mandatory");
        }

        mAudioRecord = audioRecord;
        mOutput = out;
        mPcmBuffer = createPCMBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public void start() {
        if (mAudioRecord == null) {
            throw new IllegalStateException("Audio recorder is already stopped");
        }
        mStarted.set(true);
        if (!mRecording.getAndSet(true)) {
            Thread t = new Thread(this, "AudioRecorderTask");
            t.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void pause() {
        if (mAudioRecord == null) {
            throw new IllegalStateException("Audio recorder is already stopped");
        }
        mStarted.set(false);
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        if (mAudioRecord == null) {
            throw new IllegalStateException("Audio recorder is already stopped");
        }
        mRecording.set(false);
        pause();
        stopAudioRecorder();
    }

    /**
     * Reads audio to PCM buffer in a separate thread.
     */
    @Override
    public void run() {
        startAudioRecorder();
        onRecordingStarted();

        try {
            int readSize;
            while (mRecording.get()) {
                if (mStarted.get()) {
                    readSize = mAudioRecord.read(mPcmBuffer, 0, mPcmBuffer.length);
                    if (readSize < 0) {
                        throw new IllegalStateException("AudioRecorder returned [" + readSize + "] bytes");
                    } else if (readSize > 0) {
                        onSampleRead(mPcmBuffer, readSize);
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        } finally {
            onRecordingFinished();
        }
    }

    /**
     * Creates PCM buffer capable to hold 5 seconds of audio.
     *
     * @return PCM buffer
     */
    protected byte[] createPCMBuffer() {
        int audioFormat = mAudioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8;
        return new byte[mAudioRecord.getSampleRate() * (audioFormat / 8) * mAudioRecord.getChannelCount() * 5];
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

    protected abstract void onRecordingStarted();

    protected abstract void onSampleRead(byte[] buffer, int size);

    protected abstract void onRecordingFinished();

    /**
     * Starts audio recorder.
     */
    private void startAudioRecorder() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

        if (mAudioRecord != null && mAudioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.startRecording();
        }
    }

    /**
     * Stops audio recorder.
     */
    private void stopAudioRecorder() {
        if (mAudioRecord != null && mAudioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }

        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
    }
}
