#!/bin/bash

IFS=';'

# Welcome
 dialog --backtitle "Game Media Removal Utility" --title "RetroPie Game Media Removal Utility" \
    --yesno "\nRetroPie media removal utility.\n\nThis utility will remove extra media files (boxart, cartart, snap, and wheel) for a chosen system where there is not a matching game for it.\n\nThis script expects you to be using the following media folders located in the same /roms/xxx folder as the rom files.\n\nboxart\ncartart\nsnap\nwheel\n\nWARNING: Always make a backup copy of your SD card and your roms and media files before making changes to your system.\n\n\nDo you want to proceed?" \
    25 80 2>&1 > /dev/tty \
    || exit


function main_menu() {

ls /home/pi/RetroPie/roms > /tmp/romfolders

let i=0 # define counting variable
W=() # define working array
while read -r line; do # process file by file
    let i=$i+1
    W+=($i "$line")
done < <(cat /tmp/romfolders)

ROMFLD=$(dialog --title "RetroPie Media Removal Utility" --menu "ROM folders - choose the one to clean." 24 80 17 "${W[@]}" 3>&2 2>&1 1>&3)

clear

if [ -z $ROMFLD ]; then
   return
else
dialog --infobox "...processing..." 3 20 ; sleep 2
currentfolder=`sed -n ${ROMFLD}p /tmp/romfolders`
remove_media ${currentfolder}
fi

}

function remove_media() {
dialog --infobox "...processing..." 3 20 ; sleep 2
choice=$1
directory="/home/pi/RetroPie/roms/${choice}"

ls "${directory}/boxart" | sed -e 's/\.jpg$//' | sed -e 's/\.png$//' > /tmp/boxart.txt
ls "${directory}/cartart" | sed -e 's/\.jpg$//' | sed -e 's/\.png$//' > /tmp/cartart.txt
ls "${directory}/snap" | sed -e 's/\.mp4$//' > /tmp/snap.txt
ls "${directory}/wheel" | sed -e 's/\.jpg$//' | sed -e 's/\.png$//' > /tmp/wheel.txt

rm /tmp/remove_media.sh 2> /dev/null

while read bname
do
ifexist=`ls "${directory}" |grep "${bname}"`
if [[ -z $ifexist ]]
then
echo "rm \"${directory}/boxart/${bname}.png\"" >> /tmp/remove_media.sh
echo "rm \"${directory}/boxart/${bname}.jpg\"" >> /tmp/remove_media.sh
fi
done < /tmp/boxart.txt

while read cname
do
ifexist=`ls "${directory}" |grep "${cname}"`
if [[ -z $ifexist ]]
then
echo "rm \"${directory}/cartart/${cname}.png\"" >> /tmp/remove_media.sh
echo "rm \"${directory}/cartart/${cname}.jpg\"" >> /tmp/remove_media.sh
fi
done < /tmp/cartart.txt

while read sname
do
ifexist=`ls "${directory}" |grep "${sname}"`
if [[ -z $ifexist ]]
then
echo "rm \"${directory}/snap/${sname}.mp4\"" >> /tmp/remove_media.sh
fi
done < /tmp/snap.txt

while read wname
do
ifexist=`ls "${directory}" |grep "${wname}"`
if [[ -z $ifexist ]]
then
echo "rm \"${directory}/wheel/${wname}.png\""  >> /tmp/remove_media.sh
echo "rm \"${directory}/wheel/${wname}.jpg\""  >> /tmp/remove_media.sh
fi
done < /tmp/wheel.txt

#execute the removal
chmod 777 /tmp/remove_media.sh
/tmp/remove_media.sh
rm /tmp/remove_media.sh
}

# Main

main_menu
