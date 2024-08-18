package com.example.demo;

/*
Question1 b)

Imagine you have a secret decoder ring with rotating discs labeled with the lowercase alphabet. You're given a
message s written in lowercase letters and a set of instructions shifts encoded as tuples (start_disc, end_disc,
direction). Each instruction represents rotating the discs between positions start_disc and end_disc (inclusive)
either clockwise (direction = 1) or counter-clockwise (direction = 0). Rotating a disc shifts the message by one
letter for each position moved on the alphabet (wrapping around from ‘z’ to ‘a’ and vice versa).
*/


public class SecretDecoder {

    // Helper function to rotate a single character
    private static char rotateChar(char c, int shift) {
        int originalIndex = c - 'a';
        int newIndex = (originalIndex + shift + 26) % 26; // Adding 26 to handle negative shifts
        return (char) ('a' + newIndex);
    }

    // Main function to decipher the message
    public static String decipherMessage(String s, int[][] shifts) {
        // Convert the string to a character array for in-place modification
        char[] chars = s.toCharArray();
        int length = chars.length;

        // Create an array to store cumulative rotations for each position
        int[] rotation = new int[length];

        // Apply each shift instruction to the cumulative rotation array
        for (int[] shift : shifts) {
            int startDisc = shift[0];
            int endDisc = shift[1];
            int direction = shift[2];
            int shiftAmount = direction == 1 ? 1 : -1; // Clockwise is +1, counter-clockwise is -1

            // Apply the rotation to the rotation array
            for (int i = startDisc; i <= endDisc; i++) {
                rotation[i] += shiftAmount;
            }
        }

        // Rotate each character based on the cumulative rotation
        for (int i = 0; i < length; i++) {
            chars[i] = rotateChar(chars[i], rotation[i]);
        }

        // Convert the character array back to a string
        return new String(chars);
    }

    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};

        System.out.println(decipherMessage(s, shifts)); // Output: jglko
    }
}
