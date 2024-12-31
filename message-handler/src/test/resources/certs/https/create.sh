openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048
sleep 5s
openssl req -new -x509 -key private-key.pem -out public-cert.pem -days 365 \
 -subj "/C=US/ST=California/L=SanFrancisco/O=MyCompany/OU=ITDepartment/CN=localhost/emailAddress=admin@mycompany.com"
sleep 5s
openssl pkcs12 -export -in public-cert.pem -inkey private-key.pem -out service-keystore.p12 -name service-alias -passout pass:your_password
