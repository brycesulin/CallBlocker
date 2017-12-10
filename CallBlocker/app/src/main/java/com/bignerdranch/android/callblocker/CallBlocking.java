package com.bignerdranch.android.callblocker;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

/**
 * Created by brycesulin
 */

public class CallBlocking extends BroadcastReceiver {
    MainActivity main = new MainActivity();

    private String number;

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

        // Block All Calls
        else if (main.radioValue == 1)
        {
            disconnectPhoneItelephony(context);
        }
        // Block calls from numbers not found in Contacts
        else if (main.radioValue == 2)
        {
            if (main.contactExists(context, number) == false) {
                disconnectPhoneItelephony(context);
            }
        }
        // Block calls from numbers found in Black List
        else if (main.radioValue == 3)
        {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(MainActivity.blockList.contains(new Blocklist(number)))
            {
                disconnectPhoneItelephony(context);
                return;
            }

        }
        // Cancel all Call Blocking
        else if (main.radioValue == 4)
        {

        }

        // Block calls from numbers on the blocklist, by default, if radioValues won't reference their values
        else
        {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(MainActivity.blockList.contains(new Blocklist(number)))
            {
                disconnectPhoneItelephony(context);
                return;
            }
        }
    }
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}