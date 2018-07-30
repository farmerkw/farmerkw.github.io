### 자동 로그인
A 서버에서 B 서버로 자동로그인 설정
```bash
# A 서버에서 할 일
# key 생성
ssh-keygen -t rsa

# key copy
ssh-copy-id id@hostname

# login
ssh id@hostname
```

login 계정이 다른 경우 .ssh/config 설정
```bash
Host ${hostname 설정}
        User ${user}
```
