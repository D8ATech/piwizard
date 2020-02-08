#!/bin/bash
# This script change CPU and GPU settings

infobox=""
infobox="${infobox}\n"                                                                                               
infobox="${infobox}                                     #    #   ##   #####  #    # # #    #  ####\n"  
infobox="${infobox}                                     #    #  #  #  #    # ##   # # ##   # #    #\n" 
infobox="${infobox}                                     #    # #    # #    # # #  # # # #  # #\n"      
infobox="${infobox}                                     # ## # ###### #####  #  # # # #  # # #  ###\n" 
infobox="${infobox}                                     ##  ## #    # #   #  #   ## # #   ## #    #\n" 
infobox="${infobox}                                     #    # #    # #    # #    # # #    #  ####\n"  
infobox="${infobox}\n"                                                 
infobox="${infobox}                    OVERCLOCKING YOUR PI IS NOT RECOMMENDED BY PI WIZARD - OVERCLOCK AT YOUR OWN RISK\n"
infobox="${infobox}\n"
infobox="${infobox}       PI Wizard Retro Gaming Is not responsible for any data loss or damage that result in overclocking..\n"
infobox="${infobox}===============================================================================================================\n\n"                               
infobox="${infobox}                                  @@@\n"                                       
infobox="${infobox}                              @@@@@@@\n"                   
infobox="${infobox}                          @@@@@@@@@@                |- - - - - - - - - - - - - - - - - - - - - - - -|\n"     
infobox="${infobox}                       @@@@@@@@@@@@@                |                                               |\n"      
infobox="${infobox}                      @@@@@@@@@@@                   | WEBSITE   : THEPIWIZARD.COM                   |\n"           
infobox="${infobox}                    @@@@@@@@@                       |                                               |\n"          
infobox="${infobox}                   @@@@@@@@@@@@                     | FACEBOOK  : facebook.com/groups/thepiwizard   |\n"         
infobox="${infobox}                  @@@@@@@@@@@@@                     |                                               |\n"          
infobox="${infobox}                 @@@@@@@@@@@@@                      | DONATIONS : paypal.me/piwizard                |\n"         
infobox="${infobox}                @@@@@@@@@@@@@@@@                    |                                               |\n"
infobox="${infobox}              @@@@@@@@@@@@@@@@@@                    | SUPPORT   : admin@thepiwizard.com             |\n"
infobox="${infobox}              @@@@@@@@@@@@@@@@                      |                                               |\n"               
infobox="${infobox}             @@@@@@@@@@@@@@@@@                      | - - - - - - - - - - - - - - - - - - - - - - - |\n"             
infobox="${infobox}            @@@@@@@@@@@@@@@@@@@@@\n"                               
infobox="${infobox}       @@@@@@@@@@@@@@@@@@@@@@@@@\n"                               
infobox="${infobox}     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"                   
infobox="${infobox}      @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"                 
infobox="${infobox}       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"                  
infobox="${infobox}      @@@@@@@@@@@@@@@@@@@@@@@@@@@\n"               
infobox="${infobox}     @@@@   @@@@@@@ @@@@@@@@    \n"  
infobox="${infobox}             @@@@@@  @@@@@@@@   \n"  
infobox="${infobox}            @@@@@@@  @@@@@@@@   \n" 
infobox="${infobox}            @@@@@@@  @@@@@@@@@  \n"    
infobox="${infobox}           @@@@@@@@   @@@@@@@@  \n"     
infobox="${infobox}         @@@@@@@@@@   @@@@@@@@  \n"         
infobox="${infobox}        @@@@@@@@@@     @@@@@@@@ \n"          
infobox="${infobox}        @@@@@@@@@      @@@@@@@@ \n"           
infobox="${infobox}        @@@@@@@        @@@@@@@  \n\n" 
infobox="${infobox}           P I - W I Z A R D \n"
infobox="${infobox}      Making Retro Gaming Fun Again \n"  

dialog --backtitle "RetroPie Pi 4 Overclocking tool" \
--title "RetroPie Overclock By PI Wizard" \
--msgbox "${infobox}" 50 120

function main_menu() {
    local choice

    while true; do
        choice=$(dialog --backtitle "$BACKTITLE" --title " MAIN MENU " \
            --ok-label OK --cancel-label Exit \
            --menu "OVER CLOCK AT YOUR OWN RISK" 13 100 25 \
            1 "Overclock CPU 1600GHz & GPU FREQ 500 rpi4" \
            2 "Overclock CPU 1700GHz & GPU FREQ 600 rpi4" \
			3 "Overclock CPU 1750GHz & GPU FREQ 620 rpi4" \
			4 "Overclock CPU 2000GHz & GPU FREQ 620 rpi4 - WARNING - This will cause extreame Heat" \
			5 "No overclock [Factory settings]" \
            2>&1 > /dev/tty)

        case "$choice" in
            1) overclock1600  ;;
            2) overclock1700  ;;
            3) overclock1750  ;;
            4) overclock2000  ;;
            5) nooverclock  ;;
            *)  break ;;
        esac
    done
}


function overclock1600() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	sudo cp /home/pi/RetroPie/roms/piwizard/scripts/overclock1600.txt /boot/config.txt
echo "Overclocked settings updated, rebooting"
echo
echo "YOUR PI IS NOW OVER CLOCKED TO 1600GHZ"
echo
sudo reboot
}

function overclock1700() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	sudo cp /home/pi/RetroPie/roms/piwizard/scripts/overclock1700.txt /boot/config.txt
echo "Overclocked settings updated, rebooting"
echo
echo "YOUR PI IS NOW OVERCLOCKED TO 1700GHz"
echo
sudo reboot
}

function overclock1750() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	sudo cp /home/pi/RetroPie/roms/piwizard/scripts/overclock1750.txt /boot/config.txt
echo "Overclocked settings updated, rebooting"
echo
echo "YOUR PI IS NOW OVERCLOCKED TO 1750GHz"
echo
sudo reboot
}

function overclock2000() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	sudo cp /home/pi/RetroPie/roms/piwizard/scripts/overclock2000.txt /boot/config.txt
echo "Overclocked settings updated, rebooting"
echo
echo "YOUR PI IS NOW OVERCLOCKED TO 2000GHz"
echo
sudo reboot
}

function nooverclock() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	sudo cp /home/pi/RetroPie/roms/piwizard/scripts/nooverclock.txt /boot/config.txt
echo "Overclocked settings updated, rebooting"
echo
echo "YOU ARE NOW RESTORING TO YOUR FACTORY SETTING !!!"
echo
sudo reboot
}

main_menu
