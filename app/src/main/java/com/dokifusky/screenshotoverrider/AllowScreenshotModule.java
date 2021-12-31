package com.dokifusky.screenshotoverrider;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.dokifusky.screenshotoverrider.AppInfo.APP_PKG_NAME;
import static com.dokifusky.screenshotoverrider.AppInfo.PREFS_FILE;
import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class AllowScreenshotModule implements IXposedHookLoadPackage
{
    public static final String WINDOW_CLASSNAME = "android.view.Window";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        XSharedPreferences pref = new XSharedPreferences(APP_PKG_NAME, PREFS_FILE);
        pref.makeWorldReadable();
        pref.reload();

        String packageName = lpparam.packageName;

        if (pref.getBoolean(packageName, false))
        {
            dbgPrintf("%s Detected, hooking...", packageName);
            addHooksToApp(lpparam);
            dbgPrintf("Hooked %s!", packageName);
        }
    }

    private void addHooksToApp(XC_LoadPackage.LoadPackageParam lpparam)
    {
        AllowScreenshotHook hook = new AllowScreenshotHook();

        XposedHelpers.findAndHookMethod(WINDOW_CLASSNAME, lpparam.classLoader, "addFlags",
                int.class, hook);

        XposedHelpers.findAndHookMethod(WINDOW_CLASSNAME, lpparam.classLoader, "setFlags",
                int.class, int.class, hook);
    }
}