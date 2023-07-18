/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.annotation.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 *
 * @author joergh
 */
public class JavaSourceFactory {

    Map<String, JavaType<?>> sources   = new HashMap<>();

    public JavaClassSource createClass(Class clazz) {
        return createClass(clazz.getName());
    }

    public JavaClassSource createClass(String fullQualifiedName) {
        return (JavaClassSource)sources.computeIfAbsent(fullQualifiedName, (fqn) -> createSource(JavaClassSource.class, fullQualifiedName));
    }

    public JavaInterfaceSource createInterface(Class clazz) {
        return createInterface(clazz.getName());
    }

    public JavaInterfaceSource createInterface(String fullQualifiedName) {
        return (JavaInterfaceSource)sources.computeIfAbsent(fullQualifiedName, (fqn) -> createSource(JavaInterfaceSource.class, fullQualifiedName));
    }

    public JavaEnumSource createEnum(Class clazz) {
        return createEnum(clazz.getName());
    }

    public JavaEnumSource createEnum(String fullQualifiedName) {
        return (JavaEnumSource)sources.computeIfAbsent(fullQualifiedName, (fqn) -> createSource(JavaEnumSource.class, fullQualifiedName));
    }

    public Map<String, JavaType<?>> getSources() {
        return sources;
    }

    public Collection<JavaType<?>> getJavaSources() {
        return sources.values();
    }

    public <T extends JavaSource<?>> T createSource(Class<T> javaSourceType, String fullQualifiedName) {
        int lastDot = fullQualifiedName.lastIndexOf('.');

        Roaster.create(JavaClassSource.class);

        T javaSource = Roaster.create(javaSourceType);

        return (lastDot == -1)
            ? (T)javaSource
                .setName(fullQualifiedName)
            : (T)javaSource
                .setPackage(fullQualifiedName.substring(0, lastDot))
                .setName(fullQualifiedName.substring(lastDot + 1));
    }


//    Map<JavaType, JavaClass> classes = new HashMap<JavaType, JavaClass>();
//
//    public JavaClass createClass(JavaType javaType, JavaClassType type) {
//        JavaClass javaClass = classes.get(javaType);
//        if (javaClass == null) {
//            javaClass = new JavaClass(javaType).setType(type);
//            classes.put(javaType, javaClass);
//        }
//
//        return javaClass;
//    }
//
//    public JavaClass createClass(JavaType javaType) {
//        return createClass(javaType, JavaClassType.CLASS);
//    }
//
//    public JavaClass createInterface(JavaType javaType) {
//        return createClass(javaType, JavaClassType.INTERFACE);
//    }
//
//    public JavaClass createEnum(JavaType javaType) {
//        return createClass(javaType, JavaClassType.ENUM);
//    }
//
//    public JavaClass createAnnotation(JavaType javaType) {
//        return createClass(javaType, JavaClassType.ANNOTATION);
//    }
//
//    public Collection<JavaClass> getJavaClasses() {
//        return classes.values();
//    }

}
