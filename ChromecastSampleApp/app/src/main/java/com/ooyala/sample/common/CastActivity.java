package com.ooyala.sample.common;

import android.os.Bundle;

import com.ooyala.android.OoyalaNotification;
import com.ooyala.android.OoyalaPlayer;
import com.ooyala.cast.CastManager;
import com.ooyala.cast.MediaRouterManager;

import java.util.Observable;

import androidx.annotation.Nullable;

public abstract class CastActivity extends PlayerActivity {
  private boolean wasInCastMode;
  protected CastManager castManager;
  protected MediaRouterManager mediaRouterManager;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    castManager = CastManager.getCastManager();
    mediaRouterManager = MediaRouterManager.getMediaRouterManager();
  }

  @Override
  protected void completePlayerSetup() {
    super.completePlayerSetup();
    castManager.registerWithOoyalaPlayer(player);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (player != null && !player.isInCastMode() && wasInCastMode) {
      castManager.hideCastView();
      wasInCastMode = false;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (castManager != null) {
      castManager.destroy();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (castManager != null && player != null) {
      castManager.registerWithOoyalaPlayer(player);
      mediaRouterManager.registerToOoyalaPlayer(player);
      mediaRouterManager.addMediaRouterCallback();
      mediaRouterManager.sendCurrentCastMediaRoutesToPlayer();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (castManager != null) {
      castManager.deregisterFromOoyalaPlayer();
      mediaRouterManager.deregisterFromOoyalaPlayer(player);
      mediaRouterManager.removeMediaRouterCallback();
    }
  }

  @Override
  public void update(Observable arg0, Object argN) {
    super.update(arg0, argN);

    final String arg1 = OoyalaNotification.getNameOrUnknown(argN);

    OoyalaNotification notification = null;
    if (argN instanceof OoyalaNotification) {
      notification = (OoyalaNotification) argN;
    }

    if (arg1 == OoyalaPlayer.CURRENT_ITEM_CHANGED_NOTIFICATION_NAME) {
      updateCastView(notification);
    }

    if (arg1 == OoyalaPlayer.STATE_CHANGED_NOTIFICATION_NAME) {
      if (player.isInCastMode()) {
        if (!wasInCastMode) {
          wasInCastMode = true;
        }
        updateCastViewState(player.getState());
      } else if (wasInCastMode) {
        wasInCastMode = false;
      }
    }
  }

  protected abstract void updateCastView(OoyalaNotification notification);

  protected abstract void updateCastViewState(OoyalaPlayer.State state);
}
