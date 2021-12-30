package com.dokifusky.screenshotoverrider;

import android.util.Log;

public class DebugUtil
{
    public static void dbgPrintf(String msg, Object... args)
    {
        Log.d(AppInfo.APP_NAME, String.format(msg, args));
    }
}
