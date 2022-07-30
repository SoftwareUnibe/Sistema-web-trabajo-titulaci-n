import { TopicStudent } from './../../helpers/models/topic-student';
import { User } from './../../helpers/models/user';
import { TutoringHour } from '../../helpers/models/tutorig-hour';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-constancy-tutoring-preview',
  templateUrl: './constancy-tutoring-preview.component.html',
  styleUrls: ['./constancy-tutoring-preview.component.scss']
})
export class ConstancyTutoringPreviewComponent implements OnInit {

  @Input('tutoringList') tutoringList:TutoringHour[];
  @Input('tutor') tutor:User;
  @Input('students') students:TopicStudent[];
  @Input('show') show:boolean
  @Output() showTutoringContancy = new EventEmitter<boolean>();
  actualDate:any
  constructor() { }

  ngOnInit(): void {
    this.actualDate=this.formatDate(new Date)
  }
  closeTutoringConstancy() {
    this.showTutoringContancy.emit(true)
  }
  formatDate(dateToTransform: Date):string {
    let date = new Date(dateToTransform);

    let year = date.getFullYear();
    let month =date.getMonth()+1;
    let day = date.getDate();
    return day + '/' + month + '/' + year
  }
  letPeriodArray(array: TutoringHour[]){
    let periods: string[]=[]
    periods[0]=array[0].period
   for (let element of array) {
     if (element.period!==array[0].period) {
       periods[1]=element.period
     }
   }
   return periods
  }
  letConstancyForPeriodArray(period: string, array: TutoringHour[]){
    return array.filter(value=>
       value.period===period
    )
  }
  getTotalHours(array: TutoringHour[]){
    let total: number=0
    array.forEach(element=>{
      total=total+element.hours
    })
    return total
  }
}
