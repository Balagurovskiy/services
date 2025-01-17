# messaging-serivces
Нужно заимплементить отказоустойчивую систему, которая порционно будет записывать сообщения в базу данных (дополнительные улучшения будут плюсом)
1. Имплементация 2х сервисов: 1. Сервис1 -генерирует много сообщений,  
2. Сервис 2 - принимает, обрабатывает и записывает в Н2, сервис-2 - сервис с секьюрити - обязательно. 
логирование отдельным компонентом.
<br/>



## BUILD / DEPLOYMENT
1. Build applications: <br/>
   `` mvn clean install`` <br/>
2. Create images and run containers: <br/>
   ``docker-compose build`` <br/>
   ``docker-compose up -d`` <br/>
3. To stop container: <br/>
   ``docker-compose down`` <br/>
4. Separate containers for log tracing and services: <br/>
   ``docker-compose -f docker-compose-log.yml build`` <br/>
   ``docker-compose -f docker-compose-log.yml up -d`` <br/>
   <br/>
   ``docker-compose -f docker-compose-services.yml build`` <br/>
   ``docker-compose -f docker-compose-services.yml up -d`` <br/>
## ENCRYPTION
``All generated certificates were added only for demonstration purposes``
### message-handler SSL :
1. Generate certificates for https (classpath:resources/certs/https): <br/>
   '/CN=localhost' have to be updated with actual message-handler host <br/>
  ``openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048`` <br/><br/>
  ``openssl req -new -x509 -key private-key.pem -out public-cert.pem -days 365 -subj "//CN=message-handler"``<br/><br/>
  ``openssl pkcs12 -export -in public-cert.pem -inkey private-key.pem -out service-keystore.p12 -name service-alias -passout pass:your_password`` <br/><br/>

2. With created service-keystore.p12 and pass:'your_password' update properties to enable ssl for endpoints:<br/>
  ``server.ssl.key-store=classpath:certs/https/service-keystore.p12`` <br/>
  ``server.ssl.key-store-type=PKCS12`` <br/>
  ``server.ssl.key-store-password=DEC(your_password)`` <br/>
  ``server.ssl.key-alias=service-alias`` <br/>

### message-handler JWT Token :
1. Copy (to classpath:resources/certs/token) public certificate from token provider (message-generator) and update property: <br/>
  `` security.token.rsa.public-key=classpath:certs/token/public.pem`` <br/>

### message-handler Properties :
1. Encrypt sensitive information using maven plugin : <br/>
   Manually : <br/>
   ``mvn jasypt:encrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>
   ``mvn jasypt:decrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>
   Update docker file : <br/>
   ``ENTRYPOINT ["java", "-jar", "/app/message-handler.jar", "--jasypt.encryptor.password=enc_key"]`` <br/>


### message-generator SSL :
1. Enable ssl requests; Copy (classpath:resources/certs/https) public-cert.pem from the secured service (message-handler) and 
generate truststore : <br/>
   ``keytool -importcert -file public-cert.pem -alias truststore_cert -keystore sending-truststore.p12 -storetype PKCS12 -storepass your_password`` <br/> <br/>

2. With created sending-truststore.p12 and pass:your_password update dev properties to enable ssl for requests: <br/>
   ``spring.ssl.bundle.jks.bundle-client.truststore.location=classpath:certs/https/sending-truststore.p12`` <br/>
   ``spring.ssl.bundle.jks.bundle-client.truststore.password=DEC(your_password)`` <br/><br/>
### message-generator JWT Token :
1. Generate certificates for jwt:<br/>
   ``openssl genrsa -out keypair.pem 2048`` <br/>
   ``openssl rsa -in  keypair.pem -pubout -out public.pem`` <br/>
   ``openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem`` <br/>

2. Update properties for token: <br/>
   ``security.token.rsa.private-key=classpath:certs/token/private.pem`` <br/>
   ``security.token.rsa.public-key=classpath:certs/token/public.pem`` <br/>
   ``security.token.username=message-generator`` <br/>
   ``security.token.expiration.minutes=10`` <br/>
   ``security.key.algorithm=RSA`` <br/>
   ``security.key.size=2048`` <br/>
   ``security.bundle.name=bundle-client`` <br/>

### message-generator Properties :
1. Encrypt sensitive information using maven plugin : <br/>
   Manually : <br/>
   ``mvn jasypt:encrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>
   ``mvn jasypt:decrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>
   Update docker file : <br/>
   ``ENTRYPOINT ["java", "-jar", "/app/message-generator.jar", "--jasypt.encryptor.password=enc_key"]`` <br/>

### FUNC SCHEMA
<br/>
<p align="center">
  <img src="https://github.com/Balagurovskiy/services/blob/a57d403db840138c50b6e607f7e2b198f7cb086a/scheme.jpg" title="hover text">
</p>

### Log observation :
http://localhost:3000/ <br/>
http://grafana:3000/
### Postman :
[service.postman_collection](service.postman_collection)
