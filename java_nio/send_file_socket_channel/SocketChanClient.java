package java_nio.send_file_socket_channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SocketChanClient {
    public static void main(String[] args) throws IOException {
        SocketChannel clientChan = SocketChannel.open();
        clientChan.connect(new InetSocketAddress("localhost", 9000));

        Path path = Paths.get("java_nio/send_file_socket_channel/client_sent.txt");
        FileChannel fileChan = FileChannel.open(path);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while(fileChan.read(buf) > 0) {
            buf.flip();
            clientChan.write(buf);
            buf.clear();
        }
        fileChan.close();
        System.out.println("File Sent");
        clientChan.close();
    }
}
