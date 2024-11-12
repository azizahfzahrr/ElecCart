package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("users")
    val users: List<User?>?
) {
    data class User(
        @SerializedName("address")
        val address: Address?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: Name?,
        @SerializedName("password")
        val password: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("username")
        val username: String?
    ) {
        data class Address(
            @SerializedName("city")
            val city: String?,
            @SerializedName("geolocation")
            val geolocation: Geolocation?,
            @SerializedName("number")
            val number: String?,
            @SerializedName("street")
            val street: String?,
            @SerializedName("zipcode")
            val zipcode: String?
        ) {
            data class Geolocation(
                @SerializedName("lat")
                val lat: Double?,
                @SerializedName("long")
                val long: Double?
            )
        }

        data class Name(
            @SerializedName("firstname")
            val firstname: String?,
            @SerializedName("lastname")
            val lastname: String?
        )
    }
}