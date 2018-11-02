# Helper script with functions used by piwizard.sc.  Not diectly runnable
self_update() {
    debugwrite ">>> self_update"
    cd $SCRIPTPATH
    #echo "Checking for lastest version of Pi Wizard. One moment please."

    LOCAL=$(git rev-parse master)
    REMOTE=$(git ls-remote | grep HEAD | awk -F ' ' '{print $1}')
    BASE=$(git merge-base @ "refs/heads/master")

    if [ -z $REMOTE ]; then
      ONLINE=0
    else
      ONLINE=1
    fi
    #read -p "check values"

    if [ $ONLINE -gt 0 ]; then
      if [  $LOCAL = $REMOTE ]; then
          # display_output 10 40 "VERSION = $LOCAL"
          debugwrite "Already Up to Date.  Continuing..."
      elif [ $LOCAL = $BASE ]; then
          echo "Found a new version, updating..."
          #git clean -f &> /dev/null
          git pull --force &> /dev/null
          display_output 10 60 "New version of the launcher has been installed.\n Press a buton on your controller to exit\n You will need to restart launcher."
          exit 1
      elif [ $REMOTE = $BASE ]; then
          echo "There are unpushed changes on client that would be lost.  Exiting"
          exit 1
      else
          echo "Code has diverged.  Delete this directory structure, re-clone the repository and try again. Exiting Now"
          exit 99
      fi
    fi
}

function turnoffMusic(){
  # turn off the music
  debugwrite ">>> turnoffMusic"
  if [ ! -f $MUSICFILE ]; then
    touch "$MUSICFILE"
  fi
}

function turnonMusic(){
  # turn on the music
  debugwrite ">>> turnonMusic"
  rmfile "$MUSICFILE"
}

function onlinecheck(){
  debugwrite ">>> onlinecheck"
  pOnline=$(ping -c1 google.com | grep "1 packets transmitted, 1 received" | wc -l)
  if [ $pOnline -eq 1 ]; then
    ONLINE=1
  else
    display_output 10 60 "Pi Wizard was not able connect to the Internet.\nSome functionality will not be available"
  fi
}

function output_help(){
  clear
  debugwrite ">>> output_help"
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
  fi
}

function isVIP(){
  if [ ! -z $1 ]; then
    debugwrite "isVIP : $1"
    VIP=$1
  fi
}

function rmfile(){
  debugwrite ">>> rmfile"
  if [ ! -z $1 ]; then
    if [ -f $1 ]; then
        sudo rm $1
    fi
  fi
}

function rmdir(){
  if [ ! -z $1 ]; then
    rm -rf $1
  fi
}

function cleanupExit(){
  debugwrite ">>> cleanupExit"
  rmfile "$OUTPUT"
  rmfile "$INPUT"
  clear
  echo "Thanks for using PI WIZARD"
  sleep 5
}

function smallscreencheck(){
  debugwrite ">>> smallscreencheck"
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
  debugwrite ">>> licensecheck"
  sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value

  if [  -z "$sn" ]; then
    sn="NOT DETECTED"
  fi

  FOOTERTEXT=" http://facebook.com/groups/thepiwizard - support@thepiwizard.com - http://thepiwizard.com - http://forum.thepiwizard.com - http://paypal.me/thepiwizard \n\n Serial Number of this device: $sn          Version: $LOCAL"

  if [ $ONLINE -gt 0 ]; then
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
    debugwrite ">>> deny"
    dialog --textbox inc/tocdenied.txt 9 55
    closeAllDialogs
}

function upgrade(){
  debugwrite ">>> upgrade"
	dialog --textbox inc/one.upgrade.txt 32 75
}

function support(){
  debugwrite ">>> support"
  dialog --textbox inc/one.support.txt 27 75
}

function serial(){
  debugwrite ">>> serial"
  sn=$(cat /proc/cpuinfo | grep Serial)

  if [ "$sn" -eq "" ]; then
    sn="NOT DETECTED"
  fi

  dialog --title "Your Serial Number - $sn" --textbox inc/one.serial.txt 15 80
}

function setupmusic(){
  debugwrite ">>> setupmusic"
  if [ ! -d "/home/pi/RetroPie/roms/music" ]; then
    mkdir /home/pi/RetroPie/roms/music
  else
    cd /home/pi/RetroPie/roms/music
    rmfile "*.zip"
    rmfile "*.zip.*"
  fi
}

