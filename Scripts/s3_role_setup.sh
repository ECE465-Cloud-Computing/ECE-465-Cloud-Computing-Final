#!/usr/bin/env bash

source ./lab_config.sh

echo "Running create-role"
echo q | aws iam create-role --role-name ${S3_EC2_ROLE_NAME} --assume-role-policy-document file://./Policy/trust_policy.json
echo "Running put-role-policy"
aws iam put-role-policy --role-name ${S3_EC2_ROLE_NAME} --policy-name Permissions-Policy-For-Ec2 --policy-document file://./Policy/permission_policy.json
echo "Running create-instance-profile"
aws iam create-instance-profile --instance-profile-name ${INSTANCE_PROFILE_NAME}
echo "Running add-role-to-instance-profile"
aws iam add-role-to-instance-profile --instance-profile-name ${INSTANCE_PROFILE_NAME} --role-name ${S3_EC2_ROLE_NAME}