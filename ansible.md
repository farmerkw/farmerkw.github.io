# ansible 환경 구성
- ansible 설치
```bash
# pip 설치
sudo apt-get install -y python-pip

# ansible 설치
sudo pip install ansible

# 관리 대상 정보를 관리하는 hosts file 생성
sudo mkdir /etc/ansible
sudo touch /etc/ansible/hosts
sudo chown $USER:$USER /etc/ansible/hosts

# 설치 확인
ansible --version
```
- ssh 자동 로그인 설정
```bash
ssh-keygen -t rsa
ssh-copy-id $USER@$HOST
ssh $USER@$HOST
```
- target server에 python 설치
```bash
ssh $HOST 'sudo apt-get install -y python-minimal'
```
- test
```bash
# ansible host file에 추가
echo "[WEB]
web-01
web-02

[DB]
db-01" > /etc/ansible/hosts

ansible all -m ping
```

# playbook
[document](https://docs.ansible.com/ansible/latest/user_guide/playbooks_intro.html)

chef의 cookbook과 같은 개념으로 yaml file로 표현하며 하나의 playbook은 다수의 play로 구성된다.
play는 타깃, 변수, 테스크로 구분이 된다.

```yml
---
- hosts: web-01
  vars:
    fileName: bar.txt
  tasks:
  - name: make {{ fileName }}
    file:
      path: "/data/ansible/{{ fileName }}"
      state: touch
```
## 변수
[document](https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html)

### 파일을 읽어 변수 사용 및 prompt로 값을 입력
```yml
# /data/playbook/infra.yml
---
fileName01: file01.txt

# $HOME/playbook/play-variable.yml
---
- hosts: web-01
  vars_prompt:
    - name: 'fileName02'
      prompt: 'file name 02'
      private: yes
  vars_files:
    - /data/playbook/infra.yml
  tasks:
  - name: test directory
    file:
      path: /data/ansible/variable
      state: directory
  - name: file variable
    file:
      path: /data/ansible/variable/{{ fileName01 }}
      state: touch
  - name: prompt
    file:
      path: "/data/ansible/variable/{{ fileName02 }}"
      state: touch
```
### hostvars
```yml
---
- hosts: WEB
  tasks:
  - name: print web-01  ip
    debug: msg="web-01 ip {{ hostvars['web-01']['ansible_default_ipv4']['address'] }}"
```
### groups, group_name
### [first_found](https://docs.ansible.com/ansible/2.5/plugins/lookup/first_found.html)
### [환경변수 읽기](https://docs.ansible.com/ansible/2.5/plugins/lookup/env.html)
```yml
# env.yml
---
- hosts: WEB
  tasks:
  - debug: msg="Hello {{ lookup('env', 'USER') }}"
```
### [register variable](https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html#registered-variables)
- task 결과를 저장
```yml
# register.yml
- hosts: web-01
  tasks:
  - file: dest=/data/ansible/register state=directory
    register: foo_result
  - debug: msg="{{ foo_result }}"  
```

## task
수행되는 action을 정의하는 곳이며 다양한 [Module](https://docs.ansible.com/ansible/2.5/user_guide/modules.html)을 활용하여 구성한다.

### [loop](https://docs.ansible.com/ansible/latest/user_guide/playbooks_loops.html)
```yml
# loop.yml
---
- hosts: web-01
  tasks:
  - name: create loop direcotry
    file:
      path: /data/ansible/loop
      state: directory
  - name: file create
    file:
      path: /data/ansible/loop/{{ item }}
      state: touch
    loop:
      - file01
      - file02
```

### [conditionals](https://docs.ansible.com/ansible/2.5/user_guide/playbooks_conditionals.html)


## handler
task에 의해 호출되지 않으면 실행되지 않으며, 태스카 뭔가를 변경 해
task list의 실행이 종료 되었을 때 실행된다.
```yml
# foo.j2
# handler-playbook.yml
---
- hosts: web-01
  tasks:
  - name: handler directory
    file:
      path: /data/ansible/handler
      state: directory
  - name: task02
    template:
      src: /data/playbook/foo.j2
      dest: /data/ansible/handler/foo
    notify: handler01
  handlers:
    - name: handler01
      debug: msg="Hello Handler"

````

# [roles](https://docs.ansible.com/ansible/2.5/user_guide/playbooks_reuse_roles.html)

# [tags](https://docs.ansible.com/ansible/2.6/user_guide/playbooks_tags.html)
- 특정 play만 실행 할 수 있는 기능
```yml
# tags.yml
---
- hosts: db-01
  tasks:
   - debug: msg="task1"
     tags: security
   - debug: msg="task2"
     tags: patch
   - debug: msg="task3"
   - debug: msg="task4"
```
실행
```bash
# 특정 tag만 실행
ansible-playbook tags.yml --tags "security"
# 특정 tag만 skip
ansible-playbook tags.yml --skip-tags "patch"
```