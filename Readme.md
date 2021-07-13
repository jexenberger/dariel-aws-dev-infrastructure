# Utility for deploying a new Development environment for AWS

### Features
* Create secure VPC
* Create private Subnets
* Create Codecommit repository
* Create Codebuild project with S3 bucket

### Usage

1. Install Maven
2. Install AWS CDK
3. Checkout this project
4. create the following file: _**~/.dev-environment.properties**_
5. populate with the following properties:
```properties
# Name of the project
name=my-project

# Name of the code commit repository
repository=my-project-repo

# Build command
buildCommand=mvn package

# Command to package
packageCommand=docker build -t . myimage

#Path of the output file
outputFile=target/my-project-1.0-SNAPSHOT.jar
```
6. run __cdk deploy__ in the project folder