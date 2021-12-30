package com.dokifusky.screenshotoverrider;

import android.view.Window;
import android.view.WindowManager;

import de.robv.android.xposed.XC_MethodHook;

public class AllowScreenshotHook extends XC_MethodHook
{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable
    {
        Window window = (Window) param.thisObject;
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}
