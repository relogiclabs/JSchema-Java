+++
title = 'Quick Start'
date = 2023-10-08T09:38:53+06:00
weight = 2
+++

# Getting Started
This guide will walk you through the essential steps to quickly get up and running with New JSON Schema library. It is also assumes a modest familiarity with the Java SDK and Java command-line interface including basic familiarity with Maven packages. Additionally, it considers a certain level of knowledge in Java language.

## Maven Library Package
To get started, launch your preferred IDE (such as IntelliJ IDEA, NetBeans IDE, Eclipse IDE, or VS Code) and open the Java project where you intend to include this library package. If you are using a build tool like Maven or Gradle, adding the library to your project is straightforward. For example in Maven project, navigate to the Maven `pom.xml` file and locate the section named `<dependencies>` and add the following XML snippet within the section of the file, replacing `1.x.x` with either the latest version or your preferred version:
```xml
<dependency>
    <groupId>com.relogiclabs.json</groupId>
    <artifactId>relogiclabs-json-schema</artifactId>
    <version>1.x.x</version>
</dependency>
```
For additional information regarding this library package, you can visit the Maven repository page of this library [here](https://central.sonatype.com/artifact/com.relogiclabs.json/relogiclabs-json-schema), and files are also available [here](https://repo1.maven.org/maven2/com/relogiclabs/json/relogiclabs-json-schema/).

## Write a Sample to Test
With the necessary components in place, you are now prepared to create a sample schema and validate a corresponding JSON against the schema. The subsequent example presents a Java class featuring a method designed for validating a sample JSON based on a provided schema.
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
                    "profile": {
                        "firstName": @regex("[A-Za-z ]{3,50}") #string,
                        "lastName": @regex("[A-Za-z ]{3,50}") #string,
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
                    "id": 1234,
                    "username": "johndoe",
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
Schema (Line: 6:14) [FUNC03]: Function @range(1, 10000) is applicable on #number but applied on #string of "not number"
Schema (Line: 8:20) Json (Line: 4:20) [REGX01]: Regex pattern does not match. String of pattern "[a-z_]{3,30}" is expected but found "john doe" that mismatches with pattern.
Schema (Line: 5:12) Json (Line: 2:12) [VALD01]: Validation Failed. Value {"id": @range(1, 10000) #integer, "username": @regex("[a-z_]{3,30}") #string, "role": "user" #string, "isActive": #boolean, "profile"...ing, "country": @regex("[A-Za-z ]{3,50}") #string} #object #null}} is expected but found {"id": "not number", "username": "john doe", "role": "user", "isActive": true, "profile": {"firstName": "John", "lastName": "Doe", "a...: "123 Some St", "city": "Some town", "country": "Some Country"}}}.
Schema (Line: 4:0) Json (Line: 1:0) [VALD01]: Validation Failed. Value {"user": {"id": @range(1, 10000) #integer, "username": @regex("[a-z_]{3,30}") #string, "role": "user" #string, "isActive": #boolean, ...ng, "country": @regex("[A-Za-z ]{3,50}") #string} #object #null}}} is expected but found {"user": {"id": "not number", "username": "john doe", "role": "user", "isActive": true, "profile": {"firstName": "John", "lastName": ... "123 Some St", "city": "Some town", "country": "Some Country"}}}}.
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

	at com.relogiclabs.json.schema.types.JDataType.matchForReport(JDataType.java:85)
	at com.relogiclabs.json.schema.types.JValidator.lambda$match$0(JValidator.java:82)
	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
	at com.relogiclabs.json.schema.types.JValidator.match(JValidator.java:82)
	at com.relogiclabs.json.schema.types.JObject.match(JObject.java:63)
	at com.relogiclabs.json.schema.types.JValidator.match(JValidator.java:76)
	at com.relogiclabs.json.schema.types.JObject.match(JObject.java:63)
	at com.relogiclabs.json.schema.types.JValidator.match(JValidator.java:76)
	at com.relogiclabs.json.schema.types.JRoot.match(JRoot.java:73)
	at com.relogiclabs.json.schema.JsonAssert.isValid(JsonAssert.java:54)
	at org.example.SampleSchema.checkIsValid(SampleSchema.java:61)
	at org.example.Main.main(Main.java:5)
```
For more information about the schema syntax format and library functionalities, please refer to the reference documentation [here](/JsonSchema-Java/api/index.html).