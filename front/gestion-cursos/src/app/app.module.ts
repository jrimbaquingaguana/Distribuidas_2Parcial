import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; // Para ngModel
import { RouterModule, Routes } from '@angular/router'; // Para routerLink
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { UsuarioListComponent } from './components/usuario-list/usuario-list.component';
import { UsuarioDetailComponent } from './components/usuario-detail/usuario-detail.component';
import { CursoListComponent } from './components/curso-list/curso-list.component';
import { CursoDetailComponent } from './components/curso-detail/curso-detail.component';
import { routes } from './app.routes'; // Asegúrate de que esta importación sea correcta

@NgModule({
  declarations: [
    UsuarioListComponent,
    UsuarioDetailComponent,
    CursoListComponent,
    CursoDetailComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule, // Asegúrate de que FormsModule esté aquí
    RouterModule.forRoot(routes), // Asegúrate de que RouterModule esté aquí
    BrowserAnimationsModule
  ],
  providers: [],
 
})
export class AppModule { }
