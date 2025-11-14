package com.example.vibing.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {

    private final MutableLiveData<Integer> mScore;
    private final MutableLiveData<Long> mLastCaptureTime;

    private static final long DECREMENT_RATE_MILLIS = 60 * 1000; // 1 point per minute

    public ScoreViewModel() {
        mScore = new MutableLiveData<>();
        mLastCaptureTime = new MutableLiveData<>();
        mScore.setValue(100); // Initial local score
        mLastCaptureTime.setValue(System.currentTimeMillis()); // Initial local capture time
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public void updateScoreBasedOnTime(String zoneId) {
        Integer currentScore = mScore.getValue();
        Long lastCapture = mLastCaptureTime.getValue();

        if (currentScore != null && lastCapture != null) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastCapture;

            int decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
            int newScore = Math.max(0, currentScore - decrementedAmount);

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
