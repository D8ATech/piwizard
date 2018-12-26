#!/bin/bash
. inc/variables.inc
. $SCRIPTPATH/inc/helper.inc
if [ -f "inc/adminpanel.inc" ]; then
	. inc/adminpanel.inc
fi

__version="3.0.0"

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
#### Pi Wizard v2.2
####
################################################################################
#
# Versions
#
# 0.0.1 - Initial version with self updating
#2.0.0 - First Released Version
#2.2.0 - System Update and many fixes

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
		$DIALOG  --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/piwizard.main.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
		--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
		--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
		--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
		--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
		--and-widget --begin $infotextline $menutextcol --shadow \
		--backtitle "PI WIZARD - Automatic Installer" \
		--title "[ D I S C L A I M E R ]" \
		--no-cancel \
		--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
		Accept "Agree to the EULA" \
		Decline "Decline the EULA" \
		Reboot  "Reboot my Pi" \
		Exit "Exit" 2>"$choiceMain"

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
						Exit)
								MAINRUNNING="FALSE"
								return 0;;
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
				$DIALOGONE  --keep-window  --colors --begin $infotextline $infotextcol --tailboxbg inc/one.pro.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --shadow \
				--backtitle "PI WIZARD PRO VERSION" \
				--title "[ PI WIZARD PRO VERSION INSTALLER]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Rom-Downloads "Get your Roms " \
				Single-Rom-Download "Coming Soon!" \
				__ " " \
				Colors "Customize Your Launcher Colors" \
				Backup-Restore "Backup or Restore ROMS from HDD" \
				Utility-Scripts "Optional Utility Scripts" \
				Serial-Number "The Serial Number of your PI" \
				Disk-Space "SD Card Disk Space" \
				Music "Grab a Music Pack" \
				Get-Support "View Support Methods" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back One Menu" 2>"$choiceOne"
		else
				$DIALOGONE  --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/one.standard.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
				--backtitle "PI WIZARD STANDARD VERSION" \
				--title "[ PI WIZARD STANDARD VERSION INSTALLER]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Rom-Downloads "Get your Roms " \
				Single-Rom-Download "Coming Soon to PRO" \
				__ "  " \
				Colors "Customize Your Launcher Colors" \
				Backup-Restore "Backup or Restore ROMS from HDD" \
				Utility-Scripts "Optional Utility Scripts" \
				Serial-Number "The Serial Number of your PI" \
				Disk-Space "SD Card Disk Space" \
				Music "Grab a Music Pack - PRO" \
				Upgrade-to-Pro "Compare Standard to Pro" \
				Get-Support "View Support Methods" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back One Menu" 2>"$choiceOne"
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
						Serial-Number) serial;;
						Upgrade-to-Pro) upgrade;;
						Single-Rom-Download) singlerom;;
						Reboot) rebt;;
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
	  esac
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
	DIALOGGAMES=${DIALOGGAMES=dialog}
	choiceGames=/tmp/dialoggames-$$.$RANDOM; > $choiceGames
	trap "rm -f $choiceGames" 0 1 2 5 15

	while [ "$GAMESRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
		if [ "$VIP" == "Yes" ]; then
				$DIALOGGAMES  --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/game.pro.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
				--backtitle "PI WIZARD PRO VERSION" \
				--title "[ PI WIZARD PRO VERSION Downloader ]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				__ "= Atari Systems =" \
				atari2600_pro "Atari 2600" \
				atari5200_pro "Atari 5200" \
				atari7800_pro "Atari 7800" \
				atarilynx_pro "Atari Lynx" \
				__ "= Nintendo Systems =" \
				nes_pro "Nintendo Entertainment System" \
				snes_pro "Super Nintendo" \
				snesclassic_pro "Super Nintedo Classic" \
				nds "Nintendos DS" \
				n64_pro "Nintendo 64" \
				gb_pro "Nintendo GameBoy" \
				gbc_pro "Nintendo GameBoy Color" \
				gba_pro "Nintendo GameBoy Advanced" \
				famicom_pro "Famicom" \
				fds_pro "Famicom Disk System" \
				__ "= Sega Systems =" \
				gamegear_pro "Sega Gamegear" \
				megadrive_pro "Sega Genesis" \
				mastersystem_pro "Sega MasterSytem" \
				markiii_pro "Sega MarkIII" \
				__ "= Other Systems =" \
				arcade_pro "Arcade" \
				pcengine_pro "PC Engine" \
				playstation "Playstation" \
				psp "Playstation Portable" \
				daphne "Daphne" \
				neogeo "Neogeo" \
				coleco_pro "Coleco Vision" \
				gameandwatch_pro "Game and Watch" \
				msx2_pro "MSX2" \
				msx2plus_pro "MSX2+" \
 				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceGames"
		else
				$DIALOGGAMES  --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/game.standard.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
				--backtitle "PI WIZARD STANDARD VERSION" \
				--title "[ PI WIZARD STANDARD VERSION INSTALLER]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				__ "= Atari Systems =" \
				atari5200 "Atari 5200" \
				atari7800 "Atari 7800" \
				__ "= Nintendo Systems =" \
				nes "Nintendo Entertainment System" \
				snes "Super Nintendo" \
				n64 "Nintendo 64" \
				gb "Nintendo GameBoy" \
				gbc "Nintendo GameBoy Color" \
				gba "Nintendo GameBoy Advanced" \
				__ "= Sega Systems =" \
				gamegear "Sega Gamegear" \
				megadrive "Sega Genesis" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceGames"
		fi

		retval="$?"
		choice=$(cat $choiceGames)
		echo "" > $choiceGames

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						Reboot) rebt;;
						Back) GAMESRUNNING="FALSE";;
						__);;
						*) downloadroms "$choice"
							;;
					esac
				fi
				choice=""
				;;
			$DIALOG_CANCEL)
				GAMESRUNNING="FALSE";;
			$DIALOG_ESC)
				clear
				[ -s $choiceGames ] && cat $choiceGames || echo "ESC Pressed"
				GAMESRUNNING="FALSE";;
		esac
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
				$DIALOGMUSIC --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/music.pro.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
				--backtitle "PI WIZARD PRO MUSIC INSTALLER" \
				--title "[ PI WIZARD PRO MUSIC SERVER ]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Year-1980 "1980's" \
				Year-1990 "1990's" \
				Year-2000 "2000's" \
				Top40 "Top 40's" \
				Arcade "Arcade" \
				GST1 "Game Soundtrack 1" \
				GST2 "Game Soundtrack 2" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceMusic"
		else
				$DIALOGMUSIC --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/music.standard.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
				--backtitle "PIWIZARD STANDARD MUSIC INSTALLER" \
				--title "[ PIWIZARD STANDARD MUSIC SERVER ]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceMusic"
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
	done

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

	while [ "$COLORRUNNING" == "TRUE" ];	do
		findcenter $DIALOGWIDTH $DIALOGHEIGHT
				$DIALOGCOLOR --keep-window --colors --begin $infotextline $infotextcol --tailboxbg inc/colors.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
				--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
				--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
				--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
				--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
				--and-widget --begin $infotextline $menutextcol --shadow \
				--backtitle "PI WIZARD PRO COLOR PICKER" \
				--title "[ PI WIZARD COLOR PICKER ]" \
				--menu "Make your choice:" $MENUHEIGHT $MENUWIDTH $MENUITEMS \
				Blue-Light "Blue Background Light Menu" \
				Blue-Dark "Blue Background Dark Menu" \
				Red-Light "Blue Background Light Menu" \
				Red-Dark "Blue Background Dark Menu" \
				Yellow-Light "Blue Background Light Menu" \
				Yellow-Dark "Blue Background Dark Menu" \
				Green-Light "Blue Background Light Menu" \
				Green-Dark "Blue Background Dark Menu" \
				Magenta-Light "Blue Background Light Menu" \
				Magenta-Dark "Blue Background Dark Menu" \
				Cyan-Light "Blue Background Light Menu" \
				Cyan-Dark "Blue Background Dark Menu" \
				NoColor "Remove Color" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceColor"

		retval="$?"
		choice=$(cat $choiceColor)
		echo "" > $choiceColor

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						--) ;;
						Reboot) rebt;;
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
		options+=(98 "Reboot")
		options+=(99 "Back")

		if [[ -n "$cmd" ]]; then
		unset cmd
		fi

		cmd=(dialog --keep-window --colors --begin $infotextline $infotextcol --title "[ Scripts - Instructions ]" --tailboxbg inc/scripts.txt $TXTBOXHEIGHT $TXTBOXWIDTH \
			--and-widget --keep-window --colors --begin $statustextline $menutextcol --title "ROM SERVER STATUS:" --no-shadow --infobox "$currentStatus" 5 55 \
			--and-widget --keep-window --colors --begin $countertextline $countertextcol --title "PI WIZARD DOWNLOAD COUNT:" --infobox "$romcounter" 3 55 \
			--and-widget --keep-window --colors --begin $announcetxtline $announcetxtcol --title "CURRENT ANNOUNCEMENTS:" --infobox "$announcements" 9 102 \
			--and-widget --keep-window --colors --begin $footerline $footercol --infobox "$FOOTERTEXT" 5 160 \
			--and-widget --begin $infotextline $menutextcol --no-cancel --shadow \
			--backtitle "PIWIZARD SCRIPT RUNNER" \
			--title "[ Available Scripts ]" \
			--menu "You can use the UP/DOWN arrow keys, or the number\nkeys 1 -9 to choose an option.\nReboot and Back options available at the bottom of\nthe list.\n\nMake Your Choice: \n" $MENUHEIGHT $MENUWIDTH $MENUITEMS )

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

			if [[ "$choice" = "98" ]]; then
				rebt
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
UPGRADECHECK="YES"
POSITIONAL=()

while [[ $# -gt 0 ]]
	do
	  key="$1"
	  case $key in
	      -h|--help)
	          DISPLAYHELP="YES"
	          shift # past argument
	          ;;
	      -n|--noupgrade)
	          UPGRADECHECK="NO"
	          shift # past argument
	          ;;
	      *)    # unknown option
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

smallscreencheck
getscreeninfo
licensecheck
onlinecheck

main
turnonMusic
cleanupExit
debugwrite ">>> END <<<"
