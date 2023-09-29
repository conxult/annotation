/*
 * Copyright by https://conxult.de
 */
package de.conxult.annotation.processor.modelX;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

/**
 *
 * @author joerg
 */
public class RoasterTest {

    @Test
    public void createJavaClass() throws Exception {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass
            .setPackage("com.company.example")
            .setName("Person");
        System.out.println("ääää " + javaClass.toString());
    }

    @Test
    public void formatJavaSource() throws Exception {
        String javaCode = "public class MyClass{ private String field;}";
        String formattedCode = Roaster.format(javaCode);
        System.out.println(formattedCode);
    }
}
