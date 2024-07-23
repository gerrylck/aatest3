import { Component } from '@angular/core';
import { TransactionService } from '../transaction.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  csvData: any[] = [];
  csvBlob: Blob | null = null;

  constructor(private transactionService: TransactionService) { }

  onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
      console.log('File selected:', this.selectedFile);
    } else {
      this.selectedFile = null;
    }
  }

  onUpload() {
    if (this.selectedFile) {
      console.log('Uploading file:', this.selectedFile);
      this.transactionService.uploadFile(this.selectedFile).subscribe(
        blob => {
          this.csvBlob = blob;
          console.log('File uploaded, received blob:', blob);
          this.parseCsv(blob);
        },
        error => {
          console.error('Upload failed:', error);
        }
      );
    }
  }

  parseCsv(blob: Blob) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const text = reader.result as string;
      const rows = text.split('\n');
      this.csvData = rows.map(row => row.split(','));
      console.log('CSV parsed:', this.csvData);
    };
    reader.onerror = (e) => {
      console.error('Error reading blob:', e);
    };
    reader.readAsText(blob);
  }

  downloadCsv() {
    if (this.csvBlob) {
      console.log('Downloading CSV');
      this.transactionService.downloadCsv(this.csvBlob);
    }
  }
}
