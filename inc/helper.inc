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
          systemupdate
      elif [ $LOCAL = $BASE ]; then
          echo "Found a new version, updating..."
          #git clean -f &> /dev/null
          git pull --force &> /dev/null
          display_output 10 60 "New version of the launcher has been installed.\n Press a buton on your controller to exit\n You will need to restart launcher."
          systemupdate
          exitLauncher
          clear
          exit 1
      elif [ $REMOTE = $BASE ]; then
          echo "There are unpushed changes on client that would be lost.  Exiting"
          exitLauncher
          exit 1
      else
          echo "Code has diverged.  Delete this directory structure, re-clone the repository and try again. Exiting Now"
          exitLauncher
          exit 99
      fi
    fi
}

function loadText(){
  # load the text files form the Server
  debugwrite ">>> loadText"
}

function turnoffMusic(){
  # turn off the music
  debugwrite ">>> turnoffMusic"
  if [ ! -f $MUSICFILE ]; then
    sudo touch "$MUSICFILE"
    turnOnMusicOnExit="True"
  else
    turnOnMusicOnExit="False"
  fi

}

function turnOnMusic(){
  # turn on the music
  debugwrite ">>> turnOnMusic"
  if [ "$turnOnMusicOnExit" = "True" ]; then
    rmfile "$MUSICFILE"
  fi
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
    echo "$1"
  fi
}

function isVIP(){
  if [ -z "$1" ]; then
    debugwrite ">>>> Error value not passed to isVIP"
  else
    debugwrite ">> isVIP : $1"
    VIP="$1"
  fi
}

function rmfile(){
  debugwrite ">>> rmfile"
  if [ -z "$1" ]; then
    debugwrite ">>>> Error value not passed to rmfile"
  else
    if [ -f "$1" ]; then
        sudo rm -f "$1"
    fi
  fi
}

function clearPro(){
  cd /home/pi/RetroPie/roms

  for f in */; do
    if [ -d "$f" ]; then
      if [ "$f" != "piwizard/" ]; then
        if [ "$f" != "dev/" ]; then
          if [ "$f" != "music/" ]; then
            rmdir "$f"
          fi
        fi
      fi
    fi
  done
  cd $SCRIPTPATH
  rebt "System Updated, Rebooting now..."
}

function rmtextfiles(){
  debugwrite ">>> rmtextfiles"
  rmfile "$SCRIPTPATH/inc/downloadstatus"
  rmfile "$SCRIPTPATH/inc/announce"
  rmfile "$SCRIPTPATH/inc/piwizard.main.txt"
  rmfile "$SCRIPTPATH/inc/one.pro.txt"
  rmfile "$SCRIPTPATH/inc/one.standard.txt"
  rmfile "$SCRIPTPATH/inc/game.pro.txt"
  rmfile "$SCRIPTPATH/inc/game.standard.txt"
  rmfile "$SCRIPTPATH/inc/music.pro.txt"
  rmfile "$SCRIPTPATH/inc/music.standard.txt"
  rmfile "$SCRIPTPATH/inc/colors.txt"
  rmfile "$SCRIPTPATH/inc/scripts.txt"
  rmfile "$SCRIPTPATH/inc/srd.txt"
  rmfile "$SCRIPTPATH/inc/one.support.txt"
  rmfile "$SCRIPTPATH/inc/one.upgrade.txt"
  rmfile "$SCRIPTPATH/inc/tocdenied.txt"
  rmfile "$SCRIPTPATH/inc/piwizard.validvip.txt"
  rmfile "$SCRIPTPATH/inc/piwizard.standard.txt"
  rmfile "$SCRIPTPATH/inc/piwizard.expiredvip.txt"
  rmfile "$SCRIPTPATH/inc/thanks.txt"
  rmfile "$SCRIPTPATH/inc/menu.games.pro.txt"
  rmfile "$SCRIPTPATH/inc/menu.games.standard.txt"
}

function rmdir(){
  if [ -n "$1" ]; then
    if [ -d "$1" ]; then
      sudo rm -rf "$1"
    fi
  fi
}

