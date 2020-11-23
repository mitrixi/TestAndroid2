#ssh mitrixi@10.254.0.131 'sudo killall tshark'   #for Jenkins
ssh mmtr@10.254.7.106 'tshark -Y "ip.src == 92.223.99.99 && ip.dst == 10.254.7.106"'   #for local
