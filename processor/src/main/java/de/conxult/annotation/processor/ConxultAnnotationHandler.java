/*
 * Copyright by https://conxult.de
 */
package de.conxult.annotation.processor;

import de.conxult.log.Log;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

/**
 *
 * @author joerg
 */
@Accessors(chain = true)
public class ConxultAnnotationHandler {

    protected final Log log = Log.instance(ConxultAnnotationHandler.class);

    List<Class>              annotations    = new ArrayList<>();
    Map<Class, List<Method>> classHandlers  = new HashMap<>();
    Map<Class, List<Method>> fieldHandlers  = new HashMap<>();
    Map<Class, List<Method>> methodHandlers = new HashMap<>();

    // set from processor
    @Getter @Setter
    JavaSourceFactory javaSourceFactory;

    public ConxultAnnotationHandler() {
        for (var method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ClassHandler.class) && parametersMatch(method.getParameterTypes(), null, TypeElement.class)) {
                Class<?> annotation = method.getParameterTypes()[0];
                if (!annotations.contains(annotation)) {
                    annotations.add(annotation);
                }
                classHandlers.computeIfAbsent(annotation, a -> new ArrayList<>()).add(method);
            } else if (method.isAnnotationPresent(FieldHandler.class) && parametersMatch(method.getParameterTypes(), null, VariableElement.class)) {
                Class<?> annotation = method.getParameterTypes()[0];
                if (!annotations.contains(annotation)) {
                    annotations.add(annotation);
                }
                fieldHandlers.computeIfAbsent(annotation, a -> new ArrayList<>()).add(method);
            } else if (method.isAnnotationPresent(MethodHandler.class) && parametersMatch(method.getParameterTypes(), null, ExecutableElement.class)) {
                Class<?> annotation = method.getParameterTypes()[0];
                if (!annotations.contains(annotation)) {
                    annotations.add(annotation);
                }
                methodHandlers.computeIfAbsent(annotation, a -> new ArrayList<>()).add(method);
            }
        }
        log.info(getClass().getName());
        log.info("  annotations   : " + annotations);
        log.info("  classHandlers : #" + classHandlers.size());
        log.info("  fieldHandlers : #" + fieldHandlers.size());
        log.info("  methodHandlers: #" + methodHandlers.size());
    }

    public List<Class> getAnnotations() {
        return annotations;
    }

    int processClassHandlers(Class annotationType, Annotation annotation, TypeElement element) {
        return processHandlers("class", classHandlers.get(annotationType), annotation, element);
    }

    int processFieldHandlers(Class annotationType, Annotation annotation, VariableElement element ) {
        return processHandlers("field", fieldHandlers.get(annotationType), annotation, element);
    }

    int processMethodHandlers(Class annotationType, Annotation annotation, ExecutableElement element) {
        return processHandlers("method", methodHandlers.get(annotationType), annotation, element);
    }

    int processHandlers(String label, List<java.lang.reflect.Method> methods, Annotation annotation, Object element) {
        int handlersCalled = 0;
        if (methods != null) {
            for (var method : methods) {
                try {
                    method.invoke(this, annotation, element);
                    handlersCalled++;
                } catch (Exception exception) {
                    exception.printStackTrace(System.err);
                    log.error(exception, "processHandlers(" + label + ") ! " + exception);
                }
            }
        }
        return handlersCalled;
    }

    boolean parametersMatch(Class[] methodTypes, Class... argumentTypes) {
        if (methodTypes.length != argumentTypes.length) {
            return false;
        }

        for (int i = 0; (i < methodTypes.length); i++) {
            if (argumentTypes[i] != null && !methodTypes[i].isAssignableFrom(argumentTypes[i])) {
                return false;
            }
        }
        return true;
    }

    protected JavaClassSource createClass(String className) {
        return javaSourceFactory.createClass(className);
    }

    protected JavaInterfaceSource createInterface(String className) {
        return javaSourceFactory.createInterface(className);
    }

    protected JavaEnumSource createEnum(String className) {
        return javaSourceFactory.createEnum(className);
    }

}
