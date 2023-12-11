+++
title = 'Source Build'
date = 2023-12-04T09:38:53+06:00
weight = 9
+++

# Build from Source Code
This comprehensive guide illustrates the procedures for retrieving source code from a GitHub repository, compiling the project source code into a library, and seamlessly integrating the compiled library into your project. It is important to have a foundational understanding of the Java language, as well as a modest level of familiarity with the Java SDK and command-line interface. You also need a basic knowledge of how to work with Maven packages.

## Build the Library
To get started, clone the project from the following URL using your preferred Git client (command line or GUI). You can open a terminal and enter the following Git clone command as shown below:
```shell
git clone https://github.com/relogiclabs/JsonSchema-Java.git
```
Next, navigate to the project root directory of the `JsonSchema-Java` that you just cloned. Inside the directory, you should find a `pom.xml` file. This file contains the project configuration and dependencies required to build the library. Use the `cd` command to enter the project root directory and execute the following Maven command to build the Json Schema library:

```shell
mvn clean install
```
Maven will resolve the library's dependencies, compile the code, and create a jar file which will be installed in the local Maven repository.

## Add the Library to Your Project
To integrate the library into your project, open it in your preferred IDE (such as IntelliJ IDEA, NetBeans IDE, Eclipse IDE, or VS Code). If you are using a build tool like Maven or Gradle, adding the library to your project is straightforward. For example in a Maven project, navigate to the Maven `pom.xml` file and locate the section named `<dependencies>` and add the following XML snippet within the section of the file, replacing `1.x.x` with the version you have built:
```xml
<dependency>
    <groupId>com.relogiclabs.json</groupId>
    <artifactId>relogiclabs-json-schema</artifactId>
    <version>1.x.x</version>
</dependency>
```

## Write a Sample to Test
With all the necessary components in place, you are now ready to create a sample schema and validate a corresponding JSON against the schema. The subsequent example presents a Java class featuring a method designed for validating a sample JSON based on a provided schema. If you are working with Java 17 or above, you can enhance the code further by utilizing new language features.
```java
import com.relogiclabs.json.schema.JsonSchema;

public class SampleSchema {
    public boolean checkIsValid() {
        var schema =
            """
            %title: "User Profile Response"
            %version: 1.0.0
            %schema:
            {
                "user": {
                    "id": @range(1, 10000) #integer,
                    /*username does not allow special characters*/
                    "username": @regex("[a-z_]{3,30}") #string,
                    /*currently only one role is allowed by system*/
                    "role": "user" #string,
                    "isActive": #boolean, //user account current status
                    "registeredAt": #time,
                    "profile": {
                        "firstName": @regex("[A-Za-z ]{3,50}") #string,
                        "lastName": @regex("[A-Za-z ]{3,50}") #string,
                        "dateOfBirth": #date,
                        "age": @range(18, 130) #integer,
                        "email": @email #string,
                        "pictureURL": @url #string,
                        "address": {
                            "street": @length(10, 200) #string,
                            "city": @length(3, 50) #string,
                            "country": @regex("[A-Za-z ]{3,50}") #string
                        } #object #null
                    }
                }
            }
            """;
        var json =
            """
            {
                "user": {
                    "id": 1111,
                    "username": "johndoe",
                    "role": "user",
                    "isActive": true,
                    "registeredAt": "2023-09-06T15:10:30.639Z",
                    "profile": {
                        "firstName": "John",
                        "lastName": "Doe",
                        "dateOfBirth": "1993-06-17",
                        "age": 30,
                        "email": "john.doe@example.com",
                        "pictureURL": "https://example.com/picture.jpg",
                        "address": {
                            "street": "123 Some St",
                            "city": "Some town",
                            "country": "Some Country"
                        }
                    }
                }
            }
            """;
        var jsonSchema = new JsonSchema(schema);
        return jsonSchema.isValid(json);
    }
}
```

