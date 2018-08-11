
## 대칭키(비밀키) 암호
암호화에 사용하는 비밀키와 복호화에 사용하는 비밀키가 동일 한 암호
### 블록 암호화 알고리즘
DES/3DES

AES

SEED

ArIA

#### 블록 암호화 운영 모드
ECB(electronic cookbook)모드는 주어진 평문을 블록 크키로 나누워서 차례대로 암호화 하는 방식
![ECB mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#/media/File:ECB_encryption.svg)

CBC(Cipher Block Chaning)모드는 암호문 블록과 현재 평문 블록을 XOR연산하고 그 값을 알고리즘의 입력값으로 사용하여 암호문 블록을 생성
각 블록은 동일한 키를 사용하며, 첫 번째 블록은 이전 블록이 없기 때문에 초기 백터(iv)를 사용하여 XOR 연산
순차적으로 처리해야 하기 때문에 병렬 처리가 불가능
![CBC mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#/media/File:CBC_encryption.svg)

CFB(Cipher Feedback)모드는 이전 암호문 블록을 암호 알고리즘에 입력하여 얻은 결과값과 현재 평문 블록을 XOR 연산하여 암호문 블록을 생성
![CFB mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#/media/File:CFB_encryption.svg)
ECB랑 CBC 모드는 평문을 블록 암호화했지만, CFB 모드는 평문 자체를 암호화하지 않고, 대신 블록 암호를 이용해 만든 키 스트림을 평문과 XOR 연산함으로써 암호문을 생성

키 스트림을 생성하기 때문에 스트림 암호처럼 사용하기도 한다.
복호화 연산이 암호화 연산과 완전히 같다는 것과 패딩할 필요가 없는 장점

OFB(Output Feedback)모드는 이전 블록의 암호 알고리즘 출력값을 현재 암호 알고리즘의 입력값으로 하여 얻은 결과값과 평문 블록을 XOR 연산하여 암호문 블록을 생성
![OFB mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#/media/File:OFB_encryption.svg)

CTR(Counter) 모드는 특정 형태의 유일한 넌스(nonce)와 1씩 증가하는 카운터 값을 붙인 것을 암호화하여 단이 ㄹ블록 형태의 키 스트림을 만든다.
이 키 스크림을 블록과 XOR연산하여 암호문 블록을 생성한다.
키 스트림의 계산을 병렬 처리할 수 있으므로 빠르게 계산할 수 있는 장점이 있다.
![CtR mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#/media/File:CTR_encryption_2.svg)

## 암호화와 인증을 결합한 블록 암호 운영 모드
CCM
암호화와 메시지 인증 기능을 동시에 제공하는 운영 모드
메시지 인증을 위해 CBC-MAC 모드를 사용하고, 메시지 암호화를 위하여 CTR 모드를 사용

GCM
CCM의 성능 향상을 위해 CTR 모드의 변종인 CWC(Carter-Wegman CRT)모드를 만들었는데, 
이 모드는 인증에 병렬 연산이 가능한 일반 해시를 사용

# 패딩

## 스트림 암호
블록 암호화 달리 키 스트림이라고 하는 의사난수(Pseudo-random)을 연속적으로 생성하여 암호화하려는 데이터와 결합하는 구조를 가진다.
일반적인 스트림 암호는 의사난수를 1비트 단위로 생성하고, 생성한 값과 암호화하려는 데이터를 XOR 연산하여 1비트의 암호화된 데이터를 얻는다.

## 해시 암호
임의의 크기를 가진 데이터를 입력받아서 고정된 크기의 결과값을 출력하는 함수로, 메시지 다이제스트(Message Digest)라고도 한다.
### 조건
역상 저항성(preimage resistance): 단방향 함수여야 한다. 즉, 입력값을 알면 해시값ㅇ르 구하기 쉽지만, 해시값을 알아도 입력값을 구할 수 없어야 한다.
제2 역상 저항성(second preimage resistance): 입력값ㅇ르 구한 해시값과 동일한 해시값을 가진 다른 입력값ㅇ르 찾을 수 없어야 한다. 즉 해시값에 영향을 주지 않으면서 입력값을 변경할 수 없어야 한다.
충돌 저항성(collision resistance): 충돌에 대해 안전해야 한다. 즉, 완벽한 충돌 회피란 있을 수 없지만 같은 해시값을 생성하는 두 개의 입력값을 찾을 수 없어야 한다.

### 알고리즘
MD5
SHA

## 메시지 인증 코드(MAC)
HMAC(Hash-based MAC)는 해시 기반의 메시지 인증코드로, MAC을 생성하기 위해서 해시 함수를 사용한다.
인증키로 해시 함수의 블록 크기에 맞는 두 개의 데이터 블록을 만든다.
해시 함수를 이용해서 첫 번째 블록과, 메시지를 결합한 값으로 해시값을 생성한다.
그런 다음, 다시 해시 함수를 이용해서 두 번째 블록과 생성한 해시값을 결합한 값으로 두 번째 해시값을 만든다.
이 값이 MAC이다.

CMAC(Cupher-based MAC)은 블록 암호화 기반의 메시지 인증 코드로 MAC을 생성하기 위해서 블록 암호화 알고리즘을 사용한다.