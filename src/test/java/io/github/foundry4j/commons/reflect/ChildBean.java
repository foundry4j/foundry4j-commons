package io.github.foundry4j.commons.reflect;

public class ChildBean extends SampleBean {

    private String extra;

    public ChildBean() {
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String childOnly() {
        return "child";
    }
}