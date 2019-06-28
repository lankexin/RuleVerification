package entity;

import java.util.*;

public class Component {
   
    private Map<String, String> attrs;

    Map<String, State> stateList;
    Map<String, Component> subComponentList;
    Map<String, Propagation> propagationList;
    Map<String, ExceptionXML> exceptionList;
    List<Linkpoint> linkpointList;
    List<Transition> transitionList;
    List<Port> portList;
    List<Connection> connectionList;
    List<Channel> channelList;

    public Component() {
    	attrs = new HashMap<>();
    	stateList = new HashMap<>();
    	subComponentList = new LinkedHashMap<String, Component>();
    	linkpointList = new ArrayList<>();
    	transitionList = new ArrayList<>();
    	portList = new ArrayList<Port>();
    	connectionList = new ArrayList<Connection>();
    	channelList = new ArrayList<Channel>();

    	exceptionList = new HashMap<String, ExceptionXML>();
    	propagationList = new HashMap<String, Propagation>();
    }


    public void setAttr(String key, String value) {
    	attrs.put(key, value);
    }
    
    
    public String getAttr(String key) {
    	return attrs.get(key);
    }


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
    
    public List<Channel> getChannelList() {
        return channelList;
    }
    
    
    public List<Linkpoint> getLinkpointList() {
    	return linkpointList;
    }
    
    
    public List<Connection> getConnectionList() {
    	return connectionList;
    }


    public Map<String, Component> getSubComponentList() {
		return subComponentList;
	}
    
    
    public List<Port> getPortList() {
    	return portList;
	}
    
    
    public void attrsToString() {
    	for (String attrKey : attrs.keySet()) {
    		System.out.println(attrKey + " " + attrs.get(attrKey));
    	}
    }

}
