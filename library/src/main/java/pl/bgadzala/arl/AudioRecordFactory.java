package pl.bgadzala.arl;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Factory for audio recorder.
 *
 * @author Bartosz Gadzała
 */
public class AudioRecordFactory {

    private static final int[] SAMPLE_RATES_IN_HZ = new int[]{44100, 22050, 16000, 11025, 8000};
    private static final int[] CHANNEL_CONFIGS = new int[]{AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO};
    private static final int[] AUDIO_FORMATS = new int[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT};

    private AudioRecordFactory() {
    }

    /**
     * Creates audio recorder for best quality which is available on runtime platform.
     *
     * @param audioSource source of audio (could be {@link MediaRecorder.AudioSource#MIC} for example)
     * @return best available audio recorder or <code>null</code> if recorder can not be created
     */
    public static AudioRecord createForBestQuality(int audioSource) {
        AudioRecord audioRecord = null;

        for (int sampleRateInHz : SAMPLE_RATES_IN_HZ) {
            for (int channelConfig : CHANNEL_CONFIGS) {
                for (int audioFormat : AUDIO_FORMATS) {
                    int bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
                    if (bufferSize > 0) {
                        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, 2 * bufferSize);
                        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                            return audioRecord;
                        }
                    }
                }
            }
        }

        return null;
    }
}
