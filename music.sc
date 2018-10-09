#!/bin/bash
INPUT=/tmp/menu.sh.$$
OUTPUT=/tmp/output.sh.$$
trap "rm $OUTPUT; rm $INPUT; exit" SIGHUP SIGINT SIGTERM
function display_output(){
	local h=${1-82}
	local w=${2-82}
	local t=${3-Output}
	dialog --backtitle  "Music Pack Installation" --title "${t}" --msgbox "$(<$OUTPUT)" ${h} ${w}
}
function 80(){
	clear
	mkdir /home/pi/RetroPie/roms/music
	cd /home/pi/RetroPie/roms/music
	sudo rm -f /home/pi/RetroPie/roms/music/80.zip.*
	sudo rm -f /home/pi/RetroPie/roms/music/80.zip
	clear
	echo Currently Downloading 80's Music Pack
	wget -q -c --show-progress --trust-server-names http://music.thepiwizard.com/80.zip
	unzip -o /home/pi/RetroPie/roms/music/80.zip -d /home/pi/RetroPie/roms/music
	sudo rm -f /home/pi/RetroPie/roms/music/80.zip
	clear
	echo 80's Music Installed!
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function music90(){
	clear
	mkdir /home/pi/RetroPie/roms/music
	cd /home/pi/RetroPie/roms/music
	sudo rm -f /home/pi/RetroPie/roms/music/90.zip.*
	sudo rm -f /home/pi/RetroPie/roms/music/90.zip
	clear
	echo Currently Downloading 90s Music Pack
	wget -q -c --show-progress --trust-server-names http://music.thepiwizard.com/90.zip
	unzip -o /home/pi/RetroPie/roms/music/90.zip -d /home/pi/RetroPie/roms/music
	sudo rm -f /home/pi/RetroPie/roms/music/90.zip
	clear
	echo 90s Music Installed!
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function bck(){
	sudo rm -f /home/pi/RetroPie/roms/piwizard/main.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/main.sc.*
	cd /home/pi/RetroPie/roms/piwizard
	sudo wget -q -c http://vip.thepiwizard.com/launcher/main.sc
	sudo chmod a+x /home/pi/RetroPie/roms/piwizard/main.sc
	clear
	sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/main.sc
	bash /home/pi/RetroPie/roms/piwizard/main.sc
	clear
}
while true
do
dialog --clear --nocancel --backtitle "Installation" \
--title "[ PI WIZARD MUSIC SERVER ]" \
--menu "==> Standard User Access Granted <== \n\
For Full Details Visit: www.thepiwizard.com " 25 80 50 \
Back "Back to Main Menu" \
- "____________________________________________" \
- "|   Type of music     | Song Count |  Size |" \
- "+---------------------+------------+-------+" \
Year-1980 "|       1980s         |    30      | 200MB |" \
Year-1990 "|       1990s         |    20      | 100MB |" \
Year-2000 "|       2000s         |    30      | 200MB |" \
Top40 "|      Top 40         |    40      | 300MB |" \
Arcade "|      Arcade         |     5      |  50MB |" \
Game-Soundtrack "| Game Soundtrack     |    20      | 200MB |" \
Game-Soundtrack "| Game Soundtrack     |    15      | 200MB |" \
- "+---------------------+------------+-------+" \
- " " \
Reboot "Reboot to save changes" \
Back "Back to Main Menu" 2>"${INPUT}"
menuitem=$(<"${INPUT}")
case $menuitem in
	Year-1980) music80;;
    Year-1990) music90;;
	Year-2000) music00;;
	Top40) top40;;
	Arcade) arcade;;
	Game-Soundtrack) soundtrack;;
	Game-Soundtrack) soundtrack;;
	Back) bck;;
esac
done
[ -f $OUTPUT ] && rm $OUTPUT && echo "thanks for using the PI WIZARD" && sleep 3
[ -f $INPUT ] && rm $INPUT && echo "thanks for using the PI WIZARD" && sleep 3