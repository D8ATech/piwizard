# Helper script with functions used by piwizard.sc.  Not diectly runnable
self_update() {
    cd $SCRIPTPATH
    echo "Checking for lastest version of Pi Wizard. One moment please."
    LOCAL=$(git rev-parse master)
    REMOTE=$(git ls-remote | grep HEAD | awk -F ' ' '{print $1}')
    BASE=$(git merge-base @ "refs/heads/master")
    ONLINE=$(cat $REMOTE | grep "Could not resolve" | wc -l)

    if [ ! $ONLINE -gt 0 ]; then
      if [  $LOCAL = $REMOTE ]; then
          # display_output 10 40 "VERSION = $LOCAL"
          debugwrite "Already Up to Date.  Continuing..."
      elif [ $LOCAL = $BASE ]; then
          echo "Found a new version, updating..."
          git pull --force
          display_output 10 60 "New version of the launcher has been installed.\n Press a buton on your controller to reboot.."
          rebt "silent"
          exit 1
      elif [ $REMOTE = $BASE ]; then
          echo "There are unpushed changes on client that would be lost.  Exiting"
          exit 1
      else
          echo "Code has diverged.  Delete this directory structure, re-clone the repository and try again. Exiting Now"
          exit 99
      fi
    else
      display_output 10 60 "Pi Wizard was not able connect to the Internet./nSome functionality will not be available"
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

function smallscreencheck(){
  if [ $MAXWIDTH -lt 160 ]; then
  	displaymsg=" The screen width is $MAXWIDTH which is less than 160.\n"
  else
  	displaymsg=""
  fi

  if [ $MAXHEIGHT -lt 55 ]; then
  	displaymsg="$displaymsg The screen height is $MAXHEIGHT which is less than 55.\n"
  fi

  if [ ! -z "$displaymsg" ]; then
  	displaymsg="\n$displaymsg Some of the screen may be cut off. The menus should all\n"
  	displaymsg="$displaymsg work fine but much of the extra information could be hidden\n or jumbled on the right and bottom of the screen"
  	display_output 11 70 "$displaymsg"
  fi
}

function licensecheck(){
  sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value

  if [  -z "$sn" ]; then
    sn="NOT DETECTED"
  fi

  FOOTERTEXT=" http://facebook.com/groups/thepiwizard - support@thepiwizard.com - http://thepiwizard.com - http://forum.thepiwizard.com - http://paypal.me/thepiwizard \n\n Serial Number of this device: $sn          Version: $LOCAL"

  if [ ! $ONLINE -gt 0 ]; then
    expiredvip=$(curl -s http://expire.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)

  	if [[ $expiredvip = "VIP" ]]; then
        VIP="No"
  			dialog --textbox inc/piwizard.expiredvip.txt 12 81
    else
      apicall=$(curl -s http://api.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)
      #apicall="VIP"
      if [[ $apicall = "VIP" ]]; then
          VIP="Yes"
          dialog --textbox inc/piwizard.validvip.txt 32 81
      else
          VIP="No"
          dialog --textbox inc/piwizard.standard.txt 38 81
      fi
    fi
  else
    VIP="No"
    dialog --textbox inc/piwizard.standard.txt 38 81
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

  if [ "$sn" -eq "" ]; then
    sn="NOT DETECTED"
  fi

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
    80) display_output 10 60 "The 1980's music pack will be availalbe 10/22/2018";;
      # echo Currently Downloading 80s Music Pack
      #wget --trust-server-names "http://music.thepiwizard.com/80.zip" 2>& 1 | \
      #stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
      #dialog --gauge "Downloading 80.zip" 10 100

      #unzipprogress /home/pi/RetroPie/roms/music/80.zip /home/pi/RetroPie/roms/music
      # unzip -o /home/pi/RetroPie/roms/music/80.zip -d /home/pi/RetroPie/roms/music
      #rm -f /home/pi/RetroPie/roms/music/80.zip
      #display_output 10 40 "80's Music Installed!"
      #;;
    90) display_output 10 60 "The 1990's music pack will be availalbe 10/22/2018";;
      # echo Currently Downloading 90s Music Pack
      #wget -q -c --progress=dot --trust-server-names "http://music.thepiwizard.com/90.zip" 2>& 1 | \
      #stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
      #dialog --gauge "Downloading 90.zip" 10 100

      #unzip -o /home/pi/RetroPie/roms/music/90.zip -d /home/pi/RetroPie/roms/music
      #rm -f /home/pi/RetroPie/roms/music/90.zip
      #display_output 10 40 "90s Music Installed!"
      #;;
    00) display_output 10 60 "The 2000's music pack will be availalbe 10/22/2018";;
    T40) display_output 10 60 "The Top 40's music pack will be availalbe 10/22/2018";;
    Arcade) display_output 10 60 "The Arcade music pack will be availalbe 10/22/2018";;
    GS1) display_output 10 60 "The Game Soundtrack 1 music pack will be availalbe 10/22/2018";;
    GS2) display_output 10 60 "The Game Soundtrack 2 music pack will be availalbe 10/22/2018";;
    *) display_output 10 60 "Somehow an incorrect value was sent to the music function";;
  esac

	cd $SCRIPTPATH
}

