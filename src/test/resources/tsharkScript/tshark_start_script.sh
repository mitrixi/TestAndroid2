#tshark -Y "ip.src == 92.223.99.99 && ip.dst == 10.254.7.106"   #for local
ssh mmtr@10.254.7.106 '/usr/local/bin/tshark -Y "(ip.src == 92.223.99.99 || ip.src == 178.176.158.68 || ip.src == 178.176.158.69 || ip.src == 195.161.167.68 || ip.src == 195.161.167.69) && ip.dst == 10.254.7.106"'   #for Jenkins

