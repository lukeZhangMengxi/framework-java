package nio_reactor_pattern;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

public class Reactor implements Runnable {
    
    private static final int BUFFER_SIZE = 256;
    private Queue<SocketChannel> q = new ConcurrentLinkedQueue<SocketChannel>();
    private Selector selector;

    public Reactor() {
        try {
            selector = Selector.open();
        } catch(IOException e) {
            // Hanle it
        }
    }

    public void addChannel(SocketChannel sChan) {
        q.add(sChan);
    }

    @Override
    public void run() {
        
        while(true) {
            try {
                SocketChannel socketChan = q.poll();
                if (socketChan == null) continue;

                socketChan.configureBlocking(false);
                socketChan.register(selector, SelectionKey.OP_READ);

                int readyChan = selector.select();
                if (readyChan == 0) continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while(keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
                        SocketChannel client = (SocketChannel) key.channel();
                        if (client.read(buf) < 0) {
                            key.cancel();
                            client.close();
                        } else {
                            buf.flip();
                            client.write(buf);
                            buf.clear();
                        }
                    }
                    keyIterator.remove();
                }
            } catch(IOException e) {
                // Handle it
            }
        }
    }
}
