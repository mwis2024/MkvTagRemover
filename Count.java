import java.io.FileInputStream;
import java.io.IOException;

public class Count {
    public static int execute(String IN_FILE){
        try (FileInputStream fis = new FileInputStream(IN_FILE)) {
            int sequenceFoundCount = 0;
            int length;
            final byte[] key = {18,84,-61,103};

            byte[] dataBuffer = new byte[4096];
            while((length = fis.read(dataBuffer)) != -1){
                if(length != dataBuffer.length){
                    byte[] temp = new byte[length];
                    for(int i = 0; i < length; i++){
                        temp[i] = dataBuffer[i];
                    }
                    dataBuffer = temp;
                }
                if(find(dataBuffer, key))
                    sequenceFoundCount++;
            }
            return sequenceFoundCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }
    
    private static boolean find(byte[] buffer, byte[] key) {
        for (int i = 0; i <= buffer.length - key.length; i++) {
            int j = 0;
            while (j < key.length && buffer[i + j] == key[j]) {
                j++;
            }
            if (j == key.length) {
               return true;
            }
        }
        return false;
    }
}
