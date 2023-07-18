/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.conxult.annotation.processor.modelX;

import de.conxult.annotation.processor.JavaSourceFactory;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.jupiter.api.Test;

/**
 *
 * @author joerg
 */
public class JavaSourceGeneratorTest {

  JavaSourceFactory     jsf = new JavaSourceFactory();

  @Test
  public void shouldGenerateJavaClass() throws Exception {
    JavaClassSource javaClass = jsf.createClass("de.conxult.annotation.test.TestClass");
  }

}
