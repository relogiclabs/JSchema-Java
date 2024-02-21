package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.CommonException;
import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.NotFoundClassException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.tree.EFunction;
import com.relogiclabs.jschema.internal.tree.FunctionKey;
import com.relogiclabs.jschema.internal.tree.NativeFunction;
import com.relogiclabs.jschema.internal.tree.ScriptFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JImport;
import com.relogiclabs.jschema.node.JNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.relogiclabs.jschema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.jschema.internal.tree.NativeFunction.getSignature;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.merge;
import static com.relogiclabs.jschema.internal.util.MiscellaneousHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS01;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS02;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS03;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS04;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS05;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS06;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS07;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC01;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC03;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC04;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC05;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public final class FunctionRegistry {
    private final Set<String> imports;
    private final Map<FunctionKey, List<EFunction>> functions;
    private final RuntimeContext runtime;

    public FunctionRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
        this.imports = new HashSet<>();
        this.functions = new HashMap<>();
    }

    public JImport addClass(JImport importNode) {
        addClass(importNode.getClassName(), importNode.getContext());
        return importNode;
    }

    public void addClass(String className, Context context) {
        if(!imports.contains(className)) imports.add(className);
        else throw new DuplicateImportException(formatForSchema(CLAS01,
                concat("Class already imported ", className), context));
        Class<?> providerClass;
        try {
            providerClass = Class.forName(className);
        } catch(ClassNotFoundException ex) {
            throw new NotFoundClassException(formatForSchema(CLAS02,
                    concat("Not found ", className), context));
        }
        var baseClass = FunctionProvider.class;
        // if not FunctionProvider's subclass
        if(!baseClass.isAssignableFrom(providerClass))
            throw new InvalidImportException(formatForSchema(CLAS03, concat(providerClass.getName(),
                    " needs to inherit ", baseClass.getName()), context));
        try {
            merge(functions, extractMethods(providerClass, createInstance(providerClass, context)));
        } catch(InvalidFunctionException ex) {
            throw new InvalidFunctionException(formatForSchema(ex.getCode(), ex.getMessage(), context));
        }
    }

    private static Map<FunctionKey, List<EFunction>> extractMethods(Class<?> providerClass,
                FunctionProvider instance) {
        var baseClass = FunctionProvider.class;
        Map<FunctionKey, List<EFunction>> functions = new HashMap<>();
        for(var m : providerClass.getMethods()) {
            // Methods in super class or in base class
            if(!baseClass.isAssignableFrom(m.getDeclaringClass())
                    || baseClass == m.getDeclaringClass()) continue;
            Parameter[] parameters = m.getParameters();
            if(!isValidReturnType(m.getReturnType())) throw new InvalidFunctionException(FUNC01,
                    concat("Function [", getSignature(m), "] requires valid return type"));
            if(parameters.length == 0 || parameters[0].isVarArgs())
                throw new InvalidFunctionException(FUNC02,
                    concat("Function [", getSignature(m), "] requires valid target parameter"));
            addFunction(new NativeFunction(m, parameters, instance), functions);
        }
        return functions;
    }

    private static void addFunction(EFunction function,
                Map<FunctionKey, List<EFunction>> functions) {
        var functionKey = new FunctionKey(function);
        var functionList = functions.get(functionKey);
        if(functionList == null) functionList = new ArrayList<>();
        functionList.add(function);
        functions.put(functionKey, functionList);
    }

    public void addFunction(ScriptFunction function) {
        addFunction(function, functions);
    }

    private static boolean isValidReturnType(Class<?> type) {
        if(type == boolean.class) return true;
        if(type == Boolean.class) return true;
        if(type == FutureFunction.class) return true;
        return false;
    }

    private FunctionProvider createInstance(Class<?> type, Context context) {
        try {
            var constructor = type.getDeclaredConstructor(RuntimeContext.class);
            return (FunctionProvider) constructor.newInstance(runtime);
        } catch (NoSuchMethodException e) {
            throw failOnCreateInstance(CLAS04, e, type, context);
        } catch (InstantiationException e) {
            throw failOnCreateInstance(CLAS05, e, type, context);
        } catch (InvocationTargetException e) {
            throw failOnCreateInstance(CLAS06, e, type, context);
        } catch (IllegalAccessException e) {
            throw failOnCreateInstance(CLAS07, e, type, context);
        }
    }

    private static CommonException failOnCreateInstance(String code, Exception ex,
                Class<?> type, Context context) {
        return new ClassInstantiationException(formatForSchema(code,
                concat("Fail to create instance of ", type.getName()), context), ex);
    }

    private boolean processResult(Object result) {
        return result instanceof FutureFunction future
            ? runtime.addFuture(future)
            : (boolean) result;
    }

    public boolean invokeFunction(JFunction caller, JNode target) {
        for(var e : caller.getCache()) if(e.isTargetMatch(target))
            return processResult(e.invoke(caller, target));
        Class<?> mismatchTarget = null;

        for(var f : getFunctions(caller)) {
            var schemaArgs = f.prebind(caller.getArguments());
            if(schemaArgs == null) continue;
            var targetType = f.getTargetType();
            if(isMatch(target, targetType)) {
                Object[] allArgs = addTarget(schemaArgs, target).toArray();
                var result = f.invoke(caller, allArgs);
                caller.getCache().add(f, allArgs);
                return processResult(result);
            }
            mismatchTarget = targetType;
        }
        if(mismatchTarget != null)
            return fail(new JsonSchemaException(new ErrorDetail(FUNC03,
                    "Function ", caller.getOutline(), " is incompatible with the target data type"),
                    new ExpectedDetail(caller, "applying to a supported data type such as ",
                            getTypeName(mismatchTarget)),
                    new ActualDetail(target, "applied to an unsupported data type ",
                            getTypeName(target.getClass()), " of ", target.getOutline())));

        return fail(new FunctionNotFoundException(formatForSchema(FUNC04, caller.getOutline(), caller)));
    }

    private List<EFunction> getFunctions(JFunction caller) {
        var list = functions.get(new FunctionKey(caller));
        if(list == null) list = functions.get(new FunctionKey(caller.getName(), -1));
        if(list == null) throw new FunctionNotFoundException(formatForSchema(FUNC05,
                concat("Not found function ", caller.getOutline()), caller));
        return list;
    }

    private static List<Object> addTarget(List<Object> arguments, JNode target) {
        arguments.add(0, getDerived(target));
        return arguments;
    }

    private static boolean isMatch(JNode value, Class<?> type) {
        return type.isInstance(getDerived(value));
    }

    private boolean fail(RuntimeException exception) {
        return runtime.getExceptions().fail(exception);
    }
}