import { Career } from './career';
export interface User {
    id?: string,
    name?:string,
    lastName?:string,
    secondName?:string,
    secondLastName?:string,
    verified?:boolean,
    email?:string,
    ci?:string,
    password?:string,
    roles?:string[] | Role,
    career?:Career|null
    degree?: string
}
export interface Role {
    id:number,
    rolName:string
}
