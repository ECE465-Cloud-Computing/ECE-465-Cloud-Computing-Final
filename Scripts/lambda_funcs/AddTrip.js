// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

const randomBytes = require('crypto').randomBytes;

const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();

function recordTrip(Username, trip) {

    let params = {
        TableName: 'UserTrips',
        Key: { Username: Username },
        ReturnValues: 'ALL_NEW',
        UpdateExpression: 'set #trips = list_append(if_not_exists(#trips, :empty_list), :trip)',
        ExpressionAttributeNames: {
            '#trips': 'trips'
        },
        ExpressionAttributeValues: {
            ':trip': [trip],
            ':empty_list': []
        }
    };

    return ddb.update(params).promise()
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
    console.log(event.body)
    const requestBody = JSON.parse(event.body);


    const trip = requestBody.trip;
    const Username = requestBody.Username;


    recordTrip(Username, trip).then(() => {
        callback(null, {
            statusCode: 201,
            body: ({
                Username: Username,
                trip: trip
            }),
            headers: {
                'Access-Control-Allow-Origin': '*',
            },
        });
    }).catch((err) => {
        console.error(err);
        errorResponse(err.message, context.awsRequestId, callback)
    });
};