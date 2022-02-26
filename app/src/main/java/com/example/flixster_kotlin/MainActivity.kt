package com.example.flixster_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=306c9063943f1d014159213c96b45e72"
class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

//    Define a model class to use as the data source - Done
//    Add a RecyclerView to your activity to display the items - Done
//    Create a custom row layout XML file to visualize the item - Done
//    Create a RecyclerView.Adapter and ViewHolder to render the item  Done
//    Bind the adapter to the data source to populate the RecyclerView

    val movieAdapter = MovieAdapter(this, movies)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.rvMovies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(URL, object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "OnFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.e(TAG, "OnSuccess JSON data $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged()
                    Log.i(TAG, "MovieList $movies")
                } catch (e: JSONException) {
                    Log.e(TAG, "Error found: $e" )
                }

            }

        })
    }
}