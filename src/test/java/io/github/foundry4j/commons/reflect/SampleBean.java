package io.github.foundry4j.commons.reflect;

public class SampleBean {

    private String name;
    private int age;
    public static final String CONSTANT = "hello";

    public SampleBean() {
    }

    private SampleBean(String name) {
        this.name = name;
    }

    public SampleBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String secretMethod() {
        return "secret";
    }

    public static String staticMethod(String input) {
        return "static:" + input;
    }

    public String concat(String a, String b) {
        return a + b;
    }
}