package entity;

import java.util.List;

public class Component {
//    Map<String, HashMap<String,String>> componentInfo=new HashMap<>();
    String name;

    String componentType;

    List<State> stateList;

    List<Linkpoint> inputLink;

    List<Linkpoint> outputLink;

    public Component(String name, List<Linkpoint> inputLink, List<Linkpoint> outputLink) {
        this.name = name;
        this.inputLink = inputLink;
        this.outputLink = outputLink;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public List<Linkpoint> getInputLink() {
        return inputLink;
    }

    public void setInputLink(List<Linkpoint> inputLink) {
        this.inputLink = inputLink;
    }

    public List<Linkpoint> getOutputLink() {
        return outputLink;
    }

    public void setOutputLink(List<Linkpoint> outputLink) {
        this.outputLink = outputLink;
    }

    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }

    //    public void setComponentInfo(){
        //componentInfo.put();
//    }
//    public Map<String, HashMap<String,String>> getComponentInfo(String name){
//        return componentInfo;
//    }
}
