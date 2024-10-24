+++
title = 'Directives'
date = 2023-12-04T09:38:53+06:00
weight = 4
+++

<style>
pre code { font-size: 1.1em; }
</style>

# Validation Directives
Directives serve as special instructions or commands for the JSchema parsers, interpreters, and validators. They are used to control various aspects of the validation process or to provide metadata for documentation. Additionally, they offer crucial information about schema and JSON, as well as essential customization of the validation procedure to meet specific requirements.

## Title Directive
Within a schema, the title directive is used to provide a name, label, or a brief intent of the schema for which it is written. Besides, the title directive is optional and additional description can be supplied as multiple comments inside the schema document to provide more detail.

However, this directive is only used for documentation purposes and does not have any impact in the validation process. To represent the title directive, the following example of notation can be used:
```js
%title: "Example name or brief description"
```

## Version Directive
In a schema, the version directive is used to provide a version label of the schema document which helps to keep track of updates. Although optional, the version directive is useful for documentation purposes and does not affect the validation process. The version directive can be represented using the following notation example:
```js
%version: "2.0.0-beta1"
```

## Import Directive
Import directive enables the addition or inclusion of a class, as defined by object-oriented programming, to a schema along with a set of methods that have specific signatures for performing custom validations. This feature extends the built-in validation capabilities of the schema. The example below illustrates how to utilize the import directive in Java language:
```js
%import: com.relogiclabs.jschema.test.extension.GeneralExtension1
```

## Pragma Directive
Pragma directives provide a mechanism to modify the default settings for certain predefined parameters of the validation process. This allows you to adjust the behaviours of schema validation process as per your requirements.

### Ignore Undefined Properties
The `IgnoreUndefinedProperties` pragma directive serves the purpose of instructing the validation process on how to handle JSON properties that are not explicitly defined in the schema, not even by the undefined marker or symbol. You can use this directive to specify whether such properties should be ignored during validation or if validation errors should be raised for any undefined properties in the JSON document.

The default value of this directive is `false`, which means that by default, undefined properties in the JSON document are not ignored, and validation errors will be raised for them. For example, the following usage of this directive instructs the validation process to ignore any undefined properties in the JSON document:
```js
%pragma IgnoreUndefinedProperties: true
```

### Date Data Type Format
The `DateDataTypeFormat` pragma directive enables you to customize the default format of the `#date` data type. By default, the `#date` data type follows the ISO 8601 standard, precisely using the format `YYYY-MM-DD`. Additional details on date-time patterns and formats are available [here](/JSchema-Java/articles/datetime). The subsequent example illustrates the process of defining a customized date format for the `#date` data type:
```js
%pragma DateDataTypeFormat: "DD-MM-YYYY"
```

### Time Data Type Format
To customize the default format of the `#time` data type, utilize the `TimeDataTypeFormat` pragma directive. By default, the `#time` data type follows the ISO 8601 standard, precisely in the format `YYYY-MM-DD'T'hh:mm:ss.FZZ`. Further information on date-time patterns and formats can be found [here](/JSchema-Java/articles/datetime). The following example demonstrates how to specify a customized time format for the `#time` data type:
```js
%pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
```

### Floating Point Tolerance
The pragma directive `FloatingPointTolerance` allows you to define the tolerance level for relative errors in floating-point numbers during calculations and computations carried out by the validation process. By default, this directive is set to `1E-10`, indicating a small tolerance. However, you have the flexibility to adjust this value to any desired number. To specify a custom tolerance value of `1E-07`, you can use the following notation as an example:
```js
%pragma FloatingPointTolerance: 1E-07
```

### Ignore Object Property Order
The `IgnoreObjectPropertyOrder` pragma directive provides a means to enforce a specific order or sequence of JSON object properties, following the schema definition. This requirement for strict ordering is only needed in certain special cases. By default, this directive is set to `true`, meaning that the object property order outlined in the schema document is not mandatory. However, you can override this default by setting it to `false`, as shown in the following example below:
```js
%pragma IgnoreObjectPropertyOrder: false
```

### Enable Contextual Exception
The `EnableContextualException` pragma directive enables an additional type of exception. These exceptions provide supplementary contextual information about the primary exceptions, showing the errors that occurred during the validation process. The default value of this directive is `false`, meaning no contextual exceptions are generated.
```js
%pragma EnableContextualException: true
```

### Outline Maximum Length
The pragma directive `OutlineMaximumLength` specifies the maximum length of tree outlines generated when string representations of large Schema and JSON trees or subtrees are needed. The default maximum length is `200` characters. If the tree or subtree string exceeds this limit, an outline is shown instead of the full string representation. Currently, a basic abbreviation rule is used to create the tree outlines.
```js
%pragma OutlineMaximumLength: 500
```

## Definition / Define Directive
This feature in JSchema allows you to define a name for a schema component or fragment, which can be referenced from various parts of your schema. This means that if you encounter similar validation requirements in different sections of your schema, you can conveniently refer to the named schema component instead of duplicating the same validation rules. For more information about the schema component syntax and format, please refer to the documentation [here](/JSchema-Java/articles/components). Here is a simple example of how to use this directive:
```js
%define $product: {
    "id": @length(2, 10) @regex("[a-z][a-z0-9]+") #string,
    "name": @length(5, 100) #string,
    "price": @range(0.1, 1000000),
    "inStock": #boolean
}
```

## Schema Directive
The schema directive serves as the starting or entry point for both the schema document and the schema validation process. It becomes mandatory when other directives are present within the document. In such cases, the schema directive explicitly designates the beginning of the schema document and defines the entry point for validation process.

However, if there are no other directives used in the document, the entire document itself is automatically considered as the schema document, with the document's beginning serving as its entry point. To illustrate, here is a simple example of a schema document with schema directive:
```js
%schema:
{
    "user": {
        "id": @range(1, 10000) #integer,
        /*username does not allow special characters*/
        "username": @regex("[a-z_]{3,30}") #string,
        /*currently only one role is allowed by system*/
        "role": "user" #string,
        "isActive": #boolean, //user account current status
        "registeredAt": #time
    }
}
```

## Script Directive
The script directive enables the inclusion of CScript code into a JSchema document, providing the flexibility to define custom validation functions directly within the schema document. These validation functions can accept any number of nodes across the JSON tree as parameters, enabling comprehensive processing and validation to ensure the data integrity of the entire JSON tree. For more information about CScript syntax and format, please refer to the documentation [here](/JSchema-Java/articles/cscript). The following is a simple example of how to use this directive:
```js
%script: {
    constraint function checkAccess(role) {
        if(role[0] == "user" && target > 5) return fail(
            "EX_ERRACCESS01", "Data access incompatible with 'user' role",
            expected("an access at most 5 for 'user' role"),
            actual("found access " + target + " which is greater than 5"));
    }
}
```