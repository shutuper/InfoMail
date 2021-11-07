package com.infopulse.infomail.services.scheduler.cronGenerator;

import com.infopulse.infomail.models.mail.EmailSchedule;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;


public class CronGenerator {
	/**
	 * Generate a CRON expression is a string comprising 6 or 7 fields separated by white space.
	 *
	 * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
	 * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
	 * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
	 * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
	 * @param year       mandatory = no. allowed values = {@code 1970–2099    * / , -}
	 * @return a CRON Formatted String.
	 */
	public static String generate(final String seconds,
	                              final String minutes,
	                              final String hours,
	                              final String dayOfMonth,
	                              final String month,
	                              final String dayOfWeek,
	                              final String year) {

		return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s",
				seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
	}

	public static String generate(EmailSchedule messageSchedule) {
		return switch (messageSchedule.getRepeatAt()) {
			case EVERY_DAY -> whenEveryDay(messageSchedule);
			case EVERY_WEEK -> whenEveryWeek(messageSchedule);
			case EVERY_MONTH -> whenEveryMonth(messageSchedule);
			case EVERY_YEAR -> whenEveryYear(messageSchedule);
			default -> throw new IllegalArgumentException(
					"Not define methods for parse messageSchedule with RepeatType = "
							+ messageSchedule.getRepeatAt());
		};
	}

	static String whenEveryDay(EmailSchedule messageSchedule) {
		return CronExpression.builder()
				.setHours(messageSchedule
						.getSendDateTime()
						.getHour())
				.setMinutes(messageSchedule
						.getSendDateTime()
						.getMinute())
				.build()
				.toString();
	}

	static String whenEveryWeek(EmailSchedule messageSchedule) {
		String daysOfWeek = parseDaysOfWeek(messageSchedule);

		return CronExpression.builder()
				.setHours(messageSchedule
						.getSendDateTime()
						.getHour())
				.setMinutes(messageSchedule
						.getSendDateTime()
						.getMinute())
				.setDayOfWeek(daysOfWeek)
				.build()
				.toString();
	}

	static String whenEveryMonth(EmailSchedule messageSchedule) {
		CronExpression.Builder builder = CronExpression.builder()
				.setHours(messageSchedule
						.getSendDateTime()
						.getHour())
				.setMinutes(messageSchedule
						.getSendDateTime()
						.getMinute());

		String dayOfWeek = parseDayOfWeekAndNumberOfWeek(messageSchedule);
		if (!dayOfWeek.equals("?")) {
			return builder
					.setDayOfWeek(dayOfWeek)
					.build()
					.toString();
		}

		String dayOfMonth = parseDayOfMonth(messageSchedule);
		return builder
				.setDayOfMonth(dayOfMonth)
				.build()
				.toString();
	}

	static String whenEveryYear(EmailSchedule messageSchedule) {
		CronExpression.Builder builder = CronExpression.builder()
				.setHours(messageSchedule
						.getSendDateTime()
						.getHour())
				.setMinutes(messageSchedule
						.getSendDateTime()
						.getMinute());

		String month = parseMonth(messageSchedule);
		builder.setMonth(month);

		String dayOfWeek = parseDayOfWeekAndNumberOfWeek(messageSchedule);
		if (!dayOfWeek.equals("?")) {
			return builder
					.setDayOfWeek(dayOfWeek)
					.build().toString();
		}

		String dayOfMonth = parseDayOfMonth(messageSchedule);
		return builder
				.setDayOfMonth(dayOfMonth)
				.build()
				.toString();
	}

	static String parseDaysOfWeek(EmailSchedule messageSchedule) {
		List<Integer> daysOfWeek = messageSchedule.getDaysOfWeek();
		if (daysOfWeek == null || daysOfWeek.isEmpty()) {
			int dayOfWeekValue = messageSchedule
					.getSendDateTime()
					.getDayOfWeek()
					.getValue();

			dayOfWeekValue = dayOfWeekConvertor(dayOfWeekValue);
			return Integer.toString(dayOfWeekValue);
		}

		StringBuilder builder = new StringBuilder();
		daysOfWeek.forEach(dayOfWeekValue -> {
			DayOfWeek.of(dayOfWeekValue); //check in DayOfWeek
			builder.append(dayOfWeekValue).append(',');
		});
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	static String parseDayOfMonth(EmailSchedule messageSchedule) {
		int dayOfMonth = messageSchedule.getDayOfMonth();
		if (dayOfMonth != 0) {
			checkDayOfMonthValue(dayOfMonth);
			return String.valueOf(dayOfMonth);
		}
		return String.valueOf(messageSchedule.getSendDateTime().getDayOfMonth());
	}

	static String parseDayOfWeekAndNumberOfWeek(EmailSchedule messageSchedule) {
		int dayOfWeekValue = messageSchedule.getDayOfWeek();
		int numberOfWeek = messageSchedule.getNumberOfWeek();

		if (dayOfWeekValue != 0 && numberOfWeek != 0) {

			DayOfWeek.of(dayOfWeekValue);    //check in DayOfWeek
			checkNumberOfWeekValue(numberOfWeek);

			dayOfWeekValue = dayOfWeekConvertor(dayOfWeekValue);
			return String.format("%1$s#%2$s", dayOfWeekValue, numberOfWeek);

		} else return "?";

	}

	static String parseMonth(EmailSchedule messageSchedule) {
		int monthValue = messageSchedule.getMonth();
		if (monthValue != 0) {
			Month.of(monthValue);   //check monthValue in Month
			return Integer.toString(monthValue);
		}

		return String.valueOf(messageSchedule.getSendDateTime().getMonth().getValue());
	}

	private static void checkNumberOfWeekValue(int numberOfWeek) {
		if ((numberOfWeek > 4) || (numberOfWeek < 1))
			throw new DateTimeException("Invalid value for NumberOfWeek: " + numberOfWeek);
	}

	private static void checkDayOfMonthValue(int dayOfMonth) {
		if ((dayOfMonth > 31) || (dayOfMonth < 1))
			throw new DateTimeException("Invalid value for DayOfMonth: " + dayOfMonth);
	}

	/**
	 * In Cron Weeks starter at Sunday and finish at Saturday
	 * So Sunday index 0, Saturday index 6
	 * But in {@code DayOfWeek} week start at Monday
	 */
	private static int dayOfWeekConvertor(int dayOfWeekValue) {
		dayOfWeekValue++;
		if (dayOfWeekValue == 8) dayOfWeekValue = 1;
		return dayOfWeekValue;
	}

}
