import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main{
    public static void main(String[] args) {
        String IN_FILE = args[0];
        String OUT_FILE = args[1];
        int sequencesToFind = Count.execute(IN_FILE);
        try (FileInputStream fis = new FileInputStream(IN_FILE); FileOutputStream fout = new FileOutputStream(OUT_FILE)) {
            int sequenceFoundCount = 0;
            int length;
            boolean inTagArea = false;
            final byte[] key = {18,84,-61,103};
            final byte[] key2 = {103, -56};
            final byte[] key3 = {99, -64};

            byte[] dataBuffer = new byte[4096];
            StringBuilder tagData = new StringBuilder();
            while((length = fis.read(dataBuffer)) != -1){
                if(length != dataBuffer.length){
                    byte[] temp = new byte[length];
                    for(int i = 0; i < length; i++){
                        temp[i] = dataBuffer[i];
                    }
                    dataBuffer = temp;
                }
                if(!inTagArea){
                    if(find(dataBuffer, key))
                        sequenceFoundCount++;
                    if(sequenceFoundCount == sequencesToFind){
                        inTagArea = true;
                        byte[][] temp = splitArrays(dataBuffer, key3);
                        fout.write(temp[0]);
                        // if(temp[1] != null)
                        //     tagData.append(temp[1]);
                        continue;
                    }
                    fout.write(dataBuffer, 0, length);
                }else{
                    //tagData.append(IntArrayToString(byteArrayToIntArray(dataBuffer)));
                }
            }
            // ArrayList<byte[]> tagsLeft = stringToByteArray(trimTags(tagData.toString(), "SUBTITLE"));
            // for(byte[] x : tagsLeft)
            //     fout.write(x);




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPossiblySequence(ArrayList<Integer> buffer, int... values){
        int max = buffer.size() < values.length ? buffer.size() : values.length;
        for(int i = 0; i < max; i++){
            if(buffer.get(i) != values[i])
                return false;
        }
        return true;
    }

    private static boolean hasSequenceEnded(ArrayList<Integer> buffer, int... values){
        for(int i = 0; i < values.length; i++){
            if(buffer.get(buffer.size() - 1 - i) != values[values.length - 1 - i])
                return false;
        }
        return true;
    }

    private static boolean hasSoughtTag(ArrayList<Integer> buffer, String tagName){
        StringBuilder sb = new StringBuilder();
        for(Integer x : buffer)
            sb.append((char)x.intValue());
        return sb.toString().contains(tagName);
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

    private static byte[][] splitArrays(byte[] buffer, byte[] key){
        byte[][] wrapper = new byte[2][];
        for (int i = 0; i <= buffer.length - key.length; i++) {
            int j = 0;
            while (j < key.length && buffer[i + j] == key[j]) {
                j++;
            }
            if (j == key.length) {
               wrapper[0] = Arrays.copyOfRange(buffer, 0, i+j+1);
               wrapper[1] = Arrays.copyOfRange(buffer,i+j+1, buffer.length);
               break;
            }
        }
        if(wrapper[0] == null)
            wrapper[0] = buffer;
        return wrapper;
    }

    private static String trimTags(String s, String tagName){
        String[] ss = s.split((char)103 + (char)200 +"[^" + (char)103 + (char)200 + "]*"+ tagName +"[^" + (char)103 + (char)200 + "]*(?=" + (char)103 + (char)200 +")");
        StringBuilder sb = new StringBuilder();
        for(String x : ss)
            sb.append(x);
        return sb.toString();
    }

    private static int[] byteArrayToIntArray(byte[] buffer){
        int[] temp = new int[buffer.length];
        for(int i = 0; i < buffer.length; i++){
            temp[i] = buffer[i] & 0xFF;
        }
        return temp;
    }

    private static String IntArrayToString(int[] buffer){
        StringBuilder sb = new StringBuilder();
        for(int x : buffer){
            sb.append((char)x);
        }
        return sb.toString();
    }

    private static ArrayList<byte[]> stringToByteArray(String s){
        ArrayList<byte[]> list = new ArrayList<>();
        int j;
        for(int i = 0; i < s.length(); i++){
            byte[] temp = new byte[4096];
            for(j = 0; j < 4096; j++){
                temp[j] = (byte)s.charAt(j);
            }
            if(j != 4095){
                byte[] temp2 = new byte[j];
                for(int k = 0; k < temp2.length; k++){
                    temp2[k] = temp[k];
                }
                list.add(temp2);
                break;
            }
            list.add(temp);
        }
        return list;
    }


}