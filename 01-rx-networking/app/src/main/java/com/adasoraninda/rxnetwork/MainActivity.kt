package com.adasoraninda.rxnetwork

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.adasoraninda.rxnetwork.model.Post
import com.adasoraninda.rxnetwork.network.RetrofitInstance
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val postId = 1
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val service by lazy { RetrofitInstance.api }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textId = findViewById<TextView>(R.id.text_id)
        val textTitle = findViewById<TextView>(R.id.text_title)
        val textBody = findViewById<TextView>(R.id.text_body)

        loadPostAsSingle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(this::printLogError) { posting ->
                textId.text = posting.id.toString()
                textTitle.text = posting.title
                textBody.text = posting.body
            }
            .addTo(compositeDisposable)
    }

    private fun loadPostAsSingle(): Single<Post> {
        return Single.create { observer ->
            Thread.sleep(2000)
            getDataFromService(observer)
        }
    }

    private fun printLogError(error: Throwable) {
        Log.i("MainActivity", "${error.message}")
    }

    private fun getDataFromService(observer: SingleEmitter<Post>) {
        service.getPostsById(postId).enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                val posting = response.body()

                if (posting != null) {
                    observer.onSuccess(posting)
                } else {
                    val e = IOException("An Unknown network error occurred")
                    observer.onError(e)
                }
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                observer.onError(t)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}