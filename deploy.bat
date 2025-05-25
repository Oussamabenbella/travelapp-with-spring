@echo off
echo === Building application with Maven ===
call mvn clean package -DskipTests

echo === Building Docker image ===
docker build -t travelapp:latest .

echo === Stopping old container (if exists) ===
docker stop travelapp 2>nul
docker rm travelapp 2>nul

echo === Running new container ===
docker run -d -p 8080:8080 --name travelapp travelapp:latest

echo === Deployment completed successfully ===
echo Application running at http://localhost:8080