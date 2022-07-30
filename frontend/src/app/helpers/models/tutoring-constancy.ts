import { Topic } from 'src/app/helpers/models/topic';
import { User } from 'src/app/helpers/models/user';
export interface TutoringConstancy {
    id?:string,
    generationDate?:Date,
    tutor:User,
    topic:Topic
}
