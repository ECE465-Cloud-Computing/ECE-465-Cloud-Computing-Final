#!/usr/bin/env bash

# Set up for Lambda
aws iam create-role --role-name UserTripLambda --assume-role-policy-document file://Policy/trust-policy.json
aws iam put-role-policy --role-name UserTripLambda --policy-name DynamoDBPermissions --policy-document file://Policy/lambda_policy.json
aws iam attach-role-policy --role-name UserTripLambda --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

# Remove existing lambda zip files and zip exisitng files
rm lambda_funcs/*.zip
zip ./lambda_funcs/AddTrip.zip ./lambda_funcs/AddTrip.js
zip ./lambda_funcs/AddUser.zip ./lambda_funcs/AddUser.js
zip ./lambda_funcs/DeleteTrip.zip ./lambda_funcs/DeleteTrip.js
zip ./lambda_funcs/GetTrip.zip ./lambda_funcs/GetTrip.js
zip ./lambda_funcs/Login.zip ./lambda_funcs/Login.js


ACCOUNT_ID=$(aws sts get-caller-identity | jq ".Account" | tr -d '"')

# Remove the old functions
aws lambda delete-function --function-name AddTrip
aws lambda delete-function --function-name AddUser
aws lambda delete-function --function-name DeleteTrip
aws lambda delete-function --function-name GetTrip
aws lambda delete-function --function-name Login
# Update lambda functions
aws lambda create-function --function-name AddTrip --zip-file fileb://lambda_funcs/AddTrip.zip --handler ./lambda_funcs/AddTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name AddUser --zip-file fileb://lambda_funcs/AddUser.zip --handler ./lambda_funcs/AddUser.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name DeleteTrip --zip-file fileb://lambda_funcs/DeleteTrip.zip --handler ./lambda_funcs/DeleteTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name GetTrip --zip-file fileb://lambda_funcs/GetTrip.zip --handler ./lambda_funcs/GetTrip.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
aws lambda create-function --function-name Login --zip-file fileb://lambda_funcs/Login.zip --handler ./lambda_funcs/Login.handler --runtime nodejs12.x --role arn:aws:iam::${ACCOUNT_ID}:role/UserTripLambda
