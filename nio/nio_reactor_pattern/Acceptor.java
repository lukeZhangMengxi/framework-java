package nio_reactor_pattern;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Acceptor implements Runnable {
    private ServerSocketChannel serverSocketChan;
    private Reactor[] reactors;

    private Selector selector;
    private int numOfReactors;

    public Acceptor(String host, int port, int numOfWorkerThreads) {
        try {
            selector = Selector.open();
            reactors = new Reactor[numOfWorkerThreads];
            numOfReactors = numOfWorkerThreads;
            for (int i=0; i<numOfReactors; i++) {
                reactors[i] = new Reactor();
                (new Thread(reactors[i])).start();
            }

            serverSocketChan = ServerSocketChannel.open();
            serverSocketChan.bind(new InetSocketAddress(host, port));
            serverSocketChan.configureBlocking(false);
            serverSocketChan.register(selector, SelectionKey.OP_ACCEPT);
        } catch(IOException e) {
            // Handle it
        }
    }

    @Override
    public void run() {

        int i = 0;
        while(true) {
            try {
                int readyChan = selector.select();
                if (readyChan == 0) continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while(keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChan = (ServerSocketChannel) key.channel();
                        SocketChannel socketChan = serverSocketChan.accept();
                        reactors[i % numOfReactors].addChannel(socketChan);
                        i++;
                    }
                    keyIterator.remove();
                }
            } catch(IOException e) {
                // Handle it
            }
        }
    }
}
