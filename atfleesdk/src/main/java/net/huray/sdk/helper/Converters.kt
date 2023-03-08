package net.huray.sdk.helper

import cn.icomon.icdevicemanager.model.data.ICWeightData
import cn.icomon.icdevicemanager.model.data.ICWeightExtData
import cn.icomon.icdevicemanager.model.other.ICConstant
import net.huray.sdk.model.*

internal fun convertWeightDataFrom(data: ICWeightData): AtfleeWeightData {
    return AtfleeWeightData(
        bmi = data.bmi,
        bmr = data.bmr,
        bodyFatPercent = data.bodyFatPercent,
        boneMass = data.boneMass,
        fatFreeMass = data.ffm,
        moistureMass = data.moistureMass,
        moisturePercent = data.moisturePercent,
        muscleMass = data.muscleMass,
        musclePercent = data.musclePercent,
        minerals = data.minerals,
        physicalAge = data.physicalAge,
        proteinPercent = data.proteinPercent,
        proteinMass = data.proteinMass,
        skeletalMusclePercent = data.smPercent,
        skeletalMuscleMass = data.sm,
        subcutaneousFatPercent = data.subcutaneousFatPercent,
        visceralFat = data.visceralFat,
        waistHipRatio = data.whr,
        weightKg = data.weight_kg,
        extraData = convertExtWeightDataFrom(data.extData),
    )
}

private fun convertExtWeightDataFrom(data: ICWeightExtData?): AtfleeExtraWeightData? {
    return if (data == null) null else AtfleeExtraWeightData(
        leftArm = data.left_arm,
        rightArm = data.right_arm,
        leftLeg = data.left_leg,
        rightLeg = data.right_leg,
        allBody = data.all_body,
        leftArmKg = data.left_arm_kg,
        rightArmKg = data.right_arm_kg,
        leftLegKg = data.left_leg_kg,
        rightLegKg = data.right_leg_kg,
        allBodyKg = data.all_body_kg,
        leftArmMuscle = data.left_arm_muscle,
        rightArmMuscle = data.right_arm_muscle,
        leftLegMuscle = data.left_leg_muscle,
        rightLegMuscle = data.right_leg_muscle,
        allBodyMuscle = data.all_body_muscle,
        leftArmMuscleKg = data.left_arm_muscle_kg,
        rightArmMuscleKg = data.right_arm_muscle_kg,
        leftLegMuscleKg = data.left_leg_muscle_kg,
        rightLegMuscleKg = data.right_leg_muscle_kg,
        allBodyMuscleKg = data.all_body_muscle_kg,
    )
}

internal fun getBfaTypeFrom(deviceType: AtfleeDeviceType): ICConstant.ICBFAType {
    return when (deviceType) {
        AtfleeDeviceType.T8 -> ICConstant.ICBFAType.ICBFATypeT8
        AtfleeDeviceType.T9 -> ICConstant.ICBFAType.ICBFATypeT9
        AtfleeDeviceType.I_GRIP_X -> ICConstant.ICBFAType.ICBFATypeIgrip2
    }
}

internal fun getUserGenderFrom(gender: AtfleeUserGender): ICConstant.ICSexType {
    return if (gender == AtfleeUserGender.MALE) ICConstant.ICSexType.ICSexTypeMale
    else ICConstant.ICSexType.ICSexTypeFemal
}

internal fun convertErrorFrom(code: ICConstant.ICAddDeviceCallBackCode): AtfleeError {
    return when (code) {
        ICConstant.ICAddDeviceCallBackCode.ICAddDeviceCallBackCodeFailedAndSDKNotInit -> {
            AtfleeError.SDK_NOT_INITIALIZED
        }
        ICConstant.ICAddDeviceCallBackCode.ICAddDeviceCallBackCodeFailedAndExist -> {
            AtfleeError.DEVICE_ALREADY_ADDED
        }
        else -> AtfleeError.WRONG_DEVICE_PARAMETER
    }
}

internal fun convertConnectionStateFrom(state: ICConstant.ICDeviceConnectState?): AtfleeDeviceState {
    return when (state) {
        ICConstant.ICDeviceConnectState.ICDeviceConnectStateConnected -> AtfleeDeviceState.CONNECTED
        else -> AtfleeDeviceState.DISCONNECTED
    }
}

internal fun getModelNameFrom(scanName: String, types: List<AtfleeDeviceType>): String {
    return types.firstOrNull {
        it.scanName == scanName
    }?.modelName ?: "Unknown device"
}