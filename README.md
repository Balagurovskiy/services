# messaging-serivces
Нужно заимплементить отказоустойчивую систему, которая порционно будет записывать сообщения в базу данных (дополнительные улучшения будут плюсом)
1. Имплементация 2х сервисов: 1. Сервис1 -генерирует много сообщений,  
2. Сервис 2 - принимает, обрабатывает и записывает в Н2, сервис-2 - сервис с секьюрити - обязательно. 
логирование отдельным компонентом.
<br/>


All generated certificates were added only for demonstration purposes
# BUILD / DEPLOYMENT
### DEV
#### message-handler
1. Select **dev** profile in **application.properties**
2. Generate certificates for https (classpath:resources/certs/https): <br/>

``openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048`` <br/><br/>
``openssl req -new -x509 -key private-key.pem -out public-cert.pem -days 365 \`` <br/>
`` -subj "/C=US/ST=State/L=City/O=Comp/OU=ITDep/CN=localhost/emailAddress=xx@comp.com"`` <br/><br/>
``openssl pkcs12 -export \ ``<br/> 
`` -in public-cert.pem -inkey private-key.pem -out service-keystore.p12 \ ``<br/> 
`` -name service-alias -passout pass:your_password`` <br/>

3. With created service-keystore.p12 and pass:your_password update dev properties to enable ssl for endpoints:<br/>
``server.ssl.key-store=classpath:certs/https/service-keystore.p12`` <br/>
``server.ssl.key-store-type=PKCS12`` <br/>
``server.ssl.key-store-password=DEC(your_password)`` <br/>
``server.ssl.key-alias=service-alias`` <br/>

4. Copy (to classpath:resources/certs/token) public certificate from token provider (message-generator) and update property: <br/>
`` security.token.rsa.public-key=classpath:certs/token/public.pem`` <br/>

5. Encrypt sensitive information using maven plugin  (decryption :mvn jasypt:decrypt "-Djasypt.encryptor.password=enc_key"): <br/>
   ``mvn jasypt:encrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>

#### message-generator
5. Select **dev** profile in **application.properties**
6. Enable ssl requests; Copy (classpath:resources/certs/https) public-cert.pem from the secured service (message-generator) and 
generate truststore : <br/>
``keytool -importcert -file public-cert.pem -alias truststore_cert -keystore sending-truststore.p12 -storetype PKCS12 -storepass your_password`` <br/>

7. With created sending-truststore.p12 and pass:your_password update dev properties to enable ssl for requests:
``spring.ssl.bundle.jks.bundle-client.truststore.location=classpath:certs/https/sending-truststore.p12`` <br/>
``spring.ssl.bundle.jks.bundle-client.truststore.password=DEC(your_password)`` <br/>
8. Generate certificates for jwt:<br/>
``openssl genrsa -out keypair.pem 2048`` <br/>
``openssl rsa -in  keypair.pem -pubout -out public.pem`` <br/>
``openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem`` <br/>

9. Update properties for token:
   ``security.token.rsa.private-key=classpath:certs/token/private.pem`` <br/>
   ``security.token.rsa.public-key=classpath:certs/token/public.pem`` <br/>
   ``security.token.username=message-generator`` <br/>
   ``security.token.expiration.minutes=10`` <br/>
   ``security.key.algorithm=RSA`` <br/>
   ``security.key.size=2048`` <br/>
   ``security.bundle.name=bundle-client`` <br/>

9. Encrypt sensitive information using maven plugin (using pattern ENC(...) in properties):  <br/>
   ``mvn jasypt:encrypt "-Djasypt.encryptor.password=ENC_KEY"`` <br/>

#### messages (root folder)
10. Build applications : <br/>
   `` mvn clean install`` <br/>
11. Execute docker compose for dev (password have to be the same as in steps 5, 8) : <br/>

    ``docker-compose up -d`` <br/>
    ``java -jar message-handler/target/message-handler.jar --jasypt.encryptor.password=ENC_KEY`` <br/>
    ``java -jar message-generator/target/message-generator.jar --jasypt.encryptor.password=ENC_KEY`` <br/>
### PROD
#### message-generator / message-handler
Properties should be updated with **prod** profile and all other steps should be similar to dev setup.<br/>
Each application should be prepared for release environment: prod properties have to be updated with db, host details (zipkin etc), certificate namings <br/>
(**CA subject** for release https certificate have to updated with host details for **message-handler** in **prod** env).
Update Docker file **ENTRYPOINT** : ... --jasypt.encryptor.password=ENC_KEY  (password have to be the same as during maven encryption process). <br/>
Update appender url in **logback-spring.xml** with host related to prod env.
#### messages (root folder)
1. Build applications : <br/>
    `` mvn clean install`` <br/>
2. Execute docker compose for prod; separate image creation not required (docker build -t <name>): <br/>
``docker-compose -f docker-compose-prod.yml up -d`` <br/>
### FUNC SCHEMA
<br/>
<p align="center">
  <img src="https://github.com/Balagurovskiy/messaging-serivces/blob/24f41d51ec9a8fd5826d9712550dd2816f03b958/scheme.jpg" title="hover text">
</p>

http://localhost:3000/
http://grafana:3000/

http://localhost:9090/
http://prometheus:9090/