package com.example.dogimages

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogimages.network.DogService
import com.example.dogimages.ui.ImageListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var dogService: DogService
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var imgListView: RecyclerView
    lateinit var repeatingProcess: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.imgListView = findViewById(R.id.list)
        this.dogService = DogService.create()
        this.compositeDisposable = CompositeDisposable()
        this.repeatingProcess = Observable
                .interval(1, TimeUnit.SECONDS)
                .map { getConnectionType(applicationContext) }
                .subscribe { networkStatus ->
                    if (networkStatus != 0) {
                        loadDogImages()
                    }
                }
    }

    private fun loadDogImages() {
        this.repeatingProcess.dispose()
        val disposable = dogService.randomDogs(50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.message }
                .subscribe {
                    println(it.joinToString(separator = ", "))
                    imgListView.adapter = ImageListAdapter(
                            it, getRandomDogNames(it.size), getRandomDogCharacteristics(
                            it.size
                    )
                    )
                    imgListView.layoutManager = GridLayoutManager(this, 2)
                }
        this.compositeDisposable.add(disposable)
    }

    private fun getRandomDogCharacteristics(size: Int): List<String> {
        val characteristicsArray = resources.getStringArray(R.array.dog_characteristics)
        val nameList = mutableListOf<String>()
        for (i in 1..size) {
            nameList.add(characteristicsArray[(0 until characteristicsArray.size - 1).random()])
        }
        return nameList
    }

    /**
     * Make a list of dog names long enough to give each dog a name.
     * @param size of the required List of names
     * @return List<String>
     */
    private fun getRandomDogNames(size: Int): List<String> {
        val nameArray = resources.getStringArray(R.array.dog_names)
        val nameList = mutableListOf<String>()
        for (i in 1..size) {
            nameList.add(nameArray[(0 until nameArray.size - 1).random()])
        }
        return nameList
    }

    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable.dispose()
    }

    /**
     * Returns connection type either 0, 1 or 2
     * 0 : no connection available
     * 1 : connected to mobile data
     * 2 : connected to WiFi
     * @param context
     * @return connection type
     */
    private fun getConnectionType(context: Context): Int {
        var result = 0
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = 2
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = 1
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = 3
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            result = 2
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            result = 1
                        }
                        ConnectivityManager.TYPE_VPN -> {
                            result = 3
                        }
                    }
                }
            }
        }
        return result
    }
}