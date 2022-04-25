package com.example.guitarchordprogressionproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.guitarchordprogressionproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    var globalChordAmount = ""
    var globalGenSelection = ""
    var globalKeySelection = ""
    var globalChordFlavor = ""

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
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinId2.adapter = adapter
        }
        val spinner2: Spinner = binding.spinner2
        spinner2.onItemSelectedListener = this

        // Spinner for the Key of the chord progression
        val spinId3: Spinner = binding.spinner3
        ArrayAdapter.createFromResource(
            this,
            R.array.key_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinId3.adapter = adapter
        }
        val spinner3: Spinner = binding.spinner3
        spinner3.onItemSelectedListener = this

        // Spinner for the Flavor of the chord progression
        val spinId4: Spinner = binding.spinner4
        ArrayAdapter.createFromResource(
            this,
            R.array.Chord_flavor,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinId4.adapter = adapter
        }
        val spinner4: Spinner = binding.spinner4
        spinner4.onItemSelectedListener = this

        binding.buttonRoll.setOnClickListener { generateChordProgression() }

    }

    private fun generateChordProgression() {
        // ChordAmount is assigned the value of the amount of chords the user wishes to generate.
        // This is from the spinner selection
        var chordAmount = when(globalChordAmount) {
            "3" -> 3
            "4" -> 4
            "5" -> 5
            "6" -> 6
            else -> 7
        }
        var chordProgressionSequence = ""
        var chordList = mutableListOf<Int>()
        var random_no = 0
        // If the user wants a completely randomly generated chord progression
        if (globalGenSelection == "Random") {
            repeat(chordAmount) {
                random_no = (1..7).random()
                chordProgressionSequence = "$chordProgressionSequence $random_no"
                chordList.add(random_no)
            }
        }
        // If the user wants a chord progression based on the three common root movements,
        // The 3rd, 5th and step.
        else if (globalGenSelection == "3rd/5th/step root movements") {
            var oneChord = ChordClass(6, 1)
            oneChord.addChord(2)
            oneChord.addChord(3)
            oneChord.addChord(4)
            oneChord.addChord(5)
            oneChord.addChord(6)
            oneChord.addChord(7)
            var twoChord = ChordClass(2, 2)
            twoChord.addChord(5)
            twoChord.addChord(7)
            var threeChord = ChordClass(2, 3)
            threeChord.addChord(4)
            threeChord.addChord(6)
            var fourChord = ChordClass(3, 4)
            fourChord.addChord(2)
            fourChord.addChord(5)
            fourChord.addChord(7)
            var fiveChord = ChordClass(3, 5)
            fiveChord.addChord(1)
            fiveChord.addChord(7)
            var sixChord = ChordClass(2, 6)
            sixChord.addChord(4)
            sixChord.addChord(2)
            var sevenChord = ChordClass(3, 7)
            sevenChord.addChord(1)
            sevenChord.addChord(5)
            sevenChord.addChord(6)

            val chordArray = mutableListOf<ChordClass>(oneChord, twoChord, threeChord, fourChord,
                fiveChord, sixChord, sevenChord)

            var randomChosenChord = (1..7).random()
            chordProgressionSequence = "$chordProgressionSequence $randomChosenChord"
            chordList.add(randomChosenChord)

            repeat(chordAmount - 1) {
                var tempArray = chordArray[randomChosenChord - 1].jumpingChords
                randomChosenChord = tempArray[(0 until tempArray.size).random()]
                chordList.add(randomChosenChord)
                chordProgressionSequence = "$chordProgressionSequence $randomChosenChord"
            }
        }

        var chordProgressionString = printChordsInKey(chordList, globalKeySelection, globalChordFlavor)


        val resultTextView: TextView = findViewById(R.id.textView)
        val resultTextView2: TextView = findViewById(R.id.textView6)
        resultTextView.text = chordProgressionString
        resultTextView2.text = chordProgressionSequence

    }


    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // parent.getItemAtPosition(pos) retrieves the item at the item selected spinner
        var result = parent.getItemAtPosition(pos).toString()

        // Gets the id for the spinner that has been selected
        var activityId = parent.id

        when (activityId) {
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
        "A" -> listOf<String>("A","B","C#","D","E","F#","G#")
        "A#/b♭" -> listOf<String>("b♭","C","D","E♭","F","G", "A")
        "B" -> listOf<String>("B", "C#", "D#", "E", "F#", "G#", "A#")
        "C" -> listOf<String>("C","D","E","F","G","A","B")
        "C#/D♭" -> listOf<String>("C#/D♭","B","C#","D","E","F#","G#")
        "D" -> listOf<String>("D", "E", "F#", "G", "A", "B", "C#")
        "D#/E♭" -> listOf<String>("E♭", "F", "G", "A♭", "B♭", "C", "D")
        "E" -> listOf<String>("E", "F#", "G#", "A", "B", "C#", "D#", "E")
        "F" -> listOf<String>("F", "G", "A", "B♭", "C", "D", "E", "F")
        "F#/G♭" -> listOf<String>("F#", "G#", "A#", "B", "C#", "D#", "F")
        "G" -> listOf<String>("G", "A", "B", "C", "D", "E", "F#")
        else -> listOf<String>("A♭", "B♭", "C", "D♭", "E♭", "F", "G")
    }
    var key = ""
    for (item in chordList) {
        key = keySet[item - 1]
        returnString = if (flavor == "Normal") {
            when (item) {
                1, 4, 5 -> "$returnString $key" + "Maj"
                2, 3, 6 -> "$returnString $key" + "m"
                else -> "$returnString $key" + "dim"
            }
        }

        else {
            when (item) {
                1, 4, 5 -> "$returnString $key" +"Maj7"
                5 -> "$returnString $key" + "Dom7"
                2, 3, 6 -> "$returnString $key" + "m7"
                else -> "$returnString $key" + "dim7"
            }
        }
    }
    return returnString
}


class ChordClass(private val jChordsAmount: Int, private val chordId: Int) {
    val jumpingChords: MutableList<Int> = mutableListOf()

    fun addChord(addedChord: Int) {
        jumpingChords.add(addedChord)
    }
}