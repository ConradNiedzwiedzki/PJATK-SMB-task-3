package pl.pjatk.kn_miniprojekt1.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static pl.pjatk.kn_miniprojekt1.data.GeofenceTransitionsIntentService.enqueueWork;

public class GeofenceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent i)
    {
        enqueueWork(context,i);
    }
}
