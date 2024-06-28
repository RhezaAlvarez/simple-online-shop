import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-add-item-form',
  templateUrl: './add-item-form.component.html',
  styleUrl: './add-item-form.component.scss'
})
export class AddItemFormComponent {
  addItemForm : FormGroup

  constructor(
    public modalRef : MatDialogRef<AddItemFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data : any,
    private fb : FormBuilder,
    private itemService : ItemService
  ){
    this.addItemForm = this.fb.group({
      itemName : ["", [Validators.required, Validators.maxLength(100), Validators.pattern(/^[a-zA-Z0-9]+$/)]],
      price : ["", [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      stock : ["", [Validators.required, Validators.pattern(/^[0-9]+$/)]]
    })
  }

  closeModal() : void {
    this.modalRef.close()
  }

  onSubmit() : void {
    if(this.addItemForm.valid){
      const addItem = this.addItemForm.value
      this.itemService.addItem(addItem).subscribe(() => {
        this.modalRef.close(addItem)
      }, error => {
        console.error('Error adding data:', error)
      })
    }
  }
}
