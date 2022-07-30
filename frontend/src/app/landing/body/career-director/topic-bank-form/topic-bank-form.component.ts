import { ToastService } from './../../../../helpers/services/toast.service';
import { Career } from '../../../../helpers/models/career';
import { TokenService } from '../../../../helpers/services/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TopicService } from '../../../../helpers/services/topic.service';
import { Articulation, Topic } from '../../../../helpers/models/topic';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-topic-bank-form',
  templateUrl: './topic-bank-form.component.html',
  styleUrls: ['./topic-bank-form.component.scss']
})
export class TopicBankFormComponent implements OnInit {
  topic: any = null 
  articulation = Object.keys(Articulation).map(key => ({ label: Articulation[key], value: key }))
  userCareer: Career
  buttonActionName: string = ''
  formTitle: string = ''
  loading: boolean = false
  constructor(private topicSrv: TopicService, private activatedRoute: ActivatedRoute, private tokenSrv: TokenService,
    private toastSrv: ToastService, private router: Router) {
    this.tokenSrv.getUserInfo().subscribe(data => {
      this.topic = {} as Topic
      this.userCareer = data.career
    })
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(topicId => {
      const id = topicId['id']
      if (id !== 'new') {
        this.topicSrv.getTopicByID(id).subscribe(topicData => {
          this.topic = topicData
        })
        this.formTitle = 'Editar Tema'
        this.buttonActionName = 'Guardar Cambios'
      } else {
        this.topic = {} as Topic
        this.formTitle = 'Añadir nuevo tema'
        this.buttonActionName = 'Añadir'
      }
    })
  }

  addTopic(topic: Topic, userCareer: Career) {
    this.handleButtonAction(topic, userCareer)
  }

  handleButtonAction(topic: Topic, userCareer: Career) {
    this.loading = true
    topic.career = userCareer
    if (topic.id) {
      this.topicSrv.updateTopic(topic.id, topic).subscribe(response => {
        let responseMessage = Object.values(response)
        this.toastSrv.showSuccess((responseMessage[0]), 'Guardado')
        this.loading = false
        this.router.navigateByUrl('/home')
      })
    } else {
      topic.twoStudents = false
      this.topicSrv.createTopic(topic).subscribe(response => {
        let responseMessage = Object.values(response)
        this.toastSrv.showSuccess((responseMessage[0]), 'Creado')
        this.loading = false
        this.router.navigateByUrl('/home')
      })
    }
  }
}
