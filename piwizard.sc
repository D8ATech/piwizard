#!/bin/bash
. inc/variables.inc
. $SCRIPTPATH/inc/helper.inc
if [ -f "inc/adminpanel.inc" ]; then
	. inc/adminpanel.inc
fi

__version="4.0.1"

[[ "$__debug" -eq 1 ]] && set -x

if [ ! -f "$SCRIPTPATH/.dialogrc" ]; then
	cp "$SCRIPTPATH/library/01blue_lightrc" "$SCRIPTPATH/.dialogrc"
fi

DIALOGRC="$SCRIPTPATH/.dialogrc"
export DIALOGRC

scriptdir="$(dirname "$0")"
scriptdir="$(cd "$scriptdir" && pwd)"
__logdir="$scriptdir/logs"
__tmpdir="$scriptdir/tmp"

################################################################################
####
#### Pi Wizard v4.0.0
####
################################################################################
#
# Versions
#
# 0.0.1 - Initial version with self updating
#2.0.0 - First Released Version
#2.2.0 - System Update and many fixes
#4.0.0 - update to support Pi4 and Pi3 in same code base

############################################################
##
##  STAGE 1
##
############################################################
function loading(){
	debugwrite ">>> loading - piwizard"
	mainmenu
	ONERUNNING="TRUE"
}

###################################################
##
## Main part of the application
##
###################################################
function main(){
	debugwrite ">>> main - pizard"
	DIALOG=${DIALOG=dialog}

	choiceMain=/tmp/dialogmain-$$.$RANDOM; > $choiceMain
	trap "rm -f $choiceMain" 0 1 2 5 15

	while [ "$MAINRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
		$DIALOG  --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$mainText" $TXTBOXHEIGHT $TXTBOXWIDTH \
		--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
		--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
		--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
		--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
		--and-widget --begin $infotextline $menutextcol --no-shadow \
		--backtitle "PI WIZARD - Automatic Installer" \
		--title "[ D I S C L A I M E R ]" \
		--no-cancel \
		--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
		Accept "Agree to the EULA" \
		Decline "Decline the EULA" \
		Reboot  "Reboot my Pi" \
		Exit "Exit Launcher" 2>"$choiceMain"

		retval="$?"
		choice=$(cat $choiceMain)
		echo "" > $choiceMain

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						Accept) loading;;
						Decline) deny;;
						Reboot) rebt;;
						Exit) exitLauncher;;
						*)
							echo "Unexpected Input"
							return 1
							;;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				clear
				echo "Cancel Pressed"
				#cleanup
				exit;;
			$DIALOG_ESC)
				clear
				[ -s $choiceMain ] && cat $choiceMain || echo "ESC Pressed"
				exit 1;;
	  esac
	  retval=""
	done
}


############################################################
##
##  Main Menu
##
############################################################

function mainmenu(){
	debugwrite ">>> mainmenu - piwizard"
	DIALOGONE=${DIALOGONE=dialog}
	choiceOne=/tmp/dialogone-$$.$RANDOM; > $choiceOne
	trap "rm -f $choiceOne" 0 1 2 5 15

	while [ "$ONERUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
		if [ "$VIP" == "Yes" ]; then
				$DIALOGONE  --keep-window  --colors --begin $infotextline $infotextcol --no-shadow --infobox "$mainMenuVip" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PI WIZARD PRO VERSION" \
				--title "[ PI WIZARD PRO VERSION INSTALLER]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Rom-Downloads "Get your Roms " \
				Configure "Configuration Scripts" \
				SRD "Single ROM Download" \
				Delete "Delete Full Systems" \
				__ " " \
				Colors "Customize Your Launcher Colors" \
				Utility-Scripts "Optional Utility Scripts" \
				Disk-Space "SD Card Disk Space" \
				Get-Support "View Support Methods" \
				__ "  " \
				Reboot "Reboot" \
				Exit "Exit Launcher" \
				Back "Back to Previous Menu" 2>"$choiceOne"
		else
				$DIALOGONE  --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$mainMenuStandard" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PI WIZARD STANDARD VERSION" \
				--title "[ PI WIZARD STANDARD VERSION INSTALLER]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Rom-Downloads "Get your Roms " \
				Configure "Configuration Scripts" \
				SRD "Single ROM Download" \
				Delete "Delete Full Systems" \
				__ "  " \
				Colors "Customize Your Launcher Colors" \
				Utility-Scripts "Optional Utility Scripts" \
				Disk-Space "SD Card Disk Space" \
				Upgrade-to-Pro "Compare Standard to Pro" \
				Get-Support "View Support Methods" \
				__ "  " \
				Reboot "Reboot" \
				Exit "Exit Launcher" \
				Back "Back to Previous Menu" 2>"$choiceOne"
		fi

		retval="$?"
		choice=$(cat $choiceOne)
		echo "" > $choiceOne

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						Rom-Downloads)
							gamesmenu
							GAMESRUNNING=TRUE;;
						Configure)
							configmenu
							CONFIGRUNNING="TRUE";;
						Music)
							musicmenu
							MUSICRUNNING="TRUE";;
						Get-Support) support;;
						Emulator-Bios) bios;;
						Disk-Space) disk;;
						Colors) colormenu;;
						Backup-Restore) mnuBackupRestore;;
						Utility-Scripts)
							scriptsmenu
							SCRIPTRUNNING=TRUE;;
						Upgrade-to-Pro) upgrade;;
						SRD) singlerom;;
						Delete) deleteSystemsMenu;;
						Reboot) rebt;;
						Exit) exitLauncher;;
						Back) ONERUNNING="FALSE";;
						*)
							echo "Unexpected Input"
							return 1
							;;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				ONERUNNING="FALSE";;
			$DIALOG_ESC)
				clear
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				ONERUNNING="FALSE";;
			*)
				retval="";;
	  esac
	  retvalue=""
	done
}

