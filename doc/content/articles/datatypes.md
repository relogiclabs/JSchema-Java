+++
title = 'Data Types'
date = 2023-12-04T09:38:53+06:00
weight = 6
+++

<style>
pre code { font-size: 1.1em; }
</style>

# Constraint Data Types
Data types play a pivotal role in validating JSON data for compliance with the schema. Essentially, data types determine the kind of data that a JSON element or value can contain. This mechanism serves as a fundamental process in maintaining the accuracy, consistency, and integrity of JSON document and its structure throughout the system, where data quality and reliability are vital.

In the schema document, data types are denoted by the `#` prefix. Here is an outline of all data types, including their subtypes, used in the schema document to validate a JSON document. When using multiple data types for validation, it indicates that the JSON value is considered valid if it complies with any of the specified alternative data types. All of these data types and their subtypes offer the flexibility of selecting the most appropriate type based on requirements.

```html
#any
 ┬
 ├ #primitive
 │  ┬
 │  ├ #string
 │  │  ┬
 │  │  └ #datetime
 │  │     ┬
 │  │     ├ #date
 │  │     └ #time
 │  ├ #number
 │  │  ┬
 │  │  ├ #integer
 │  │  ├ #float
 │  │  └ #double  
 │  ├ #boolean
 │  └ #null  
 └ #composite
    ┬
    ├ #array
    └ #object
```

### The Any Data Type
This data type accepts any valid JSON value that conforms to the JSON standard. It is the least restrictive data type and serves as the parent type for all other data types defined in this schema, each of which imposes more specific constraints. Here is the [specification](https://www.json.org) of JSON document containing rules of all valid JSON values. Following is the syntax for specifying this data type:
```html
#any
```

### The Primitive Data Type
This serves as the foundational data type for all non-composite or primitive JSON values. While it is not intended for direct use in a schema, it organizes common functionalities across other sub-data types. Nevertheless, it remains valid for use, as illustrated by the following syntax:
```html
#primitive
```

### The Composite Data Type
As the parent data type for two kinds of composite JSON values, namely arrays and objects, this composite type consists of zero or more JSON values. It is not designed to be used in a schema but rather the organization of shared functionalities among other sub-data types. Despite this, it is still valid for use, demonstrated by the following syntax:
```html
#composite
```

### The Array Data Type
This data type represents the JSON array type and accepts any JSON array specified by the JSON standard. The specification document for JSON provides details about the various syntax and forms of JSON arrays. Below is the syntax for specifying this data type:
```html
#array
```

### The Object Data Type
This data type represents the JSON object type and accepts any JSON object specified by the JSON standard. The specification document for JSON provides details about the different syntax and forms of JSON objects. Following is the syntax for specifying this data type:
```html
#object
```

### The String Data Type
This is one of the most commonly used data types in a JSON document, designed to accept any JSON string as specified by the JSON standard. The syntax for specifying this data type is as follows:
```html
#string
```

### The Date Time Data Type
The date-time data type serves as the parent data type for both date and time types. It is a subtype of JSON string type and thus formatted as per the JSON string specification. It is not intended to be used directly in a schema, but it provides common functionality for both date and time types.
```html
#datetime
```

### The Date Data Type
The date data type accepts a string representation of a date, conforming to the ISO 8601 standard (date part only). This is the default configuration, which can be modified using the directive described [here](/JSchema-Java/articles/directives). It is a subtype of date-time type and thus also formatted as per the JSON string specification. Detailed explanations of the ISO 8601 standard can be found in this [document](https://www.iso.org/iso-8601-date-and-time-format.html). Furthermore, you can refer to this [document](/JSchema-Java/articles/datetime) for a detailed description of the date pattern associated with this data type. To define this data type in schema, use the following syntax:
```html
#date
```

### The Time Data Type
The time data type accepts a string representation of a time (including both date and time parts), in accordance with the ISO 8601 standard. This default configuration can be modified using the directive described [here](/JSchema-Java/articles/directives). Similar to the date data type, it is a subtype of date-time data type and thus also formatted as per the JSON string specification. Here is the ISO 8601 standard [document](https://www.iso.org/iso-8601-date-and-time-format.html), which contains detailed explanations. Furthermore, you can refer to this [document](/JSchema-Java/articles/datetime) for a detailed description of the date-time pattern associated with this data type. To define this data type in schema, use the following syntax:
```html
#time
```

### The Number Data Type
The number data type serves as the parent data type for all numeric types accommodated by the schema, including integer, floating-point, and exponent-based numbers. It accepts any JSON numerical value in accordance with the JSON standard. The syntax for specifying the number type is as follows:
```html
#number
```

### The Integer Data Type
The integer data type is a subtype of the number data type that only allows integral numbers or whole numbers without any fraction and exponent and thus provides constraints for a wide range of real-world scenarios where numbers cannot involve decimal points or exponents. To specify the integer type in schema, use the following syntax:
```html
#integer
```

### The Float Data Type
The float data type is also a subtype of the number data type that only accepts floating point numbers and does not allow exponent in numbers or integral numbers. This constraint is useful for various real-world applications that require numbers to be exclusively in floating-point format. To specify the float type in schema, use the following syntax:
```html
#float
```

### The Double Data Type
The double data type, as a subtype of the number data type, exclusively accepts numbers with exponents. It can either be an integral number with an exponent or a floating-point number with an exponent. This constraint distinguishes it from other number formats and makes it particularly useful for handling large numbers with exponents. To specify the double type in a schema, use the following syntax:
```html
#double
```

### The Boolean Data Type
The boolean data type is a binary or switch-based data type that only accepts two values, namely `true` and `false`. It is particularly useful in situations where toggling and switching are necessary. To specify the boolean type in the schema, use the following syntax:
```html
#boolean
```

### The Null Data Type
The null data type serves as a special constraint within JSchema documents, facilitating the controlled use of `null` in place of other JSON elements or values. Typically, it is combined with other data types to permit the use of `null` for specific JSON elements or values. This can set constraints for scenarios in which an array without any elements and an object without any properties can either have `null` or only be allowed to be empty. 

Additionally, the `@nonempty` constraint functions can be employed to further control the use of empty values within a JSON document. To specify the null type in the schema, use the following syntax:
```html
#null
```