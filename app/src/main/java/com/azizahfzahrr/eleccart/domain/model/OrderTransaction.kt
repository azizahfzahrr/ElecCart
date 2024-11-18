package com.azizahfzahrr.eleccart.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderTransaction (
    val id: String,
    val status: String,
    val totalPrice: Int,
): Parcelable