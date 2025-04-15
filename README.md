# 🔐 EnigmaEncoder (Java)

This Java program simulates the **Enigma machine**, a cipher device famously used during World War II to encrypt messages.

The program reads a plaintext message from a file, encrypts it using Enigma-like logic (rotors, reflector, ring settings, and plugboard), and writes the resulting ciphertext to a new file.

---

## 📌 What the Code Does

- **Reads a plaintext message** from `textFiles/plaintext.txt`.
- Uses preset **Enigma machine settings** including:
  - **Rotors** (I–V) with their specific wiring and notch positions
  - A **Reflector** (either B or C)
  - **Ring settings** and **starting rotor positions**
  - A **plugboard** that swaps letter pairs before and after encryption
- **Encrypts each letter** by passing it through:
  1. The plugboard
  2. Three rotors (right to left)
  3. The reflector
  4. Back through the rotors (left to right)
  5. The plugboard again
- Handles **rotor stepping**, including the Enigma’s unique **double-stepping mechanism**
- **Writes the encrypted message** (ciphertext) to `textFiles/ciphertext.txt`

---

## 🧪 How to Use It

1. Add your message to `textFiles/plaintext.txt`.
2. Compile the Java file:
   ```bash
   javac EnigmaEncoder.java
3. Run java EnigmaEncoder
