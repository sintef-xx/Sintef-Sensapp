package sintef.envision.sensor.dwr;

import java.util.ArrayList;
import java.util.List;

import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.Actions;
import de.ifgi.envision.resources.model.enums.States;

/**
 * Simple Utitily class which renders valid actions for a given resource
 * 
 * @author pajoma
 *
 */
public class ActionLinkBuilder {

	private final Resource resource;

	public ActionLinkBuilder(Resource resource) {
		this.resource = resource;
	}
	
	
	public List<Actions> getActions() {
		int state = Integer.parseInt(this.resource.getState());  
		States st = States.getState(state); 
		
		List<Actions> actions = new ArrayList<Actions>(); 
		
		// default actions, always valid
		actions.add(Actions.UPLOAD_RESOURCE); 
		actions.add(Actions.CREATE_REMOTE_RESOURCE); 
		
		
		if(resource == null) return actions; 
//		if(resource.getType() == Types.WSML_ONTOLOGY)
//		if(resource instanceof WebServiceResource) return getServiceActions(actions, st); 
//		if(resource instanceof OntologyResource) return getOntologyActions(actions, st); 
//		if(resource instanceof WorkflowResource) return getWorkflowActions(actions, st);
		else return  new ArrayList<Actions>(); 
		
	}


	private List<Actions> getServiceActions(List<Actions> actions, States st) {
		switch (st) {
		case LINK_ONLY:
			actions.add(Actions.TRANSLATE_SERVICE);
			actions.add(Actions.DELETE_RESOURCE);
			break;
		case TRANSLATED: 
			actions.add(Actions.ANNOTATE_SERVICE);
			actions.add(Actions.DELETE_RESOURCE);
			break;
		case ANNOTATED_DRAFT: 
			actions.add(Actions.MARK_ANNOTATIONS_FINISHED);
			actions.add(Actions.DELETE_RESOURCE);
			break;			
		case ANNOTATED: 
			actions.add(Actions.PUBLISH_SERVICE);
			actions.add(Actions.ANNOTATE_SERVICE);
			actions.add(Actions.DELETE_RESOURCE);
			break;	
		case PUBLISHED: 
			actions.add(Actions.UNPUBLISH_SERVICE);
			break;				
		default:
			break;
		}
		return actions; 
	}
	
	private List<Actions> getOntologyActions(List<Actions> actions, States st) {
		switch (st) {
		case LINK_ONLY:
			actions.add(Actions.DELETE_RESOURCE);
			break;
		case UPLOADED: 
			actions.add(Actions.DELETE_RESOURCE);
			break;
		case PUBLISHED: 
			actions.add(Actions.UNPUBLISH_SERVICE);
			break;				
		default:
			break;
		}
		return actions; 
	}

	private List<Actions> getWorkflowActions(List<Actions> actions, States st) {
		switch (st) {
		case UPLOADED:
			actions.add(Actions.DELETE_RESOURCE);
			break;
		case TRANSLATED_DRAFT: 
			actions.add(Actions.DELETE_RESOURCE);
			break;
		default:
			return getServiceActions(actions, st);
		}
		return actions; 
	}


}
