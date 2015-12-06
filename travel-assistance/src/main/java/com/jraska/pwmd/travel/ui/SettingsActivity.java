package com.jraska.pwmd.travel.ui;

import android.os.Bundle;
import android.widget.EditText;
import butterknife.Bind;
import com.jraska.pwmd.travel.R;
import com.jraska.pwmd.travel.TravelAssistanceApp;
import com.jraska.pwmd.travel.settings.SettingsManager;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

  //region Fields

  @Bind(R.id.settings_assistant_email) EditText _assistantEmailText;

  @Inject SettingsManager _settingsManager;

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    TravelAssistanceApp.getComponent(this).inject(this);

    _assistantEmailText.setText(_settingsManager.getAssistantEmail());
  }

  @Override
  protected void onDestroy() {
    _settingsManager.setAssistantEmail(_assistantEmailText.getText().toString());

    super.onDestroy();
  }

  //endregion
}