function cleanupExit(){
  debugwrite ">>> cleanupExit"
  rmfile "$OUTPUT"
  rmfile "$INPUT"
  rmtextfiles
  echo "$thanksPage"
  sleep 5
}

function downloadedImageCheck(){
  debugwrite ">>> downloadedImageCheck"
  if [[ -f "/bin/iploaded" ]]; then
    rmfile "/bin/iploaded"
    clearPro
  fi
}

function smallscreencheck(){
  debugwrite ">>> smallscreencheck"
  displaymsg=""

  if [ $MAXWIDTH -lt 160 ]; then
  	displaymsg=" The screen width is $MAXWIDTH which is less than 160.\n"
  fi

  if [ $MAXHEIGHT -lt 55 ]; then
  	displaymsg="$displaymsg The screen height is $MAXHEIGHT which is less than 55.\n"
  fi

  if [ -n "$displaymsg" ]; then
  	displaymsg="\n$displaymsg Some of the screen may be cut off. The menus should all\n"
  	displaymsg="$displaymsg work fine but much of the extra information could be hidden\n or jumbled on the right and bottom of the screen"
  	display_output 11 70 "$displaymsg"
  fi
}

function getVersion(){
  imgVersion=$(tail -n 1 update.log | awk -F '-' '{ print $1 }')
  lVersion=$(git tag | head -n 1)

  if [ -z "$lVersion" ]; then
    lVersion="$LAUNCHERVERSION"
  fi

  FOOTERTEXT="                    http://facebook.com/groups/thepiwizard - support@thepiwizard.com - http://thepiwizard.com - https://paypal.me/piwizard                    \n\n Serial Number of this device: \Zb\Z1$sn\Zn  Image Version: \Zb\Z1$imgVersion\Zn Launcher Version: \Zb\Z1$lVersion\Zn Build: \Zb\Z1$LOCAL\Zn"

}