function music(){
  debugwrite ">>> music"
  setupmusic
  case $1 in
    80) display_output 10 60 "The 1980's music pack will be availalbe 10/22/2018";;
      # echo Currently Downloading 80s Music Pack
      #wget --trust-server-names "http://music.thepiwizard.com/80.zip" 2>& 1 | \
      #stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
      #dialog --gauge "Downloading 80.zip" 10 100

      #unzipprogress /home/pi/RetroPie/roms/music/80.zip /home/pi/RetroPie/roms/music
      # unzip -o /home/pi/RetroPie/roms/music/80.zip -d /home/pi/RetroPie/roms/music
      #rmfile "/home/pi/RetroPie/roms/music/80.zip"
      #display_output 10 40 "80's Music Installed!"
      #;;
    90) display_output 10 60 "The 1990's music pack will be availalbe 10/22/2018";;
      # echo Currently Downloading 90s Music Pack
      #wget -q -c --progress=dot --trust-server-names "http://music.thepiwizard.com/90.zip" 2>& 1 | \
      #stdbuf -o0 awk '/[.] +[0-9][0-9]?[0-9]?%/ { print substr($0,63,3) }' | \
      #dialog --gauge "Downloading 90.zip" 10 100

      #unzip -o /home/pi/RetroPie/roms/music/90.zip -d /home/pi/RetroPie/roms/music
      #rmfile "/home/pi/RetroPie/roms/music/90.zip"
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
  debugwrite ">>> downloader"
  if [ -z $3 ]; then
    count=1
  else
    count=$3
  fi

  if [ $ONLINE -gt 0 ]; then
    download="False"
    filesexist=$(ls -al /home/pi/RetroPie/roms/$1 | grep -v "drw" | grep -v "total" | wc -l)

    if [ $filesexist -gt 0 ]; then
      dialog --title "Download $2 again?" \
        --yesno "This ROM was previously downloaded.\nAre you sure you want to download it again?" 7 60
      response=$?
      case $response in
         0) rmdir "/home/pi/RetroPie/roms/$1"
            download="True";;
         *) download="False";;
      esac
    else
      download="True"
    fi

    if [ "$download" == "True" ]; then

      if [ ! -d "/home/pi/RetroPie/roms/$1" ]; then
        mkdir /home/pi/RetroPie/roms/$1
      fi

      cd /home/pi/RetroPie/roms/$1
      i=1
      while [ $i -le $count ]
        do
          if [ "$VIP" == "Yes" ]; then
              downloadserver="http://roms.thepiwizard.com/pro"
          else
              downloadserver="http://roms.thepiwizard.com"
          fi

          wget --trust-server-names "$downloadserver/$2-$i.zip" 2>& 1 | \
          stdbuf -o0 awk -F '%' '{ print $1 }' | awk -F ' ' '{ print $NF }' | \
          dialog --gauge "Downloading $2-$i.zip" 10 100

          unzipprogress "/home/pi/RetroPie/roms/$1/$2-$i.zip" "/home/pi/RetroPie/roms/$1"
          if [ "$unzipsuccess" != "True" ]; then
            displayerror="True"
          else
            rmfile "/home/pi/RetroPie/roms/$1/$2-$i.zip"
          fi
          unzipsuccess="True"
          ((i++))
        done

        if [ "$displayerror" != "True" ]; then
          display_output 10 40 "$1 Rom Pack Installed"
        else
          display_output 10 102 "$unzipmessage"
          unzipmessage=""
          displayerror="False"
        fi
    else
      display_output 10 40 "Download canceled per your request"
    fi

    cd $SCRIPTPATH
  else
    display_output 10 40 "Launcher is currently offline so downloading is disabled"
  fi
}

