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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Kevin Raoofi
 */
public class ChronoBackupper implements Runnable {

    private final Path sourceDirectory;
    private final Path destDirectory;

    private final DateTimeFormatter dtf;

    private final int threshold;

    public ChronoBackupper(Path sourceDirectory, Path destDirectory,
            DateTimeFormatter dtf, int threshold) {
        this.sourceDirectory = sourceDirectory;
        this.destDirectory = destDirectory;
        this.dtf = dtf;
        this.threshold = threshold;
    }

    public File getFileName() {
        return this.destDirectory.resolve(LocalDateTime.now()
                .format(dtf))
                .toFile();

    }

    @Override
    public void run() {
        if (this.threshold == 0) {
            return;
        }
        try {
            final File dest;
            try {
                dest = getFileName();
            } catch (Exception e) {
                Logger.getLogger(BackupService.class.getName())
                        .log(Level.SEVERE, null, e);
                return;
            }
            if (dest.exists()) {
                System.out.println("Destination already exists: " + dest);
                return;
            }
            System.out.println("Writing to: " + dest);
            FileUtils.copyDirectory(sourceDirectory.toFile(), dest);

            File[] listFiles = Files.list(destDirectory)
                    .filter(Files::isDirectory)
                    .sorted()
                    .map(Path::toFile)
                    .toArray(File[]::new);

            if (listFiles.length > this.threshold) {
                for (int i = 0; i < listFiles.length - this.threshold; i++) {
                    // System.out.println("Deleting: " + listFiles[i]);
                    FileUtils.deleteDirectory(listFiles[i]);
                }
            } else {
                //System.out.println(listFiles.length);
            }
        } catch (IOException ex) {
            Logger.getLogger(BackupService.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

}
