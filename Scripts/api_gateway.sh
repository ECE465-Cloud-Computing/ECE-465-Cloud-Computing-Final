#!/usr/bin/env bash

source ./load_lab_config.sh

CHILDNAME=GETROUTE

API_ID=$(aws apigateway create-rest-api --name 'ece465final' --region us-west-2) | jq '.id'
echo ${API_ID}

ROOT_ID=$(aws apigateway get-resources --rest-api-id ${API_ID} --region us-west-2) | jq '.items.id'

CHILD_ID=$(aws apigateway create-resource --rest-api-id ${API_ID} ${PREAMBLE} --parent-id ${ROOT_ID} --path-part ${CHILDNAME}) | jq '.id'

aws apigateway put-method --rest-api-id ${API_ID} --resource-id ${CHILD_ID} --http-method GET --authorization-type "NONE" ${PREAMBLE} --request-parameters method.request.querystring.start=true,method.request.querystring.end=true,method.request.querystring.filter=true

aws apigateway put-method-response --rest-api-id ${CHILD_ID} --resource-id ${CHILD_ID} --http-method GET --status-code 200  ${PREAMBLE}

URI=$(aws apigateway put-integration --rest-api-id ${CHILD_ID} --resource-id ${CHILD_ID} --http-method GET --type HTTP --integration-http-method GET ${PREAMBLE}) | jq '.uri'

aws apigateway put-integration-response --rest-api-id ${CHILD_ID} --resource-id ${CHILD_ID} --http-method GET --status-code 200 --selection-pattern ""  ${PREAMBLE}

aws apigateway create-deployment --rest-api-id ${CHILD_ID} ${PREAMBLE} --stage-name test --stage-description 'Test stage' --description 'First deployment'