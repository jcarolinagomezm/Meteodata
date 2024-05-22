import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpRequest, HttpHandler, HttpErrorResponse,} from '@angular/common/http';
import { Observable, throwError  } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Injectable()
export class Interceptor implements HttpInterceptor {

    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = localStorage.getItem('token') || '';
        const modifiedRequest = request.clone({
            setHeaders: {
                Authorization: 'Bearer '+token,
                'Content-Type': 'application/json',
            },
        });
        return next.handle(modifiedRequest)
        // return next.handle(modifiedRequest).pipe(
        //     catchError((error: HttpErrorResponse) => {
        //         if (error.status === 403) {
        //             Swal.fire({
        //                 title: 'Cago rey'
        //             })
        //         }
        //         return throwError('cago rey');
        //     })
        // );
    }
}