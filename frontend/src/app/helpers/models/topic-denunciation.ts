import { TopicStudent } from './topic-student';
export interface TopicDenunciation {
    id?: string
    date?: Date,
    investigationLine?: InvestigationLine,
    investigationModality?: InvestigationModality,
    projectType?: ProjectType,
    semesterLevel?: string,
    topicStudent?: TopicStudent,
    ciudad?: string,
    articulationTopic?: string
}
export enum InvestigationModality {
    'Emprendimiento' = 'Emprendimiento',
    'Examen Complexivo' = 'Examen Complexivo',
    'Modelo de Negocio' = 'Modelo de Negocio',
    'Producto o Presentación Artística' = 'Producto o Presentación Artística',
    'Propuesta Tecnológica' = 'Propuesta Tecnológica',
    'Proyecto de Investigación' = 'Proyecto de Investigación'
}

export enum ProjectType {
    'Docencia' = 'Docencia',
    'Investigación' = 'Investigación',
    'Vinculación' = 'Vinculación'
}

export enum SemesterLevel {
    'Sexto' = 'Sexto',
    'Séptimo' = 'Séptimo',
    'Octavo' = 'Octavo',
    'Noveno' = 'Noveno',
    'Décimo' = 'Décimo'

}

export enum InvestigationLine {
    'Gestión educacional' = 'Gestión educacional',
    'Educación universitaria' = 'Educación universitaria',
    'Comportamiento humano' = 'Comportamiento humano',
    'Gestión organizacional, emprendimiento, TIC e innovación' = 'Gestión organizacional, emprendimiento, TIC e innovación',
    'Salud y desarrollo humano integral' = 'Salud y desarrollo humano integral',
    'Cultura, arte, medios de comunicación y sociedad' = 'Cultura, arte, medios de comunicación y sociedad',
    'Sustentabilidad y desarrollo sustentable' = 'Sustentabilidad y desarrollo sustentable',
    'Estado, derecho y justicia' = 'Estado, derecho y justicia'
}

