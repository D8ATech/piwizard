
#INPUT=/tmp/menu.sh.$$
#OUTPUT=/tmp/output.sh.$$
#trap "rm $OUTPUT; rm $INPUT; exit" SIGHUP SIGINT SIGTERM
#function display_output(){
#	local h=${1-82}
#	local w=${2-82}
#	local t=${3-Output}
#	dialog --backtitle " Automatic Installer " --title "${t}" --clear --msgbox "$(<$OUTPUT)" ${h} ${w}
#}


self_update() {
    cd $SCRIPTPATH
    echo "Checking for lastest version of Pi Wizard. One moment please."
    LOCAL=$(git rev-parse master)
    REMOTE=$(git ls-remote | grep HEAD | awk -F ' ' '{print $1}')
    BASE=$(git merge-base @ "refs/heads/master")

    if [  $LOCAL = $REMOTE ]; then
        debugwrite "Already Up to Date.  Continuing..."
    elif [ $LOCAL = $BASE ]; then
        echo "Found a new version, updating..."
        git pull --force
        read -p "New version installed.  Press Enter to restart..."
        cd $SCRIPTPATH
        bash "$SCRIPTNAME ${ARGS[@]}" &
        exit 1
    elif [ $REMOTE = $BASE ]; then
        echo "There are unpushed changes on client that would be lost.  Exiting"
        exit 1
    else
        echo "Code has diverged.  Delete this directory structure, reclone the repository and try again. Exiting Now"
        exit 99
    fi
}

function output_help(){
  clear
  echo "###########################################################################################################"
  echo "##                                                                                                      ##"
  echo "##         PPPPPPPPPP    IIIIIIIIIIII                                                                   ##"
  echo "##         P         P         I                                                                        ##"
  echo "##         P         P         I                                                                        ##"
  echo "##         P         P         I                                                                        ##"
  echo "##         P PPPPPPPP          I                                                                        ##"
  echo "##         P                   I                                                                        ##"
  echo "##         P                   I                                                                        ##"
  echo "##         P                   I                                                                        ##"
  echo "##         P             IIIIIIIIIIII                                                                   ##"
  echo "##                                                                                                      ##"
  echo "##                                                                                                      ##"
  echo "##         W          W   IIIIIIIIIIII   ZZZZZZZZZZZZ           AA        RRRRRRRRRR     DDDDDDDDD      ##"
  echo "##         W          W        I                  ZZ           A  A       R         R    D        D     ##"
  echo "##         W          W        I                 Z            A    A      R          R   D         D    ##"
  echo "##         W    WW    W        I                Z            A      A     R          R   D          D   ##"
  echo "##         W   W  W   W        I               Z            A        A    R         R    D          D   ##"
  echo "##         W  W    W  W        I              Z            AAAAAAAAAAAA   RPPPPPPPPR     D          D   ##"
  echo "##         W W      W W        I             Z             A          A   R       R      D         D    ##"
  echo "##         WW        WW        I           ZZ              A          A   R        R     D        D     ##"
  echo "##         W          W   IIIIIIIIIIII   ZZZZZZZZZZZZ      A          A   R         R    DDDDDDDDD      ##"
  echo "##                                                                                                      ##"
  echo "##         Version 1.0.0                                                                                ##"                                                                                                   ##"
  echo "##                                                                                                      ##"
  echo "##########################################################################################################"
  echo " "
  echo "Current available options are:"
  echo "  -h | --help      : Display this helpful information"
  echo "  -n | --noupdate  : Skip the auto update check"
  echo " "
}

function debugwrite(){
  if [ "$DEBUG" == "TRUE" ]; then
    echo $1
    read
  fi
}

function isVIP(){
  if [ ! -z $1 ]; then
    debugwrite "isVIP : $1"
    VIP=$1
  fi
}

function rmfile (){
  if [ -f $1 ]; then
      rm $1
  fi
}

