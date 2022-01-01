package com.dokifusky.screenshotoverrider;

import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;

import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class FlagDisablerHook extends XC_MethodHook
{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
        if ((int)param.args[0] == WindowManager.LayoutParams.FLAG_SECURE)
        {
            param.args[0] = 0;
            dbgPrintf("FlagDisablerHook: Intercepted setFlags(%d, ...) call",
                    param.args[0]);
        }
    }
}
