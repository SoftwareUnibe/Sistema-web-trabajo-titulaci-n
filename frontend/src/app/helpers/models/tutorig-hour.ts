import { Topic } from './topic';
export interface TutoringHour {
    id?:string,
    topic?:Topic | any,
    number?:number,
    activity?:string,
    date?:Date,
    hours?:number,
    period?:string
}
