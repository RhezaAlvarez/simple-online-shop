import { Component } from '@angular/core';
import { OrderDetailModel } from '../../schemas/order-detail-model';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.scss'
})
export class OrderDetailComponent {
  orderDetail : OrderDetailModel | undefined

  constructor(
    private route : ActivatedRoute,
    private router: Router,
    private orderService : OrderService,
    private dialog : MatDialog
  ){}

  ngOnInit() : void {
    this.loadOrderDetail()
  }

  loadOrderDetail() : void {
    const id = Number(this.route.snapshot.paramMap.get('id'))
    this.orderService.getOrderById(id).subscribe(data => {
      this.orderDetail = data
    })
  }

  deleteButtonClick(id : number) : void {
    this.orderService.deleteOrder(id).subscribe(() => {
      this.router.navigate(['/order'])
    }, error => {
      console.error('Error deleting order:', error)
    })
  }

  // editButtonClick(data : OrderDetailModel) : void {
  //   const modalRef = this.dialog.open(EditItemFormComponent, {
  //     width : "300px",
  //     data : {...data}
  //   })

  //   modalRef.afterClosed().subscribe(result => {
  //     if (result) {
  //       this.loadOrderDetail();
  //     }
  //   })
  // } 
}
