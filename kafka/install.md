# 설치
```bash
# jdk install
sudo apt-get install openjdk-8-jdk

# directory 생성
mkdir -p ~/apps/kafka
mkdir -p /data/kafka

cd ~/apps/kafka
wget http://apache.mirror.cdnetworks.com/kafka/1.1.0/kafka_2.12-1.1.0.tgz

tar xvfz kafka_2.12-1.1.0.tgz
ln -s kafka_2.12-1.1.0 default

# 환경 변수
echo "export KAFKA_HOME=\"$HOME/apps/kafka/default\"" >> ~/.profile
echo "export PATH=\"\$PATH:\$KAFKA_HOME/bin\"" >> ~/.profile
echo "alias kafka-start=\"sudo systemctl start kafka.service\"" >> ~/.bash_aliases
echo "alias kafka-stop=\"sudo systemctl stop kafka.service\"" >> ~/.bash_aliases
echo "alias kafka-status=\"sudo systemctl status kafka.service\"" >> ~/.bash_aliases
source ~/.profile
echo $KAFKA_HOME

# config 수정
## 고유한 ID 할당
broker_id=$(echo $HOSTNAME | awk -F- '{print $2}' | sed 's/^0*//')
sed -i -e "s/broker.id=0/broker.id=${broker_id}/g" $KAFKA_HOME/config/server.properties

## log directory
sed -i -e 's/log.dirs=\/tmp\/kafka-logs/log.dirs=\/data\/kafka/g' $KAFKA_HOME/config/server.properties
## zookeeper
sed -i -e "s/zookeeper.connect=localhost:2181/zookeeper.connect=${zookeeper_connection}/g" $KAFKA_HOME/config/server.properties

# systemd 등록
## systemd 등록
export KAFKA_SYSTEMD_FILE="/etc/systemd/system/kafka.service"
echo "[Unit]
Description=kafka-server
After=network,target

[Service]
Type=simple
User=$USER
Group=$USER
SyslogIdentifier=kafka-server
WorkingDirectory=$KAFKA_HOME
Restart=wlways
RestartSec=0s
ExecStart=$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
ExecStop=$KAFKA_HOME/bin/kafka-server-stop.sh" | sudo tee $KAFKA_SYSTEMD_FILE

sudo systemctl daemon-reload
sudo systemctl start kafka-server.service 
```