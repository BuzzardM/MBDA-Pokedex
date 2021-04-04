package com.deadlinehunters.pokedex.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.model.Pokemon
import com.deadlinehunters.pokedex.model.PokemonResult
import com.deadlinehunters.pokedex.model.PokemonStat
import com.deadlinehunters.pokedex.model.PokemonType
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private val galleryRequest = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val changeImg = findViewById<Button>(R.id.changeImgBtn)
        changeImg.setOnClickListener {
            if (requestPermission()) {
                val imgPicker = Intent(Intent.ACTION_PICK)
                imgPicker.type = "image/*"
                startActivityForResult(imgPicker, galleryRequest)
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        getPokemonResults(requestQueue)

        return super.onCreateView(name, context, attrs)
    }

    private fun getPokemonResults(requestQueue: RequestQueue) {
        val pokemonData: PokemonResult? = intent.getParcelableExtra("pokemon_data")
        val url = pokemonData?.url
        val pokemon = mutableListOf<Pokemon>()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val height = response.getInt("height")
                val weight = response.getInt("weight")
                val id = response.getInt("id")
                val name = response.getString("name")
                val jsonStats = response.getJSONArray("stats")
                val jsonTypes = response.getJSONArray("types")

                val stats = mutableListOf<PokemonStat>()
                val types = mutableListOf<PokemonType>()

                (0 until jsonStats.length())
                    .asSequence()
                    .map { jsonStats.getJSONObject(it) }
                    .mapTo(stats) {
                        PokemonStat(
                            it.getInt("base_stat"),
                            it.getJSONObject("stat").getString("name")
                        )
                    }

                (0 until jsonTypes.length())
                    .asSequence()
                    .map { jsonTypes.getJSONObject(it) }
                    .mapTo(types) {
                        PokemonType(
                            it.getInt("slot"),
                            it.getJSONObject("type").getString("name")
                        )
                    }

                pokemon.add(Pokemon(id, name, height, weight, stats, types))
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val img = findViewById<ImageView>(R.id.coolImg)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                galleryRequest -> {
                    val selectedImage = data?.data

                    try {
                        selectedImage?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    selectedImage
                                )
                                img.setImageBitmap(bitmap)
                            } else {
                                val src =
                                    ImageDecoder.createSource(this.contentResolver, selectedImage)
                                val bitmap = ImageDecoder.decodeBitmap(src)
                                img.setImageBitmap(bitmap)
                            }
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
}