function clearfiles(){
    if [ "$DEBUG" == "PROD" ]; then
        case $1 in
          SPLASH)
            #rmfile "/$SCRIPTPATH/inc/splash.sc"
            #rmfile "/$SCRIPTPATH/inc/splash.sc.*"
            ;;
          THEMES)
            #rmfile "/$SCRIPTPATH/inc/theme.vip"
          	#rmfile "/$SCRIPTPATH/inc/theme.vip.*"
            ;;
          KEY)
            rmfile "/$SCRIPTPATH/*.key"
            rmfile "/$SCRIPTPATH/*.key.*"
            ;;
          ZIP)
            rmfile "gamelistfullset.zip"
            rmfile "gamelistfullset.zip.*"
            ;;
          RPI)
            #rmfile "/$SCRIPTPATH/inc/rpiloc.vip"
            #rmfile "/$SCRIPTPATH/inc/rpiloc.vip.*"
            ;;
          SH)
            #rmfile "/$SCRIPTPATH/inc/*.sh"
            #rmfile "/$SCRIPTPATH/inc/*.sh.*"
            ;;
          BIOS)
            rmfile "/home/pi/RetroPie/BIOS/bios.zip"
            rmfile "/home/pi/RetroPie/BIOS/bios.zip.*"
            ;;
          ALL)
           clearAllFiles
           ;;
          *)
            #rmfile "/$SCRIPTPATH/inc/*.sc"
            #rmfile "/$SCRIPTPATH/inc/*.sc.*"
            #rmfile "/$SCRIPTPATH/inc/*.vip"
            #rmfile "/$SCRIPTPATH/inc/*.vip.*"
            #rmfile "/$SCRIPTPATH/inc/*.dev"
            #rmfile "/$SCRIPTPATH/inc/*.dev.*"
          ;;
        esac
    fi
}

function clearAllFiles(){
    clearfiles "BIOS"
    clearfiles "SH"
    clearfiles "RPI"
    clearfiles "ZIP"
    clearfiles "KEY"
    clearfiles "THEMES"
    clearfiles "SPLASH"
    clearfiles
}

function cleanupExit(){
  if [ -f $OUTPUT ]; then
  	rm $OUTPUT
  fi
  if [ -f $INPUT ]; then
  	rm $INPUT
  fi
  clear
  echo "Thanks for using PI WIZARD"
  sleep 5
}

function licensecheck(){
  sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value

  expiredvip=$(curl http://expire.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)

	if [[ $expiredvip = "VIP" ]]; then
      VIP="No"
			dialog --textbox inc/piwizard.expiredvip.txt 12 81
  else
    apicall=$(curl http://api.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)
    #apicall="VIP"
    if [[ $apicall = "VIP" ]]; then
        VIP="Yes"
        dialog --textbox inc/piwizard.validvip.txt 32 81
    else
        VIP="No"
        dialog --textbox inc/piwizard.standard.txt 38 81
    fi
  fi
}

function deny(){
    dialog --textbox inc/tocdenied.txt 9 55
    closeAllDialogs
}

function upgrade(){
	dialog --textbox inc/one.upgrade.txt 32 75
}

function support(){
  dialog --textbox inc/one.support.txt 27 75
}

function serial(){
sn=$(cat /proc/cpuinfo | grep Serial)
  dialog --title "Your Serial Number - $sn" --textbox inc/one.serial.txt 15 80
}

function setupmusic(){
  if [ ! -d "/home/pi/RetroPie/roms/music" ]; then
    mkdir /home/pi/RetroPie/roms/music
  else
    cd /home/pi/RetroPie/roms/music
    if [ -f "*.zip" ]; then
      rm -f *.zip
    fi
    if [ -f ".zip.*" ]; then
      rm -f *.zip.*
    fi
  fi
}

function music(){
  setupmusic
  case $1 in
    80)
      # echo Currently Downloading 80s Music Pack
      wget -q -c --show-progress --trust-server-names http://music.thepiwizard.com/80.zip  2 > & 1;
      grep "%" |\
      sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download 80.zip" 10 100
      unzip -o /home/pi/RetroPie/roms/music/80.zip -d /home/pi/RetroPie/roms/music
      rm -f /home/pi/RetroPie/roms/music/80.zip
      display_output 10 40 "80's Music Installed!"
      ;;
    90)
      # echo Currently Downloading 90s Music Pack
      wget -q -c --show-progress --trust-server-names http://music.thepiwizard.com/90.zip 2 > & 1;
      grep "%" |\
      sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download 90.zip" 10 100
      unzip -o /home/pi/RetroPie/roms/music/90.zip -d /home/pi/RetroPie/roms/music
      rm -f /home/pi/RetroPie/roms/music/90.zip
      display_output 10 40 "90s Music Installed!"
      ;;
    00) display_output 10 40 "The 2000's music pack will be coming soon";;
    T40) display_output 10 40 "The Top 40's music pack will be coming soon";;
    Arcade) display_output 10 40 "The Arcade music pack will be coming soon";;
    GS1) display_output 10 40 "The Game Soundtrack 1 music pack will be coming soon";;
    GS2) display_output 10 40 "The Game Soundtrack 2 music pack will be coming soon";;
    *) display_output 10 40 "Somehow an incorrect value was sent to the music function";;
  esac

	cd $SCRIPTPATH
}


