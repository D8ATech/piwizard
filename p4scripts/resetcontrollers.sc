#!/bin/bash
# Reset all of your ES controller configuration. Similar to EZH Script.

infobox=""
infobox="${infobox}\n"
infobox="${infobox}Complete Controller Configuration Reset Script !\n"
infobox="${infobox}\n"
infobox="${infobox}\n"
infobox="${infobox}\n"
infobox="${infobox}This script will remove all controller profiles from your system, will apply a new clean es_input.cfg file so on your next reboot you will need to configure all.\n"
infobox="${infobox}\n"
infobox="${infobox}I suggest you always setup your controllers in order of preference.\nIf you have dual arcade, for example, first set P1 and P2 etc. If you prefer BT controller to be first start there and then connect your wired ones.\n"
infobox="${infobox}\n"
infobox="${infobox}\n"
infobox="${infobox}\n"


dialog --backtitle "Full Controllers Reset" \
--title "Simpler Controller Reset Script !" \
--msgbox "${infobox}" 35 110

function main_menu() {
    local choice

    while true; do
        choice=$(dialog --backtitle "$BACKTITLE" --title " MAIN MENU " \
            --ok-label OK --cancel-label Exit \
            --menu "Are you ready?" 25 75 20 \
            1 "Reset All ES Controller Configs" \
            2 "Reboot" \
            2>&1 > /dev/tty)

        case "$choice" in
            1) reset_ctrls  ;;
            2) reboot  ;;
            *)  break ;;
        esac
    done
}


function reset_ctrls() {
	dialog --infobox "...Resetting..." 3 20 ; sleep 2
	rm /opt/retropie/configs/all/retroarch-joypads/*
	rm /home/pi/.emulationstation/es_input.cfg
	cp /home/pi/RetroPie/roms/piwizard/scripts/es_input.cfg /home/pi/.emulationstation/
}

function reboot() {
	dialog --infobox "...Starting..." 3 20 ; sleep 1
	clear
	sudo reboot
}

main_menu
