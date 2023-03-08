# Atflee Device SDK 매니저
앳플리 체성분계 SDK의 사용을 쉽게 하도록 도와주는 라이브러리

## SDK 파일
- [libs](/atfleesdk/libs) 폴더 안에 aar포멧의 라이브러리 파일로 추가되어있다.
- [구글드라이브](https://drive.google.com/drive/folders/1ytOjlsaynPEWYcKVjd5gXmwRJltnL3yd?usp=sharing_eil_m&ts=63e33b0f): 업체에서 제공받은 예제 원본 파일

## Jitpack 배포
해당 라이브러리는 jitpack으로 배포되었으며, 아래의 과정을 통해 새 버전을 배포할 수 있다.
- 깃허브 저장소에서 새로운 태그를 지정한다.
- [Jitpack](https://jitpack.io/) 홈페이지의 `Git repo url` 검색창에 `huraypositive/atflee-android-sdk`를 넣어 repo를 찾은 후, 새로 추가한 태그의 버전을 빌드한다.
- 안드로이드 프로젝트에서 새로 빌드된 버전으로 업데이트하여 정상 배포되었는지를 테스트한다.

## 적용 기기 목록
- T8
- T9
- iGripX (아이그립X)

## 특이사항
- 앳플리 기기는 측정 기록을 저장하지 않음

## 의존성 추가
```gradle
// in your root build.gradle
allprojects {
    repositories {
        /*..*/.
        maven { url 'https://jitpack.io' }
    }
}

// in your app-level build.gradle
dependencies {
    implementation 'com.github.huraypositive:atflee-android-sdk:$version'
}
```

## Sample Code
- [sample](/sample) 모듈 참고

## 사용 방법

## Manifest.xml 권한 추가
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

## AtfleeManager 객체 초기화
```kotlin
private val atfleeManager = AtfleeManager.getInstance(context, atfleeCallback)
```

## AtfleeManager API 사용법
### 1.기본 함수들
```kotlin
// 스캔 시작 (emptyList를 넘기면 모든 타입의 앳플리 기기를 스캔)
fun startScan(deviceTypes: List<AtfleeDeviceType>)

// 스캔 중지
fun stopScan()

// 측정모드 시작 (기기를 연결하고 측정 모드로 진입)
fun startMeasureMode(deviceAddress: String, deviceType: AtfleeDeviceType, user: AtfleeUser)

// 연결 끊기
fun disconnect(address: String)

// AtfleeManager 해제
fun dispose()
```

### 3.AtfleeCallback 인터페이스
AtfleeManager를 사용할 클래스에 인터페이스를 구현한다.
```kotlin
class DemoViewModel : ViewModel(), AtfleeCallback {

    // 스캔된 기기의 목록을 업데이트
    override fun onScanned(devices: List<DiscoveredAtfleeDevice>) {
        // 이 때 사용할 기기의 address를 안드로이드 기기에 저장해두었다가 startMeasure() 호출 시 사용하길 권장함 
    }

    // 기기의 연결 상태를 업데이트
    override fun onDeviceConnectionChanged(state: AtfleeDeviceState) { /*..*/ }

    // 체중계에서 감지된 실시간 몸무게 값 수신 (분석 전)
    override fun onReceiveUnstableWeight(weight: Double) { /*..*/ }

    // 체성분 분석 시작 알림
    override fun onStartMeasureWeight() { /*..*/ }

    // 측정 완료된 상세 체성분 데이터를 업데이트
    override fun onCompleteMeasureWeight(data: AtfleeWeightData) {  /*..*/ }

    // 기기 연결 실패 시 에러 코드 업데이트
    override fun onError(error: AtfleeError) { /*..*/ }
    
}
```

### 4. AtfleeWeightData
```kotlin
data class AtfleeWeightData(
    val bmi: Double,  // (BMI)
    val bmr: Int, // 기초대사량 (kcal)
    val bodyFatPercent: Double, // 체지방률(%)
    val boneMass: Double, // 골질량(kg)
    val fatFreeMass: Double, // 제지방체중(kg)
    val moistureMass: Double, // 체수분(kg)
    val moisturePercent: Double, // 체수분율(%)
    val muscleMass: Double, // 근육(kg)
    val musclePercent: Double, // 근육율 (%)
    val minerals: Double, // 무기질(kg)
    val physicalAge: Double, // 신체나이
    val proteinPercent: Double, // 체단백질율(%)
    val proteinMass: Double, // 체단백질(kg)
    val skeletalMuscleMass: Double, // 골격근량(kg)
    val skeletalMusclePercent: Double, // 골격근율(%)
    val subcutaneousFatPercent: Double, // 피하지방율(%)
    val visceralFat: Double, // 내장지방지수
    val waistHipRatio: Double, // 복부지방율(%)
    val weightKg: Double, // 몸무게(kg)
    val extraData: AtfleeExtraWeightData? = null, // 아이그립X 모델 사용 시 들어오는 추가 데이터
)
```

### 5. AtfleeExtraWeightData
```kotlin
data class AtfleeExtraWeightData(
    val leftArm: Double, // 왼팔 체지방율(%) 
    val rightArm: Double, // 오른팔 체지방율(%) 
    val leftLeg: Double, // 왼발 체지방율(%) 
    val rightLeg: Double, // 오른발 체지방율(%) 
    val allBody: Double, // 몸통 체지방율(%) 
    val leftArmKg: Double, // 왼팔 체지방
    val rightArmKg: Double, // 오른팔 체지방
    val leftLegKg: Double, // 왼발 체지방
    val rightLegKg: Double, // 오른발 체지방
    val allBodyKg: Double, // 몸통 체지방
    val leftArmMuscle: Double, // 왼팔 골격근
    val rightArmMuscle: Double, // 오른팔 골격근
    val leftLegMuscle: Double, // 왼발 골격근
    val rightLegMuscle: Double, // 오른발 골격근
    val allBodyMuscle: Double, // 몸통 골격근
    val leftArmMuscleKg: Double, // 왼팔 골격근량(kg) 
    val rightArmMuscleKg: Double, // 오른팔 골격근량(kg) 
    val leftLegMuscleKg: Double, // 왼발 골격근량(kg) 
    val rightLegMuscleKg: Double, // 오른발 골격근량(kg) 
    val allBodyMuscleKg: Double, // 몸통 골격근량(kg) 
)
```
