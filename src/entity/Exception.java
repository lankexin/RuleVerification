package entity;

import java.util.ArrayList;
import java.util.List;

public class Exception {
    String attrs;
    public Exception() { attrs = ""; }
    public void setAttr(String name) {
        attrs=name;
    }
    public String getAttr() {
        return attrs;
    }
}
