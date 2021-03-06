package org.jbehave.core.steps;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.model.ExamplesTable;

public class SomeSteps extends Steps {

    public SomeSteps() {
    }

    public Object args;

    public void aMethod() {
    }

    public void aFailingMethod() {
        throw new RuntimeException();
    }

    public void aPendingMethod() {
        throw new PendingStepFound("a step");
    }

    public void aMethodWith(String args) {
        this.args = args;
    }

    public void aMethodWith(double args) {
        this.args = args;
    }

    public void aMethodWith(long args) {
        this.args = args;
    }

    public void aMethodWith(int args) {
        this.args = args;
    }

    public void aMethodWith(float args) {
        this.args = args;
    }

    public void aMethodWithListOfStrings(List<String> args) {
        this.args = args;
    }

    public void aMethodWithListOfLongs(List<Long> args) {
        this.args = args;
    }

    public void aMethodWithListOfIntegers(List<Integer> args) {
        this.args = args;
    }

    public void aMethodWithListOfDoubles(List<Double> args) {
        this.args = args;
    }

    public void aMethodWithListOfFloats(List<Float> args) {
        this.args = args;
    }
    
    public void aMethodWithListOfNumbers(List<Number> args) {
        this.args = args;
    }
    
    public void aMethodWithSetOfStrings(Set<String> args) {
        this.args = args;
    }

    public void aMethodWithSetOfNumbers(Set<Number> args) {
        this.args = args;
    }

    public void aMethodWithDate(Date args) {
        this.args = args;
    }

    public void aMethodWithExamplesTable(ExamplesTable args) {
        this.args = args;
    }

    public ExamplesTable aMethodReturningExamplesTable(String value){
    	return new ExamplesTable(value);
    }

    public ExamplesTable aFailingMethodReturningExamplesTable(String value){
        throw new RuntimeException(value);
    }

    public static Method methodFor(String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(SomeSteps.class);
        for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
            if (md.getMethod().getName().equals(methodName)) {
                return md.getMethod();
            }
        }
        return null;
    }

}
