package com.example.frontend2.data.network.dto.response

data class InspectionsListResponse(
    val id: Int,
    val vehicleId: Int,                         // Mã phương tiện
    val centerId: Int,                          // Mã trung tâm đăng kiểm
    val inspectorId: Int,                       // Mã người kiểm định
    val inspectionDate: String,                 // Ngày đăng kiểm
    val nextInspectionDate: String,             // Ngày đăng kiểm tiếp theo
    val certificateNumber: String,              // Số chứng nhận đăng kiểm
    val result: String?,                        // Kết quả kiểm định
    val notes: String,                          // Ghi chú
    val fee: String,                            // Phí kiểm định
    val mileage: Int?,                          // Số km đã đi
    val status: String,                         // Trạng thái kiểm định
    val createdAt: String,                      // Ngày tạo
    val updatedAt: String,                      // Ngày cập nhật
    val deletedAt: String?,                     // Ngày xóa
    val center: CenterData,                     // Thông tin trung tâm đăng kiểm
    val vehicle: VehicleData                    // Thông tin phương tiện
)

data class InspectionByIdResponse(
    val id: Int,
    val vehicleId: Int,
    val centerId: Int,
    val inspectorId: Int,
    val inspectionDate: String,
    val nextInspectionDate: String,
    val certificateNumber: String,
    val result: String?,
    val notes: String?,
    val fee: String,
    val mileage: Int?,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val criteriaResults: List<CriteriaResult>,  // Danh sách kết quả kiểm tra tiêu chí
    val center: CenterData,
    val vehicle: VehicleData
)

data class CenterData(
    val id: Int,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val directorName: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

data class VehicleData(
    val id: Int,
    val ownerId: Int,
    val vehicleTypeId: Int,
    val registrationNumber: String,
    val chassisNumber: String,
    val engineNumber: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val color: String,
    val seatCount: Int,
    val firstRegistrationDate: String,
    val purposeOfUse: String,
    val registrationStatus: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

data class CriteriaResult(
    val id: Int,
    val inspectionId: Int,                      // Mã đăng kiểm
    val criteriaId: Int,                        // Mã tiêu chí
    val result: String,                         // Kết quả kiểm tra
    val notes: String,                          // Ghi chú
    val inspectorId: Int,                       // Mã người kiểm định
    val createdAt: String,                      // Ngày tạo
    val updatedAt: String,                      // Ngày cập nhật
    val deletedAt: String?                      // Ngày xóa
)