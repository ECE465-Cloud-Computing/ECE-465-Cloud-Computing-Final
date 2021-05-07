#!/usr/bin/env bash

source ./lab_config.sh

# Make s3 bucket
aws s3 mb s3://${S3_NAME}
# Copy input files inside Inputs folder to s3
aws s3 cp ../Inputs s3://${S3_NAME}/ --recursive
# List new contents of s3 bucket
aws s3 ls s3://${S3_NAME} --recursive

