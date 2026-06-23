import { IInspection, IInspectionStatistics } from "./Inspection";
import { IOwner } from "./Owner";

export interface IVehicle {
    id: number
    registrationNumber: string
    ownerId?: number
    vehicleTypeId?: number
    vehicleTypeName: string
    chassisNumber?: string
    engineNumber?: string
    brand?: string
    model?: string
    manufactureYear?: number
    color?: string
    seatCount?: number
    firstRegistrationDate?: string
    purposeOfUse?: string
    registrationStatus?: string
    status?: string

    // Inspection Info
    lastInspectionDate?: string // result: passed, conditional
    nextInspectionDate?: string // result: passed, conditional
}

export interface IVehicleDetail extends IVehicle {
    owner: IOwner
    inspections: IInspection[]
    statistics?: IInspectionStatistics
}

export interface IVehicleListItem {
    id: number;
    registrationNumber: string;
    status: string;
    registrationStatus: string;
    ownerName: string | null;
    vehicleTypeName: string | null;
    inspectedAt: string | null;
}

