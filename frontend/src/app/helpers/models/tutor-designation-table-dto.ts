import { User } from 'src/app/helpers/models/user';
import { TopicStudent } from './topic-student';
export interface TutorDesignationTableDto {
    id?: string,
    date?: Date,
    teacher?: User,
    topicStudent?: TopicStudent
}
