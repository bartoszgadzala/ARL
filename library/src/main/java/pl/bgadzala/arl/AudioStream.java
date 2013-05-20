package pl.bgadzala.arl;

/**
 * Defines interface for audio streams.
 *
 * @author Bartosz Gadzała
 */
public interface AudioStream {

    /**
     * Describes type of audio data.
     */
    public enum DataType {
        /** Audio header. */
        HEADER,
        /** Audio data (e.g. PCM chunks). */
        DATA
    }

    /**
     * Uses to specify what kind of data will be written to the audio stream.
     * It can be used to omit header data and store only audio chunks if desired
     * or to separate them and store in different locations.
     *
     * @param dataType data type that will be stored after that invocation
     */
    void setDataType(DataType dataType);

    /**
     * Sets the stream pointer to the specified position.
     *
     * @param position offset measured from the beginning of the stream
     */
    void seek(long position);

    /**
     * Sets the length of the stream.
     *
     * @param length new length of the stream
     */
    void setLength(long length);

    /**
     * @param data bytes array to write to the stream
     */
    void write(byte[] data);

    /**
     * @param data bytes array to write to the stream
     * @param offset offset measured from the beginning of the data
     * @param size number of bytes to write
     */
    void write(byte[] data, int offset, int size);

    /**
     * @param data integer to write
     */
    void write(int data);

    /**
     * @param data short to write
     */
    void write(short data);

    /**
     * Closes stream.
     */
    void close();
}