function downloadroms(){
  debugwrite ">>> downloadroms"
  case $1 in
    atari2600_pro) downloader "atari2600" "atari2600" 1;;
    atari5200_pro) downloader "atari5200" "atari5200" 1;;
    atari7800_pro) downloader "atari7800" "atari7800" 1;;
    atarilynx_pro) downloader "atarilynx" "atarilynx" 1;;
    coleco_pro) downloader "coleco" "coleco" 1;;
    famicom_pro) downloader "famicom" "famicom" 1;;
    fds_pro) downloader "fds" "fds" 1;;
    gameandwatch_pro) downloader "gameandwatch" "gameandwatch" 1;;
    gamegear_pro) downloader "gamegear" "gamegear" 1;;
    gb_pro) downloader "gb" "gb" 1;;
    gba_pro) downloader "gba" "gba" 5;;
    gbc_pro) downloader "gbc" "gbc" 1;;
    mame2003_pro) downloader "mame2003" "mame2003" 1;;
    markiii_pro) downloader "markiii" "markiii" 1;;
    mastersystem_pro) downloader "mastersystem" "mastersystem" 1;;
    megadrive_pro) downloader "megadrive" "megadrive" 1;;
    msx2_pro) downloader "msx2" "msx2" 1;;
    msx2plus_pro) downloader "msx2+" "msx2+" 1;;
    n64_pro) downloader "n64" "n64" 3;;
    nes_pro) downloader "nes" "nes" 1;;
    snes_pro) downloader "snes" "snes" 2;;
    snesclassic_pro) downloader "snes-classic" "snesclassic" 1;;
    pcengine_pro) downloader "pcengine" "pcengine" 1;;
    arcade_pro) downloader "arcade" "arcade" 6;;

    atari2600) downloader "atari2600" "atari2600" 1;;
    atari5200) downloader "atari5200" "atari5200" 1;;
    atari7800) downloader "atari7800" "atari7800" 1;;
    atarilynx) downloader "atarilynx" "atarilynx" 1;;
    nes) downloader "nes" "nes" 1;;
    snes) downloader "snes" "snes" 1;;
    n64) downloader "n64" "n64" 1;;
    gb) downloader "gb" "gb" 1;;
    gba) downloader "gba" "gba" 1;;
    gbc) downloader "gbc" "gbc" 1;;
    gamegear) downloader "gamegear" "gamegear" 1;;
    megadrive) downloader "megadrive" "megadrive" 1;;
    neogeo) downloader "neogeo" "neogeo" 1;;

    *) display_output 10 65 "Somehow an incorrect value was sent to the downloading function";;
  esac
}

function closeAllDialogs(){
  debugwrite ">>> closeAllDialogs"
  RUNNING="FALSE"
  MAINRUNNING="FALSE"
  ONERUNNING="FALSE"
  MUSICRUNNING="FALSE"
  GAMESRUNNING="FALSE"
}

