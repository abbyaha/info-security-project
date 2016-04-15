package haseley.abby.info_security_password_creator_and_manager;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

// Author: Daniele Bellutta
// April 2016

public final class Crypto {

    private static final int KEYLENGTH = 16;
    private static final int NUMROUNDS = 4;
    private static final int BUFFERLENGTH = 16;

    // This is a private constructor to prevent the class from being instantiated.
    private Crypto() {
        // Do nothing.
    }

    // This is a simple XOR cipher method. It takes in the key and the data to encrypt or decrypt.
    private static byte[] vernam(byte[] key, byte[] data) {
        byte ciphertext[] = null;

        // Check that the data is of the correct length.
        if (key.length == data.length) {
            ciphertext = new byte[data.length];

            // Loop through each byte in the data and XOR it with the corresponding byte in the key.
            for (int i = 0; i < data.length; i++) {
                ciphertext[i] = (byte) (data[i] ^ key[i]);
            }
        }
        return ciphertext;
    }

    // This is a simple method to check if an integer (the "targetValue") appears in an array of
    // integers ("intArray").
    private static boolean contains(int[] intArray, int targetValue) {
        boolean answer = false;

        // Loop through the elements of the array of integers and check if any of them are equal to
        // the target value.
        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] == targetValue) {
                answer = true;
                break;
            }
        }

        return answer;
    }

    // This is the method for the transposition round. It takes in a Random object, which is
    // expected to have been seeded, and the data to transpose.
    private static byte[] transpose(Random random, byte[] data) {
        byte[] transposed = new byte[data.length];
        // Create an array to store the history of previously generated random indices (so as to
        // prevent collisions).
        int[] history = new int[data.length];
        Arrays.fill(history, -1);

        // Loop through the bytes in the data and transpose them to new, random positions.
        for (int i = 0; i < data.length; i++) {
            // Generate a new random index for the current byte.
            int randomIndex = random.nextInt(transposed.length);
            // Check if this index has already been created. If it has, then continue generating
            // a new one until one that has not already been taken is found.
            while (contains(history, randomIndex)) {
                randomIndex = random.nextInt(transposed.length);
            }
            // Add the generated index to the history.
            history[i] = randomIndex;

            // Transpose the current byte to its new position.
            transposed[randomIndex] = data[i];
        }

        return transposed;
    }

    // This is the method to "untranspose" the data during decryption. It works in much the same
    // way as the "transpose()" method.
    private static byte[] untranspose(Random random, byte[] data) {
        byte[] untransposed = new byte[data.length];
        // Create an array to store the history of previously generated random indices (so as to
        // prevent collisions).
        int[] history = new int[data.length];
        Arrays.fill(history, -1);

        // Loop through the bytes in the data and transpose them to their original positions.
        for (int i = 0; i < data.length; i++) {
            // Generate a new random index for the current byte.
            int randomIndex = random.nextInt(data.length);
            // Check if this index has already been created. If it has, then continue generating
            // a new one until one that has not already been taken is found.
            while (contains(history, randomIndex)) {
                randomIndex = random.nextInt(data.length);
            }
            // Add the generated index to the history.
            history[i] = randomIndex;

            // Go to the position in the data, grab that value, and put it back in its proper place.
            untransposed[i] = data[randomIndex];
        }

        return untransposed;
    }

    // This is a method to pad the data to a multiple of 128 bits. It simply pads the correct
    // number of bytes, with each byte of padding containing the number of bytes that were padded.
    private static byte[] addPadding(byte[] data) {
        // Calculate how much padding is needed.
        int length16 = (data.length + (KEYLENGTH - ((data.length)%KEYLENGTH)));
        int numPadded = length16 - data.length;

        // Even if the data already has a length that is a multiple of 128 bits, we still need to
        // pad.
        if (numPadded == 0) {
            length16 = length16 + KEYLENGTH;
            numPadded = KEYLENGTH;
        }

        byte padded[] = new byte[length16];
        System.arraycopy(data, 0, padded, 0, data.length);

        // Actually pad the data with the number of bytes that were padded.
        for (int i = data.length; i < length16; i++) {
            padded[i] = (byte)(numPadded);
        }

        return padded;
    }

    // This method removes extra padding from the data passed into it.
    private static byte[] removePadding(byte[] data) {
        int numPadded = (int)(data[data.length - 1]);
        int unpaddedLength = data.length - numPadded;

        // Check how many bytes were padded.
        byte unpadded[] = new byte[unpaddedLength];

        // Remove the padding.
        System.arraycopy(data, 0, unpadded, 0, unpaddedLength);

        return unpadded;
    }

    // This is the method to encrypt data. It takes in the encryption key and the plaintext to
    // encrypt.
    public static byte[] encrypt(byte[] key, byte[] plaintext) {
        // Pad the plaintext.
        byte padded[] = addPadding(plaintext);
        byte ciphertext[] = new byte[padded.length];

        // Split the padded plaintext into blocks.
        for (int i = 0; i < ciphertext.length; i = i + KEYLENGTH) {
            byte ciphertextBlock[] = new byte[KEYLENGTH];
            System.arraycopy(padded, i, ciphertextBlock, 0, KEYLENGTH);

            // Perform the four rounds of encryption for the current block.
            for (int r = 0; r < NUMROUNDS; r++) {
                byte[] subKey = new byte[BUFFERLENGTH];
                Arrays.fill(subKey, (byte)(0));
                // Grab one quarter of the key to use as the seed for the Random object for the
                // current round of encryption.
                System.arraycopy(key, (r*(KEYLENGTH/NUMROUNDS)), subKey, 0, KEYLENGTH/NUMROUNDS);

                // Initialize the Random object for the transposition stage.
                ByteBuffer enBuffer = ByteBuffer.wrap(subKey);
                Random enRandom = new Random(enBuffer.getLong());

                // Perform the transposition stage.
                ciphertextBlock = transpose(enRandom, ciphertextBlock);
                // Perform the substitution stage.
                ciphertextBlock = vernam(key, ciphertextBlock);
            }

            System.arraycopy(ciphertextBlock, 0, ciphertext, i, ciphertextBlock.length);
        }

        return ciphertext;
    }

    public static byte[] decrypt(byte[] key, byte[] ciphertext) {
        byte padded[] = new byte[ciphertext.length];

        // Split the ciphertext into blocks.
        for (int i = 0; i < ciphertext.length; i = i + KEYLENGTH) {
            byte plaintextBlock[] = new byte[KEYLENGTH];
            System.arraycopy(ciphertext, i, plaintextBlock, 0, KEYLENGTH);

            // Perform the four rounds of decryption for the current block.
            for (int r = 1; r <= NUMROUNDS; r++) {
                byte[] subKey = new byte[BUFFERLENGTH];
                Arrays.fill(subKey, (byte)(0));
                // Grab one quarter of the key to use as the seed for the Random object for the
                // current round of encryption. Notice that this is done in reverse with respect to
                // the encryption method.
                System.arraycopy(key, (key.length - (r*(KEYLENGTH/NUMROUNDS))), subKey, 0, KEYLENGTH/NUMROUNDS);

                // Initialize the Random object for the transposition stage.
                ByteBuffer deBuffer = ByteBuffer.wrap(subKey);
                Random deRandom = new Random(deBuffer.getLong());

                // First, perform the substitution stage (this is the reverse of what happens
                // during encryption).
                plaintextBlock = vernam(key, plaintextBlock);
                // Then perform the transposition stage.
                plaintextBlock = untranspose(deRandom, plaintextBlock);
            }

            // Put the decrypted block into the "padded" array.
            System.arraycopy(plaintextBlock, 0, padded, i, plaintextBlock.length);
        }

        // Finally, remove the padding.
        return removePadding(padded);
    }
}
