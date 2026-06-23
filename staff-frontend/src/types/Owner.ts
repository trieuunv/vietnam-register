import { IVehicle } from "./Vehicle";

export interface IOwner {
    id: number;
    citizenNumber?: string;
    userId?: number;
    fullName: string;
    dayOfBirth?: string;
    gender?: 'Male' | 'Female' | 'Other';
    email: string;
    phone?: string;
    createdAt?: string;
}

export interface IOwnerDetail extends IOwner {
    vehicles: IVehicle[],
}
