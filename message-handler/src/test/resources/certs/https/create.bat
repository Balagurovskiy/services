openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048

openssl req -new -x509 -key private_key.pem -out public-cert.pem -days 365 \
-subj "/C=US/ST=California/L=San Francisco/O=My Company/OU=IT Department/CN=localhost/emailAddress=admin@mycompany.com"

openssl pkcs12 -export -in public-cert.pem -inkey private-key.pem -out service-keystore.p12 -name service-alias -passout pass:your_password
