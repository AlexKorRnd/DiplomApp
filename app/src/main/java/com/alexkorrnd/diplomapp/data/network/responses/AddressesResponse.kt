package com.alexkorrnd.diplomapp.data.network.responses

class AddressesResponse(val status: String,
                        val results: List<AddressesResponse>?
) {
    class AddressesResponse(val geometry: GeometryResponse?
    ) {
        class GeometryResponse(val location: LocationResponse?
        ) {
            class LocationResponse(val lat: Double,
                                   val lng: Double
            )


        }

    }

    val location: AddressesResponse.GeometryResponse.LocationResponse?
        get() = results?.get(0)?.geometry?.location
}
