import { TopicStudent } from './topic-student';
export interface TopicApproval {

        id?: string,
        date?: Date,
        documentNumber?: number,
        meetingDate?: string | Date,
        meetingNumber?: string,
        observations?: string[],
        topicStudent?: TopicStudent,
        trato?: string

}
