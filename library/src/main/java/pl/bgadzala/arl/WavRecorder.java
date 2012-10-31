package pl.bgadzala.arl;

import android.media.AudioFormat;
import android.media.AudioRecord;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Recorder which allows to encode audio stream as WAV.
 *
 * @author Bartosz Gadzała
 */
public class WavRecorder extends AbstractRecorder {

    /**
     * Size of payload in bytes.
     */
    private int mPayloadSize;

    public WavRecorder(AudioRecord audioRecord, RandomAccessFile out) {
        super(audioRecord, out);
    }

    /**
     * Invoked where recording has started. Responsible for writing header (without payload
     * size which is unknown at the moment of creating header).
     */
    protected void onRecordingStarted() {
        try {
            int bitsPerSample = mAudioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8;
            int channels = mAudioRecord.getChannelCount();
            int sampleRate = mAudioRecord.getSampleRate();

            mOutput.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed
            mOutput.writeBytes("RIFF");
            mOutput.writeInt(0); // Final file size not known yet, write 0
            mOutput.writeBytes("WAVE");
            mOutput.writeBytes("fmt ");
            mOutput.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
            mOutput.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
            mOutput.writeShort(Short.reverseBytes((short) channels));// Number of channels, 1 for mono, 2 for stereo
            mOutput.writeInt(Integer.reverseBytes(sampleRate)); // Sample rate
            mOutput.writeInt(Integer.reverseBytes(sampleRate * bitsPerSample * channels / 8)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
            mOutput.writeShort(Short.reverseBytes((short) (channels * bitsPerSample / 8))); // Block align, NumberOfChannels*BitsPerSample/8
            mOutput.writeShort(Short.reverseBytes((short) bitsPerSample)); // Bits per sample
            mOutput.writeBytes("data");
            mOutput.writeInt(0); // Data chunk size not known yet, write 0
        } catch (IOException ex) {
            throw new RuntimeException("Error while writing header", ex);
        }
    }

    /**
     * Invoked every time PCM buffer was read.
     *
     * @param buffer PCM buffer read from AudioRecord
     * @param size   size of PCM buffer
     */
    protected void onSampleRead(byte[] buffer, int size) {
        try {
            mOutput.write(buffer, 0, size);
            mPayloadSize += size;
        } catch (IOException ex) {
            throw new RuntimeException("Error while writing PCM buffer of length [" + size + "]", ex);
        }
    }

    /**
     * Invoked when recording has finished. Responsible for updating WAV header.
     */
    protected void onRecordingFinished() {
        try {
            mOutput.seek(4); // Write size to RIFF header
            mOutput.writeInt(Integer.reverseBytes(mPayloadSize + 36));
            mOutput.seek(40); // Write size to Subchunk2Size field
            mOutput.writeInt(Integer.reverseBytes(mPayloadSize));
            mOutput.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error while closing WAV file", ex);
        }
    }
}
