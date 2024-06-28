import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-add-order-form',
  templateUrl: './add-order-form.component.html',
  styleUrl: './add-order-form.component.scss'
})
export class AddOrderFormComponent {
  addOrderForm : FormGroup

  constructor(
    public modalRef : MatDialogRef<AddOrderFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data : any,
    private fb : FormBuilder,
    private orderService : OrderService
  ){
    this.addOrderForm = this.fb.group({
      customerName : ["", [Validators.required, Validators.maxLength(255), Validators.pattern(/^[a-zA-Z ]+$/)]],
      itemName : ["", [Validators.required, Validators.maxLength(255), Validators.pattern(/^[a-zA-Z0-9 ]+$/)]],
      quantity : ["", [Validators.required, Validators.maxLength(255), Validators.pattern(/^[0-9]+$/)]]
    })
  }

  closeModal() : void{
    this.modalRef.close()
  }

  onSubmit() : void {
    if(this.addOrderForm.valid){
      const addOrder = this.addOrderForm.value
      this.orderService.addOrder(addOrder).subscribe(() => {
        this.modalRef.close()
      }, error => {
        console.error('Error adding data:', error)
      })
    }
  }
}
