import { Component, ViewChild } from '@angular/core';
import { ItemModel } from '../../schemas/item-model';
import { ItemService } from '../../services/item.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { AddItemFormComponent } from '../../components/add-item-form/add-item-form.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrl: './item.component.scss'
})
export class ItemComponent {
  displayedColumns: string[] = ['ID', 'Name', 'Code', 'Stock', 'Price', 'Is Available', 'Last Re Stock', 'Details'];
  items: ItemModel[] = []
  private itemChangedSubscription: Subscription | undefined
  totalItems! : number
  currentPage = 0
  pageSize = 10
  filterBy : string = "itemName"
  filterValue : string = ""
  sortBy : string = "Name asc"

  @ViewChild(MatPaginator) paginator!: MatPaginator
  @ViewChild(MatSort) sort! : MatSort

  constructor(private itemService : ItemService, private router: Router, private dialog : MatDialog){ }

  ngOnInit() : void {
    this.loadItems()
    this.itemChangedSubscription = this.itemService.itemChanged.subscribe(() => {
      this.loadItems(); // Muat ulang data item ketika ada perubahan
    })
  }

  loadItems(): void {
    this.itemService.getAllItem(this.currentPage, this.pageSize, this.filterBy, this.filterValue, this.sortBy).subscribe(data => {
      this.items = data
      const itemTemp = this.items.find(item => item.totalRecord > 0)
      this.totalItems = itemTemp? itemTemp.totalRecord : 0
      this.paginator.length = this.totalItems;
    })
  }

  addButtonClick() : void {
    const modalRef = this.dialog.open(AddItemFormComponent, {
      width : "300px",
    })

    // modalRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     this.loadCustomerDetail();
    //   }
    // })
  } 

  detailButtonClick(id : number) : void {
    this.router.navigate(['/item-detail', id])
  }

  ngOnDestroy(): void {
    if (this.itemChangedSubscription) {
      this.itemChangedSubscription.unsubscribe();
    }
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadItems();
  }

  applyFilter() {
    this.currentPage = 0
    this.loadItems()
  }

  onSortChange(sortBy : any) : void {
    this.sortBy = `${sortBy.active} ${sortBy.direction}`
    if(sortBy.direction != ""){
      this.loadItems()
    }
  }
}
