package de.olivermakesco.bta_utils.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;

import de.olivermakesco.bta_utils.config.BtaUtilsConfig;

import net.minecraft.server.MinecraftServer;

public class MinecraftStopIfNoPlayers {

    private static int playerCounter = 0;

    public static void scheduleServerShutdownIfNoPlayers() {
        if (BtaUtilsConfig.as_wait_delay > 0) {
            System.out.println("Starting new thread for Shutdown");
            Thread thread = new Thread(() -> {
                while (playerCounter < BtaUtilsConfig.as_wait_delay) {
                    System.out.println("Step " + playerCounter);
                    if (getOnlinePlayerCount() > 0) {
                        playerCounter = 0;
                    } else {
                        playerCounter++;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (SystemUtils.IS_OS_LINUX) {
                        shutdownSystem(BtaUtilsConfig.as_shutdown_delay);
                    } else {
                        System.out.println("Physical machine shutdown not supported on non-Linux systems!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }
    }

    public static int getOnlinePlayerCount() {
        try {
            MinecraftServer server = MinecraftServer.getInstance();
            return server.configManager.playerEntities.size();
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public static void shutdownSystem(int delayInSeconds) throws IOException {
        if (playerCounter >= BtaUtilsConfig.as_wait_delay) {
            String delayMinutes = "+" + delayInSeconds;
            System.out.println("Shutdown started in " + delayMinutes);
            ProcessBuilder processBuilder = new ProcessBuilder("shutdown", "-P", delayMinutes);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = 0;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("Proces zakończony z kodem wyjścia: " + exitCode);

            MinecraftServer server = MinecraftServer.getInstance();
            System.out.println("Shutdown server");
            server.initiateShutdown();
        }
    }
}