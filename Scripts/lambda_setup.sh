#!/usr/bin/env bash

#aws iam create-role --role-name UserTripLambda --assume-role-policy-document file://Policy/trust-policy.json
#aws iam put-role-policy --role-name UserTripLambda --policy-name DynamoDBPermissions --policy-document file://Policy/lambda_policy.json
#aws iam attach-role-policy --role-name UserTripLambda --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
#
#zip ./lambda_funcs/AddTrip.zip ./lambda_funcs/AddTrip.js
#zip ./lambda_funcs/AddUser.zip ./lambda_funcs/AddUser.js
#zip ./lambda_funcs/DeleteTrip.zip ./lambda_funcs/DeleteTrip.js
#zip ./lambda_funcs/GetTrip.zip ./lambda_funcs/GetTrip.js


ACCOUNT_ID=$(aws sts get-caller-identity | jq ".Account" | tr -d '"')

aws lambda delete-function --function-name AddTrip
aws lambda delete-function --function-name AddUser
aws lambda delete-function --function-name DeleteTrip
aws lambda delete-function --function-name GetTrip

aws lambda create-function --function-name AddTrip --zip-file fileb://lambda_funcs/AddTrip.zip --handler ./lambda_funcs/AddTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name AddUser --zip-file fileb://lambda_funcs/AddUser.zip --handler ./lambda_funcs/AddUser.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name DeleteTrip --zip-file fileb://lambda_funcs/DeleteTrip.zip --handler ./lambda_funcs/DeleteTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name GetTrip --zip-file fileb://lambda_funcs/GetTrip.zip --handler ./lambda_funcs/GetTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
