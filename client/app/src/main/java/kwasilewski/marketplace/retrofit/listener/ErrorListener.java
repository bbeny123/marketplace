package kwasilewski.marketplace.retrofit.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import kwasilewski.marketplace.activity.NetErrorActivity;

public interface ErrorListener {

    default void unauthorized(Activity activity) {
        unhandledError(activity, "Unhandled unauthorized");
    }

    default void notAcceptable(Activity activity) {
        unhandledError(activity, "Unhandled notAcceptable");
    }

    default void notFound(Activity activity) {
        unhandledError(activity, "Unhandled notFound");
    }

    default void serverError(Activity activity) {
        unhandledError(activity, "Unhandled serverError");
    }

    default void failure(Activity activity) {
        unhandledError(activity, "Unhandled failure");
    }

    default void unhandledError(Activity activity, String error) {
        Log.d("RetrofitListener", error);
        activity.startActivity(new Intent(activity, NetErrorActivity.class));
    }

}
