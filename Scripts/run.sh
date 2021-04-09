#!/usr/bin/env bash

# fetch config from AWS for currently running infrastructure
source ./load_lab_config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./run-${NOW}.log"


echo "Running Full AWS infrastructure for ${APP_TAG_NAME}:${APP_TAG_VALUE}" | tee ${LOGFILE}
echo "Running run.sh at ${NOW}" | tee -a ${LOGFILE}

PROG="single-node-multithreaded-0.0.1-jar-with-dependencies.jar"

# get public IP addresses of the instances (in the public subnet)
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PublicIpAddress]' --output text | tr '\n' ' ')
PRIVATE_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PrivateIpAddress]' --output text | tr '\n' ' ')
echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}
echo "Private IP addresses: ${PRIVATE_IPS}" | tee -a ${LOGFILE}

IFS=' ' read -r -a INSTANCES_IPS_ARRAY <<< "$INSTANCES_IPS"
IFS=' ' read -r -a PRIVATE_IPS_ARRAY <<< "$PRIVATE_IPS"

NUM_IPS=0
for host in ${INSTANCES_IPS}
do
  NUM_IPS=$((NUM_IPS+1))
done
echo "${NUM_IPS}"

echo "${PRIVATE_IPS_ARRAY[0]}"



for ((i = 0 ; i < NUM_IPS-1 ; i++)); do
  echo "Running ${PROG} at ${USER}@${INSTANCES_IPS_ARRAY[${i}]}:~/ ..." | tee -a ${LOGFILE}
	(ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${i}]} "java -cp ${PROG} edu.cooper.ece465.WorkerMain 6666 ${AIRLINES[${i}]}" | tee -a ${LOGFILE}) & disown %1
#	ssh -n -f user@host "sh -c 'nohup java -cp ${PROG} edu.cooper.ece465.Main 6666 > /dev/null 2>&1 &'"
done
ssh -i ${KEY_FILE} ${USER}@${INSTANCES_IPS_ARRAY[${NUM_IPS}-1]} "java -cp ${PROG} edu.cooper.ece465.CoordinatorMain 6666 ${PRIVATE_IPS_ARRAY[0]} ${PRIVATE_IPS_ARRAY[1]} ${PRIVATE_IPS_ARRAY[2]} ${PRIVATE_IPS_ARRAY[3]}" | tee -a ${LOGFILE}

echo "Done." | tee -a ${LOGFILE}

exit 0


