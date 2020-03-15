package edu.umsl.jthkn9.mimicgame

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible


class GameActivity : AppCompatActivity() {

    //sounds
    lateinit var coin: MediaPlayer
    lateinit var dk: MediaPlayer
    lateinit var dieSound: MediaPlayer
    lateinit var life: MediaPlayer
    lateinit var zelda: MediaPlayer
    lateinit var sonic: MediaPlayer



    //a private list of the current sequence
    private var btnSequence = ArrayList<ButtonLocation>()

    //if this variable is positive then we are blocking the ui.
    //if it is zero then we allow button presses.
    private var animationsPlaying: Int = 0

    private var isRoundOver: Boolean = true;

    //keep track of what spot in btnSequence we are on.
    private var btnSequenceIndex: Int = 0

    //keep track of current score
    private var score: Int = 0
    private var gameOver: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_layout)
        val score: TextView = this.findViewById(R.id.score)
        coin = MediaPlayer.create(this.applicationContext, R.raw.coin)
        dk = MediaPlayer.create(this.applicationContext, R.raw.dk_jump)
        zelda = MediaPlayer.create(this.applicationContext, R.raw.rupee)
        sonic = MediaPlayer.create(this.applicationContext, R.raw.sonic)
        life = MediaPlayer.create(this.applicationContext, R.raw.mariolife)
        dieSound = MediaPlayer.create(this.applicationContext, R.raw.diesound)
        score.text = "0"
        val topLeft: Button = this.findViewById(R.id.topLeftBtn)
        val topRight: Button = this.findViewById(R.id.topRightBtn)
        val botLeft: Button = this.findViewById(R.id.botLeftBtn)
        val botRight: Button = this.findViewById(R.id.botRightBtn)
        setOnTouchButtonListener(topLeft,ButtonLocation.TOPLEFT)
        setOnTouchButtonListener(topRight,ButtonLocation.TOPRIGHT)
        setOnTouchButtonListener(botLeft,ButtonLocation.BOTTOMLEFT)
        setOnTouchButtonListener(botRight,ButtonLocation.BOTTOMRIGHT)


        //mp.start()
        pickNextButton()
        doSequenceOfButtons()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchButtonListener(btn: Button,btnLocation: ButtonLocation) {

        btn.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                //if the round is not over yet, the animations are not playing, and the game is not over
                MotionEvent.ACTION_DOWN -> if(!isRoundOver && this.animationsPlaying == 0 && !gameOver) {
                    if (btnSequenceIndex == btnSequence.size - 1){
                        isRoundOver = true;
                    }
                    btn.alpha = 0.2f
                    btnChosen(btnLocation)
                    if(!gameOver) {
                        when (btnLocation) {
                            ButtonLocation.BOTTOMRIGHT -> {
                                sonic.start()
                            }
                            ButtonLocation.TOPLEFT -> {
                                coin.start()
                            }
                            ButtonLocation.TOPRIGHT -> {
                                zelda.start()
                            }
                            ButtonLocation.BOTTOMLEFT -> {
                                dk.start()
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP ->  {
                    btn.alpha = 1f
                }
            }
            return@OnTouchListener true
        })
    }
    private fun endGame(){
        gameOver = true
        lightUpButton(btnSequence.last(),true)
        btnSequence.clear()

        //rumble the phone

        //show game over
        val gameOverTextView: TextView = this.findViewById(R.id.gameOver)
//        val menuBtn: Button = this.findViewById(R.id.menu)
//        val playBtn: Button = this.findViewById(R.id.playButton)
//set gameover listeners
        val menuBtn: Button = this.findViewById(R.id.gameMenuBtn)
        menuBtn.setOnClickListener {
            if(gameOver) {
                dieSound.stop()
                onBackPressed()
            }
        }
        val playBtn: Button = this.findViewById(R.id.gamePlayBtn)
        gameOverTextView.visibility = View.VISIBLE
        menuBtn.visibility = View.VISIBLE
        playBtn.visibility = View.VISIBLE
        //play gameover sound.
        dieSound.start()

    }
    private fun incrementScore(){
        score++
        val scoreTextView: TextView = this.findViewById(R.id.score)
        scoreTextView.text = "$score"//score.toString()
    }
    private fun btnChosen(btn: ButtonLocation){
        if(btnSequence[btnSequenceIndex] == btn){
            if(btnSequenceIndex == btnSequence.size - 1){
                btnSequenceIndex = 0;
                //completed round
                incrementScore()
                pickNextButton()
                doSequenceOfButtons()
            }
            else{
                btnSequenceIndex++
            }
        }
        //else lose
        else{
            endGame()
        }
    }
    private fun pickNextButton(){

        //pick a button
        val choice = ButtonLocation.values().random()
        btnSequence.add(choice)
    }
    private fun lightUpButton(location: ButtonLocation, repeat: Boolean){
        val flashAnimation: Animation =
            AlphaAnimation(1F, 0.2F) // Change alpha from fully visible to almost invisible
        flashAnimation.duration = 500 // duration - half a second

        flashAnimation.interpolator = LinearInterpolator() // do not alter animation rate
        if(repeat){
            flashAnimation.repeatCount = Animation.INFINITE
        }
        else {
            flashAnimation.repeatCount = 1 // Repeat animation once
        }
        flashAnimation.repeatMode =
            Animation.REVERSE // Reverse animation at the end so the button will fade back in
        flashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //animationsPlaying--
                //Does this happen ever and not trigger end or start?
            }

            override fun onAnimationEnd(animation: Animation?) {
                animationsPlaying--
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        val btn: Button = when(location){
            ButtonLocation.TOPLEFT-> {
                if(!gameOver)
                    coin.start()
                this.findViewById(R.id.topLeftBtn)
            }
            ButtonLocation.BOTTOMLEFT -> {
                if(!gameOver)
                    dk.start()
                this.findViewById(R.id.botLeftBtn)
            }
            ButtonLocation.BOTTOMRIGHT ->{
                if(!gameOver)
                    sonic.start()
                this.findViewById(R.id.botRightBtn)
            }
            ButtonLocation.TOPRIGHT -> {
                if(!gameOver)
                    zelda.start()
                this.findViewById(R.id.topRightBtn)
            }
        }
        btn.startAnimation(flashAnimation)
    }
    private fun doSequenceOfButtons(){
        val aniHandler =  Handler()
        var waitTime = 500L
        for (btn in btnSequence){
            animationsPlaying++
            aniHandler.postDelayed(Runnable {
                lightUpButton(btn,false)

            },waitTime)
            waitTime += 800
        }
        isRoundOver = false
    }
}


