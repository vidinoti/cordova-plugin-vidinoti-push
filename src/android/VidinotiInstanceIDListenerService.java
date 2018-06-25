package com.vidinoti.cordova;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vidinoti.android.vdarsdk.VDARSDKController;

/**
 * Service required for the push notifications.
 * It receives the update of the Firebase token that needs to be forwarded to the Vidinoti SDK.
 */
public class VidinotiInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Forward the token to the Vidinoti SDK
        VDARSDKController controller = VDARSDKController.getInstance();
        if (controller != null) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            controller.updatePushNotificationToken(refreshedToken);
        }
    }

}