package com.example.frontend2.presentation.navigation

sealed class Screen(val route: String) {

    data object Login : Screen("login")
    data object Register : Screen("register")

    data object Home : Screen("home")

    data object AppointmentManager : Screen("appointment_manager")
    data object AppointmentCreate : Screen("appointment_create/{centerId}")
    data object AppointmentDetail : Screen("appointment_detail/{appointmentId}")

    data object InspectionCenterLookUp : Screen("inspection_center_look_up")
    data object InspectionCenterDetail : Screen("inspection_center_detail/{inspectionCenterId}")

    data object InspectionManager : Screen("inspection_manager")
    data object InspectionDetail : Screen("inspection_detail/{inspectionId}")

    data object VehicleManager : Screen("vehicle_manager")
    data object VehicleRegister : Screen("vehicle_register")
    data object VehicleEdit : Screen("vehicle_edit/{vehicleId}") {
        fun createRoute(vehicleId: Int) = "vehicle_edit/$vehicleId"
    }

    data object ViolationLookup : Screen("violation_lookup")

    data object SettingDetail : Screen("setting_detail")

    data object ProfileDetail : Screen("profile_detail")

    data object CitizenCard : Screen("citizen_card")
}