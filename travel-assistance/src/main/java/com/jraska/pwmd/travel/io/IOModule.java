package com.jraska.pwmd.travel.io;

import android.content.Context;
import android.os.Environment;
import com.jraska.dagger.PerApp;
import dagger.Module;
import dagger.Provides;

import java.io.File;

@Module
public class IOModule {
  @Provides @PerApp @CacheDir File cacheDir(Context context) {
    return context.getExternalCacheDir();
  }

  @PerApp @Provides @PicturesDir
  public File providePicturesDir(Context context) {
    return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
  }

  @PerApp @Provides @SoundsDir
  public File provideSoundDir(Context context) {
    return context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES);
  }
}
