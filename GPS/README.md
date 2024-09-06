# Interfacing the GPS cordinations to raspberry PI


We firslty had the SIM808 module which supoosed to get the cordiantions (lat, long) in addition to the current speed so we first made a python script which supossed to go and interface with that module via UART and parse the return output to get what he need


But we tried to use the module with raspberry pi but nothing appears then we tried with ttl and even arduino and nothing works

the SIM808 was faulty so we had to think of another method to get these cordinations and also the speed



## UDP Connection to get the GPS

We made a bash script which is supposed to establish a UDP connection port at 8888 to recive the cordinateds and the speed and redircet this json output into a file located locally on the rootfile system and then the script will go and parse these files via regular expressions to get the speed and the cordinations


The script will firslty need a package called GPSD in apt which will handle the input we get from the UDP connection

then

```
sudo systemctl stop gpsd
sudo systemctl stop gpsd.socket
```

This will stop every connection is running from before to be able to secure or establish any new connection


```
gpsd -N udp://$(hostname -I | awk '{print $1}'):8888 &

```
Then we will run in the background a process in gpsd that will take the host machine ipv4 and at port 8888 to make anyone can send udp stream at this port


```
gpspipe -w > "$output_file" 2> /dev/null &
```

This will open another shell process and then redirect its output on a file on the rootfile system




```
speed=$(echo "$last_line" | jq -r '.speed // "n/a"')
lat=$(echo "$last_line" | jq -r '.lat // "n/a"')
lon=$(echo "$last_line" | jq -r '.lon // "n/a"')

```
This will lastly pasrse the file we already redirected by regex to get what we neeed






Then we made a systemd service in order to be able to automatically run this script at booting


```
[Unit]
Description=GPS Data Script Service
After=network-online.target
Wants=network-online.target

[Service]
ExecStart=/usr/bin/gps_data.sh
Restart=always
User=root

[Install]
WantedBy=multi-user.target


```







