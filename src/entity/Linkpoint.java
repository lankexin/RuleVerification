package entity;

public class Linkpoint {
    String name;
    String precondition;
    /**
     * 存放一个组件的入接口和出接口的匹配关系
     */
    String postcondition;
    Long period;

    public Linkpoint(String name, String precondition, String postcondition) {
        this.name = name;
        this.precondition = precondition;
        this.postcondition = postcondition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getPostcondition() {
        return postcondition;
    }

    public void setPostcondition(String postcondition) {
        this.postcondition = postcondition;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

}
