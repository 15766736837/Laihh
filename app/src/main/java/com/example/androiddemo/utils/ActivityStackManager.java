package com.example.androiddemo.utils;

import android.app.Activity;

import java.util.Stack;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityStackManager {
    private static Stack<AppCompatActivity> activityStack;
    private static ActivityStackManager instance;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        if (instance == null) {
            instance = new ActivityStackManager();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 获取指定的Activity
     */
    public static AppCompatActivity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (AppCompatActivity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    public void onDestory() {
        if (activityStack != null)
            while (!activityStack.empty()) {
                instance.popActivity();
            }
        activityStack = null;
        instance = null;
    }
}
