import { Component, ViewChild } from '@angular/core';
import { OrderModel } from './order-model';
import { Subscription } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';
import { OrderService } from '../../services/order.service';
import { MatSort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AddOrderFormComponent } from '../../components/add-order-form/add-order-form.component';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {
  displayedColumns: string[] = ['ID', 'Order Code', 'Order Date', 'Customer Name', 'Item Name', 'Quantity', 'Total Price', 'Details'];
  orders: OrderModel[] = []
  private orderChangedSubscription: Subscription | undefined
  totalOrders! : number
  currentPage = 0
  pageSize = 10 
  filterBy : string = "itemName"
  filterValue : string = ""
  sortBy : string = "Order Date desc"

  @ViewChild(MatPaginator) paginator!: MatPaginator
  @ViewChild(MatSort) sort! : MatSort

  constructor(private orderService : OrderService, private router: Router, private dialog : MatDialog){ }

  ngOnInit() : void {
    this.loadOrders()
    this.orderChangedSubscription = this.orderService.orderChanged.subscribe(() => {
      this.loadOrders(); // Muat ulang data item ketika ada perubahan
    })
  }

  loadOrders(): void {
    this.orderService.getAllOrder(this.currentPage, this.pageSize, this.filterBy, this.filterValue, this.sortBy).subscribe(data => {
      this.orders = data
      const orderTemp = this.orders.find(order => order.totalRecord > 0)
      this.totalOrders = orderTemp? orderTemp.totalRecord : 0
      this.paginator.length = this.totalOrders;
    })
  }

  addButtonClick() : void {
    const modalRef = this.dialog.open(AddOrderFormComponent, {
      width : "300px",
    })

    modalRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadOrders();
      }
    })
  } 

  detailButtonClick(id : number) : void {
    this.router.navigate(['/order-detail', id])
  }

  ngOnDestroy(): void {
    if (this.orderChangedSubscription) {
      this.orderChangedSubscription.unsubscribe();
    }
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadOrders();
  }

  applyFilter() {
    this.currentPage = 0
    this.loadOrders()
  }

  onSortChange(sortBy : any) : void {
    this.sortBy = `${sortBy.active} ${sortBy.direction}`
    if(sortBy.direction != ""){
      this.loadOrders()
    }
  }

  downloadReportButtonClick() {
    this.orderService.downloadReportOrder(this.filterBy, this.filterValue, this.sortBy).subscribe((response : any) => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ordersReport.pdf';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    })
  }
}
