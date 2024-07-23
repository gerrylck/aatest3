import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'http://localhost:8080/api/transactions';

  constructor(private http: HttpClient) { }

  uploadFile(file: File): Observable<Blob> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post(`${this.apiUrl}/process`, formData, {
      responseType: 'blob'
    });
  }

  downloadCsv(blob: Blob) {
    const csvFile = new Blob([blob], { type: 'text/csv' });
    saveAs(csvFile, 'Output.csv');
  }
}
