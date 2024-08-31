+++
title = 'Quick Start'
date = 2023-11-03T09:38:53+06:00
weight = 2
+++

# Getting Started
This guide will walk you through the essential steps to quickly get up and running with the JSchema library. It is also assumed a modest familiarity with the Java language, Java SDK, and Java command-line interface, including basic familiarity with Maven packages.

## Maven Library Package
To get started, launch your preferred IDE (such as IntelliJ IDEA, NetBeans IDE, Eclipse IDE, or VS Code) and open the Java project where you intend to include this library package. If you are using a build tool like Maven or Gradle, adding the library to your project is straightforward. For example in Maven project, navigate to the Maven `pom.xml` file and locate the section named `<dependencies>` and add the following XML snippet within the section, replacing `2.x.x` with either the latest version or your preferred version:
```xml
<dependency>
    <groupId>com.relogiclabs.json</groupId>
    <artifactId>relogiclabs-jschema</artifactId>
    <version>2.x.x</version>
</dependency>
```
For additional information regarding this library package, you can visit the Maven repository page of this library [here](https://central.sonatype.com/artifact/com.relogiclabs.json/relogiclabs-json-schema), and files are also available [here](https://repo1.maven.org/maven2/com/relogiclabs/json/relogiclabs-json-schema/).

## Write a Sample to Test
With all the necessary components in place, you are now ready to create a sample schema and validate a corresponding JSON against the schema. The subsequent example presents a Java class featuring a method designed for validating a sample JSON based on a provided schema. If you are working with Java 17 or above, you can enhance the code further by utilizing new language features.
```java
import com.relogiclabs.jschema.JsonSchema;

public class SampleSchema {
    public boolean checkIsValid() {
        var schema =
            """
            %title: "User Profile Response"
            %version: "1.0.0-basic"
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
Let's intentionally introduce a few errors by modifying the previous JSON document and examine the validation results. The revised JSON, shown below, contains two purposeful errors. The `id` within the `user` object is now a string type value, and the `username` is now invalid due to a space inserted in its value.
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
The schema validation using `JsonSchema` attempts to continue processing JSON data even when encountering validation errors, allowing it to identify and report multiple issues in the data all at once. However, if a critical schema error is detected that could lead to unpredictable behavior, the validator will halt processing to prevent unintended consequences.
```json
DataTypeValidationException: Schema (Line: 6:31) Json (Line: 3:14) [DTYPMS01]: Data type mismatched. Data type #integer is expected but found #string inferred by "not number".
FunctionValidationException: Schema (Line: 6:14) Json (Line: 3:14) [FNTRGT01]: Function @range(1, 10000) is incompatible with target data type. A supported data type such as #number is expected but found unsupported target #string of "not number".
FunctionValidationException: Schema (Line: 8:20) Json (Line: 4:20) [REGXCF01]: Target mismatched with regex pattern. A string following pattern "[a-z_]{3,30}" is expected but mismatched for target "john doe".
```

## Assertion for Validation
To utilize this library for test automation and API testing, you can use the following alternative code snippet to perform assertions on input JSON against a specified schema. For instance, let's examine how to assert the JSON, which has been intentionally altered to introduce few errors, against the aforementioned schema. The following demonstrates the adjusted code for asserting the JSON with errors:
```java
...

try {
    JsonAssert.isValid(schema, json);
} catch(Exception e) {
    e.printStackTrace();
}

...
```
In contrast to continuing processing after encountering validation errors, schema assertion using `JsonAssert` throws an exception immediately upon discovering the first error, reducing processing time when a detailed report of all validation issues is not required. The following presents the printed stack trace for the preceding example:
```json
com.relogiclabs.jschema.exception.DataTypeValidationException: DTYPMS01: Data type mismatched
Expected (Schema Line: 6:31): data type #integer
Actual (Json Line: 3:14): found #string inferred by "not number"

	at com.relogiclabs.jschema.node.JValidator.matchDataType(JValidator.java:88)
	at com.relogiclabs.jschema.node.JValidator.match(JValidator.java:77)
	at com.relogiclabs.jschema.node.JObject.match(JObject.java:60)
	at com.relogiclabs.jschema.node.JValidator.match(JValidator.java:71)
	at com.relogiclabs.jschema.node.JObject.match(JObject.java:60)
	at com.relogiclabs.jschema.node.JValidator.match(JValidator.java:71)
	at com.relogiclabs.jschema.node.JRoot.match(JRoot.java:52)
	at com.relogiclabs.jschema.tree.SchemaTree.match(SchemaTree.java:38)
	at com.relogiclabs.jschema.JsonAssert.isValid(JsonAssert.java:61)
	at com.relogiclabs.jschema.JsonAssert.isValid(JsonAssert.java:72)
	at com.example.SampleSchema.checkIsValid(SampleSchema.java:69)
	at com.example.Main.main(Main.java:5)
```
Similar to the above illustration, there are a lot of test cases with various types of examples available in the source code repository [here](https://github.com/relogiclabs/JSchema-Java/tree/master/src/test/java/com/relogiclabs/jschema/test).