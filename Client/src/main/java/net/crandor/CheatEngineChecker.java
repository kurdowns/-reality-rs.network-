package net.crandor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Stuart on 01/06/2016.
 */
public class CheatEngineChecker implements Runnable {

    @Override
    public void run() {

        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }

        while (true) {
            try {
                String line;
                Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /v");

                try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {

                    while ((line = input.readLine()) != null) {
                        line = line.toLowerCase();
                        //if (line.contains("art*mo*ney") || line.contains("am745")) {
                        //     System.exit(0);
                        //  }

                        if (line.contains("cheatengine") || line.contains("cheat engine")) {
                            System.exit(0);
                        }
                    }
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
