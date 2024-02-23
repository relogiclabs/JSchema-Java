package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class ScriptLiteralTests {
    @Test
    public void When_ObjectCreateAddUpdate_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value": @objectTest
            }
            %script: {
                constraint objectTest() {
                    var test = null;
                    var obj = { "k1": 1, k3: { 
                        k2: 20.5 + 4.2 * 3.5 + 8.7 - 5.0, 
                        k3: test 
                    } };
                    obj["k2"] = "test";
                    obj.k4 = 100.5;
                    obj.k1 = 100;
                    obj.k3.k1 = 10;
                    // Mixed property access check
                    if(obj["k3"].k2 != 38.9) return fail("Invalid: " + obj);
                    if(obj != target) return fail("Invalid: " + obj);
                }
            }
            """;
        var json =
            """
            {
                "value": { 
                    "k1": 100, 
                    "k2": "test", 
                    "k3": {
                        "k1": 10,
                        "k2": 38.9,
                        "k3": null
                    },
                    "k4": 100.5
                }
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ArrayCreateAddUpdateWithObject_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value": @objectTest
            }
            %script: {
                constraint objectTest() {
                    var result = 30;
                    var obj = { k1: 1, k2: 20, "k3": { 
                        k2: [ 10, 30.9, 20.5 + 4.2 * 3.5 + 8.7 - 5.0,
                            { k4 : result }
                        ]
                    } };
                    obj["k1"] = "test";
                    obj.k3.k2[4] = [10, 20];
                    if(obj.k3.k2 != target) return fail("Invalid: " + obj);
                    if(obj.k3.k2[2] != 38.9) return fail("Invalid: " + obj);
                    if(obj.k3.k2[4] != [10, 20]) return fail("Invalid: " + obj);
                    obj.k2 = 100;
                    if(obj.k2 != 100) return fail("Invalid: " + obj);
                }
            }
            """;
        var json =
            """
            {
                "value": [10, 30.9, 38.9, {"k4": 30}, [10, 20]]
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ArrayCreateAddUpdate_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value": @arrayTest
            }
            %script: {
                constraint arrayTest() {
                    var test = null;
                    var arr = [1, 20, [100.5, 1E-3, test], 50, test, undefined];
                    arr[0] = { k10: 10E2 };
                    arr[6] = 34.5;
                    if(arr[2] != target) return fail("Invalid: " + arr);
                    if(arr[0] != { k10: 10E2 }) return fail("Invalid: " + arr);
                }
            }
            """;
        var json =
            """
            {
                "value": [100.5, 1E-3, null]
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_HandleUndefinedFromSchema_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(10, !) #integer,
                "value2": @checkRange(!, 1000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min != undefined && target < min) return fail(
                        "RANGEERR01", "The value is less than minimum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                    if(max != undefined && target > max) return fail(
                        "RANGEERR02", "The value is greater than maximum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": 10,
                "value2": 1000
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_HandleNullFromSchema_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(10, null) #integer,
                "value2": @checkRange(null, 10000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min != null && target < min) return fail(
                        "RANGEERR01", "The value is less than minimum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                    if(max != null && target > max) return fail(
                        "RANGEERR02", "The value is greater than maximum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": 90,
                "value2": 500
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_HandleNullOrUndefinedFromSchema_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(0, 100) #integer,
                "value2": @checkRange(200, 1000) #integer,
                "value3": @checkRange(20, !) #integer,
                "value4": @checkRange(null, 1000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min && target < min) return fail(
                        "RANGEERR01", "The value is less than minimum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                    if(max && target > max) return fail(
                        "RANGEERR02", "The value is greater than maximum",
                        expected("a value in range " + [min, max]),
                        actual("but found " + target + " which out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": 90,
                "value2": 500,
                "value3": 20,
                "value4": 1000
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ApplyRangeAndIteratorOnString_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value": @stringTest
            }
            %script: {
                constraint stringTest() {
                    if(target[-4..] != "text") return fail("Invalid: " + target[-4..]);
                    if(target[0..] != "This is a text") return fail("Invalid: " + target[0..]);
                    if(target[2..7] != "is is") return fail("Invalid: " + target[2..7]);
                    if(target[5..-5] != "is a") return fail("Invalid: " + target[5..-5]);
                    if(target[..-5] != "This is a") return fail("Invalid: " + target[..-5]);
                    var string = "";
                    foreach(var c in target) string = string + c;
                    if(target != string) return fail("Invalid: " + string);
                }
            }
            """;
        var json =
            """
            {
                "value": "This is a text"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ApplyRangeAndIteratorOnArray_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value": @arrayTest
            }
            %script: {
                constraint arrayTest() {
                    if(target[3..] != [4, 5, 6, 7]) return fail("Invalid: " + target[3..]);
                    if(target[3..6] != [4, 5, 6]) return fail("Invalid: " + target[3..6]);
                    if(target[-5..-2] != [3, 4, 5]) return fail("Invalid: " + target[-5..-2]);
                    if(target[..4] != [1, 2, 3, 4]) return fail("Invalid: " + target[..4]);
                    var array = [], index = 0;
                    foreach(var e in target) array[index++] = e;
                    if(target != array) return fail("Invalid: " + array);
                }
            }
            """;
        var json =
            """
            {
                "value": [1, 2, 3, 4, 5, 6, 7]
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}