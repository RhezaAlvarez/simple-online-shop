import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CustomerModel } from './customer-model';
import { CustomerService } from '../../services/customer.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { AddCustomerFormComponent } from '../../components/add-customer-form/add-customer-form.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent implements OnInit, OnDestroy{
  displayedColumns: string[] = ['ID', 'Name', 'Address', 'Code', 'Is Active', 'Last Order Date', 'Details'];
  customers: CustomerModel[] = []
  private customerChangedSubscription: Subscription | undefined
  totalItems! : number
  currentPage = 0
  pageSize = 10
  filterBy : string = "customerName"
  filterValue : string = ""
  sortBy : string = "Name asc"

  @ViewChild(MatPaginator) paginator!: MatPaginator
  @ViewChild(MatSort) sort! : MatSort

  constructor(private customerService : CustomerService, private router: Router, private dialog : MatDialog){ }

  ngOnInit() : void {
    this.loadCustomers()
    this.customerChangedSubscription = this.customerService.customerChanged.subscribe(() => {
      this.loadCustomers(); // Muat ulang data customer ketika ada perubahan
    })
  }

  loadCustomers(): void {
    this.customerService.getAllCustomer(this.currentPage, this.pageSize, this.filterBy, this.filterValue, this.sortBy).subscribe(data => {
      this.customers = data
      const custTemp = this.customers.find(customer => customer.totalRecord > 0)
      this.totalItems = custTemp? custTemp.totalRecord : 0
      this.paginator.length = this.totalItems;
    })
  }

  addButtonClick() : void {
    const modalRef = this.dialog.open(AddCustomerFormComponent, {
      width : "300px",
    })

    // modalRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     this.loadCustomerDetail();
    //   }
    // })
  } 

  detailButtonClick(id : number) : void {
    this.router.navigate(['/customer-detail', id])
  }

  ngOnDestroy(): void {
    if (this.customerChangedSubscription) {
      this.customerChangedSubscription.unsubscribe();
    }
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCustomers();
  }

  applyFilter() {
    this.currentPage = 0
    this.loadCustomers()
  }

  onSortChange(sortBy : any) : void {
    this.sortBy = `${sortBy.active} ${sortBy.direction}`
    if(sortBy.direction != ""){
      this.loadCustomers()
    }
  }
}
