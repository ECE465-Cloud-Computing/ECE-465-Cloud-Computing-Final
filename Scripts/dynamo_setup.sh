#!/usr/bin/env bash

aws dynamodb create-table --table-name UserTrips --attribute-definitions AttributeName=Username,AttributeType=S --key-schema AttributeName=Username,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1