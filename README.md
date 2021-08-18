# Getting Started

### Problem Statement

    Implement a rest service that accepts requests to validate a bank account and returns information if the requested account is valid. 
    The service doesn't store any data but instead sends validation requests to other account data providers, 
    aggregates this data and returns to the client. DO NOT implement data providers, only the service. 
    Assume those data providers exist and are given their names and api urls.
    Request has one mandatory field - bank account number.
    It may have an optional field "providers", which is a list of names of data providers used to query information.
    If this field is empty then all providers defined in the application's configuration must be used.
    Data providers are defined in the application's configuration and must not be written in the code. 
    Every provider accepts requests with one mandatory field - bank account number. Data providers return messages with 
    only one field "isValid", which is boolean.
    You don't need to implement data providers, only the service that polls data providers.
    Response is an array of objects, each object has two fields: provider and isValid. 
    Provider is a string and is the name of a data provider, isValid is a boolean value that data provider returned.

### Pre-Requisites to run the framework:

    1) JDK 1.8
    2) Maven 3.8.2

### How to run the framework:

    1) Download the project from github and unzip it (if downloaded as zip).
    2) Navigate to the project's directory.
    3) Execute the command: "mvn spring-boot:run -Drun.profiles=<env>" where <env> can be uat/prod. For example: "mvn spring-boot:run -Drun.profiles=uat"
    4) Use a postman to consume the validate-account api.

    **  PS: The jar file can be generated out of this project and that jar will need the application-<env>.yml based on environment
    **      and can be run as: "java -jar accountValidate-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=uat"

### API's created:

    1) validate-account (HttpMethod.POST and HttpMethod.OPTIONS) and input for the same

        {
          "accountNumber": "12345678",
          "providers": [
            "provider1",
            "provider2"
          ]
        }

    **  PS: Reason to chose POST HttpMethod for this API was:
    **      PUT generally does not return a response body but 201 or 200 in most of the cases.
    **      GET generally does not accept any payload
    **      Since it is just for validation purpose and there is no database persistence, POST can be used.

### Assumptions:

    1) Data provider api existence had to be assumed and hence the account number which are even numbers would return true for the providers
    2) Providers which are part of the application-<env> ymls are valid and anything other than, our api will return a BAD_REQUEST.
    3) Duplicate providers in the input will be handled gracefully and output will be provided for only one of the duplicates.
    4) Account Number is only made of numbers.
    5) Account Number cannot be empty.
    6) OPTIONS have been added to show the supported http methods for validate-account.

### Tests Covered:

    Controller:
        * testValidateAccountControllerWithInvalidContentType
        * testValidateAccountControllerWithNoProvider
        * testValidateAccountController
        * testValidateAccountControllerWithEmptyAccountNumber
        * testValidateAccountControllerWithMissingAccountNumber
        * testValidateAccountControllerWithDuplicateProvider
        * testValidateAccountControllerWithInvalidProvider
        * testValidateAccountControllerWithMissingProviderParam
        * testValidateAccountControllerWithAlphaNumericAccountNumber
        * testOptionsForValidateAccountController

    Service:
        * verifyInputFromServiceToDataProvider
        * verifyServiceCallsDataProvider

    Data Provider:
        * verifyDataProviderWithMissingAccountNumber()
        * testDataProviderWithInvalidProviderName()
        * verifyDataProviderWithValidInput()
        * verifyDataProviderWithInvalidAccountNumber()
        * verifyServiceCallsDataProviderWithEmptyProvidersSet()
        * verifyDataProviderForAccountNumber()
        * verifyServiceCallsDataProviderWithNoProviders()

### Usage:

    1) POST /validate-account
        curl -X POST http://localhost:8080/validate-account --data '{"accountNumber": "123", "providers": ["provider1","provider2"]}' --header "Content-Type:application/json"

    2) OPTIONS /validate-account
        curl -v -X OPTIONS http://localhost:8080/validate-account"# validate-account-demo" 
