import { TopicStudent } from './topic-student';
export interface TopicProposal {
    id?: string,
    objectiveGeneral?: string,
    objectivesSpecific?: string[],
    bibliographicReferences?: string[],
    studyJustification?: string,
    topicDescription?: string,
    topicStudent?: TopicStudent,
    scope?:string
}
