#tshark -Y "ip.src == 92.223.99.99 && ip.dst == 10.254.7.106"   #for local
ssh mmtr@10.254.7.106 '/usr/local/bin/tshark'   #for Jenkins

