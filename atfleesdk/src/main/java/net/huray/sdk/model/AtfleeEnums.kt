package net.huray.sdk.model

enum class AtfleeDeviceType(
    val modelName: String,
    val scanName: String,
    val id: Int
) {
    T8("T8", "T8.", 0),
    T9("T9", "T9", 1),
    I_GRIP_X("iGripX", "iGripX_JC780", 2);

    companion object {
        fun fromId(id: Int): AtfleeDeviceType {
            return values().firstOrNull {
                it.id == id
            } ?: throw java.lang.IllegalStateException("$id is not supported device id")
        }
    }
}

enum class AtfleeUserGender {
    MALE,
    FEMALE
}

enum class AtfleeError {
    SDK_NOT_INITIALIZED,
    DEVICE_ALREADY_ADDED,
    WRONG_DEVICE_PARAMETER;
}

enum class AtfleeDeviceState {
    CONNECTED,
    DISCONNECTED;
}
