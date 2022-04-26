package com.example.guitarchordprogressionproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.example.guitarchordprogressionproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var globalChordAmount = ""
    private var globalGenSelection = ""
    private var globalKeySelection = ""
    private var globalChordFlavor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Spinner for the amount of chords in the chord progression
        val spinId: Spinner = binding.spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.chord_amounts_array,
            R.layout.spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            // Apply the adapter to the spinner
            spinId.adapter = adapter
        }
        // Listeners for the spinners
        // They alter the values of the global variables
        val spinner1: Spinner = binding.spinner
        spinner1.onItemSelectedListener = this


        // Spinner for the type of Progression algorithm used
        val spinId2: Spinner = binding.spinner2
        ArrayAdapter.createFromResource(
            this,
            R.array.progression_algorithm,
            R.layout.spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            spinId2.adapter = adapter
        }
        val spinner2: Spinner = binding.spinner2
        spinner2.onItemSelectedListener = this

        // Spinner for the Key of the chord progression
        val spinId3: Spinner = binding.spinner3
        ArrayAdapter.createFromResource(
            this,
            R.array.key_array,
            R.layout.spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            spinId3.adapter = adapter
        }
        val spinner3: Spinner = binding.spinner3
        spinner3.onItemSelectedListener = this

        // Spinner for the Flavor of the chord progression
        val spinId4: Spinner = binding.spinner4
        ArrayAdapter.createFromResource(
            this,
            R.array.Chord_flavor,
            R.layout.spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            spinId4.adapter = adapter
        }
        val spinner4: Spinner = binding.spinner4
        spinner4.onItemSelectedListener = this


        val animationId = binding.animationView
        animationId.visibility = View.GONE

        binding.buttonRoll.setOnClickListener {
            animationId.visibility = View.VISIBLE
            timer.start()
        }

    }

    private fun generateChordProgression() {
        // ChordAmount is assigned the value of the amount of chords the user wishes to generate.
        // This is from the spinner selection
        val chordAmount = when(globalChordAmount) {
            "3" -> 3
            "4" -> 4
            "5" -> 5
            "6" -> 6
            else -> 7
        }
        var chordProgressionSequence = ""
        val chordList = mutableListOf<Int>()
        var randomNo: Int
        // If the user wants a completely randomly generated chord progression
        if (globalGenSelection == "Random") {
            repeat(chordAmount) {
                randomNo = (1..7).random()
                chordProgressionSequence = "$chordProgressionSequence $randomNo"
                chordList.add(randomNo)
            }
        }
        // If the user wants a chord progression based on the three common root movements,
        // The 3rd, 5th and step.
        else if (globalGenSelection == "3rd/5th/step root movements") {
            val oneChord = ChordClass()
            oneChord.addChord(2)
            oneChord.addChord(3)
            oneChord.addChord(4)
            oneChord.addChord(5)
            oneChord.addChord(6)
            oneChord.addChord(7)
            val twoChord = ChordClass()
            twoChord.addChord(5)
            twoChord.addChord(7)
            val threeChord = ChordClass()
            threeChord.addChord(4)
            threeChord.addChord(6)
            val fourChord = ChordClass()
            fourChord.addChord(2)
            fourChord.addChord(5)
            fourChord.addChord(7)
            val fiveChord = ChordClass()
            fiveChord.addChord(1)
            fiveChord.addChord(7)
            val sixChord = ChordClass()
            sixChord.addChord(4)
            sixChord.addChord(2)
            val sevenChord = ChordClass()
            sevenChord.addChord(1)
            sevenChord.addChord(5)
            sevenChord.addChord(6)

            val chordArray = mutableListOf(oneChord, twoChord, threeChord, fourChord,
                fiveChord, sixChord, sevenChord)

            var randomChosenChord = (1..7).random()
            chordProgressionSequence = "$chordProgressionSequence $randomChosenChord"
            chordList.add(randomChosenChord)

            repeat(chordAmount - 1) {
                val tempArray = chordArray[randomChosenChord - 1].jumpingChords
                randomChosenChord = tempArray[(0 until tempArray.size).random()]
                chordList.add(randomChosenChord)
                chordProgressionSequence = "$chordProgressionSequence $randomChosenChord"
            }
        }

        val chordProgressionString = printChordsInKey(chordList, globalKeySelection, globalChordFlavor)

        // Updating the textView
        val resultTextView: TextView = findViewById(R.id.textView)
        val resultTextView2: TextView = findViewById(R.id.textView6)
        resultTextView.text = chordProgressionString
        resultTextView2.text = chordProgressionSequence

    }

    // A timer for the timer animation
    private val timer = object: CountDownTimer(3000, 1000) {
        // We don't need anything done while the ticker is counting down.
        override fun onTick(p0: Long) {
            {}
        }

        override fun onFinish() {
            val animationId = binding.animationView
            animationId.visibility = View.GONE
            generateChordProgression()
        }
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // parent.getItemAtPosition(pos) retrieves the item at the item selected spinner
        val result = parent.getItemAtPosition(pos).toString()

        // Gets the id for the spinner that has been selected
        when (parent.id) {
            R.id.spinner -> globalChordAmount = result
            R.id.spinner2 -> globalGenSelection = result
            R.id.spinner3 -> globalKeySelection = result
            R.id.spinner4 -> globalChordFlavor = result
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }


}

fun printChordsInKey(chordList: MutableList<Int>, key: String, flavor: String): String {
    var returnString = ""

    val keySet = when (key) {
        "A" -> listOf("A","B","C#","D","E","F#","G#")
        "A#/b♭" -> listOf("b♭","C","D","E♭","F","G", "A")
        "B" -> listOf("B", "C#", "D#", "E", "F#", "G#", "A#")
        "C" -> listOf("C","D","E","F","G","A","B")
        "C#/D♭" -> listOf("C#/D♭","B","C#","D","E","F#","G#")
        "D" -> listOf("D", "E", "F#", "G", "A", "B", "C#")
        "D#/E♭" -> listOf("E♭", "F", "G", "A♭", "B♭", "C", "D")
        "E" -> listOf("E", "F#", "G#", "A", "B", "C#", "D#", "E")
        "F" -> listOf("F", "G", "A", "B♭", "C", "D", "E", "F")
        "F#/G♭" -> listOf("F#", "G#", "A#", "B", "C#", "D#", "F")
        "G" -> listOf("G", "A", "B", "C", "D", "E", "F#")
        else -> listOf("A♭", "B♭", "C", "D♭", "E♭", "F", "G")
    }
    var keyIndex: String
    for (item in chordList) {
        keyIndex = keySet[item - 1]
        returnString = if (flavor == "Normal") {
            when (item) {
                1, 4, 5 -> "$returnString $keyIndex" + "Maj"
                2, 3, 6 -> "$returnString $keyIndex" + "m"
                else -> "$returnString $keyIndex" + "dim"
            }
        }

        else {
            when (item) {
                1, 4 -> "$returnString $keyIndex" +"Maj7"
                5 -> "$returnString $keyIndex" + "7"
                2, 3, 6 -> "$returnString $keyIndex" + "m7"
                else -> "$returnString $keyIndex" + "dim7"
            }
        }
    }
    return returnString
}



class ChordClass {
    val jumpingChords: MutableList<Int> = mutableListOf()

    fun addChord(addedChord: Int) {
        jumpingChords.add(addedChord)
    }
}