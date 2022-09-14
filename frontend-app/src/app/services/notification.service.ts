import {Injectable} from '@angular/core'
import {Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    private readonly errorsSubject$ = new Subject<string>();

    public errors$() {
        return this.errorsSubject$.asObservable();
    }

    public showError(message: string): void {
        this.errorsSubject$.next(message);
    }
}