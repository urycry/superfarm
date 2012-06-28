package com.richardhughes.superfarm;

import java.util.ArrayList;
import java.util.Hashtable;

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

	private long _secondCounter = 0;

	private long _realMillisecondsInGameSecond = 0;
	
	public boolean Load(int startingYear, int dayInRealSeconds) {

		this.SetupDaysInMonth();

		this._year = startingYear;
		this._dayInRealSeconds = dayInRealSeconds;

		// 1 day is 86 400 000 milliseconds
		this._realMillisecondsInGameSecond = 86400000 / (this._dayInRealSeconds * 1000);

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

		this._secondCounter += game.LastFrameTime;

		if(this._secondCounter >= this._realMillisecondsInGameSecond) {

			this._seconds += (int)(this._secondCounter / this._realMillisecondsInGameSecond); // in case we need to add more than one second

			this._secondCounter = 0;

			if(this._seconds >= 60) {

				this._minutes++;

				this._seconds = 0;
			}

			if(this._minutes >= 60) {

				this._hours++;

				this._minutes = 0;
			}

			if(this._hours >= 24) {

				//this._hours++;

				this._hours = 0;
			}
		}
	}
}
