package com.looigi.newlooplayer;

import java.io.File;
import java.io.IOException;

public class PuliziaGenerale {
    public void Pulizia() {
        String Path = VariabiliGlobali.getInstance().getPercorsoDIR();

        // Eliminazione schifezze che si creano da sole tipo la cartella HTTP:
        File dir = new File(Path + "/http:");
        if (dir.exists()) {
            String deleteCmd = "rm -r " + Path + "/http:";
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {

            }
        }

    }
}
