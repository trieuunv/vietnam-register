import { ICriteriaResult } from "./Criteria";
import { IOwner } from "./Owner";
import { IVehicle } from "./Vehicle";

export interface IInspection {
    id: number
    vehicleId?: number
    centerId?: number
    centerName?: number
    inspectorId?: number
    inspectionDate: string
    nextInspectionDate?: string
    certificateNumber?: string
    result: string
    notes?: string
    fee?: number
    mileage?: number
    status: string
}

export interface IInspectionDetail extends IInspection {
    vehicle: IVehicle
    owner: IOwner
    criteriaResults: ICriteriaResult[]
}

export interface IInspectionListItem {
    id: number
    registrationNumber: string
    inspectorName: string
    inspectionDate: string
    result: string
    status: string
}

export interface IInspectionStatistics {
    totalInspections: number
    passedCount: number
    conditionalCount: number
    failedCount: number
    lastInspectionDate: string | null
    nextInspectionDate: string | null
}




