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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Why isn't this public? Because it's a hack job and contains some business
 * logic
 *
 * @author Kevin Raoofi
 */
enum BackupPeriod {
    SECOND(Settings::getSecondThreshold, TimeUnit.SECONDS),
    MINUTE(Settings::getMinuteThreshold, TimeUnit.MINUTES),
    HOUR(Settings::getHourlyThreshold, TimeUnit.MINUTES),
    DAY(Settings::getDailyThreshold, TimeUnit.HOURS),
    WEEK(Settings::getWeeklyThreshold, TimeUnit.HOURS),
    MONTH(Settings::getMonthlyThreshold, TimeUnit.HOURS);

    final Function<Settings, Integer> thresholdAccessor;
    final TimeUnit checkFreqUnit;

    private BackupPeriod(Function<Settings, Integer> thresholdAccessor,
            TimeUnit checkFreqUnit) {
        this.thresholdAccessor = thresholdAccessor;
        this.checkFreqUnit = checkFreqUnit;
    }

    /**
     * Returns a date time formatter where the most precise field used is of the
     * given period. That is, the formatter for HOUR cannot display minutes or
     * seconds but does display the day.
     *
     * @return formatter with most precise field is of the given period
     */
    public DateTimeFormatter getFormatter() {
        final DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
        final Deque<String> ptnStack = new LinkedList<>();

        if (this.equals(WEEK)) {
            return DateTimeFormatter.ofPattern("YYYY-'W'w");
        }

        switch (this) {
            case SECOND:
                ptnStack.push("ss");
            case MINUTE:
                ptnStack.push("mm");
            case HOUR:
                ptnStack.push("'T'HH");
            case DAY:
                ptnStack.push("-dd");
            case MONTH:
                ptnStack.push("YYYY-MM");
        }

        for (String ptn : ptnStack) {
            dtfb.appendPattern(ptn);
        }

        return dtfb.toFormatter();
    }
}
