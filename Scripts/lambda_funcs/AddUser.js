// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();

exports.handler = (event, context, callback) => {
    console.log(event.body)
    const requestBody = JSON.parse(event.body);

    const Username = requestBody.Username;
    const password = requestBody.password;

    let param = {
        TableName:"UserTrips",
        Item:{
            "Username": Username,
            "password": password
        },
        ConditionExpression: "attribute_not_exists(Username)"
    }

    ddb.put(param, function(err, data) {
        if (err) {
            callback(new Error("Error 500: Username already exists."));
        } else {
            callback(null, {
                statusCode: 201,
                body: ({
                    Username: Username,
                    password: password
                }),
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
            });
        }
    });
};