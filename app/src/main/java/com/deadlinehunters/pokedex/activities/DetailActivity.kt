package com.deadlinehunters.pokedex.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.deadlinehunters.pokedex.data.Pokemon
import com.deadlinehunters.pokedex.data.PokemonResult
import com.deadlinehunters.pokedex.data.PokemonViewModel
import com.deadlinehunters.pokedex.fragments.EditPokemonNameFragment
import java.io.ByteArrayOutputStream


class DetailActivity : AppCompatActivity(), EditPokemonNameFragment.EditPokemonNameDialogListener {

    private val galleryRequest = 1
    private lateinit var mPokemonViewModel: PokemonViewModel
    private lateinit var pokemon: Pokemon

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        val view =
            (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.activity_detail,
                null)
        prepareAndShowView(view)
    }

    private fun prepareAndShowView(view: View) {
        if (intent.hasExtra("favorite_pokemon")) {
            pokemon = intent.getParcelableExtra("favorite_pokemon")!!
            prepareAndShowWithoutAPICall(view, pokemon)
        } else {
            val pokemonData: PokemonResult? = intent.getParcelableExtra("pokemon_data")
            prepareAndShowWithAPICall(view, Volley.newRequestQueue(applicationContext), pokemonData)
        }
    }

    private fun prepareAndShowWithAPICall(view: View, requestQueue: RequestQueue, pokemonData: PokemonResult?) {
        if (pokemonData == null)
            return

        val request = JsonObjectRequest(
            Request.Method.GET, pokemonData.url, null,
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

                fillView(view, pokemon)
                showView(view)
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }

    private fun prepareAndShowWithoutAPICall(view: View, pokemon: Pokemon) {
        fillView(view, pokemon)
        addFavoriteViewFeatures(view)
        showView(view)
    }

    private fun showView(view: View) {
        setContentView(view)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun fillView(view: View, pokemon: Pokemon) {
        /* Get all views */
        val pokemonImageView = view.findViewById<ImageView>(R.id.pokemon_details_imageview)
        val pokemonNameTextView = view.findViewById<TextView>(R.id.pokemon_details_name_textview)
        val pokemonNumberTextView = view.findViewById<TextView>(R.id.pokemon_details_number_textview)
        val pokemonWeightTextView = view.findViewById<TextView>(R.id.pokemon_details_weight_label)
        val pokemonHeightTextView = view.findViewById<TextView>(R.id.pokemon_details_height_label)
        val pokemonHPTextView = view.findViewById<TextView>(R.id.pokemon_stat_hp_textview)
        val pokemonAttackTextView = view.findViewById<TextView>(R.id.pokemon_stat_attack_textview)
        val pokemonDefenseTextView = view.findViewById<TextView>(R.id.pokemon_stat_defense_textview)
        val pokemonSpAttackTextView = view.findViewById<TextView>(R.id.pokemon_stat_spattack_textview)
        val pokemonSpDefenseTextView = view.findViewById<TextView>(R.id.pokemon_stat_spdefense_textview)
        val pokemonSpeedTextView = view.findViewById<TextView>(R.id.pokemon_stat_speed_textview)

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
        setTypeView(pokemon.type1, view.findViewById(R.id.pokemon_details_type1_textview))
        setTypeView(pokemon.type2, view.findViewById(R.id.pokemon_details_type2_textview))
    }

    private fun addFavoriteViewFeatures(view: View) {
        val deleteButton = view.findViewById<ImageButton>(R.id.pokemon_details_delete_pokemon_button)
        val favoriteButton = view.findViewById<ImageButton>(R.id.pokemon_details_add_favorite_button)
        val pokemonBackgroundImageView = view.findViewById<ImageView>(R.id.pokemon_details_background)

        deleteButton.visibility = VISIBLE
        favoriteButton.setImageResource(R.drawable.ic_save)
        favoriteButton.setOnClickListener {
            mPokemonViewModel.updatePokemon(pokemon)
            Toast.makeText(
                applicationContext,
                "${pokemon.name} has been added to favorites!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (pokemon.background != null && pokemon.background!!.size > 1) {
            val bmp =
                BitmapFactory.decodeByteArray(pokemon.background, 0, pokemon.background!!.size)
            pokemonBackgroundImageView.setImageBitmap(bmp)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setTypeView(typeName: String?, view: TextView) {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.type_background)
        val colorString: String

        val mTypeName: String = if (typeName.isNullOrEmpty())
            "none"
        else
            typeName

        colorString = mPokemonViewModel.getColorStringFromType(mTypeName, this)

        view.text = mTypeName.toUpperCase()
        drawable?.setTint(Color.parseColor(colorString))
        view.background = drawable
    }

    /* gallery permission methods */
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val img = findViewById<ImageView>(R.id.pokemon_details_background)

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

    /* back button click handler */
    @Suppress("UNUSED_PARAMETER")
    fun backButtonOnClick(view: View) {
        super.onBackPressed()
    }

    /* edit background click handler */
    @Suppress("UNUSED_PARAMETER")
    fun editBackgroundClick(view: View) {
        if (requestPermission()) {
            val imgPicker = Intent(Intent.ACTION_PICK)
            imgPicker.type = "image/*"
            startActivityForResult(imgPicker, galleryRequest)
        }
    }

    private fun addImgToPokemon(bitmap: Bitmap) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        val bitArray = bos.toByteArray()
        pokemon.background = bitArray
    }

    /* methods for editing pokemon name */
    @Suppress("UNUSED_PARAMETER")
    fun editNameClick(view: View) {
        val editFragment = EditPokemonNameFragment()
        editFragment.show(supportFragmentManager, "fragment_edit_pokemon_name")
    }

    override fun onFinishEditDialog(inputText: String?) {
        if (inputText.isNullOrEmpty()){
            Toast.makeText(applicationContext, "Name can not be empty.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        findViewById<TextView>(R.id.pokemon_details_name_textview).text = inputText.toString()
        pokemon.name = inputText.toString()
    }

    /* favorite button click handler */
    @Suppress("UNUSED_PARAMETER")
    fun favoriteButtonClick(view: View) {
        mPokemonViewModel.addPokemon(pokemon)
        Toast.makeText(
            applicationContext,
            "${pokemon.name} has been added to favorites!",
            Toast.LENGTH_SHORT
        ).show()
        super.onBackPressed()
    }

    /* delete button click handler */
    @Suppress("UNUSED_PARAMETER")
    fun deleteButtonClick(view: View) {
        mPokemonViewModel.deletePokemon(pokemon.id)
        Toast.makeText(applicationContext, "${pokemon.name} has been deleted!", Toast.LENGTH_SHORT)
            .show()
        super.onBackPressed()
    }
}