function mnuBackupRestore(){
	display_output 10 40 " Coming Soon!"
}


############################################################
##
##  Games
##
############################################################

function gamesmenu(){
	debugwrite ">>> gamesmenu - piwizard"
	#DIALOGGAMES=${DIALOGGAMES=dialog}
	#choiceGames=/tmp/dialoggames-$$.$RANDOM; > $choiceGames
	#trap "rm -f $choiceGames" 0 1 2 5 15

	while [ "$GAMESRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT

		if [[ -n "$gameNames" ]]; then
			unset gameNames
		fi

		if [[ -n "$gameDirs" ]]; then
			unset gameDirs
		fi

		if [[ -n "$gameZips" ]]; then
			unset gameZips
		fi

		if [ "$VIP" == "Yes" ]; then

			# Build the VIP menu

			if [[ -n "$options" ]]; then
				unset options
			fi
			counter=0

			while read p; do
				gameName=$(echo "$p" | awk -F '|' '{ print $1 }')
				gameNames+=("$gameName")

				if [[ $gameName == *"= " ]]; then
					options+=("_" "${gameName}")
				else
					options+=($((counter)) "${gameName}")
				fi

				gameDir=$(echo "$p" | awk -F '|' '{ print $2 }')
				gameDirs+=("${gameDir}")
				gameZip=$(echo "$p" | awk -F '|' '{ print $3 }')
				gameZips+=("${gameZip}")
				((counter++))
			done <inc/menu.games.pro.txt

			#display_output 15 100 "${gameNames[@]} ${gameDirs[@]} ${gameZips[@]}"

			# Add in static options
			options+=(97 "Reboot")
			options+=(98 "Exit Launcher")
			options+=(99 "Back to Previous Menu")

			if [[ -n "$cmd" ]]; then
				unset cmd
			fi

			cmd=(dialog  --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$gamesMenuVip" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PI WIZARD PRO VERSION" \
				--title "[ PI WIZARD PRO VERSION Downloader ]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS)

				if [[ -n "$choices" ]]; then
					unset choices
				fi

				choices=$("${cmd[@]}" "${options[@]}" 2>&1 >/dev/tty)
		else

			# Build the Standard Menu
			if [[ -n "$options" ]]; then
				unset options
			fi
			counter=0

			while read p; do
				gameName=$(echo "$p" | awk -F '|' '{ print $1 }')
				gameNames+=("${gameName}")

				if [[ $gameName == *"= " ]]; then
					options+=("_" "${gameName}")
				else
					options+=($((counter)) "${gameName}")
				fi

				gameDir=$(echo "$p" | awk -F '|' '{ print $2 }')
				gameDirs+=("${gameDir}")
				gameZip=$(echo "$p" | awk -F '|' '{ print $3 }')
				gameZips+=("${gameZip}")
				((counter++))
			done <inc/menu.games.standard.txt

			# Add in static options
			options+=(97 "Reboot")
			options+=(98 "Exit Launcher")
			options+=(99 "Back to Previous Menu")

			if [[ -n "$cmd" ]]; then
				unset cmd
			fi

			cmd=(dialog --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$gamesMenuStandard" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PI WIZARD STANDARD VERSION" \
				--title "[ PI WIZARD STANDARD VERSION INSTALLER]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS )

			if [[ -n "$choices" ]]; then
				unset choices
			fi

			choices=$("${cmd[@]}" "${options[@]}" 2>&1 >/dev/tty)
		fi

		# Menu is built, now get the choices that were made

		for choice in $choices
		do
			#display_output 15 100 "$choice | ${gameNames[$choice]} | ${gameDirs[$choice]} | ${gameZips[$choice]}"

			for ((i = 0; i < $counter; i++)); do
				if [[ "$i" = "$choice" ]]; then
					#display_output 10 80 "i: $i | Choice: $choice | gameNames[$i]: ${gameNames[$i]}"

					if [[ ${gameNames[$i]} == *"= "* ]]; then
						debugwrite "Don't do anything this is a header"
					else
						downloader ${gameDirs[$i]} ${gameDirs[$i]} ${gameZips[$i]}
					fi
				fi
			done

			if [[ "$choice" = "97" ]]; then
				rebt
				break
			fi
			if [[ "$choice" = "98" ]]; then
				exitLauncher
				break
			fi
			if [[ "$choice" = "99" ]]; then
				GAMESRUNNING="FALSE"
			fi
		done
		choices=""
	done
}


############################################################
##
##  Music Menu
##
############################################################

function musicmenu(){
	debugwrite ">>> musicmenu - piwizard"
	DIALOGMUSIC=${DIALOGMUSIC=dialog}
	choiceMusic=/tmp/dialogmusic-$$.$RANDOM; > $choiceMusic
	trap "rm -f $choiceMusic" 0 1 2 5 15

	while [ "$MUSICRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
		if [ "$VIP" == "Yes" ]; then
				$DIALOGMUSIC --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$musicMenuVip" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PI WIZARD PRO MUSIC INSTALLER" \
				--title "[ PI WIZARD PRO MUSIC SERVER ]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Year-1980 "1980's" \
				Year-1990 "1990's" \
				Year-2000 "2000's" \
				Top40 "Top 40's" \
				Arcade "Arcade" \
				GST1 "Game Soundtrack 1" \
				GST2 "Game Soundtrack 2" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Exit "Exit Launcher" \
				Back "Back to Previous Menu" 2>"$choiceMusic"
		else
				$DIALOGMUSIC --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$musicMenuStandard" $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:"--no-shadow --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
				--backtitle "PIWIZARD STANDARD MUSIC INSTALLER" \
				--title "[ PIWIZARD STANDARD MUSIC SERVER ]" \
				--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				__ "  " \
				Reboot "Reboot to save changes" \
				Exit "Exit Launcher" \
				Back "Back to Previous Menu" 2>"$choiceMusic"
		fi

		retval="$?"
		choice=$(cat $choiceMusic)
		echo "" > $choiceMusic

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						Year-1980) music "80";;
						Year-1990) music "90";;
						Year-2000) music "00";;
						Top40) music "T40";;
						Arcade) music "Arcade";;
						GST1) music "GS1";;
						GST2) music "GS2";;
						Reboot) rebt;;
						Exit) exitLauncher;;
						Back) MUSICRUNNING="FALSE";;
						*)
							echo "Unexpected Input"
							return 1
							;;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				MUSICRUNNING="FALSE";;
			$DIALOG_ESC)
				clear
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				MUSICRUNNING="FALSE";;
		esac
		retval=""
	done

}

