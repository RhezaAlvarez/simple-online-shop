import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-edit-form',
  templateUrl: './edit-form.component.html',
  styleUrl: './edit-form.component.scss'
})
export class EditFormComponent {
  editCustomerForm : FormGroup
  pic!: File; 

  constructor(
    public modalRef: MatDialogRef<EditFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private customerService: CustomerService
  ){
    this.editCustomerForm = this.fb.group({
      customerName : [data.customerName, Validators.required],
      customerAddress : [data.customerAddress, Validators.required],
      customerPhone : [data.customerPhone, Validators.required],
      pic : [null]
    })
  }

  onPicChange(event: any): void {
    if (event.target.files.length > 0) {
      this.pic = event.target.files[0];
    }
  }

  closeModal() : void {
    this.modalRef.close()
  }

  onSubmit() : void{
    if (this.editCustomerForm.valid) {
      const updatedCustomer = this.editCustomerForm.value;
      this.customerService.editCustomer(this.data.customerId, updatedCustomer, this.pic).subscribe(() => {
        this.modalRef.close(updatedCustomer);
      }, error => {
        console.error('Error updating customer:', error);
      });
    }
  }
}
