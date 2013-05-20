package pl.bgadzala.arl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Random access file backed audio stream.
 */
public class RAFAudioStream implements AudioStream {

    private RandomAccessFile mFile;

    public RAFAudioStream(RandomAccessFile file) {
        mFile = file;
    }

    /**
     * Method unused.
     *
     * @param dataType data type that will be stored after that invocation
     */
    @Override
    public void setDataType(DataType dataType) {
        // ignored
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seek(long position) {
        try {
            mFile.seek(position);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot seek to [%d]", position), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLength(long length) {
        try {
            mFile.setLength(length);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot set length to [%d]", length), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] data) {
        try {
            mFile.write(data);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot write data [%s]", Arrays.toString(data)), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] data, int offset, int size) {
        try {
            mFile.write(data, offset, size);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot write [%d] bytes of data [%s] from offset [%d]",
                    size, Arrays.toString(data), offset), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int data) {
        try {
            mFile.writeInt(data);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot write integer [%d]", data), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(short data) {
        try {
            mFile.writeShort(data);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Cannot write short [%d]", data), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            mFile.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error while closing stream", ex);
        }
    }
}
