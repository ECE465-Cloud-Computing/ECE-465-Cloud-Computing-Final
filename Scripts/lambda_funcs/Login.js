// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();

exports.handler = (event, context, callback) => {

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