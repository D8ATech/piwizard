#!/bin/bash
# OverScan On/Off Switch

infobox=""
infobox="${infobox}\n"
infobox="${infobox}OverScan On/Off\n\n"
infobox="${infobox}\n"
infobox="${infobox}This script will disable or enable the OverScan.\n"
infobox="${infobox}\n"
infobox="${infobox}\n\n"
infobox="${infobox}**Enable**\nWhen you select the Enable option, the overscan is enabled"
infobox="${infobox}\n"
infobox="${infobox}**Disable**\nwhen you select the Disable option, the overscan is disabled\n"
infobox="${infobox}\n"
infobox="${infobox}\n"

dialog --backtitle "OverScan On/Off Switch" \
--title "OverScan On/Off Switch !" \
--msgbox "${infobox}" 35 110

function main_menu() {
    local choice

    while true; do
        choice=$(dialog --backtitle "$BACKTITLE" --title " MAIN MENU " \
            --ok-label OK --cancel-label Exit \
            --menu "What action would you like to perform?" 25 75 20 \
            1 "Enable Overscan" \
            2 "Disable Overscan" \
            2>&1 > /dev/tty)

        case "$choice" in
            1) enable_overscan  ;;
            2) disable_overscan  ;;
            *)  break ;;
        esac
    done
}


function enable_overscan() {
	dialog --infobox "...Removing..." 3 20 ; sleep 2
	echo "Your Retropie is about to reboot so that the settings take effect!"
sleep 3
sudo perl -p -i -e 's/disable_overscan=1/#disable_overscan=1/g' /boot/config.txt
sudo reboot

}

function disable_overscan() {
	dialog --infobox "...Applying..." 3 20 ; sleep 2
	echo "Your Retropie is about to reboot so that the settings take effect!"
sleep 3
sudo perl -p -i -e 's/#disable_overscan=1/disable_overscan=1/g' /boot/config.txt
sudo reboot

}

main_menu
