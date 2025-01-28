package com.google.jetstream.presentation.components.shim

import android.content.res.Configuration

enum class FormFactor {
    Undefined,
    Normal,
    Desk,
    Car,
    Tv,
    Appliance,
    Watch,
    VRHeadset,
}

enum class NightMode {
    Undefined,
    Yes,
    No,
}

class UiMode(val formFactor: FormFactor, val nightMode: NightMode) {

    companion object {
        fun from(configuration: Configuration): UiMode = from(configuration.uiMode)

        private fun from(uiMode: Int): UiMode {
            val nightModeIntValue = uiMode and Configuration.UI_MODE_NIGHT_MASK
            val nightMode = nightMode(nightModeIntValue)
            val formFactor = formFactor(uiMode - nightModeIntValue)
            return UiMode(formFactor, nightMode)
        }

        private fun nightMode(value: Int): NightMode {
            return when (value) {
                Configuration.UI_MODE_NIGHT_YES -> NightMode.Yes
                Configuration.UI_MODE_NIGHT_NO -> NightMode.No
                else -> NightMode.Undefined
            }
        }

        private fun formFactor(value: Int): FormFactor {
            return when (value) {
                Configuration.UI_MODE_TYPE_NORMAL -> FormFactor.Normal
                Configuration.UI_MODE_TYPE_DESK -> FormFactor.Desk
                Configuration.UI_MODE_TYPE_CAR -> FormFactor.Car
                Configuration.UI_MODE_TYPE_TELEVISION -> FormFactor.Tv
                Configuration.UI_MODE_TYPE_APPLIANCE -> FormFactor.Appliance
                Configuration.UI_MODE_TYPE_WATCH -> FormFactor.Watch
                Configuration.UI_MODE_TYPE_VR_HEADSET -> FormFactor.VRHeadset
                else -> FormFactor.Undefined
            }
        }
    }

}

