package edu.umsl.jthkn9.mimicgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val playGameBtn: Button = this.findViewById(R.id.playButton)
        playGameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            this.startActivity(intent)
        }
        val optionBtn: Button = this.findViewById(R.id.optionBtn)
        optionBtn.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            this.startActivity(intent)
        }
        val scoreBtn: Button = this.findViewById(R.id.highScoreBtn)
        scoreBtn.setOnClickListener {
            val intent = Intent(this, ScoresActivity::class.java)
            this.startActivity(intent)
        }
    }
}
