import { FinalDegreeCalendar } from './final-degree-calendar';
import { User } from './user';
import { Career } from './career';
export interface CalendarDetail {
    id?: string,
    date?: Date,
    hour?: Date,
    career?: Career,
    student?: User,
    tribunalBoss?: User,
    tutor?: User,
    reader?: User,
    secretary?: string,
    finalDegreeCalendar?: FinalDegreeCalendar
}
