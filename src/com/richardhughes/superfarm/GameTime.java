package com.richardhughes.superfarm;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

public class GameTime {

	private Hashtable<Month, Integer> _daysInMonth = new Hashtable<Month, Integer>();
	public Hashtable<Month, Integer> GetDaysInMonth() { return this._daysInMonth; }

	private int _seconds = 0;
	public int GetSeconds() { return this._seconds; }
	public void SetSeconds(int value) { this._seconds = value; }

	private int _minutes = 0;
	public int GetMinutes() { return this._minutes; }
	public void SetMinutes(int value) { this._minutes = value; }
	
	private int _hours = 0;
	public int GetHours() { return this._hours; }
	public void SetHours(int value) { this._hours = value; }

	private Day _day = Day.Monday;
	public Day GetDay() { return this._day; }
	public void SetDay(Day value) { this._day = value; }

	private int _dayOfMonth = 1; // the actual date itself, as opposed to the day of week
	public int GetDayOfMonth() { return this._dayOfMonth; }
	public void SetDayOfMonth(int value) { this._dayOfMonth = value; }

	private Month _month = Month.January;
	public Month GetMonth() { return this._month; }
	public void SetMonth(Month value) { this._month = value; }

	private int _year = 0; // start year will be read from game settings
	public int GetYear() { return this._year; }
	public void SetYear(int value) { this._year = value; }

	private int _dayInRealSeconds = 0; // the number of real seconds (in living people time) to an in-game day
	public int GetDayInRealSeconds() { return this._dayInRealSeconds; }
	public void SetDayInRealSeconds(int value) { this._dayInRealSeconds = value; }

	private long _timeCounter = 0;

	private double _realSecondsInGameSecond = 0;

	public boolean Load(int startingYear, int dayInRealSeconds) {

		this.SetupDaysInMonth();

		this._year = startingYear;
		this._dayInRealSeconds = dayInRealSeconds;

		// 86400 is number of seconds per real day
		this._realSecondsInGameSecond = this._dayInRealSeconds / 86400.0d;

		return true;
	}

	private void SetupDaysInMonth() {

		this._daysInMonth.clear();

		this._daysInMonth.put(Month.January, 31);
		this._daysInMonth.put(Month.Febuary, 28); // no leap years
		this._daysInMonth.put(Month.March, 31);
		this._daysInMonth.put(Month.April, 30);
		this._daysInMonth.put(Month.May, 31);
		this._daysInMonth.put(Month.June, 30);
		this._daysInMonth.put(Month.July, 31);
		this._daysInMonth.put(Month.August, 31);
		this._daysInMonth.put(Month.September, 30);
		this._daysInMonth.put(Month.October, 31);
		this._daysInMonth.put(Month.November, 30);
		this._daysInMonth.put(Month.December, 31);
	}

	public void Update(SuperFarmGame game) {

		this._timeCounter += game.TimeSinceLastFrame;

		double time = this._timeCounter / 1000.0d;
		
		if(time >= this._realSecondsInGameSecond) {

			this._seconds += time / this._realSecondsInGameSecond; // in case we need to add more than one second

			this._timeCounter = 0;

			if(this._seconds >= 60) {

				this._minutes += (int)(this._seconds / 60);

				this._seconds = this._seconds % 60;
			}

			if(this._minutes >= 60) {

				this._hours += (int)(this._minutes / 60);

				this._minutes = this._minutes % 60;
			}

			if(this._hours >= 24) {

				this.AdvanceDay();

				this._hours = 0;
			}
		}
	}

	private void AdvanceDay() {

		if(this._day == Day.Monday)
			this._day = Day.Tuesday;
		else if(this._day == Day.Tuesday)
			this._day = Day.Wednesday;
		else if(this._day == Day.Wednesday)
			this._day = Day.Thursday;
		else if(this._day == Day.Thursday)
			this._day = Day.Friday;
		else if(this._day == Day.Friday)
			this._day = Day.Saturday;
		else if(this._day == Day.Saturday)
			this._day = Day.Sunday;
		else if(this._day == Day.Sunday)
			this._day = Day.Monday;

		this.AdvanceDayOfMonth();
	}

	private void AdvanceDayOfMonth() {

		this._dayOfMonth++;

		if(this._dayOfMonth > this._daysInMonth.get(this._month)) {

			this._dayOfMonth = 1;

			this.AdvanceMonth();
		}
	}

	private void AdvanceMonth() {

		if(this._month == Month.January)
			this._month = Month.Febuary;
		else if(this._month == Month.Febuary)
			this._month = Month.March;
		else if(this._month == Month.March)
			this._month = Month.April;
		else if(this._month == Month.April)
			this._month = Month.May;
		else if(this._month == Month.May)
			this._month = Month.June;
		else if(this._month == Month.June)
			this._month = Month.July;
		else if(this._month == Month.July)
			this._month = Month.August;
		else if(this._month == Month.August)
			this._month = Month.September;
		else if(this._month == Month.September)
			this._month = Month.October;
		else if(this._month == Month.October)
			this._month = Month.November;
		else if(this._month == Month.November)
			this._month = Month.December;
		else if(this._month == Month.December) {

			this._month = Month.January;

			this._year++;
		}
	}
}
