import { Faculty } from './faculty';
export interface Career {
    id?: string,
    degree:string,
    name:string,
    faculty:Faculty,
    hasProduct:boolean
}
