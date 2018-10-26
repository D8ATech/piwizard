#!/bin/bash
var=$(cat /proc/cpuinfo | grep Serial | cut -d ' ' -f 2 | cut -c9-)
sed -i "381s/ouet/"$var"/" /home/pi/.irssi/config
sed -i "381s/ouet/"$var"/" /home/pi/.irssi/config
irssi
