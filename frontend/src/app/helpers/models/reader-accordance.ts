import { Topic } from 'src/app/helpers/models/topic';
export interface ReaderAccordance {
    id?:string,
    date?:Date,
    accordance?:boolean,
    maxDate?:Date,
    topic?:Topic,
    observations?:string[]
}
