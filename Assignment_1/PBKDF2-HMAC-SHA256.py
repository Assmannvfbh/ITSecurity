import hashlib
import time

psw = bytes("padpassppword", 'utf-8')
secret = bytes("seesederseeseeder", 'utf-8')

def pbkdf2_hmac_sha256(password, master_seed, transform_rounds, dklen=32):

    dk = b""
    block_index = 1

    while len(dk) < dklen:
        
        prev = u = hmac(password, master_seed + block_index.to_bytes(4, 'big'))
        block_index += 1

        for _ in range(transform_rounds - 1):
            u = hmac(password, u)
            prev = bytes(x ^ y for x, y in zip(prev, u))

        dk += prev

    return dk[:dklen]


def hmac(key, message):

    block_size = 64

    # If the key is longer than the block size, hash it else pad it with 0s
    if len(key) > block_size:
        key = hashlib.sha256(key).digest()

    if len(key) < block_size:
        key += b'\x00' * (block_size - len(key))

    # Paddings
    inner_pad = bytes((x ^ 0x36) for x in key)
    outer_pad = bytes((x ^ 0x5C) for x in key)

    hmac_hash = hashlib.sha256(outer_pad + hashlib.sha256(inner_pad + message).digest()).digest()

    return hmac_hash

print("Comparison results:\n", pbkdf2_hmac_sha256(psw, secret, 1, 32).hex(), "\n", hashlib.pbkdf2_hmac("sha256", psw, secret, 1, 32).hex())

start = time.time()
pbkdf2_hmac_sha256(psw, secret, 24000, 32)
end = time.time()
print("\n", end - start)