function licensecheck(){
  debugwrite ">>> licensecheck"
  if [ -z "$SerialNo" ]; then
    sn=$(cat /proc/cpuinfo | grep Serial| cut -d ' ' -f 2) #store the serial in the $sn value
  else
    sn="$SerialNo"
  fi

  if [ -z "$sn" ]; then
    sn="NOT DETECTED"
  fi

  getVersion

  if [ $ONLINE -gt 0 ]; then
    expiredvip=$(curl -s http://expire.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)

  	if [[ $expiredvip = "VIP" ]]; then
        VIP="No"
  			dialog --msgbox "$expiredVipPage" 12 81
    else
      apicall=$(curl -s http://api.thepiwizard.com/vip/${sn} | cut -f2- -d: | cut -d '}' -f 1 | cut -c2- | rev | cut -c2- | rev)
      if [[ $ADMINPRO = "TRUE" ]]; then
        apicall="VIP"
      fi
      if [[ $apicall = "VIP" ]]; then
          VIP="Yes"
          dialog --colors --msgbox "$vipPage" 32 81
      else
          VIP="No"
          downloadedImageCheck
          dialog --colors --msgbox "$standardPage" 38 81
      fi
    fi
  else
    VIP="No"
    dialog --colors --msgbox "$standardPage" 38 81
  fi
}

function deny(){
    debugwrite ">>> deny"
    dialog --colors --msgbox "$tocDeniedPage" 9 55
    closeAllDialogs
}

function upgrade(){
  debugwrite ">>> upgrade"
	dialog --colors --msgbox "$upgradePage" 32 75
}

function support(){
  debugwrite ">>> support"
  dialog --colors --msgbox  "$supportPage" 27 75
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

function singlerom(){
  debugwrite ">>> singlerom"
  if [ "$sn" = "NOT DETECTED" ]; then
    display_output 10 60 "Sorry, Single ROM Download is only availabe when your serial number is detected."
    return
  fi

  if [ "$VIP" = "No" ]; then
    display_output 10 60 "Sorry, Single ROM Download is only available when you are a Pro member"
    return
  fi

  # Display Instructions
  dialog --colors --msgbox "$srdText" 38 80
  # Download the serialno.zip
  cd /home/pi/RetroPie/roms
  rmfile "$sn.zip"
  srddownloadserver="http://thepiwizardserver.com/srd"
  wget --trust-server-names "$srddownloadserver/$sn.zip" 2>& 1 | \
  stdbuf -o0 awk -F '%' '{ print $1 }' | awk -F ' ' '{ print $NF }' | \
  dialog --gauge "Downloading $sn.zip" 10 100

  # Did the Download Succeed?
  if [ -f "$sn.zip" ]; then
      # Get the contents of the zip into an array

      # BAD unzip -l "$sn.zip" | grep "/" | sed 's/  / /g' | sed 's/  / /g' | cut -d ' ' -f 5- > files.txt
      unzip -l "$sn.zip" | grep "/" > unformatedfiles.txt
      touch files.txt
      while read p; do
        gameFile=$(echo "$p" | awk -F '/' '{ print $2 }' | sed 's/ /\\ /g')
        gamePath=$(echo "$gameFile" | awk -F '-' '{ print $1 }')
        echo "$gamePath/$gameFile" >> files.txt
      done <unformatedfiles.txt
      rmfile "unformatedfiles.txt"
      # Extract the zip

      unzipprogress "/home/pi/RetroPie/roms/$sn.zip" "/home/pi/RetroPie/roms" 1

      # go though the array
      while read p; do
          zippath=$(echo $p | awk -F '/' '{ print $1 }')
          gamename=$(echo $p | awk -F '/' '{ print $2 }')
          unzipname="/home/pi/RetroPie/roms/$zippath/$gamename"

          # display_output 10 80 "zippath: $zippath \ngamename: $gamename\n unzipname:$unzipname"

          cd "/home/pi/RetroPie/roms/$zippath"
          sudo chown -R pi:pi *.*

          unzipprogress "$unzipname" "/home/pi/RetroPie/roms/$zippath"
          cd "/home/pi/RetroPie/roms"
          if [ "$unzipsuccess" != "True" ]; then
            displayerror="True"
          else
            rmfile "/home/pi/RetroPie/roms/$p"
          fi
          unzipsuccess="True"
      done <files.txt
      rmfile "/home/pi/RetroPie/roms/files.txt"

      # If Everything was succesful call API to delete zip on SERVER
      if [ "$displayerror" != "True" ]; then
        display_output 10 40 "$sn.zip Single Rom Dowload Pack Installed"
        rmfile "/home/pi/RetroPie/roms/$sn.zip"
        curl -q "http://thepiwizardserver.com/srd/delete.php?sn=$sn"
      else
        display_output 52 142 "$unzipmessage"
        unzipmessage=""
        displayerror="False"
      fi
  else
    # Did the Download Fail?  Abort
    errormessage="$sn.zip was either not found or failed to download.\n"
    errormessage+="If you have not received an email please try again after you get the email.\n"
    errormessage+="If you have received the email and you are getting this message, \n"
    errormessage+="did you get the email more than 24 hours ago?\n"
    errormessage+="If you so you will need to start over.\n"
    errormessage+="Otherwise, please contact support."
    display_output 10 80 "$errormessage"
  fi
  cd $SCRIPTPATH
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

function cpfiles(){
  debugwrite ">>> cpfiles $1 to $2"
  if [ -z "$1" ]; then
    dialog_output 10 40 "No source file was provided"
    return
  fi
  if [ -z "$2" ]; then
    dialog_output 10 40 "No destination file was provided"
    return
  fi

  if [ -z "$3" ]; then
    cp -rf $1 $2
  else
    sudo cp -rf $1 $2
  fi
}

function colorpicker(){
  debugwrite ">>> colorpicker"
  case $1 in
    Blue-Light) cpfiles  "$SCRIPTPATH/library/01blue_lightrc" "$SCRIPTPATH/.dialogrc";;
    Blue-Dark)  cpfiles "$SCRIPTPATH/library/01blue_darkrc" "$SCRIPTPATH/.dialogrc";;
    Red-Light) cpfiles "$SCRIPTPATH/library/01red_lightrc" "$SCRIPTPATH/.dialogrc";;
    Red-Dark)  cpfiles "$SCRIPTPATH/library/01red_darkrc" "$SCRIPTPATH/.dialogrc";;
    Yellow-Light) cpfiles "$SCRIPTPATH/library/01yellow_lightrc" "$SCRIPTPATH/.dialogrc";;
    Yellow-Dark) cpfiles "$SCRIPTPATH/library/01yellow_darkrc" "$SCRIPTPATH/.dialogrc";;
    Green-Light) cpfiles "$SCRIPTPATH/library/01green_lightrc" "$SCRIPTPATH/.dialogrc";;
    Green-Dark) cpfiles "$SCRIPTPATH/library/01green_darkrc" "$SCRIPTPATH/.dialogrc";;
    Magenta-Light) cpfiles "$SCRIPTPATH/library/01magenta_lightrc" "$SCRIPTPATH/.dialogrc";;
    Magenta-Dark) cpfiles "$SCRIPTPATH/library/01magenta_darkrc" "$SCRIPTPATH/.dialogrc";;
    Cyan-Light) cpfiles "$SCRIPTPATH/library/01cyan_lightrc" "$SCRIPTPATH/.dialogrc";;
    Cyan-Dark) cpfiles "$SCRIPTPATH/library/01cyan_darkrc" "$SCRIPTPATH/.dialogrc";;
    NoColor) cpfiles "$SCRIPTPATH/library/01no_colorrc" "$SCRIPTPATH/.dialogrc";;
    *) display_output 10 60 "Somehow an incorrect value was sent to the colorpicker function";;
  esac
  DIALOGRC="$SCRIPTPATH/.dialogrc"
  export DIALOGRC
}

