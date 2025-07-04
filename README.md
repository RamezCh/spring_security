# Spring Security

This is a test field where I apply different security principles and methods starting from the most basic to advanced.

## Encoding

Encoding is converting data from one form to another without any secret key. It is easily reversible and not used for securing data. E.g., Base64 or UNICODE or ASCII

## Encryption

There are two main types of encryption: symmetric encryption and asymmetric encryption.

### Symmetric Encryption
In symmetric encryption, the same secret key is used for both encryption and decryption. This makes it fast and efficient, ideal for encrypting large volumes of data, such as data at rest (e.g., files stored on disk or in AWS S3 buckets).

However, the security of this method depends heavily on how well the secret key is protected. If the key is exposed or mishandled by any party involved, the data becomes vulnerable. Because of the difficulty in securely sharing the key across multiple parties, symmetric encryption is best suited for situations where only one or a few trusted parties need access, such as within a single application or system.

### Asymmetric Encryption
Asymmetric encryption uses a pair of mathematically related keys: a public key (shared openly) for encryption, and a private key (kept secret) for decryption.

Anyone can use the public key to encrypt data, but only the holder of the matching private key can decrypt it. This eliminates the need to share a secret key over insecure channels, making it ideal for secure communications between untrusted parties, such as in data in transit scenarios.

For example, if Application A wants to securely receive data from Application B:

1. Application A generates a public-private key pair and shares the public key with Application B.
2. Application B uses the public key to encrypt the data before sending it.
3. Only Application A can decrypt the message using its private key.
This approach ensures that even if the encrypted data is intercepted, it remains unreadable without the private key. Asymmetric encryption is commonly used in protocols like HTTPS to establish secure communication and exchange symmetric keys securely.

```shell
# enc is encrypt
# AES-256 is an encryption algorithm (Advanced Encryption Standard) 256 is key size in bits
# cbc means Cipher Block chaining
# 12345 is the password used to generate the encryption key
# -pbkdf2 is Password-Based key Derivation Function 2 used to derive key and IV (Initialization Vector aka Random value) from the password
# Basically adds randomness to the password and applies it multiple times so it becomes stronger
# -in is input
# -out is output
# base64 encodes in base64 after previous steps done
openssl enc -aes-256-cbc -pass pass:12345 -pbkdf2 -in plain.txt -out encrypted_text.txt -base64
# If you do not use -base64 at the end its not readable so we use it to convert from binary to ASCII text

# to decrypt use below
openssl enc -aes-256-cbc -base64 -pass pass:12345 -d -pbkdf2 -in encrypted_text.txt -out decrypt.txt
# pass must match, if it doesn't gives error
# -d means decrypt
```

## Hashing

Hashing is a one-way process once data is hashed, it cannot be reversed to retrieve the original input.
It uses mathematical algorithms to convert input data (like a text or files) into a fixed-length string called a hash or digest.

Key properties:

- **Deterministic**: The same input always gives the same hash.
- **Sensitive** to change: Even a small change in the input results in a completely different hash.
- **Fixed output size**: No matter how big or small the input is, the hash will always have the same length. For example, SHA-256 always outputs a 256-bit hash, typically displayed as a 64-character hexadecimal string.

Think of hashing like making a smoothie:
Fruits → Blender → Smoothie

But you can’t reverse the process:
Smoothie → Blender → Fruits?

Common uses include verifying data integrity, storing passwords securely, and ensuring message authenticity in cryptographic systems.

```shell
echo -n "Hackerman@12345" | openssl dgst -sha256 # We always get the same output
```

### So You think you are safe from hackers? Think again.

When you log in, the password is converted to a hash value using the same hashing algorithm of the saved password in the DB then compared together, and if the hash value is the same, the login is successful.

A hacker can attempt to guess a user's password by trying different combinations. One method is a brute-force attack, where every possible password combination is systematically tried until a match is found. Another common method is a dictionary attack, where the hacker uses a list of commonly used passwords such as those found in files like rockyou.txt to guess the correct one.