############################################################
##
##  Config Menu
##
############################################################
function configmenu(){
	debugwrite ">>> configmenu - piwizard"
	CONFIGRUNNING="TRUE"
	DIALOGCONFIG=${DIALOGCONFIG=dialog}
	choiceConfig=/tmp/dialogconfig-$$.$RANDOM; > $choiceConfig
	trap "rm -f $choiceConfig" 0 1 2 5 15

	while [ "$CONFIGRUNNING" == "TRUE" ]; do 
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
			$DIALOGCONFIG --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$configMenuText" $TXTBOXHEIGHT $TXTBOXWIDTH \
			--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
			--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
			--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
			--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
			--and-widget --begin $infotextline $menutextcol --no-shadow \
			--backtitle "PI WIZARD" \
			--title "[ PI WIZARD CONFIG ]" \
			--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
			bezelproject "Bezel Project" \
			overclock "Overclock" \
			removemedia "Remove Media" \
			videoloading "Video Loading" \
			OverscanOn-Off "Overscan on-off" \
			Music-Stop "Music Stop" \
			Music-Start "Music Start" \
			__ "  " \
			Reboot "Reboot" \
			Exit "Exit Launcher" \
			Back "Back to Previous Menu" 2>"$choiceConfig"

		retval="$?"
		choice=$(cat $choiceConfig)
		echo "" > $choiceConfig

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						--) ;;
						Reboot) rebt;;
						Exit) exitLauncher;;
						Back) CONFIGRUNNING="FALSE";;
						*) processConfigScript "$choice";;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				CONFIGRUNNING="FALSE";;
			$DIALOG_ESC)
				clear
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				CONFIGRUNNING="FALSE";;
		esac
		retval=""
	done
	CONFIGRUNNING="TRUE"
}


