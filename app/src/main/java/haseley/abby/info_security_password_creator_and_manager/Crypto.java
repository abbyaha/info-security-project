package haseley.abby.info_security_password_creator_and_manager;

// Author: Daniele Bellutta
// April 2016

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public final class Crypto {

    private static final int KEYLENGTH = 16;
    private static final int NUMROUNDS = 4;
    private static final int BUFFERLENGTH = 16;

    // This is a private constructor to prevent the class from being instantiated.
    private Crypto() {
        // Do nothing.
    }

    private static byte[] vernam(byte[] key, byte[] data) {
        byte ciphertext[] = null;
        if (key.length == data.length) {
            ciphertext = new byte[data.length];

            for (int i = 0; i < data.length; i++) {
                ciphertext[i] = (byte) (data[i] ^ key[i]);
            }
        }
        return ciphertext;
    }

    private static boolean contains(int[] intArray, int targetValue) {
        boolean answer = false;

        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] == targetValue) {
                answer = true;
                break;
            }
        }

        return answer;
    }

    private static byte[] transpose(Random random, byte[] data) {
        byte[] transposed = new byte[data.length];
        int[] history = new int[data.length];
        Arrays.fill(history, -1);

        for (int i = 0; i < data.length; i++) {
            int randomIndex = random.nextInt(transposed.length);
            while (contains(history, randomIndex)) {
                randomIndex = random.nextInt(transposed.length);
            }
            history[i] = randomIndex;

            transposed[randomIndex] = data[i];
        }

        return transposed;
    }

    private static byte[] untranspose(Random random, byte[] data) {
        byte[] untransposed = new byte[data.length];
        int[] history = new int[data.length];
        Arrays.fill(history, -1);

        for (int i = 0; i < data.length; i++) {
            int randomIndex = random.nextInt(data.length);
            while (contains(history, randomIndex)) {
                randomIndex = random.nextInt(data.length);
            }
            history[i] = randomIndex;

            untransposed[i] = data[randomIndex];
        }

        return untransposed;
    }

    private static byte[] addPadding(byte[] data) {
        int length16 = (data.length + (KEYLENGTH - ((data.length)%KEYLENGTH)));
        int numPadded = length16 - data.length;

        if (numPadded == 0) {
            length16 = length16 + KEYLENGTH;
            numPadded = KEYLENGTH;
        }

        byte padded[] = new byte[length16];
        System.arraycopy(data, 0, padded, 0, data.length);

        for (int i = data.length; i < length16; i++) {
            padded[i] = (byte)(numPadded);
        }

        return padded;
    }

    private static byte[] removePadding(byte[] data) {
        int numPadded = (int)(data[data.length - 1]);
        int unpaddedLength = data.length - numPadded;

        byte unpadded[] = new byte[unpaddedLength];

        System.arraycopy(data, 0, unpadded, 0, unpaddedLength);

        return unpadded;
    }

    public static byte[] encrypt(byte[] key, byte[] plaintext) {
        byte padded[] = addPadding(plaintext);
        byte ciphertext[] = new byte[padded.length];

        for (int i = 0; i < ciphertext.length; i = i + KEYLENGTH) {
            byte ciphertextBlock[] = new byte[KEYLENGTH];
            System.arraycopy(padded, i, ciphertextBlock, 0, KEYLENGTH);

            for (int r = 0; r < NUMROUNDS; r++) {
                byte[] subKey = new byte[BUFFERLENGTH];
                Arrays.fill(subKey, (byte)(0));
                System.arraycopy(key, (r*(KEYLENGTH/NUMROUNDS)), subKey, 0, KEYLENGTH/NUMROUNDS);

                ByteBuffer enBuffer = ByteBuffer.wrap(subKey);
                Random enRandom = new Random(enBuffer.getLong());

                ciphertextBlock = transpose(enRandom, ciphertextBlock);
                ciphertextBlock = vernam(key, ciphertextBlock);
            }

            System.arraycopy(ciphertextBlock, 0, ciphertext, i, ciphertextBlock.length);
        }

        return ciphertext;
    }

    public static byte[] decrypt(byte[] key, byte[] ciphertext) {
        byte padded[] = new byte[ciphertext.length];

        for (int i = 0; i < ciphertext.length; i = i + KEYLENGTH) {
            byte plaintextBlock[] = new byte[KEYLENGTH];
            System.arraycopy(ciphertext, i, plaintextBlock, 0, KEYLENGTH);

            for (int r = 1; r <= NUMROUNDS; r++) {
                byte[] subKey = new byte[BUFFERLENGTH];
                Arrays.fill(subKey, (byte)(0));
                System.arraycopy(key, (key.length - (r*(KEYLENGTH/NUMROUNDS))), subKey, 0, KEYLENGTH/NUMROUNDS);

                ByteBuffer deBuffer = ByteBuffer.wrap(subKey);
                Random deRandom = new Random(deBuffer.getLong());

                plaintextBlock = vernam(key, plaintextBlock);
                plaintextBlock = untranspose(deRandom, plaintextBlock);
            }

            System.arraycopy(plaintextBlock, 0, padded, i, plaintextBlock.length);
        }

        return removePadding(padded);
    }
}
