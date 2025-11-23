package com.example.vibing.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {

    private final MutableLiveData<Integer> mScore;
    private final MutableLiveData<Long> mLastCaptureTime;
    private final MutableLiveData<Integer> mMoneyScore;

    private static final long DECREMENT_RATE_MILLIS = 1 * 1000; // 1 point per second

    public ScoreViewModel() {
        mScore = new MutableLiveData<>();
        mLastCaptureTime = new MutableLiveData<>();
        mMoneyScore = new MutableLiveData<>();
        mScore.setValue(10); // Initial local score
        mLastCaptureTime.setValue(System.currentTimeMillis()); // Initial local capture time
        mMoneyScore.setValue(0); // Initial money score
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<Integer> getMoneyScore() {
        return mMoneyScore;
    }

    public void addMoneyBonus(int amount) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            mMoneyScore.setValue(currentMoney + amount);
        }
    }

    public void setZoneScore(int newScore) {
        mScore.setValue(newScore);
        mLastCaptureTime.setValue(System.currentTimeMillis());
    }

    public void updateScoreBasedOnTime(String zoneId) {
        Integer currentScore = mScore.getValue();
        Long lastCapture = mLastCaptureTime.getValue();

        if (currentScore != null && lastCapture != null) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastCapture;

            int decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
                        int newScore = Math.max(10, currentScore - decrementedAmount);

            mScore.setValue(newScore);
            mLastCaptureTime.setValue(currentTime);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // No longer need to remove callbacks for scoreDecreaser
    }
}
