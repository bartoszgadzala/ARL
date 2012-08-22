package pl.bgadzala.arl;

import android.media.AudioRecord;

/**
 * Task responsible for reading from {@link android.media.AudioRecord} and writing
 * result to PCM buffer.
 *
 * @author Bartosz Gadzała
 */
class RecordAudioTask implements Runnable {

    /**
     * Source of audio.
     */
    private AudioRecord mAudioRecord;

    /**
     * Controls recording task - setting to <code>false</code> finishes this tasks.
     */
    private boolean mRecording;

    @Override
    public void run() {
        startAudioRecorder();

        while (mRecording) {
            // TODO: read to PCM buffer
        }

        stopAudioRecorder();
    }

    private void startAudioRecorder() {
        if (mAudioRecord != null && mAudioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.startRecording();
        }
    }

    private void stopAudioRecorder() {
        if (mAudioRecord != null && mAudioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
            mAudioRecord.stop();
        }
    }
}
