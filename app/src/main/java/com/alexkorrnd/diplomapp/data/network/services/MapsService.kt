package com.alexkorrnd.diplomapp.data.network.services

import com.alexkorrnd.diplomapp.data.network.responses.AddressesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

interface MapsService {

    @GET("maps/api/geocode/json")
    fun loadCoordinates(@Query("address") address: String): Single<AddressesResponse>
}
