import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AppConstants} from "./app.constants";
import {ErrorResponse} from "./models/ErrorResponse";

@Injectable()
export class ServerErrorInterceptor implements HttpInterceptor {

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        return next.handle(request).pipe(
            catchError(response => {
                if (response.status === 400) {
                    if (response.error.errors.length > 0) {
                        response.error.errors.forEach((error: ErrorResponse) => {
                            console.log(error.code + ": " + error.message);
                            throw new Error(error.message);
                        });
                    }
                }
                return throwError(() => new Error(response));
            })
        )
    }
}