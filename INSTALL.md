# Installation
- - -
## Requirements
- AWS CLI

### NOTE: All commands should be ran in the scripts directory.
### 1) Setup SSH key
- Run ```create_keypair.sh```
### 2) Setup VPC
- Run ```create_vpc.sh```
### 3) Setup EC2 instances
- Run ```create_ec2_in_vpc.sh```
### 4) Setup DynamoDB
- Run ```dynamo_setup.sh```
### 5) Setup S3
- First run ```s3_role_setup.sh```
- Then run ```setup_s3.sh```
### 6) Setup Lambda
- Run ```lambda_setup.sh```
### 7) Setup Elastic IP
- Go to the AWS Console and go to VPC. Click on **Elastic IPS** on the left **Virtual Private Cloud** dropdown.
- Click on **Allocate Elastic IP Address** at the top right and **Allocate**.
- Open up ```lab_config.sh``` and overwrite **ELASTIC_ID** and **ELASTIC_IP** with the details of the newly allocated Elastic ip. 
### 8) Setup Gateway (Manually)
- Resource: /
    * GET - Method Execution
      * **Setup**
        * **Integration type**: HTTP
        * **HTTP Method**: GET
        * **Endpoint URL**: ```http://[PUT ELASTIC IP HERE]:5000```
      * Add to **Method Request** query string **start**, **filter**, **end**
- Resource: /trips
    * DELETE - Method Execution
      * **Setup**
        * **Integration type**: Lambda Function
        * **Lambda Function**: DeleteTrip
      * Go to **Integration Request**, **Mapping Templates**, and change check **When there are no templates defined (recommended)**.
        * Add mapping template ```application/json```
        * Add to the template ```{"body": "{\"Username\":\"$input.params('Username')\"}"}```
        * Save
    * GET - Method Execution
      * **Setup**
          * **Integration type**: Lambda Function
          * **Lambda Function**: GetTrip
      * Go to **Integration Request**, **Mapping Templates**, and change check **When there are no templates defined (recommended)**.
          * Add mapping template ```application/json```
          * Add to the template ```{"body": "{\"Username\":\"$input.params('Username')\"}"}```
          * Save
      * Go to **Method Request**, **URL Query String Parameters**, and add the query string ```Username```. Check the box marked required and save.
    * POST - Method Execution
        * **Setup**
            * **Integration type**: Lambda Function
            * **Lambda Function**: AddTrip
- Resource: /user
    * GET - Method Execution
        * **Setup**
            * **Integration type**: Lambda Function
            * **Lambda Function**: Login
      * Go to **Integration Request**, **Mapping Templates**, and change check **When there are no templates defined (recommended)**.
          * Add mapping template ```application/json```
          * Add to the template
            
            ```{"body": "{\"Username\":\"$input.params('Username')\", \"password\":\"$input.params('password')\"}"}```
          * Save
      * Go to **Method Request**, **URL Query String Parameters**, and add the query strings ```Username``` and ```Password```. Check the boxes marked required and save both.
      * Go to **Method Response**, click **Add Response**, and add HTTP Status ```400```.
      * Go to **Integration Response** and click **Add Integration response**.
          * Lambda Error Regex: ```.*Error 400.*```
          * Method Response Status: ```400```
          * Save.
  * POST - Method Execution
      * **Setup**
          * **Integration type**: Lambda Function
          * **Lambda Function**: AddUser
    * Go to **Method Response**, click **Add Response**, and add HTTP Status ```500```.
    * Go to **Integration Response** and click **Add Integration response**.
        * Lambda Error Regex: ```.*Error 500.*```
        * Method Response Status: ```500```
        * Save.
- Go through each resource and click on **ACTIONS** and **Enable CORS**. Enable CORS on each resource.
- Click on **Actions** and **Deploy API**, create a new stage and name it ```beta```. Click Deploy
- Go to **Stages** on the left drop down and select **beta**. Copy the **Invoke URL** and go to ```react-app/src/index.js```. Change ```axios.defaults.baseURL = 'https://ce4c9ztp47.execute-api.us-east-1.amazonaws.com/beta';``` to ```axios.defaults.baseURL = '[PLACE INVOKE URL HERE]';```
### 9) AWS Amplify
  * Create a web app using the react-app folder. If you choose to use the Github repo, choose the main branch and check ```Connecting a monorepo? Pick a folder.```. Enter for the root directory ```react-app```.
### 10) Deploy built program
- Run ```deploy.sh```
### 11) Start up the servers
- Run ```run.sh```
### 12) If finished running servers, shut them down.
- Run ```clean.sh```

## Note: If you encounter a browser error with CORS, then go back to your API gateway. Go to the **Integration Response** of the offending method and verify that header mappings has the expected CORS headers. Do the same for the **Method Response**.