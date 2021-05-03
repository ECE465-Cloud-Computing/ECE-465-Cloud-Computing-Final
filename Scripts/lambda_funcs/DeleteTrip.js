// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0


const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();

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


    const Username = requestBody.Username;

    let params = {
        TableName: 'UserTrips',
        Key: {Username: Username},
        UpdateExpression: 'set #trips = :empty_list',
        ExpressionAttributeNames: {
            '#trips': 'trips'
        },
        ExpressionAttributeValues: {
            ':empty_list': []
        }
    };

    ddb.update(params, function(err, data) {
        if (err) {
            errorResponse(err.message, context.awsRequestId, callback);
        } else {
            callback(null, {
                statusCode: 201,
                body: JSON.stringify(data),
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
            });
        }
    });

};