############################################################
##
##  Color Menu
##
############################################################

function colormenu(){
	debugwrite ">>> musicmenu - piwizard"
	COLORRUNNING="TRUE"
	DIALOGCOLOR=${DIALOGCOLOR=dialog}
	choiceColor=/tmp/dialogcolor-$$.$RANDOM; > $choiceColor
	trap "rm -f $choiceColor" 0 1 2 5 15

	while [ "$COLORRUNNING" == "TRUE" ]; do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
			$DIALOGCOLOR --keep-window --colors --begin $infotextline $infotextcol --no-shadow --infobox "$colorMenuText" $TXTBOXHEIGHT $TXTBOXWIDTH \
			--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
			--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
			--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
			--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
			--and-widget --begin $infotextline $menutextcol --no-shadow \
			--backtitle "PI WIZARD PRO COLOR PICKER" \
			--title "[ PI WIZARD COLOR PICKER ]" \
			--menu "" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
			Blue-Light "Blue Background Light Menu" \
			Blue-Dark "Blue Background Dark Menu" \
			Red-Light "Red Background Light Menu" \
			Red-Dark "Red Background Dark Menu" \
			Yellow-Light "Yellow Background Light Menu" \
			Yellow-Dark "Yellow Background Dark Menu" \
			Green-Light "Green Background Light Menu" \
			Green-Dark "Green Background Dark Menu" \
			Magenta-Light "Magenta Background Light Menu" \
			Magenta-Dark "Magenta Background Dark Menu" \
			Cyan-Light "Cyan Background Light Menu" \
			Cyan-Dark "Cyan Background Dark Menu" \
			NoColor "Remove Color" \
			__ "  " \
			Reboot "Reboot" \
			Exit "Exit Launcher" \
			Back "Back to Previous Menu" 2>"$choiceColor"

		retval="$?"
		choice=$(cat $choiceColor)
		echo "" > $choiceColor

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						--) ;;
						Reboot) rebt;;
						Exit) exitLauncher;;
						Back) COLORRUNNING="FALSE";;
						*) colorpicker "$choice";;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				COLORRUNNING="FALSE";;
			$DIALOG_ESC)
				clear
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				COLORRUNNING="FALSE";;
		esac
	done
	COLORRUNNING="TRUE"
}

############################################################
##  Delete Systems Menu
##
############################################################

function deleteSystemsMenu() {
    if [[ -n "$systemNames" ]]; then
        unset systemNames
    fi

    if [[ -n $options ]]; then
        unset $options
    fi
    counter=0
		cd /home/pi/RetroPie/roms

    for f in *; do
      if [ -d ${f} ]; then
        systemName=$(echo "$f")
        if [[ "$systemName" != "piwizard" ]]; then
	        if [[ "$systemName" != "dev" ]]; then
            if [[ "$systemName" != "music" ]]; then
              systemNames+=("$systemName")
              options+=("$systemName" "$counter" "off")
              let counter=counter+1
            fi
	        fi
        fi
      fi
    done

		if [ $counter -eq 0 ]; then
			display_output 10 40 "There are no systems to be deleted"
		else
	    if [[ -n $choices ]]; then
	       unset choices
	    fi

	    choices=$(dialog --backtitle "Select the systems you want to delete" \
	    --title "Remove Systems from PiWizard" --clear \
	    --checklist "Available Systems" 20 61 $counter \
	    "${options[@]}" \
	    2>&1 >/dev/tty)

	    case $choices in
	      1)
	        echo "Cancel pressed - $choices";;
	      255)
	        echo "ESC pressed";;
	      *)
	        arr=( $choices )
					dialog --title "Are you sure?" \
						--yesno "These Systems will be removed.\n$choices\nYou will need to restart your sytem.\nAre you sure you want to do this?" 8 60
					response=$?

					case $response in
						 0) for i in "${arr[@]}"; do deleteSystem $i; done;;
						 *) download="False";;
					esac
	        ;;
	    esac
		fi
}
############################################################
##  Scripts Menu
##
############################################################

