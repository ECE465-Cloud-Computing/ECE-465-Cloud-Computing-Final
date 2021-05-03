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


exports.handler = (event, context, callback) => {

    // The body field of the event in a proxy integration is a raw string.
    // In order to extract meaningful values, we need to first parse this string
    // into an object. A more robust implementation might inspect the Content-Type
    // header first and use a different parsing strategy based on that value.
    console.log(event.body)
    const requestBody = JSON.parse(event.body);

    const username = requestBody.Username;
    const password = requestBody.password;

    let param = {
        TableName:"UserTrips",
        KeyConditionExpression: '#usr = :u',
        ExpressionAttributeNames:{
            "#usr": "Username"
        },
        ExpressionAttributeValues: {
            ':u': username,
        },
    }

    ddb.query(param, function(err, data) {
        if (err) {
            callback(new Error("Error 400: Username not found."));
        } else if (data.Items[0] == undefined) {
            callback(new Error("Error 400: Username not found."));
        } else if (data.Items[0].password != password) {
            callback(new Error("Error 400: Invalid password."));
        } else {
            callback(null, {
                body: {
                    'username': (data.Items[0].Username),
                    'trips': (data.Items[0].trips)
                },
            });
        }
    });
};