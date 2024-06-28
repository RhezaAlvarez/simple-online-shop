import { HttpClient, HttpParams } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { ItemModel } from "../pages/item/item-model";
import { ItemDetailModel } from "../pages/item-detail/item-detail-model";

@Injectable({
    providedIn: 'root'
})
export class ItemService{
    private url = 'http://localhost:8080/api/items'
    itemChanged = new EventEmitter<void>() // EventEmitter untuk mengeluarkan event ketika data berubah

    constructor(private http: HttpClient){}

    getAllItem(currentPage: number, pageSize: number, filterBy: string, filterValue: string, sortBy : string) : Observable<ItemModel[]>{
        const params = new HttpParams()
            .set('currentPage', currentPage.toString())
            .set('pageSize', pageSize.toString())
            .set('filterBy', filterBy)
            .set('filterValue', filterValue)
            .set('sortBy', sortBy)
        
        return this.http.get<ItemModel[]>(`${this.url}/get-all`, { params })
    }

    getItemById(id:number) : Observable<ItemDetailModel>{
        return this.http.get<ItemDetailModel>(`${this.url}/${id}`)
    }

    addItem(item:String) : Observable<void>{
        const formData = new FormData()
        formData.append('item', JSON.stringify(item))
        
        return this.http.post<void>(`${this.url}/create`, formData)
    }

    editItem(id:number, item:String) : Observable<void>{
        const formData = new FormData()
        formData.append('item', JSON.stringify(item))
        
        return this.http.put<void>(`${this.url}/update/${id}`, formData)
    }

    deleteItem(id:number) : Observable<void> {
        return this.http.delete<void>(`${this.url}/delete/${id}`).pipe(tap(()=>{
            this.itemChanged.emit() // Emit event ketika item dihapus
        }))
    }
}