import { HttpClient, HttpParams } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { OrderModel } from "../pages/order/order-model";
import { OrderDetailModel } from "../pages/order-detail/order-detail-model";

@Injectable({
    providedIn: 'root' 
})
export class OrderService{
    private url = 'http://localhost:8080/api/orders'
    orderChanged = new EventEmitter<void>() // EventEmitter untuk mengeluarkan event ketika data berubah

    constructor(private http: HttpClient){}

    getAllOrder(currentPage: number, pageSize: number, filterBy: string, filterValue: string, sortBy : string) : Observable<OrderModel[]>{
        const params = new HttpParams()
            .set('currentPage', currentPage.toString())
            .set('pageSize', pageSize.toString())
            .set('filterBy', filterBy)
            .set('filterValue', filterValue)
            .set('sortBy', sortBy)
        
        return this.http.get<OrderModel[]>(`${this.url}/get-all`, { params })
    }

    getOrderById(id:number) : Observable<OrderDetailModel>{
        return this.http.get<OrderDetailModel>(`${this.url}/${id}`)
    }

    addOrder(order:String) : Observable<void>{
        const formData = new FormData()
        formData.append('order', JSON.stringify(order))
        
        return this.http.post<void>(`${this.url}/create`, formData)
    }

    deleteOrder(id:number) : Observable<void> {
        return this.http.delete<void>(`${this.url}/delete/${id}`).pipe(tap(()=>{
            this.orderChanged.emit() // Emit event ketika order dihapus
        }))
    }

    downloadReportOrder(filterBy: string, filterValue: string, sortBy: string) {
        const params = new HttpParams()
          .set('filterBy', filterBy)
          .set('filterValue', filterValue)
          .set('sortBy', sortBy);
      
        return this.http.post(`${this.url}/download-report`, null, {
          params,
          responseType: 'blob' as 'json' // responseType should be blob
        });
    }
}