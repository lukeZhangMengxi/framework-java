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
    private String[] sendMsgs, echoMsgs;

    @Test
    public void singleThreadServerSingleClient() throws IOException, InterruptedException {
        sendMsgs = new String[] { "Hello World!" };
        echoMsgs = new String[1];

        // Run server in a new thread
        new Thread(new Acceptor(ip, port, 1)).start();

        Thread t = new Thread(new EchoTestClient(0));
        t.start();
        t.join();

        assertEquals(0, sendMsgs[0].compareTo(echoMsgs[0]));
    }

    @Test
    public void singleThreadServerMultiClients() throws IOException, InterruptedException {
        sendMsgs = new String[] { "Hello World!", "Second Test~", "another One#", "jewkhr", "ewr23r", "weasd21", "902109u", "wqjkeh2" };
        int size = sendMsgs.length;
        echoMsgs = new String[size];

        // Run server in a new thread
        new Thread(new Acceptor(ip, port, 1)).start();

        Thread[] clients = new Thread[size];
        for (int i=0; i<size; i++) {
            clients[i] = new Thread(new EchoTestClient(i));
            clients[i].start();
        }
        for (Thread t: clients) { t.join(); }

        for (int i=0; i<sendMsgs.length; i++) {
            assertEquals(0, sendMsgs[i].compareTo(echoMsgs[i]));
        }
    }

    @Test
    public void multiThreadServerMultiClients() throws IOException, InterruptedException {
        /*
            The 3-time average runtime is slower than `singleThreadServerMultiClients` by 198 ms (28.5%).
            Guess the thread-creation is dominating at this case.
        */
        sendMsgs = new String[] { "Hello World!", "Second Test~", "another One#", "jewkhr", "ewr23r", "weasd21", "902109u", "wqjkeh2" };
        int size = sendMsgs.length;
        echoMsgs = new String[size];
        
        // Run server in a new thread
        new Thread(new Acceptor(ip, port, size)).start();

        Thread[] clients = new Thread[size];
        for (int i=0; i<size; i++) {
            clients[i] = new Thread(new EchoTestClient(i));
            clients[i].start();
        }
        for (Thread t: clients) { t.join(); }

        for (int i=0; i<sendMsgs.length; i++) {
            assertEquals(0, sendMsgs[i].compareTo(echoMsgs[i]));
        }
    }

    public class EchoTestClient implements Runnable {

        private int msgIndex;

        public EchoTestClient(int idx) {
            msgIndex = idx;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChan = SocketChannel.open(new InetSocketAddress(ip, port));
            
                socketChan.configureBlocking(false);
                // Busy waiting until connection established
                while (!socketChan.isConnected()) {}
        
                ByteBuffer buf = ByteBuffer.allocate(1024);
                buf.put(sendMsgs[msgIndex].getBytes());
                buf.flip();
        
                socketChan.write(buf);

                Thread.sleep(400);

                ByteBuffer readBuffer = ByteBuffer.allocate(256);
                readBuffer.clear();
                socketChan.read(readBuffer);
                echoMsgs[msgIndex] = new String(readBuffer.array()).trim();

                socketChan.shutdownOutput();
                socketChan.close();
            } catch(IOException e) { 
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}