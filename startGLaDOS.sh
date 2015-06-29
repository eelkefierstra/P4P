#!/bin/bash
sudo ~/pi-blaster/./pi-blaster --pcm
cd ~/P4P/Endless/bin
export LD_LIBRARY_PATH=/home/pi/git/robidouille/raspicam_cv
java Main
cd ~/
sudo kill $(pgrep pi-blaster)

