package com.cnl.mybot.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;

public class LogRedirect {

    public static PrintStream consoleOut;

    private static String currentFile;
    private static PrintStream currentOut;

    public static void redirect() throws IOException {
        FileOutputStream pw = createOutput();
        if (pw == null) return;
        consoleOut = System.out;
        currentOut = new PrintStream(pw);
        System.setOut(currentOut);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.setOut(consoleOut);
            try {
                currentOut.flush();
                currentOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        scheduled();
    }

    private static void scheduled() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.add(Calendar.DATE, 1);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String lastFile = currentFile;
                    FileOutputStream newOutput = createOutput();
                    if (newOutput == null) return;
                    PrintStream newStream = new PrintStream(newOutput);
                    System.setOut(newStream);
                    currentOut.flush();
                    currentOut.close();
                    currentOut = newStream;
                    String zip = compressArchive(lastFile);
                    boolean success = new File(lastFile).delete();
                    System.out.println("delete last log " + (success ? "success" : "failed"));
                    System.out.println("gz file path: " + zip);
                } catch (Exception ignore) {
                }
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    public static String compressArchive(String path) throws IOException {
        final String name = path + ".gz";
        GZIPOutputStream out;
        try {
            out = new GZIPOutputStream(new FileOutputStream(name));
        } catch (FileNotFoundException e) {
            System.err.println("Could not create file: " + name);
            return null;
        }

        FileInputStream in;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.err.println("File not found. " + path);
            return null;
        }

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();

        out.finish();
        out.close();
        return name;
    }

    private static FileOutputStream createOutput() throws IOException {
        File folder = new File("log");
        if (!folder.exists() && !folder.mkdir()) return null;
        long time = System.currentTimeMillis();
        File file = new File(folder, time + ".log");
        currentFile = file.getPath();
        return new FileOutputStream(file);
    }

}
