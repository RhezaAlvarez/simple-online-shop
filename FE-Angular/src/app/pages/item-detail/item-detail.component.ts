import { Component } from '@angular/core';
import { ItemDetailModel } from '../../schemas/item-detail-model';
import { ActivatedRoute, Router } from '@angular/router';
import { ItemService } from '../../services/item.service';
import { MatDialog } from '@angular/material/dialog';
import { EditItemFormComponent } from '../../components/edit-item-form/edit-item-form.component';

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrl: './item-detail.component.scss'
})
export class ItemDetailComponent {
  itemDetail : ItemDetailModel | undefined

  constructor(
    private route : ActivatedRoute,
    private router: Router,
    private itemService : ItemService,
    private dialog : MatDialog
  ){}

  ngOnInit() : void {
    this.loadItemDetail()
  }

  loadItemDetail() : void {
    const id = Number(this.route.snapshot.paramMap.get('id'))
    this.itemService.getItemById(id).subscribe(data => {
      this.itemDetail = data
    })
  }

  deleteButtonClick(id : number) : void {
    this.itemService.deleteItem(id).subscribe(() => {
      this.router.navigate(['/item'])
    }, error => {
      console.error('Error deleting item:', error)
    })
  }

  editButtonClick(data : ItemDetailModel) : void {
    const modalRef = this.dialog.open(EditItemFormComponent, {
      width : "300px",
      data : {...data}
    })

    modalRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadItemDetail();
      }
    })
  } 
}
