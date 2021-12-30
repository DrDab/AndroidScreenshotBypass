package com.dokifusky.screenshotoverrider;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class AllowScreenshotModule implements IXposedHookLoadPackage
{
    public static final String WINDOW_CLASSNAME = "android.view.Window";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        XSharedPreferences pref =
                new XSharedPreferences("com.dokifusky.screenshotoverrider",
                        "enabledApps");
        pref.makeWorldReadable();
        pref.reload();

        String packageName = lpparam.packageName;

        if (pref.contains(packageName))
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