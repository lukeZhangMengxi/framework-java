package nio_channel_buffer.send_msg_udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import nio_channel_buffer.NioSendMsgConfig;

public class UDPServer {
    public void receive() throws IOException {
        DatagramChannel datagramChan = DatagramChannel.open();
        datagramChan.configureBlocking(false);
        datagramChan.bind(new InetSocketAddress(
            NioSendMsgConfig.SOCKET_SERVER_IP, NioSendMsgConfig.SOCKET_SERVER_PORT));
        System.out.println("UDP Server started!");

        Selector selector = Selector.open();
        datagramChan.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            ByteBuffer buf = ByteBuffer.allocate(NioSendMsgConfig.SEND_BUFFER_SIZE);

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    datagramChan.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }
            iterator.remove();
        }
        selector.close();
        datagramChan.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }
}