function runScript(){
  debugwrite ">>> runScript $1"
  if [ -z $1 ]; then
    break
  fi

  if [ -f "$SCRIPTPATH/scripts/$1" ]; then
    cd "$SCRIPTPATH/scripts"
    sudo chmod +x "$1" && sudo "./$1" && sudo chmod -x "$1"
    cd "$SCRIPTPATH"
  fi
}

function config(){
  display_output 10 40 "Coming Soon"
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
         0) download="True";;
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
            downloadserver="http://games.thepiwizard.com/pro"
        else
            downloadserver="http://games.thepiwizard.com/standard"
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
  romcounter=$(curl -s http://thepiwizard.com/counter/counter.txt)
}

function getscreeninfo(){
  debugwrite ">>> getscreeninfor"
    if [ $ONLINE -gt 0 ]; then
      cd /home/pi/RetroPie/roms/piwizard/inc
      rmtextfiles

      for i in {1..20}
      do
        case $i in
          1) wget --trust-server-names --quiet "http://thepiwizard.com/downloadstatus" 2>& 1;;
          2) wget --trust-server-names --quiet "http://thepiwizard.com/announce" 2>& 1;;
            # Load Text Screens
          3) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/piwizard.main.txt" 2>& 1;;
          4) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/one.pro.txt" 2>& 1;;
          5) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/one.standard.txt" 2>& 1;;
          6) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/game.pro.txt" 2>& 1;;
          7) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/game.standard.txt" 2>& 1;;
          8) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/music.pro.txt" 2>& 1;;
          9) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/music.standard.txt" 2>& 1;;
          10) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/colors.txt" 2>& 1;;
          11) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/scripts.txt" 2>& 1;;
            # Load Static Text pages
          12) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/one.support.txt" 2>& 1;;
          13) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/one.upgrade.txt" 2>& 1;;
          14) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/tocdenied.txt" 2>& 1;;
          15) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/piwizard.validvip.txt" 2>& 1;;
          16) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/piwizard.standard.txt" 2>& 1;;
          17) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/piwizard.expiredvip.txt" 2>& 1;;
          18) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/thanks.txt" 2>& 1
              wget --trust-server-names --quiet "http://launcher.thepiwizard.com/srd.txt" 2>& 1;;
            # Load Menus
          19) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/menu.games.pro.txt" 2>& 1;;
          20) wget --trust-server-names --quiet "http://launcher.thepiwizard.com/menu.games.standard.txt" 2>& 1;;
        esac
        echo $(($i * 5))
      done | dialog --title 'Initialization' --guage 'Downloading...' 10 100 0

      currentStatus=$(<downloadstatus)
      announcements=$(<announce)
      mainText=$(<piwizard.main.txt)
      mainMenuVip=$(<one.pro.txt)
      mainMenuStandard=$(<one.standard.txt)
      gamesMenuVip=$(<game.pro.txt)
      gamesMenuStandard=$(<game.standard.txt)
      musicMenuVip=$(<music.pro.txt)
      musicMenuStandard=$(<music.standard.txt)
      colorMenuText=$(<colors.txt)
      scriptsMenuText=$(<scripts.txt)
      supportPage=$(<one.support.txt)
      upgradePage=$(<one.upgrade.txt)
      tocDeniedPage=$(<tocdenied.txt)
      vipPage=$(<piwizard.validvip.txt)
      standardPage=$(<piwizard.standard.txt)
      expiredVipPage=$(<piwizard.expiredvip.txt)
      thanksPage=$(<thanks.txt)
      srdText=$(<srd.txt)
      cd $SCRIPTPATH
    else
      currentStatus="Currently Offline"
      announcements="Currently Offline"
      mainText="Currently Offline"
      mainMenuVip="Currently Offline"
      mainMenuStandard="Currently Offline"
      gamesMenuVip="Currently Offline"
      gamesMenuStandard="Currently Offline"
      musicMenuVip="Currently Offline"
      musicMenuStandard="Currently Offline"
      colorMenuText="Currently Offline"
      scriptsMenuText="Currently Offline"
      supportPage="Currently Offline"
      upgradePage="Currently Offline"
      tocDeniedPage="Currently Offline"
      vipPage="Currently Offline"
      standardPage="Currently Offline"
      expiredVipPage="Currently Offline"
      thanksPage="Currently Offline"
      srdText="Currently Offline"
    fi
}

