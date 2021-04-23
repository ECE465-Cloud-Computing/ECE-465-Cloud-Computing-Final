#!/usr/bin/env bash

# load up variables
source ./load_lab_config.sh

aws ec2 authorize-security-group-ingress ${PREAMBLE} --group-id sg-0920e9a57e8bc44d7 --protocol tcp --port 0-65535 --cidr 0.0.0.0/0 | tee ${LOGFILE}
