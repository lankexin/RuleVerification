package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Component {
    //Map<String, HashMap<String,String>> componentInfo=new HashMap<>();
   
    private Map<String, String> attrs;

    Map<String, State> stateList;
    Map<String, Component> componentList;
    Map<String, Propagation> propagationList;
    Map<String, ExceptionXML> exceptionList;
    List<Linkpoint> linkpointList;
    List<Transition> transitionList;
    
    public Component() {
    	attrs = new HashMap<>();
    	stateList = new HashMap<>();
    	linkpointList = new ArrayList<>();
    	transitionList = new ArrayList<>();
    	exceptionList = new HashMap<String, ExceptionXML>();
    	propagationList = new HashMap<String, Propagation>();
    }


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getComponentType() {
//        return componentType;
//    }
//
//    public void setComponentType(String componentType) {
//        this.componentType = componentType;
//    }
    
    public void setAttr(String key, String value) {
    	attrs.put(key, value);
    }
    
    public String getAttr(String key) {
    	return attrs.get(key);
    }

//    public List<Linkpoint> getInputLink() {
//        return inputLink;
//    }
//
//    public void setInputLink(List<Linkpoint> inputLink) {
//        this.inputLink = inputLink;
//    }
//
//    public List<Linkpoint> getOutputLink() {
//        return outputLink;
//    }
//
//    public void setOutputLink(List<Linkpoint> outputLink) {
//        this.outputLink = outputLink;
//    }

    public Map<String, State> getStateList() {
        return stateList;
    }
    
    public Map<String, ExceptionXML> getExceptionList() {
        return exceptionList;
    }
    
    public Map<String, Propagation> getPropagationList() {
		return propagationList;
	}

    public void setStateList(Map<String, State> stateList) {
        this.stateList = stateList;
    }
    
    public List<Transition> getTransitionList() {
        return transitionList;
    }
    
    public List<Linkpoint> getLinkpointList() {
    	return linkpointList;
    }

    //    public void setComponentInfo(){
        //componentInfo.put();
//    }
//    public Map<String, HashMap<String,String>> getComponentInfo(String name){
//        return componentInfo;
//    }
    
    public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }
}