function exitLauncher(){
  debugwrite ">>> exitLauncher"
  turnOnMusic
  cleanupExit
  exit 0
}

function rebt(){
    debugwrite ">>> rebt"
    if [ -n "$1" ]; then
      dialog --infobox "$thanksPage" 12 80
    else
      dialog --infobox "$1" 12 80
    fi
    closeAllDialogs
    cleanupExit
    turnOnMusic
    sudo reboot now
  	exit 91
  	break
}

function ext(){
  debugwrite ">>> ext"
	dialog --infobox "$tocDeniedPage" 12 80
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
  debugwrite "Made it to unzipprogress - 1:$1 | 2:$2 | 3:$3"
  if [ -n "$1" ]; then
    filename="$1"
    if [ -n "$2" ]; then
      destination="$2"

      if [ -f "$filename" ]; then
        filename=$(sed 's/(/\\(/g' <<< "$filename")
        filename=$(sed 's/)/\\)/g' <<< "$filename")
        filename=$(sed 's/ /\\ /g' <<< "$filename")

        if [ -n "$3" ]; then
          eval sudo unzip -o "$filename" -d "$destination" 2>&1 | dialog --title "Unzipping $filename" --progressbox 3 102 #;
        else
          eval unzip -o "$filename" -d "$destination" 2>&1 | dialog --title "Unzipping $filename" --progressbox 3 102 #;
        fi
        retcode=$?
        if [ $retcode -ne 0 ]; then
          unzipsuccess="False"
          unzipmessage+=$(printf "unzip '$filename' failed with exit code [%d].\n" $retcode)
          unzipmessage+="\n"
        else
          if [ "$unzipsuccess" != "False" ]; then
            unzipsuccess="True"
          fi
          #unzipmessage="success"
        fi
      else
        unzipsuccess="False"
        unzipmessage+=$(printf "unzip '$filename' failed becuase the file was not found")
        unzipmessage+="\n"
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

  if [ -f "update.log" ]; then
    readarray -t remoteversion < update.log

    cd $SCRIPTPATH

  # determine what versions need to be installed
    toinstall=(`echo ${remoteversion[@]} ${localversion[@]} | tr ' ' '\n' | sort | uniq -u `)
    #declare -p toinstall


  # if new version available ask if they want to install
    if [ ${#toinstall[@]} -gt 0 ]; then
      upgradenow="True"
    else
      upgradenow="False"
    fi
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
      return 99
    fi
  #else
    #display_output 5 102 "Already up to date"
  fi
  rmfile "$SCRIPTPATH/library/update.log"
}
