package com.varsitycollege.xbcad.healthbuddies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.varsitycollege.xbcad.healthbuddies.databinding.ActivityBarcodescannerBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.spi.FileTypeDetector

class barcodescanner : AppCompatActivity() {
    private lateinit var binding: ActivityBarcodescannerBinding
    private lateinit var barcodeDetector: BarcodeDetector
    lateinit var cameraSource: CameraSource

    private var barcodeProcessed = false

    var intentData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_barcodescanner)

        binding = ActivityBarcodescannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun iniBc(){
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            //.setFacing(CameraSource.CAMERA_FACING_FRONT)
            .build()

        binding.surfaceView!!.holder.addCallback(object :SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                    cameraSource.start(binding.surfaceView!!.holder)
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object :Detector.Processor<Barcode>{
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                if (!barcodeProcessed) {
                    val barcodes = detections.detectedItems
                    if (barcodes.size() != 0) {
                        // Barcode exists
                        binding.txtBarcodeValue!!.post {
                            binding.btnAction!!.text = "SEARCH ITEM"
                            intentData = barcodes.valueAt(0).displayValue
                            // The value
                            binding.txtBarcodeValue.setText(intentData)


                            val productService = ProductService()
                            val barcode = intentData
                            productService.getProductByBarcode(barcode, object : Callback<ProductResponse> {

                                override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                                    if (response.isSuccessful) {
                                        val product = response.body()?.product
                                        val productName = product?.product_name ?: "Product name not available"
                                        val brand = product?.brands ?: "Brand not available"
                                        val servingSize = product?.serving_size ?: "Serving size not available\n(Value is per 100g)"
                                        val ingredients = product?.ingredients_text ?: "Ingredients not available"
                                        val allergens = product?.allergens_from_ingredients ?: "Allergens not available"
                                        val imageUrl = product?.image_url // Get the image URL

                                        val nutriments = product?.nutriments

                                        // Check if nutriments is not null before accessing its properties
                                        val energyValue = nutriments?.energy
                                        val energyUnit = nutriments?.energy_unit


                                        if((productName.equals("Product name not available"))== false) {
                                            Toast.makeText(
                                                this@barcodescanner,
                                                "The barcode is: $intentData",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            // Start DisplayActivity with the product information
                                            val intent =
                                                Intent(this@barcodescanner, displayfood::class.java)
                                            intent.putExtra("productName", productName)
                                            intent.putExtra("brand", brand)
                                            intent.putExtra("servingSize", servingSize)
                                            intent.putExtra("energyValue", energyValue)
                                            intent.putExtra("ingredients", ingredients)
                                            intent.putExtra("allergens", allergens)
                                            intent.putExtra("energyUnit", energyUnit)
                                            intent.putExtra(
                                                "imageUrl",
                                                imageUrl
                                            ) // Pass the image URL to the next activity
                                            startActivity(intent)
                                        }
                                        else{
                                            Toast.makeText(this@barcodescanner, "Unable to find product. Try Again.", Toast.LENGTH_SHORT).show()
                                        }

                                    } else {
                                        Toast.makeText(this@barcodescanner, "Failed to retrieve product information", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                                    Toast.makeText(this@barcodescanner, "Unable to find product. Try Again.", Toast.LENGTH_SHORT).show()
                                }
                            })

                            finish()
                            barcodeProcessed =
                                true // Set the flag to true to indicate barcode has been processed
                        }
                    }
                }
            }

        })
    }
    override  fun onPause(){
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        iniBc()
    }
}

data class ProductResponse(
    val product: Product
)

data class Product(
    val product_name: String,
    val brands: String?,
    val serving_size: String?,
    val ingredients_text: String?,
    val allergens_from_ingredients: String?,
    val image_url: String?,
    val nutriments: Nutriments? // Add a field for nutriments
)

data class Nutriments(

    val energy: Double?,
    val energy_unit: String?
)



interface OpenFoodFactsApiService {
    @GET("api/v0/product/{barcode}.json")
    fun getProductByBarcode(@Path("barcode") barcode: String): Call<ProductResponse>
}


class ProductService {
    private val apiService: OpenFoodFactsApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(OpenFoodFactsApiService::class.java)
    }

    fun getProductByBarcode(barcode: String, callback: Callback<ProductResponse>) {
        val call = apiService.getProductByBarcode(barcode)
        call.enqueue(callback)
    }
}




