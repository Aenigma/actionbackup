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

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import org.compbox.actionbackup.BackupService;

/**
 *
 * @author Kevin Raoofi
 */
public class TrayDriver {

    // http://javachannel.org/posts/how-to-access-static-resources/
    public static void main(String... args) throws IOException,
            InterruptedException, AWTException {
        final TrayIconStateHandler tish = new TrayIconStateHandler();
        final BackupService bs = BackupService.performSetup(tish
                .hookableFactory());

        if (bs == null) {
            return;
        }

        final SystemTray tray = SystemTray.getSystemTray();
        final PopupMenu popupmenu = new PopupMenu("Tray Menu");
        final TrayIcon trayIcon = tish.getTrayIcon();
        trayIcon.setImageAutoSize(true);

        final MenuItem shutdown = new MenuItem("Exit");
        shutdown.addActionListener((e) -> {
            bs.shutdown();
            tish.shutdown();
            tray.remove(trayIcon);
        });

        final MenuItem about = new MenuItem("About");
        about.addActionListener((e) -> {
            final AboutDialog aboutDialog = new AboutDialog(null, true);
            aboutDialog.setVisible(true);
        });

        popupmenu.add(about);
        popupmenu.add(shutdown);
        trayIcon.setPopupMenu(popupmenu);

        tray.add(trayIcon);

        bs.call();
    }
}
