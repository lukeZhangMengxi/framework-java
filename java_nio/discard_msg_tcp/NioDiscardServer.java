package java_nio.discard_msg_tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import java_nio.NioSendMsgConfig;

public class NioDiscardServer {
    public void start() throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverChan = ServerSocketChannel.open();
        serverChan.configureBlocking(false);
        serverChan.bind(new InetSocketAddress(
            NioSendMsgConfig.SOCKET_SERVER_IP, NioSendMsgConfig.SOCKET_SERVER_PORT));

        System.out.println("Server started!");
        serverChan.register(selector, SelectionKey.OP_ACCEPT);

        while(selector.select() > 0) {
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while(keys.hasNext()) {
                SelectionKey k = keys.next();

                if (k.isAcceptable()) {
                    SocketChannel socketChan = serverChan.accept();
                    socketChan.configureBlocking(false);
                    socketChan.register(selector, SelectionKey.OP_READ);
                }
                else if (k.isReadable()) {
                    SocketChannel socketChan = (SocketChannel) k.channel();

                    ByteBuffer buf = ByteBuffer.allocate(NioSendMsgConfig.SEND_BUFFER_SIZE);
                    int len = 0;
                    while ((len = socketChan.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, buf.limit()));
                        buf.clear();
                    }
                    socketChan.close();
                }

                keys.remove();
            }
        }
        serverChan.close();
    }

    public static void main(String[] args) throws IOException {
        new NioDiscardServer().start();
    }
}
