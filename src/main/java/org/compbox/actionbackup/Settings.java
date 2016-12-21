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

/**
 *
 * @author Kevin Raoofi
 */
class Settings {

    static Settings getDefaults() {
        Settings settings = new Settings();
        settings.setSourceDir("src");
        settings.setDestDir("dest");

        settings.setSecondThreshold(0);
        settings.setMinuteThreshold(60);
        settings.setHourlyThreshold(48);
        settings.setDailyThreshold(14);
        settings.setWeeklyThreshold(15);
        settings.setMonthlyThreshold(Integer.MAX_VALUE);

        return settings;
    }

    private int secondThreshold;
    private int minuteThreshold;
    private int hourlyThreshold;
    private int dailyThreshold;
    private int weeklyThreshold;
    private int monthlyThreshold;
    private String sourceDir;
    private String destDir;

    public int getSecondThreshold() {
        return secondThreshold;
    }

    public void setSecondThreshold(int secondThreshold) {
        this.secondThreshold = secondThreshold;
    }

    public int getMinuteThreshold() {
        return minuteThreshold;
    }

    public void setMinuteThreshold(int minuteThreshold) {
        this.minuteThreshold = minuteThreshold;
    }

    public int getHourlyThreshold() {
        return hourlyThreshold;
    }

    public void setHourlyThreshold(int hourlyThreshold) {
        this.hourlyThreshold = hourlyThreshold;
    }

    public int getDailyThreshold() {
        return dailyThreshold;
    }

    public void setDailyThreshold(int dailyThreshold) {
        this.dailyThreshold = dailyThreshold;
    }

    public int getWeeklyThreshold() {
        return weeklyThreshold;
    }

    public void setWeeklyThreshold(int weeklyThreshold) {
        this.weeklyThreshold = weeklyThreshold;
    }

    public int getMonthlyThreshold() {
        return monthlyThreshold;
    }

    public void setMonthlyThreshold(int monthlyThreshold) {
        this.monthlyThreshold = monthlyThreshold;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getDestDir() {
        return destDir;
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

}
