package com.looigi.newlooplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPulizia {
    private static final LogPulizia ourInstance = new LogPulizia();

    public static LogPulizia getInstance() {
        return ourInstance;
    }

    private LogPulizia() {
        File dir = new File(VariabiliGlobali.getInstance().getPercorsoDIR());
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void EliminaFileLog() {
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "LogPulizia.txt");
    }

    public void ScriveLog(String RigaDebug) {
        if (VariabiliGlobali.getInstance().isAzionaDebug()) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
            String strDate = formatter.format(date);

            String Scritta = strDate + ": " + RigaDebug + '\n';
            Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "LogPulizia.txt", Scritta);
        }
    }
}
