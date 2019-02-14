package Marlang.AbilityWar.Utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import Marlang.AbilityWar.AbilityWar;

/**
 * Timer Base
 * @author _Marlang 말랑
 */
abstract public class TimerBase {

	private static ArrayList<TimerBase> Tasks = new ArrayList<TimerBase>();
	
	public static ArrayList<TimerBase> getTasks() {
		return Tasks;
	}

	public static void ResetTasks() {
		ArrayList<TimerBase> Reset = new ArrayList<TimerBase>(getTasks());
		
		for(TimerBase timer : Reset) {
			timer.StopTimer(true);
		}
		
		Tasks = new ArrayList<TimerBase>();
	}

	/**
	 * Register TimerBase
	 * @param timers
	 */
	private static void Register(TimerBase timer) {
		Tasks.add(timer);
	}

	/**
	 * Unregister TimerBase
	 * @param timers
	 */
	private static void Unregister(TimerBase timer) {
		if(Tasks.contains(timer)) {
			Tasks.remove(timer);
		}
	}

	private int Task = -1;

	protected boolean InfiniteTimer;
	protected boolean ProcessDuringGame = true;
	protected int Count;

	private int TempCount;

	protected int Period = 20;

	abstract public void TimerStart(Data<?>... args);

	abstract public void TimerProcess(Integer Seconds);

	abstract public void TimerEnd();

	public boolean isTimerRunning() {
		return Task != -1;
	}

	/**
	 * 타이머를 시작합니다.
	 */
	public void StartTimer(Data<?>... args) {
		if(!this.isTimerRunning()) {
			TempCount = Count;
			this.Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), new TimerTask(), 0, Period);
			if(ProcessDuringGame) {
				Register(this);
			}
			TimerStart(args);
		}
	}

	/**
	 * 타이머를 종료합니다.
	 */
	public void StopTimer(boolean Silent) {
		if(this.isTimerRunning()) {
			Bukkit.getScheduler().cancelTask(Task);
			Unregister(this);
			TempCount = Count;
			this.Task = -1;
			if(!Silent) {
				TimerEnd();
			}
		}
	}
	
	public int getCount() {
		return Count;
	}
	
	public int getTempCount() {
		return TempCount;
	}
	
	public TimerBase setPeriod(Integer Period) {
		this.Period = Period;
		return this;
	}
	
	public TimerBase setProcessDuringGame(boolean bool) {
		this.ProcessDuringGame = bool;
		return this;
	}
	
	public int getFixedTime(Integer Seconds) {
		return (int) (Seconds / (20 / Period));
	}
	
	/**
	 * 일반 타이머
	 */
	public TimerBase(int Count) {
		this.Count = Count;
		InfiniteTimer = false;
	}
	
	/**
	 * 무한 타이머
	 */
	public TimerBase() {
		InfiniteTimer = true;
	}
	
	public static class Data<T> {
		
		private final T value;
		private final Class<T> valueClass;
		
		public Data(T value, Class<T> clazz) {
			this.value = value;
			this.valueClass = clazz;
		}
		
		@SuppressWarnings("unchecked")
		public <U> U getValue(Class<U> clazz) {
			if(value != null) {
				if(clazz.equals(valueClass)) {
					return (U) value;
				}
			}

			return null;
		}
		
	}
	
	private final class TimerTask extends Thread {

		@Override
		public void run() {
			if (ProcessDuringGame) {
				if (AbilityWarThread.isGameTaskRunning()) {
					if (InfiniteTimer) {
						TimerProcess(-1);
					} else {
						if (TempCount > 0) {
							TimerProcess(TempCount);

							if (TempCount <= 0) {
								StopTimer(false);
							}

							TempCount--;
						} else {
							StopTimer(false);
						}
					}
				} else {
					StopTimer(true);
				}
			} else {
				if (InfiniteTimer) {
					TimerProcess(-1);
				} else {
					TimerProcess(TempCount);

					if (TempCount <= 0) {
						StopTimer(false);
					}

					TempCount--;
				}
			}
		}

	}

}