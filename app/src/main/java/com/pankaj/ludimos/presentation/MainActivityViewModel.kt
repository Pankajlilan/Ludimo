package com.pankaj.ludimos.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pankaj.ludimos.common.Constants
import com.pankaj.ludimos.model.BallTracking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private var _getBallTrackLiveData = MutableStateFlow(BallTracking())
    var getBallTrackLiveData: StateFlow<BallTracking> = _getBallTrackLiveData

    //  Getting the after parsing and storing the data from JSON file to Ball Tracking model
    fun getBallTrackData(context: Context): MutableStateFlow<BallTracking> {
        _getBallTrackLiveData.value = parseJSONToBallTrackModel(context)
        return _getBallTrackLiveData
    }

    private fun parseJSONToBallTrackModel(context: Context): BallTracking {
        val response = readFromAssets(context);
        val gson = Gson()
        val jsonData: BallTracking = gson.fromJson(response, BallTracking::class.java)
        return jsonData
    }

    private fun readFromAssets(context: Context): String {
        val bufferReader = context.assets.open(Constants.BALL_TRACK).bufferedReader()
        return bufferReader.use {
            it.readText()
        }
    }
}