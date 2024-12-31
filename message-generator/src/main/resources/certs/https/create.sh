#keytool -import -alias service-cert -file public-cert.pem -keystore service-truststore.p12 -storetype PKCS12 -storepass your_password
keytool -importcert -file public-cert.pem -alias truststore_cert -keystore sending-truststore.p12 -storetype PKCS12 -storepass your_password
#keytool -delete -noprompt -alias "service-cert" -keystore sending-truststore.p12