function downloader(){
  if [ ! -d "/home/pi/RetroPie/roms/$1" ]; then
    mkdir /home/pi/RetroPie/roms/$1
  fi
  cd /home/pi/RetroPie/roms/$1

  if [ -f "/home/pi/RetroPie/roms/$1/$2.*" ]; then
    rm '/home/pi/RetroPie/roms/$1/$2.*'
  fi

  if [ -f "/home/pi/RetroPie/roms/$1/$2" ]; then
    rm '/home/pi/RetroPie/roms/$1/$2'
  fi

  clear
  echo "Currently Downloading $1 Rom Pack"

  if [ "$VIP" == "Yes" ]; then
      wget -q -c --show-progress --trust-server-names "http://roms.thepiwizard.com/pro/$2" 2 > & 1;
      grep "%" |\
      sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download $2" 10 100
  else
      wget -q -c --show-progress --limit-rate 500k --trust-server-names "http://roms.thepiwizard.com/$2" 2 > & 1;
      grep "%" |\
      sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download $2" 10 100
  fi
  unzip -o /home/pi/RetroPie/roms/$1/$2 -d /home/pi/RetroPie/roms/$1
  # if [ -f "/home/pi/RetroPie/roms/$1/$2"]; then
    rm -f /home/pi/RetroPie/roms/$1/$2
  #fi
  display_output 10 40 "$1 Rom Pack Installed"
  cd $SCRIPTPATH
}
function downloadroms(){
  case $1 in
    atari2600) downloader "atari2600" "atari2600.zip";;
    atari5200) downloader "atari5200" "atari5200.zip";;
    atari7800) downloader "atari7800" "atari7800.zip";;
    atarijaguar) downloader "atarijaguar" "atarijaguar.zip";;
    atarilynxs) downloader "atarilynks" "atarilynks.zip";;
    coleco) downloader "coleco" "coleco.zip";;
    famicom) downloader "famicom" "famicom.zip";;
    fba) downloader "fba" "fba.zip";;
    fds) downloader "fds" "fds.zip";;
    gameandwatch) downloader "gameandwatch" "gameandwatch.zip";;
    gamegear) downloader "gamegear" "gamegear.zip";;
    gb) downloader "gb" "gb.zip";;
    gba) downloader "gba" "gba.zip";;
    gbc) downloader "gbc" "gbc.zip";;
    mame2003) downloader "mame2003" "mame2003.zip";;
    markiii) downloader "markiii" "markiii.zip";;
    mastersystem) downloader "mastersystem" "mastersystem.zip";;
    megadrive) downloader "megadrive" "megadrive.zip";;
    msx2) downloader "msx2" "msx2.zip";;
    msx2plus) downloader "msx2+" "msx2+.zip";;
    n64) downloader "n64" "n64.zip";;
    neogeo) downloader "neogeo" "neogeo.zip";;
    nes) downloader "nes" "nes.zip";;
    snes) downloader "snes" "snes.zip";;
    snesclasic) downloader "snes-classic" "snes-classic.zip";;
    *) display_output 10 40 "Somehow an incorrect value was sent to the download function";;
  esac
}


function closeAllDialogs(){
  RUNNING="FALSE"
  MAINRUNNING="FALSE"
  ONERUNNING="FALSE"
  MUSICRUNNING="FALSE"
  GAMESRUNNING="FALSE"
}

function findcenter(){
  menuwidth=$1
  menuheight=$2

  display_output 10 80 "menuwidth: $1 - menuheight $2 - MAX WIDTH: $MAXWIDTH - MAX HEIGHT: $MAXHEIGHT"

  #Calculate Width
  if [ $menuwidth -gt $MAXWIDTH ]; then
    MENUX=2
  else
    MENUX=$(expr $(expr $MAXWIDTH / 2) - $(expr $menuwidth / 2))
  fi

  if [ $MENUX -lt 1 ]; then
    MENUX=2
  fi

  #Calculate Height
  if [ $menuheight -gt $MAXHEIGHT ]; then
    MENUY=2
  else
    MENUY=$(expr $(expr $MAXHEIGHT / 2) - $(expr $menuheight / 2))
  fi

  if [ $MENUY -lt 1 ]; then
    MENUY=2
  fi

  infotextline=$MENUY
  infotextcol=$(expr $MENUX + 67)
  menutextcol=$MENUX
}

function rebt(){
  	dialog --textbox inc/thanks.txt 12 80
  	display_output 10 80 "This is where we would reboot but for now we are just going to exit"
    closeAllDialogs
    clear
    # sudo reboot now
  	exit 91
  	break
}

function ext(){ #clean and delete function
	dialog --textbox inc/tocdenied.txt 12 80
	exit
}

