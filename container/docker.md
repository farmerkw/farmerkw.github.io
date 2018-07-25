# docker

## local image 검색
sudo docker images ${imageName}


## Tip
- local image 전체 지우기 
```bash
docker images -a | awk '{if(NR>1)print $3}' | xargs sudo docker rmi
```
