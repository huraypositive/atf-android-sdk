package net.huray.sdk.model

data class AtfleeUser(
    val age: Int,
    val height: Int,
    val weight: Double,
    val gender: AtfleeUserGender,
    val deviceType: AtfleeDeviceType
)
