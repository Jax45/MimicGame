package edu.umsl.jthkn9.mimicgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_scores.*

class ScoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)
        activityScoreListing.layoutManager = LinearLayoutManager(this)
        //activityScoreListing.adapter = ScoreAdapter()
    }
}
