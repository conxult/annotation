/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.annotation.processor;

import de.conxult.log.Log;
import de.conxult.util.MetaInfServices;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.jboss.forge.roaster.model.JavaType;

/**
 *
 * @author joerg
 *
 */
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class ConxultAnnotationProcessor
    extends AbstractProcessor {

    Log log = Log.instance(ConxultAnnotationProcessor.class);

    List<ConxultAnnotationHandler> handlers          = new ArrayList<>();

    Set<Class>                     annotations       = new HashSet<>();

    JavaSourceFactory              javaSourceFactory = new JavaSourceFactory();

    public ConxultAnnotationProcessor() {
        loadAnnotationHandlers();
        annotations.forEach(a -> log.info("processing @"+a.getName()));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return annotations.stream().map(Class::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (ConxultAnnotationHandler handler : handlers) {
            for (Class annotationType : handler.getAnnotations()) {
                for (Element element : (Set<Element>) roundEnv.getElementsAnnotatedWith(annotationType)) {
                    Annotation annotation = element.getAnnotation(annotationType);
                    if (element instanceof TypeElement typeElement) {
                        handler.processClassHandlers(annotationType, annotation, typeElement);
                    }
                }
                for (Element element : (Set<Element>) roundEnv.getElementsAnnotatedWith(annotationType)) {
                    Annotation annotation = element.getAnnotation(annotationType);
                    if (element instanceof VariableElement variableElement) {
                        handler.processFieldHandlers(annotationType, annotation, variableElement);
                    }
                }
                for (Element element : (Set<Element>) roundEnv.getElementsAnnotatedWith(annotationType)) {
                    Annotation annotation = element.getAnnotation(annotationType);
                    if (element instanceof ExecutableElement executableElement) {
                        handler.processMethodHandlers(annotationType, annotation, executableElement);
                    }
                }
            }
        }

        if (javaSourceFactory.getSources().isEmpty()) {
            return false;
        }

        javaSourceFactory.getJavaSources().forEach(this::writeJavaType);
        javaSourceFactory.getSources().clear();

        return true;
    }

    protected void writeJavaType(JavaType javaType) {
        try {
            log.info("generate " + javaType.getQualifiedName());
            OutputStream outputStream = processingEnv.getFiler().createSourceFile(javaType.getQualifiedName()).openOutputStream();
            PrintWriter outputWriter = new PrintWriter(outputStream, true);

            outputWriter.print(javaType.toString());
            outputWriter.close();
        } catch (IOException ioException) {
            log.warn(ioException, "createClassFile(" + javaType.getQualifiedName() + ") failed! ");
        }
    }

    void loadAnnotationHandlers() {
        for (Class<? extends ConxultAnnotationHandler> annotationHandlerClass : MetaInfServices.locateClasses(ConxultAnnotationHandler.class)) {
            try {
                ConxultAnnotationHandler annotationHandler = ((Class<? extends ConxultAnnotationHandler>)annotationHandlerClass).getConstructor().newInstance();
                handlers.add(annotationHandler.setJavaSourceFactory(javaSourceFactory));
                annotations.addAll(annotationHandler.annotations);
            } catch (Exception any) {
                log.error(any, "loadAnnotationHandlers failed!");
            }
        }
    }

}
