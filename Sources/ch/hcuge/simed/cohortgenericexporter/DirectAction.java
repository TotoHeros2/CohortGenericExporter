package ch.hcuge.simed.cohortgenericexporter;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WORequest;

import er.extensions.appserver.ERXDirectAction;

import ch.hcuge.simed.cohortgenericexporter.component.Main;

public class DirectAction extends ERXDirectAction {
	public DirectAction(WORequest request) {
		super(request);
	}

	@Override
	public WOActionResults defaultAction() {
		return pageWithName(Main.class.getName());
	}
	
	public Application application() {
		return (Application)WOApplication.application();
	}
	
	@Override
	public Session session() {
		return (Session)super.session();
	}
}
