/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.annotation.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joerg
 */

@TestAnnotation("Class")
public class TestClass {

  @TestAnnotation("Field")
  public int someVariableInt = 4711;

  @TestAnnotation("Constructor()")
  public TestClass() {
  }

  @TestAnnotation("Method(Date)")
  public String someTest(Date someDate) throws Exception {
    return "Hallo";
  }

  @TestAnnotation("Method()")
  public String someMethod(Map<String, Date> someDates, int i, List<String> strings) throws Exception {
    return "Hallo";
  }

  @TestAnnotation1("Method1")
  public String someMethod1() throws Exception {
    return "Hallo";
  }

  @TestAnnotation2("Method2")
  public String someMethod2() throws Exception {
    return "Hallo";
  }

}
