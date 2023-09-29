/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.annotation.test;

import de.conxult.annotation.processor.ClassHandler;
import de.conxult.annotation.processor.ConxultAnnotationHandler;
import de.conxult.annotation.processor.FieldHandler;
import de.conxult.annotation.processor.MethodHandler;
import java.io.Serializable;
import java.util.HashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 *
 * @author joerg
 */
public class TestAnnotationHandler
    extends ConxultAnnotationHandler {

    @ClassHandler
    public void classHandler(TestAnnotation test, TypeElement element) {
        System.out.println("classHandler " + test.value() + " on " + element.asType());
        JavaClassSource someType = getJavaSourceFactory().createClass("de.hogi.SomeClass");
        someType.setAbstract(true);
        someType.addAnnotation("java.lang.Deprecated");
        someType.extendSuperType(HashMap.class);
        someType.implementInterface(Comparable.class);
        someType.implementInterface(Serializable.class);
    }

    @FieldHandler
    public void fieldHandler(TestAnnotation test, VariableElement element) {
        System.out.println("fieldHandler " + test.value() + " on " + element.asType() + " " + element.getSimpleName());
        JavaClassSource someType = getJavaSourceFactory().createClass("de.hogi.SomeField");
        someType.setAbstract(true);
        someType.addAnnotation("java.lang.Deprecated");
        someType.extendSuperType(HashMap.class);
        someType.implementInterface(Comparable.class);
        someType.implementInterface(Serializable.class);
    }

    @MethodHandler
    public void methodHandler(TestAnnotation test, ExecutableElement element) {
        System.out.println("methodHandler " + test.value() + " on " + element.getReturnType() + " " + element.getSimpleName());
        element.getParameters().forEach((v) ->
            System.out.println("  " + v.asType().getKind() + " " + v.asType() + " " + v.getSimpleName()));
//        System.out.println("methodHandler " + test.value() + " on " + element.getReturnType() + " " + element.getSimpleName() + "(");
        JavaClassSource someType = getJavaSourceFactory().createClass("de.hogi.SomeMethod");
        if (!someType.isAbstract()) {
            someType.setAbstract(true);
            someType.addAnnotation("java.lang.Deprecated");
            someType.extendSuperType(HashMap.class);
            someType.implementInterface(Comparable.class);
            someType.implementInterface(Serializable.class);
        }
    }

    @MethodHandler
    public void methodHandler1(TestAnnotation1 test, ExecutableElement element) {
        System.out.println("methodHandler1 " + test.value() + " on " + element.getReturnType() + " " + element.getSimpleName());
    }

    @MethodHandler
    public void methodHandler2(TestAnnotation2 test, ExecutableElement element) {
        System.out.println("methodHandler2 " + test.value() + " on " + element.getReturnType() + " " + element.getSimpleName());
    }

}
