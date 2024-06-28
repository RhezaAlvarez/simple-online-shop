import { Component, OnInit } from '@angular/core';
import { CustomerDetailModel } from './customer-detail-model';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { MatDialog } from '@angular/material/dialog';
import { EditFormComponent } from '../../components/edit-form/edit-form.component';

@Component({
  selector: 'app-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.scss'
})
export class CustomerDetailComponent implements OnInit {
  customerDetail : CustomerDetailModel | undefined

  constructor(
    private route : ActivatedRoute,
    private router: Router,
    private customerService : CustomerService,
    private dialog : MatDialog
  ){}

  ngOnInit() : void {
    this.loadCustomerDetail()
  }

  loadCustomerDetail() : void {
    const id = Number(this.route.snapshot.paramMap.get('id'))
    this.customerService.getCustomerById(id).subscribe(data => {
      this.customerDetail = data
    })
  }

  deleteButtonClick(id : number) : void {
    this.customerService.deleteCustomer(id).subscribe(() => {
      this.router.navigate(['/'])
    }, error => {
      console.error('Error deleting customer:', error)
    })
  }

  editButtonClick(data : CustomerDetailModel) : void {
    const modalRef = this.dialog.open(EditFormComponent, {
      width : "300px",
      data : {...data}
    })

    modalRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCustomerDetail();
      }
    })
  } 
}
