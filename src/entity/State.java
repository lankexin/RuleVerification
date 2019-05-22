package entity;

import java.util.List;

public class State {
    private String name;
    private List<Fault> faultList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Fault> getFaultList() {
        return faultList;
    }

    public void setFaultList(List<Fault> faultList) {
        this.faultList = faultList;
    }
}
