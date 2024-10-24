# JSchema - The Customizable JSON Schema
A JSON Schema is crucial for making communication, interoperability, validation, testing, documentation, and specification seamless. All of this combined contributes to better maintenance and evolution of data-driven applications and systems. If you are interested in a comprehensive overview of the roles and uses of JSON Schema in modern web applications, check out our in-depth post [here](https://www.relogiclabs.com/2023/01/the-roles-of-json-schema.html).

## Design Goals
The traditional standard JSON Schema rigorously follows the conventional JSON structure, which unfortunately comes at the expense of simplicity, conciseness, and readability. Our goal is to develop a new JSON Schema that promotes these essential aspects that were previously missing.

This new schema is simple, lucid, easy to grasp, and doesn't require much prior knowledge to get started. Additionally, its simplicity and conciseness allow us and machines to read-write more efficiently. Moreover, a large set of constraint data types and functions within the core schema promotes the precise definition of JSON documents, significantly reducing the potential for communication gaps among collaborators. Furthermore, its inherent extensibility not only facilitates the test automation process in API testing but also simplifies the integrations of new constraints and functionalities to meet the diverse requirements of modern web services.

## Basic Example
Let's explore an example of our schema for a typical JSON API response containing information about a user profile or account. The schema is very self-explanatory and thus almost no prior knowledge is required to understand the schema and the JSON responses specified by this schema.
```cpp
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
```
In the above example, two types of constraints are used: constraint functions (also referred to as validation functions, such as `@range(1, 10000)`) and constraint data types (also referred to as validation data types, such as `#integer`). All constraint functions begin with the `@` symbol, while all constraint data types start with `#`. C-style comments are also supported within the schema. In this example, `address` can be `null` (like an optional input for users) and if it is `null` then no constraints of `address` are applicable. The following JSON is one of the examples that will be successfully validated against the above schema. To start your journey with the JSON validation library, please consult the documentation available [here](https://relogiclabs.github.io/JSchema-Java/articles/introduction).
```json
{
    "user": {
        "id": 1234,
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
```
## Extended Example
The next example represents an expanded version of the previous one, which brings more complexity. To effectively construct such schemas with multiple layers of nested structures, including custom validation functions, it's beneficial to have a fundamental understanding of this schema syntax. While the syntax may seem difficult at first, it becomes straightforward once you have a basic understanding of it. For more detailed information, reference documentation is available [here](https://relogiclabs.github.io/JSchema-Java/articles/introduction).
```cpp
%title: "Extended User Profile Dashboard API Response"
%version: "2.0.0-extended"
%import: com.relogiclabs.jschema.test.extension.GeneralExtension1

%pragma DateDataTypeFormat: "DD-MM-YYYY"
%pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
%pragma IgnoreUndefinedProperties: true

%define $post: {
    "id": @range(1, 1000) #integer,
    "title": @length(10, 100) #string,
    "content": @length(30, 1000) #string,
    "tags": $tags
} #object

%define $product: {
    "id": @length(2, 10) @regex("[a-z][a-z0-9]+") #string,
    "name": @length(5, 30) #string,
    "brand": @length(5, 30) #string,
    "price": @range(0.1, 1000000),
    "inStock": #boolean,
    "specs": {
        "cpu": @length(5, 30) #string,
        "ram": @regex("[0-9]{1,2}GB") #string,
        "storage": @regex("[0-9]{1,4}GB (SSD|HDD)") #string
    } #object #null
}

%define $tags: @length(1, 10) #string*($tag) #array
%define $tag: @length(3, 20) @regex("[A-Za-z_]+") #string

%schema:
{
    "user": {
        "id": @range(1, 10000) #integer,
        /*username does not allow special characters*/
        "username": @regex("[a-z_]{3,30}") #string,
        "role": @enum("user", "admin") #string &role,
        "isActive": #boolean, //user account current status
        "registeredAt": @after("01-01-2010 00:00:00") #time,
        "dataAccess": @checkAccess(&role) #integer,
        "ipAddress": @checkIPAddress #string,
        "profile": {
            "firstName": @regex("[A-Za-z]{3,50}") #string,
            "lastName": @regex("[A-Za-z]{3,50}") #string,
            "dateOfBirth": @before("01-01-2006") #date,
            "age": @range(18, 128) #integer,
            "email": @email #string,
            "pictureURL": @url #string,
            "address": {
                "street": @length(10, 200) #string,
                "city": @length(3, 50) #string,
                "country": @regex("[A-Za-z ]{3,50}") #string
            } #object #null,
            "hobbies": !?
        },
        "posts": @length(0, 1000) #object*($post) #array,
        "preferences": {
            "theme": @enum("light", "dark") #string,
            "fontSize": @range(9, 24) #integer,
            "autoSave": #boolean
        }
    },
    "products": #object*($product) #array,
    "weather": {
        "temperature": @range(-50, 60) #integer #float,
        "isCloudy": #boolean
    }
}

%script: {
    constraint function checkAccess(role) {
        // Auto-unpacking unwraps single-value '&role' array into its value
        // 'target' keyword refers to the target JSON value
        if(role == "user" && target > 5) return fail(
            "ERRACCESS01", "Data access incompatible with 'user' role",
            expected("an access at most 5 for 'user' role"),
            actual("found access " + target + " that is greater than 5"));
    }
}
```
The subsequent JSON sample is an illustrative example that successfully validates against the expanded schema mentioned earlier. Within this example, recurring JSON structures appear that can be validated by defining components or nested functions and data types. Besides, reusing simple component definitions, you can achieve a clear and concise schema when validating large JSON with repetitive structures. This improves the overall readability and maintainability of the schema.
```json
{
    "user": {
        "id": 1111,
        "username": "johndoe",
        "role": "admin",
        "isActive": true,
        "registeredAt": "06-09-2023 15:10:30",
        "dataAccess": 10,
        "ipAddress": "127.0.0.1",
        "profile": {
            "firstName": "John",
            "lastName": "Doe",
            "dateOfBirth": "17-06-1993",
            "age": 30,
            "email": "john.doe@example.com",
            "pictureURL": "https://example.com/picture.jpg",
            "address": {
                "street": "123 Some St",
                "city": "Some town",
                "country": "Some Country"
            }
        },
        "posts": [
            {
                "id": 1,
                "title": "Introduction to JSON",
                "content": "JSON (JavaScript Object Notation) is a lightweight data interchange format...",
                "tags": [
                    "JSON",
                    "tutorial",
                    "data"
                ]
            },
            {
                "id": 2,
                "title": "Working with JSON in Java",
                "content": "Java provides great support for working with JSON...",
                "tags": [
                    "Java",
                    "JSON",
                    "tutorial"
                ]
            },
            {
                "id": 3,
                "title": "Introduction to JSON Schema",
                "content": "A JSON schema defines the structure and data types of JSON objects...",
                "tags": [
                    "Schema",
                    "JSON",
                    "tutorial"
                ]
            }
        ],
        "preferences": {
            "theme": "dark",
            "fontSize": 14,
            "autoSave": true
        }
    },
    "products": [
        {
            "id": "p1",
            "name": "Smartphone",
            "brand": "TechGiant",
            "price": 1.99,
            "inStock": true,
            "specs": null
        },
        {
            "id": "p2",
            "name": "Laptop",
            "brand": "SuperTech",
            "price": 11.99,
            "inStock": false,
            "specs": {
                "cpu": "Ryzen",
                "ram": "11GB",
                "storage": "11GB SSD"
            }
        }
    ],
    "weather": {
        "temperature": 25.5,
        "isCloudy": true,
        "conditions": null
    }
}
```
For more information about the schema syntax format and library functionalities, please refer to the reference documentation [here](https://relogiclabs.github.io/JSchema-Java/api/index.html).