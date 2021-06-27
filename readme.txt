Deployment:
1. install jre;
2. install maven;
3. run "mvn clean package";
4. run "java -jar target/*.jar" will default run backend server ,provide restful api service at port 8080;
5. if run "java -jar target/*.jar --root=home --output=home" CLI command will get parameter assigned root folder and output folder.