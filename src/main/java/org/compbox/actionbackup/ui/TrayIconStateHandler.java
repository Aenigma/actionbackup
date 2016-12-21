/*
 * Copyright 2016 Kevin Raoofi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.compbox.actionbackup.ui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.TrayIcon;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import org.compbox.actionbackup.utils.HookableRunnable.HookableRunnableFactory;

/**
 *
 * @author Kevin Raoofi
 */
public class TrayIconStateHandler {

    private static final Image[] images;
    private static final String[] toolTips = new String[]{
        "Idle",
        "Error!",
        "Currently backing up..."};

    static {
        final String[] imageResources = new String[]{
            "/icons/ok.png",
            "/icons/err.png",
            "/icons/prog.png"
        };
        images = new Image[imageResources.length];
        try {
            for (int i = 0; i < imageResources.length; i++) {
                images[i] = ImageIO.read(TrayDriver.class.getResource(
                        imageResources[i]));
            }
        } catch (IOException ex) {
            throw new IOError(ex);
        }
    }

    private final TrayIcon trayIcon;
    private final AtomicInteger ai = new AtomicInteger(0);
    private final Runnable updater = new Updater();

    private final ScheduledExecutorService ses = Executors
            .newScheduledThreadPool(1);

    private final Runnable enterState = () -> {
        ai.incrementAndGet();
        update();
    };
    private final Runnable exitState = () -> {
        ai.decrementAndGet();
    };

    public TrayIconStateHandler() {
        this.trayIcon = new TrayIcon(images[0], toolTips[0]);

        this.ses.scheduleWithFixedDelay(updater, 5, 5, TimeUnit.SECONDS);
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public Runnable getEnterState() {
        return enterState;
    }

    public Runnable getExitState() {
        return exitState;
    }

    public HookableRunnableFactory hookableFactory() {
        return new HookableRunnableFactory(
                Arrays.asList(enterState),
                Arrays.asList(exitState)
        );
    }

    public void shutdown() {
        this.ses.shutdown();
    }

    public void update() {
        EventQueue.invokeLater(this.updater);
    }

    private class Updater implements Runnable {

        @Override
        public void run() {
            // magic numbers wee
            if (ai.get() == 0) {
                trayIcon.setImage(images[0]);
                trayIcon.setToolTip(toolTips[0]);
            } else {
                trayIcon.setImage(images[2]);
                trayIcon.setToolTip(toolTips[2]);
            }
        }

    }
}
