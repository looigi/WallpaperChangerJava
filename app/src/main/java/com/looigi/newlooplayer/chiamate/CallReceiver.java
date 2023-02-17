package com.looigi.newlooplayer.chiamate;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

import java.util.Date;

public class CallReceiver extends GestioneChiamate {
    @Override
    protected void onIncomingCallStarted(String number, Date start) {
		Log.getInstance().ScriveLog("Incoming Call Started: " + number);
    	if (VariabiliGlobali.getInstance().isStaSuonando()) {
			Utility.getInstance().premutoPlay(false);
    	}
    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {
		Log.getInstance().ScriveLog("Outgoing Call Started: " + number);
		if (VariabiliGlobali.getInstance().isStaSuonando()) {
			Utility.getInstance().premutoPlay(false);
		}
    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {
		Log.getInstance().ScriveLog("Incoming Call End: " + number);
		if (VariabiliGlobali.getInstance().isStaSuonando()) {
			Utility.getInstance().premutoPlay(true);
    	}
    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
		Log.getInstance().ScriveLog("Outgoing Call Ended: " + number);
		if (VariabiliGlobali.getInstance().isStaSuonando()) {
			Utility.getInstance().premutoPlay(true);
    	}
    }

    @Override
    protected void onMissedCall(String number, Date start) {
		Log.getInstance().ScriveLog("On Missed Call: " + number);
		if (VariabiliGlobali.getInstance().isStaSuonando()) {
			Utility.getInstance().premutoPlay(true);
    	}
    }
}