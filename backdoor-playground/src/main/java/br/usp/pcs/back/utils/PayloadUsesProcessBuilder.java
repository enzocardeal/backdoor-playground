package br.usp.pcs.back.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// This payload use ProcessBuilder to call
// the "ls" command. It can be
// modified to call some harmful command
public class PayloadUsesProcessBuilder {
    PayloadUsesProcessBuilder(){}
    public static void run(){
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            ProcessBuilder builder = new ProcessBuilder();

            if (isWindows) {
                builder.command("cmd.exe", "/c", "dir");
            } else {
                builder.command("sh", "-c", "ls");
            }

            builder.directory(new File(System.getProperty("user.home")));
            Process process;
            process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode;
            exitCode = process.waitFor();

            assert exitCode == 0;
            future.get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
