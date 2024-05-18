parser grammar JsonParser;

options { tokenVocab = JsonLexer; }

json
    : valueNode EOF
    ;

valueNode
    : primitiveNode
    | objectNode
    | arrayNode
    ;

objectNode
    : LBRACE ( propertyNode ( COMMA propertyNode )* )? RBRACE
    ;

propertyNode
    : STRING COLON valueNode
    ;

arrayNode
    : LBRACKET ( valueNode ( COMMA valueNode )* )? RBRACKET
    ;

primitiveNode
    : TRUE              # TrueNode
    | FALSE             # FalseNode
    | STRING            # StringNode
    | INTEGER           # IntegerNode
    | FLOAT             # FloatNode
    | DOUBLE            # DoubleNode
    | NULL              # NullNode
    ;