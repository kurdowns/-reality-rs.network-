package net.crandor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class RSSocket implements Runnable {

    public RSSocket(GameShell RSApplet_, Socket socket1) throws IOException {
        closed = false;
        isWriter = false;
        hasIOError = false;
        rsApplet = RSApplet_;
        socket = socket1;
        socket.setSoTimeout(30000);
        socket.setTcpNoDelay(true);
        socket.setReceiveBufferSize(16384);
        socket.setSendBufferSize(16384);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void close() {
    	if (!closed) {
            synchronized (this) {
            	closed = true;
	            notifyAll();
	        }
            isWriter = false;
    	}
    }

    public int read() throws IOException {
        if (closed) {
            return 0;
        }
        return inputStream.read();
    }

    public int available() throws IOException {
        if (closed) {
            return 0;
        }
        return inputStream.available();
    }

    public void flushInputStream(byte buf[], int off, int len) throws IOException {
        if (closed) {
            return;
        }
        while (len > 0) {
            int step = inputStream.read(buf, off, len);
            if (step <= 0) {
                throw new IOException("EOF");
            }
            len -= step;
            off += step;
        }

    }

    public void queueBytes(int i, byte abyte0[]) throws IOException {
        if (closed) {
            return;
        }
        if (hasIOError) {
            hasIOError = false;
            throw new IOException("Error in writer thread");
        }
        if (buffer == null) {
            buffer = new byte[5000];
        }
        synchronized (this) {
            for (int l = 0; l < i; l++) {
                buffer[buffIndex] = abyte0[l];
                buffIndex = (buffIndex + 1) % 5000;
                if (buffIndex == (writeIndex + 4900) % 5000)
                    throw new IOException("buffer overflow");
            }

            if (!isWriter) {
                isWriter = true;
                rsApplet.startRunnable(this, 3);
            }
            notifyAll();
        }
    }

    @Override
	public void run() {
		try {
			for (;;) {
				int len;
				int off;
				synchronized (this) {
					if (buffIndex == writeIndex) {
						if (closed) {
							break;
						}
						try {
							wait();
						} catch (InterruptedException _ex) {
						}
					}
					if (!isWriter)
						return;
					off = writeIndex;
					if (writeIndex <= buffIndex)
						len = buffIndex - writeIndex;
					else
						len = 5000 - writeIndex;
				}
				if (len > 0) {
					try {
						outputStream.write(buffer, off, len);
					} catch (IOException _ex) {
						hasIOError = true;
					}
					writeIndex = (writeIndex + len) % 5000;
					try {
						if (buffIndex == writeIndex) {
							outputStream.flush();
						}
					} catch (IOException _ex) {
						hasIOError = true;
					}
				}
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException _ex) {
				System.out.println("Error closing stream");
			}
			buffer = null;
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

    public void printDebug() {
        System.out.println("dummy:" + closed);
        System.out.println("tcycl:" + writeIndex);
        System.out.println("tnum:" + buffIndex);
        System.out.println("writer:" + isWriter);
        System.out.println("ioerror:" + hasIOError);
        try {
            System.out.println("available:" + available());
        } catch (IOException _ex) {
        }
    }

    private InputStream inputStream;
    private OutputStream outputStream;
    private final Socket socket;
    private boolean closed;
    private final GameShell rsApplet;
    private byte[] buffer;
    private int writeIndex;
    private int buffIndex;
    private boolean isWriter;
    private boolean hasIOError;
}
