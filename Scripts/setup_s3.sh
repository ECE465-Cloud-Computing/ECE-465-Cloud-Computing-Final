#!/usr/bin/env bash

source ./lab_config.sh

aws s3 mb s3://${S3_NAME}
aws s3 cp ../Inputs s3://${S3_NAME}/ --recursive
aws s3 ls s3://${S3_NAME} --recursive

