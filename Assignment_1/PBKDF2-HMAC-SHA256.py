import hmac
import hashlib

psw = bytes("passppword", 'utf-8')
secret = bytes("seeseeder", 'utf-8')

def pbkdf2(password, master_seed, transform_rounds, key_size=32):

    # The '36' byte sequence (ipad) is used for inner padding
    # The '5C' byte sequence (opad) is used for outer padding
    # Create the inner and outer pad sequences by XORing the padded key with specific byte sequences
    Si = bytes((Key ^ 0x36) for Key in range(256))
    So = bytes((Key ^ 0x5C) for Key in range(256))

    # Pad the key with zeroes on the right to match the desired length
    s_zpad = master_seed.ljust(key_size, b'\0')

    # XOR is performed by translating each byte in the padded key according to the byte sequences
    s_ipad = s_zpad.translate(Si)  # Inner pad sequence
    s_opad = s_zpad.translate(So)  # Outer pad sequence

    hashed_psw = hashlib.sha256(s_ipad+password).digest()
    for i in range(transform_rounds):
        hashed_psw =  hashlib.sha256(s_opad + hashed_psw).digest()
    return hashed_psw


print("Comparison results:\n", pbkdf2(psw, secret, 1).hex(), "\n", hmac.new(psw,secret,digestmod="sha256").hexdigest())
