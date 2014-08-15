package com.mimic.accesrest;

import com.mimic.accesrest.notifications.Notifications;
import com.parse.Parse;
import com.parse.PushService;

public class Application extends android.app.Application {

	  public Application() {
	  }

	  @Override
	  public void onCreate() {
	    super.onCreate();
		 Parse.initialize(this, "MxTgJ6p4hA3BcLzlTdcdtORyQ02SgFqWK96ioEzX", "bFcgmlfdt3AVHFwsGSnvw2AL5IUdxoLead8e3fSl");

	    PushService.setDefaultPushCallback(this, Notifications.class);
	  }
}