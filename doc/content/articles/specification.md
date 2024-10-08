+++
title = 'Specification'
date = 2023-10-08T09:38:53+06:00
weight = 5
+++

<style>
pre code { font-size: 1.1em; }
</style>

# Specification
JSchema is a vocabulary that allows you to describe the structure and constraints of JSON documents. It provides a way to define rules for validating the data in a JSON document. With JSchema, you can ensure that your JSON data follows a specific format and adheres to certain rules.

## Grammar
JSchema grammar is introduced through a notation similar to what is known as the McKeeman form grammar notation, which is a simplified version of the Backus-Naur form and Extended Backus–Naur form. This form minimizes the usage of complex structures of meta-characters, making the grammar highly readable and easy to understand without requiring extensive prior knowledge of grammar syntax. Moreover, inside the grammar whitespace defining rules are ignored to make it clear and concise.

```html
schema
    schema-header-opt defines-opt schema-core defines-opt
    validator

schema-header-opt
    title-opt version-opt imports-opt pragmas-opt

title-opt
    ''
    '%title' ':' string

version-opt
    ''
    '%version' ':' string

imports-opt
    ''
    imports

imports
    import imports

import
    '%import' ':' class-identifier

class-identifier
    identifier
    identifier '.' class-identifier

pragmas-opt
    ''
    pragmas

pragmas
    pragma pragmas

pragma
    '%pragma' identifier ':' primitive

defines-opt
    ''
    defines

defines
    define defines

define
    '%define' alias-name ':' validator-main

alias-name
    '$' identifier

schema-core
    '%schema' ':' validator

validator
    validator-main
    alias-name

validator-main
    value-opt functions-opt datatypes-opt receivers-opt this-opt

value-opt
    ''
    value

value
    primitive
    object
    array

functions-opt
    ''
    functions

functions
    function functions

function
    function-name function-args-opt

function-name
    '@' identifier
    '@' identifier '*'

function-args-opt
    ''
    '(' ')'
    '(' function-args ')'

function-args
    function-arg
    function-arg ',' function-args

function-arg
    value
    receiver

datatypes-opt
    ''
    datatypes

datatypes
    datatype datatypes

datatype
    datatype-name datatype-arg-opt

datatype-name
    '#' alphas
    '#' alphas '*'

alphas
    alpha alphas

datatype-arg-opt
    ''
    '(' alias-name ')'

receivers-opt
    ''
    receivers

receivers
    receiver receivers

receiver
    '&' identifier

this-opt
    ''
    '?'

object
    '{' '}'
    '{' properties '}'

properties
    property
    property ',' properties

property
    string ':' validator

array
    '[' ']'
    '[' elements ']'

elements
    validator
    validator ',' elements

primitive
    string
    number
    undefined
    'true'
    'false'
    'null'

undefined
    '!'

identifier
    alpha
    alpha alpha-numerics

alpha-numerics
    alpha-numeric alpha-numerics

alpha-numeric
    alpha
    '0' . '9'

alpha
    'A' . 'Z'
    'a' . 'z'
    '_'

string
    '"' characters '"'

characters
    ''
    character characters

character
    '0020' . '10FFFF' - '"' - '\'
    '\' escape

escape
    '"'
    '\'
    '/'
    'b'
    'f'
    'n'
    'r'
    't'
    'u' hex hex hex hex

hex
    digit
    'A' . 'F'
    'a' . 'f'

number
    integer
    float
    double

integer
    positive-integer
    negative-integer

float
    integer fraction

fraction
    '.' digits

double
    integer fraction-opt exponent

fraction-opt
    ''
    '.' digits

exponent
    'E' sign-opt digits
    'e' sign-opt digits

sign-opt
    ''
    '+'
    '-'

positive-integer
    digit
    one-to-nine digits

negative-integer
    '-' digit
    '-' one-to-nine digits

digits
    digit
    digit digits

digit
    '0'
    one-to-nine

one-to-nine
    '1' . '9'

```
To explore more about McKeeman form grammar notation and standard JSON document grammar in McKeeman form notation, please visit [this page](https://www.json.org). The resource offers information regarding JSON specification and implementations in different programming languages.