function downloader(){
  if [ ! $ONLINE -gt 0 ]; then
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
        wget --trust-server-names "http://roms.thepiwizard.com/pro/$2" 2>& 1 | \
        stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
        dialog --gauge "Downloading $2" 10 100
    else
        wget --limit-rate 500k --trust-server-names "http://roms.thepiwizard.com/$2" 2>& 1 | \
        stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
        dialog --gauge "Downloading $2" 10 100
    fi
    unzipprogress "/home/pi/RetroPie/roms/$1/$2" "/home/pi/RetroPie/roms/$1"
    #unzip -o /home/pi/RetroPie/roms/$1/$2 -d /home/pi/RetroPie/roms/$1
    rm -f /home/pi/RetroPie/roms/$1/$2

    display_output 10 40 "$1 Rom Pack Installed"
    cd $SCRIPTPATH
  else
    display_output 10 40 "Launcher is currently offline so downloading is disabled"
}

function downloadroms(){
  case $1 in
    atari2600) downloader "atari2600" "atari2600.zip";;
    atari5200) downloader "atari5200" "atari5200.zip";;
    atari7800) downloader "atari7800" "atari7800.zip";;
    atarijaguar) downloader "atarijaguar" "atarijaguar.zip";;
    atarilynx) downloader "atarilynx" "atarilynx.zip";;
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
    snesclassic) downloader "snes" "snes-classic.zip";;
    *) display_output 10 65 "Somehow an incorrect value was sent to the downloading function";;
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

  #display_output 10 80 "menuwidth: $1 - menuheight $2 - MAX WIDTH: $MAXWIDTH - MAX HEIGHT: $MAXHEIGHT"

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
  statustextline=$(expr $MENUY + 33)
  infotextcol=$(expr $MENUX + 58)
  menutextcol=$MENUX
  countertextcol=$MENUX
  countertextline=$(expr $statustextline + 6)
  announcetxtcol=$infotextcol
  announcetxtline=$statustextline
  footercol=$menutextcol
  footerline=$(expr $MAXHEIGHT - 7)
  romcounter=$(curl -s http://thepiwizard.com/counter.txt)
}

function getscreeninfo(){
    if [ ! $ONLINE -gt 0 ]; then
      currentStatus=$(curl -s http://thepiwizard.com/downloadstatus)
      announcements=$(curl -s http://thepiwizard.com/announce)
    else
      currentStatus="Currently Offline"
      announcements="Currently Offline"
    fi
}

function rebt(){
    if [ ! -z "$1" ]; then
      dialog --textbox inc/thanks.txt 12 80
    fi
  	# display_output 10 80 "This is where we would reboot but for now we are just going to exit"
    closeAllDialogs
    clear
    echo "Pi Wizard in now rebooting..."
    sudo reboot now
  	exit 91
  	break
}

function ext(){
	dialog --textbox inc/tocdenied.txt 12 80
	exit
}

function disk(){
	df -lh > diskinfo
	echo " " >> diskinfo
	echo " Usually your sd card is named '/' and" >> diskinfo
	echo " your usb storage are /dev/sda, sdb, sdc, sdd " >> diskinfo
	dialog --textbox diskinfo $HEIGHT $WIDTH
	rmfile "./diskinfo"
}

function bios(){
  if [ ! $ONLINE -gt 0 ]; then
    if [ ! -d "/home/pi/RetroPie/BIOS" ]; then
      mkdir /home/pi/RetrpPie/BIOS
    fi

    cd /home/pi/RetroPie/BIOS

    wget -q -c --progress=dot "http://roms.thepiwizard.com/bios.zip" 2>& 1 | \
    stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
    dialog --gauge "Downloading bios.zip" 10 100

  	unzip -o bios.zip -d /home/pi/RetroPie/BIOS
  	rm -f /home/pi/RetroPie/BIOS/bios.zip
  	rm -f /home/pi/RetroPie/BIOS/bios.zip.*
   	display_output 10 40 "Additional BIOS Installed into the Bios Folder"
  	cd $SCRIPTPATH
  else
    display_output 10 40 "Downloading disabled because no internet detected"
  fi 
}

function display_output(){
	local h="$1"
	local w="$2"
  local m="$3"
	dialog --backtitle "Pi Wizard Autonomous Romset Installation" --title "Pi Wizard Message" --msgbox "$m" "$h" "$w"
}

function unzipprogress(){
  debugwrite "Made it to unzipprogress - $1 : $2"
  if [ ! -z $1 ]; then
    filename=$1
    if [ ! -z $2 ]; then
      destination=$2
      pv -n $filename | unzip -o "$filename" -d "$destination" 2>&1 | dialog --colors --title "\Zu\Z5Percentage will not increment\Zn" --gauge "Extracting $filename" 6 60 #;
    else
      debugwrite "no destination passed"
    fi
  else
    debugwrite "no filename passed"
  fi
}
