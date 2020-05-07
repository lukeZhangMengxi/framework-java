package nio_channel_buffer.discard_msg_tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import nio_channel_buffer.NioSendMsgConfig;

public class NioDiscardClient {
    public void start() throws IOException {
        SocketChannel socketChan = SocketChannel.open(new InetSocketAddress(
            NioSendMsgConfig.SOCKET_SERVER_IP, NioSendMsgConfig.SOCKET_SERVER_PORT));
        
        socketChan.configureBlocking(false);
        // Busy waiting until connection established
        while (!socketChan.isConnected()) {
            System.out.println("Connecting...");
        }
        System.out.println("Connection established!");

        ByteBuffer buf = ByteBuffer.allocate(NioSendMsgConfig.SEND_BUFFER_SIZE);
        buf.put("Hello this is a msg".getBytes());
        buf.flip();

        socketChan.write(buf);
        socketChan.shutdownOutput();
        socketChan.close();
    }

    public static void main(String[] args) throws IOException {
        new NioDiscardClient().start();
    }
}