function findcenter(){
  debugwrite ">>> findcenter"
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
  debugwrite ">>> getscreeninfor"
    if [ $ONLINE -gt 0 ]; then
      currentStatus=$(curl -s http://thepiwizard.com/downloadstatus)
      announcements=$(curl -s http://thepiwizard.com/announce)
    else
      currentStatus="Currently Offline"
      announcements="Currently Offline"
    fi
}

function rebt(){
    debugwrite ">>> rebt"
    if [ ! -z "$1" ]; then
      dialog --textbox inc/thanks.txt 12 80
    fi
  	# display_output 10 80 "This is where we would reboot but for now we are just going to exit"
    closeAllDialogs
    clear
    turnonMusic
    echo "Pi Wizard in now rebooting..."
    sudo reboot now
  	exit 91
  	break
}

function ext(){
  debugwrite ">>> ext"
	dialog --textbox inc/tocdenied.txt 12 80
	exit
}

function disk(){
  debugwrite ">>> disk"
	df -lh > diskinfo
	echo " " >> diskinfo
	echo " Usually your sd card is named '/' and" >> diskinfo
	echo " your usb storage are /dev/sda, sdb, sdc, sdd " >> diskinfo
	dialog --textbox diskinfo $HEIGHT $WIDTH
	rmfile "./diskinfo"
}

function bios(){
  debugwrite ">>> bios"
  if [ $ONLINE -gt 0 ]; then
    if [ ! -d "/home/pi/RetroPie/BIOS" ]; then
      mkdir /home/pi/RetrpPie/BIOS
    fi

    cd /home/pi/RetroPie/BIOS

    wget --trust-server-names "http://roms.thepiwizard.com/bios.zip" 2>& 1 | \
    stdbuf -o0 awk -F '%' '{ print $1 }' | awk -F ' ' '{ print $NF }' | \
    dialog --gauge "Downloading bios.zip" 10 100

  	unzipprogress "/home/pi/RetroPie/BIOS/bios.zip" "/home/pi/RetroPie/BIOS"
  	rmfile "/home/pi/RetroPie/BIOS/bios.zip"
  	rmfile "/home/pi/RetroPie/BIOS/bios.zip.*"
   	display_output 10 40 "Additional BIOS Installed into the Bios Folder"
  	cd $SCRIPTPATH
  else
    display_output 10 40 "Downloading disabled because no internet detected"
  fi
}

function display_output(){
  debugwrite ">>> display_output"
	local h="$1"
	local w="$2"
  local m="$3"
	dialog --backtitle "Pi Wizard Autonomous Romset Installation" --title "Pi Wizard Message" --msgbox "$m" "$h" "$w"
}

function unzipprogress(){
  debugwrite ">>> unzipprogress"
  debugwrite "Made it to unzipprogress - $1 : $2 : $3"
  if [ ! -z $1 ]; then
    filename=$1
    if [ ! -z $2 ]; then
      destination=$2

      if [ -f $filename ]; then
        if [ ! -z $3 ]; then
          eval sudo unzip -o "$filename" -d "$destination" 2>&1 | dialog --title "Unzipping $filename" --progressbox 3 102 #;
        else
          eval unzip -o "$filename" -d "$destination" 2>&1 | dialog --title "Unzipping $filename" --progressbox 3 102 #;
        fi
        retcode=$?
        if [ $retcode -ne 0 ]; then
          unzipsuccess="False"
          unzipmessage+=$(printf "unzip '$1' failed with exit code [%d]." $retcode)
          #printf "Error : [%d] when executing command: '$cmnd'" $ret_code
        else
          unzipsuccess="True"
          #unzipmessage="success"
        fi
        #pv -n $filename | unzip -o "$filename" -d "$destination" 2>&1 | dialog --colors --title "\Zu\Z5Percentage will not increment\Zn" --gauge "Extracting $filename" 6 60 #;
      else
        unzipsuccess="False"
        unzipmessage+=$(printf "unzip '$1' failed becuase the file was not found")
      fi
    else
      debugwrite "no destination passed"
    fi
  else
    debugwrite "no filename passed"
  fi
}

function processScript(){
  debugwrite ">>> processScript"
  filetorun="/home/pi/RetroPie/roms/dev/updatecleanup.sc"
  if [ -f "$filetorun" ]; then
    sudo chmod +x $filetorun
    source "$filetorun"
    retcode=$?
    if [ $retcode -eq 0 ]; then
      DATE=$(date +%Y-%m-%d)
      sudo chmod -x $filetorun
      sudo mv "$filetorun" "$filetorun.$DATE"
    else
      display_output 10 40 "The cleanup script failed to run with return code: $retcode"
    fi
  fi
}


function systemupdate(){
  debugwrite ">>> systemupdate"
  declare -a localversion
  declare -a remoteversion
  toinstall=()

  # Read local update.log
    # If local upate.log doesn't exist assume 2.0
    if [ ! -f "update.log" ]; then
      echo "2.0-Base" >> update.log
    fi
    readarray -t localversion < update.log

  # Read Remote update.log
    rmfile  "$SCRIPTPATH/library/update.log"
    cd /home/pi/RetroPie/roms/piwizard/library

    wget --trust-server-names "http://thepiwizard.com/upgrade/update.log" 2>& 1 | \
    stdbuf -o0 awk -F '%' '{ print $1 }' | awk -F ' ' '{ print $NF }' | \
    dialog --gauge "Checking For Updates" 10 100

    readarray -t remoteversion < update.log

    cd $SCRIPTPATH

  # determine what versions need to be installed
    toinstall=(`echo ${remoteversion[@]} ${localversion[@]} | tr ' ' '\n' | sort | uniq -u `)
    #declare -p toinstall


  # if new version available ask if they want to install
    if [ ${#toinstall[@]} -gt 0 ]; then
      dialog --title "Upgrades Available" \
        --yesno "There are upgrades available to install.\nDo you want to download and install\nA reboot is required?" 7 60
      response=$?
      case $response in
         0) upgradenow="True";;
         *) upgradenow="False";;
      esac
    else
      upgradenow="False"
    fi

  # If yes, go through loop of updating each version one at a time
  if [ "$upgradenow" == "True" ]; then
    for installloop in "${toinstall[@]}"; do
      updatefile=$(echo $installloop | awk -F '-' '{ print $NF }')

      cd /home/pi/RetroPie/roms/piwizard/library
      rmfile "/home/pi/RetroPie/roms/piwizard/library/$updatefile"

      wget --trust-server-names "http://thepiwizard.com/upgrade/$updatefile" 2>& 1 | \
      stdbuf -o0 awk -F '%' '{ print $1 }' | awk -F ' ' '{ print $NF }' | \
      dialog --gauge "Downloading $updatefile" 10 100

      if [ -f "$updatefile" ]; then
        unzipprogress "/home/pi/RetroPie/roms/piwizard/library/$updatefile" "/" "sudo"
      else
        displayerror="True"
        unzipmessage+="File Download Failure - $updatefile"
      fi

      if [ "$unzipsuccess" != "True" ]; then
        displayerror="True"
      else
        rmfile "/home/pi/RetroPie/roms/piwizard/library/$updatefile"
        echo "$installloop" >> /home/pi/RetroPie/roms/piwizard/update.log
        processScript
      fi

    done

    cd $SCRIPTPATH

    if [ "$displayerror" != "True" ]; then
      display_output 10 40 "Upgrade installed.  Time to reboot"
      rmfile  "$SCRIPTPATH/library/update.log"
      rebt
    else
      display_output 10 102 "$unzipmessage"
    fi
  else
    display_output 5 102 "Already up to date"
  fi
  rmfile "$SCRIPTPATH/library/update.log"
}
