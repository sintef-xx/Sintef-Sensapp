package de.ifgi.envision.tests;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.ObjectFactory;

public class JUnitScope implements Scope {
    public String getConversationId() {
        return null;
    }

    public Object get(String name, ObjectFactory<?> objectFactory) {
        return objectFactory.getObject();
    }

    public Object remove(String name) {
        return null;
    }

    public void registerDestructionCallback(String name, Runnable callback) {
    }


	public Object resolveContextualObject(String arg0) {
		return null;
	}
}