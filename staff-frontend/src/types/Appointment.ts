import moment from "moment";
import { IOwner } from "./Owner";
import { IVehicle } from "./Vehicle";
import { IInspection } from "./Inspection";

export interface IAppointment {
    id: number,
    appointmentDate: string;
    status: string;
    ownerId?: number;
    createdBy?: number;
    vehicleId?: number;
    centerId?: number;
    confirmationCode: string;
    notes?: string;
    inspection?: IInspection;
}

export interface IAppointmentDetail extends IAppointment {
    owner: IOwner,
    vehicle: IVehicle,
}

export interface IAppointmentListItem {
    id: number;
    appointmentDate: string;
    status: 'completed' | 'pending' | 'canceled' | string;
    confirmationCode: string;
    ownerName: string;
    vehicleRegNumber: string;
    centerName: string;
}

export interface IAppointmentListFilter {
    status?: string | null;
    dateFrom?: moment.Moment | null;
    dateTo?: moment.Moment | null;
    ownerName?: string;
    vehicleRegNumber?: string;
}


