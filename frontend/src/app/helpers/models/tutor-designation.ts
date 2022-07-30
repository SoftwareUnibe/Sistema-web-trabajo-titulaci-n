import { TopicStudent } from './topic-student';
import { User } from './user';
export interface TutorDesignation {
    id?: string,
    date?: Date,
    teacher?: User,
    topicStudent?: TopicStudent
}
