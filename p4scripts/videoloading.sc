#!/bin/bash

infobox= ""
infobox="${infobox}_______________________________________________________\n\n"
infobox="${infobox}\n"
infobox="${infobox}RetroPie Video Loading Screen Script\n\n"
infobox="${infobox}\n"
infobox="${infobox}Video loading screen has already been installed on this base image.\n"
infobox="${infobox}\n"
infobox="${infobox}When you launch a game, a video is playing as loading screen.\n"
infobox="${infobox}\n"
infobox="${infobox}\n\n"
infobox="${infobox}**Disable**\nwhen you run the disable option, the videoloadingscreens folder is renamed to videoloadingscreens_disable\n"
infobox="${infobox}\n"
infobox="${infobox}**Enable**\nwhen you run the enable option, the videoloadingscreens_disable folder is renamed to videoloadingscreens\n"
infobox="${infobox}\n"

dialog --backtitle "RetroPie Video Loading Screen Script" \
--title "RetroPie Video Loading Screen Script (by Régalad & WDG)" \
--msgbox "${infobox}" 35 110



function main_menu() {
    local choice

    while true; do
        choice=$(dialog --backtitle "$BACKTITLE" --title " MAIN MENU " \
            --ok-label OK --cancel-label Exit \
            --menu "What action would you like to perform?" 25 75 20 \
            1 "Disable videoloadingscreens" \
            2 "Enable videoloadingscreens" \
            2>&1 > /dev/tty)

        case "$choice" in
            1) disable_videoloadingscreens  ;;
            2) enable_videoloadingscreens  ;;
            *)  break ;;
        esac
    done
}


function disable_videoloadingscreens() {
dialog --infobox "...processing..." 3 20 ; sleep 2
disable_dir="/home/pi/RetroPie/videoloadingscreens_disable"
enable_dir="/home/pi/RetroPie/videoloadingscreens"

if [[ -d "$enable_dir" ]]; then
 mv /home/pi/RetroPie/videoloadingscreens /home/pi/RetroPie/videoloadingscreens_disable
fi

}

function enable_videoloadingscreens() {
dialog --infobox "...processing..." 3 20 ; sleep 2
disable_dir="/home/pi/RetroPie/videoloadingscreens_disable"
enable_dir="/home/pi/RetroPie/videoloadingscreens"

if [[ -d "$disable_dir" ]]; then
 mv /home/pi/RetroPie/videoloadingscreens_disable /home/pi/RetroPie/videoloadingscreens
fi

}

main_menu