## Create Validation Errors
Let's intentionally introduce a few errors by modifying the previous JSON document and then examine the validation results. To begin, we'll alter the `id` within the `user` object to a string type and observe the outcome. Additionally, we'll modify the `username` by inserting a space into its value, thus creating an invalid `username`. Below is the revised JSON representation, now containing these purposeful errors.
```json
{
    "user": {
        "id": "not number",
        "username": "john doe",
        "role": "user",
        "isActive": true,
        "profile": {
            "firstName": "John",
            "lastName": "Doe",
            "age": 30,
            "email": "john.doe@example.com",
            "pictureURL": "https://example.com/picture.jpg",
            "address": {
                "street": "123 Some St",
                "city": "Some town",
                "country": "Some Country"
            }
        }
    }
}
```
To achieve the desired outcome, please make the following changes to the preceding code. Specifically, ensure that any schema validation errors are displayed in the console. The modified code snippet that invokes the `writeError` method to display the errors if validation fails is as follows:
```java
...

var jsonSchema = new JsonSchema(schema);
if(!jsonSchema.isValid(json)) jsonSchema.writeError();

...
```
Here is the error as displayed in the console. More specific errors will be listed first, followed by more general errors. Consequently, the specific errors will precisely pinpoint the issues within the JSON document, while the generic errors will provide contextual information about where the errors occurred.
```json
Schema (Line: 6:31) Json (Line: 3:14) [DTYP04]: Data type mismatch. Data type #integer is expected but found #string inferred by "not number".
Schema (Line: 6:14) Json (Line: 3:14) [FUNC03]: Function @range(1, 10000) is incompatible with the target data type. Applying to a supported data type such as #number is expected but applied to an unsupported data type #string of "not number".
Schema (Line: 8:20) Json (Line: 4:20) [REGX01]: Regex pattern does not match. String of pattern "[a-z_]{3,30}" is expected but found "john doe" that mismatches with pattern.
Schema (Line: 5:12) Json (Line: 2:12) [VALD01]: Validation failed. Value {"id": @range(1, 10000) #integer, "username": @regex("[a-z_]{3,30}") #string, "role": "user" #string, "isActive": #boolean, "register...ing, "country": @regex("[A-Za-z ]{3,50}") #string} #object #null}} is expected but found {"id": "not number", "username": "john doe", "role": "user", "isActive": true, "registeredAt": "2023-09-06T15:10:30.639Z", "profile":...: "123 Some St", "city": "Some town", "country": "Some Country"}}}.
Schema (Line: 4:0) Json (Line: 1:0) [VALD01]: Validation failed. Value {"user": {"id": @range(1, 10000) #integer, "username": @regex("[a-z_]{3,30}") #string, "role": "user" #string, "isActive": #boolean, ...ng, "country": @regex("[A-Za-z ]{3,50}") #string} #object #null}}} is expected but found {"user": {"id": "not number", "username": "john doe", "role": "user", "isActive": true, "registeredAt": "2023-09-06T15:10:30.639Z", "... "123 Some St", "city": "Some town", "country": "Some Country"}}}}.
```

## Assertion for Validation
To utilize this library for test automation and API testing, you can use the following alternative code snippet to perform assertions on input JSON against a specified schema. For instance, let's examine how to assert the JSON, which has been intentionally altered to introduce some errors, against the aforementioned schema. The following demonstrates the adjusted code for asserting the JSON with errors:
```java
...

try {
    JsonAssert.isValid(schema, json);
} catch(Exception e) {
    e.printStackTrace();
}

...
```
The following presents the printed stack trace for the preceding example. It's important to note that when using `JsonAssert`, it throws an exception upon encountering the first error, thus preventing the continuation of processing the rest of the schema:
```json
com.relogiclabs.json.schema.exception.JsonSchemaException: DTYP04: Data type mismatch
Expected (Schema Line: 6:31): data type #integer
Actual (Json Line: 3:14): found #string inferred by "not number"

	at com.relogiclabs.json.schema.type.JDataType.matchForReport(JDataType.java:90)
	at com.relogiclabs.json.schema.type.JDataType.matchForReport(JDataType.java:72)
	at com.relogiclabs.json.schema.type.JValidator.lambda$matchDataType$4(JValidator.java:74)
	at com.relogiclabs.json.schema.type.JValidator.matchDataType(JValidator.java:74)
	at com.relogiclabs.json.schema.type.JValidator.match(JValidator.java:64)
	at com.relogiclabs.json.schema.type.JObject.match(JObject.java:57)
	at com.relogiclabs.json.schema.type.JValidator.match(JValidator.java:59)
	at com.relogiclabs.json.schema.type.JObject.match(JObject.java:57)
	at com.relogiclabs.json.schema.type.JValidator.match(JValidator.java:59)
	at com.relogiclabs.json.schema.type.JRoot.match(JRoot.java:50)
	at com.relogiclabs.json.schema.tree.SchemaTree.match(SchemaTree.java:33)
	at com.relogiclabs.json.schema.JsonAssert.isValid(JsonAssert.java:61)
	at com.relogiclabs.json.schema.JsonAssert.isValid(JsonAssert.java:72)
	at org.example.SampleSchema.checkIsValid(SampleSchema.java:64)
	at org.example.Main.main(Main.java:5)
```
For more information about the schema syntax format and library functionalities, please refer to the reference documentation [here](/JsonSchema-Java/api/index.html).