Once the hacker finds a password that, when hashed, matches a hash stored in the database, they know it's the correct password. They can then search the entire database for other accounts with the same hash, giving them access to potentially thousands of accounts that use the same password.

Additionally, hackers may use rainbow tables precomputed databases of password hashes to quickly look up the original password from a given hash. This allows them to bypass the need to compute hashes manually, speeding up the cracking process significantly.

### So how do we overcome those drawbacks?

We should use salts. For example, if the password is '12345' the backend generates a random salt, combines it with the password (e.g., prepends it), then hashes the result. Both the salt and the resulting hash are stored together. The structure of the generated hash depends on the algorithm used. For instance, in bcrypt, the output string typically includes:
- The algorithm version,
- The cost factor,
- The salt and
- The hashed password,

All are encoded into a single string for easy storage and verification. However, this approach alone primarily helps prevent precomputed attacks like rainbow tables (each password has a unique salt value so no longer all similar hashes) and does not protect against weak passwords being guessed through brute-force or dictionary attacks.

### What else could we do for brute force and dictionary attacks?

We can set a maximum of three login attempts and impose password restrictions, such as requiring at least eight characters including numbers, letters, and special characters.

To protect against brute-force attacks, we can use adaptive password hashing algorithms like Bcrypt, Scrypt, or Argon2. These algorithms are designed to be computationally intensive (and in some cases, memory-intensive), making them resistant to large-scale brute-force or dictionary attacks. They can be tuned to take a specific amount of time typically around one second to compute a hash, which significantly increases the resources (CPU, memory, time) required by an attacker attempting to crack passwords.

For legitimate users, this delay is negligible since login events are infrequent, and users are usually kept authenticated via session tokens after the initial login. Even in cases where a user does log in manually, a short delay of one or two seconds doesn’t negatively impact the user experience.

This trade-off makes such algorithms ideal for securely storing passwords in modern systems.

## PasswordEncoder in Spring Security

The PasswordEncoder interface includes an encode method which accepts a CharSequence (the raw password) and returns its hashed representation as a String.

Another important method is matches, which takes two parameters: rawPassword (as a CharSequence) and encodedPassword (as a String). It returns a boolean indicating whether the provided raw password, when encoded, matches the stored encoded password. This method is used during authentication to verify user credentials.

The last method is upgradeEncoding that takes encodedPassword as String and checks if it should be encoded again for better security and if so, returns true else false. By default, a single time hashing along a salt value is enough.

### DelegatingPasswordEncoder

It uses the default password encoder setup by the framework. At this time it is bcrypt

### NoOpPasswordEncoder

This is a deprecated encoder used for demo purposes and NOT in production. It does not hash the password or anything. It just returns the raw Password input.

### StandardPasswordEncoder

This is deprecated as well. It uses SHA-256 hashing with 1024 iterations and a random 8-byte salt value. This is no longer strong due to advancements in CPU and GPU systems.

### Pbkdf2PasswordEncoder

Has a default salt value of 16 bytes and the number of iterations 310 thousand. These settings are configurable. The more iterations, the longer the hash process, the slower the hacks can iterate. You can also add a secret key using .SecretKeyFactoryAlgorithm.

This password encoder strength depends on the secret key and the function you provide, so it is not strong and not recommended in production.

### BCryptPasswordEncoder

It has three different versions 2a, 2b, 2y. We can select one of them or let the Spring Security use the default version. The strength parameter is a value between 4 and 31. The higher, the stronger. It sets the number of log rounds to use.

The default strength value is 10.

### SCryptPasswordEncoder

It is an advanced hashing method of BCrypt. You can set the CPU cost, Memory Cost, and parallelization parameter. This makes the hacker's life much harder.

### Argon2PasswordEncoder

Is even a more advanced hashing algorithm of SCryptPasswordEncoder offering similar parameters which won the most secure hashing algorithm contest.