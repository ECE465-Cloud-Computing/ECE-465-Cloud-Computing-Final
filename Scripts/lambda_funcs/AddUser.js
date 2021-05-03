// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

const randomBytes = require('crypto').randomBytes;

const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();


function toUrlString(buffer) {
    return buffer.toString('base64')
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=/g, '');
}

function errorResponse(errorMessage, awsRequestId, callback) {
    callback(null, {
        statusCode: 500,
        body: JSON.stringify({
            Error: errorMessage,
            Reference: awsRequestId,
        }),
        headers: {
            'Access-Control-Allow-Origin': '*',
        },
    });
}


exports.handler = (event, context, callback) => {

    // The body field of the event in a proxy integration is a raw string.
    // In order to extract meaningful values, we need to first parse this string
    // into an object. A more robust implementation might inspect the Content-Type
    // header first and use a different parsing strategy based on that value.
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
            // errorResponse(err.message, context.awsRequestId, callback);
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