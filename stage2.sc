#!/bin/bash
INPUT=/tmp/menu.sh.$$
OUTPUT=/tmp/output.sh.$$
trap "rm $OUTPUT; rm $INPUT; exit" SIGHUP SIGINT SIGTERM
function display_output(){
	local h=${1-82}
	local w=${2-82}
	local t=${3-Output}
	dialog --backtitle  "Romset Installation" --title "${t}" --msgbox "$(<$OUTPUT)" ${h} ${w}
}
function nes(){
	clear
	mkdir /home/pi/RetroPie/roms/nes
	cd /home/pi/RetroPie/roms/nes
	sudo rm -f /home/pi/RetroPie/roms/nes/nes.zip.*
	sudo rm -f /home/pi/RetroPie/roms/nes/nes.zip
	clear
	echo Currently Downloading Nintendo Entertainment System Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/nes.zip
	unzip -o /home/pi/RetroPie/roms/nes/nes.zip -d /home/pi/RetroPie/roms/nes
	sudo rm -f /home/pi/RetroPie/roms/nes/nes.zip
	clear
	echo Nintendo Entertainment System Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function snes(){
	clear
	mkdir /home/pi/RetroPie/roms/snes
	cd /home/pi/RetroPie/roms/snes
	sudo rm -f /home/pi/RetroPie/roms/nes/snes.zip.*
	sudo rm -f /home/pi/RetroPie/roms/nes/snes.zip
	clear
	echo Currently Downloading Super Nintendo Entertainment System Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/snes.zip
	unzip -o /home/pi/RetroPie/roms/snes/snes.zip -d /home/pi/RetroPie/roms/snes
	sudo rm -f /home/pi/RetroPie/roms/snes/snes.zip
	clear
	echo Super Nintendo Entertainment System Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function gbc(){
	clear
	mkdir /home/pi/RetroPie/roms/gbc
	cd /home/pi/RetroPie/roms/gbc
	sudo rm -f /home/pi/RetroPie/roms/gbc/gbc.zip.*
	sudo rm -f /home/pi/RetroPie/roms/gbc/gbc.zip
	clear
	echo Currently Downloading GameBoy Color Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/gbc.zip
	unzip -o /home/pi/RetroPie/roms/gbc/gbc.zip -d /home/pi/RetroPie/roms/gbc
	sudo rm -f /home/pi/RetroPie/roms/gbc/gbc.zip
	clear
	echo GameBoy Color Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function gba(){
	clear
	mkdir /home/pi/RetroPie/roms/gba
	cd /home/pi/RetroPie/roms/gba
	sudo rm -f /home/pi/RetroPie/roms/gba/gba.zip.*
	sudo rm -f /home/pi/RetroPie/roms/gba/gba.zip
	clear
	echo Currently Downloading GameBoy Advance Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/gba.zip
	unzip -o /home/pi/RetroPie/roms/gba/gba.zip -d /home/pi/RetroPie/roms/gba
	sudo rm -f /home/pi/RetroPie/roms/gba/gba.zip
	clear
	echo GameBoy Advance Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function gb(){
	clear
	mkdir /home/pi/RetroPie/roms/gb
	cd /home/pi/RetroPie/roms/gb
	sudo rm -f /home/pi/RetroPie/roms/gb/gb.zip.*
	sudo rm -f /home/pi/RetroPie/roms/gb/gb.zip
	clear
	echo Currently Downloading GameBoy Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/gb.zip
	unzip -o /home/pi/RetroPie/roms/gb/gb.zip -d /home/pi/RetroPie/roms/gb
	sudo rm -f /home/pi/RetroPie/roms/gb/gb.zip
	clear
	echo GameBoy Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function n64(){
	clear
	mkdir /home/pi/RetroPie/roms/n64
	cd /home/pi/RetroPie/roms/n64
	sudo rm -f /home/pi/RetroPie/roms/n64/n64.zip.*
	sudo rm -f /home/pi/RetroPie/roms/n64/n64.zip
	clear
	echo Currently Downloading Nintendo 64 Rom Pack
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/n64.zip
	unzip -o /home/pi/RetroPie/roms/n64/n64.zip -d /home/pi/RetroPie/roms/n64
	sudo rm -f /home/pi/RetroPie/roms/n64/n64.zip
	clear
	echo Nintendo 64 Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function megadrive(){
	clear
	mkdir /home/pi/RetroPie/roms/megadrive
	cd /home/pi/RetroPie/roms/megadrive
	sudo rm -f /home/pi/RetroPie/roms/megadrive/megadrive.zip.*
	sudo rm -f /home/pi/RetroPie/roms/megadrive/megadrive.zip
	clear
	echo Currently Downloading Sega Genesis
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/megadrive.zip
	unzip -o /home/pi/RetroPie/roms/megadrive/megadrive.zip -d /home/pi/RetroPie/roms/megadrive
	sudo rm -f /home/pi/RetroPie/roms/megadrive/megadrive.zip
	clear
	echo Sega Genesis Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function gamegear(){
	clear
	mkdir /home/pi/RetroPie/roms/gamegear
	cd /home/pi/RetroPie/roms/gamegear
	sudo rm -f /home/pi/RetroPie/roms/gamegear/gamegear.zip.*
	sudo rm -f /home/pi/RetroPie/roms/gamegear/gamegear.zip
	clear
	echo Currently Downloading Sega Genesis
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/gamegear.zip
	unzip -o /home/pi/RetroPie/roms/gamegear/gamegear.zip -d /home/pi/RetroPie/roms/gamegear
	sudo rm -f /home/pi/RetroPie/roms/gamegear/gamegear.zip
	clear
	echo Sega Genesis GameGear Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function atari7800(){
	clear
	mkdir /home/pi/RetroPie/roms/atari7800
	cd /home/pi/RetroPie/roms/atari7800
	sudo rm -f /home/pi/RetroPie/roms/atari7800/atari7800.zip.*
	sudo rm -f /home/pi/RetroPie/roms/atari7800/atari7800.zip
	clear
	echo Currently Downloading Atari 7800
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/atari7800.zip
	unzip -o /home/pi/RetroPie/roms/atari7800/atari7800.zip -d /home/pi/RetroPie/roms/atari7800
	sudo rm -f /home/pi/RetroPie/roms/atari7800/atari7800.zip
	clear
	echo Atari 7800 Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function atari2600(){
	clear
	mkdir /home/pi/RetroPie/roms/atari2600
	cd /home/pi/RetroPie/roms/atari2600
	sudo rm -f /home/pi/RetroPie/roms/atari2600/atari2600.zip.*
	sudo rm -f /home/pi/RetroPie/roms/atari2600/atari2600.zip
	clear
	echo Currently Downloading Atari 2600
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/atari2600.zip
	unzip -o /home/pi/RetroPie/roms/atari2600/atari2600.zip -d /home/pi/RetroPie/roms/atari2600
	sudo rm -f /home/pi/RetroPie/roms/atari2600/atari2600.zip
	clear
	echo Atari 2600 Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function neogeo(){
	clear
	mkdir /home/pi/RetroPie/roms/neogeo
	cd /home/pi/RetroPie/roms/neogeo
	sudo rm -f /home/pi/RetroPie/roms/neogeo/neogeo.zip.*
	sudo rm -f /home/pi/RetroPie/roms/neogeo/neogeo.zip
	clear
	echo Currently Downloading NeoGeo
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/neogeo.zip
	unzip -o /home/pi/RetroPie/roms/neogeo/neogeo.zip -d /home/pi/RetroPie/roms/neogeo
	sudo rm -f /home/pi/RetroPie/roms/neogeo/neogeo.zip
	clear
	echo NeoGeo Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function atari5200(){
	clear
	mkdir /home/pi/RetroPie/roms/atari5200
	cd /home/pi/RetroPie/roms/atari5200
	sudo rm -f /home/pi/RetroPie/roms/atari5200/atari5200.zip.*
	sudo rm -f /home/pi/RetroPie/roms/atari5200/atari5200.zip
	clear
	echo Currently Downloading Atari 5200
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/atari5200.zip
	unzip -o /home/pi/RetroPie/roms/atari5200/atari5200.zip -d /home/pi/RetroPie/roms/atari5200
	sudo rm -f /home/pi/RetroPie/roms/atari5200/atari5200.zip
	clear
	echo Atari 5200 Rom Pack Installed
	sleep 3
	###sudo reboot now
	cd /home/pi
}
function atarilynks(){
	clear
	mkdir /home/pi/RetroPie/roms/atarilynks
	cd /home/pi/RetroPie/roms/atarilynks
	sudo rm -f /home/pi/RetroPie/roms/atarilynks/atarilynks.zip.*
	sudo rm -f /home/pi/RetroPie/roms/atarilynks/atarilynks.zip
	clear
	echo Currently Downloading Atari Lynks
	wget -q -c --show-progress --limit-rate 500k --trust-server-names http://roms.thepiwizard.com/atarilynks.zip
	unzip -o /home/pi/RetroPie/roms/atarilynks/atarilynks.zip -d /home/pi/RetroPie/roms/atarilynks
	sudo rm -f /home/pi/RetroPie/roms/atarilynks/atarilynks.zip
	clear
	echo Atari Lynks Rom Pack Installed
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
function rebt(){
	sudo rm -f /home/pi/RetroPie/piwizard/*.sc
	sudo rm -f /home/pi/RetroPie/piwizard/*.sc.*
	sudo rm -f /home/pi/RetroPie/piwizard/*.vip
	sudo rm -f /home/pi/RetroPie/piwizard/*.vip.*
	clear
	echo thanks for using PI WIZARD
	sleep 5
	sudo reboot now
	break
}
while true
do
dialog --clear --nocancel --backtitle "Installation" \
--title "[ PI WIZARD ROM SERVER ]" \
--menu "==> Standard User Access Granted <== \n\
For Full Details Visit: www.thepiwizard.com -  Please keep this project alive by donating:  http://paypal.me/piwizard\n\
\n\
Use the arrow keys to select your game system you want to install" 35 100 50 \
Reboot "Reboot to save changes" \
Back "Back to Main Menu" \
- "_______________________________________________________ " \
- "| STANDARD ROMS | PRO ROMS | SIZE STANDARD | SIZE PRO | " \
Nintendo-Entertainment-System "|       100     |    750   |     10MB      |     NA   | " \
Super-Nintendo-Entertainment-System "|       100     |    850   |     78MB      |     NA   | " \
Nintendo-64 "|       100     |    365   |     1.3GB     |     NA   | " \
GameBoy "|       100     |    830   |     9.MB      |     NA   | " \
GameBoy-Color "|       100     |    540   |     44MB      |     NA   | " \
GameBoy-Advance "|       100     |  1,133   |     317MB     |     NA   | " \
-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- "| x x x x x x x |  x x x x |  x x x x x x  |  x x x x | " \
Sega-Genesis "|       100     |    940   |     62MB      |     NA   | " \
Sega-GameGear "|       100     |    337   |     15MB      |     NA   | " \
-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- "| x x x x x x x |  x x x x |  x x x x x x  |  x x x x | " \
Atari2600 "|       100     |    669   |     445KB     |     NA   | " \
Atari5200 "|        10     |     80   |     105KB     |     NA   | " \
Atari7800 "|        20     |     60   |     612KB     |     NA   | " \
Atari-Lynxs "|        10     |     80   |     2MB       |     NA   | " \
-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- "| x x x x x x x |  x x x x |  x x x x x x  |  x x x x | " \
NeoGeo "|        20     |    120   |     220MB     |     NA   | " \
-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- "+-----------------------------------------------------+ " \
Reboot "Reboot to save changes" \
Back "Back to Main Menu" 2>"${INPUT}"
menuitem=$(<"${INPUT}")
case $menuitem in
	Nintendo-Entertainment-System) nes;;
    Super-Nintendo-Entertainment-System) snes;;
    Nintendo-64) n64;;
    GameBoy) gb;;
    GameBoy-Color) gbc;;
    GameBoy-Advance) gba;;
    Sega-Genesis) megadrive;;
    Atari7800) atari7800;;
    Atari2600) atari2600;;
    Atari5200) atari5200;;
    Atari-Lynks) atarilynks;;
    Sega-GameGear) gamegear;;
    NeoGeo) neogeo;;
	Reboot) rebt;;
	Back) bck;;
esac
done
[ -f $OUTPUT ] && rm $OUTPUT && echo "thanks for using the PI WIZARD" && sleep 3
[ -f $INPUT ] && rm $INPUT && echo "thanks for using the PI WIZARD" && sleep 3