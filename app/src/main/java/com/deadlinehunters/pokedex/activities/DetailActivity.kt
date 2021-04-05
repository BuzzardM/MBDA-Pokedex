package com.deadlinehunters.pokedex.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.fragments.EditPokemonNameFragment
import com.deadlinehunters.pokedex.data.Pokemon
import com.deadlinehunters.pokedex.data.PokemonResult
import com.deadlinehunters.pokedex.data.PokemonViewModel
import java.io.ByteArrayOutputStream


class DetailActivity : AppCompatActivity(), EditPokemonNameFragment.EditPokemonNameDialogListener {

    private val galleryRequest = 1
    private lateinit var mPokemonViewModel: PokemonViewModel
    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.hide()

        val requestQueue = Volley.newRequestQueue(applicationContext)
        getPokemonResults(requestQueue)

        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
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
                    .forEach {
                        stats[it.getJSONObject("stat").getString("name")] = it.getInt("base_stat")
                    }

                (0 until jsonTypes.length())
                    .asSequence()
                    .map { jsonTypes.getJSONObject(it) }
                    .forEach {
                        types[it.getInt("slot")] = it.getJSONObject("type").getString("name")
                    }

                pokemon = Pokemon(
                    0, id, name, height, weight,
                    stats["hp"]!!,
                    stats["attack"]!!,
                    stats["defense"]!!,
                    stats["special-attack"]!!,
                    stats["special-defense"]!!,
                    stats["speed"]!!,
                    types[1],
                    types[2],
                    null
                )

                fillView(pokemon)
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
        val pokemonId = String.format("%03d", (pokemon.pokemonId))
        val imageResource = resources.getIdentifier(
            "@drawable/pokemon_$pokemonId",
            null,
            packageName
        )

        /* Set views with correct values */
        pokemonImageView.setImageResource(imageResource)
        pokemonNameTextView.text = pokemon.name.capitalize()
        pokemonNumberTextView.text = "No. $pokemonId"
        pokemonWeightTextView.text = (pokemon.weight.toDouble() / 10).toString() + " kg"
        pokemonHeightTextView.text = (pokemon.height.toDouble() / 10).toString() + " m"
        pokemonHPTextView.text = pokemon.hp.toString()
        pokemonAttackTextView.text = pokemon.attack.toString()
        pokemonDefenseTextView.text = pokemon.defense.toString()
        pokemonSpAttackTextView.text = pokemon.spattack.toString()
        pokemonSpDefenseTextView.text = pokemon.spdefense.toString()
        pokemonSpeedTextView.text = pokemon.speed.toString()

        /* set type views */
        setType(pokemon.type1, findViewById(R.id.pokemon_details_type1_textview))
        setType(pokemon.type2, findViewById(R.id.pokemon_details_type2_textview))
    }

    @SuppressLint("DefaultLocale")
    private fun setType(typeName: String?, view: TextView) {
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

        view.text = mTypeName.toUpperCase()
        drawable?.setTint(Color.parseColor(colorString))
        view.background = drawable
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val img = findViewById<ImageView>(R.id.header)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                galleryRequest -> {
                    val selectedImage = data?.data

                    try {
                        selectedImage?.let {
                            val bitmap: Bitmap

                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    selectedImage
                                )
                                img.setImageBitmap(bitmap)
                            } else {
                                val src =
                                    ImageDecoder.createSource(this.contentResolver, selectedImage)
                                bitmap = ImageDecoder.decodeBitmap(src)
                                img.setImageBitmap(bitmap)
                            }
                            addImgToPokemon(bitmap)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

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

    @Suppress("UNUSED_PARAMETER")
    fun backButtonOnClick(view: View) {
        super.onBackPressed()
    }

    fun editBackgroundClick(view: View) {
        if (requestPermission()) {
            val imgPicker = Intent(Intent.ACTION_PICK)
            imgPicker.type = "image/*"
            startActivityForResult(imgPicker, galleryRequest)
        }
    }

    fun editNameClick(view: View) {
        val editFragment = EditPokemonNameFragment()
        editFragment.show(supportFragmentManager, "fragment_edit_pokemon_name")
    }

    override fun onFinishEditDialog(inputText: String?) {
        findViewById<TextView>(R.id.pokemon_details_name_textview).text = inputText.toString()
        pokemon.name = inputText.toString()
    }

    fun favoriteButtonClick(view: View) {
        insertPokemon()
    }

    private fun insertPokemon() {
        if (pokemon.name.isNotBlank())
            mPokemonViewModel.addPokemon(pokemon)
    }

    private fun addImgToPokemon(bitmap: Bitmap) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitArray = bos.toByteArray()

        pokemon.background = bitArray
    }
}
