import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'environments/environment';
import { HttpClient } from '@angular/common/http';
import { AlertService } from './alert.service';
import { Observable } from 'rxjs';
import { WeatherInterface } from '@/models/weather.interface';
import { StationInterface } from '@/models/station.interface';

@Injectable({
  providedIn: 'root'
})
export class WeatherDataService {
  public apiUrl: string;

  constructor(private toastr: ToastrService, private http: HttpClient, private alert: AlertService) {
    this.apiUrl = environment.apiUrl
  }

  loadDataStation(): Observable<StationInterface[]> {
    try {
      return this.http.get<StationInterface[]>(this.apiUrl+'/station/')
    } catch (error) {
      this.toastr.error(error.message);
    }
  }


  loadDataWeather(stationId){
    try {
      return this.http.get<any>(this.apiUrl+'/weather/'+stationId)
    } catch (error) {
      this.toastr.error(error.message);
    }
  }

  chargeDataWeather(archivo){
    try{
      console.log(archivo)
      let url = 'https://xqki41ej46.execute-api.us-east-1.amazonaws.com/dev/meteodata/csv-upload';
      return this.http.post<any>(url,archivo)
    }catch (error){
      this.toastr.error(error.message);
    }
  }
}



