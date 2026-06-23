import moment, { Moment } from 'moment';

/**
 * Convert Moment object to string 'YYYY-MM-DD'
 */
export const momentToString = (date?: Moment | null): string | null => {
    return date ? date.format('YYYY-MM-DD') : null;
};

/**
 * Convert string 'YYYY-MM-DD' to Moment object
 */
export const stringToMoment = (dateStr?: string | null | undefined): Moment | undefined => {
    return dateStr ? moment(dateStr, 'YYYY-MM-DD') : undefined;
};


