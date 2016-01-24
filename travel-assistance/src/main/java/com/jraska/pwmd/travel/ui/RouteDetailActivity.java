package com.jraska.pwmd.travel.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.jraska.pwmd.travel.R;
import com.jraska.pwmd.travel.TravelAssistanceApp;
import com.jraska.pwmd.travel.data.NoteSpec;
import com.jraska.pwmd.travel.data.RouteData;
import com.jraska.pwmd.travel.media.SoundsManager;
import com.jraska.pwmd.travel.persistence.TravelDataRepository;

import javax.inject.Inject;

public class RouteDetailActivity extends BaseActivity implements RouteDisplayFragment.EventListener {
  //region Constants

  public static final String ROUTE_ID = "RouteId";

  //endregion

  //region Fields

  private RouteDisplayFragment _routeDisplayFragment;

  @Inject TravelDataRepository _travelDataRepository;
  @Inject SoundsManager _soundsManager;

  private long _routeId;

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_route_detail);
    TravelAssistanceApp.getComponent(this).inject(this);

    _routeDisplayFragment = (RouteDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    _routeId = getIntent().getLongExtra(ROUTE_ID, 0);

    showRoute();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_route_detail, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_route_navigate:
        startNavigation();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  //endregion

  //region RouteDisplayFragment.EventListener impl

  @Override
  public boolean onNoteSpecClicked(NoteSpec noteSpec) {
    if (noteSpec.getSoundId() != null) {
      _soundsManager.play(noteSpec.getSoundId());
    }

    return false;
  }


  //endregion

  //region Methods

  public void showRoute() {
    if (!_routeDisplayFragment.isRouteDisplayed()) {
      RouteData routeData = loadRoute();

      _routeDisplayFragment.displayRoute(routeData);
    }
  }

  protected RouteData loadRoute() {
    long routeId = getIntent().getLongExtra(ROUTE_ID, 0);
    RouteData routeData = _travelDataRepository.select(routeId);
    return routeData;
  }

  protected void startNavigation() {
    NavigationActivity.startNavigationActivity(this, _routeId);
  }

  //endregion
}

