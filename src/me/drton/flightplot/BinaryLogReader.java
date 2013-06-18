package me.drton.flightplot;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;

/**
 * User: ton Date: 03.06.13 Time: 14:51
 */
public abstract class BinaryLogReader implements LogReader {
    protected ByteBuffer buffer;
    protected SeekableByteChannel channel = null;

    public BinaryLogReader(String fileName) throws IOException {
        buffer = ByteBuffer.allocate(8192);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.flip();
        channel = Files.newByteChannel(new File(fileName).toPath());
    }

    @Override
    public void close() throws IOException {
        channel.close();
        channel = null;
    }

    public int fillBuffer() throws IOException {
        buffer.compact();
        int n = channel.read(buffer);
        buffer.flip();
        if (n < 0)
            throw new EOFException();
        return n;
    }

    protected long position() throws IOException {
        return channel.position() - buffer.remaining();
    }

    protected int position(long pos) throws IOException {
        buffer.clear();
        channel.position(pos);
        int n = channel.read(buffer);
        buffer.flip();
        if (n < 0)
            throw new EOFException();
        return n;
    }
}