import { RouterModule, Routes } from "@angular/router";
import { CustomerComponent } from "./pages/customer/customer.component";
import { ItemComponent } from "./pages/item/item.component";
import { OrderComponent } from "./pages/order/order.component";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { CustomerDetailComponent } from "./pages/customer-detail/customer-detail.component";
import { ItemDetailComponent } from "./pages/item-detail/item-detail.component";
import { OrderDetailComponent } from "./pages/order-detail/order-detail.component";
import { CommonModule } from "@angular/common";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { EditFormComponent } from "./components/edit-form/edit-form.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormField, MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSortModule } from "@angular/material/sort";
import { MatSelectModule } from "@angular/material/select";
import { AddCustomerFormComponent } from "./components/add-customer-form/add-customer-form.component";
import { AddItemFormComponent } from "./components/add-item-form/add-item-form.component";
import { EditItemFormComponent } from "./components/edit-item-form/edit-item-form.component";
import { AddOrderFormComponent } from "./components/add-order-form/add-order-form.component";
import { MatIconModule } from "@angular/material/icon";

const routes : 
    Routes = [
        {path:"", component: CustomerComponent},
        {path:"item", component: ItemComponent},
        {path:"order", component: OrderComponent},
        {path:"customer-detail/:id", component: CustomerDetailComponent},
        {path:"item-detail/:id", component: ItemDetailComponent},
        {path:"order-detail/:id", component: OrderDetailComponent}
    ];

@NgModule({
    declarations : [
        AppComponent,
        CustomerComponent,
        ItemComponent,
        OrderComponent,
        CustomerDetailComponent,
        ItemDetailComponent,
        OrderDetailComponent,
        NavbarComponent,
        EditFormComponent,
        AddCustomerFormComponent,
        AddItemFormComponent,
        EditItemFormComponent,
        AddOrderFormComponent
    ],
    imports : [
        BrowserModule,
        RouterModule.forRoot(routes),
        HttpClientModule,
        MatTableModule,
        MatButtonModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        FormsModule,
        ReactiveFormsModule,
        MatPaginatorModule,
        MatSortModule,
        MatSelectModule,
        MatIconModule
    ],
    providers : [
    provideAnimationsAsync('noop')
  ],
    bootstrap : [AppComponent]
})
export class AppModule {}