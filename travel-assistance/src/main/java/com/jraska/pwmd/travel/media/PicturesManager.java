package com.jraska.pwmd.travel.media;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.jraska.common.ArgumentCheck;
import com.jraska.dagger.PerApp;
import com.jraska.pwmd.travel.io.PicturesDir;
import timber.log.Timber;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

@PerApp
public class PicturesManager {
  //region Constants

  public static final String JPG_EXTENSION = ".jpg";

  //endregion

  //region Fields

  private final File _imagesDir;

  //endregion

  //region Constructors

  @Inject
  public PicturesManager(@PicturesDir @NonNull File imagesDir) {
    ArgumentCheck.notNull(imagesDir);

    _imagesDir = imagesDir;
  }

  //endregion

  //region Methods

  public boolean imageExists(UUID id) {
    if (id == null) {
      return false;
    }

    return getImageFile(id).exists();
  }

  public UUID createPictureId() {
    return UUID.randomUUID();
  }

  public Uri createPictureUri(UUID uuid) {
    File imageFile = getImageFile(uuid);
    Uri imageUri = Uri.fromFile(imageFile);

    return imageUri;
  }

  @NonNull
  public File getImageFile(UUID uuid) {
    return new File(_imagesDir, uuid.toString() + JPG_EXTENSION);
  }

  public UUID getIdForUri(@NonNull Uri imageUri) {
    String uriValue = imageUri.toString();

    int lastIndexOfPath = uriValue.lastIndexOf('/');
    int lastIndex = uriValue.length() - JPG_EXTENSION.length();
    String uuidValue = uriValue.substring(lastIndexOfPath + 1, lastIndex);

    return UUID.fromString(uuidValue);
  }

  public boolean deleteImage(UUID imageId) {
    File imageFile = getImageFile(imageId);
    if (imageFile.exists() && imageFile.delete()) {
      Timber.i("Image " + imageId + " successfully deleted");
      return true;
    } else {
      Timber.w("Image " + imageId + " was not deleted ");
      return false;
    }
  }

  //endregion
}
