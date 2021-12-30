package com.dokifusky.screenshotoverrider;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class MyModule implements IXposedHookLoadPackage
{
    public static final String WINDOW_CLASSNAME = "android.view.Window";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        if (lpparam.packageName.equals("im.howlr.app"))
        {
            dbgPrintf("Howlr Detected, hooking...");
            addHooksToApp(lpparam);
            dbgPrintf("Hooked!");
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