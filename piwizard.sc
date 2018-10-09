#!/bin/bash
INPUT=/tmp/menu.sh.$$
OUTPUT=/tmp/output.sh.$$
trap "rm $OUTPUT; rm $INPUT; exit" SIGHUP SIGINT SIGTERM
function display_output(){
	local h=${1-82}
	local w=${2-82}
	local t=${3-Output}
	dialog --backtitle " Automatic Installer " --title "${t}" --clear --msgbox "$(<$OUTPUT)" ${h} ${w}
}
function loading(){ 
sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value
apicall=$(curl http://expire.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev) 
	if [[ $apicall = "VIP" ]];
		then
			clear
			echo "     * * * PRO MEMBERSHIP NOT VALID * * *     " >> expire_box
			echo "     * * * Oh No!  Your Pro Membership has expired * * *     " >> expire_box
			echo "     * * *Please help keep the project going by donating today * * *     " >> expire_box
			echo "     * * *Visit http://thepiwizard.com to regain your Pro Membership by making a donation * * * " >> expire_box
			whiptail --textbox expire_box 12 90 
			sudo rm ./expire_box 
			cd /home/pi/RetroPie/roms/nes
			sudo wget http://thepiwizard.com/neslist.txt
            sudo xargs rm < '/home/pi/RetroPie/roms/nes/neslist.txt'
			cd /home/pi/RetroPie/roms/piwizard
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
			clear
			sudo wget -q --show-progress http://vip.thepiwizard.com/launcher/main.sc 
			clear
			sudo chmod a+x /home/pi/RetroPie/roms/piwizard/main.sc 
			clear
			sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/main.sc 
			clear
			bash /home/pi/RetroPie/roms/piwizard/main.sc
else
sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value
apicall=$(curl http://api.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev) 
	if [[ $apicall = "VIP" ]];
		then
			clear
			echo "pro membership activated " >> pro_box
			echo "     * * * Database : api * * *     " >> pro_box
			echo "     * * * pro_box * * * " >> pro_box
			dialog --textbox pro_box 12 90 
			sudo rm ./pro_box 
			cd /home/pi/RetroPie/roms/piwizard
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
			clear
			sudo wget -q --show-progress http://vip.thepiwizard.com/launcher/main.vip 
			clear
			sudo chmod a+x /home/pi/RetroPie/roms/piwizard/main.vip 
			clear
			sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/main.vip 
			clear
			bash /home/pi/RetroPie/roms/piwizard/main.vip
else
if [[ $apicall = "Standard" ]];
		then
			clear
			echo " =+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+-++=+-++=+-++=+-+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+" >> standard_box
		    echo "=+=                                              PI WIZARD                                                   =+=" >> standard_box
		    echo "=+=                                 STANDARD USER LEVEL ACCESS GRANTED                                       =+=" >> standard_box
		    echo " =+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+-++=+-++=+-++=+-+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=" >> standard_box
			echo " " >> standard_box
			echo " " >> standard_box
			echo "                                        Standard Level Features Include:" >> standard_box
			echo "                                               ~ Limited Rom Packs" >> standard_box
			echo "                                  ~ No ROM Media (snaps, boxart, cartart ect)" >> standard_box
			echo "                                                ~ Limited Support" >> standard_box
			echo "                                        ~ Capped Download speed of 500k" >> standard_box
			echo " " >> standard_box
			echo " " >> standard_box
			echo "==============================================================================================================" >> standard_box
			echo "==  _____              ______         _                          _____            _           _             ==" >> standard_box           
			echo "== |  __ \            |  ____|       | |                        |_   _|          | |         | |       _    ==" >> standard_box 
			echo "== | |__) | __ ___    | |__ ___  __ _| |_ _   _ _ __ ___  ___     | |  _ __   ___| |_   _  __| | ___  (_)   ==" >> standard_box
			echo "== |  ___/  __/ _ \   |  __/ _ \/ _  | __| | | |  __/ _ \/ __|    | | |  _ \ / __| | | | |/ _  |/ _ \       ==" >> standard_box    
			echo "== | |   | | | (_) |  | | |  __/ (_| | |_| |_| | | |  __/\__ \   _| |_| | | | (__| | |_| | (_| |  __/  _    ==" >> standard_box
			echo "== |_|   |_|  \___/   |_|  \___|\____|\__|\____|_|  \___||___/  |_____|_| |_|\___|_|\____|\____|\___| (_)   ==" >> standard_box
            echo "==                                                                                                          ==" >> standard_box
            echo "==                                                                                                          ==" >> standard_box
            echo "== ~ FULL ROM PACKS - Access to over 8000 ROMS                                                              ==" >> standard_box
            echo "== ~ FULL ROM MEDIA - Auto installed per system download (SNAPS, BOXART, CARTART, WHEEL)                    ==" >> standard_box
            echo "== ~ UNCAPPED DOWNLOAD SPEED                                                                                ==" >> standard_box
            echo "== ~ CUSTOM MUSIC PACKS                                                                                     ==" >> standard_box
            echo "== ~ CUSTOM SCRIPT PACKS                                                                                    ==" >> standard_box
            echo "== ~ PRIORITY SUPPORT                                                                                       ==" >> standard_box
            echo "== ~ SINGLE ROM DOWNLOADS                                                                                   ==" >> standard_box
            echo "== ~ MUCH MORE TO COME!                                                                                     ==" >> standard_box
		    echo "==                         =======>     FOR FULL DETAILS VISIT:      <=======                               ==" >> standard_box
		    echo "==                     ===============>     WWW.THEPIWIZARD.COM   <===============                          ==" >> standard_box
			echo "==============================================================================================================" >> standard_box
	        echo " " >> standard_box
	        echo " " >> standard_box
	        echo " " >> standard_box
	        echo "                                     Your Pi Serial Number:  $sn                                                " >> standard_box
	        echo "                   To register your PI and use Pro features visit http://thepiwizard.com                        " >> standard_box
	         [$ figlet -f standard "TEST"]
			whiptail --textbox standard_box 75 120 
			sudo rm ./standard_box 
			cd /home/pi/RetroPie/roms/piwizard
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
			sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
			clear
			sudo wget -q --show-progress http://vip.thepiwizard.com/launcher/main.sc 
			clear
			sudo chmod a+x /home/pi/RetroPie/roms/piwizard/main.sc 
			clear
			sudo chown pi:pi /home/pi/RetroPie/roms/piwizard/main.sc 
			clear
			bash /home/pi/RetroPie/roms/piwizard/main.sc
fi
fi
fi
}
function ext(){ #clean and delete function
	clear
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sh
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sh.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
	clear
	echo "Denial of Term of Service. PiWizard UNINSTALLED" > denial_box
	dialog --textbox denial_box 12 80
	break
}
function rebt(){
	sudo rm /home/pi/RetroPie/roms/piwizard/*.sc
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.sc.*
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip
	sudo rm -f /home/pi/RetroPie/roms/piwizard/*.vip.*
	clear
	echo "Thanks for using PiWizard" > thanks
	dialog --textbox thanks 12 80
	sudo reboot now
	break
}
while true
do
#160 character per line
#47 lines free
dialog --clear --nocancel --backtitle "Automatic Installer" \
--title "[ D I S C L A I M E R ]" \
--menu " ======> Please read the following before accessing PI WIZARD <====== \n
\n
PI WIZARD is an automatic installer of various scripts and roms upon your request.  PI WIZARD comes in 2 versions Standard and Pro.  In the event that you do not continue your Pro Version, PI WIZARD has the right to remove all or any features that are only granted to the Pro version.  You will still have access to the standard version.  For full details Please visit our website below to read the full disclaimer. \n
\n
Facebook PI WIZARD group http://facebook.com/groups/thepiwizard \n
PI WIZARD Website http://thepiwizard.com \n
PayPal Donation Link https://www.paypal.me/piwizard \n
\n
Choose the TASK" 25 80 80 \
Accept  "I agree to the EULA" \
Decline "Decline the EULA" \
Reboot  "Reboot" 2>"${INPUT}"
menuitem=$(<"${INPUT}")
case $menuitem in
	Accept) loading;;
	Decline) deny;;
	Reboot) rebt;;
	Exit) ext;;
esac
done
[ -f $OUTPUT ] && rm $OUTPUT && clear && echo Thanks for using PI WIZARD && sleep 5
[ -f $INPUT ] && rm $INPUT && clear && echo Thanks for using PI WIZARD && sleep 5