function scriptsmenu(){
	debugwrite ">>> scriptsmenu - piwizard"
#	DIALOGSCRIPTS=${DIALOGSCRIPTS=dialog}
#	choiceScripts=/tmp/dialogscripts-$$.$RANDOM; > $choiceScripts
#	trap "rm -f $choiceScripts" 0 1 2 5 15

	while [ "$SCRIPTRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT

		if [[ -n "$availVers" ]]; then
			unset availVers
		fi

		availVers=($(ls -l $SCRIPTPATH/scripts/*.sc | awk -F '/' '{print $NF}' | sort -V))

		size=$(echo "${#availVers[@]}")

		if [[ -n "$options" ]]; then
			unset options
		fi

		# Build the options of the Menu
		for ((i = 0; i < $size; i++)); do
			options+=($((i)) "${availVers[$i]}")
		done

		# Add in static options
		options+=(97 "Reboot")
		options+=(98 "Exit Launcher")
		options+=(99 "Back to Previous Menu")

		if [[ -n "$cmd" ]]; then
			unset cmd
		fi

		cmd=(dialog --keep-window --colors --begin $infotextline $infotextcol --no-shadow --title "[ Scripts - Instructions ]" --infobox "$scriptsMenuText" $TXTBOXHEIGHT $TXTBOXWIDTH \
			--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 6 55 \
			--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --no-shadow --infobox "$romcounter" 3 55 \
			--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --no-shadow --infobox "$announcements" 9 102 \
			--and-widget --keep-window --colors --begin $footerline $footercol --no-shadow --infobox "$FOOTERTEXT" 5 160 \
			--and-widget --begin $infotextline $menutextcol --no-cancel --no-shadow \
			--backtitle "PIWIZARD SCRIPT RUNNER" \
			--title "[ Available Scripts ]" \
			--menu "You can use the UP/DOWN arrow keys, or the number\nkeys 1 -9 to choose an option.\nReboot and Back options available at the bottom of\nthe list.\n" $MENUHEIGHT $MENUWIDTH $MENUITEMS )

		if [[ -n "$choices" ]]; then
			unset choices
		fi

		choices=$("${cmd[@]}" "${options[@]}" 2>&1 >/dev/tty)

		for choice in $choices
		do
			for ((i = 0; i < $size; i++)); do
				if [[ "$i" = "$choice" ]]; then
						runScript "${availVers[$i]}"
				fi
			done

			if [[ "$choice" = "97" ]]; then
				rebt
				break
			fi
			if [[ "$choice" = "98" ]]; then
				exitLauncher
				break
			fi
			if [[ "$choice" = "99" ]]; then
				SCRIPTRUNNING="FALSE"
			fi
		done
	done
}

############################
# Initialization
#
############################
# Run the script
#

debugwrite ">>> BEGIN <<<"
#for i in $(seq 0 1 100) ; do echo $i | dialog --guage "Please wait" 10 70 0; done
turnoffMusic
offMusic
UPGRADECHECK="YES"
POSITIONAL=()

while [[ $# -gt 0 ]]
	do
	  key="$1"
	  case $key in
	      -h|--help)
				debugwrite ">>>> key = $key - displaying help"
				DISPLAYHELP="YES"
	        	shift # past argument
	            ;;
	      -n|--noupgrade)
				debugwrite ">>>> key = $key - not checking for updates"
	            UPGRADECHECK="NO"
	        	shift # past argument
	        	;;
	      *)    # unknown option
				debugwrite ">>>> key = $key - not sure what to do with this"
	        	POSITIONAL+=("$key") # save it in an array for later
	        	shift # past argument
	        	;;
	  esac
	done

if [ "$DISPLAYHELP" == "YES" ]; then
  output_help
  exit
fi

if [ "$UPGRADECHECK" == "YES" ]; then
  self_update
fi

getscreeninfo
#read -p "getscreeninfo complete Press Enter"

smallscreencheck
#read -p "smallscreencheck complete Press Enter"

onlinecheck
#read -p "onlinecheck complete Press Enter"

licensecheck
#read -p "licensecheck complete Press Enter"

main
debugwrite ">>> END <<<"
