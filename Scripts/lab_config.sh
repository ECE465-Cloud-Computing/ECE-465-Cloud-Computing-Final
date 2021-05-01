#!/usr/bin/env bash

# PROFILE=default
REGION=us-east-1
# PREAMBLE="--profile ${PROFILE} --region ${REGION}"
PREAMBLE="--region ${REGION}"

VPC_CDR=10.0.0.0/16
PUBLIC_CDR=10.0.1.0/24
PRIVATE_CDR=10.0.2.0/24

INSTANCE_TYPE=t2.micro

# for Amazon Linux 2 on x86_64
AMI_ID=ami-047a51fa27710816e
USER=ec2-user
KEY_NAME=lab1
KEY_FILE=~/.ssh/pems/${KEY_NAME}.pem

APP_TYPE=type
APP_TYPE_NAME=distributed-app
APP_TAG_NAME=APP
APP_TAG_VALUE=multi-node

AIRLINES=("A" "B" "C" "D")
INSTANCES_COUNT=$(((${#AIRLINES[@]})+1))

S3_NAME=ece465spring2021airline
INSTANCE_PROFILE_NAME=EC2-S3
S3_EC2_ROLE_NAME=S3_ACCESS_ROLE
ELASTIC_ID=eipalloc-0eef3bb272339dfb6