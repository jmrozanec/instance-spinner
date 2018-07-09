#!/bin/bash
HOME="/home/ec2-user"
META_HOME=$HOME/metadata
mkdir -p $META_HOME

echo "This can be used to pack and upload trained models somewhere ..."

time=$(date '+%Y-%m-%d-%H-%M-%S')
filename="model-$time.tar.gz"
tar -czf $filename $META_HOME
#TODO once we packed the model, we can upload it to some repository
