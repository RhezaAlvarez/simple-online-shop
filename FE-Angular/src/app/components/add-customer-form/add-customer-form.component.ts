import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-add-customer-form',
  templateUrl: './add-customer-form.component.html',
  styleUrl: './add-customer-form.component.scss'
})
export class AddCustomerFormComponent {
  addCustomerForm : FormGroup
  pic! : File

  constructor(
    public modalRef : MatDialogRef<AddCustomerFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data : any,
    private fb : FormBuilder,
    private customerService : CustomerService
  ){
    this.addCustomerForm = this.fb.group({
      customerName : ["", [Validators.required, Validators.maxLength(100), Validators.pattern(/^[a-zA-Z0-9 ]+$/)]],
      customerAddress : ["", [Validators.required, Validators.maxLength(100), Validators.pattern(/^[a-zA-Z0-9 ]+$/)]],
      customerPhone : ["", [Validators.required, Validators.maxLength(100), Validators.pattern(/^[0-9]+$/)]],
      pic : [null]
    })
  }

  onPicChange(event : any) : void {
    if(event.target.files.length > 0){
      this.pic = event.target.files[0]
    }
  }

  closeModal() : void {
    this.modalRef.close()
  }

  onSubmit() : void {
    if(this.addCustomerForm.valid){
      const addCustomer = this.addCustomerForm.value
      this.customerService.addCustomer(addCustomer, this.pic).subscribe(() => {
        this.modalRef.close(addCustomer)
      }, error => {
        console.error('Error adding data:', error)
      })
    }
  }
}
