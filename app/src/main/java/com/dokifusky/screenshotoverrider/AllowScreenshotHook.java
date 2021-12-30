package com.dokifusky.screenshotoverrider;

import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;

import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

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

        // the only methods that will trigger this hook are addFlags() and setFlags().
        // thus, ok to assume int argument in index zero will be flag.
        if ((int)param.args[0] == WindowManager.LayoutParams.FLAG_SECURE)
        {
            dbgPrintf("Secure flag added. method=\"%s\", args=%s",
                    param.method, Arrays.toString(param.args));
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            dbgPrintf("Cleared secure flag!");
        }
    }
}