#function dev(){
#      msgtxt=$(cat inc/developer)
#      display_output $HEIGHT $WIDTH "$msgtxt"
#}

#function uninstall(){
  ######    TO DO    ######
#}

function disk(){
	df -lh > diskinfo
	echo " " >> diskinfo
	echo " Usually your sd card is named '/' and" >> diskinfo
	echo " your usb storage are /dev/sda, sdb, sdc, sdd " >> diskinfo
	dialog --textbox diskinfo $HEIGHT $WIDTH
	rmfile "./diskinfo"
}

#function splash(){
  ######    TO DO    ######
#}

function bios(){
  if [ ! -d "/home/pi/RetroPie/BIOS" ]; then
    mkdir /home/pi/RetrpPie/BIOS
  fi

  cd /home/pi/RetroPie/BIOS

  wget -q -c --show-progress http://roms.thepiwizard.com/bios.zip 2 > & 1;
  grep "%" |\
  sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download bios.zip" 10 100
	unzip -o bios.zip -d /home/pi/RetroPie/BIOS
	rm -f /home/pi/RetroPie/BIOS/bios.zip
	rm -f /home/pi/RetroPie/BIOS/bios.zip.*
 	display_output 10 40 "Additional BIOS Installed into the Bios Folder"
	cd $SCRIPTPATH
}

#function drv(){
  ######    TO DO    ######
#}

#function scrpt(){
  ######    TO DO    ######
#}


#function thm(){
  ######    TO DO    ######

	#cd /home/pi/RetroPie/roms/gamewizard
	#sudo rm -f /home/pi/RetroPie/roms/gamewizard/theme.sc
	#sudo rm -f /home/pi/RetroPie/roms/gamewizard/theme.sc.*
	#wget -q -c --show-progress http://pi.gamewizard.ca/gamewizard-rc3/theme.sc
	#sudo chmod a+x /home/pi/RetroPie/roms/gamewizard/theme.sc
	#sudo chown pi:pi /home/pi/RetroPie/roms/gamewizard/theme.sc
	#bash /home/pi/RetroPie/roms/gamewizard/theme.sc
  #cd $SCRIPTPATH
#}

function display_output(){
	local h="$1"
	local w="$2"
  local m="$3"
	dialog --backtitle "Pi Wizard Autonomous Romset Installation" --title "Pi Wizard Message" --msgbox "$m" "$h" "$w"
}

#function wgetfile(){
##  if [ ! -z $1 ]; then
##    URL="$1"
##    filename=${URL##*/}  # retain the part after the last slash
##    if [ ! -z $2 ]; then
##      transfervip=$2
##    else
##      transfervip="FALSE"
##    fi
##    if [ "$transfervip" == "TRUE" ]; then
##        wget --progress=dot "$URL" 2 > & 1;
##        grep "%" |\
##        sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download $filename" 10 100
##    else
##      ### wget --progress=dot --limit-rate 1024k "$URL" 2 > & 1
##      grep "%" |\
##      sed -u -e "s,\.,,g" | awk '{print $2}' | sed -u -e "s,\%,,g" | dialog --gauge "Download $filename" 10 100
##    fi
##  else
##    #debugwrite "oops! shouldn't have got here"
##  fi
#}

#function unzipprogress(){
#  debugwrite "Made it to unzipprogress - $1 : $2"
#  if [ ! -z $1 ]; then
#    filename=$1
#    if [ ! -z $2 ]; then
#      destination=$2
#      # pv -n $filename | unzip -o $filename -d $destination 2 > & 1; | dialog --gauge "Extracting $filename" 6 50
#    else
#      debugwrite "no destination passed"
#    fi
#  else
#    debugwrite "no filename passed"
#  fi
#}

#function romset(){
#
#   ######    TO DO    ######
#
#  cd $SCRIPTPATH
#  getGameList
#	bash ./rpiloc.sc "$VIP"
#  bashreturn=$?
#  if [ $bashreturn == 91 ]; then
#    exit
#  fi
#}

#function getGameList(){

#    ######    TO DO    ######
#
#  #wgetfile "http://pi.piwizard.ca/gamelistxml/gamelistfullset.zip" "$VIP"
#  #rmfile "gamelistfullset.zip"
#
#  #debugwrite "Going to unzip now"
#  #unzipprogress "gamelistfullset.zip" "/opt/retropie/configs/all/emulationstation/gamelists"
#  #rmfile "gamelistfullset.zip"
#  #clearfiles "ZIP"
#  #sudo chown -R pi:pi /opt/retropie/configs/all/emulationstation/gamelists
#}
