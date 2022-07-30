import { Injectable, Component } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanDeactivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';


export interface ComponentCanDeactivate {
  canDeactivate: () => boolean
}

@Injectable({
  providedIn: 'root'
})
export class CanDeactivateGuard implements CanDeactivate<ComponentCanDeactivate> {
  canDeactivate(component: ComponentCanDeactivate): boolean {
    //Si el componente implementa el método entonces lo ejecuta,
    //caso contrario permite navegar a otra pantalla
    return component.canDeactivate ? component.canDeactivate() : true;
  }

}
