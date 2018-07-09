#!/bin/bash
id=$(wget -q -O - http://169.254.169.254/latest/meta-data/instance-id)
aws ec2 terminate-instances --instance-ids $id
