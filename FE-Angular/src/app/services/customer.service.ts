import { HttpClient, HttpParams } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { CustomerModel } from "../schemas/customer-model";
import { CustomerDetailModel } from "../schemas/customer-detail-model";

@Injectable({
    providedIn: 'root'
})
export class CustomerService{
    private url = 'http://localhost:8080/api/customers'
    customerChanged = new EventEmitter<void>() // EventEmitter untuk mengeluarkan event ketika data berubah

    constructor(private http: HttpClient){}

    getAllCustomer(currentPage: number, pageSize: number, filterBy: string, filterValue: string, sortBy : string) : Observable<CustomerModel[]>{
        const params = new HttpParams()
            .set('currentPage', currentPage.toString())
            .set('pageSize', pageSize.toString())
            .set('filterBy', filterBy)
            .set('filterValue', filterValue)
            .set('sortBy', sortBy)
        
        return this.http.get<CustomerModel[]>(`${this.url}/get-all`, { params })
    }

    getCustomerById(id:number) : Observable<CustomerDetailModel>{
        return this.http.get<CustomerDetailModel>(`${this.url}/${id}`)
    }

    addCustomer(customer:String, file:File) : Observable<void>{
        const formData = new FormData()
        formData.append('customer', JSON.stringify(customer))
        if(file){
            formData.append('pic', file)
        }
        formData.forEach((value, key) => {
            console.log(`${key}: ${value}`);
        });
        
        return this.http.post<void>(`${this.url}/create`, formData)
    }

    editCustomer(id:number, customer:String, file:File) : Observable<void>{
        const formData = new FormData()
        formData.append('customer', JSON.stringify(customer))
        if(file){
            formData.append('pic', file)
        }
        formData.forEach((value, key) => {
            console.log(`${key}: ${value}`);
        });
        
        return this.http.put<void>(`${this.url}/update/${id}`, formData)
    }

    deleteCustomer(id:number) : Observable<void> {
        return this.http.delete<void>(`${this.url}/delete/${id}`).pipe(tap(()=>{
            this.customerChanged.emit() // Emit event ketika customer dihapus
        }))
    }
}