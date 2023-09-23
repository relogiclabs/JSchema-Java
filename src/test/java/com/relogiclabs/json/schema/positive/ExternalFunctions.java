package com.relogiclabs.json.schema.positive;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JBoolean;
import com.relogiclabs.json.schema.types.JNumber;
import com.relogiclabs.json.schema.types.JString;

public class ExternalFunctions extends FunctionBase {
    public static final String EVENFUNC01 = "EVENFUNC01";

    public ExternalFunctions(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean even(JNumber target) {
        boolean result = (target.toDouble() % 2 == 0);
        if(!result) failWith(new JsonSchemaException(
                new ErrorDetail(EVENFUNC01, "Number is not even"),
                new ExpectedDetail(target, "even number"),
                new ActualDetail(target, "number ", target, " is odd")));
        return true;
    }

    public boolean canTest(JNumber target, JString str1, JBoolean bool1, JNumber... args) {
        return true;
    }
}