package com.infopulse.infomail.services.scheduler.cronGenerator;

public class CronExpressionBuilder {

    private final String seconds;
    private String minutes;
    private String hours;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String year;

    private boolean isTypeAlreadySet;   //param dayOfWeek or dayOfMonth was changed
    private boolean isADayOfWeekType;   //dayOfWeek "*"

    /**
     * Create a CRON expression
     *
     * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
     * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
     * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
     * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
     * @param year       mandatory = no. allowed values = {@code 1970–2099    * / , -}
     */
    private CronExpressionBuilder(String seconds,
                                  String minutes,
                                  String hours,
                                  String dayOfMonth,
                                  String month,
                                  String dayOfWeek,
                                  String year) {

        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
    }

    /** @return a CRON Formatted String*/
    @Override
    public String toString() {
        return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s",
                seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CronExpressionBuilder cron;
        private Builder(){
            cron = new CronExpressionBuilder("0", "*", "*",
                    "?", "*", "*", "*");
            cron.isADayOfWeekType = true;
            cron.isTypeAlreadySet = false;
        };

        public CronExpressionBuilder build() {return cron;}

        public Builder setMinutes(String minutes) {
            cron.minutes = minutes;
            return this;
        }
        public Builder setMinutes(Integer minutes) {
            cron.minutes = Integer.toString(minutes);
            return this;
        }

        public Builder setHours(String hours) {
            cron.hours = hours;
            return this;
        }
        public Builder setHours(Integer hours) {
            cron.hours = Integer.toString(hours);
            return this;
        }

        public Builder setDayOfMonth(String dayOfMonth) {
            if(cron.isTypeAlreadySet && cron.isADayOfWeekType) {
                throw new RuntimeException("We can't set DayOfMonth because DayOfWeek already set");
            }

            cron.isTypeAlreadySet = true;
            cron.isADayOfWeekType = false;
            cron.dayOfWeek = "?";
            cron.dayOfMonth = dayOfMonth;
            return this;
        }

        public Builder setMonth(String month) {
            cron.month = month;
            return this;
        }

        public Builder setDayOfWeek(String dayOfWeek) {
            if(cron.isTypeAlreadySet && !cron.isADayOfWeekType) {
                throw new RuntimeException("We can't set DayOfWeek because DayOfMonth already set");
            }

            cron.isTypeAlreadySet = true;
            cron.isADayOfWeekType = true;
            cron.dayOfWeek = dayOfWeek;
            return this;
        }

        public Builder setYear(String year) {
            cron.year = year;
            return this;
        }

    }
}
