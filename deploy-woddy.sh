#!/bin/bash

# 배포 스크립트 체크리스트
# 1. 도커 데몬이 실행중인가요?
# 2. 환경변수 설정이 정확한가요?
# 3  woddy-server.pem을 가지고 있나요?
# 4. woddy-server.pem이 script와 같은 경로에 있나요?
# 5. woddy-server.pem과 script가 build.gradle과 같은 경로에 있나요?

# === 환경 변수 설정 ===
AWS_ACCESS_KEY_ID=""
AWS_SECRET_ACCESS_KEY=""
AWS_REGION=""
AWS_OUTPUT_FORMAT=""
ECR_URI=""
EC2_KEY=""  # EC2 키 파일 경로
EC2_USER=""
EC2_IP=""
DB_USERNAME=""
DB_PASSWORD=""

# === EC2 키 파일 확인 ===

export DB_USERNAME=$DB_USERNAME
export DB_PASSWORD=$DB_PASSWORD

if [ ! -f "$EC2_KEY" ]; then
  echo "EC2 키 파일을 찾을 수 없습니다: $EC2_KEY"
  exit 1
fi

# === 스크립트 실행 ===
echo "=== Woddy 서버 배포 스크립트 시작 ==="

# 1. AWS CLI 설정
echo "1. AWS CLI 설정..."
aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
aws configure set region $AWS_REGION
aws configure set output $AWS_OUTPUT_FORMAT

if [ $? -ne 0 ]; then
  echo "AWS CLI 설정 실패. 스크립트를 종료합니다."
  exit 1
fi

# 2. 프로젝트 클린 및 빌드
echo "2. 프로젝트 클린 및 빌드..."
./gradlew clean build
if [ $? -ne 0 ]; then
  echo "프로젝트 빌드 실패. 스크립트를 종료합니다."
  exit 1
fi

# 3. Docker 이미지 재빌드 및 재실행
echo "3. Docker 이미지 재빌드 및 재실행..."
docker-compose down
docker-compose up --build -d
if [ $? -ne 0 ]; then
  echo "도커 재빌드 및 실행 실패. 스크립트를 종료합니다."
  exit 1
fi

# 4. Docker 이미지를 ECR에 푸시
echo "4. Docker 이미지를 ECR에 푸시..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_URI
docker build --platform linux/amd64 -t woddy-server:latest -f ./Dockerfile .
docker tag woddy-server:latest $ECR_URI/woddy-server:latest
docker push $ECR_URI/woddy-server:latest
if [ $? -ne 0 ]; then
  echo "ECR 푸시 실패. 스크립트를 종료합니다."
  exit 1
fi

# 5. EC2에서 Docker 이미지 가져오기 및 배포
echo "5. EC2에서 Docker 이미지 가져오기 및 컨테이너 배포..."
ssh -i "$EC2_KEY" $EC2_USER@$EC2_IP << EOF
  aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
  aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
  aws configure set region $AWS_REGION
  aws configure set output $AWS_OUTPUT_FORMAT
  aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_URI
  docker pull $ECR_URI/woddy-server:latest
  docker system prune -f
  docker-compose up -d
EOF

if [ $? -ne 0 ]; then
  echo "EC2 배포 실패. 스크립트를 종료합니다."
  exit 1
fi

echo "=== Woddy 서버 배포 완료 ==="