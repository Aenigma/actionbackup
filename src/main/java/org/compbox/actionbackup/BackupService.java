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
package org.compbox.actionbackup;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import org.compbox.actionbackup.utils.HookableRunnable.HookableRunnableFactory;

/**
 *
 * @author Kevin Raoofi
 */
public class BackupService implements Callable<Void> {

    private static final Path SETTINGS_FILE = Paths.get("config.json");
    // i love jackson; i hate java preferences API
    private static final ObjectMapper OM = new ObjectMapper();

    static Settings getSettings() throws IOException {

        // if the settings file doesn't exist, let's be cool and prepopulate it!
        if (!Files.exists(SETTINGS_FILE)) {
            final Settings settings = Settings.getDefaults();
            OM.writerWithDefaultPrettyPrinter()
                    .writeValue(Files.newBufferedWriter(SETTINGS_FILE), settings);

            return null;
        }
        final Settings settings = OM.readValue(Files.newBufferedReader(
                SETTINGS_FILE),
                Settings.class);

        return settings;
    }

    public static BackupService performSetup(HookableRunnableFactory pprwf)
            throws IOException {
        // let's read settings from a file now!
        final Settings settings = getSettings();

        if (settings == null) {
            JOptionPane.showMessageDialog(null,
                    "Hey, you didn't make a config file. I made one for you. "
                    + "Please configure it and rerun. You can find the config "
                    + "file at: \n"
                    + SETTINGS_FILE.toAbsolutePath());
            return null;
        }

        return new BackupService(settings, pprwf);
    }

    public static void main(String... args) throws IOException {
        performSetup(HookableRunnableFactory.DEFAULT)
                .call();
    }

    private final ScheduledExecutorService ses;
    private final Settings settings;

    private final HookableRunnableFactory pprwf;

    BackupService(Settings settings, HookableRunnableFactory pprwf) {
        this.ses = Executors.newScheduledThreadPool(16);
        this.settings = settings;
        this.pprwf = pprwf;
    }

    private void submit(Runnable r, TimeUnit tu) {
        final Runnable task = this.pprwf.createInstance(r);
        ses.scheduleAtFixedRate(task, 0, 1, tu);
    }

    public void shutdown() {
        this.ses.shutdown();
    }

    @Override
    public Void call() throws IOException {
        // now we're reading the settings file
        final Path src = Paths.get(settings.getSourceDir());
        final Path dest = Paths.get(settings.getDestDir());

        // we gonna iterate through
        for (BackupPeriod bp : BackupPeriod.values()) {
            final int threshold = bp.thresholdAccessor.apply(settings);

            if (threshold > 0) {
                final Path periodDest = dest.resolve(bp.name()
                        .toLowerCase());

                Files.createDirectories(periodDest);

                final ChronoBackupper backupper = new ChronoBackupper(
                        src,
                        periodDest,
                        bp.getFormatter(),
                        threshold);
                submit(backupper, bp.checkFreqUnit);
            }
        }
        return null;
    }
}
