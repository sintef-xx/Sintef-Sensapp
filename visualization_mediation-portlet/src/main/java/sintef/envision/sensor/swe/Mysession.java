package sintef.envision.sensor.swe;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.ScriptSession;

public class Mysession {
	static Map sessionMap= new HashMap(); 
	
	static Map sesMap= new HashMap();
	
	static String remoteUser;
	
	public static void setRemoteUser(String user)
	{
		remoteUser=user;
	}
	
	public static void addSESSubscribe(String resourceId, String sid)
	{
		sesMap.put(resourceId, sid);
		System.out.println("saved resource:"+resourceId+":"+sid);
	}
	
	public static String deleteSESSubscribeBySID(String sid)
	{
		 for   (Object o  : sesMap.keySet()){    
			 String id=(String)sesMap.get(o); 
			 if(id.equals(sid))
			 {
				 deleteSESSubscribe((String)o);
			 }
			 return ""+o;
		 }
		 return null;
	}
	
	public static void deleteSESSubscribe(String resourceId)
	{
		sesMap.remove(resourceId);
		System.out.println("remove subscribe:"+resourceId);
	}
	
	public static ScriptSession getSesstionByResourceId(String resourceId)
	{
		if(sesMap!=null)
		{
			String sid=(String)sesMap.get(resourceId);
			System.out.println("save subscribe:"+resourceId);
			return getSessionById(sid);
		}
		return null;
	}
	
	public static String getRemoteUser()
	{
		return remoteUser;
	}
	
	public static void addMap(String id, ScriptSession s)
	{
		sessionMap.put(id, s);
		System.out.println("saved session:"+id+":"+s.toString());
	}
	
	public static ScriptSession getSessionById(String id)
	{
		if(sessionMap!=null)
		{
			return (ScriptSession)sessionMap.get(id);
		}
		return null;
	}
	
	public static void deleteSessionById(String id)
	{
		if(sessionMap!=null)
		{
			sessionMap.remove(id);
			System.out.println("delete session:"+id);
		}
	}
}
