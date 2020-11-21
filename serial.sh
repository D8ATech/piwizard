#!/bin/bash

function main_menu() {
    local choice
    while true; do
        choice=$(dialog --backtitle "$BACKTITLE" --title " View Your Serial Number " \
            --ok-label OK --cancel-label Exit \
            --menu " www.thepiwizard.com " 12 65 20 \
            1 "View Your Serial Number" \
	        2>&1 > /dev/tty)
        case "$choice" in
            1) view_serial ;;
            *) break ;;
        esac
    done
}

function view_serial() {
sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2)
echo "Your Serial Number Is: $sn" >> read_me
echo "" >> read_me
echo " Here are some common numbers letters that are hard to tell the difference: " >> read_me
echo " 0 = zero - 1 = one - 2 = two - 3 = three - 4 = four - 5 = five - 6 = six - 7 = seven - 8 = eight - 9 = nine" >> read_me
echo " " >> read_me
echo " O as in Owl - Z as in Zebra - e as in Edward - b as in Boy - d as in Dog - f as in Frank - s as in Sam " >> read_me
echo " " >> read_me
echo "Hopefully this will help trying to read your serial number" >> read_me
echo "Thank you for Using PI Wizard" >> read_me
dialog --aspect 9 --textbox read_me 0 0
sudo rm read_me
}

main_menu