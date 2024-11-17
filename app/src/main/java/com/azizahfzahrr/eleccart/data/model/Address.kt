package com.azizahfzahrr.eleccart.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int = 0,
    val recipientName: String,
    val recipientPhone: String,
    val recipientFullAddress: String,
    val recipientProvince: String,
    val recipientPostalCode: String
) : Parcelable