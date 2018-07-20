# Zookeeper
ubuntu-16.04

## 설치
```bash
# 기본 directory 생성
mkdir -p /data/zookeeper
mkdir -p ~/apps/zookeeper

# Download
cd ~/apps/zookeeper
wget http://apache.mirror.cdnetworks.com/zookeeper/current/zookeeper-3.4.12.tar.gz
tar xvfz zookeeper-3.4.12.tar.gz
ln -s zookeeper-3.4.12 default 

# node id 생성
# host name 뒤에 숫자 형식이기 때문에 잘라서 사용
echo $HOSTNAME | awk -F- '{print $2}' | sed 's/^0*//' > /data/zookeeper/myid

# 환경 변수 설정
echo "export ZOOKEEPER_HOME=\"$HOME/apps/zookeeper/default\"" >> ~/.profile
echo "export PATH=\"\$PATH:\$ZOOKEEPER_HOME/bin\"" >> ~/.profile
source ~/.profile

# log 파일 설정
mkdir -p $ZOOKEEPER_HOME/logs
echo "ZOO_LOG_DIR=$ZOOKEEPER_HOME/logs" >> "$ZOOKEEPER_HOME/bin/zkEnv.sh"

# config 설정 정보 추가
cp $ZOOKEEPER_HOME/conf/zoo_sample.cfg $ZOOKEEPER_HOME/conf/zoo.cfg
## data directory 변경
sed -i -e 's/dataDir=\/tmp\/zookeeper/dataDir=\/data\/zookeeper/g' $ZOOKEEPER_HOME/conf/zoo.cfg
## zookeeper 설치 서버 정보
echo "server.1=zookeeper-01:2888:3888" >> $ZOOKEEPER_HOME/conf/zoo.cfg
echo "server.2=zookeeper-02:2888:3888" >> $ZOOKEEPER_HOME/conf/zoo.cfg
echo "server.3=zookeeper-03:2888:3888" >> $ZOOKEEPER_HOME/conf/zoo.cfg

# systemd 등록
export ZOOKEEPER_SYSTEMD_FILE="/etc/systemd/system/zookeeper.service"
echo "[UNIT]
Description=zookeeper-server
After=network,target

[Service]
Type=forking
User=$USER
Group=$USER
SyslogIdentifier=zookeeper-server
workingDirectory=$ZOOKEEPER_HOME
Restart=always
RestartSec=0s
ExecStart=$ZOOKEEPER_HOME/bin/zkServer.sh start
ExecStop=$ZOOKEEPER_HOME/bin/zkServer.sh stop" | sudo tee $ZOOKEEPER_SYSTEMD_FILE

sudo systemctl daemon-reload
sudo systemctl start zookeeper.service
ps -ef | grep java

# alias 등록
echo "alias zoo-run=\"sudo systemctl start zookeeper.service\"" >> ~/.bash_aliases
echo "alias zoo-stop=\"sudo systemctl stop zookeeper.service\"" >> ~/.bash_aliases
echo "alias zoo-status=\"sudo systemctl status zookeeper.service\"" >> ~/.bash_aliases
echo "alias zoo-log=\"tail -F $ZOOKEEPER_HOME/logs/zookeeper.out\"" >> ~/.bash_aliases
source ~/.bash_aliases

```
