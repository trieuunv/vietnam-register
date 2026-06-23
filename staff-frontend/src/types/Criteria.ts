export interface ICriteriaResult {
    id: number;
    inspectionId: number;
    criteriaId: number;
    criteriaName: string;
    inspectorId: number;
    result: string;
    notes?: string;
}
