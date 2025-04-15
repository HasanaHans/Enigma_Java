import java.util.Scanner;
import java.io.File;
import  java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class EnigmaEncoder {

    // Enigma Settings
    static String[] rotors = {"III", "II", "I"};
    static String reflector = "C";
    static String ringSettings = "ACA";
    static String ringPositions = "AGA";
    static String plugboard = "AZ BP CH DM EM FS GW JY KT LQ ";

    public static String encode(String plaintext) {
        // Enigma Rotors and reflectors
        String[] rotor1 = {
                "EKMFLGDQVZNTOWYHXUSPAIBRCJ",
                "Q"
        };
        String[] rotor2 = {
                "AJDKSIRUXBLHWTMCQGZNPYFVOE",
                "E"
        };
        String[] rotor3 = {
                "BDFHJLCPRTXVZNYEIWGAKMUSQO",
                "V"
        };
        String[] rotor4 = {
                "ESOVPZJAYQUIRHXLNFTGKDCMWB",
                "J"
        };
        String[] rotor5 = {
                "VZBRGITYUPSDNHLXAWMJQOFECK",
                "Z"
        };

        String[][] rotorArr = {rotor1, rotor2, rotor3, rotor4, rotor5};

        char[][] reflectorB = {
                {'A', 'Y'},
                {'Y', 'A'},
                {'B', 'R'},
                {'R', 'B'},
                {'C', 'U'},
                {'U', 'C'},
                {'D', 'H'},
                {'H', 'D'},
                {'E', 'Q'},
                {'Q', 'E'},
                {'F', 'S'},
                {'S', 'F'},
                {'G', 'L'},
                {'L', 'G'},
                {'I', 'P'},
                {'P', 'I'},
                {'J', 'X'},
                {'X', 'J'},
                {'K', 'N'},
                {'N', 'K'},
                {'M', 'O'},
                {'O', 'M'},
                {'T', 'Z'},
                {'Z', 'T'},
                {'V', 'W'},
                {'W', 'V'}
        };

        char[][] reflectorC = {
                {'A', 'F'},
                {'F', 'A'},
                {'B', 'V'},
                {'V', 'B'},
                {'C', 'P'},
                {'P', 'C'},
                {'D', 'J'},
                {'J', 'D'},
                {'E', 'I'},
                {'I', 'E'},
                {'G', 'O'},
                {'O', 'G'},
                {'H', 'Y'},
                {'Y', 'H'},
                {'K', 'R'},
                {'R', 'K'},
                {'L', 'Z'},
                {'Z', 'L'},
                {'M', 'X'},
                {'X', 'M'},
                {'N', 'W'},
                {'W', 'N'},
                {'Q', 'T'},
                {'T', 'Q'},
                {'S', 'U'},
                {'U', 'S'}
        };

        char[][] reflectorArr = reflector.equals("B") ? reflectorB : reflectorC;

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char leftNotch;
        char middleNotch;
        char rightNotch;

        // A = Left, B = Mid, C=Right
        String leftRotor = rotorArr[getIndex(rotors[0])][0];
        String middleRotor = rotorArr[getIndex(rotors[1])][0];
        String rightRotor = rotorArr[getIndex(rotors[2])][0];
        leftNotch = rotorArr[getIndex(rotors[0])][1].charAt(0);
        middleNotch = rotorArr[getIndex(rotors[1])][1].charAt(0);
        rightNotch = rotorArr[getIndex(rotors[2])][1].charAt(0);

        char leftLetter = ringPositions.charAt(0);
        char middleLetter = ringPositions.charAt(1);
        char rightletter = ringPositions.charAt(2);

        char leftRotorSetting = ringSettings.charAt(0);
        int offsetLeftRtrSetting = alphabet.indexOf(leftRotorSetting);
        char middleRotorSetting = ringSettings.charAt(1);
        int offsettMiddleRotorSetting = alphabet.indexOf(middleRotorSetting);
        char rightRotorSetting = ringSettings.charAt(2);
        int offsettRighRotorSetting = alphabet.indexOf(rightRotorSetting);

        leftRotor = offSetter(leftRotor, offsetLeftRtrSetting);
        middleRotor = offSetter(middleRotor, offsettMiddleRotorSetting);
        rightRotor = offSetter(rightRotor, offsettRighRotorSetting);

        if (offsetLeftRtrSetting > 0) {
            leftRotor = leftRotor.substring(26 - offsetLeftRtrSetting) + leftRotor.substring(0, 26 - offsetLeftRtrSetting);
        }
        if (offsettMiddleRotorSetting > 0) {
            middleRotor = middleRotor.substring(26 - offsettMiddleRotorSetting) + middleRotor.substring(0, 26 - offsettMiddleRotorSetting);
        }
        if (offsettRighRotorSetting > 0) {
            rightRotor = rightRotor.substring(26 - offsettRighRotorSetting) + rightRotor.substring(0, 26 - offsettRighRotorSetting);
        }

        StringBuilder ciphertext = new StringBuilder();

        // Convert plugboard settings into a 2D array
        String[] plugboardConnections = plugboard.toUpperCase().split(" ");
        char[][] plugboardArr = new char[26][2];
        for (String pair : plugboardConnections) {
            if (pair.length() == 2) {
                plugboardArr[pair.charAt(0) - 'A'][0] = pair.charAt(1);
                plugboardArr[pair.charAt(1) - 'A'][0] = pair.charAt(0);
            }
        }

        plaintext = plaintext.toUpperCase();
        for (char letter : plaintext.toCharArray()) {
            char encryptedLetter = letter;

            if (Character.isLetter(letter)) {
                // Rotate Rotors - This happens as soon as a key is pressed, before encrypting the letter.
                boolean rotorTrigger = false;
                // Third rotor rotates by 1 for every key being pressed
                if (rightletter == rightNotch) {
                    rotorTrigger = true;
                }
                rightletter = alphabet.charAt((alphabet.indexOf(rightletter) + 1) % 26);
                // Check if middleRotor needs to rotate
                if (rotorTrigger) {
                    rotorTrigger = false;
                    if (middleLetter == middleNotch) {
                        rotorTrigger = true;
                    }
                    middleLetter = alphabet.charAt((alphabet.indexOf(middleLetter) + 1) % 26);

                    // Check if leftRotor needs to rotate
                    if (rotorTrigger) {
                        rotorTrigger = false;
                        leftLetter = alphabet.charAt((alphabet.indexOf(leftLetter) + 1) % 26);
                    }
                } else {
                    // Check for double step sequence.
                    if (middleLetter == middleNotch) {
                        middleLetter = alphabet.charAt((alphabet.indexOf(middleLetter) + 1) % 26);
                        leftLetter = alphabet.charAt((alphabet.indexOf(leftLetter) + 1) % 26);
                    }
                }

                // Implement plugboard encryption.
                if (plugboardArr[letter - 'A'][0] != '\0') {
                    encryptedLetter = plugboardArr[letter - 'A'][0];
                }

                // Rotors & Reflector Encryption
                int offsetA = alphabet.indexOf(leftLetter);
                int offsetB = alphabet.indexOf(middleLetter);
                int offsetC = alphabet.indexOf(rightletter);

                // Wheel 3 Encryption
                int pos = alphabet.indexOf(encryptedLetter);
                char let = rightRotor.charAt((pos + offsetC) % 26);
                pos = alphabet.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetC + 26) % 26);

                // Wheel 2 Encryption
                pos = alphabet.indexOf(encryptedLetter);
                let = middleRotor.charAt((pos + offsetB) % 26);
                pos = alphabet.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetB + 26) % 26);

                // Wheel 1 Encryption
                pos = alphabet.indexOf(encryptedLetter);
                let = leftRotor.charAt((pos + offsetA) % 26);
                pos = alphabet.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetA + 26) % 26);

                // Reflector encryption.
                for (char[] pair : reflectorArr) {
                    if (pair[0] == encryptedLetter) {
                        encryptedLetter = pair[1];
                        break;
                    }
                }

                // Back through the rotors
                // Wheel 1 Encryption
                pos = alphabet.indexOf(encryptedLetter);
                let = alphabet.charAt((pos + offsetA) % 26);
                pos = leftRotor.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetA + 26) % 26);

                // Wheel 2 Encryption
                pos = alphabet.indexOf(encryptedLetter);
                let = alphabet.charAt((pos + offsetB) % 26);
                pos = middleRotor.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetB + 26) % 26);

                // Wheel 3 Encryption
                pos = alphabet.indexOf(encryptedLetter);
                let = alphabet.charAt((pos + offsetC) % 26);
                pos = rightRotor.indexOf(let);
                encryptedLetter = alphabet.charAt((pos - offsetC + 26) % 26);

                // Implement plugboard encryption!
                if (plugboardArr[encryptedLetter - 'A'][0] != '\0') {
                    encryptedLetter = plugboardArr[encryptedLetter - 'A'][0];
                }
            }
            ciphertext.append(encryptedLetter);
        }

        return ciphertext.toString();
    }

    public static String offSetter(String str, int amount) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (((c - 'A' + amount) % 26) + 'A');
                output.append(shiftedChar);
            } else {
                output.append(c);
            }
        }

        return output.toString();
    }

    public static int getIndex(String rotor) {
        switch (rotor) {
            case "I":
                return 0;
            case "II":
                return 1;
            case "III":
                return 2;
            case "IV":
                return 3;
            case "V":
                return 4;
            default:
                return -1;
        }
    }

    public static String readFile(){
        // Opening the plaintext file.
        File plaintextFile = new File("textFiles/plaintext.txt");
        Scanner reader;
        try {
            reader = new Scanner(plaintextFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Reading the plaintext file.
        String plaintext = "";
        while (reader.hasNextLine()) {
            plaintext = plaintext + reader.nextLine();
        }
        reader.close();
        return plaintext;
    }

    public static void writeFile(String plaintext){
        FileWriter writer;
        try {
            writer = new FileWriter("textFiles/ciphertext.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String ciphertext = encode(plaintext);

        try {
            writer.write(ciphertext);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        String plaintext = readFile();
        writeFile(plaintext);


    }
}
