package com.deadlinehunters.pokedex.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.model.Pokemon
import com.deadlinehunters.pokedex.model.PokemonResult

class DetailActivity : AppCompatActivity() {

    private val galleryRequest = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.hide()

        val requestQueue = Volley.newRequestQueue(applicationContext)
        getPokemonResults(requestQueue)

//        val changeImg = findViewById<Button>(R.id.changeImgBtn)
//        changeImg.setOnClickListener {
//            if (requestPermission()) {
//                val imgPicker = Intent(Intent.ACTION_PICK)
//                imgPicker.type = "image/*"
//                startActivityForResult(imgPicker, galleryRequest)
//            }
//        }
    }

    private fun getPokemonResults(requestQueue: RequestQueue) {
        val pokemonData: PokemonResult? = intent.getParcelableExtra("pokemon_data")
        val url = pokemonData?.url

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val height = response.getInt("height")
                val weight = response.getInt("weight")
                val id = response.getInt("id")
                val name = response.getString("name")
                val jsonStats = response.getJSONArray("stats")
                val jsonTypes = response.getJSONArray("types")

                val stats = mutableMapOf<String, Int>()
                val types = mutableMapOf<Int, String>()

                (0 until jsonStats.length())
                    .asSequence()
                    .map { jsonStats.getJSONObject(it) }
                    .forEach { stats[it.getJSONObject("stat").getString("name")] = it.getInt("base_stat") }

                (0 until jsonTypes.length())
                    .asSequence()
                    .map { jsonTypes.getJSONObject(it) }
                    .forEach { types[it.getInt("slot")] = it.getJSONObject("type").getString("name") }

                fillView(Pokemon(id, name, height, weight, stats, types))
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun fillView(pokemon: Pokemon) {
        /* Get all views */
        val pokemonImageView = findViewById<ImageView>(R.id.pokemon_details_imageview)
        val pokemonNameTextView = findViewById<TextView>(R.id.pokemon_details_name_textview)
        val pokemonNumberTextView = findViewById<TextView>(R.id.pokemon_details_number_textview)
        val pokemonWeightTextView = findViewById<TextView>(R.id.pokemon_details_weight_label)
        val pokemonHeightTextView = findViewById<TextView>(R.id.pokemon_details_height_label)
        val pokemonHPTextView = findViewById<TextView>(R.id.pokemon_stat_hp_textview)
        val pokemonAttackTextView = findViewById<TextView>(R.id.pokemon_stat_attack_textview)
        val pokemonDefenseTextView = findViewById<TextView>(R.id.pokemon_stat_defense_textview)
        val pokemonSpAttackTextView = findViewById<TextView>(R.id.pokemon_stat_spattack_textview)
        val pokemonSpDefenseTextView = findViewById<TextView>(R.id.pokemon_stat_spdefense_textview)
        val pokemonSpeedTextView = findViewById<TextView>(R.id.pokemon_stat_speed_textview)

        /* get needed variables */
        val pokemonId = String.format("%03d", (pokemon.id))
        val imageResource = resources.getIdentifier(
            "@drawable/pokemon_$pokemonId",
            null,
            packageName
        )
        val stats = pokemon.stats
        val types = pokemon.types

        /* Set views with correct values */
        pokemonImageView.setImageResource(imageResource)
        pokemonNameTextView.text = pokemon.name.capitalize()
        pokemonNumberTextView.text = "No. $pokemonId"
        pokemonWeightTextView.text = (pokemon.weight.toDouble()/10).toString() + " kg"
        pokemonHeightTextView.text = (pokemon.height.toDouble()/10).toString() + " m"
        pokemonHPTextView.text = stats["hp"].toString()
        pokemonAttackTextView.text = stats["attack"].toString()
        pokemonDefenseTextView.text = stats["defense"].toString()
        pokemonSpAttackTextView.text = stats["special-attack"].toString()
        pokemonSpDefenseTextView.text = stats["special-defense"].toString()
        pokemonSpeedTextView.text = stats["speed"].toString()
        
        /* set type views */
        for (i in 1..2) {
            setType(i, types[i])
        }
       
    }

    @SuppressLint("DefaultLocale")
    private fun setType(typeNumber: Int, typeName: String?) {
        val pokemonType1TextView = findViewById<TextView>(R.id.pokemon_details_type1_textview)
        val pokemonType2TextView = findViewById<TextView>(R.id.pokemon_details_type2_textview)
        val drawable = AppCompatResources.getDrawable(this, R.drawable.type_background)
        val colorString: String

        val mTypeName: String = if (typeName.isNullOrEmpty())
            "none"
        else
            typeName

        when (mTypeName) {
            "normal" -> colorString = "#AAAA99"
            "fire" -> colorString = "#FF4422"
            "water" -> colorString = "#3399FF"
            "electric" -> colorString = "#FFCC33"
            "grass" -> colorString = "#77CC55"
            "ice" -> colorString = "#66CCFF"
            "fighting" -> colorString = "#BB5544"
            "poison" -> colorString = "#AA5599"
            "ground" -> colorString = "#DDBB55"
            "flying" -> colorString = "#8899FF"
            "psychic" -> colorString = "#FF5599"
            "bug" -> colorString = "#AABB22"
            "rock" -> colorString = "#BBAA66"
            "ghost" -> colorString = "#6666BB"
            "dragon" -> colorString = "#7766EE"
            "dark" -> colorString = "#775544"
            "steel" -> colorString = "#AAAABB"
            "fairy" -> colorString = "#EE99EE"
            else -> {
                colorString = "#000000"
            }
        }

        when (typeNumber) {
            1 -> {
                pokemonType1TextView.text = mTypeName.toUpperCase()
                drawable?.setTint(Color.parseColor(colorString))
                pokemonType1TextView.background = drawable
            }
            2 -> {
                pokemonType2TextView.text = mTypeName.toUpperCase()
                drawable?.setTint(Color.parseColor(colorString))
                pokemonType2TextView.background = drawable
            }
        }




    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        val img = findViewById<ImageView>(R.id.coolImg)
//
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                galleryRequest -> {
//                    val selectedImage = data?.data
//
//                    try {
//                        selectedImage?.let {
//                            if (Build.VERSION.SDK_INT < 28) {
//                                val bitmap = MediaStore.Images.Media.getBitmap(
//                                    this.contentResolver,
//                                    selectedImage
//                                )
//                                img.setImageBitmap(bitmap)
//                            } else {
//                                val src =
//                                    ImageDecoder.createSource(this.contentResolver, selectedImage)
//                                val bitmap = ImageDecoder.decodeBitmap(src)
//                                img.setImageBitmap(bitmap)
//                            }
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
//    }

    private fun requestPermission(): Boolean {
        if (!isPermissionGranted(READ_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                showPermissionReasonAndRequest(
                    "Permission required",
                    "This permission has to be granted to be able to change backgrounds",
                    READ_EXTERNAL_STORAGE,
                    galleryRequest
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    galleryRequest
                )
            }
        }

        return isPermissionGranted(READ_EXTERNAL_STORAGE)
    }

    private fun Activity.showPermissionReasonAndRequest(
        title: String,
        message: String,
        permission: String,
        requestCode: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            run {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
