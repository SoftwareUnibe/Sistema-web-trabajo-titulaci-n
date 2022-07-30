import { Career } from './career';
export interface Topic {
    articulation?: Articulation,
    description?: string,
    id?: string,
    name?: string,
    topicStatus?: string,
    career?: Career,
    twoStudents:boolean,
    hasReader?: boolean
}

export enum Articulation {
    'Investigación' = 'Investigación',
    'Prácticas Laborales' = 'Prácticas Laborales',
    'Proyecto Integrador de Saberes' = 'Proyecto Integrador de Saberes',
    'Prácticas de Servicio Comunitario' = 'Prácticas de Servicio Comunitario'
  }
  
