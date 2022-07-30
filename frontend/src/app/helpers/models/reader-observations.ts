import { Topic } from './topic';
export interface ReaderObservations {
    id?:string,
    mainObservation?:string[],
    descObservation?:string[],
    topic?:Topic,
    date?:Date
}
