package java_nio.send_file_socket_channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class SocketChanServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChan = ServerSocketChannel.open();
        serverChan.socket().bind(new InetSocketAddress(9000));

        SocketChannel clientChan = serverChan.accept();
        System.out.println("Connection Set:  " + clientChan.getRemoteAddress());

        Path path = Paths.get("java_nio/send_file_socket_channel/server_received.txt");
        FileChannel fileChan = FileChannel.open(path,
            EnumSet.of(StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE));

        ByteBuffer buf = ByteBuffer.allocate(1024);
        while(clientChan.read(buf) > 0) {
            buf.flip();
            fileChan.write(buf);
            buf.clear();
        }
        fileChan.close();
        System.out.println("File Received");
        clientChan.close();

    }
}