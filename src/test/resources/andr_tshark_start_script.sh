ssh root@10.254.0.131 '/usr/bin/tshark -i enp2s0 -Y "(ip.src == 92.223.99.99 || ip.src == 178.176.158.69 || ip.src == 195.161.167.68) && ip.dst == 10.10.0.102"'   #for Jenkins
