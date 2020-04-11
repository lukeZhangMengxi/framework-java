package java_nio.copy_file_channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class CopyFile {
    public static void main(String[] args) throws IOException {
        String src = "java_nio/copy_file_channel/input.txt";
        String dest = "java_nio/copy_file_channel/output.txt";

        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);

        FileChannel inputChannel = fis.getChannel();
        FileChannel outputChannel = fos.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(64);
        while (true) {
            int read = inputChannel.read(buf);

            if (read == -1) break;

            buf.flip();

            outputChannel.write(buf);
            buf.clear();
        }

        fis.close();
        fos.close();
        
    }
}
