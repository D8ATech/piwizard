#!/bin/bash -x
. inc/variables.inc
. $SCRIPTPATH/inc/helper.inc
DIALOGRC="$SCRIPTPATH/.dialogrc"
export DIALOGRC
################################################################################
####
#### Pi Wizard v1.0
####
################################################################################
#
# Versions
#
# 0.0.1 - Initial version with self updating
#


############################################################
##
##  STAGE 1
##
############################################################
function loading(){
	licensecheck

	mainmenu
	ONERUNNING="TRUE"

	#bashreturn=$?
	#if [ $bashreturn == 91 ]; then
	#	exit
	#fi

	#exitcheck "BASE"
	#cd $SCRIPTPATH
}

###################################################
##
## Main part of the application
##
###################################################
function main(){
	exitcheck "BASE"
	DIALOG=${DIALOG=dialog}
	choiceMain=/tmp/dialogmain-$$.$RANDOM; > $choiceMain
	trap "rm -f $choiceMain" 0 1 2 5 15

	while [ "$MAINRUNNING" == "TRUE" ];	do
		#160 character per line
		#47 lines free
		$DIALOG  --keep-window --begin 2 45 --tailboxbg inc/piwizard.main.txt 25 62 \
		--and-widget --begin 2 1 \
		--backtitle "PI WIZARD - Automatic Installer" \
		--title "[ D I S C L A I M E R ]" \
		--menu "Make your choice:" 12 40 25 \
		Accept "Agree to the EULA" \
		Decline "Decline the EULA" \
		Reboot  "Reboot my Pi" \
		Exit "Exit" 2>"$choiceMain"
		#--no-cancel was removed for testing
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
				[ -s $choiceMain ] && cat $choiceMain || echo "ESC Pressed"
				exit 1;;
	  esac
		#exitcheck "BASE"
	done
}


############################################################
##
##  STAGE 2
##
############################################################

function mainmenu(){

	DIALOGONE=${DIALOGONE=dialog}
	choiceOne=/tmp/dialogone-$$.$RANDOM; > $choiceOne
	trap "rm -f $choiceOne" 0 1 2 5 15

	while [ "$ONERUNNING" == "TRUE" ];	do
		if [ "$VIP" == "Yes" ]; then
				$DIALOGONE  --keep-window --begin 2 55 --tailboxbg inc/one.pro.txt 25 55 \
				--and-widget --begin 2 1 \
				--backtitle "PI WIZARD PRO VERSION" \
				--title "[ PI WIZARD PRO VERSION INSTALLER]" \
				--menu "Make your choice:" 22 50 25 \
				Rom-Downloads "Get your Roms " \
				Single-Rom-Download "Coming Soon!" \
				Emulator-Bios "Download all the extra Emulator Bios" \
				__ " " \
				Serial-Number "A simple way to view your Serial Number of your PI" \
				Disk-Space "View your SD Card Disk Space" \
				Music "Grab a Music Pack" \
				Get-Support "View Multiple Support Methods" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceOne"
		else
				$DIALOGONE  --keep-window --begin 2 55 --tailboxbg inc/one.standard.txt 25 55 \
				--and-widget --begin 2 1 \
				--backtitle "PI WIZARD STANDARD VERSION" \
				--title "[ PI WIZARD STANDARD VERSION INSTALLER]" \
				--menu "Make your choice:" 22 50 25 \
				Rom-Downloads "Get your Roms " \
				Single-Rom-Download "Coming Soon to PRO" \
				Emulator-Bios "Download all the extra Emulator Bios" \
				__ "  " \
				Serial-Number "View the Serial Number of your PI" \
				Disk-Space "View your SD Card Disk Space" \
				Music "Grab a Music Pack - PRO" \
				Upgrade-to-Pro "Compare Standard to Pro" \
				Get-Support "View Multiple Support Methods" \
				__ "  " \
				Reboot "Reboot to save changes" \
				Back "Back to Main Menu" 2>"$choiceOne"
		fi

		retval="$?"
		choice=$(cat $choiceOne)
		echo "" > $choiceOne

		case $retval in
			$DIALOG_ACTION)
				if [ ! -z "$choice" ]; then
					case $choice in
						Rom-Downloads) full;;
						Music)
							musicmenu
							MUSICRUNNING="TRUE";;
						Get-Support) support;;
						Emulator-Bios) bios;;
						Disk-Space) disk;;
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
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				ONERUNNING="FALSE";;
	  esac
		#exitcheck "BASE"
	done
}










############################################################
##
##  STAGE 3
##
############################################################







############################################################
##
##  MUSIC
##
############################################################

function musicmenu(){
	DIALOGMUSIC=${DIALOGMUSIC=dialog}
	choiceMusic=/tmp/dialogmusic-$$.$RANDOM; > $choiceMusic
	trap "rm -f $choiceMusic" 0 1 2 5 15

	while [ "$MUSICRUNNING" == "TRUE" ];	do
		if [ "$VIP" == "Yes" ]; then
				$DIALOGMUSIC --keep-window --begin 2 55 --tailboxbg inc/music.pro.txt 25 55 \
				--and-widget --begin 2 1 \
				--backtitle "PI WIZARD PRO MUSIC INSTALLER" \
				--title "[ PI WIZARD PRO MUSIC SERVER ]" \
				--menu "Make your choice:" 22 50 25 \
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
				$DIALOGMUSIC --keep-window --begin 2 55 --tailboxbg inc/music.standard.txt 25 55 \
				--and-widget --begin 2 1 \
				--backtitle "PIWIZARD STANDARD MUSIC INSTALLER" \
				--title "[ PI WIZARD STANDARD MUSIC SERVER ]" \
				--menu "Make your choice:" 22 50 25 \
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
				[ -s $choiceOne ] && cat $choiceOne || echo "ESC Pressed"
				MUSICRUNNING="FALSE";;
		esac
		#exitcheck "BASE"
	done

}


############################
# Initialization
#
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
  clear
  self_update
fi

############################
# Run the script
#

main
	cleanupExit
