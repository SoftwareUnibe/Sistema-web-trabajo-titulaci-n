import { User } from './user';
import { Topic } from "./topic";

export interface TopicStudent {
  id?: string,
  assignedDate?: string,
  paymentDenunciation?: PaymentDenunciation,
  student?: User,
  topic?: Topic,
  topicEvaluation?: TopicEvaluation
  thesisSent?: boolean,
  antiPlagiarismLetterSent?: boolean
}

export enum TopicEvaluation {
  'Aprobado' = 'Aprobado',
  'Aprobado con observaciones' = 'Aprobado con observaciones',
  'Reprobado' = 'Reprobado'
}

export enum PaymentDenunciation {
  'No pagado' = 'No pagado',
  'Pagado' = 'Pagado'
}