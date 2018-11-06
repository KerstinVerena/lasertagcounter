package com.example.android.lasertagcounter;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static final String stateScoreTeamRed = "Team Red Points";
    static final String stateScoreTeamGreen = "Team Green Points";
    static final String stateRechargeRed = "Recharge Red Time Left";
    static final String stateIsRedBaseImmune = "Red Currently Recharging";
    static final String stateRechargeGreen = "Recharge Green Time Left";
    static final String stateIsGreenBaseImmune = "Green Currently Recharging";
    public static TextView scoreViewRed;
    public static TextView scoreViewGreen;
    int scoreTeamRed = 0;
    int scoreTeamGreen = 0;
    private CountDownTimer baseRedRecharging;
    private CountDownTimer baseGreenRecharging;
    private Button redBaseDestroyed;
    private Button greenBaseDestroyed;
    private Button redBaseHit;
    private Button greenBaseHit;
    public int rechargeDuration = 60000;
    private Boolean isRedBaseImmune = false;
    private long redBaseRechargeLeft;
    private Boolean isGreenBaseImmune = false;
    private long greenBaseRechargeLeft;


    /**
     * Save the current scores so that they are not reset when app is rotated.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            scoreTeamRed = savedInstanceState.getInt(stateScoreTeamRed);
            scoreTeamGreen = savedInstanceState.getInt(stateScoreTeamGreen);
            redBaseRechargeLeft = savedInstanceState.getLong(stateRechargeRed);
            isRedBaseImmune = savedInstanceState.getBoolean(stateIsRedBaseImmune);
            greenBaseRechargeLeft = savedInstanceState.getLong(stateRechargeGreen);
            isGreenBaseImmune = savedInstanceState.getBoolean(stateIsGreenBaseImmune);
        }

        scoreViewRed = findViewById(R.id.team_red_score);
        scoreViewGreen = findViewById(R.id.team_green_score);
        redBaseDestroyed = findViewById(R.id.base_destroyed_red_button);
        greenBaseDestroyed = findViewById(R.id.base_destroyed_green_button);
        redBaseHit = findViewById(R.id.base_hit_red);
        greenBaseHit = findViewById(R.id.base_hit_green);

        if(isRedBaseImmune) {
            restartRechargeTimerRed();
        }

        if(isGreenBaseImmune) {
            restartRechargeTimerGreen();
        }
    }

    /**
     * Saved Instance State Bundle is used so that the scores don't reset when the app is rotated.
     * Information and code about the activity lifecyle: https://developer.android.com/guide/components/activities/activity-lifecycle.html
     * Code for using Activity States: https://stackoverflow.com/questions/151777/saving-android-activity-state-using-save-instance-state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(stateScoreTeamRed, scoreTeamRed);
        outState.putInt(stateScoreTeamGreen, scoreTeamGreen);
        outState.putLong(stateRechargeRed, redBaseRechargeLeft);
        outState.putBoolean(stateIsRedBaseImmune, isRedBaseImmune);
        outState.putLong(stateRechargeGreen, greenBaseRechargeLeft);
        outState.putBoolean(stateIsGreenBaseImmune, isGreenBaseImmune);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scoreTeamRed = savedInstanceState.getInt(stateScoreTeamRed);
        scoreTeamGreen = savedInstanceState.getInt(stateScoreTeamGreen);
        displayForTeamRed(scoreTeamRed);
        displayForTeamGreen(scoreTeamGreen);
        redBaseRechargeLeft = savedInstanceState.getLong(stateRechargeRed);
        isRedBaseImmune = savedInstanceState.getBoolean(stateIsRedBaseImmune);
        greenBaseRechargeLeft = savedInstanceState.getLong(stateRechargeGreen);
        isGreenBaseImmune = savedInstanceState.getBoolean(stateIsGreenBaseImmune);
    }

    /**
     * This method is called when the "Player Hit"-button for Team Red is clicked.
     * It increases the points of Team Red and decreases the Points of Team Green, when a player of
     * Team Red hits a player of Team Green
     */
    public void playerHitTeamRed(View view) {
        scoreTeamRed = scoreTeamRed + 30;
        scoreTeamGreen = scoreTeamGreen - 10;
        displayForTeamRed(scoreTeamRed);
        displayForTeamGreen(scoreTeamGreen);

    }

    /**
     * This method is called when the "Power Up"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Team Red collects a Power Up.
     */
    public void powerUpCollectedTeamRed(View view) {
        scoreTeamRed = scoreTeamRed + 20;
        displayForTeamRed(scoreTeamRed);

    }

    /**
     * This method is called when the "Base Hit"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Tem Red hits, but doesn't destroy the
     * base of Team Green.
     */
    public void baseHitTeamRed(View view) {
        if (isRedBaseImmune) {
            Toast.makeText(this, getString(R.string.base_is_recharging), Toast.LENGTH_SHORT)
                    .show();
        } else {
        scoreTeamRed = scoreTeamRed + 10;
        displayForTeamRed(scoreTeamRed); }
    }

    /**
     * This method is called when the "Base Destroyed"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Tem Red destroys the base of Team Green.
     */
    public void baseDestroyedTeamRed(View view) {
        if (isRedBaseImmune) {
            Toast.makeText(this, getString(R.string.base_is_recharging), Toast.LENGTH_SHORT)
                    .show();
        } else {
        scoreTeamRed = scoreTeamRed + 101;
        displayForTeamRed(scoreTeamRed);
        rechargeBaseRed();}
    }

    /**
     * This method is called when the "Player Hit"-button for Team Red is clicked.
     * It increases the points of Team Red and decreases the Points of Team Green, when a player of
     * Team Red hits a player of Team Green
     */
    public void playerHitTeamGreen(View view) {
        scoreTeamGreen = scoreTeamGreen + 30;
        scoreTeamRed = scoreTeamRed - 10;
        displayForTeamRed(scoreTeamRed);
        displayForTeamGreen(scoreTeamGreen);
    }

    /**
     * This method is called when the "Power Up"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Team Red collects a Power Up.
     */
    public void powerUpCollectedTeamGreen(View view) {
        scoreTeamGreen = scoreTeamGreen + 20;
        displayForTeamGreen(scoreTeamGreen);

    }

    /**
     * This method is called when the "Base Hit"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Tem Red hits, but doesn't destroy the
     * base of Team Green.
     */
    public void baseHitTeamGreen(View view) {
        if (isGreenBaseImmune) {
            Toast.makeText(this, getString(R.string.base_is_recharging), Toast.LENGTH_SHORT)
                    .show();
        } else {
        scoreTeamGreen = scoreTeamGreen + 10;
        displayForTeamGreen(scoreTeamGreen);}

    }

    /**
     * This method is called when the "Base Destroyed"-button for Team Red is clicked.
     * It increases the points of Team Red when a player of Tem Red destroys the base of Team Green.
     */
    public void baseDestroyedTeamGreen(View view) {
        if (isGreenBaseImmune) {
            Toast.makeText(this, getString(R.string.base_is_recharging), Toast.LENGTH_SHORT)
                    .show();
        } else {
        scoreTeamGreen = scoreTeamGreen + 101;
        displayForTeamGreen(scoreTeamGreen);
        rechargeBaseGreen(); }
    }


    /**
     * This method resets the scores for both teams.
     */
    public void resetAllScores(View view) {
        scoreTeamRed = 0;
        scoreTeamGreen = 0;
        displayForTeamRed(scoreTeamRed);
        displayForTeamGreen(scoreTeamGreen);


        if (isRedBaseImmune) {
            baseRedRecharging.cancel();
            baseRedRecharging.onFinish();
        }

        if (isGreenBaseImmune) {
            baseGreenRecharging.cancel();
            baseGreenRecharging.onFinish();
        }
    }

    /**
     * Displays the given score for Team Red.
     */
    public void displayForTeamRed(int score) {
        scoreViewRed.setText(String.valueOf(score));
    }


    /**
     * Displays the given score for Team Green.
     */
    public void displayForTeamGreen(int score) {
        scoreViewGreen.setText(String.valueOf(score));
    }

    /**
     * Timer for recharging after the Red Team destroyed the enemy base.
     */
    private void rechargeBaseRed () {
        // Code for disabling a button during a timer: https://stackoverflow.com/questions/41368127/enable-and-disable-button-using-timer-in-android
        redBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        redBaseHit.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        isRedBaseImmune = true;
        baseRedRecharging = new CountDownTimer(rechargeDuration, 1000) {
            @Override
            public void onTick(long timeToRecharge) {
                String rechargesMessage = getString(R.string.base_recharging);
                rechargesMessage += timeToRecharge / 1000;
                rechargesMessage += getString(R.string.time_value);
                redBaseDestroyed.setText(rechargesMessage);
                redBaseHit.setText(rechargesMessage);
                redBaseRechargeLeft = timeToRecharge;
            }

            @Override
            public void onFinish() {
                redBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.colorTeamRed));
                redBaseDestroyed.setText(getString(R.string.enemy_base_destroyed_text));
                redBaseHit.setBackgroundColor(getResources().getColor(R.color.colorTeamRed));
                redBaseHit.setText(getString(R.string.enemy_base_hit_text));
                isRedBaseImmune = false;
            }
        }.start();
    }

    /**
     * Timer for recharging enemy base of Red Team when device is rotated during recharging.
     */
    private void restartRechargeTimerRed (){
        redBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        redBaseHit.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        isRedBaseImmune = true;
        baseRedRecharging = new CountDownTimer(redBaseRechargeLeft, 1000) {
            @Override
            public void onTick(long timeToRecharge) {
                String rechargesMessage = getString(R.string.base_recharging);
                rechargesMessage += timeToRecharge / 1000;
                rechargesMessage += getString(R.string.time_value);
                redBaseDestroyed.setText(rechargesMessage);
                redBaseHit.setText(rechargesMessage);
                redBaseRechargeLeft = timeToRecharge;
            }

            @Override
            public void onFinish() {
                redBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.colorTeamRed));
                redBaseDestroyed.setText(getString(R.string.enemy_base_destroyed_text));
                redBaseHit.setBackgroundColor(getResources().getColor(R.color.colorTeamRed));
                redBaseHit.setText(getString(R.string.enemy_base_hit_text));
                isRedBaseImmune = false;
            }
        }.start();
    }

    /**
     * Timer for recharging after the Green Team destroyed the enemy base.
     */
    private void rechargeBaseGreen () {
        // Code for disabling a button during a timer: https://stackoverflow.com/questions/41368127/enable-and-disable-button-using-timer-in-android
        greenBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        greenBaseHit.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        isGreenBaseImmune = true;
        baseGreenRecharging = new CountDownTimer(rechargeDuration, 1000) {
            @Override
            public void onTick(long timeToRecharge) {
                String rechargesMessage = getString(R.string.base_recharging);
                rechargesMessage += timeToRecharge / 1000;
                rechargesMessage += getString(R.string.time_value);
                greenBaseDestroyed.setText(rechargesMessage);
                greenBaseHit.setText(rechargesMessage);
                greenBaseRechargeLeft = timeToRecharge;
            }

            @Override
            public void onFinish() {
                greenBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.colorTeamGreen));
                greenBaseDestroyed.setText(getString(R.string.enemy_base_destroyed_text));
                greenBaseHit.setBackgroundColor(getResources().getColor(R.color.colorTeamGreen));
                greenBaseHit.setText(getString(R.string.enemy_base_hit_text));
                isGreenBaseImmune = false;
            }
        }.start();
    }

    /**
     * Timer for recharging enemy base of Green Team when device is rotated during recharging.
     */
    private void restartRechargeTimerGreen (){
        greenBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        greenBaseHit.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        isGreenBaseImmune = true;
        baseGreenRecharging = new CountDownTimer(greenBaseRechargeLeft, 1000) {
            @Override
            public void onTick(long timeToRecharge) {
                String rechargesMessage = getString(R.string.base_recharging);
                rechargesMessage += timeToRecharge / 1000;
                rechargesMessage += getString(R.string.time_value);
                greenBaseDestroyed.setText(rechargesMessage);
                greenBaseHit.setText(rechargesMessage);
                greenBaseRechargeLeft = timeToRecharge;
            }

            @Override
            public void onFinish() {
                greenBaseDestroyed.setBackgroundColor(getResources().getColor(R.color.colorTeamGreen));
                greenBaseDestroyed.setText(getString(R.string.enemy_base_destroyed_text));
                greenBaseHit.setBackgroundColor(getResources().getColor(R.color.colorTeamGreen));
                greenBaseHit.setText(getString(R.string.enemy_base_hit_text));
                isGreenBaseImmune = false;
            }
        }.start();
    }

}