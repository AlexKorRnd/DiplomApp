package com.alexkorrnd.diplomapp.presentation.maps

import android.util.Log
import com.alexkorrnd.diplomapp.MainApp
import com.alexkorrnd.diplomapp.R
import com.alexkorrnd.diplomapp.data.network.RestApi
import com.alexkorrnd.diplomapp.data.network.responses.AddressesResponse
import com.alexkorrnd.diplomapp.data.network.services.MapsService
import com.alexkorrnd.diplomapp.presentation.BasePresenter
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MapsPresenter(private val view: MapsPresenter.View): BasePresenter {

    interface View {
        fun onCoordinatesLoaded(locationResponse: AddressesResponse.AddressesResponse.GeometryResponse.LocationResponse)
        fun onShowError(message: String)
    }

    companion object {
        private val TAG = MapsPresenter::class.java.simpleName
    }

    private val mapsService: MapsService = RestApi.createService(MapsService::class.java)


    fun loadCoordinates(address: String) {
        mapsService.loadCoordinates(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleSubscriber<AddressesResponse?>() {
                    override fun onSuccess(t: AddressesResponse?) {
                        if (t == null || t.location == null) {
                            view.onShowError(MainApp.instance.getString(R.string.maps_error_default))
                        } else {
                            view.onCoordinatesLoaded(t.location!!)
                        }
                    }

                    override fun onError(error: Throwable?) {
                        Log.d(TAG, error?.message, error)
                        view.onShowError(MainApp.instance.getString(R.string.maps_error_default))
                    }
                })
    }

    override fun start() {

    }

    override fun stop() {
    }
}
