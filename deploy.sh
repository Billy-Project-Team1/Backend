REPOSITORY=/home/ubuntu/app #REPOSITORY 변수에 파일 위치 지정
cd $REPOSITORY

#각각의 변수들에 값 담아주기
APP_NAME=rhythme
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

#돌아가고 있는 nohup pid 값 담아줌
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ] # 구동 중인 서버가 없다면
then
echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else # 구동 중인 서버가 있다면 꺼줌
echo "> kill -9 $CURRENT_PID"
sudo kill -9 $CURRENT_PID
sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
