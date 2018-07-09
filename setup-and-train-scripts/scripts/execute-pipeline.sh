#!/bin/bash
HOME="/home/ec2-user"
logfile="/tmp/pipeline-status.txt"
# we should execute:
# data-sync.sh
# architecture-train.py
# architecture-predict.py
echo "[data sync] [START]">>$logfile
/bin/bash $HOME/data-sync.sh
echo "[data sync] [END]">>$logfile
echo "[model train] [START]">>$logfile
/usr/bin/python $HOME/architecture-train.py > /tmp/architecture-train.out
echo "[model train] [END]">>$logfile
echo "[pack and upload] [START]">>$logfile
/bin/bash $HOME/pack-and-upload.sh
echo "[pack and upload] [END]">>$logfile
echo "[terminate instance] [START]">>$logfile
#/bin/bash $HOME/terminate-instance.sh
echo "[terminate instance] [START]">>$logfile
