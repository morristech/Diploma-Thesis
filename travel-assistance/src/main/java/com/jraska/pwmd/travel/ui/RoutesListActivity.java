package com.jraska.pwmd.travel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.jraska.common.events.Observer;
import com.jraska.pwmd.core.gps.Position;
import com.jraska.pwmd.travel.R;
import com.jraska.pwmd.travel.TravelAssistanceApp;
import com.jraska.pwmd.travel.data.Path;
import com.jraska.pwmd.travel.data.RouteData;
import com.jraska.pwmd.travel.data.RouteDescription;
import com.jraska.pwmd.travel.persistence.TravelDataRepository;
import com.jraska.pwmd.travel.tracking.TrackingManager;

import javax.inject.Inject;
import java.util.*;

public class RoutesListActivity extends BaseActivity {
  //region Fields

  @Bind(R.id.routes_recycler_view) RecyclerView _routesRecycler;
  @Bind(R.id.routes_empty_view) View _emptyView;

  @Inject TrackingManager _trackingManager;
  @Inject TravelDataRepository _travelDataRepository;
  @Inject RoutesAdapter _routesAdapter;

  private Observer<RouteDescription> _descriptionsObserver = new Observer<RouteDescription>() {
    @Override
    public void update(Object sender, RouteDescription args) {
      _routesAdapter.add(args);
    }
  };

  //endregion

  //region Activity overrides

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);

    TravelAssistanceApp.getComponent(this).inject(this);

    setupRoutes();
    refreshRoutes();
    registerOnRouteChangedObservers();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.routes, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_routes_help:
        openHelpRequests();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onDestroy() {
    unregisterOnRouteChangeObservers();

    super.onDestroy();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }

  //endregion

  //region Methods

  protected void setupRoutes() {
    _routesRecycler.setAdapter(_routesAdapter);
    _routesRecycler.setLayoutManager(new LinearLayoutManager(this));

    _routesAdapter.setItemClickListener(new RoutesAdapter.OnItemClickListener() {
      @Override public void onItemClick(int position, View itemView) {
        showRoute(position);
      }
    });
    _routesAdapter.setItemDeleteClickListener(new RoutesAdapter.OnItemDeleteListener() {
      @Override public void onItemDelete(int position, View itemView) {
        deleteRoute(_routesAdapter.getItem(position));
      }
    });
  }

  protected void showRoute(int position) {
    RouteDescription item = _routesAdapter.getItem(position);
    showRoute(item);
  }

  protected void showRoute(RouteDescription route) {
    Intent intent = new Intent(this, RouteDisplayActivity.class);
    intent.putExtra(RouteDisplayActivity.ROUTE_ID, route.getId());

    startActivity(intent);
  }

  void registerOnRouteChangedObservers() {
    _travelDataRepository.getOnNewRoute().registerObserver(_descriptionsObserver);
  }

  void unregisterOnRouteChangeObservers() {
    _travelDataRepository.getOnNewRoute().registerObserver(_descriptionsObserver);
  }

  void refreshRoutes() {
    TravelDataRepository service = _travelDataRepository;
    List<RouteDescription> routeDescriptions = service.selectAllRouteDescriptions();

    _routesAdapter.clear();

    _routesAdapter.addAll(routeDescriptions);
    _routesAdapter.notifyDataSetChanged();

    updateEmptyView();
  }

  private void updateEmptyView() {
    if (_routesAdapter.getItemCount() == 0) {
      _routesRecycler.setVisibility(View.GONE);
      _emptyView.setVisibility(View.VISIBLE);
    } else {
      _routesRecycler.setVisibility(View.VISIBLE);
      _emptyView.setVisibility(View.GONE);
    }
  }

  protected void deleteRoute(RouteDescription route) {
    _travelDataRepository.deleteRoute(route.getId());
    refreshRoutes();
  }

  protected void openHelpRequests() {
    startActivity(new Intent(RoutesListActivity.this, HelpRequestSendActivity.class));
  }


  @OnClick(R.id.btnStartTracking) void startTracking() {
    _trackingManager.startTracking();
  }

  @OnClick(R.id.btnStopTracking) void stopTracking() {
    _trackingManager.stopTracking();
  }

  @OnClick(R.id.btnSaveRoute) void saveRoute() {
    TrackingManager.PathInfo lastPath = _trackingManager.getLastPath();
    if (lastPath == null) {
      Toast.makeText(this, getString(R.string.noRouteToSave), Toast.LENGTH_SHORT).show();
      return;
    }

    RouteData routeData = new RouteData(new RouteDescription(UUID.randomUUID(), lastPath.getStart(), lastPath.getEnd(), "Test"), lastPath.getPath());

    _travelDataRepository.insertRoute(routeData);

    Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();

    refreshRoutes();
  }



  //endregion
}
