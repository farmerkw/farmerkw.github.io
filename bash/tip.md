## bash로 암복호화
https://superuser.com/questions/20549/how-can-i-encrypt-a-string-in-the-shell

```bash
# key 생성
openssl genrsa -out key.txt 2048
# 암호화
echo "Hello World Bash" | openssl rsautl -inkey key.txt -encrypt > encrypt.txt
```