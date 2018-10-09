#!/bin/bash
INPUT=/tmp/menu.sh.$$
OUTPUT=/tmp/output.sh.$$
trap "rm $OUTPUT; rm $INPUT; exit" SIGHUP SIGINT SIGTERM
function display_output(){
	local h=${1-82}
	local w=${2-82}
	local t=${3-Output}
	dialog --backtitle "PIWIZARD AUTOMATIC ROM & SCRIPT INSTALLER " --title "${t}" --clear --msgbox "$(<$OUTPUT)" ${h} ${w}
}
function full(){
	clear
	cd /home/pi/RetroPie/roms/piwizard
	clear
	sudo rm -f /home/pi/RetroPie/roms/piwizard/stage2.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/stage2.sc.* 
	clear
	wget -q -c --show-progress http://vip.thepiwizard.com/launcher/stage2.sc
	clear
	sudo chmod a+x /home/pi/RetroPie/roms/piwizard/stage2.sc
	clear
	sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/stage2.sc
	clear
	bash /home/pi/RetroPie/roms/piwizard/stage2.sc
}
function singlerom(){
    serial=$(cat /proc/cpuinfo | grep Serial | cut -d ' ' -f 2)
    cd /home/pi/RetroPie/roms
	sudo wget -q -c -o --show-progress http://www.thepiwizard.ca/entry/login/"$serial".zip
	unzip -o /home/pi/RetroPie/roms/"$serial".zip -d /home/pi/RetroPie/
	unzip /home/pi/RetroPie/roms/nes/'*.zip' -d /home/pi/RetroPie/roms/nes
	sudo rm ./"$serial".zip
	sudo rm /home/pi/RetroPie/roms/nes/*.zip
	#sudo pkill emulationstation
	#emulationstation &
	#sleep 10
	clear
}
function bck(){
	clear
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.key
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.key.*
	clear
	wget -q -c --show-progress http://vip.thepiwizard.com/launcher/main.sc
	clear
	sudo chmod a+x /home/pi/RetroPie/roms/piwizard/main.sc
	clear
	sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/main.sc
	clear
	bash /home/pi/RetroPie/roms/piwizard/main.sc
	break
}
function serial(){
clear
sudo rm -f /home/pi/RetroPie/roms/piwizard/msgbox
#sudo rm -f /home/pi/RetroPie/serial/serial.txt
#sudo rm -f /boot/serial.txt
sudo touch /home/pi/RetroPie/serial/serial.txt
sudo chown pi:pi /home/pi/RetroPie/serial/serial.txt
sudo cat /proc/cpuinfo | sudo grep Serial >> /home/pi/RetroPie/serial/serial.txt
#sudo cp /home/pi/RetroPie/serial.txt /boot/serial.txt
cat /proc/cpuinfo | grep Serial >> msgbox
echo "-=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=-" >> msgbox
echo "Upon donation you will be redirected to the registration form.  If you do NOT enter your Serial Number exactly as shown above (including all the 0's) the system will NOT see your PI as a Pro Version." >> msgbox 
echo "-=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=- -=-=-=-=-=-=-=-" >> msgbox
echo "In this event you will need contact support via facebook, live chat, email (admin@thepiwizard.com) and include a screenshot of the paypal receipt and also include the serial number." >> msgbox
whiptail --title "Your Serial Number" --textbox msgbox 30 120
}
function bios(){
	clear
	cd /home/pi/RetroPie/BIOS
	clear
	wget -q -c --show-progress http://roms.thepiwizard.com/bios.zip
	unzip -o bios.zip -d /home/pi/RetroPie/BIOS
	sudo rm -f /home/pi/RetroPie/BIOS/bios.zip
	sudo rm -f /home/pi/RetroPie/BIOS/bios.zip.*
	cd /home/pi
	echo "Additional BIOS Installed into the Bios Folder" 
	echo " "
	echo "This window will close in 20 seconds"
    sleep 20
}
function space(){
sudo rm /home/pi/RetroPie/roms/piwizard/space
	df / >> space
	echo "The easy way to ready the information above is to simply look at the % used." >> space
	whiptail --title "Your Disk Space" --textbox space 10 80
}
function support(){
clear
sudo rm -f /home/pi/RetroPie/roms/piwizard/support
echo "Facebook : http://facebook.com/groups/thepiwizard" >> support
echo "E-Mail   : support@thepiwizard.com" >> support
echo "WebSite  : http://thepiwizard.com " >> support
echo "Forum    : http://forum.thepiwizard.com " >> support
echo "PayPal   : http://paypal.me/thepiwizard " >> support
echo "          __________________________________________ " >> support
echo "IRC Chat :|      IRC Server     | PORT |    ROOM   | " >> support
echo "          | irc.thepiwizard.com | 6667 | #piwizard | " >> support
echo "          | irc.thepiwizard.com | 6667 | #support  | " >> support
echo "          ------------------------------------------ " >> support
echo " " >> support
echo " Please keep this project alive and donate today " >> support
whiptail --title "Support Information" --textbox support 20 60
}
function upgrade(){
clear
sudo rm -f /home/pi/RetroPie/roms/piwizard/upgrade
echo "-----------------------------------------------------" >> upgrade
echo "---    Tired of having slow download speeds?      ---" >> upgrade
echo "---    Want More Games? Or Snaps? Or BoxArt?      ---" >> upgrade
echo "---         How about Priority Support?           ---" >> upgrade
echo "---    Then why not become a Pro member today!    ---" >> upgrade
echo "---  All you have to do is make  simple dontation ---" >> upgrade
echo "--- This project runs off donations so lets keep  ---" >> upgrade
echo "--- it going by donating today.  There are a few  ---" >> upgrade
echo "---           ways you can become Pro             ---" >> upgrade
echo "-----------------------------------------------------" >> upgrade
echo "-----------------------------------------------------" >> upgrade
echo "                                ____________________         " >> upgrade
echo "                                | STANDARD VS PRO  |         " >> upgrade
echo "Limited Rom Sets =============> |     X    |       |         " >> upgrade
echo "Priority Support =============> |          |   X   |         " >> upgrade
echo "ROM Media Packs ==============> |          |   X   |         " >> upgrade
echo "Music Pack Updates ===========> |          |   X   |         " >> upgrade
echo "1000 Games with No Media =====> |     X    |       |         " >> upgrade
echo "6000 Games with Media ========> |          |   X   |         " >> upgrade
echo "MAME Games ===================> |          |   X   |         " >> upgrade
echo "Access to Pro Area ===========> |          |   X   |         " >> upgrade
echo "Early Access to new versions => |          |   X   |         " >> upgrade
echo "                                --------------------         " >> upgrade
echo " " >> upgrade
echo "    Visit http://thepiwizard.com to upgrade today " >> upgrade
whiptail --title "Standard Vs Pro " --textbox upgrade 33 65
}
function rebt(){
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.key
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.key.*
	clear
	echo thanks for using PIWIZARD
	sleep 5
	sudo reboot now
	break
}
function music(){
	clear
	cd /home/pi/RetroPie/roms/piwizard
	clear
	sudo rm -f /home/pi/RetroPie/roms/piwizard/music.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/music.sc.* 
	clear
	wget -q -c --show-progress http://vip.thepiwizard.com/launcher/music.sc
	clear
	sudo chmod a+x /home/pi/RetroPie/roms/piwizard/music.sc
	clear
	sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/music.sc
	clear
	bash /home/pi/RetroPie/roms/piwizard/music.sc
}
while true
do
dialog --clear --nocancel --backtitle "PIWIZARD" \
--title "[ PI WIZARD AUTOMATIC SCRIPT AND ROM INSTALLER]" \
--menu "===> STANDARD VERSION <=== \n\
Unlock all of the features by being a PRO Member \n\
Visit www.thepiwizard.com for full details" 25 95 20 \
Rom-Downloads "Get your Roms " \
Single-Rom-Download "Coming Soon!" \
Emulator-Bios "Download all the extra Emulator Bios" \
-=-=-=-=-=-=-=- " -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- " \
Serial-Number "A simple way to view your Serial Number of your PI" \
Disk-Space "View your SD Card Disk Space" \
Music "Grab a Music Pack" \
Upgrade-to-Pro "Compare Standard to Pro" \
Get-Support "View Multiple Support Methods" \
-=-=-=-=-=-=-=- " -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- " \
Reboot "Reboot to save changes" \
Back "Back to Main Menu" 2>"${INPUT}"
menuitem=$(<"${INPUT}")
case $menuitem in
	Rom-Downloads) full;;
	Music) music;;
	Get-Support) support;;
	Emulator-Bios) bios;;
	Disk-Space) space;;
	Serial-Number) serial;;
	Upgrade-to-Pro) upgrade;;
	Single-Rom-Download) singlerom;;
	Reboot) rebt;;
	Back) bck;;
esac
done
[ -f $OUTPUT ] && rm $OUTPUT && echo "thanks for using Gamewizard RC3" && sleep 5
[ -f $INPUT ] && rm $INPUT && echo thanks for using Gamewizard RC3 && sleep 5