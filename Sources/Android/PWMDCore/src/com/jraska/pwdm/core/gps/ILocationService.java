package com.jraska.pwdm.core.gps;

import com.jraska.common.events.IObservable;
import com.jraska.core.JRApplication;
import com.jraska.core.services.IAppService;

public interface ILocationService extends IAppService
{
	//region Events

	IObservable<Position> getNewPosition();

	//endregion

	//region Properties

	Position getLastPosition();
	boolean isTracking();
	boolean isTrackingAvailable();

	//endregion

	//region Methods

	void startTracking(LocationSettings settings);
	void stopTracking();

	//endregion

	//region Nested classes

	static class Stub
	{
		public static ILocationService asInterface()
		{
			return JRApplication.getService(ILocationService.class);
		}
	}

	//endregion
}
