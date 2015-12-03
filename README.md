# Encryption Package
This is a simple private key encryption package useful for encrypting and decrypting text.
Three ciphers are currently implemented: Affine, Vigenere, and a custom Matrix cipher.

#Overview
In general these private key ciphers work using modular arithmetic.
To do this, one must generate an element 'a' that must have a multiplicative inverse and an element 'b' that must have an additive inverse.
The former element 'a' has a more difficult condition to meet, but given valid values for 'a' and 'b' one can use the following simple function:

f[x] = (a * x) + b

f^(-1)[x] = a^(-1) * (x-b)

for a given 'x', an individual or string of characters.

#Future Plans
There are plans to create a larger application allowing for easier user interfacing.
