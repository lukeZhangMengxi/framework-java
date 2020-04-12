package java_nio.send_msg_udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.Scanner;

import java_nio.NioSendMsgConfig;

public class UDPClient {
    public void send() throws IOException {
        DatagramChannel datagramChan = DatagramChannel.open();
        datagramChan.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(NioSendMsgConfig.SEND_BUFFER_SIZE);

        Scanner scanner = new Scanner(System.in);
        System.out.println("UDP client started!");
        System.out.println("Please enter message to send:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buf.put((LocalDateTime.now().toString() + " >>" + next).getBytes());
            
            buf.flip();
            
            datagramChan.send(buf, new InetSocketAddress(
                NioSendMsgConfig.SOCKET_SERVER_IP, NioSendMsgConfig.SOCKET_SERVER_PORT));

            buf.clear();
        }

        datagramChan.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }
}
