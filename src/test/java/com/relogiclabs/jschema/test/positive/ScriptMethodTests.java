package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class ScriptMethodTests {
    @Test
    public void When_StringToNumberConversionInObject_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "number1": @numberTest(100, "#integer"),
                "number2": @numberTest(-10, "#integer"),
                "number3": @numberTest(10, "#integer"),
                "number4": @numberTest(200.8, "#double"),
                "number5": @numberTest(-2.15928E3, "#double")
            }
            %script: {
                constraint numberTest(value, type) {
                    var num = target.number();
                    if(num != value) throw("Invalid: " + target);
                    if(num.type() != type) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "number1": "100",
                "number2": "-10",
                "number3": "+10",
                "number4": "200.8",
                "number5": "-2.15928E3"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DifferentArrayMethodsInObject_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "max1": @arrayMaxTest(100),
                "max2": @arrayMaxTest(99.99),
                "min1": @arrayMinTest(8),
                "min2": @arrayMinTest(2.24),
                "sum1" : @arraySumTest(376, "#integer"),
                "sum2" : @arraySumTest(298.84, "#double"),
                "sort1" : @arraySortTest1(),
                "sort2" : @arraySortTest2(),
                "pushPop" : @arrayPushPopTest(),
                "reverse" : @arrayReverseTest()
            }
            %script: {
                constraint arrayMaxTest(value) {
                    var num = target.max();
                    if(num != value) throw("Invalid: " + target);
                }

                constraint arrayMinTest(value) {
                    var num = target.min();
                    if(num != value) throw("Invalid: " + target);
                }

                constraint arraySumTest(total, type) {
                    var sum = target.sum();
                    if(sum != total) throw("Invalid: " + sum);
                    if(sum.type() != type) throw("Invalid: " + target);
                }

                constraint arraySortTest1() {
                    // target array is readonly and not modifiable
                    var sorted = [2.24, 9, 18.86, 32.04, 37, 39.8, 68, 91.9];
                    if(target.copy().sort() != sorted) throw("Invalid: " + target);
                }

                constraint arraySortTest2() {
                    var sorted = ["c", "e", "g", "i", "l", "o", "r"];
                    if(target.copy().sort() != sorted) throw("Invalid: " + target);
                }

                constraint arrayPushPopTest() {
                    // target array is readonly and not modifiable
                    var array = target.copy();
                    if(target != array) throw("Invalid: " + target);
                    var element = array.pop();
                    if(element != 144) throw("Invalid: " + target);
                    array.push(144, 233);
                    if(array[array.length() - 1] != 233) throw("Invalid: " + target);
                }

                constraint arrayReverseTest() {
                    var array = ["Test", 89, 55, 34, 21, 13, 8, 5, 3, 2, 1, 1];
                    if(array.reverse() != target) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "max1": [75, 49, 100, 40, 16, 74, 8, 64],
                "max2": [91.9, 68, 99.99, 37, 39.8, 9, 32.04, 2.24],
                "min1": [75, 49, 13, 40, 16, 74, 8, 64],
                "min2": [91.9, 68, 18.86, 37, 39.8, 9, 32.04, 2.24],
                "sum1": [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144],
                "sum2": [91.9, 68, 18.86, 37, 39.8, 9, 32.04, 2.24],
                "sort1": [91.9, 68, 18.86, 37, 39.8, 9, 32.04, 2.24],
                "sort2": ["r", "e", "l", "o", "g", "i", "c"],
                "pushPop" : [1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144],
                "reverse" : [1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, "Test"]
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DifferentStringMethodsInObject_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "startsWith": @startsWithTest(),
                "endsWith": @endsWithTest(),
                "upper": @upperTest(),
                "lower": @lowerTest(),
                "trim" : @trimTest()
            }
            %script: {
                constraint startsWithTest() {
                    var result = target.startsWith("https://");
                    if(!result) throw("Invalid: " + target);
                }

                constraint endsWithTest() {
                    var result = target.endsWith(".org");
                    if(!result) throw("Invalid: " + target);
                }

                constraint upperTest() {
                    var result = target.upper();
                    if(result != "THIS IS A TEST") throw("Invalid: " + target);
                }

                constraint lowerTest() {
                    var result = target.lower();
                    if(result != "this is a test") throw("Invalid: " + target);
                }

                constraint trimTest() {
                    var result = target.trim();
                    if(result != "test") throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "startsWith": "https://github.com",
                "endsWith": "https://www.mozilla.org",
                "upper": "This is a test",
                "lower": "This is a test",
                "trim": "   test   "
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}