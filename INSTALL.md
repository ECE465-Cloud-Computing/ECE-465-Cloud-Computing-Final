# Installation
- - -
## Requirements
- AWS CLI

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
### 9) Deploy built program
- Run ```deploy.sh```
### 10) Start up the servers
- Run ```run.sh```
### 11) If finished running servers, shut them down.
- Run ```clean.sh```