#!/usr/bin/env bash

# load up variables
source ./lab_config.sh


INSTANCES_IDS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query "Reservations[*].Instances[*].InstanceId" --output text | tr '\n' ' ')

INSTANCES_IDS_ARRAY=$(echo $INSTANCES_IDS | tr " " "\n")
for instance in $INSTANCES_IDS_ARRAY
do
  instance_id=${instance}
done
aws ec2 associate-address --instance-id ${instance_id} --allocation-id eipalloc-0eef3bb272339dfb6 --allow-reassociation
