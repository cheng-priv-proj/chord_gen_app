package com.example.guitarchordprogressionproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.guitarchordprogressionproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRoll.setOnClickListener { generateChordProgression() }

    }

    private fun generateChordProgression() {
        // ChordAmount is assigned the value of the amount of chords the user wishes to generate.
        // This corresponds to the chordAmountSelection radioGroup
        var chordAmount = when(binding.chordAmountSelection.checkedRadioButtonId) {
            R.id.three_chords -> 3
            R.id.four_chords -> 4
            R.id.five_chords -> 5
            else -> 6
        }
        var chordProgressionString = ""

        // If the user wants a completely randomly generated chord progression
        if (binding.chordTypeSelection.checkedRadioButtonId == R.id.random_Movement_Selection) {
            repeat(chordAmount) {
                chordProgressionString = (((1..7).random()).toString()) + " " + chordProgressionString
            }
        }
        // If the user wants a chord progression based on the three common root movements,
        // The 3rd, 5th and step.
        else if (binding.chordTypeSelection.checkedRadioButtonId == R.id.root_Movement_Selection) {
            var oneChord = chordClass(6, 1)
            oneChord.addChord(2)
            oneChord.addChord(3)
            oneChord.addChord(4)
            oneChord.addChord(5)
            oneChord.addChord(6)
            oneChord.addChord(7)
            var twoChord = chordClass(2, 2)
            twoChord.addChord(5)
            twoChord.addChord(7)
            var threeChord = chordClass(2, 3)
            threeChord.addChord(4)
            threeChord.addChord(6)
            var fourChord = chordClass(3, 4)
            fourChord.addChord(2)
            fourChord.addChord(5)
            fourChord.addChord(7)
            var fiveChord = chordClass(3, 5)
            fiveChord.addChord(1)
            fiveChord.addChord(7)
            var sixChord = chordClass(2, 6)
            sixChord.addChord(4)
            sixChord.addChord(2)
            var sevenChord = chordClass(3, 7)
            sevenChord.addChord(1)
            sevenChord.addChord(5)
            sevenChord.addChord(6)

            val chordArray = mutableListOf<chordClass>(oneChord, twoChord, threeChord, fourChord,
                fiveChord, sixChord, sevenChord)

            var randomChosenChord = (1..7).random()
            chordProgressionString = "$chordProgressionString $randomChosenChord"

            repeat(chordAmount - 1) {
                var tempArray = chordArray[randomChosenChord - 1].jumpingChords
                randomChosenChord = tempArray[(0 until tempArray.size).random()]
                chordProgressionString = "$chordProgressionString $randomChosenChord"
            }

        }

        val resultTextView: TextView = findViewById(R.id.textView)

        resultTextView.text = chordProgressionString

    }
}


class chordClass(private val jChordsAmount: Int, private val chordId: Int) {
    val jumpingChords: MutableList<Int> = mutableListOf()

    fun addChord(addedChord: Int) {
        jumpingChords.add(addedChord)
    }
}