package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.GeneralExtension;
import com.relogiclabs.jschema.extension.ScriptFunction;
import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.script.GBoolean;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.library.CommonMethods;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EBoolean;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EValue;

import static com.relogiclabs.jschema.type.EType.ANY;
import static com.relogiclabs.jschema.type.EType.COMPOSITE;
import static com.relogiclabs.jschema.type.EType.NUMBER;
import static java.util.Objects.requireNonNull;

// GeneralExtension support all types of extensions
public class GeneralExtension2 extends GeneralExtension {
    @ConstraintFunction
    public static boolean checkPrime(EInteger number) {
        var num = number.getValue();
        var sqrt = Math.sqrt(num);
        for (int i = 2; i <= sqrt; i++) {
            if(num % i == 0) return false;
        }
        return true;
    }

    @ScriptFunction
    public static ENumber maxNum(ENumber value, ENumber... values) {
        var max = value;
        for(var v : values) if(max.toDouble() < v.toDouble()) max = v;
        return max;
    }

    @ScriptMethod(ANY)
    public EValue getTypeName() {
        var common = requireNonNull(CommonMethods.getInstance(this));
        return GString.from(common.type().getValue().substring(1));
    }

    @ScriptMethod(NUMBER)
    public EBoolean isEvenCheck() {
        var tolerance = getRuntime().getPragmas().getFloatingPointTolerance();
        return GBoolean.from(Math.abs(getSelf(ENumber.class).toDouble() % 2) < tolerance);
    }

    @ScriptMethod(COMPOSITE)
    public EBoolean isEmptyCheck() {
        if(getSelf() instanceof EArray a) return GBoolean.from(a.size() == 0);
        if(getSelf() instanceof EObject o) return GBoolean.from(o.size() == 0);
        throw new IllegalStateException("Invalid instance type");
    }
}