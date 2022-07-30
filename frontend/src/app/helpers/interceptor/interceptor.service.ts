import { Router } from '@angular/router';
import { ToastService } from './../services/toast.service';
import { JwtDTO } from './../models/jwt-dto';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './../services/auth.service';
import { TokenService } from './../services/token.service';
import { catchError, concatMap } from 'rxjs/operators';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

  constructor(private tokenService: TokenService,
    private toastSrv:ToastService, private router:Router) {

  }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError((err: HttpErrorResponse) => {
      if (err.status === 403) {
        this.router.navigateByUrl("/home");
        this.toastSrv.clear()
        this.toastSrv.showInfo('Usuario no autorizado','No autorizado')
        return next.handle(req);
      }
      if (err.status === 401) {
        this.tokenService.logOut();
        this.toastSrv.clear()
        this.toastSrv.showInfo('Su sesión ha caducado','Inicie Sesión nuevamente')
        return next.handle(req);
      } else {
        return throwError(err);
      }
    }));
  }
}
export const intercerptorProvider = [{ provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true }]
