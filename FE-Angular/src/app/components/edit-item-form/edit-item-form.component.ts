import { Component, Inject } from '@angular/core';
import { EditFormComponent } from '../edit-form/edit-form.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-edit-item-form',
  templateUrl: './edit-item-form.component.html',
  styleUrl: './edit-item-form.component.scss'
})
export class EditItemFormComponent {
  editItemForm : FormGroup

  constructor(
    public modalRef: MatDialogRef<EditItemFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private itemService: ItemService
  ){
    this.editItemForm = this.fb.group({
      price : [data.price, Validators.required],
      stock : [data.stock, Validators.required]
    })
  }

  closeModal() : void {
    this.modalRef.close()
  }

  onSubmit() : void{
    if (this.editItemForm.valid) {
      const updatedItem = this.editItemForm.value;
      this.itemService.editItem(this.data.itemId, updatedItem).subscribe(() => {
        this.modalRef.close(updatedItem);
      }, error => {
        console.error('Error updating item:', error);
      });
    }
  }
}
