package nio_reactor_pattern;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.junit.Test;

public class MyTest {

    private String ip = "localhost";
    private int port = 9000;

    @Test
    public void singleThreadServer() throws IOException, InterruptedException {
        String sendMsg = "Hello World!";
        String echoMsg;
        
        // Run server in a new thread, non-blocking
        new Thread(new Acceptor(ip, port, 1)).start();

        // Run client in the current thread, blocking
        echoMsg = new EchoTestClient(sendMsg).start();
        
        assertEquals(0, sendMsg.compareTo(echoMsg));
    }

    public class EchoTestClient {

        private String sendMsg;

        public EchoTestClient(String msg) {
            sendMsg = msg;
        }

        public String start() throws IOException, InterruptedException {
            String echoMsg;
            SocketChannel socketChan = SocketChannel.open(new InetSocketAddress(ip, port));
            
            socketChan.configureBlocking(false);
            // Busy waiting until connection established
            while (!socketChan.isConnected()) {}
    
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.put(sendMsg.getBytes());
            buf.flip();
    
            socketChan.write(buf);

            Thread.sleep(400);

            ByteBuffer readBuffer = ByteBuffer.allocate(256);
            readBuffer.clear();
            socketChan.read(readBuffer);
            echoMsg = new String(readBuffer.array()).trim();

            socketChan.shutdownOutput();
            socketChan.close();

            return echoMsg;
        }
    }
}