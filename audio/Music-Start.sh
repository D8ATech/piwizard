#!/bin/bash
sudo rm /home/pi/RetroPie/roms/music/DisableMusic
mpg123 -f 16384 -Z /home/pi/bgm/*.mp3 >/dev/null 2>&1 &
