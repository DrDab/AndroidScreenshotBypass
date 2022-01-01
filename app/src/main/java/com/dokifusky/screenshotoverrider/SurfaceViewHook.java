package com.dokifusky.screenshotoverrider;

import de.robv.android.xposed.XC_MethodHook;

import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class SurfaceViewHook extends XC_MethodHook
{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
        if (param.args[0].equals(true))
        {
            param.args[0] = false;
            dbgPrintf("SurfaceViewHook: Intercepted setSecure(true) call");
        }
    }
}
