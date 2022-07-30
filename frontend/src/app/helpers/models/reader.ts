import { Topic } from './topic';
import { User } from './user';
export interface Reader{
    id?: string,
    date?: Date,
    maxDate?: Date,
    reader?: User,
    topic?: Topic,
    state?: string
}